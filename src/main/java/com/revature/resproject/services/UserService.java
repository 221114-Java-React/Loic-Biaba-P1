package com.revature.resproject.services;

import com.revature.resproject.daos.UserDAO;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.dtos.requests.NewUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.utils.Sequence;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;


import java.util.List;

/* purpose of UserService is to validate and retrieve data from the DAO (DATA ACCESS OBJECT) */
/* Service class is essentially an api */
public class UserService {
    /* dependency injection = when a class is dependent on another class */
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public void saveUser(NewUserRequest req) {
        List<String> usernames = userDAO.findAllUsernames();

        if (!isValidUsername(req.getUsername())) throw new InvalidUserException("Username needs to be 8-20 characters long");
        if (usernames.contains(req.getUsername())) throw new InvalidUserException("Username is already taken :(");
        if (!isValidPassword(req.getPassword1())) throw new InvalidUserException("Password needs to be minimum eight characters, at least one letter and one number");
        if (!req.getPassword1().equals(req.getPassword2())) throw new InvalidUserException("Passwords do not match :(");

        User createdUser = new User(Sequence.nextValue(), req.getUsername(), req.getEmail(), req.getPassword1(), req.getUsername(), req.getSurname(), Boolean.FALSE, Role.DEFAULT);
        System.out.println(createdUser.toString());
        userDAO.save(createdUser);
    }

    /* helper functions */
    private boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
}
