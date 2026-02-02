package com.quizapp.view;

import com.quizapp.data.DataManager;
import com.quizapp.model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Die TimeMode-Klasse implementiert den zeitbasierten Spielmodus der Anwendung.
 * <p>
 * Im Gegensatz zum klassischen Quiz muss der Spieler hier gegen die Uhr antreten.
 * Diese Klasse verwaltet:
 * </p>
 * <ul>
 * <li>Den Countdown-Timer (20 Sekunden pro Frage) mit visueller Progress-Bar.</li>
 * <li>Die dynamische Punktevergabe (Basis-Punkte + Bonus für verbleibende Sekunden).</li>
 * <li>Die spezifischen Joker für diesen Modus (50:50 und Frage überspringen).</li>
 * <li>Das automatische Beenden der Runde bei Zeitablauf.</li>
 * </ul>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class TimeMode {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private String currentUsername = "Gast"; // Standardwert

    // --- FARB-DEFINITIONEN ---
    private static final Color ERROR_BG = new Color(255, 230, 230); // Hellrot
    private static final Color ERROR_BORDER = new Color(255, 100, 100); // Rot
    private static final Color HEADER_BG = new Color(13, 44, 94);
    private static final Color BG_COLOR   = new Color(250, 251, 252);
    private static final Color TEXT_DARK  = new Color(33, 37, 41);
    private static final Color CARD_SHADOW = new Color(230, 230, 230);
    private static final Color CARD_WHITE  = Color.WHITE;

    // Timer Farben
    private static final Color TIMER_TRACK = new Color(230, 233, 238);
    private static final Color TIMER_FILL  = new Color(76, 120, 230);
    private static final Color TIMER_TEXT  = new Color(200, 60, 45);

    // Feedback & Joker Farben
    private static final Color OK_BORDER   = new Color(76, 175, 80);
    private static final Color OK_BG       = new Color(233, 245, 234);
    private static final Color ALERT_BG    = new Color(250, 226, 226);
    private static final Color ALERT_TEXT  = new Color(170, 60, 60);
    private static final Color JOKER_RING_PURPLE = new Color(225, 200, 255);
    private static final Color JOKER_TEXT_PURPLE = new Color(120, 50, 180);
    private static final Color SKIP_RING_TEAL = new Color(180, 240, 230);
    private static final Color SKIP_TEXT_TEAL = new Color(0, 150, 130);

    // --- SPIELZUSTAND ---
    private List<Question> questions;
    private int questionIndex = 0;
    private int score = 0;
    private static final int SECONDS_PER_QUESTION = 20;
    private boolean used5050 = false;
    private boolean acceptingAnswers = true;

    // --- UI Referenzen ---
    private JLabel pointsValue;
    private JLabel questionCounterValue;
    private JProgressBar timerBar;
    private JLabel timerSecondsLabel;
    private JLabel questionLabel;
    private JPanel alertPanel;
    private final AnswerOption[] answerOptions = new AnswerOption[4];
    private javax.swing.Timer tickTimer;
    private long questionStartMs;

    /**
     * Erstellt die TimeMode-Ansicht und initialisiert die UI-Komponenten.
     *
     * @param onBack Callback-Funktion für die Rückkehr zum Hauptmenü.
     */
    public TimeMode(Runnable onBack) {
        this.onBack = onBack;
        // Daten laden, aber Spiel noch NICHT starten
        DataManager.ensureQuestionsExist();
        this.questions = DataManager.loadQuestions();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.add(buildStatusBar(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(BG_COLOR);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(30, 0, 30, 0));

        center.add(buildQuestionCard());
        center.add(Box.createVerticalStrut(28));
        center.add(buildAnswersGrid());
        center.add(Box.createVerticalStrut(28));
        center.add(buildJokersRow());
        center.add(Box.createVerticalStrut(28));

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backWrap.setOpaque(false);
        JButton backBtn = new JButton("Zurück");
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBackground(Color.WHITE);
        backBtn.addActionListener(e -> {
            stopTimer();
            onBack.run();
        });
        backWrap.add(backBtn);
        center.add(backWrap);

        JScrollPane pageScroll = new JScrollPane(center);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    /**
     * Startet eine neue Spielrunde im Zeitmodus.
     * <p>
     * Wählt zufällig 10 Fragen aus dem Pool aus, setzt den Punktestand zurück
     * und startet den Ablauf der ersten Frage.
     * </p>
     *
     * @param username Der Name des aktuellen Spielers.
     */
    public void startGame(String username) {
        this.currentUsername = username;
        this.score = 0;
        this.questionIndex = 0;
        this.used5050 = false;

        // 1. Alle Fragen laden
        List<Question> allQuestions = DataManager.loadQuestions();

        if (allQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Keine Fragen vorhanden!");
            onBack.run();
            return;
        }

        // 2. Zufälliges Mischen (Shuffle)
        java.util.Collections.shuffle(allQuestions);

        // 3. Nur die ersten 10 Fragen auswählen
        int limit = Math.min(allQuestions.size(), 10);
        this.questions = allQuestions.subList(0, limit);

        loadQuestion(0);
    }

    // --- UI BUILDER METHODEN ---

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(18, 40, 18, 40));
        bar.setPreferredSize(new Dimension(0, 90));

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        JLabel pointsLabel = new JLabel("PUNKTE");
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        pointsLabel.setForeground(Color.GRAY);
        pointsValue = new JLabel("0");
        pointsValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        pointsValue.setForeground(TEXT_DARK);
        left.add(pointsLabel);
        left.add(pointsValue);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        timerBar = new JProgressBar(0, SECONDS_PER_QUESTION * 1000);
        timerBar.setValue(SECONDS_PER_QUESTION * 1000);
        timerBar.setBorderPainted(false);
        timerBar.setStringPainted(false);
        timerBar.setForeground(TIMER_FILL);
        timerBar.setBackground(TIMER_TRACK);
        timerBar.setPreferredSize(new Dimension(520, 14));
        timerBar.setMaximumSize(new Dimension(520, 14));
        timerSecondsLabel = new JLabel(SECONDS_PER_QUESTION + "s", SwingConstants.CENTER);
        timerSecondsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerSecondsLabel.setForeground(TIMER_TEXT);
        timerSecondsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerSecondsLabel.setBorder(new EmptyBorder(6, 0, 0, 0));
        center.add(timerBar);
        center.add(timerSecondsLabel);

        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        JLabel qLabel = new JLabel("FRAGE", SwingConstants.RIGHT);
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        qLabel.setForeground(Color.GRAY);
        questionCounterValue = new JLabel("1 / -", SwingConstants.RIGHT);
        questionCounterValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        questionCounterValue.setForeground(TEXT_DARK);
        right.add(qLabel);
        right.add(questionCounterValue);

        bar.add(left, BorderLayout.WEST);
        bar.add(center, BorderLayout.CENTER);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildQuestionCard() {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.setColor(CARD_SHADOW); g2.fillRoundRect(5, 5, w - 10, h - 10, 22, 22);
                g2.setColor(CARD_WHITE); g2.fillRoundRect(0, 0, w - 10, h - 10, 22, 22);
                g2.setColor(HEADER_BG); g2.fillRoundRect(0, 0, 10, h - 10, 22, 22);
                g2.fillRect(6, 0, 4, h - 10);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(980, 220));
        card.setBorder(new EmptyBorder(25, 50, 25, 40));
        questionLabel = new JLabel("Willkommen");
        questionLabel.setFont(new Font("Serif", Font.BOLD, 34));
        questionLabel.setForeground(TEXT_DARK);
        alertPanel = new JPanel(new BorderLayout());
        alertPanel.setBackground(ALERT_BG);
        alertPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        alertPanel.setVisible(false);
        JLabel alertText = new JLabel("Zeit abgelaufen!", SwingConstants.CENTER);
        alertText.setForeground(ALERT_TEXT);
        alertText.setFont(new Font("SansSerif", Font.BOLD, 18));
        alertPanel.add(alertText, BorderLayout.CENTER);
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.add(questionLabel);
        inner.add(Box.createVerticalStrut(18));
        inner.add(alertPanel);
        card.add(inner, BorderLayout.CENTER);
        wrap.add(card);
        return wrap;
    }

    private JPanel buildAnswersGrid() {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(980, 220));
        answerOptions[0] = new AnswerOption("A.");
        answerOptions[1] = new AnswerOption("B.");
        answerOptions[2] = new AnswerOption("C.");
        answerOptions[3] = new AnswerOption("D.");
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            answerOptions[i].setOnClick(() -> onAnswerClicked(idx));
            grid.add(answerOptions[i]);
        }
        wrap.add(grid);
        return wrap;
    }

    private JPanel buildJokersRow() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setOpaque(false);
        CircularButton fifty = new CircularButton("50:50", "Joker", JOKER_RING_PURPLE, JOKER_TEXT_PURPLE);
        fifty.setOnClick(() -> {
            if (!used5050 && acceptingAnswers) {
                used5050 = true;
                apply5050();
                fifty.setEnabledLook(false);
            }
        });
        CircularButton skip = new CircularButton("▶|", "Skip", SKIP_RING_TEAL, SKIP_TEXT_TEAL);
        skip.setOnClick(() -> {
            if (acceptingAnswers) {
                stopTimer();
                nextQuestion();
            }
        });
        panel.add(fifty);
        panel.add(skip);
        return panel;
    }

    // --- LOGIC ---

    private void loadQuestion(int index) {
        questionIndex = index;
        if (index >= questions.size()) return;

        Question q = questions.get(index);
        questionLabel.setText(q.getText());
        alertPanel.setVisible(false);
        questionCounterValue.setText((index + 1) + " / " + questions.size());
        pointsValue.setText(String.valueOf(score));

        acceptingAnswers = true;
        for (int i = 0; i < 4; i++) {
            answerOptions[i].setText(q.getOptions()[i]);
            answerOptions[i].setVisible(true);
            answerOptions[i].setState(AnswerOption.State.NORMAL);
            answerOptions[i].setEnabledLook(true);
        }
        startTimer();
    }

    /**
     * Stoppt den laufenden Timer für die aktuelle Frage.
     */
    private void stopTimer() {
        if (tickTimer != null && tickTimer.isRunning()) tickTimer.stop();
    }

    /**
     * Startet den Countdown-Timer für die aktuelle Frage.
     * <p>
     * Ein {@link javax.swing.Timer} aktualisiert alle 50ms die Progress-Bar und
     * das Textlabel. Wenn die Zeit abläuft, wird {@link #onTimeExpired()} aufgerufen.
     * </p>
     */
    private void startTimer() {
        stopTimer();
        timerBar.setMaximum(SECONDS_PER_QUESTION * 1000);
        timerBar.setValue(SECONDS_PER_QUESTION * 1000);
        timerSecondsLabel.setText(SECONDS_PER_QUESTION + "s");
        questionStartMs = System.currentTimeMillis();
        tickTimer = new javax.swing.Timer(50, e -> {
            long elapsed = System.currentTimeMillis() - questionStartMs;
            long remaining = (SECONDS_PER_QUESTION * 1000L) - elapsed;
            if (remaining <= 0) {
                timerBar.setValue(0);
                timerSecondsLabel.setText("0s");
                onTimeExpired();
                return;
            }
            timerBar.setValue((int) remaining);
            int sec = (int) Math.ceil(remaining / 1000.0);
            timerSecondsLabel.setText(sec + "s");
        });
        tickTimer.start();
    }

    /**
     * Wird aufgerufen, wenn der Timer 0 erreicht.
     * Deaktiviert Eingaben, zeigt die Lösung und erzwingt den Wechsel zur nächsten Frage.
     */
    private void onTimeExpired() {
        stopTimer();
        if (!acceptingAnswers) return;
        acceptingAnswers = false;
        alertPanel.setVisible(true);
        Question q = questions.get(questionIndex);
        for (int i = 0; i < 4; i++) {
            if (i == q.getCorrectIndex()) answerOptions[i].setState(AnswerOption.State.CORRECT);
            else answerOptions[i].setEnabledLook(false);
        }
        startDelayTransition(1500);
    }

    /**
     * Verarbeitet den Klick auf eine Antwort.
     * <p>
     * Berechnet die Punkte basierend auf der verbleibenden Zeit:
     * {@code 10 Basispunkte + verbleibende Sekunden}.
     * </p>
     *
     * @param idx Index der gewählten Antwort.
     */
    private void onAnswerClicked(int idx) {
        if (!acceptingAnswers) return;
        stopTimer();
        acceptingAnswers = false;
        Question q = questions.get(questionIndex);

        if (idx == q.getCorrectIndex()) {
            // Richtige Antwort: Punkteberechnung
            int remainingSec = (int)(timerBar.getValue() / 1000);
            int earned = 10 + remainingSec;
            score += earned;
            pointsValue.setText(String.valueOf(score));
        } else {
            // Falsche Antwort: Rot markieren
            answerOptions[idx].setState(AnswerOption.State.WRONG);
        }

        // Richtige Antwort immer Grün anzeigen
        answerOptions[q.getCorrectIndex()].setState(AnswerOption.State.CORRECT);

        for (int i = 0; i < 4; i++) {
            if (i != q.getCorrectIndex() && i != idx) {
                answerOptions[i].setEnabledLook(false);
            }
        }
        startDelayTransition(1500);
    }

    private void startDelayTransition(int delayMs) {
        javax.swing.Timer pause = new javax.swing.Timer(delayMs, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            nextQuestion();
        });
        pause.setRepeats(false);
        pause.start();
    }

    private void nextQuestion() {
        if (questionIndex + 1 >= questions.size()) {
            stopTimer();
            DataManager.saveHighscore(currentUsername, score);
            JOptionPane.showMessageDialog(mainPanel, "Time-Mode beendet!\nPunkte: " + score + "\nErgebnis für " + currentUsername + " gespeichert!");
            onBack.run();
            return;
        }
        loadQuestion(questionIndex + 1);
    }

    private void apply5050() {
        Question q = questions.get(questionIndex);
        int hidden = 0;
        for (int i = 0; i < 4 && hidden < 2; i++) {
            if (i != q.getCorrectIndex()) {
                answerOptions[i].setVisible(false);
                hidden++;
            }
        }
    }

    /**
     * Gibt das Haupt-Panel zurück.
     * @return Das JPanel der TimeMode-Seite.
     */
    public JPanel getMainPanel() { return mainPanel; }

    static class CircularButton extends JPanel {
        private Runnable onClick; private boolean enabledLook = true;
        CircularButton(String symbol, String label, Color ringColor, Color textColor) {
            setLayout(new BorderLayout()); setOpaque(false);
            JPanel circle = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g); Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color fill = new Color(ringColor.getRed(), ringColor.getGreen(), ringColor.getBlue(), enabledLook ? 60 : 25);
                    g2.setColor(fill); g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(enabledLook ? ringColor : new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(3)); g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
                }
            };
            circle.setOpaque(false); circle.setPreferredSize(new Dimension(60, 60)); circle.setLayout(new GridBagLayout());
            JLabel sym = new JLabel(symbol); sym.setFont(new Font("SansSerif", Font.BOLD, 14));
            sym.setForeground(enabledLook ? textColor : new Color(160, 160, 160));
            circle.add(sym);
            JLabel lbl = new JLabel(label, SwingConstants.CENTER); lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(Color.GRAY); lbl.setBorder(new EmptyBorder(5, 0, 0, 0));
            add(circle, BorderLayout.CENTER); add(lbl, BorderLayout.SOUTH);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (onClick != null) onClick.run(); }});
        }
        void setOnClick(Runnable r) { this.onClick = r; }
        void setEnabledLook(boolean enabled) { this.enabledLook = enabled; repaint(); }
    }

    static class AnswerOption extends JPanel {
        enum State { NORMAL, CORRECT, WRONG }

        private Runnable onClick; private State state = State.NORMAL; private boolean enabledLook = true;
        private final JLabel prefixLabel; private final JLabel textLabel;
        AnswerOption(String prefix) {
            setLayout(new BorderLayout(14, 0)); setOpaque(false); setBorder(new EmptyBorder(0, 25, 0, 10)); setCursor(new Cursor(Cursor.HAND_CURSOR));
            prefixLabel = new JLabel(prefix); prefixLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); prefixLabel.setForeground(TEXT_DARK);
            textLabel = new JLabel("—"); textLabel.setFont(new Font("SansSerif", Font.PLAIN, 18)); textLabel.setForeground(new Color(70, 70, 70));
            add(prefixLabel, BorderLayout.WEST); add(textLabel, BorderLayout.CENTER);
            addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (enabledLook && onClick != null) onClick.run(); }});
        }
        void setText(String t) { textLabel.setText(t); }
        void setOnClick(Runnable r) { this.onClick = r; }
        void setState(State s) { this.state = s; repaint(); }
        void setEnabledLook(boolean enabled) { this.enabledLook = enabled; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1; int h = getHeight() - 1;
            g2.setColor(new Color(220, 220, 220)); g2.fillRoundRect(2, 2, w, h, 20, 20);

            // Background Colors
            if (state == State.CORRECT) g2.setColor(OK_BG);
            else if (state == State.WRONG) g2.setColor(ERROR_BG);
            else g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            // Borders
            if (state == State.CORRECT) {
                g2.setColor(OK_BORDER);
                g2.setStroke(new BasicStroke(2));
            } else if (state == State.WRONG) {
                g2.setColor(ERROR_BORDER);
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setColor(enabledLook ? new Color(230, 230, 230) : new Color(245, 245, 245));
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawRoundRect(0, 0, w, h, 20, 20);
        }
    }
}