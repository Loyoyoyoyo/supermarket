package com.lolo.supermarket.service;

import com.lolo.supermarket.entity.User;

import java.util.List;

public interface UserService {
    User getUserByName(String name);

    User getUserByEmail(String email);

    List<String> RoleInfoByUser(String username);

    List<String> PermissionInfoByRole(List<String> roles);

    int signUp(User user);

    int signIn(User user);

    int rePass(User user);

}
