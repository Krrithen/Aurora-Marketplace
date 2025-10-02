package com.example.Users.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UsersDTO {

    @Id
    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userAddress;
    private String userPhoneNo;
}
