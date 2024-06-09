package com.example.backendl.Controller;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.persistence.DataQueryBuilder;
import com.example.backendl.Model.Friend;
import com.example.backendl.bean.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    HttpSession session;

    @GetMapping
    public String getFriends(Model model) {
        if (session.isPresent()) {
            BackendlessUser user = session.getUser();
            String whereClause = "OwnerId = '" + user.getObjectId() + "'";
            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);
            List<Friend> friends = Backendless.Data.of(Friend.class).find(queryBuilder);

            if (friends != null && !friends.isEmpty()) {
                model.addAttribute("friends", friends);
                // Отримання інформації про друзів (користувачів) за їх ObjectId
                List<BackendlessUser> users = new ArrayList<>();
                for (Friend friend : friends) {
                    String friendObjectId = friend.getOwnersFriendId();
                    BackendlessUser friendUser = getUserById(friendObjectId);
                    users.add(friendUser);
                }
                model.addAttribute("users", users);
            }
            //model.addAttribute("userinssys", user);
            return "friends";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/delete/{friendId}")
    public String deleteFriend(@PathVariable("friendId") String friendId) {
        if (session.isPresent()) {
            Backendless.Data.of("Friend").remove(friendId);
        }
        return "redirect:/friends";
    }
    private BackendlessUser getUserById(String objectId) {
        String whereClause = "objectId = '" + objectId + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        List<BackendlessUser> users = Backendless.Data.of(BackendlessUser.class).find(queryBuilder);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
    @PostMapping("/add")
    public String addFriend(@RequestParam String friendEmail) {
        if (session.isPresent()) {

            BackendlessUser currentUser = session.getUser();
            BackendlessUser friendUser = getUserByEmail(friendEmail);

            if (friendUser != null) {
                if (!isFriendAlreadyAdded(currentUser.getObjectId(), friendUser.getObjectId())) {
                    Friend friend = new Friend();
                    friend.setOwnerId(currentUser.getObjectId());
                    friend.setOwnersFriendId(friendUser.getObjectId());
                    friend.setCreated(new Date());
                    friend.setFriendStatus("pending");
                    Backendless.Data.of(Friend.class).save(friend);
                }
            }

            return "redirect:/friends";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/confirm/{friendId}")
    public String confirmFriend(@PathVariable("friendId") String friendId) {
        if (session.isPresent()) {
            Friend friend = Backendless.Data.of(Friend.class).findById(friendId);
            if (friend != null && friend.getFriendStatus().equals("pending")) {
                friend.setFriendStatus("confirmed");
                Backendless.Data.of(Friend.class).save(friend);
            }
        }
        return "redirect:/friends";
    }
//отримання користувача за поштою
    private BackendlessUser getUserByEmail(String email) {
        String whereClause = "email = '" + email + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        List<BackendlessUser> users = Backendless.Data.of(BackendlessUser.class).find(queryBuilder);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
    // додати того ж  друга не можна
    private boolean isFriendAlreadyAdded(String ownerId, String ownersFriendId) {
        String whereClause = "OwnerId = '" + ownerId + "' AND OwnersFriendId = '" + ownersFriendId + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        List<Friend> existingFriends = Backendless.Data.of(Friend.class).find(queryBuilder);
        return !existingFriends.isEmpty();
    }

}
