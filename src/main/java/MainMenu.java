import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu {
    private JPanel mainPanel;
    private final Runnable onStartQuiz;

    // --- FARBEN ---
    private static final Color HEADER_BG = new Color(13, 44, 94);       // Dunkelblau
    private static final Color BG_COLOR = new Color(250, 251, 252);     // Hellgrau/Weiß
    private static final Color ACCENT_BLUE = new Color(167, 191, 222);
    private static final Color ACCENT_RED = new Color(230, 92, 85);
    private static final Color ACCENT_YELLOW = new Color(245, 215, 66);
    private static final Color ACCENT_GREY = new Color(89, 97, 110);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    public MainMenu(Runnable onStartQuiz) {
        this.onStartQuiz = onStartQuiz;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // 1. HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 40, 0, 40));

        // Linke Seite:
        JLabel lblBrand = new JLabel("QuizApp");
        lblBrand.setFont(new Font("Serif", Font.BOLD, 32));
        lblBrand.setForeground(Color.WHITE);

        // Rechte Seite: User + Abmelden
        JPanel userP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        userP.setOpaque(false);

        JLabel uName = new JLabel("Angemeldet als: Istiqlal");
        uName.setFont(new Font("SansSerif", Font.BOLD, 14));
        uName.setForeground(Color.WHITE);

        JButton btnOut = new JButton("Abmelden");
        btnOut.setBackground(new Color(60, 90, 160));
        btnOut.setForeground(Color.WHITE);
        btnOut.setFocusPainted(false);
        btnOut.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnOut.setOpaque(true);
        btnOut.setBorderPainted(false);
        btnOut.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnOut.addActionListener(e -> JOptionPane.showMessageDialog(mainPanel, "Logout erfolgreich (Simulation)"));

        userP.add(uName);
        userP.add(btnOut);

        header.add(lblBrand, BorderLayout.WEST);
        header.add(userP, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // 2. CONTENT BEREICH (Vertikale Box)
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(BG_COLOR);
        contentBox.setBorder(new EmptyBorder(50, 0, 0, 0));

        // Titel
        JLabel title = new JLabel("Hauptmenü");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(title);
        contentBox.add(Box.createVerticalStrut(40));

        // Grid (Karten)
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setBackground(BG_COLOR);
        grid.setMaximumSize(new Dimension(950, 420));
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);

        grid.add(createCard("Quiz Starten", "Klassischer Modus. Keine Zeitbegrenzung.\nTesten Sie Ihr Wissen.", "🎓", ACCENT_BLUE, true));
        grid.add(createCard("Zeit-Modus", "20 Sekunden pro Frage. Bonuspunkte für\nGeschwindigkeit!", "⏱", ACCENT_RED, false));
        grid.add(createCard("Highscores", "Sehen Sie die besten Ergebnisse und Ihre\nPlatzierung.", "🏆", ACCENT_YELLOW, false));
        grid.add(createCard("Verwaltung", "Fragen hinzufügen oder bearbeiten.\n(Simuliert)", "⚙", ACCENT_GREY, false));

        contentBox.add(grid);

        // 3. CREDITS
        contentBox.add(Box.createVerticalStrut(40)); // Abstand zum Grid

        // Wrapper-Panel
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
                JOptionPane.showMessageDialog(mainPanel, "Credits:\nEntwickelt von Gruppe 25\nSoftware Engineering I");
            }
        });

        creditsPanel.add(credits);
        contentBox.add(creditsPanel);

        // Platzhalter zum unteren Rand
        contentBox.add(Box.createVerticalStrut(30));

        mainPanel.add(contentBox, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String desc, String icon, Color accent, boolean isStart) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Schatten
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 20, 20);
                // Hintergrund Weiß
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 20, 20);
                // Akzent-Balken links
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 12, getHeight()-6, 20, 20);
                g2.fillRect(8, 0, 4, getHeight()-6);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 30, 25, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon links
        JPanel iconP = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(242, 242, 242)); // Grauer Kreis
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

        // Text Rechts
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

        if (isStart) {
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { onStartQuiz.run(); }
            });
        }
        return card;
    }

    public JPanel getMainPanel() { return mainPanel; }
}