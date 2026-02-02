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
 * Diese Klasse nutzt die Google Gson Library für die Serialisierung und Deserialisierung von Objekten.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class DataManager {

    private static final String HIGHSCORE_FILE = "highscores.json";
    private static final String QUESTIONS_FILE = "questions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ==========================================
    //              HIGHSCORE VERWALTUNG
    // ==========================================

    public static void saveHighscore(String name, int score) {
        List<HighscoreEntry> scores = loadHighscores();
        scores.add(new HighscoreEntry(name, score, java.time.LocalDate.now().toString()));

        try (Writer writer = new FileWriter(HIGHSCORE_FILE)) {
            gson.toJson(scores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<HighscoreEntry> loadHighscores() {
        try (Reader reader = new FileReader(HIGHSCORE_FILE)) {
            Type listType = new TypeToken<ArrayList<HighscoreEntry>>(){}.getType();
            List<HighscoreEntry> list = gson.fromJson(reader, listType);

            if (list != null) {
                list.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
                return list;
            }
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Stellt sicher, dass Dummy-Daten (Axel Muster, Kim Beispiel) vorhanden sind.
     * (Anforderung 1c)
     */
    public static void ensureHighscoresExist() {
        List<HighscoreEntry> current = loadHighscores();
        if (current.isEmpty()) {
            current.add(new HighscoreEntry("Axel Muster", 100, "2024-01-15"));
            current.add(new HighscoreEntry("Kim Beispiel", 1, "2024-02-20"));

            try (Writer writer = new FileWriter(HIGHSCORE_FILE)) {
                gson.toJson(current, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ==========================================
    //              FRAGEN VERWALTUNG
    // ==========================================

    public static void saveQuestion(Question q) {
        List<Question> questions = loadQuestions();
        questions.add(q);
        try (Writer writer = new FileWriter(QUESTIONS_FILE)) {
            gson.toJson(questions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Question> loadQuestions() {
        try (Reader reader = new FileReader(QUESTIONS_FILE)) {
            Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
            List<Question> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void updateQuestion(int index, Question updatedQ) {
        List<Question> all = loadQuestions();
        if (index >= 0 && index < all.size()) {
            all.set(index, updatedQ);
            saveAllQuestions(all);
        }
    }

    public static void deleteQuestion(int index) {
        List<Question> all = loadQuestions();
        if (index >= 0 && index < all.size()) {
            all.remove(index);
            saveAllQuestions(all);
        }
    }

    private static void saveAllQuestions(List<Question> questions) {
        try (java.io.Writer writer = new java.io.FileWriter(QUESTIONS_FILE)) {
            gson.toJson(questions, writer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void ensureQuestionsExist() {
        List<Question> current = loadQuestions();
        if (current.isEmpty()) {
            saveQuestion(new Question("Welcher Datentyp speichert Text in Java?",
                    new String[]{"int", "String", "boolean", "char"}, 1));
            saveQuestion(new Question("Wie beendet man eine Schleife vorzeitig?",
                    new String[]{"stop", "exit", "break", "return"}, 2));
            saveQuestion(new Question("Was ist die Größe eines 'int' in Java?",
                    new String[]{"32 Bit", "16 Bit", "64 Bit", "8 Bit"}, 0));
            saveQuestion(new Question("Was ist Big Data?",
                    new String[]{"Eine große Datei", "Verarbeitung riesiger Datenmengen", "Ein großer Server", "Ein langes Kabel"}, 1));
        }
    }

    // ==========================================
    //        ANFORDERUNGEN FÜR BERICHT
    // ==========================================

    /**
     * Filtert alle Highscore-Einträge für einen spezifischen Benutzernamen.
     * (Benötigt für Blackbox-Test 2.2.2)
     */
    public static List<HighscoreEntry> getUserHighscores(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
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
     * ✅ ANFORDERUNG 1d: Method getUserHighscore (Singular)
     * <p>
     * Gibt den höchsten Punktwert eines Spielers als String zurück.
     * </p>
     *
     * @param username Der Benutzername.
     * @return Die höchste erreichte Punktzahl als String (z.B. "150"). Gibt "0" zurück, wenn kein Eintrag existiert.
     */
    public static String getUserHighscore(String username) {
        // Wir nutzen die existierende Methode, um Code-Duplizierung zu vermeiden
        List<HighscoreEntry> userEntries = getUserHighscores(username);

        if (userEntries.isEmpty()) {
            return "0";
        }

        // Finde den maximalen Score
        int maxScore = 0;
        for (HighscoreEntry entry : userEntries) {
            if (entry.getScore() > maxScore) {
                maxScore = entry.getScore();
            }
        }

        return String.valueOf(maxScore);
    }
}