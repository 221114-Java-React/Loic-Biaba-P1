package com.revature.resproject.handlers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/* purpose of this UserHandler class is to handle http verbs and endpoints */
    /* hierarchy dependency injection -> userhandler -> userservice -> userdao */
    public class UserHandler {
        private final UserService userService;
         private final TokenService tokenService;
        private final ObjectMapper mapper;
        private final static Logger logger = LoggerFactory.getLogger(User.class);

        public UserHandler(UserService userService, TokenService tokenService, ObjectMapper mapper) {
            this.userService = userService;
            this.tokenService = tokenService;
            this.mapper = mapper;
        }
    public void signup(Context ctx) throws IOException {



        NewUserRequest req = mapper.readValue(ctx.req.getInputStream(), NewUserRequest.class);

            try {
                String token = ctx.req.getHeader("Authorization");
                if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
                //  logger.info(token);
                Principal principal = tokenService.extractRequesterDetails(token);
                if (principal == null) throw new InvalidAuthException("Invalid token");
                if (!principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

                User createdUser = null;

                if (userService.isValidUsername(req.getUsername())) {
                    if (!userService.isDuplicateUsername(req.getUsername())) {
                        if (!userService.isDuplicateEmail(req.getEmail())) {
                             if (userService.isValidPassword(req.getPassword1())) {
                                  if (userService.isSamePassword(req.getPassword1(), req.getPassword2())) {
                                createdUser = userService.signup(req);
                                    } else throw new InvalidUserException("Passwords does not match");
                                } else throw new InvalidUserException("Password needs to be minimum eight characters, at least one letter and one number");
                        } else throw new InvalidUserException("Email is already taken");
                    } else throw new InvalidUserException("Username is already taken");
                } else throw new InvalidUserException("Username needs to be 8 - 20 characters long");

                ctx.status(201); // CREATED
                ctx.json(createdUser);

            } catch (InvalidUserException | InvalidAuthException e) {
                ctx.status(403); // FORBIDDEN
                ctx.json(e);

            }
        }


    public void getAllUsers(Context ctx) {
       try {
           String token = ctx.req.getHeader("Authorization");
           if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
         //  logger.info(token);
           Principal principal = tokenService.extractRequesterDetails(token);
           if (principal == null) throw new InvalidAuthException("Invalid token");
           if (!principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

          // logger.info("Principal: " + principal.toString());
          // logger.info("Principal: " + tokenService.extractDetails(token));
           List<User> users = userService.getAllUsers();
           ctx.json(users);
       } catch (InvalidAuthException e) {
           ctx.status(401);
           ctx.json(e);
       } catch (NullPointerException e) {
           e.printStackTrace();
           ctx.json(e);
       }
    }

    public void getAllUsersByUsername(Context ctx) {

        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            //  logger.info(token);
            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals(Role.ADMIN)) throw new InvalidAuthException("You are not authorized to do this");

            String username = ctx.req.getParameter("username");
            //logger.info("Searched username = " + username);
            List<User> users = userService.getAllUsersByUsername(username);
            if (users.isEmpty()) {throw new InvalidUserException("User not found");}

            ctx.json(users);
        } catch (InvalidAuthException | InvalidUserException e) {
            ctx.status(401);
            ctx.json(e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            ctx.json(e);
        }
    }
}

