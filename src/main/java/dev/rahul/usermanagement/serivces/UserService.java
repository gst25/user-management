package dev.rahul.usermanagement.serivces;

import dev.rahul.usermanagement.dtos.UserDto;
import dev.rahul.usermanagement.models.Role;
import dev.rahul.usermanagement.models.User;
import dev.rahul.usermanagement.repositories.RoleRepository;
import dev.rahul.usermanagement.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserDto::from).orElse(null);
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);
        if(optionalUser.isEmpty()){
           return null;
        }
        User user = optionalUser.get();
        user.setRoles(Set.copyOf(roles));
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }
}
