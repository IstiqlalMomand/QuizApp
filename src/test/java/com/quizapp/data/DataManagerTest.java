package com.quizapp.data;

import com.quizapp.model.GameLogic;
import com.quizapp.model.HighscoreEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Testklasse implementiert die geforderten Unit-Tests für das Software-Engineering-Projekt.
 * <p>
 * Sie demonstriert zwei grundlegende Testverfahren:
 * <ul>
 * <li><b>Whitebox-Test:</b> Überprüfung der internen Logik und Pfade (Code-Coverage).</li>
 * <li><b>Blackbox-Test:</b> Überprüfung der Funktionalität anhand von Eingabe-Äquivalenzklassen.</li>
 * </ul>
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
class DataManagerTest {

    // ==========================================
    //        2.2.1 WHITEBOX TEST
    // ==========================================

    /**
     * Führt einen <b>Whitebox-Test</b> für die Methode {@link GameLogic#calculateScore(boolean, int)} durch.
     * <p>
     * <b>Begründung der Auswahl:</b><br>
     * Diese Methode wurde gewählt, da sie durch verschachtelte Verzweigungen ({@code if/else})
     * eine nicht-triviale interne Struktur aufweist. Ziel ist es, verschiedene Pfade im
     * Kontrollflussgraphen abzudecken (Zweigüberdeckung).
     * </p>
     * <p>
     * <b>Getestete Pfade:</b>
     * <ol>
     * <li><b>Pfad 1 (Falsch):</b> Antwort ist falsch -> Erwartet 0 Punkte.</li>
     * <li><b>Pfad 2 (Richtig & Schnell):</b> Antwort korrekt und > 10s übrig -> Erwartet 15 Punkte (Basis + Bonus).</li>
     * <li><b>Pfad 3 (Richtig & Langsam):</b> Antwort korrekt und <= 10s übrig -> Erwartet 11 Punkte (Basis + kleiner Bonus).</li>
     * </ol>
     * </p>
     */
    @Test
    void testCalculateScore_Whitebox() {
        GameLogic logic = new GameLogic();

        // Pfad 1: Falsche Antwort
        // Erwartung: Der else-Zweig der äußeren Bedingung wird durchlaufen.
        assertEquals(0, logic.calculateScore(false, 20), "Falsche Antwort sollte 0 Punkte geben.");

        // Pfad 2: Richtige Antwort + Zeitbonus (> 10s)
        // Erwartung: Der if-Zweig der inneren Bedingung wird durchlaufen.
        assertEquals(15, logic.calculateScore(true, 15), "Schnelle richtige Antwort sollte 15 Punkte geben.");

        // Pfad 3: Richtige Antwort + geringer Zeitbonus (<= 10s)
        // Erwartung: Der else-if-Zweig der inneren Bedingung wird durchlaufen.
        assertEquals(11, logic.calculateScore(true, 5), "Langsame richtige Antwort sollte 11 Punkte geben.");
    }

    // ==========================================
    //        2.2.2 BLACKBOX TEST
    // ==========================================

    /**
     * Führt einen <b>Blackbox-Test</b> für die Methode {@link DataManager#getUserHighscores(String)} durch.
     * <p>
     * <b>Begründung der Auswahl:</b><br>
     * Dieser Test basiert auf der Methode der <b>Äquivalenzklassenbildung</b>. Es wird nicht der interne Code
     * betrachtet, sondern das erwartete Verhalten bei verschiedenen Eingabekategorien.
     * </p>
     * <p>
     * <b>Gebildete Äquivalenzklassen:</b>
     * <ol>
     * <li><b>Gültiger, vorhandener Benutzer:</b> Ein Benutzer, der bereits Einträge hat -> Erwartet: Liste mit Daten.</li>
     * <li><b>Gültiger, unbekannter Benutzer:</b> Ein Name, der nicht existiert -> Erwartet: Leere Liste (kein Fehler).</li>
     * <li><b>Ungültige Eingabe (null):</b> Ein technischer Grenzfall -> Erwartet: Leere Liste (robuster Umgang mit null).</li>
     * </ol>
     * </p>
     */
    @Test
    void testGetUserHighscores_Blackbox() {
        // Setup: Einen Test-Datensatz speichern, um sicherzustellen, dass Klasse 1 Daten findet.
        DataManager.saveHighscore("TestUserBlackbox", 100);

        // Äquivalenzklasse 1: Existierender Benutzer
        List<HighscoreEntry> result = DataManager.getUserHighscores("TestUserBlackbox");
        assertFalse(result.isEmpty(), "Sollte Einträge für einen existierenden Benutzer finden.");
        assertEquals("TestUserBlackbox", result.get(0).getPlayerName(), "Der Name im Eintrag sollte übereinstimmen.");

        // Äquivalenzklasse 2: Nicht existierender Benutzer
        List<HighscoreEntry> empty = DataManager.getUserHighscores("GhostUser");
        assertTrue(empty.isEmpty(), "Sollte eine leere Liste für unbekannte Benutzer zurückgeben.");

        // Äquivalenzklasse 3: Ungültige Eingabe (null)
        // Testet die Robustheit der Methode
        List<HighscoreEntry> nullResult = DataManager.getUserHighscores(null);
        assertTrue(nullResult.isEmpty(), "Sollte eine leere Liste bei null-Eingabe zurückgeben (NullPointerException vermeiden).");
    }
}