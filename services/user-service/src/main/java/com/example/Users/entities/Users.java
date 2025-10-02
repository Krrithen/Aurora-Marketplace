package com.example.Users.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = Users.COLLECTION_NAME4)
public class Users {

    public static final String COLLECTION_NAME4 = "Users";

    @Id
    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userAddress;
    private String userPhoneNo;
}
