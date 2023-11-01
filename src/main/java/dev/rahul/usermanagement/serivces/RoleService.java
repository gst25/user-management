package dev.rahul.usermanagement.serivces;

import dev.rahul.usermanagement.models.Role;
import dev.rahul.usermanagement.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role createRole(String role) {
        Role saveRole = new Role();
        saveRole.setRole(role);
        return roleRepository.save(saveRole);
    }
}
