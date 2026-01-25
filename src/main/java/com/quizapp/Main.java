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

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // --- Pages ---
            // Login page sets username and enters main menu
            Login loginPage = new Login(username -> {
                headerBar.setUsername(username);
                frame.add(headerBar, BorderLayout.NORTH); // show header after login
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "MAIN_MENU");
            });

            // You can keep your existing constructors
            MainMenu mainMenu = new MainMenu(
                    () -> cardLayout.show(cardPanel, "QUIZ"),
                    () -> cardLayout.show(cardPanel, "HIGHSCORES"),
                    () -> cardLayout.show(cardPanel, "CREDITS"),
                    () -> cardLayout.show(cardPanel, "TIME_MODE"),
                    () -> cardLayout.show(cardPanel, "ADMIN")
            );

            Quiz quizPage = new Quiz(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Highscores highscoresPage = new Highscores(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Credits creditsPage = new Credits(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            TimeMode timeModePage = new TimeMode(() -> cardLayout.show(cardPanel, "MAIN_MENU"));
            Admin adminPage = new Admin(() -> cardLayout.show(cardPanel, "MAIN_MENU"));

            // --- Add cards ---
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
                headerBar.setUsername("-");
                frame.remove(headerBar);
                frame.revalidate();
                frame.repaint();

                cardLayout.show(cardPanel, "LOGIN");
                loginPage.focusUsername();
            });
        });
    }
}