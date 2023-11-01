package dev.rahul.usermanagement.serivces;

import dev.rahul.usermanagement.dtos.UserDto;
import dev.rahul.usermanagement.models.Session;
import dev.rahul.usermanagement.models.SessionStatus;
import dev.rahul.usermanagement.models.User;
import dev.rahul.usermanagement.repositories.SessionRepository;
import dev.rahul.usermanagement.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
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
            List<Session> userSessions = sessionRepository.findByUsersId(user.getId());
            long activeSessions = userSessions.stream().filter((session) -> session.getExpiryDate().getTime()>new Date().getTime()).count();
            if (bCryptPasswordEncoder.matches(password, user.getPassword()) && activeSessions<2) {
                String token = RandomStringUtils.randomAlphanumeric(30);
                Session session = new Session();
                session.setSessionStatus(SessionStatus.ACTIVE);
                session.setExpiryDate(new Date());
                session.setToken(token);
                session.setUsers(user);
                session.setExpiryDate(new Date(new Date().getTime() + 2 * 60 * 60 * 1000));
                sessionRepository.save(session);
                UserDto userDto = new UserDto();
                userDto.setEmail(user.getEmail());
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);
                return new ResponseEntity<>(userDto, map, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Void> logOut(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndId(token, userId);
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

    public ResponseEntity<UserDto> validateToken(String token, String userId) {
        return null;
    }
}
