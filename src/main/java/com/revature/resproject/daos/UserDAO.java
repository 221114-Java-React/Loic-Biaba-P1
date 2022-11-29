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
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            /* always start with the PrepareStatement */
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (userid, username, email, userpassword, givenname, surname, isActive, roleid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getUsername());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());
            ps.setString(5, obj.getGivenName());
            ps.setString(6, obj.getSurname());
            ps.setBoolean(7, obj.isActive());
            ps.setInt(8, obj.getRole().ordinal());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public List<String> findAllUsernames() {
        List<String> usernames = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (username) from users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String currentUsername = rs.getString("username");
                usernames.add(currentUsername);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }

}