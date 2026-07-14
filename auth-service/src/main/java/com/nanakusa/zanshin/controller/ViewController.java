package com.nanakusa.zanshin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zanshin")
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
