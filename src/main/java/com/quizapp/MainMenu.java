package com.quizapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu {
    private final JPanel mainPanel;

    private final Runnable onStartQuiz;
    private final Runnable onOpenHighscores;
    private final Runnable onOpenCredits;
    private final Runnable onOpenTimeMode;
    private final Runnable onOpenAdmin;

    // --- COLORS ---
    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color ACCENT_BLUE = new Color(167, 191, 222);
    private static final Color ACCENT_RED = new Color(230, 92, 85);
    private static final Color ACCENT_YELLOW = new Color(245, 215, 66);
    private static final Color ACCENT_GREY = new Color(89, 97, 110);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

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

        // 1) CONTENT AREA (vertical box)
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(BG_COLOR);
        contentBox.setBorder(new EmptyBorder(50, 0, 40, 0));

        // Title
        JLabel title = new JLabel("Hauptmenü");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentBox.add(title);
        contentBox.add(Box.createVerticalStrut(40));

        // Grid (cards)
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setBackground(BG_COLOR);
        grid.setMaximumSize(new Dimension(950, 420));
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);

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
                "Fragen hinzufügen oder bearbeiten.\n(Simuliert)",
                "⚙",
                ACCENT_GREY,
                onOpenAdmin
        ));

        contentBox.add(grid);

        // 2) CREDITS LINK
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

        // ✅ SCROLL WRAPPER (wrap the correct panel: contentBox)
        JScrollPane pageScroll = new JScrollPane(contentBox);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String desc, String icon, Color accent, Runnable onClick) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // shadow
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

                // white background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);

                // left accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 12, getHeight() - 6, 20, 20);
                g2.fillRect(8, 0, 4, getHeight() - 6);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 30, 25, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
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

        // Text
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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}