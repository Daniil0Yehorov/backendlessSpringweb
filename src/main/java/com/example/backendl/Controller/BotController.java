package com.example.backendl.Controller;
import com.backendless.*;
import com.backendless.exceptions.BackendlessException;
import com.backendless.logging.Logger;
import com.example.backendl.bean.HttpSession;
import com.example.backendl.config.BackendlessConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class BotController {
@Autowired
    public BackendlessConfig backendlessConfig;
    private static final Logger logger = Backendless.Logging.getLogger(BotController.class);

    @Autowired
    private  final HttpSession session;
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String name,
                           @RequestParam int age,
                           @RequestParam String gender,
                           @RequestParam String country,
                           @RequestParam String login) {
        backendlessConfig.init();
        try {

            BackendlessUser user = new BackendlessUser();
            user.setEmail(email);
            user.setPassword(password);
            user.setProperty("name", name);
            user.setProperty("age", age);
            user.setProperty("gender", gender);
            user.setProperty("country", country);
            user.setProperty("login", login);

            BackendlessUser registeredUser = Backendless.UserService.register(user);

            if (registeredUser != null) {
                // Створення папки та файлу sharewithme
                String userFolderPath = "/user_directories/" + login + "/shared_with_me/";
                try {
                    Backendless.Files.saveFile(userFolderPath, "test.txt", new byte[0]);
                } catch (BackendlessException e) {
                    System.out.println("error: " + e.getMessage());
                }
                return "redirect:/loginpage";
            } else {
                System.out.println("Erorr");
                return "Провалена реєстрація.";
            }
        } catch (BackendlessException e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
            return "Провалена реєстрація: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        backendlessConfig.init();
        try {
            logger.info("Спроба авторизації користувача з email: " + email+"та password"+password);
           BackendlessUser user=Backendless.UserService.login(email, password);
           session.setUser(user);
            return "redirect:/MainforUser";
        } catch (BackendlessException e) {
            logger.error("Помилка авторизації користувача з email: " + email, e);
            return "Невдачна спроба: " + e.getMessage();
        }
    }
    @PostMapping("/restore-password")
    public String restorePassword(@RequestParam String email) {
        backendlessConfig.init();
        try {
            Backendless.UserService.restorePassword(email);
            return "Інструкції для відновлення паролю відправлені на вашу пошту.";
        } catch (BackendlessException e) {
            return "Помилка при відновленні паролю: " + e.getMessage();
        }
    }
    @GetMapping("/logout")
    public String logout() {
        backendlessConfig.init();
        try {
            Backendless.UserService.logout();
            return "redirect:/";
        } catch (BackendlessException e) {
            return "Помилка при виході з системи: " + e.getMessage();
        }
    }
}