package dev.rahul.usermanagement.dtos;

import dev.rahul.usermanagement.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetRoleRequestDto {
    private List<Long> roleIds;
}
