package com.quizapp.model;

public class GameLogic {

    /**
     * Calculates points based on correctness and remaining time.
     * Requirement: Method with at least 2 branches for Whitebox Test.
     */
    public int calculateScore(boolean isCorrect, int secondsLeft) {
        int points = 0;

        // Branch 1
        if (isCorrect) {
            points = 10; // Base points

            // Branch 2 (Nested)
            if (secondsLeft > 10) {
                points += 5; // Speed bonus
            } else if (secondsLeft > 0) {
                points += 1; // Small bonus
            }
        } else {
            // Branch 3
            points = 0;
        }

        return points;
    }
}