package dev.rahul.usermanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "User")
public class User extends BaseModal{

    private String email;

    private String password;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
