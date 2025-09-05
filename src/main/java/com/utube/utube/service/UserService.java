package com.utube.utube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utube.utube.entity.Profile;
import com.utube.utube.entity.User;
import com.utube.utube.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUser(String username) {
        return this.userRepo.findByUsername(username).orElse(null);
    }

    public User getUser(Long id) {
        return this.userRepo.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return this.userRepo.save(user);
    }


    public void deleteUser(User user) {
        this.userRepo.delete(user);
    }

}
