package com.example.Users.controller;

import com.example.Users.entities.Users;
import com.example.Users.services.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @PostMapping("/AddUserDetails")
    public ResponseEntity<Users> addUsersDetails(@RequestBody Users users){
        usersService.addUserDetails(users);
        return new ResponseEntity<Users>(users,HttpStatus.OK);
    }

    @GetMapping("/GetAllUserDetails")
    public ResponseEntity<List<Users>> getAllUserDetails(){
        List<Users> usersList=usersService.getAllUserDetails();
        return new ResponseEntity<List<Users>>(usersList,HttpStatus.OK);
    }

    @GetMapping("/GetUserDetailsByUserName/{userName}")
    public ResponseEntity<Object> getUserDetailsByUserName(@PathVariable("userName") String userName ){
        Optional<Users> users=usersService.getUserDetailsByUserName(userName);
        if(users.isPresent()){
            return new ResponseEntity<Object>(users,HttpStatus.OK);
        }
        return new ResponseEntity<Object>("null",HttpStatus.OK);
    }

    @GetMapping("/GetUserDetailsByUserId/{userId}")
    public ResponseEntity<Object> getUserDetailsByUserId(@PathVariable("userId") String userId ){
        Optional<Users> users=usersService.getUserDetailsByUserId(userId);
        if(users.isPresent()){
            return new ResponseEntity<Object>(users,HttpStatus.OK);
        }
        return new ResponseEntity<Object>("null",HttpStatus.OK);
    }

    @GetMapping("/GetUserMailByUserId/{userId}")
    public ResponseEntity<String> getUserMailByUserId(@PathVariable("userId") String userId ){
        String email = usersService.getUserMailByUserId(userId);
        return new ResponseEntity<String>(email,HttpStatus.OK);
    }
    @GetMapping("/GetUserNameByUserId/{userId}")
    public ResponseEntity<String> getUserNameByUserId(@PathVariable("userId") String userId ){
        String name = usersService.getUserNameByUserId(userId);
        return new ResponseEntity<String>(name,HttpStatus.OK);
    }
}
