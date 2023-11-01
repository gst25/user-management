package dev.rahul.usermanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseModal  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
