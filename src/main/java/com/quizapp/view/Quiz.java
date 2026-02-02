package com.quizapp.view;

import com.quizapp.data.DataManager;
import com.quizapp.model.Question;
import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Die Quiz-Klasse repräsentiert die eigentliche Spieloberfläche für den klassischen Modus.
 * <p>
 * Sie steuert den gesamten Spielablauf einer Runde, einschließlich:
 * </p>
 * <ul>
 * <li>Auswahl von 10 zufälligen Fragen aus dem Datenbestand.</li>
 * <li>Darstellung der Fragen und Antwortmöglichkeiten.</li>
 * <li>Validierung der Benutzereingaben mit visuellem Feedback (Grün/Rot).</li>
 * <li>Verwaltung der Joker (50:50, Überspringen).</li>
 * <li>Speicherung des Endergebnisses via {@link DataManager}.</li>
 * </ul>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class Quiz {
    // ... (Code unverändert, aber @return bei getMainPanel hinzufügen) ...
    private final JPanel mainPanel;
    private final Runnable onBackToMenu;

    private String currentUsername = "Gast";

    private static final Color HEADER_BG = new Color(13, 44, 94);
    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color CARD_SHADOW = new Color(230, 230, 230);
    private static final Color CARD_WHITE  = Color.WHITE;

    private static final Color OK_BORDER   = new Color(76, 175, 80);
    private static final Color OK_BG       = new Color(233, 245, 234);
    private static final Color ERROR_BG    = new Color(255, 230, 230);
    private static final Color ERROR_BORDER = new Color(255, 100, 100);

    private static final Color JOKER_RING_PURPLE = new Color(225, 200, 255);
    private static final Color JOKER_TEXT_PURPLE = new Color(120, 50, 180);
    private static final Color SKIP_RING_TEAL = new Color(180, 240, 230);
    private static final Color SKIP_TEXT_TEAL = new Color(0, 150, 130);

    private List<Question> questions;
    private int questionIndex = 0;
    private int score = 0;
    private boolean used5050 = false;
    private boolean acceptingAnswers = true;

    private JLabel scoreLabel;
    private JLabel questionCounterLabel;
    private JLabel questionTextLabel;
    private final AnswerButton[] answerButtons = new AnswerButton[4];

    /**
     * Erstellt die Quiz-Ansicht und baut die grafische Oberfläche auf.
     *
     * @param onBackToMenu Callback-Funktion, um nach Spielende zum Hauptmenü zurückzukehren.
     */
    public Quiz(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;

        DataManager.ensureQuestionsExist();
        this.questions = DataManager.loadQuestions();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        JPanel contentBody = new JPanel();
        contentBody.setLayout(new BoxLayout(contentBody, BoxLayout.Y_AXIS));
        contentBody.setBackground(BG_COLOR);
        contentBody.setBorder(new EmptyBorder(0, 0, 40, 0));

        contentBody.add(buildStatsBar());
        contentBody.add(Box.createVerticalStrut(40));

        contentBody.add(buildQuestionCard());
        contentBody.add(Box.createVerticalStrut(30));

        contentBody.add(buildAnswersGrid());
        contentBody.add(Box.createVerticalStrut(40));

        contentBody.add(buildJokerPanel());
        contentBody.add(Box.createVerticalStrut(25));

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backWrap.setOpaque(false);
        JButton backBtn = new PrimaryButton("Zurück");
        backBtn.addActionListener(e -> onBackToMenu.run());
        backWrap.add(backBtn);
        contentBody.add(backWrap);

        JScrollPane scroll = new JScrollPane(contentBody);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scroll, BorderLayout.CENTER);
    }

    /**
     * Startet eine neue Spielrunde.
     *
     * @param username Der Name des Spielers für die Highscore-Speicherung.
     */
    public void startGame(String username) {
        this.currentUsername = username;
        this.score = 0;
        this.questionIndex = 0;
        this.used5050 = false;

        List<Question> allQuestions = DataManager.loadQuestions();

        if (allQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Keine Fragen gefunden!");
            onBackToMenu.run();
            return;
        }

        java.util.Collections.shuffle(allQuestions);

        int limit = Math.min(allQuestions.size(), 10);
        this.questions = allQuestions.subList(0, limit);

        loadQuestion(0);
    }

    private void loadQuestion(int index) {
        if (index >= questions.size()) return;
        questionIndex = index;
        Question q = questions.get(index);

        questionTextLabel.setText("<html><center>" + q.getText() + "</center></html>");
        questionCounterLabel.setText((index + 1) + " / " + questions.size());
        scoreLabel.setText(String.valueOf(score));

        acceptingAnswers = true;
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(q.getOptions()[i]);
            answerButtons[i].setVisible(true);
            answerButtons[i].setState(AnswerButton.State.NORMAL);
            answerButtons[i].setEnabledLook(true);
        }
    }

    private void onAnswerClicked(int idx) {
        if (!acceptingAnswers) return;
        acceptingAnswers = false;

        Question q = questions.get(questionIndex);

        if (idx == q.getCorrectIndex()) {
            score += 10;
            scoreLabel.setText(String.valueOf(score));
        } else {
            answerButtons[idx].setState(AnswerButton.State.WRONG);
        }

        answerButtons[q.getCorrectIndex()].setState(AnswerButton.State.CORRECT);

        for (int i = 0; i < 4; i++) {
            if (i != q.getCorrectIndex() && i != idx) {
                answerButtons[i].setEnabledLook(false);
            }
        }

        Timer t = new Timer(1500, e -> {
            ((Timer)e.getSource()).stop();
            nextQuestion();
        });
        t.setRepeats(false);
        t.start();
    }

    private void nextQuestion() {
        if (questionIndex + 1 >= questions.size()) {
            DataManager.saveHighscore(currentUsername, score);
            JOptionPane.showMessageDialog(mainPanel,
                    "Quiz beendet!\nPunkte: " + score + "\nGespeichert für: " + currentUsername);
            onBackToMenu.run();
            return;
        }
        loadQuestion(questionIndex + 1);
    }

    private void apply5050() {
        Question q = questions.get(questionIndex);
        int hidden = 0;
        for (int i = 0; i < 4 && hidden < 2; i++) {
            if (i != q.getCorrectIndex()) {
                answerButtons[i].setVisible(false);
                hidden++;
            }
        }
    }

    private JPanel buildStatsBar() {
        JPanel statsBar = new JPanel(new BorderLayout());
        statsBar.setBackground(Color.WHITE);
        statsBar.setBorder(new EmptyBorder(15, 50, 15, 50));
        statsBar.setMaximumSize(new Dimension(2000, 80));

        JPanel pLeft = new JPanel(new GridLayout(2, 1));
        pLeft.setOpaque(false);
        JLabel l1 = new JLabel("PUNKTE");
        l1.setFont(new Font("SansSerif", Font.BOLD, 12));
        l1.setForeground(Color.GRAY);
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        scoreLabel.setForeground(TEXT_DARK);
        pLeft.add(l1);
        pLeft.add(scoreLabel);

        JPanel pRight = new JPanel(new GridLayout(2, 1));
        pRight.setOpaque(false);
        JLabel r1 = new JLabel("FRAGE", SwingConstants.RIGHT);
        r1.setFont(new Font("SansSerif", Font.BOLD, 12));
        r1.setForeground(Color.GRAY);
        questionCounterLabel = new JLabel("1 / -", SwingConstants.RIGHT);
        questionCounterLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        questionCounterLabel.setForeground(TEXT_DARK);
        pRight.add(r1);
        pRight.add(questionCounterLabel);

        statsBar.add(pLeft, BorderLayout.WEST);
        statsBar.add(pRight, BorderLayout.EAST);
        return statsBar;
    }

    private JPanel buildQuestionCard() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_SHADOW);
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 20, 20);
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, 0, 10, getHeight() - 8, 20, 20);
                g2.fillRect(6, 0, 4, getHeight() - 8);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(900, 160));
        card.setBorder(new EmptyBorder(10, 50, 10, 20));

        questionTextLabel = new JLabel("Lade Frage...");
        questionTextLabel.setFont(new Font("Serif", Font.BOLD, 28));
        questionTextLabel.setForeground(TEXT_DARK);
        card.add(questionTextLabel, BorderLayout.CENTER);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildAnswersGrid() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(900, 180));

        answerButtons[0] = new AnswerButton("A.");
        answerButtons[1] = new AnswerButton("B.");
        answerButtons[2] = new AnswerButton("C.");
        answerButtons[3] = new AnswerButton("D.");

        for (int i = 0; i < 4; i++) {
            int idx = i;
            answerButtons[i].setOnClick(() -> onAnswerClicked(idx));
            grid.add(answerButtons[i]);
        }

        wrapper.add(grid);
        return wrapper;
    }

    private JPanel buildJokerPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setOpaque(false);

        CircularButton fifty = new CircularButton("50:50", "Joker", JOKER_RING_PURPLE, JOKER_TEXT_PURPLE);
        fifty.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!used5050 && acceptingAnswers) {
                    used5050 = true;
                    apply5050();
                    fifty.setEnabledLook(false);
                }
            }
        });

        CircularButton skip = new CircularButton("▶|", "Skip", SKIP_RING_TEAL, SKIP_TEXT_TEAL);
        skip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (acceptingAnswers) nextQuestion();
            }
        });

        panel.add(fifty);
        panel.add(skip);
        return panel;
    }

    /**
     * Gibt das Hauptpanel zurück.
     * @return Das Panel.
     */
    public JPanel getMainPanel() { return mainPanel; }

    static class CircularButton extends JPanel {
        private boolean enabledLook = true;

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
        }
        void setEnabledLook(boolean enabled) { this.enabledLook = enabled; repaint(); }
    }

    static class AnswerButton extends JPanel {
        enum State { NORMAL, CORRECT, WRONG }
        private Runnable onClick;
        private State state = State.NORMAL;
        private boolean enabledLook = true;
        private final JLabel prefixLabel;
        private final JLabel textLabel;

        AnswerButton(String prefix) {
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

            if (state == State.CORRECT) g2.setColor(OK_BG);
            else if (state == State.WRONG) g2.setColor(ERROR_BG);
            else g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            if (state == State.CORRECT) { g2.setColor(OK_BORDER); g2.setStroke(new BasicStroke(2)); }
            else if (state == State.WRONG) { g2.setColor(ERROR_BORDER); g2.setStroke(new BasicStroke(2)); }
            else { g2.setColor(enabledLook ? new Color(230, 230, 230) : new Color(245, 245, 245)); g2.setStroke(new BasicStroke(1)); }
            g2.drawRoundRect(0, 0, w, h, 20, 20);
        }
    }
}