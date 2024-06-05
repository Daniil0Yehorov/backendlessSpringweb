package com.example.backendl.Controller;

import com.backendless.BackendlessUser;
import com.example.backendl.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@AllArgsConstructor
public class ControllerMain {
    @Autowired
    HttpSession session;
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
    @GetMapping("/MainforUser")
    public String userf(Model model){
        model.addAttribute("title", "User");
        if(session.isPresent()){
        BackendlessUser user=session.getUser();
        model.addAttribute("user",  user);
            return "MainforUser.html";
        }
        else { return "redirect:/";}
       }
}
