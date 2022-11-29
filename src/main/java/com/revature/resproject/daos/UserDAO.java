package com.revature.resproject.daos;

import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* purpose of UserDAO is to return data from the database */
/* DAO = DATA ACCESS OBJECT */

public class UserDAO implements CrudDAO<User> {
    @Override
    public void save(User obj) {

    }

    @Override
    public void delete(User obj) {

    }

    @Override
    public void update(User obj) {

    }

    @Override
    public User findById() {
        return null;
    }

    @Override
    public List<User> findAll() {

        return null;
    }

    /* custom methods */

}