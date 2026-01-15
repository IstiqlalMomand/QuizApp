package com.quizapp;

import com.quizapp.ui.components.HeaderBar;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiz App - Clickable Mockup");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            HeaderBar headerBar = new HeaderBar("QuizApp");
            headerBar.setUsername("Istiqlal");

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // Pages
            Quiz quizPage = new Quiz(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Highscores highscoresPage = new Highscores(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Credits creditsPage = new Credits(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            TimeMode timeModePage = new TimeMode(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Admin adminPage = new Admin(() -> cardLayout.show(cardPanel, "MAIN_MENU"));

            // Main menu with 5 actions
            MainMenu mainMenu = new MainMenu(
                    () -> cardLayout.show(cardPanel, "QUIZ"),
                    () -> cardLayout.show(cardPanel, "HIGHSCORES"),
                    () -> cardLayout.show(cardPanel, "CREDITS"),
                    () -> cardLayout.show(cardPanel, "TIME_MODE"),
                    () -> cardLayout.show(cardPanel, "ADMIN")
            );

            // Add to CardLayout
            cardPanel.add(mainMenu.getMainPanel(), "MAIN_MENU");
            cardPanel.add(quizPage.getMainPanel(), "QUIZ");
            cardPanel.add(highscoresPage.getMainPanel(), "HIGHSCORES");
            cardPanel.add(creditsPage.getMainPanel(), "CREDITS");
            cardPanel.add(timeModePage.getMainPanel(), "TIME_MODE");
            cardPanel.add(adminPage.getMainPanel(), "ADMIN");

            headerBar.onLogout(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            headerBar.onHome(() -> cardLayout.show(cardPanel, "MAIN_MENU"));

            frame.add(headerBar, BorderLayout.NORTH);
            frame.add(cardPanel, BorderLayout.CENTER);

            cardLayout.show(cardPanel, "MAIN_MENU");
            frame.setVisible(true);
        });
    }
}