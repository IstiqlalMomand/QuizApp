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

public class DataManager {

    private static final String HIGHSCORE_FILE = "highscores.json";
    private static final String QUESTIONS_FILE = "questions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // --- HIGHSCORES ---

    public static void saveHighscore(String name, int score) {
        List<HighscoreEntry> scores = loadHighscores();
        // Add new score with today's date
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
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>(); // Return empty list if file doesn't exist yet
        }
    }

    // --- QUESTIONS ---

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
            return new ArrayList<>(); // Return empty if file missing
        }
    }

    // ✅ UPDATED: Generates 10 questions if file is missing
    public static void ensureQuestionsExist() {
        List<Question> current = loadQuestions();
        if (current.isEmpty()) {
            // 1. Real Question
            saveQuestion(new Question("Was ist das Hauptziel des 'Waterfall Model'?",
                    new String[]{"Lineare Phasenabfolge", "Iterative Entwicklung", "Rapid Prototyping", "Kundenkollaboration"}, 0));

            // 2. Real Question
            saveQuestion(new Question("Was ist ein 'Stakeholder'?",
                    new String[]{"Server-Admin", "Interessengruppe", "Entwickler", "Aktionär"}, 1));

            // 3. Real Question
            saveQuestion(new Question("Wofür steht SDLC?",
                    new String[]{"System Design Logic", "Software Development Life Cycle", "Secure Data Link", "None"}, 1));

            // 4-10. Filler Questions (To reach 10)
            for (int i = 4; i <= 10; i++) {
                saveQuestion(new Question("Dies ist Frage Nummer " + i + " (Platzhalter)",
                        new String[]{"Richtige Antwort A", "Falsche Antwort B", "Falsche Antwort C", "Falsche Antwort D"}, 0));
            }
        }
    }
    //  Added for Report Requirement 2.2.2 (Blackbox Test)
    public static java.util.List<HighscoreEntry> getUserHighscores(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>(); // Empty list for invalid input
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
}