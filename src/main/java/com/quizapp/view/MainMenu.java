package com.quizapp.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Die MainMenu-Klasse stellt das Hauptmenü der Anwendung dar.
 * <p>
 * Sie dient als zentraler Verteiler (Hub) für die Navigation innerhalb der App.
 * Von hier aus kann der Benutzer verschiedene Spielmodi starten (Klassisch, Zeit-Modus),
 * die Highscores einsehen oder in den Administrator-Bereich wechseln.
 * <br>
 * Das Design basiert auf einem Grid-Layout mit interaktiven "Karten" (Cards),
 * die über benutzerdefiniertes Rendering (Schatten, abgerundete Ecken) verfügen.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class MainMenu {
    private final JPanel mainPanel;

    // Callbacks für die Navigation
    private final Runnable onStartQuiz;
    private final Runnable onOpenHighscores;
    private final Runnable onOpenCredits;
    private final Runnable onOpenTimeMode;
    private final Runnable onOpenAdmin;

    // --- Corporate Identity Farben ---
    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color ACCENT_BLUE = new Color(167, 191, 222);
    private static final Color ACCENT_RED = new Color(230, 92, 85);
    private static final Color ACCENT_YELLOW = new Color(245, 215, 66);
    private static final Color ACCENT_GREY = new Color(89, 97, 110);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    /**
     * Erstellt das Hauptmenü und initialisiert alle UI-Komponenten.
     * <p>
     * Die Navigation erfolgt über {@link Runnable}-Callbacks, um die View-Logik
     * von der Steuerungslogik (in {@link com.quizapp.Main}) zu entkoppeln.
     * </p>
     *
     * @param onStartQuiz      Aktion beim Klick auf "Quiz Starten" (Klassischer Modus).
     * @param onOpenHighscores Aktion beim Klick auf "Highscores".
     * @param onOpenCredits    Aktion beim Klick auf den "Credits"-Link.
     * @param onOpenTimeMode   Aktion beim Klick auf "Zeit-Modus".
     * @param onOpenAdmin      Aktion beim Klick auf "Verwaltung" (Admin-Login).
     */
    public MainMenu(
            Runnable onStartQuiz,
            Runnable onOpenHighscores,
            Runnable onOpenCredits,
            Runnable onOpenTimeMode,
            Runnable onOpenAdmin
    ) {
        this.onStartQuiz = onStartQuiz;
        this.onOpenHighscores = onOpenHighscores;
        this.onOpenCredits = onOpenCredits;
        this.onOpenTimeMode = onOpenTimeMode;
        this.onOpenAdmin = onOpenAdmin;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // 1) CONTENT BEREICH (Vertikale Box)
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(BG_COLOR);
        contentBox.setBorder(new EmptyBorder(50, 0, 40, 0));

        // Titel
        JLabel title = new JLabel("Hauptmenü");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentBox.add(title);
        contentBox.add(Box.createVerticalStrut(40));

        // Grid für die Menü-Karten
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setBackground(BG_COLOR);
        grid.setMaximumSize(new Dimension(950, 420));
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hinzufügen der Karten mit Titel, Beschreibung, Icon und Farbe
        grid.add(createCard(
                "Quiz Starten",
                "Klassischer Modus. Keine Zeitbegrenzung.\nTesten Sie Ihr Wissen.",
                "🎓",
                ACCENT_BLUE,
                onStartQuiz
        ));

        grid.add(createCard(
                "Zeit-Modus",
                "20 Sekunden pro Frage. Bonuspunkte für\nGeschwindigkeit!",
                "⏱",
                ACCENT_RED,
                onOpenTimeMode
        ));

        grid.add(createCard(
                "Highscores",
                "Sehen Sie die besten Ergebnisse und Ihre\nPlatzierung.",
                "🏆",
                ACCENT_YELLOW,
                onOpenHighscores
        ));

        grid.add(createCard(
                "Verwaltung",
                "Fragen hinzufügen oder bearbeiten.\n",
                "⚙",
                ACCENT_GREY,
                onOpenAdmin
        ));

        contentBox.add(grid);

        // 2) CREDITS LINK (Fußzeile)
        contentBox.add(Box.createVerticalStrut(40));

        JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        creditsPanel.setBackground(BG_COLOR);
        creditsPanel.setMaximumSize(new Dimension(2000, 50));

        JLabel credits = new JLabel("<html><u>Credits anzeigen</u></html>");
        credits.setFont(new Font("SansSerif", Font.PLAIN, 14));
        credits.setForeground(new Color(150, 150, 150));
        credits.setCursor(new Cursor(Cursor.HAND_CURSOR));

        credits.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onOpenCredits != null) onOpenCredits.run();
            }
        });

        creditsPanel.add(credits);
        contentBox.add(creditsPanel);

        contentBox.add(Box.createVerticalStrut(30));

        // ScrollPane für den Fall kleiner Bildschirme
        JScrollPane pageScroll = new JScrollPane(contentBox);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    /**
     * Hilfsmethode zur Erstellung einer stilisierten Menü-Karte.
     * <p>
     * Jede Karte ist ein interaktives Panel mit Schattenwurf, einem Icon,
     * einem farbigen Akzentstreifen und Text.
     * </p>
     *
     * @param title   Der Titel der Karte.
     * @param desc    Die Beschreibung der Funktion.
     * @param icon    Das Emoji-Icon als String.
     * @param accent  Die Akzentfarbe für den linken Streifen.
     * @param onClick Die Aktion, die beim Klicken ausgeführt wird.
     * @return Ein fertiges JPanel, das dem Grid hinzugefügt werden kann.
     */
    private JPanel createCard(String title, String desc, String icon, Color accent, Runnable onClick) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Zeichnet den Schatten
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

                // Zeichnet den weißen Hintergrund
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);

                // Zeichnet den farbigen Akzentstreifen links
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 12, getHeight() - 6, 20, 20);
                g2.fillRect(8, 0, 4, getHeight() - 6);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 30, 25, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon Container
        JPanel iconP = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(242, 242, 242));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        iconP.setPreferredSize(new Dimension(60, 60));
        iconP.setOpaque(false);

        JLabel lIcon = new JLabel(icon);
        lIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconP.add(lIcon);

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        iconWrap.setOpaque(false);
        iconWrap.add(iconP);

        // Text Container
        JPanel textP = new JPanel();
        textP.setLayout(new BoxLayout(textP, BoxLayout.Y_AXIS));
        textP.setOpaque(false);

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Serif", Font.BOLD, 20));
        lTitle.setForeground(TEXT_DARK);
        lTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea lDesc = new JTextArea(desc);
        lDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lDesc.setForeground(Color.GRAY);
        lDesc.setWrapStyleWord(true);
        lDesc.setLineWrap(true);
        lDesc.setEditable(false);
        lDesc.setOpaque(false);
        lDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        textP.add(lTitle);
        textP.add(Box.createVerticalStrut(5));
        textP.add(lDesc);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textP, BorderLayout.CENTER);

        // Klick-Listener hinzufügen
        if (onClick != null) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
            });
        }

        return card;
    }

    /**
     * Gibt das Haupt-Panel zurück, damit es in das CardLayout eingebunden werden kann.
     *
     * @return Das JPanel des Hauptmenüs.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}