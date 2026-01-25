package com.quizapp.model;

public class HighscoreEntry {
    private String playerName;
    private int score;
    private String date;

    // Constructor
    public HighscoreEntry(String playerName, int score, String date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getDate() { return date; }
}