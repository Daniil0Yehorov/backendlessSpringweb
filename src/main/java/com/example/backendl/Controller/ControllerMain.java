package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.example.backendl.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.HashMap;


@Controller
@AllArgsConstructor
public class ControllerMain {
    @Autowired
    HttpSession session;
    @GetMapping("/")
    public String reg(Model model){
        model.addAttribute("title", "reg Page");
        //додавання до бекендлессу таблиці
        HashMap Place=new HashMap();
        Place.put("description", "");
        Place.put("location",  "");
        Place.put("hashtag", "");
        Place.put("created", new Date());
        Place.put("imageUrl", "imageUrl");
        Place.put("ownerId", "id");

        HashMap Friend=new HashMap();
        Place.put("created", new Date());
        Place.put("imageUrl", "imageUrl");
        Place.put("ownerId", "OwnerId");
        Place.put("ownerId", "OwnersFriendId");
       // Place.put("FriendStatus","FriendStatus");
        Backendless.Data.of("Friend").save(Friend);
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
