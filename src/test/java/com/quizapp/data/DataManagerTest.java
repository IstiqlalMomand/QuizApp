package com.quizapp.data;

import com.quizapp.model.GameLogic;
import com.quizapp.model.HighscoreEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Testklasse implementiert die geforderten Unit-Tests für das Software-Engineering-Projekt.
 * <p>
 * Abgedeckte Anforderungen laut Aufgabenblatt:
 * 1. Whitebox-Test: Kontrollflussgraph (GameLogic)
 * 2. Blackbox-Test: Äquivalenzklassen (getUserHighscore & getUserHighscores)
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.2
 */
class DataManagerTest {

    // ==========================================
    //  1. WHITEBOX TEST (Anforderung 1a, 1b, 1c)
    // ==========================================

    /**
     * Whitebox-Test für {@link GameLogic#calculateScore(boolean, int)}.
     * <p>
     * <b>Begründung:</b> Die Methode enthält verschachtelte Verzweigungen (if/else).
     * Wir testen alle Pfade des Kontrollflussgraphen (Zweigüberdeckung).
     * </p>
     * <ul>
     * <li>Pfad 1: Falsch -> 0 Punkte</li>
     * <li>Pfad 2: Richtig + Schnell -> 15 Punkte</li>
     * <li>Pfad 3: Richtig + Langsam -> 11 Punkte</li>
     * </ul>
     */
    @Test
    void testCalculateScore_Whitebox() {
        GameLogic logic = new GameLogic();

        assertEquals(0, logic.calculateScore(false, 20), "Pfad 1: Falsche Antwort");
        assertEquals(15, logic.calculateScore(true, 15), "Pfad 2: Richtig (>10s)");
        assertEquals(11, logic.calculateScore(true, 5), "Pfad 3: Richtig (<=10s)");
    }

    // ==========================================
    //  2. BLACKBOX TEST (Anforderung 2a, 2b, 2c)
    // ==========================================

    /**
     *  Testen der Methode getUserHighscore (Singular).
     * <p>
     * Wir bilden Äquivalenzklassen für die Eingabe "username".
     * </p>
     * <ul>
     * <li><b>ÄK 1 (Gültig):</b> Vorhandener Nutzer -> Erwartet: Höchster Score als String ("100").</li>
     * <li><b>ÄK 2 (Unbekannt):</b> Nicht vorhandener Nutzer -> Erwartet: "0".</li>
     * <li><b>ÄK 3 (Robustheit):</b> Null Eingabe -> Erwartet: "0".</li>
     * </ul>
     */
    @Test
    void testGetUserHighscore_Singular_Blackbox() {
        // Setup: Wir speichern 50 und 100, um zu prüfen, ob wirklich das MAXIMUM (100) gefunden wird.
        DataManager.saveHighscore("TestWinner", 50);
        DataManager.saveHighscore("TestWinner", 100);

        // ÄK 1: Nutzer existiert -> Muss 100 sein
        String score = DataManager.getUserHighscore("TestWinner");
        assertEquals("100", score, "Sollte den höchsten Score (100) zurückgeben.");

        // ÄK 2: Nutzer unbekannt -> 0
        String unknown = DataManager.getUserHighscore("Niemand");
        assertEquals("0", unknown);

        // ÄK 3: Null-Eingabe -> 0
        String invalid = DataManager.getUserHighscore(null);
        assertEquals("0", invalid);
    }

    /**
     * Test einer "weiteren Methode" (getUserHighscores - Plural).
     * <p>
     * Prüft, ob die Liste der Einträge korrekt zurückgegeben wird.
     * </p>
     */
    @Test
    void testGetUserHighscores_List_Blackbox() {
        DataManager.saveHighscore("TestListUser", 10);

        // ÄK 1: Gültiger Nutzer -> Liste voll
        List<HighscoreEntry> results = DataManager.getUserHighscores("TestListUser");
        assertFalse(results.isEmpty());
        assertEquals("TestListUser", results.get(0).getPlayerName());

        // ÄK 2: Unbekannter Nutzer -> Liste leer
        List<HighscoreEntry> empty = DataManager.getUserHighscores("Ghost");
        assertTrue(empty.isEmpty());
    }
}