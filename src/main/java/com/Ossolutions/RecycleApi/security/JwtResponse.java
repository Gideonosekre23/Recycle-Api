package com.Ossolutions.RecycleApi.security;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private final String token;
    private final String username;
    private final String fullName;
    private final Integer points;

    public JwtResponse(String token, String username, String fullName, Integer points) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.points = points;
    }

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Integer getPoints() {
        return this.points;
    }
}
