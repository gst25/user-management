package dev.rahul.usermanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {

    private String userId;
    private String token;
}
