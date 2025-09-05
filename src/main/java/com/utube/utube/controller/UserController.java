package com.utube.utube.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("register.html");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login.html");
    }

    @GetMapping("/getPremium")
    public ModelAndView resultPage() {
        return new ModelAndView("premium-page.html");
    }

    @PostMapping("/authenticate")
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

}
