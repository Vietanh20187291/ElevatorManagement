package com.project.entity;

import com.project.entity.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private int userId;
    private String username;
    private UserRole role;

    public UserDTO(int userId, String username, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}

