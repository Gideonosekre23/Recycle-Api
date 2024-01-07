package com.Ossolutions.RecycleApi.Model;

public class UserLeaderboardInfo {
    private String username;
    private String fullName;
    private int points;
    private int position;

    public UserLeaderboardInfo(String username, String fullName, int points, int position) {
        this.username = username;
        this.fullName = fullName;
        this.points = points;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
