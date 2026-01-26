package com.quizapp.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testQuestionCreation() {
        // 1. Create data
        String text = "Is Java Object Oriented?";
        String[] options = {"Yes", "No", "Maybe", "Sometimes"};
        int correctIndex = 0;

        // 2. Create Object
        Question q = new Question(text, options, correctIndex);

        // 3. Verify getters work correctly
        assertEquals("Is Java Object Oriented?", q.getText());
        assertEquals("Yes", q.getOptions()[0]);
        assertEquals(0, q.getCorrectIndex());
    }

    @Test
    void testInvalidIndex() {
        // Optional: Check what happens if we give a weird index (Edge case)
        String[] options = {"A", "B", "C", "D"};
        Question q = new Question("Test", options, 99);

        // We just expect it to store 99, even if it's invalid for the game
        assertEquals(99, q.getCorrectIndex());
    }
}