package dev.rahul.usermanagement.serivces;

import dev.rahul.usermanagement.dtos.UserDto;
import dev.rahul.usermanagement.models.Role;
import dev.rahul.usermanagement.models.Session;
import dev.rahul.usermanagement.models.SessionStatus;
import dev.rahul.usermanagement.models.User;
import dev.rahul.usermanagement.repositories.SessionRepository;
import dev.rahul.usermanagement.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDto> login(String password, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Session> userSessions = sessionRepository.findByUserId(user.getId());
            long activeSessions = userSessions.stream().filter((session) -> session.getExpiryDate().getTime()>new Date().getTime()).count();
            if (bCryptPasswordEncoder.matches(password, user.getPassword()) && activeSessions<2) {
                HashMap<String, Object> mapContent = new HashMap<>();
                mapContent.put("emails", user.getEmail());
                mapContent.put("roles", user.getRoles());
                mapContent.put("createdAt", new Date());
                mapContent.put("expirationAt", new Date(new Date().getTime() + 2 * 60 * 60 * 1000));
                MacAlgorithm alg = Jwts.SIG.HS512; //or HS384 or HS256
                SecretKey key = alg.key().build();
                String jwsToken = Jwts.builder().claims(mapContent).signWith(key, alg).compact();
                Session session = new Session();
                session.setSessionStatus(SessionStatus.ACTIVE);
                session.setExpiryDate(new Date());
                session.setToken(jwsToken);
                session.setUser(user);
                session.setExpiryDate(new Date(new Date().getTime() + 2 * 60 * 60 * 1000));
                sessionRepository.save(session);
                UserDto userDto = new UserDto();
                userDto.setEmail(user.getEmail());
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add(HttpHeaders.SET_COOKIE, "auth-token:" + jwsToken);
                return new ResponseEntity<>(userDto, map, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Void> logOut(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser(token, userId);
        if(optionalSession.isPresent()){
           Session session = optionalSession.get();
           if(session.getSessionStatus().equals(SessionStatus.ACTIVE)){
               session.setSessionStatus(SessionStatus.ENDED);
               sessionRepository.save(session);
           }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserDto> signUp(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(UserDto.from(savedUser), HttpStatus.OK);
    }

    public SessionStatus validateToken(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser(token, userId);
        if (optionalSession.isEmpty()) {
            return SessionStatus.ENDED;
        }

        Session session = optionalSession.get();
        if (session.getSessionStatus().equals(SessionStatus.ENDED)) {
            return SessionStatus.ENDED;
        }

        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);

        String email = (String) claimsJws.getPayload().get("email");
        List<Role> roles = (List<Role>) claimsJws.getPayload().get("roles");
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");

        if (createdAt.before(new Date())) {
            return SessionStatus.ENDED;
        }

        return SessionStatus.ACTIVE;
    }
}
