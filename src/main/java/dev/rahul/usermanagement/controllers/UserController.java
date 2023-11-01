package dev.rahul.usermanagement.controllers;
import dev.rahul.usermanagement.dtos.SetRoleRequestDto;
import dev.rahul.usermanagement.dtos.UserDto;
import dev.rahul.usermanagement.serivces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        System.out.println(System.getenv("ENV_CURR"));
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable("id") Long userId) {
        UserDto userDto = userService.getUserDetails(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDto> setUserRole(@PathVariable("id") Long userId, @RequestBody SetRoleRequestDto setRoleRequestDto){
        UserDto userDto = userService.setUserRoles(userId, setRoleRequestDto.getRoleIds());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
