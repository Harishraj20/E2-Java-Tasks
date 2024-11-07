package com.task.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.Model.User;
import com.task.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public String addUsers(User user) {
        String message = repo.addUserInfo(user);

        return message;
    }

    public List<User> fetchDetails() {

        return repo.getUsers();

    }

    public String verifyLogin(User user) {

        return repo.validateLogin(user);
    }

}
