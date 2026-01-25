package com.quizapp.model;

public class HighscoreEntry {
    private String playerName;
    private int score;
    private String date;

    public HighscoreEntry(String playerName, int score, String date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getDate() { return date; }
}