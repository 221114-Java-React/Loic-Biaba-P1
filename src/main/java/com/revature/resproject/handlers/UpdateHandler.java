package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.dtos.requests.NewUpdateUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.List;

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
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            //  logger.info(token);
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

            User updatedUser = null;
            if (userService.isDuplicateUsername(req.getUsername())) {
                List<User> users = userService.getAllUsersByUsername(req.getUsername());
                for (User candidate: users) {
                    if (!candidate.getRole().equals(Role.ADMIN) && !candidate.getRole().equals(Role.MANAGER)) {
                        updatedUser =  userService.upgradeRole(req);
                    } else throw new InvalidUserException("Admin or Manager cannot be upgraded");
                }
             //   updatedUser =  userService.upgradeRole(req);
            } else throw new InvalidUserException("User doesn't exist");
            ctx.json(updatedUser);
            ctx.status(202); // ACCEPTED
        } catch (InvalidUserException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }
    }
    public void activateUser(Context ctx) throws IOException {
        NewUpdateUserRequest req = mapper.readValue(ctx.req.getInputStream(), NewUpdateUserRequest.class);
        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            //  logger.info(token);
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

            User changedUser = null;
            if (userService.isDuplicateUsername(req.getUsername())) {
                List<User> users = userService.getAllUsersByUsername(req.getUsername());
                for (User candidate: users) {
                    if (!candidate.getRole().equals(Role.ADMIN) && !candidate.getRole().equals(Role.MANAGER)) {
                        changedUser =  userService.activateUser(req);
                    } else throw new InvalidUserException("Admin or Manager cannot be activated/deactivated");
                }
                //   updatedUser =  userService.upgradeRole(req);
            } else throw new InvalidUserException("User doesn't exist");

            ctx.json(changedUser);
            ctx.status(202); // ACCEPTED
        } catch (InvalidUserException | InvalidAuthException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }
    }

}
