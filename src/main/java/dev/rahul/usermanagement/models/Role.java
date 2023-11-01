package dev.rahul.usermanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role extends BaseModal{

    @Column(unique = true)
    private String role;

}
