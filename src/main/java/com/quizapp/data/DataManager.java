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

            saveQuestion(new Question("Welches Schlüsselwort erstellt ein Objekt?",
                    new String[]{"create", "make", "init", "new"}, 3)); // D

            saveQuestion(new Question("Was ist das Standard-Package in Java?",
                    new String[]{"java.lang", "java.util", "java.io", "java.awt"}, 0)); // A

            saveQuestion(new Question("Welche Methode ist der Einstiegspunkt?",
                    new String[]{"start", "main", "run", "init"}, 1)); // B

            saveQuestion(new Question("Wie kommentiert man eine einzelne Zeile?",
                    new String[]{"/*", "#", "//", "--"}, 2)); // C

            saveQuestion(new Question("Welcher Operator prüft auf Gleichheit?",
                    new String[]{"=", ":=", "equals", "=="}, 3)); // D

            saveQuestion(new Question("Was ist 'null' in Java?",
                    new String[]{"Die Zahl 0", "Eine fehlende Referenz", "Ein leerer String", "Ein Fehler"}, 1)); // B

            saveQuestion(new Question("Welche Schleife prüft die Bedingung am Ende?",
                    new String[]{"do-while", "for", "while", "foreach"}, 0)); // A


            // --- 2. OOP ---
            saveQuestion(new Question("Was ist Vererbung?",
                    new String[]{"Verstecken von Daten", "Übernahme von Eigenschaften", "Polymorphie", "Kapselung"}, 1)); // B

            saveQuestion(new Question("Welches Keyword nutzt man für Vererbung?",
                    new String[]{"extends", "implements", "inherits", "super"}, 0)); // A

            saveQuestion(new Question("Was ist Kapselung?",
                    new String[]{"Vererbung von Methoden", "Überladen von Methoden", "Verbergen von Daten", "Löschen von Objekten"}, 2)); // C

            saveQuestion(new Question("Ein Objekt ist eine Instanz einer...?",
                    new String[]{"Methode", "Variable", "Klasse", "Funktion"}, 2)); // C

            saveQuestion(new Question("Welche Methode wird beim Erstellen eines Objekts aufgerufen?",
                    new String[]{"Destruktor", "Konstruktor", "Main", "Setter"}, 1)); // B

            saveQuestion(new Question("Was bedeutet Polymorphie?",
                    new String[]{"Mehrfachvererbung", "Datenkapselung", "Statische Typisierung", "Vielgestaltigkeit"}, 3)); // D

            saveQuestion(new Question("Welches Keyword macht eine Variable unveränderbar?",
                    new String[]{"static", "final", "const", "immutable"}, 1)); // B

            saveQuestion(new Question("Was ist ein Interface?",
                    new String[]{"Eine Klasse mit Konstruktor", "Eine Sammlung abstrakter Methoden", "Eine Variable", "Ein Datentyp"}, 1)); // B

            saveQuestion(new Question("Was bedeutet 'Overloading'?",
                    new String[]{"Gleicher Name, andere Parameter", "Gleicher Name, gleiche Parameter", "Überschreiben einer Methode", "Löschen einer Methode"}, 0)); // A

            saveQuestion(new Question("Was ist 'this'?",
                    new String[]{"Referenz auf die Elternklasse", "Ein statischer Wert", "Referenz auf das aktuelle Objekt", "Eine Schleife"}, 2)); // C


            // --- 3. ALGORITHMEN ---
            saveQuestion(new Question("Welche Datenstruktur arbeitet nach dem LIFO-Prinzip?",
                    new String[]{"Queue", "List", "Stack", "Tree"}, 2)); // C

            saveQuestion(new Question("Welche Datenstruktur arbeitet nach dem FIFO-Prinzip?",
                    new String[]{"Queue", "Stack", "Array", "Graph"}, 0)); // A

            saveQuestion(new Question("Was ist die Zeitkomplexität von Binary Search?",
                    new String[]{"O(n)", "O(log n)", "O(n^2)", "O(1)"}, 1)); // B

            saveQuestion(new Question("Welches ist ein Sortieralgorithmus?",
                    new String[]{"BinarySearch", "Dijkstra", "QuickSort", "Prim"}, 2)); // C

            saveQuestion(new Question("Was speichert eine HashMap?",
                    new String[]{"Nur Werte", "Schlüssel-Wert-Paare", "Nur Schlüssel", "Sortierte Listen"}, 1)); // B

            saveQuestion(new Question("Was ist ein Array?",
                    new String[]{"Variable Größe", "Verkettete Liste", "Feste Größe, gleicher Datentyp", "Ein Baum"}, 2)); // C

            saveQuestion(new Question("Welche Struktur hat eine Wurzel (Root)?",
                    new String[]{"Stack", "Baum (Tree)", "Queue", "Hashmap"}, 1)); // B

            saveQuestion(new Question("Was ist Rekursion?",
                    new String[]{"Eine Schleife", "Ein Fehler", "Eine Funktion ruft sich selbst auf", "Eine Datenbankabfrage"}, 2)); // C

            saveQuestion(new Question("Was ist der schlechteste Fall für Bubble Sort?",
                    new String[]{"O(n log n)", "O(n^2)", "O(n)", "O(1)"}, 1)); // B

            saveQuestion(new Question("Was ist ein Algorithmus?",
                    new String[]{"Ein Computerprogramm", "Eine Hardware", "Eine Schritt-für-Schritt-Anleitung", "Ein Datentyp"}, 2)); // C


            // --- 4. SQL ---
            saveQuestion(new Question("Welcher Befehl holt Daten aus einer DB?",
                    new String[]{"GET", "SELECT", "FETCH", "PULL"}, 1)); // B

            saveQuestion(new Question("Welcher Befehl fügt Daten ein?",
                    new String[]{"ADD", "UPDATE", "INSERT INTO", "PUT"}, 2)); // C

            saveQuestion(new Question("Was ist ein Primary Key?",
                    new String[]{"Fremdschlüssel", "Eindeutige ID", "Ein Datentyp", "Eine Tabelle"}, 1)); // B

            saveQuestion(new Question("Wofür steht SQL?",
                    new String[]{"Strong Question Language", "Simple Query List", "System Query Logic", "Structured Query Language"}, 3)); // D

            saveQuestion(new Question("Welcher Befehl löscht eine Tabelle?",
                    new String[]{"DROP TABLE", "DELETE TABLE", "REMOVE TABLE", "CLEAR TABLE"}, 0)); // A

            saveQuestion(new Question("Was macht 'JOIN'?",
                    new String[]{"Löscht Duplikate", "Verbindet zwei Tabellen", "Sortiert Daten", "Erstellt eine Tabelle"}, 1)); // B

            saveQuestion(new Question("Welcher Befehl ändert Daten?",
                    new String[]{"CHANGE", "MODIFY", "UPDATE", "ALTER"}, 2)); // C

            saveQuestion(new Question("Was ist ein Foreign Key?",
                    new String[]{"Hauptschlüssel", "Ein Passwort", "Verweis auf Primary Key anderer Tabelle", "Ein Index"}, 2)); // C

            saveQuestion(new Question("Was bedeutet ACID?",
                    new String[]{"Association, Class, Interface, Data", "Atomicity, Consistency, Isolation, Durability", "Access, Control, Input, Data", "All, Clear, Insert, Delete"}, 1)); // B

            saveQuestion(new Question("Welcher Datentyp speichert Text in SQL?",
                    new String[]{"INT", "FLOAT", "VARCHAR", "BOOL"}, 2)); // C


            // --- 5. NETZWERKE ---
            saveQuestion(new Question("Wofür steht HTTP?",
                    new String[]{"High Transfer Text Protocol", "HyperText Transfer Protocol", "Hyper Tool Transfer Protocol", "Home Text Type Protocol"}, 1)); // B

            saveQuestion(new Question("Welcher Port ist Standard für HTTPS?",
                    new String[]{"80", "22", "443", "21"}, 2)); // C

            saveQuestion(new Question("Was ist eine IP-Adresse?",
                    new String[]{"Ein Passwort", "Adresse eines Geräts im Netz", "Ein Protokoll", "Ein Webbrowser"}, 1)); // B

            saveQuestion(new Question("Wofür steht HTML?",
                    new String[]{"HyperText Markup Language", "Home Tool Made Language", "Hyperlink Text Main Language", "High Tech Main Logic"}, 0)); // A

            saveQuestion(new Question("Was macht DNS?",
                    new String[]{"Verschlüsselt Daten", "Speichert Webseiten", "Wandelt Domainnamen in IPs um", "Sendet E-Mails"}, 2)); // C

            saveQuestion(new Question("Was ist Ping?",
                    new String[]{"Ein Spiel", "Testet Erreichbarkeit eines Hosts", "Ein Webserver", "Ein Virus"}, 1)); // B

            saveQuestion(new Question("Welches Protokoll ist verbindungslos?",
                    new String[]{"TCP", "UDP", "HTTP", "FTP"}, 1)); // B

            saveQuestion(new Question("Was ist Localhost?",
                    new String[]{"192.168.0.1", "10.0.0.1", "0.0.0.0", "127.0.0.1"}, 3)); // D

            saveQuestion(new Question("Was ist CSS?",
                    new String[]{"Computer Style System", "Cascading Style Sheets", "Creative Style Sheet", "Coding Super System"}, 1)); // B

            saveQuestion(new Question("Was ist ein API?",
                    new String[]{"Advanced Program Input", "Automated Process Interaction", "Application Programming Interface", "Apple Phone Interface"}, 2)); // C


            // --- 6. OS ---
            saveQuestion(new Question("Was ist der Kernel?",
                    new String[]{"Ein Programm", "Kern des Betriebssystems", "Ein Speicher", "Ein Prozessor"}, 1)); // B

            saveQuestion(new Question("Was ist ein Thread?",
                    new String[]{"Ein Kabel", "Ein leichtgewichtiger Prozess", "Ein Virus", "Ein Ordner"}, 1)); // B

            saveQuestion(new Question("Was ist Deadlock?",
                    new String[]{"Ein Absturz", "Prozesse blockieren sich gegenseitig", "Ein blauer Bildschirm", "Ein Speicherfehler"}, 1)); // B

            saveQuestion(new Question("Welches ist ein Open-Source OS?",
                    new String[]{"Windows", "macOS", "Linux", "iOS"}, 2)); // C

            saveQuestion(new Question("Was verwaltet das Dateisystem?",
                    new String[]{"Den Prozessor", "Dateien und Verzeichnisse", "Das Internet", "Die Grafikkarte"}, 1)); // B

            saveQuestion(new Question("Was ist RAM?",
                    new String[]{"Festplatte", "Prozessor", "Grafikspeicher", "Arbeitsspeicher"}, 3)); // D

            saveQuestion(new Question("Was bedeutet GUI?",
                    new String[]{"Global User Input", "Graphical User Interface", "General Unit Interface", "Gaming User Interface"}, 1)); // B

            saveQuestion(new Question("Was macht der Scheduler?",
                    new String[]{"Speichert Dateien", "Verteilt CPU-Zeit an Prozesse", "Druckt Dokumente", "Startet den PC"}, 1)); // B

            saveQuestion(new Question("Was ist Virtualisierung?",
                    new String[]{"3D Grafik", "Simulation von Hardware", "Internet surfen", "Datenkompression"}, 1)); // B

            saveQuestion(new Question("Was ist ein Treiber?",
                    new String[]{"Ein Benutzer", "Ein Auto", "Software zur Hardware-Steuerung", "Ein Werkzeug"}, 2)); // C


            // --- 7. SOFTWARE ENGINEERING ---
            saveQuestion(new Question("Wofür steht Scrum?",
                    new String[]{"Programmiersprache", "Agiles Framework", "Datenbank", "Betriebssystem"}, 1)); // B

            saveQuestion(new Question("Was ist ein User Story?",
                    new String[]{"Ein Märchen", "Ein Bug Report", "Beschreibung einer Anforderung", "Ein Testfall"}, 2)); // C

            saveQuestion(new Question("Was ist Refactoring?",
                    new String[]{"Code löschen", "Code verbessern ohne Funktionsänderung", "Neue Features bauen", "Testen"}, 1)); // B

            saveQuestion(new Question("Was ist Unit Testing?",
                    new String[]{"Testen des ganzen Systems", "Testen einzelner Komponenten", "Benutzertest", "Hardwaretest"}, 1)); // B

            saveQuestion(new Question("Wofür steht UML?",
                    new String[]{"Universal Main Logic", "User Made Language", "Unified Modeling Language", "Unit Mode List"}, 2)); // C

            saveQuestion(new Question("Was ist ein Singleton?",
                    new String[]{"Eine Variable", "Klasse mit nur einer Instanz", "Ein Datentyp", "Ein Fehler"}, 1)); // B

            saveQuestion(new Question("Was ist Waterfall?",
                    new String[]{"Eine agile Methode", "Ein Diagramm", "Ein lineares Vorgehensmodell", "Ein Algorithmus"}, 2)); // C

            saveQuestion(new Question("Was ist MVC?",
                    new String[]{"Main Version Code", "Make View Create", "Model View Controller", "Multi Variable Coding"}, 2)); // C

            saveQuestion(new Question("Was ist ein Bug?",
                    new String[]{"Ein Fehler im Programm", "Ein Feature", "Ein Virus", "Ein Benutzer"}, 0)); // A

            saveQuestion(new Question("Was ist CI/CD?",
                    new String[]{"Code Input / Code Delete", "Continuous Integration / Deployment", "Create Interface / Create Database", "Clean Input / Clean Data"}, 1)); // B


            // --- 8. SECURITY ---
            saveQuestion(new Question("Was ist Phishing?",
                    new String[]{"Ein Virus", "Datenklau durch gefälschte Mails", "Hacken von WLAN", "Ein Passwort"}, 1)); // B

            saveQuestion(new Question("Was macht eine Firewall?",
                    new String[]{"Löscht Viren", "Überwacht Netzwerkverkehr", "Beschleunigt PC", "Speichert Passwörter"}, 1)); // B

            saveQuestion(new Question("Was ist HTTPS?",
                    new String[]{"Schnelles HTTP", "Verschlüsseltes HTTP", "Altes HTTP", "Neues Internet"}, 1)); // B

            saveQuestion(new Question("Was ist SQL Injection?",
                    new String[]{"Datenbank Update", "Ein Backup", "Einschleusen von SQL-Befehlen", "Ein Login"}, 2)); // C

            saveQuestion(new Question("Was ist ein Hash?",
                    new String[]{"Ein Passwort", "Einweg-Verschlüsselung", "Ein Backup", "Eine Datei"}, 1)); // B

            saveQuestion(new Question("Was ist 2FA?",
                    new String[]{"2 Fast Answers", "Zwei-Faktor-Authentifizierung", "2 File Access", "2 Firewalls Active"}, 1)); // B

            saveQuestion(new Question("Was ist Malware?",
                    new String[]{"Hardware", "Schadsoftware", "Antivirus", "Update"}, 1)); // B

            saveQuestion(new Question("Was ist ein VPN?",
                    new String[]{"Very Private Name", "Virtual Private Network", "Virus Protection Net", "Visual Public Network"}, 1)); // B

            saveQuestion(new Question("Was ist Brute Force?",
                    new String[]{"Ein starker PC", "Ausprobieren aller Passwörter", "Ein Virus", "Eine Firewall"}, 1)); // B

            saveQuestion(new Question("Was bedeutet DSGVO?",
                    new String[]{"Datenschutz-Grundverordnung", "Daten-Sicherheits-Gesetz", "Deutsche-Software-GmbH", "Digital-Service-Group"}, 0)); // A


            // --- 9. TOOLS ---
            saveQuestion(new Question("Welcher Befehl speichert Änderungen?",
                    new String[]{"git save", "git commit", "git store", "git keep"}, 1)); // B

            saveQuestion(new Question("Welcher Befehl lädt Änderungen herunter?",
                    new String[]{"git push", "git down", "git pull", "git get"}, 2)); // C

            saveQuestion(new Question("Was ist ein Repository?",
                    new String[]{"Ein Ordner", "Speicherort für Code", "Eine Datei", "Ein Server"}, 1)); // B

            saveQuestion(new Question("Was ist ein Branch?",
                    new String[]{"Ein Fehler", "Ein Entwicklungszweig", "Ein Tag", "Ein User"}, 1)); // B

            saveQuestion(new Question("Was macht 'git clone'?",
                    new String[]{"Kopiert ein Repo", "Löscht ein Repo", "Erstellt ein Repo", "Zeigt Status"}, 0)); // A

            saveQuestion(new Question("Was ist Maven?",
                    new String[]{"Ein Editor", "Build Management Tool", "Ein Virus", "Ein Spiel"}, 1)); // B

            saveQuestion(new Question("Was ist eine IDE?",
                    new String[]{"Internet Data Exchange", "Integrated Development Environment", "Input Data Error", "Internal Drive Error"}, 1)); // B

            saveQuestion(new Question("Was ist Docker?",
                    new String[]{"Ein Betriebssystem", "Container-Plattform", "Eine Datenbank", "Ein Editor"}, 1)); // B

            saveQuestion(new Question("Was ist Stack Overflow?",
                    new String[]{"Ein Speicherfehler", "Forum für Entwickler", "Ein Virus", "Ein Buch"}, 1)); // B

            saveQuestion(new Question("Was ist JSON?",
                    new String[]{"Java Source Open Network", "Just Simple Object Name", "JavaScript Object Notation", "Join System On Net"}, 2)); // C


            // --- 10. GENERAL ---
            saveQuestion(new Question("Wer gilt als erster Programmierer?",
                    new String[]{"Alan Turing", "Ada Lovelace", "Bill Gates", "Steve Jobs"}, 1)); // B

            saveQuestion(new Question("Was ist binär?",
                    new String[]{"0 bis 9", "0 und 1", "A bis Z", "Ja und Nein"}, 1)); // B

            saveQuestion(new Question("Was ist 1 Byte?",
                    new String[]{"4 Bit", "16 Bit", "8 Bit", "1 Bit"}, 2)); // C

            saveQuestion(new Question("Was ist CPU?",
                    new String[]{"Central Power Unit", "Central Processing Unit", "Computer Personal Unit", "Core Program Unit"}, 1)); // B

            saveQuestion(new Question("Was ist BIOS?",
                    new String[]{"Basic Input Output System", "Big Input Open System", "Base Internal OS", "Binary Input OS"}, 0)); // A

            saveQuestion(new Question("Was ist Open Source?",
                    new String[]{"Kostenpflichtig", "Quellcode ist öffentlich", "Geheim", "Nur für Windows"}, 1)); // B

            saveQuestion(new Question("Was ist KI?",
                    new String[]{"Keine Info", "Künstliche Intelligenz", "Key Input", "Kernel Interface"}, 1)); // B

            saveQuestion(new Question("Was ist Cloud Computing?",
                    new String[]{"Wettervorhersage", "IT-Dienste über Internet", "Festplatte", "Monitor"}, 1)); // B

            saveQuestion(new Question("Was ist IoT?",
                    new String[]{"Input of Text", "Intranet of Tools", "Internet of Things", "Image of Time"}, 2)); // C

            saveQuestion(new Question("Was ist Big Data?",
                    new String[]{"Eine große Datei", "Verarbeitung riesiger Datenmengen", "Ein großer Server", "Ein langes Kabel"}, 1)); // B
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