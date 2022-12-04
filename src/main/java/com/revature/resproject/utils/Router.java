package com.revature.resproject.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.resproject.daos.ReimbursementDAO;
import com.revature.resproject.daos.UserDAO;
import com.revature.resproject.handlers.AuthHandler;
import com.revature.resproject.handlers.ReimbursementHandler;
import com.revature.resproject.handlers.UpdateHandler;
import com.revature.resproject.handlers.UserHandler;
import com.revature.resproject.services.ReimbursementService;
import com.revature.resproject.services.TokenService;
import com.revature.resproject.services.UserService;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {
    public static void router(Javalin app) {
        ObjectMapper mapper = new ObjectMapper();
        JwtConfig jwtConfig = new JwtConfig();
        TokenService tokenService = new TokenService(jwtConfig);

        /* User */
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        UserHandler userHandler = new UserHandler(userService, tokenService, mapper);
        UpdateHandler updateHandler = new UpdateHandler(userService, tokenService, mapper);

        /* auth */
        AuthHandler authHandler = new AuthHandler(userService, tokenService, mapper);
        /* tickets */
        ReimbursementDAO reimbDAO = new ReimbursementDAO();
        ReimbursementService reimbService = new ReimbursementService(reimbDAO);
        ReimbursementHandler reimbursementHandler = new ReimbursementHandler(reimbService, tokenService, mapper);
        /* handler groups */
        /* routes -> handler -> service -> dao */
        app.routes(() -> {
            /* users */
            path("/users", () -> {
                get(userHandler::getAllUsers);
                post(c -> userHandler.signup(c));
                get("/name", userHandler::getAllUsersByUsername);
                post("/name", updateHandler::upgradeUser);
                post("/permission", updateHandler::activateUser);

            });
            /* auth */
            path("/auth", () -> {
                post(authHandler::authenticateUser);
            });
            /* tickets */
            path("/tickets", () -> {
                get(reimbursementHandler::getAllTickets);
                post(reimbursementHandler::register);
                get("/status", reimbursementHandler::getAllTicketsByStatus);
                post("/status", reimbursementHandler::processTicket);
                post("/change", reimbursementHandler::updateTicket);
        });
      });
    }
}
