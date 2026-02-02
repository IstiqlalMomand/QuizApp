package com.quizapp;

import com.quizapp.data.DataManager;
import com.quizapp.view.components.HeaderBar;
import com.quizapp.view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Die Hauptklasse (Entry Point) der QuizApp-Anwendung.
 * <p>
 * Diese Klasse ist verantwortlich für die Initialisierung des Hauptfensters (JFrame),
 * den Aufbau der Navigationsstruktur mittels {@link CardLayout} und die Verwaltung
 * des globalen Benutzerzustands. Sie verknüpft die verschiedenen Views (Login, Menü, Quiz etc.)
 * und implementiert die Sicherheitslogik für den Administrator-Zugang.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class Main {

    /**
     * Die Main-Methode dient als Einstiegspunkt der Anwendung.
     * <p>
     * Sie startet die grafische Benutzeroberfläche (GUI) im Event Dispatch Thread (EDT),
     * um Thread-Sicherheit zu gewährleisten. Hier werden alle Seiten (Views) instanziiert
     * und die Navigationslogik (Callback-Methoden) definiert.
     * </p>
     *
     * @param args Kommandozeilenargumente (werden in dieser Anwendung nicht verwendet).
     */
    public static void main(String[] args) {
        // Daten-Initialisierung VOR dem Start der UI
        // 1. Sicherstellen, dass Fragen existieren
        DataManager.ensureQuestionsExist();
        // 2. Sicherstellen, dass Dummy-Highscores existieren (Anforderung 1c)
        DataManager.ensureHighscoresExist();

        SwingUtilities.invokeLater(() -> {
            // Initialisierung des Hauptfensters
            JFrame frame = new JFrame("QuizApp");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
            frame.setLayout(new BorderLayout());

            // Header-Leiste initialisieren (wird erst nach Login angezeigt)
            HeaderBar headerBar = new HeaderBar("QuizApp");
            headerBar.setUsername("-");

            // Zustandsspeicher für den aktuellen Benutzer
            // (Array wird genutzt, um den Wert innerhalb von Lambda-Ausdrücken veränderbar zu machen)
            final String[] currentUser = { "Gast" };

            // Layout-Manager für die Navigation (Seitenwechsel)
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // --- Initialisierung der Seiten (Views) ---

            // Login-Seite: Setzt den Benutzer und zeigt bei Erfolg die Header-Leiste
            Login loginPage = new Login(username -> {
                currentUser[0] = username; // Benutzer speichern

                headerBar.setUsername(username);
                frame.add(headerBar, BorderLayout.NORTH); // Header nach Login anzeigen
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "MAIN_MENU");
            });

            // Initialisierung der Funktionsseiten mit Navigations-Callbacks
            Quiz quizPage = new Quiz(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Highscores highscoresPage = new Highscores(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Credits creditsPage = new Credits(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            TimeMode timeModePage = new TimeMode(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Admin adminPage = new Admin(() -> cardLayout.show(cardPanel, "MAIN_MENU"));

            // HAUPTMENÜ: Definition der Aktionen für die Menü-Buttons
            MainMenu mainMenu = new MainMenu(
                    // 1. Klassisches Quiz starten
                    () -> {
                        quizPage.startGame(currentUser[0]);
                        cardLayout.show(cardPanel, "QUIZ");
                    },
                    // 2. Highscores anzeigen
                    () -> {
                        highscoresPage.refresh();
                        cardLayout.show(cardPanel, "HIGHSCORES");
                    },
                    // 3. Credits anzeigen
                    () -> cardLayout.show(cardPanel, "CREDITS"),
                    // 4. Zeit-Modus starten
                    () -> {
                        timeModePage.startGame(currentUser[0]);
                        cardLayout.show(cardPanel, "TIME_MODE");
                    },
                    // 5. Admin-Bereich (🔒 GESICHERT DURCH SHA-256)
                    () -> {
                        JPasswordField pf = new JPasswordField();
                        int result = JOptionPane.showConfirmDialog(
                                frame, pf, "Bitte Admin-Passwort eingeben:",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                        );

                        if (result == JOptionPane.OK_OPTION) {
                            String input = new String(pf.getPassword());

                            // Prüfung des Hash-Wertes statt Klartext-Vergleich
                            if (checkPassword(input)) {
                                cardLayout.show(cardPanel, "ADMIN");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Falsches Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
            );

            // --- Hinzufügen der Seiten zum CardLayout ---
            cardPanel.add(loginPage.getMainPanel(), "LOGIN");
            cardPanel.add(mainMenu.getMainPanel(), "MAIN_MENU");
            cardPanel.add(quizPage.getMainPanel(), "QUIZ");
            cardPanel.add(highscoresPage.getMainPanel(), "HIGHSCORES");
            cardPanel.add(creditsPage.getMainPanel(), "CREDITS");
            cardPanel.add(timeModePage.getMainPanel(), "TIME_MODE");
            cardPanel.add(adminPage.getMainPanel(), "ADMIN");

            // Startkonfiguration: Header noch nicht sichtbar
            frame.add(cardPanel, BorderLayout.CENTER);

            // Startseite ist der Login
            cardLayout.show(cardPanel, "LOGIN");
            frame.setVisible(true);

            loginPage.focusUsername();

            // --- Globale Navigations-Logik (Header) ---

            // Logout: Setzt Benutzer zurück, entfernt Header und kehrt zum Login zurück
            headerBar.onLogout(() -> {
                currentUser[0] = "Gast";
                headerBar.setUsername("-");
                frame.remove(headerBar);
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "LOGIN");
                loginPage.focusUsername();
            });

            // Home: Kehrt immer zum Hauptmenü zurück
            headerBar.onHome(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
        });
    }

    /**
     * Überprüft das eingegebene Administrator-Passwort auf Sicherheit.
     * <p>
     * Die Methode hasht das eingegebene Passwort mit dem SHA-256 Algorithmus und vergleicht
     * es mit dem fest hinterlegten Hash-Wert. Dies verhindert, dass das Passwort im
     * Quellcode im Klartext lesbar ist.
     * <br>
     * Der hinterlegte Hash entspricht dem Passwort: "admin123" (oder dein gewähltes PW).
     * </p>
     *
     * @param plainText Das vom Benutzer eingegebene Passwort im Klartext.
     * @return {@code true}, wenn der Hash der Eingabe mit dem gespeicherten Admin-Hash übereinstimmt, sonst {@code false}.
     */
    private static boolean checkPassword(String plainText) {
        // SHA-256 Hash für das Admin-Passwort
        String storedHash = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";

        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Konvertierung des Byte-Arrays in einen Hex-String
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().equals(storedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}