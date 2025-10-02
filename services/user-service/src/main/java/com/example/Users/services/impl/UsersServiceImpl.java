package com.example.Users.services.impl;

import com.example.Users.entities.Users;
import com.example.Users.repository.UsersRepository;
import com.example.Users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public Users addUserDetails(Users users) {
        return usersRepository.save(users);
    }

    @Override
    public List<Users> getAllUserDetails() {
        return usersRepository.findAll();
    }

    @Override
    public Optional<Users> getUserDetailsByUserName(String userName) {
        return usersRepository.findByUserName(userName);
    }

    @Override
    public Optional<Users> getUserDetailsByUserId(String userId) {
        return usersRepository.findById(userId);
    }

    @Override
    public String getUserMailByUserId(String userId) {
        Optional<Users> temp = usersRepository.findById(userId);
        if(temp.isPresent()){
            return usersRepository.findById(userId).get().getUserEmail();
        }
        return "Invalid Email";
    }

    @Override
    public String getUserNameByUserId(String userId) {
        Optional<Users> temp1 = usersRepository.findById(userId);
        if(temp1.isPresent()){
            return usersRepository.findById(userId).get().getUserName();
        }
        return "Invalid Name";
    }
}
