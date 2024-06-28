package ru.ry0.hclogin.models;

import java.io.Serializable;

public class AuthData implements Serializable {
    private String session;
    private String token;
    private User user;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
