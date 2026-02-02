package com.quizapp.model;

/**
 * Diese Klasse kapselt die reine Geschäftslogik (Business Logic) für die Punkteberechnung.
 * <p>
 * Sie ist bewusst unabhängig von der Benutzeroberfläche (View) gehalten, um die Testbarkeit
 * mittels JUnit zu erleichtern.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class GameLogic {

    /**
     * Standard-Konstruktor.
     */
    public GameLogic() {
        // Leerer Konstruktor
    }

    /**
     * Berechnet die Punktzahl für eine gegebene Antwort basierend auf der Korrektheit
     * und der verbleibenden Zeit.
     * <p>
     * <b>Hinweis für den Projektbericht (2.2.1 Whitebox-Test):</b><br>
     * Diese Methode wurde gezielt für den Whitebox-Test ausgewählt, da sie über
     * mehrere bedingte Verzweigungen ({@code if/else}) verfügt.
     * </p>
     * <p>
     * <b>Logik der Punktevergabe:</b>
     * </p>
     * <ul>
     * <li>Falsche Antwort: 0 Punkte.</li>
     * <li>Richtige Antwort: 10 Basispunkte.</li>
     * <li>Zeitbonus (&gt; 10 Sekunden übrig): +5 Punkte (Gesamt: 15).</li>
     * <li>Zeitbonus (&gt; 0 Sekunden übrig): +1 Punkt (Gesamt: 11).</li>
     * </ul>
     *
     * @param isCorrect   {@code true}, wenn die vom Spieler gewählte Antwort korrekt war.
     * @param secondsLeft Die verbleibende Zeit in Sekunden zum Zeitpunkt der Antwort.
     * @return Die berechnete Punktzahl als Integer (0, 10, 11 oder 15).
     */
    public int calculateScore(boolean isCorrect, int secondsLeft) {
        int points = 0;

        // Zweig 1: Prüfung der Korrektheit
        if (isCorrect) {
            points = 10; // Basispunkte

            // Zweig 2 (Verschachtelt): Prüfung des Zeitbonus
            if (secondsLeft > 10) {
                points += 5; // Großer Zeitbonus
            } else if (secondsLeft > 0) {
                points += 1; // Kleiner Zeitbonus
            }
        } else {
            // Zweig 3: Antwort falsch
            points = 0;
        }

        return points;
    }
}