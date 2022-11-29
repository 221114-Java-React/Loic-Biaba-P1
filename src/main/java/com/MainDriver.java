package com;

import com.revature.resproject.utils.ConnectionFactory;
import io.javalin.Javalin;

import java.sql.SQLException;

public class MainDriver {
    public static void main(String[] args) throws SQLException {
        System.out.println(ConnectionFactory.getInstance().getConnection());
        Javalin app = Javalin.create(c -> {
            c.contextPath = "/ersproject";
        }) .start(8080);
                // .get("/", ctx -> ctx.result("Hello World"))

        //System.out.println("Hello world!");
    }
}