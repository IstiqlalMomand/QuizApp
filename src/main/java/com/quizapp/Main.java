import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Swing GUI auf dem Event-Thread starten
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiz App - Clickable Mockup");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 800);
            frame.setLocationRelativeTo(null);

            // Container mit CardLayout (wie ein Stapel Karten)
            JPanel cards = new JPanel(new CardLayout());

            // --- NAVIGATIONSLOGIK ---
            // Wir definieren Aktionen für den Wechsel
            Runnable showQuiz = () -> {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "QUIZ");
            };

            Runnable showMenu = () -> {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "MENU");
            };

            // --- ANSICHTEN ERSTELLEN ---
            // Wir übergeben die Navigations-Befehle an die Klassen
            MainMenu menuView = new MainMenu(showQuiz);
            Quiz quizView = new Quiz(showMenu);

            // Ansichten zum Stapel hinzufügen
            cards.add(menuView.getMainPanel(), "MENU");
            cards.add(quizView.getMainPanel(), "QUIZ");

            // Frame zusammenbauen
            frame.add(cards);
            frame.setVisible(true);
        });
    }
}