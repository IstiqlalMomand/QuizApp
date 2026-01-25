package com.quizapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TimeMode {
    private final JPanel mainPanel;
    private final Runnable onBack;

    // --- COLORS ---
    private static final Color HEADER_BG = new Color(13, 44, 94);
    private static final Color BG_COLOR   = new Color(250, 251, 252);
    private static final Color TEXT_DARK  = new Color(33, 37, 41);

    private static final Color CARD_SHADOW = new Color(230, 230, 230);
    private static final Color CARD_WHITE  = Color.WHITE;

    // Timer: Blue fill, Red text when low
    private static final Color TIMER_TRACK = new Color(230, 233, 238);
    private static final Color TIMER_FILL  = new Color(76, 120, 230);
    private static final Color TIMER_TEXT  = new Color(200, 60, 45);

    private static final Color OK_BORDER   = new Color(76, 175, 80);
    private static final Color OK_BG       = new Color(233, 245, 234);

    private static final Color ALERT_BG    = new Color(250, 226, 226);
    private static final Color ALERT_TEXT  = new Color(170, 60, 60);

    // Joker colors
    private static final Color JOKER_RING_PURPLE = new Color(225, 200, 255);
    private static final Color JOKER_TEXT_PURPLE = new Color(120, 50, 180);
    private static final Color SKIP_RING_TEAL = new Color(180, 240, 230);
    private static final Color SKIP_TEXT_TEAL = new Color(0, 150, 130);

    // --- GAME STATE ---
    private final List<Question> questions = new ArrayList<>();
    private int questionIndex = 0;
    private int score = 0;

    private static final int TOTAL_QUESTIONS = 10;
    private static final int SECONDS_PER_QUESTION = 20;

    private boolean used5050 = false;
    private boolean acceptingAnswers = true;

    // --- UI refs ---
    private JLabel pointsValue;
    private JLabel questionCounterValue;

    private JProgressBar timerBar;
    private JLabel timerSecondsLabel;

    private JLabel questionLabel;
    private JPanel alertPanel;

    private final AnswerOption[] answerOptions = new AnswerOption[4];

    private javax.swing.Timer tickTimer;
    private long questionStartMs;

    public TimeMode(Runnable onBack) {
        this.onBack = onBack;

        seedQuestions();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // TOP STATUS BAR
        mainPanel.add(buildStatusBar(), BorderLayout.NORTH);

        // CENTER CONTENT (Scrollable)
        JPanel center = new JPanel();
        center.setBackground(BG_COLOR);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Question card
        center.add(buildQuestionCard());
        center.add(Box.createVerticalStrut(28));

        // Answers grid
        center.add(buildAnswersGrid());
        center.add(Box.createVerticalStrut(28));

        // Jokers
        center.add(buildJokersRow());
        center.add(Box.createVerticalStrut(28));

        // Back button
        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backWrap.setOpaque(false);
        JButton backBtn = new JButton("Zurück");
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setBackground(Color.WHITE);
        backBtn.addActionListener(e -> {
            stopTimer(); // Ensure timer stops when leaving
            onBack.run();
        });
        backWrap.add(backBtn);
        center.add(backWrap);

        JScrollPane pageScroll = new JScrollPane(center);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);

        // Start first question
        loadQuestion(0);
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(18, 40, 18, 40));
        bar.setPreferredSize(new Dimension(0, 90));

        // Left: points
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

        // Center: timer bar + seconds
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
        // Removed RIGHT_TO_LEFT orientation for standard drain behavior

        timerSecondsLabel = new JLabel(SECONDS_PER_QUESTION + "s", SwingConstants.CENTER);
        timerSecondsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerSecondsLabel.setForeground(TIMER_TEXT);
        timerSecondsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerSecondsLabel.setBorder(new EmptyBorder(6, 0, 0, 0));

        center.add(timerBar);
        center.add(timerSecondsLabel);

        // Right: question counter
        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);

        JLabel qLabel = new JLabel("FRAGE", SwingConstants.RIGHT);
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        qLabel.setForeground(Color.GRAY);

        questionCounterValue = new JLabel("1 / " + TOTAL_QUESTIONS, SwingConstants.RIGHT);
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
                int w = getWidth();
                int h = getHeight();
                g2.setColor(CARD_SHADOW);
                g2.fillRoundRect(5, 5, w - 10, h - 10, 22, 22);
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, w - 10, h - 10, 22, 22);
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, 0, 10, h - 10, 22, 22);
                g2.fillRect(6, 0, 4, h - 10);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(980, 220));
        card.setBorder(new EmptyBorder(25, 50, 25, 40));

        questionLabel = new JLabel("—");
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
                stopTimer(); // BUG FIX: Stop timer immediately on skip
                nextQuestion();
            }
        });

        panel.add(fifty);
        panel.add(skip);
        return panel;
    }

    private void seedQuestions() {
        // Simple demo data
        questions.add(new Question(
                "Was ist das Hauptziel des 'Waterfall Model'?",
                new String[]{"Lineare Phasenabfolge", "Iterative Entwicklung", "Rapid Prototyping", "Kundenkollaboration"}, 0
        ));
        questions.add(new Question(
                "Was ist ein 'Stakeholder' im Software Engineering?",
                new String[]{"Der Server-Admin", "Person mit Interesse am Projekt", "Der Hauptentwickler", "Ein Aktionär der Firma"}, 1
        ));
        questions.add(new Question(
                "Was bezeichnet 'White Box Testing'?",
                new String[]{"Testen ohne Code-Kenntnis", "Testen der internen Struktur", "Nur UI testen", "Nur Performance testen"}, 1
        ));
        while (questions.size() < TOTAL_QUESTIONS) {
            int n = questions.size() + 1;
            questions.add(new Question("Frage " + n + ": Platzhalter Text?",
                    new String[]{"Option A", "Option B", "Option C", "Option D"}, 0));
        }
    }

    // --- LOGIC ---

    private void loadQuestion(int index) {
        questionIndex = index;
        Question q = questions.get(index);

        questionLabel.setText(q.text);
        alertPanel.setVisible(false);
        questionCounterValue.setText((index + 1) + " / " + TOTAL_QUESTIONS);

        // Ensure score is current (in case of skip)
        pointsValue.setText(String.valueOf(score));

        // Reset answers
        acceptingAnswers = true;
        for (int i = 0; i < 4; i++) {
            answerOptions[i].setText(q.options[i]);
            answerOptions[i].setVisible(true);
            answerOptions[i].setState(AnswerOption.State.NORMAL);
            answerOptions[i].setEnabledLook(true);
        }

        // BUG FIX: Start the timer for the new question
        startTimer();
    }

    private void stopTimer() {
        if (tickTimer != null && tickTimer.isRunning()) {
            tickTimer.stop();
        }
    }

    private void startTimer() {
        stopTimer(); // Ensure no duplicates

        // Reset visuals
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
            // "Ceil" makes it feel more natural (shows 1s until it hits 0.0s)
            int sec = (int) Math.ceil(remaining / 1000.0);
            timerSecondsLabel.setText(sec + "s");
        });
        tickTimer.start();
    }

    private void onTimeExpired() {
        stopTimer(); // BUG FIX: Stop immediately to prevent overlapping events
        if (!acceptingAnswers) return;

        acceptingAnswers = false;
        alertPanel.setVisible(true);

        Question q = questions.get(questionIndex);
        // Highlight correct
        for (int i = 0; i < 4; i++) {
            if (i == q.correctIndex) answerOptions[i].setState(AnswerOption.State.CORRECT);
            else answerOptions[i].setEnabledLook(false);
        }

        // Delay then next
        startDelayTransition(1500);
    }

    private void onAnswerClicked(int idx) {
        if (!acceptingAnswers) return;

        stopTimer(); // BUG FIX: Stop timer immediately
        acceptingAnswers = false;
        Question q = questions.get(questionIndex);

        if (idx == q.correctIndex) {
            score += 10;
            // BUG FIX: Update UI immediately so user sees points
            pointsValue.setText(String.valueOf(score));
        }

        for (int i = 0; i < 4; i++) {
            if (i == q.correctIndex) answerOptions[i].setState(AnswerOption.State.CORRECT);
            else answerOptions[i].setEnabledLook(false);
        }

        startDelayTransition(800);
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
        if (questionIndex + 1 >= TOTAL_QUESTIONS) {
            stopTimer();
            JOptionPane.showMessageDialog(mainPanel, "Time-Mode beendet!\nPunkte: " + score);
            onBack.run();
            return;
        }
        loadQuestion(questionIndex + 1);
    }

    private void apply5050() {
        Question q = questions.get(questionIndex);
        int hidden = 0;
        for (int i = 0; i < 4 && hidden < 2; i++) {
            if (i != q.correctIndex) {
                answerOptions[i].setVisible(false);
                hidden++;
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // --- MODELS & COMPONENTS ---

    private static class Question {
        final String text;
        final String[] options;
        final int correctIndex;

        Question(String text, String[] options, int correctIndex) {
            this.text = text;
            this.options = options;
            this.correctIndex = correctIndex;
        }
    }

    static class CircularButton extends JPanel {
        private Runnable onClick;
        private boolean enabledLook = true;

        CircularButton(String symbol, String label, Color ringColor, Color textColor) {
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color fill = new Color(ringColor.getRed(), ringColor.getGreen(), ringColor.getBlue(), enabledLook ? 60 : 25);
                    g2.setColor(fill);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(enabledLook ? ringColor : new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
                }
            };
            circle.setOpaque(false);
            circle.setPreferredSize(new Dimension(60, 60));
            circle.setLayout(new GridBagLayout());

            JLabel sym = new JLabel(symbol);
            sym.setFont(new Font("SansSerif", Font.BOLD, 14));
            sym.setForeground(enabledLook ? textColor : new Color(160, 160, 160));
            circle.add(sym);

            JLabel lbl = new JLabel(label, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(Color.GRAY);
            lbl.setBorder(new EmptyBorder(5, 0, 0, 0));

            add(circle, BorderLayout.CENTER);
            add(lbl, BorderLayout.SOUTH);

            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { if (onClick != null) onClick.run(); }
            });
        }
        void setOnClick(Runnable r) { this.onClick = r; }
        void setEnabledLook(boolean enabled) { this.enabledLook = enabled; repaint(); }
    }

    static class AnswerOption extends JPanel {
        enum State { NORMAL, CORRECT }
        private Runnable onClick;
        private State state = State.NORMAL;
        private boolean enabledLook = true;
        private final JLabel prefixLabel;
        private final JLabel textLabel;

        AnswerOption(String prefix) {
            setLayout(new BorderLayout(14, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(0, 25, 0, 10));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            prefixLabel = new JLabel(prefix);
            prefixLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            prefixLabel.setForeground(TEXT_DARK);

            textLabel = new JLabel("—");
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            textLabel.setForeground(new Color(70, 70, 70));

            add(prefixLabel, BorderLayout.WEST);
            add(textLabel, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (enabledLook && onClick != null) onClick.run();
                }
            });
        }
        void setText(String t) { textLabel.setText(t); }
        void setOnClick(Runnable r) { this.onClick = r; }
        void setState(State s) { this.state = s; repaint(); }
        void setEnabledLook(boolean enabled) { this.enabledLook = enabled; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;

            g2.setColor(new Color(220, 220, 220));
            g2.fillRoundRect(2, 2, w, h, 20, 20);

            if (state == State.CORRECT) g2.setColor(OK_BG);
            else g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            if (state == State.CORRECT) {
                g2.setColor(OK_BORDER);
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setColor(enabledLook ? new Color(230, 230, 230) : new Color(245, 245, 245));
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawRoundRect(0, 0, w, h, 20, 20);
        }
    }
}