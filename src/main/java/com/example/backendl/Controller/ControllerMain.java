package com.example.backendl.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ControllerMain {
    @GetMapping("/")
    public String reg(Model model){
        model.addAttribute("title", "reg Page");
        return "index.html";}
    @GetMapping("/loginpage")
    public String log(Model model){
        model.addAttribute("title", "login Page");
        return "login.html";}
    @GetMapping("/nopassword")
    public String forgottenpass(Model model){
        model.addAttribute("title", "forgotPage");
        return "forgot-password.html";}
}
