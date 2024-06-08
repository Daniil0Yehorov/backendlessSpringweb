package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.files.FileInfo;
import com.backendless.geo.GeoPoint;
import com.example.backendl.bean.HttpSession;
import com.example.backendl.config.BackendlessConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class UserController {
@Autowired
    HttpSession session;
    //удаление файла в корневой папке пользователя
    @RequestMapping(value = "/delete_File", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteFile(@RequestParam String filename){
    String userLogin = (String) session.getUser().getProperty("login");
        System.out.println("/user_directories/" + userLogin + "/"+filename);
    Backendless.Files.remove("/user_directories/" + userLogin + "/"+filename);
    return "redirect:/MainforUser";
}
    @GetMapping("/MainforUser")
    public String userf(Model model){
        model.addAttribute("title", "User");
        if(session.isPresent()){
            BackendlessUser user=session.getUser();
            //передача
            String userLogin = (String) session.getUser().getProperty("login");
            List<FileInfo> fileList =  Backendless.Files.listing("/user_directories/" + userLogin + "/");
            if(!fileList.isEmpty()){model.addAttribute("fileList", fileList);}
            model.addAttribute("user",  user);
            return "MainforUser.html";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        BackendlessUser user = session.getUser();
        String userLogin = (String) session.getUser().getProperty("login");
        String userDirectory = "/user_directories/" + userLogin + "/";

        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        Backendless.Files.upload(tempFile, userDirectory);

        tempFile.delete();

        return "redirect:/MainforUser";
    }

    @PostMapping("/shareFile")
    public String shareFile(@RequestParam String filename, @RequestParam String targetUser) {
        BackendlessUser user=session.getUser();
        try {
            if (targetUser != null) {
                String userFolderName = targetUser.replace("@", "_").replace(".", "_");
                String sharedWithMeFolderPath = "/user_directories/"  +user.getProperty("login")+ "/shared_with_me/"+ userFolderName;
                Backendless.Files.saveFile(sharedWithMeFolderPath,userFolderName+".txt", new byte[0]);
            }
        } catch (BackendlessException e) {
            System.err.println("Error sharing file: " + e.getMessage());
        }
        return "redirect:/MainforUser";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            model.addAttribute("user", user);
            return "profile";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam String name,
                                @RequestParam int age,
                                @RequestParam String gender,
                                @RequestParam String country) {
        BackendlessUser user = session.getUser();
        user.setProperty("name", name);
        user.setProperty("age", age);
        user.setProperty("gender", gender);
        user.setProperty("country", country);
        try {
            Backendless.UserService.update(user);
        } catch (BackendlessException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "redirect:/profile";
    }
    @PostMapping("/updateLocation")
    public String updateLocation(@RequestParam double latitude, @RequestParam double longitude) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            String wktString = "POINT(" + longitude + " " + latitude + ")";
            user.setProperty("myLocation", wktString);

            try {
                Backendless.UserService.update(user);
                System.out.println("Location updated successfully.");
            } catch (BackendlessException e) {
                System.err.println("Error updating location: " + e.getMessage());
            }
        } else {
            System.err.println("Error: User session is not present.");
        }
        return "redirect:/profile";
    }
    // Метод для завантаження та оновлення аватара користувача
    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            try {
                String fileUrl = uploadFileToBackendless(file);

                if (fileUrl != null) {
                    user.setProperty("Avatarsrc", fileUrl);
                    Backendless.UserService.update(user);

                    return "redirect:/profile";
                } else {
                    return "redirect:/errorPage";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/errorPage";
            }
        } else {
            return "redirect:/login";
        }

    }

    // Метод для завантаження файлу на сервер Backendless
    private String uploadFileToBackendless(MultipartFile file) throws IOException {
        try {
            String userLogin = (String) session.getUser().getProperty("login");
            String filename = file.getOriginalFilename();

            String userDirectory = "/user_directories/" + userLogin + "/user_avatars/";

            File tempFile = File.createTempFile("upload", filename);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            try {
                Backendless.Files.upload(tempFile, userDirectory);
            } finally {
                tempFile.delete();
            }
            return userDirectory;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }











}
