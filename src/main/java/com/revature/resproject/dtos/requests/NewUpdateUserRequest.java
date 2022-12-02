package com.revature.resproject.dtos.requests;

public class NewUpdateUserRequest {
    private String username;

    public NewUpdateUserRequest() {
        super();
    }

    public NewUpdateUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "NewUpdateUserRequest{" +
                "Username='" + username + '\'' +
                '}';
    }
}
