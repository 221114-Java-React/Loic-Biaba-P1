package com.revature.resproject.services;

import com.revature.resproject.daos.UserDAO;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.dtos.requests.NewUpdateUserRequest;
import com.revature.resproject.dtos.requests.NewUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.utils.Sequence;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
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
    public User signup(NewUserRequest req) {
        User createdUser = new User(Sequence.nextValue(), req.getUsername(), req.getEmail(), req.getPassword1(), req.getGivenName(), req.getSurname(), Boolean.TRUE, Role.DEFAULT);
       // System.out.println(createdUser.toString());
        userDAO.save(createdUser);

        return createdUser;
    }

    public Principal login(NewLoginRequest req) throws InvalidUserException {
        User validUser = userDAO.getUserByUsernameAndPassword(req.getUsername(), req.getPassword());
        if (validUser == null) throw new InvalidAuthException("Invalid username or password");
        return new Principal(validUser.getId(), validUser.getUsername(), validUser.getRole());
    }
    public User upgradeRole(NewUpdateUserRequest req) throws InvalidUserException {
        User updatedUser = userDAO.updateUserRole(req.getUsername());
        if (updatedUser == null) throw new InvalidUserException("Unable to update role");
       return updatedUser;
       // return new Principal(validUser.getId(), validUser.getUsername(), validUser.getRole());
    }
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    public List<User> getAllUsersByUsername(String username) {
        return userDAO.getAllUsersByUsername(username);
    }
    /* helper functions */
    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }
    public boolean isDuplicateUsername(String username) {
        List<String> usernames = userDAO.findAllUsernames();
        return usernames.contains(username);
    }

    public boolean isDuplicateEmail(String email) {
        List<String> emails = userDAO.findAllEmails();
        return emails.contains(email);
    }
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
    public boolean isSamePassword(String password1, String password2) {
        return password1.equals(password2);
    }

    public User activateUser(NewUpdateUserRequest req) {
        User changedUser = userDAO.changeUserPermission(req.getUsername());
        if (changedUser == null) throw new InvalidUserException("Unable to activate/deactivate user");
        return changedUser;
    }
}
