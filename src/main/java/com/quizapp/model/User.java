package com.quizapp.model;

/**
 * Repräsentiert einen aktiven Benutzer oder Spieler innerhalb einer laufenden Sitzung.
 * <p>
 * Diese Modellklasse speichert den Namen des aktuellen Spielers sowie dessen
 * momentanen Punktestand während eines Quiz-Durchlaufs. Sie dient primär
 * der temporären Datenhaltung zur Laufzeit und nicht der langfristigen Persistenz
 * (dafür siehe {@link HighscoreEntry}).
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class User {
    private String username;
    private int currentScore;

    /**
     * Erstellt eine neue Benutzer-Instanz.
     * <p>
     * Der Punktestand wird initial auf 0 gesetzt, da ein neues Spiel beginnt.
     * </p>
     *
     * @param username Der gewählte Benutzername für die aktuelle Sitzung.
     */
    public User(String username) {
        this.username = username;
        this.currentScore = 0;
    }

    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return Der Name des Benutzers.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gibt den aktuellen Punktestand des Spielers in der laufenden Runde zurück.
     *
     * @return Die aktuelle Punktzahl.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Aktualisiert den Punktestand des Spielers.
     * <p>
     * Wird typischerweise nach jeder beantworteten Frage aufgerufen, um
     * Punkte hinzuzufügen.
     * </p>
     *
     * @param currentScore Die neue Punktzahl, die gesetzt werden soll.
     */
    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
}