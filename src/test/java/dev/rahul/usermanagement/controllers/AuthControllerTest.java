package dev.rahul.usermanagement.controllers;

import dev.rahul.usermanagement.dtos.LoginRequestDto;
import dev.rahul.usermanagement.dtos.UserDto;
import dev.rahul.usermanagement.models.Role;
import dev.rahul.usermanagement.serivces.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;


    @Test
    void login() {
        UserDto userDto = new UserDto();
        userDto.setEmail("abc");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role());
        userDto.setRoles(roles);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, HttpStatus.OK);
        when(authService.login(any(String.class), any(String.class))).thenReturn(responseEntity);
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("abc");
        loginRequestDto.setPassword("abc");
        ResponseEntity<UserDto> res = authController.login(loginRequestDto);
        assertThat(res.getBody().getEmail()).isEqualTo("abc");
    }

    @Test
    void logOut() {
    }

    @Test
    void signUp() {
    }

    @Test
    void validateToken() {
    }
}