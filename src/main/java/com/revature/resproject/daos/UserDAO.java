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
            PreparedStatement ps = con.prepareStatement("INSERT INTO user_t (userid, username, email, userpassword, givenname, surname, isActive, roleid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
        List<User> users = new ArrayList<User>();
        Role task[] = Role.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * from user_t");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User currentUser = new User(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("userpassword"),
                        rs.getString("givenname"), rs.getString("surname"), rs.getBoolean("isactive"), task[rs.getInt("roleid")]);
                users.add(currentUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /* custom methods */

    public List<String> findAllUsernames() {
        List<String> usernames = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (username) from user_t");
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
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;
        Role task[] = Role.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_t WHERE username = ? AND userpassword = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("userpassword"),
                        rs.getString("givenname"), rs.getString("surname"), rs.getBoolean("isactive"), task[rs.getInt("roleid")]);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public List<User> getAllUsersByUsername(String username) {
        List<User> users = new ArrayList<User>();
        Role task[] = Role.values();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_t WHERE username LIKE ?");
            ps.setString(1, username + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("userpassword"),
                        rs.getString("givenname"), rs.getString("surname"), rs.getBoolean("isactive"), task[rs.getInt("roleid")]);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}