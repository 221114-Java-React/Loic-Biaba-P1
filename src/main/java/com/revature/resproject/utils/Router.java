package com.revature.resproject.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.daos.UserDAO;
import com.revature.resproject.handlers.AuthHandler;
import com.revature.resproject.handlers.UserHandler;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {
    public static void router(Javalin app) {
        ObjectMapper mapper = new ObjectMapper();
        //JwtConfig jwtConfig = new JwtConfig();
        //TokenService tokenService = new TokenService(jwtConfig);

        /* User */
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        UserHandler userHandler = new UserHandler(userService, mapper);

        /* auth */
        AuthHandler authHandler = new AuthHandler(userService, mapper);

        /* handler groups */
        /* routes -> handler -> service -> dao */
        app.routes(() -> {
            /* user */
            path("/users", () -> {
               // get(c -> c.result("Hey!!!"));
                post(c -> userHandler.signup(c));
            });
            /* auth */
            path("/auth", () -> {
                post(authHandler::authenticateUser);
            });


        });
    }
}
