package com.quizapp.model;

public class User {
    private String username;
    private int currentScore;

    public User(String username) {
        this.username = username;
        this.currentScore = 0;
    }

    public String getUsername() { return username; }
    public int getCurrentScore() { return currentScore; }
    public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
}