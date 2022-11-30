package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.dtos.requests.NewUpdateUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.User;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;

import java.io.IOException;

public class UpdateHandler {
    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper mapper;

    public UpdateHandler(UserService userService, TokenService tokenService, ObjectMapper mapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.mapper = mapper;
    }
    public void upgradeUser(Context ctx) throws IOException {
        NewUpdateUserRequest req = mapper.readValue(ctx.req.getInputStream(), NewUpdateUserRequest.class);
        try {
            User updatedUser = null;
            if (userService.isDuplicateUsername(req.getUsername())) {
                updatedUser =  userService.upgradeRole(req);
            } else throw new InvalidUserException("User doesn't exist");
            ctx.json(updatedUser);
            ctx.status(202); // ACCEPTED
        } catch (InvalidUserException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }
    }

}
