package com.quizapp;

import com.quizapp.view.components.HeaderBar;
import com.quizapp.view.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("QuizApp");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            HeaderBar headerBar = new HeaderBar("QuizApp");
            headerBar.setUsername("-");

            // State to hold current user
            final String[] currentUser = { "Gast" };

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // --- Pages ---
            Login loginPage = new Login(username -> {
                currentUser[0] = username; // Store user

                headerBar.setUsername(username);
                frame.add(headerBar, BorderLayout.NORTH); // show header after login
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "MAIN_MENU");
            });

            // Initialize Pages
            Quiz quizPage = new Quiz(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Highscores highscoresPage = new Highscores(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Credits creditsPage = new Credits(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            TimeMode timeModePage = new TimeMode(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Admin adminPage = new Admin(() -> cardLayout.show(cardPanel, "MAIN_MENU"));

            MainMenu mainMenu = new MainMenu(
                    // 1. Classic Quiz Action
                    () -> {
                        quizPage.startGame(currentUser[0]); // Start game with user
                        cardLayout.show(cardPanel, "QUIZ");
                    },
                    // 2. Highscores Action
                    () -> {
                        highscoresPage.refresh(); // Refresh data
                        cardLayout.show(cardPanel, "HIGHSCORES");
                    },
                    // 3. Credits Action
                    () -> cardLayout.show(cardPanel, "CREDITS"),
                    // 4. Time Mode Action
                    () -> {
                        timeModePage.startGame(currentUser[0]); // Start game with user
                        cardLayout.show(cardPanel, "TIME_MODE");
                    },
                    // 5. Admin Action
                    () -> cardLayout.show(cardPanel, "ADMIN")
            );

            // --- Add cards (Standard setup) ---
            cardPanel.add(loginPage.getMainPanel(), "LOGIN");
            cardPanel.add(mainMenu.getMainPanel(), "MAIN_MENU");
            cardPanel.add(quizPage.getMainPanel(), "QUIZ");
            cardPanel.add(highscoresPage.getMainPanel(), "HIGHSCORES");
            cardPanel.add(creditsPage.getMainPanel(), "CREDITS");
            cardPanel.add(timeModePage.getMainPanel(), "TIME_MODE");
            cardPanel.add(adminPage.getMainPanel(), "ADMIN");

            // Start WITHOUT header visible
            frame.add(cardPanel, BorderLayout.CENTER);

            // Start on login
            cardLayout.show(cardPanel, "LOGIN");
            frame.setVisible(true);

            loginPage.focusUsername();

            // Logout = back to login + hide header again
            headerBar.onLogout(() -> {
                currentUser[0] = "Gast";
                headerBar.setUsername("-");
                frame.remove(headerBar);
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "LOGIN");
                loginPage.focusUsername();
            });

            // Home button
            headerBar.onHome(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
        });
    }
}