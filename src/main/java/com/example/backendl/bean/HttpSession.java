package com.example.backendl.bean;

import com.backendless.BackendlessUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class HttpSession {

    private BackendlessUser user;
    public boolean isPresent() {
        return user != null;
    }
    public void clearUser() {
        user = null;
    }
    public void setUser(BackendlessUser user) {
        this.user = user;
    }
    public BackendlessUser getUser() {
        return user;
    }
}

