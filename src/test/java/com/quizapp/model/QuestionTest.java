package com.quizapp.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für das Datenmodell {@link Question}.
 * <p>
 * Diese Unit-Tests stellen sicher, dass die grundlegende Datenhaltung der Anwendung funktioniert.
 * Es wird überprüft, ob Frage-Objekte korrekt instanziiert werden können und ob die
 * Datenkapselung (Getter-Methoden) wie erwartet arbeitet.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
class QuestionTest {

    /**
     * Verifiziert die korrekte Erstellung und Datenabfrage einer {@link Question}-Instanz.
     * <p>
     * <b>Szenario:</b> Erstellen eines Frage-Objekts mit gültigen Daten.
     * <br>
     * <b>Erwartung:</b> Der Konstruktor speichert die Werte korrekt und die Getter
     * geben exakt die gleichen Werte zurück.
     * </p>
     */
    @Test
    void testQuestionCreation() {
        // 1. Testdaten vorbereiten
        String text = "Is Java Object Oriented?";
        String[] options = {"Yes", "No", "Maybe", "Sometimes"};
        int correctIndex = 0;

        // 2. Objekt erstellen
        Question q = new Question(text, options, correctIndex);

        // 3. Verifizieren (Assertions)
        assertEquals("Is Java Object Oriented?", q.getText(), "Fragetext sollte übereinstimmen");
        assertEquals("Yes", q.getOptions()[0], "Erste Antwortoption sollte 'Yes' sein");
        assertEquals(0, q.getCorrectIndex(), "Der Index der richtigen Antwort sollte 0 sein");
    }

    /**
     * Testet das Verhalten der Klasse bei logisch ungültigen Indizes (Randfall-Test).
     * <p>
     * <b>Szenario:</b> Übergabe eines Index (99), der außerhalb des Antwort-Arrays liegt.
     * <br>
     * <b>Erwartung:</b> Da die Klasse {@link Question} als einfaches POJO (Plain Old Java Object)
     * konzipiert ist und keine Validierungslogik im Konstruktor besitzt, wird erwartet,
     * dass der Wert 99 ohne Exception gespeichert wird.
     * </p>
     */
    @Test
    void testInvalidIndex() {
        // Testdaten mit Index außerhalb des Bereichs (Edge Case)
        String[] options = {"A", "B", "C", "D"};
        Question q = new Question("Test", options, 99);

        // Wir erwarten, dass die Klasse den Wert speichert, auch wenn er für das Spiel ungültig wäre.
        // Dies bestätigt, dass die Validierung an anderer Stelle (z.B. UI oder GameLogic) erfolgen muss.
        assertEquals(99, q.getCorrectIndex());
    }
}