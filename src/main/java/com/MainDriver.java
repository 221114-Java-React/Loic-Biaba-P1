package com;

import io.javalin.Javalin;

public class MainDriver {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                        .get("/", ctx -> ctx.result("Hello World"))
                                        .start(7070);
        //System.out.println("Hello world!");
    }
}