package com.example.backendl.Controller;
import com.backendless.*;
import com.backendless.exceptions.BackendlessException;
import com.backendless.files.BackendlessFile;
import com.example.backendl.bean.HttpSession;
import com.example.backendl.config.BackendlessConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BotController {
@Autowired
    public BackendlessConfig backendlessConfig;
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
            System.out.println("Начало регистрации пользователя...");
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
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
                System.out.println("Пользователь успешно зарегистрирован.");
                return "Ви зареєстровані.";
            } else {
                System.out.println("Ошибка регистрации пользователя.");
                return "Провалена реєстрація.";
            }
        } catch (BackendlessException e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
            return "Провалена реєстрація: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        backendlessConfig.init();
        try {
           BackendlessUser user=Backendless.UserService.login(email, password);
           session.setUser(user);

            return "redirect:/MainforUser";//not working fix it
        } catch (BackendlessException e) {
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

    @GetMapping("/current-user")
    public String currentUser() {
        backendlessConfig.init();
        BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        if (currentUser != null) {
            return "Поточний користувач: " + currentUser.getProperty("login");
        } else {
            return "Користувач не авторизований.";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        backendlessConfig.init();
        try {
            Backendless.UserService.logout();
            return "Ви вийшли з системи.";
        } catch (BackendlessException e) {
            return "Помилка при виході з системи: " + e.getMessage();
        }
    }
}