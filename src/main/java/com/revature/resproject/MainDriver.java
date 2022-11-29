package com.revature.resproject;

import com.revature.resproject.utils.ConnectionFactory;
import com.revature.resproject.utils.Router;
import io.javalin.Javalin;

import java.sql.SQLException;

public class MainDriver {
    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin.create(c -> {
           c.contextPath = "/ersproject";}
        ).start(8080);
                // .get("/", ctx -> ctx.result("Hello World"))
        Router.router(app);
        //System.out.println("Hello world!");
    }
}