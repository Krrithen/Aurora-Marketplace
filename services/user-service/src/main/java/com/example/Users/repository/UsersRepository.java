package com.example.Users.repository;

import com.example.Users.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<Users,String > {
    public Optional<Users> findByUserName(String userName);

}
