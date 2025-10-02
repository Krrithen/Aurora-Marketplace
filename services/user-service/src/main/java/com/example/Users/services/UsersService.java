package com.example.Users.services;

import com.example.Users.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    public Users addUserDetails(Users users);
    public List<Users> getAllUserDetails();
    public Optional<Users> getUserDetailsByUserName(String userName);
    public Optional<Users> getUserDetailsByUserId(String userId);
    public String getUserMailByUserId(String userId);
    public String getUserNameByUserId(String userId);


}
