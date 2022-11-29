package com.revature.resproject.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewUserRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.models.User;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
//import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
//import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
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
        //  private final TokenService tokenService;
        private final ObjectMapper mapper;
        private final static Logger logger = LoggerFactory.getLogger(User.class);

        public UserHandler(UserService userService, ObjectMapper mapper) {
            this.userService = userService;
            //  this.tokenService = tokenService;
            this.mapper = mapper;
        }

        public void signup(Context c) throws IOException {
            NewUserRequest req = mapper.readValue(c.req.getInputStream(), NewUserRequest.class);
            try {
                userService.saveUser(req);
                c.status(201); // CREATED
            } catch (InvalidUserException e) {
                c.status(403); // FORBIDDEN
                c.json(e);
            }
        }

    }

