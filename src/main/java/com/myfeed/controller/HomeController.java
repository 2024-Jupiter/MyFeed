package com.myfeed.controller;

import com.myfeed.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        return "redirect:/api/users/test";
    }

}
