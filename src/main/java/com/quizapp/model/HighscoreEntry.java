package com.quizapp.model;

/**
 * Repräsentiert einen einzelnen Eintrag in der Highscore-Liste.
 * <p>
 * Diese Datenklasse speichert die Informationen eines abgeschlossenen Spiels,
 * bestehend aus dem Spielernamen, der erreichten Punktzahl und dem Datum.
 * Objekte dieser Klasse werden durch den {@link com.quizapp.data.DataManager}
 * in eine JSON-Datei serialisiert und von dort geladen.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class HighscoreEntry {
    private String playerName;
    private int score;
    private String date;

    /**
     * Erstellt einen neuen Highscore-Eintrag.
     *
     * @param playerName Der Name des Spielers.
     * @param score      Die erreichte Punktzahl.
     * @param date       Das Datum des Spiels als String (üblicherweise im Format YYYY-MM-DD).
     */
    public HighscoreEntry(String playerName, int score, String date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return Der Spielername.
     */
    public String getPlayerName() { return playerName; }

    /**
     * Gibt die erreichte Punktzahl zurück.
     *
     * @return Die Punktzahl als Integer.
     */
    public int getScore() { return score; }

    /**
     * Gibt das Datum des Spiels zurück.
     *
     * @return Das Datum als String.
     */
    public String getDate() { return date; }
}