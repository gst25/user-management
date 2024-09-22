package dev.rahul.usermanagement.controllers;

import dev.rahul.usermanagement.dtos.*;
import dev.rahul.usermanagement.models.SessionStatus;
import dev.rahul.usermanagement.serivces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping ("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request){
       return authService.login(request.getPassword(), request.getEmail());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogoutRequestDto request){
         return authService.logOut(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request){
        return authService.signUp(request.getEmail(), request.getPassword());
    }

    @PostMapping("/validate")
    public SessionStatus validateToken(@RequestBody ValidateTokenRequestDto request) {
        return authService.validateToken(request.getToken(),request.getUserId());
    }

}
