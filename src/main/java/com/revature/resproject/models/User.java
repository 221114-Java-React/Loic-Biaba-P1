package com.revature.resproject.models;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;
    private boolean isActive;
    private Role role;

    public User() {
        super();
    }
    public User(int id, String username, String email, String password, String givenName, String surname, boolean isActive, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.givenName = givenName;
        this.surname = surname;
        this.isActive = isActive;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "Employee Id=" + id +
                ", Username='" + username + '\'' +
                ", Email='" + email + '\'' +
                ", Password='" + password + '\'' +
                ", Given Name='" + givenName + '\'' +
                ", Surname='" + surname + '\'' +
                ", is Employee Active=" + isActive +
                ", Role=" + role +
                '}';
    }
}
