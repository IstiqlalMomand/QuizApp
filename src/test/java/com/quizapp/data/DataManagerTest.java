package com.quizapp.data;

import com.quizapp.model.GameLogic;
import com.quizapp.model.HighscoreEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {

    // --- 2.2.1 WHITEBOX TEST (GameLogic) ---
    // We chose 'calculateScore' because it has multiple branches (if/else).
    @Test
    void testCalculateScore_Whitebox() {
        GameLogic logic = new GameLogic();

        // Path 1: Wrong Answer (Result 0)
        assertEquals(0, logic.calculateScore(false, 20));

        // Path 2: Correct + Fast (Result 15)
        assertEquals(15, logic.calculateScore(true, 15));

        // Path 3: Correct + Slow (Result 11)
        assertEquals(11, logic.calculateScore(true, 5));
    }

    // --- 2.2.2 BLACKBOX TEST (getUserHighscores) ---
    // Equivalence Class 1: Valid User with scores (Expect List)
    // Equivalence Class 2: Valid User NO scores (Expect Empty)
    // Equivalence Class 3: Invalid/Null User (Expect Empty)
    @Test
    void testGetUserHighscores_Blackbox() {
        // Setup: Save a dummy score first to ensure data exists
        DataManager.saveHighscore("TestUserBlackbox", 100);

        // Class 1: Existing User
        List<HighscoreEntry> result = DataManager.getUserHighscores("TestUserBlackbox");
        assertFalse(result.isEmpty(), "Should find entries for existing user");
        assertEquals("TestUserBlackbox", result.get(0).getPlayerName());

        // Class 2: Non-existent User
        List<HighscoreEntry> empty = DataManager.getUserHighscores("GhostUser");
        assertTrue(empty.isEmpty(), "Should return empty list for unknown user");

        // Class 3: Null Input
        List<HighscoreEntry> nullResult = DataManager.getUserHighscores(null);
        assertTrue(nullResult.isEmpty(), "Should return empty list for null input");
    }
}