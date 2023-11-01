package dev.rahul.usermanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "Session")
public class Session extends BaseModal{
    
    private String token;

    private Date expiryDate;

    @ManyToOne
    private User users;

    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
