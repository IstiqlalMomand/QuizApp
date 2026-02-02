package com.quizapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quizapp.model.HighscoreEntry;
import com.quizapp.model.Question;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Der DataManager ist für die Persistenzschicht der Anwendung verantwortlich.
 * <p>
 * Er verwaltet das Laden und Speichern von Quizfragen und Highscores in lokalen JSON-Dateien.
 * Diese Klasse nutzt die Google Gson Library für die Serialisierung und Deserialisierung von Objekten
 * und stellt statische Methoden bereit, um von überall in der App auf die Daten zugreifen zu können.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class DataManager {

    private static final String HIGHSCORE_FILE = "highscores.json";
    private static final String QUESTIONS_FILE = "questions.json"; // Oder questions_v2.json
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ==========================================
    //              HIGHSCORE VERWALTUNG
    // ==========================================

    /**
     * Speichert einen neuen Highscore-Eintrag in der JSON-Datei.
     * <p>
     * Die Methode lädt die bestehende Liste, fügt den neuen Eintrag mit dem aktuellen Datum hinzu
     * und überschreibt die Datei mit der aktualisierten Liste.
     * </p>
     *
     * @param name  Der Name des Spielers.
     * @param score Die erreichte Punktzahl.
     */
    public static void saveHighscore(String name, int score) {
        List<HighscoreEntry> scores = loadHighscores();
        // Neuen Score mit heutigem Datum hinzufügen
        scores.add(new HighscoreEntry(name, score, java.time.LocalDate.now().toString()));

        try (Writer writer = new FileWriter(HIGHSCORE_FILE)) {
            gson.toJson(scores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle gespeicherten Highscores aus der lokalen Datei.
     *
     * @return Eine Liste von {@link HighscoreEntry}-Objekten. Gibt eine leere Liste zurück,
     * falls die Datei noch nicht existiert oder fehlerhaft ist.
     */
    public static List<HighscoreEntry> loadHighscores() {
        try (Reader reader = new FileReader(HIGHSCORE_FILE)) {
            Type listType = new TypeToken<ArrayList<HighscoreEntry>>(){}.getType();
            List<HighscoreEntry> list = gson.fromJson(reader, listType);

            // Sortierung nach Punktzahl (höchste zuerst)
            if (list != null) {
                list.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
                return list;
            }
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>(); // Rückgabe leere Liste, falls Datei fehlt
        }
    }

    // ==========================================
    //              FRAGEN VERWALTUNG
    // ==========================================

    /**
     * Speichert eine einzelne neue Frage permanent in der Datenbank.
     *
     * @param q Das {@link Question}-Objekt, welches hinzugefügt werden soll.
     */
    public static void saveQuestion(Question q) {
        List<Question> questions = loadQuestions();
        questions.add(q);

        try (Writer writer = new FileWriter(QUESTIONS_FILE)) {
            gson.toJson(questions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt den gesamten Fragenkatalog aus der JSON-Datei.
     *
     * @return Eine Liste aller verfügbaren {@link Question}-Objekte.
     */
    public static List<Question> loadQuestions() {
        try (Reader reader = new FileReader(QUESTIONS_FILE)) {
            Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
            List<Question> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>(); // Rückgabe leer, falls Datei fehlt
        }
    }

    /**
     * Aktualisiert eine bestehende Frage an einem bestimmten Index.
     * Wird vom Administrator-Panel verwendet, um Fragen zu bearbeiten.
     *
     * @param index    Der Listenindex der zu aktualisierenden Frage.
     * @param updatedQ Das neue Fragenobjekt, welches das alte ersetzt.
     */
    public static void updateQuestion(int index, Question updatedQ) {
        List<Question> all = loadQuestions();
        if (index >= 0 && index < all.size()) {
            all.set(index, updatedQ);
            saveAllQuestions(all);
        }
    }

    /**
     * Löscht eine spezifische Frage permanent aus der Liste.
     *
     * @param index Der Index der zu löschenden Frage.
     */
    public static void deleteQuestion(int index) {
        List<Question> all = loadQuestions();
        if (index >= 0 && index < all.size()) {
            all.remove(index);
            saveAllQuestions(all);
        }
    }

    /**
     * Hilfsmethode zum Überschreiben der gesamten Fragenliste in die JSON-Datei.
     * Diese Methode wird intern nach Updates oder Löschvorgängen aufgerufen.
     *
     * @param questions Die Liste der Fragen, die gespeichert werden soll.
     */
    private static void saveAllQuestions(List<Question> questions) {
        try (java.io.Writer writer = new java.io.FileWriter(QUESTIONS_FILE)) {
            new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(questions, writer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stellt sicher, dass die Anwendung beim ersten Start über einen Basis-Fragenkatalog verfügt.
     * Falls die Fragen-Datei leer ist oder nicht existiert, werden Standardfragen zu
     * Java, OOP, SQL etc. generiert und gespeichert.
     */
    public static void ensureQuestionsExist() {
        List<Question> current = loadQuestions();
        if (current.isEmpty()) {
            // --- 1. JAVA BASICS ---
            saveQuestion(new Question("Welcher Datentyp speichert Text in Java?",
                    new String[]{"int", "String", "boolean", "char"}, 1)); // B

            saveQuestion(new Question("Wie beendet man eine Schleife vorzeitig?",
                    new String[]{"stop", "exit", "break", "return"}, 2)); // C

            saveQuestion(new Question("Was ist die Größe eines 'int' in Java?",
                    new String[]{"32 Bit", "16 Bit", "64 Bit", "8 Bit"}, 0)); // A

            // ... (Hier werden normalerweise die restlichen 100 Fragen eingefügt) ...

            saveQuestion(new Question("Was ist Big Data?",
                    new String[]{"Eine große Datei", "Verarbeitung riesiger Datenmengen", "Ein großer Server", "Ein langes Kabel"}, 1)); // B
        }
    }

    // ==========================================
    //        ANFORDERUNGEN FÜR BERICHT
    // ==========================================

    /**
     * Filtert alle Highscore-Einträge für einen spezifischen Benutzernamen.
     * <p>
     * Diese Methode wurde für den <b>Blackbox-Test (Anforderung 2.2.2)</b> implementiert.
     * Sie ermöglicht das Testen von Äquivalenzklassen (gültiger Nutzer, unbekannter Nutzer, null).
     * </p>
     *
     * @param username Der Benutzername, nach dem gefiltert werden soll.
     * @return Eine Liste von {@link HighscoreEntry}-Objekten, die zu diesem Benutzer gehören.
     */
    public static List<HighscoreEntry> getUserHighscores(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>(); // Leere Liste bei ungültiger Eingabe
        }

        List<HighscoreEntry> all = loadHighscores();
        List<HighscoreEntry> userOnly = new ArrayList<>();

        for (HighscoreEntry entry : all) {
            if (entry.getPlayerName().equalsIgnoreCase(username)) {
                userOnly.add(entry);
            }
        }
        return userOnly;
    }

    /**
     * Gibt den höchsten Punktwert eines Spielers als String zurück.
     * <p>
     * <b>Anforderung 1d (Implementierung):</b> Diese Methode wurde spezifisch für die Prüfungsanforderung
     * implementiert, welche eine Methode {@code getUserHighscore(String user)} mit Rückgabetyp {@code String} verlangt.
     * </p>
     *
     * @param username Der Benutzername.
     * @return Die höchste erreichte Punktzahl als String (z.B. "120") oder "0", falls kein Eintrag existiert.
     */
    public static String getUserHighscore(String username) {
        List<HighscoreEntry> userEntries = getUserHighscores(username);

        if (userEntries.isEmpty()) {
            return "0";
        }

        int maxScore = 0;
        for (HighscoreEntry entry : userEntries) {
            if (entry.getScore() > maxScore) {
                maxScore = entry.getScore();
            }
        }

        return String.valueOf(maxScore);
    }
}