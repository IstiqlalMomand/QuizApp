package com.quizapp.model;

/**
 * Repräsentiert eine einzelne Quizfrage innerhalb der Anwendung.
 * <p>
 * Diese Datenklasse speichert den Fragetext, die möglichen Antwortoptionen und
 * den Index der korrekten Antwort. Sie dient als primäres Datenmodell für die
 * Serialisierung und Deserialisierung durch GSON (JSON-Speicherung).
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class Question {
    private String text;
    private String[] options; // Array aus 4 Antwortmöglichkeiten
    private int correctIndex; // 0, 1, 2 oder 3

    /**
     * Erstellt eine neue Frage-Instanz.
     * <p>
     * Dieser Konstruktor wird sowohl beim manuellen Erstellen von Fragen (z.B. im Admin-Bereich
     * oder bei der Initialisierung) als auch von GSON beim Laden aus der JSON-Datei verwendet.
     * </p>
     *
     * @param text         Der eigentliche Text der Frage.
     * @param options      Ein String-Array, das exakt 4 Antwortmöglichkeiten enthalten muss.
     * @param correctIndex Der Index (0 bis 3), der auf die korrekte Antwort im {@code options}-Array verweist.
     */
    public Question(String text, String[] options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    /**
     * Gibt den Text der Frage zurück.
     *
     * @return Der Fragetext als String.
     */
    public String getText() { return text; }

    /**
     * Gibt die verfügbaren Antwortmöglichkeiten zurück.
     *
     * @return Ein Array von Strings, das die Antworten enthält.
     */
    public String[] getOptions() { return options; }

    /**
     * Gibt den Index der korrekten Antwort zurück.
     *
     * @return Ein Integer zwischen 0 und 3, der die Position der richtigen Antwort im Array markiert.
     */
    public int getCorrectIndex() { return correctIndex; }
}