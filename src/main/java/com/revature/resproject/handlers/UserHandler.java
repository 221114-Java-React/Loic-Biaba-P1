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
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

    /* purpose of this UserHandler class is to handle http verbs and endpoints */
    /* hierarchy dependency injection -> userhandler -> userservice -> userdao */
    public class UserHandler {
    }

