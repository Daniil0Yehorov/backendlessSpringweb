package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.files.FileInfo;
import com.example.backendl.bean.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

}
