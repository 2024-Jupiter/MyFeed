package com.myfeed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "/main";
    }

//    @GetMapping("/home")
//    public String home() {
//        return "home";
//    }

    @GetMapping("/home2")
    public String home2() {
        return "home2";
    }


    @GetMapping("/view/home")
    public String loginPage() {
        return "/main";
    }


}
