package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.models.User;
import com.revature.resproject.services.UserService;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
import com.revature.resproject.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthHandler {
    private final UserService userService;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public AuthHandler(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }
    public void authenticateUser(Context ctx) throws IOException {
        NewLoginRequest req = mapper.readValue(ctx.req.getInputStream(), NewLoginRequest.class);
        logger.info("Attempting to login...");
        try {
            userService.login(req);
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
}
