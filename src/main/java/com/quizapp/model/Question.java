package com.quizapp.model;

public class Question {
    private String text;
    private String[] options; // Array of 4 answers
    private int correctIndex; // 0, 1, 2, or 3

    // Constructor needed for GSON later
    public Question(String text, String[] options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    // Getters
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}