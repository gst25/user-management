package dev.rahul.usermanagement.controllers;

import dev.rahul.usermanagement.dtos.CreateRoleRequestDto;
import dev.rahul.usermanagement.models.Role;
import dev.rahul.usermanagement.serivces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDto createRoleRequestDto ) {
        Role role = roleService.createRole(createRoleRequestDto.getRole());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
