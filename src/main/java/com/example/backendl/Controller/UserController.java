package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.files.FileInfo;
import com.example.backendl.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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






}
