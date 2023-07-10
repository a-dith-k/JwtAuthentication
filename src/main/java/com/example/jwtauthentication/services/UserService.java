package com.example.jwtauthentication.services;

import com.example.jwtauthentication.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private List<User> userList = new ArrayList<>();

    public UserService() {
        userList.add(new User(UUID.randomUUID().toString(), "Hari", "adith@2351"));
        userList.add(new User(UUID.randomUUID().toString(), "Basil", "basil@2351"));
        userList.add(new User(UUID.randomUUID().toString(), "Akarsh", "akarsh@2351"));
        userList.add(new User(UUID.randomUUID().toString(), "Samjith", "samjith@2351"));
    }

    public List<User> getUserList() {
        return userList;
    }
}