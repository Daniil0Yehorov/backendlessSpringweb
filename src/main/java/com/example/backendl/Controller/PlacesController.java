package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.persistence.DataQueryBuilder;
import com.example.backendl.Model.Place;
import com.example.backendl.bean.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/places")
public class PlacesController {
    @Autowired
    HttpSession session;

    @GetMapping
    public String getMyPlaces(Model model) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            String whereClause = "ownerId = '" + user.getObjectId() + "'";
            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);
            List<Place> places = Backendless.Data.of(Place.class).find(queryBuilder);
            model.addAttribute("places", places);
            model.addAttribute("user",user);
            return "myPlaces";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/add")
    public String addPlace(@RequestParam String description,
                           @RequestParam(required = false) Double latitude,
                           @RequestParam(required = false) Double longitude,
                           @RequestParam  String hashtag,
                           @RequestParam String imageUrl) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            HashMap Place=new HashMap();
            Place.put("description", description);
            if(latitude==null && longitude==null){
                Place.put("location", user.getProperty("myLocation"));
            }
            else if(latitude==null){
                Place.put("location", user.getProperty("myLocation"));
            }
            else if(longitude==null){Place.put("location", user.getProperty("myLocation"));}
            else{   Place.put("location", latitude + "," + longitude);}
            Place.put("hashtag", hashtag);
            Place.put("created", new Date());
            Place.put("imageUrl", imageUrl);
            Place.put("ownerId", user.getObjectId());

            Backendless.Data.of("Place").save(Place);
        } else {
            return "redirect:/";
        }
        return "redirect:/places";
    }

    @PostMapping("/delete")
    public String deletePlace(@RequestParam String objectId) {
        if (session.isPresent()) {
            Backendless.Data.of("Place").remove(objectId);
        } else {
            return "redirect:/";
        }
        return "redirect:/places";
    }

    @GetMapping("/find")
    public String findPlace(@RequestParam String query, Model model) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            String whereClause = "ownerId = '" + user.getObjectId() + "' AND description LIKE '%" + query + "%'";
            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);
            List<Place> places = Backendless.Data.of(Place.class).find(queryBuilder);
            model.addAttribute("places", places);
            return "myPlaces";
        } else {
            return "redirect:/";
        }
    }
}
