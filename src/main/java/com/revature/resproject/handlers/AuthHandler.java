package com.revature.resproject.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.dtos.requests.NewLoginRequest;
import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.User;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import com.revature.resproject.utils.custom_exceptions.InvalidAuthException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthHandler {
    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public AuthHandler(UserService userService, TokenService tokenService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }
    public void authenticateUser(Context ctx) throws IOException {
        NewLoginRequest req = mapper.readValue(ctx.req.getInputStream(), NewLoginRequest.class);
       // logger.info("Attempting to login...");
        try {
           Principal principal =  userService.login(req);
           String token = tokenService.generateToken(principal);
            /* set the header with the auth token */
           ctx.res.setHeader("authorization", token);
           ctx.json(principal);
           ctx.status(202); // ACCEPTED
         //   logger.info("Login successful...");
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
}
