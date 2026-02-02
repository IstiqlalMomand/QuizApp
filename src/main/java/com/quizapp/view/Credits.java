package com.quizapp.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.quizapp.view.components.PrimaryButton;
import java.awt.*;

/**
 * Die Credits-Klasse stellt die "Über uns"-Seite (Impressum) der Anwendung dar.
 * <p>
 * Diese View informiert den Nutzer über das Entwicklungsteam und den akademischen
 * Kontext des Projekts (Software Engineering Kurs an der Universität Potsdam).
 * Sie verwendet ein benutzerdefiniertes Karten-Design ("Card UI") zur ansprechenden
 * Darstellung der Informationen.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class Credits {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    /**
     * Erstellt die Credits-Seite und initialisiert die UI-Komponenten.
     * <p>
     * Das Layout besteht aus einem scrollbaren Bereich, der eine grafisch gestaltete
     * Karte mit den Namen der Entwickler und dem Modulnamen enthält.
     * </p>
     *
     * @param onBack Callback-Funktion, die ausgeführt wird, wenn der "Zurück"-Button gedrückt wird
     * (dient der Navigation zurück zum Hauptmenü).
     */
    public Credits(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(40, 0, 0, 0));

        JLabel title = new JLabel("Credits");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(25));

        // Benutzerdefiniertes Panel mit Schattenwurf und abgerundeten Ecken
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 1;
                int h = getHeight() - 1;

                // Zeichnet den Schatten
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, w - 6, h - 6, 20, 20);

                // Zeichnet den weißen Hintergrund der Karte
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setMaximumSize(new Dimension(950, 260));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel team = new JLabel("Team");
        team.setFont(new Font("SansSerif", Font.BOLD, 16));
        team.setForeground(TEXT_DARK);

        JLabel members = new JLabel("<html>• Istiqlal Momand<br>• Helal Storany</html>");
        members.setFont(new Font("SansSerif", Font.PLAIN, 14));
        members.setForeground(new Color(80, 80, 80));
        members.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel course = new JLabel("<html><br>Software Engineering 1<br>University of Potsdam</html>");
        course.setFont(new Font("SansSerif", Font.PLAIN, 14));
        course.setForeground(new Color(120, 120, 120));

        card.add(team);
        card.add(members);
        card.add(course);

        content.add(card);
        content.add(Box.createVerticalStrut(25));

        JButton back = new PrimaryButton("Zurück");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> onBack.run());

        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scroll, BorderLayout.CENTER);
    }

    /**
     * Gibt das Haupt-Panel dieser View zurück.
     * * @return Das JPanel der Credits-Seite für das CardLayout.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}