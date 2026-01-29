package com.quizapp.view;

import com.quizapp.data.DataManager;
import com.quizapp.model.Question;
import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    // Inputs
    private JTextArea questionArea;
    private JTextField ansA, ansB, ansC, ansD;
    private JButton saveButton;
    private JPanel listBody;

    //  STATE: -1 means "New Question", otherwise it's the index we are editing
    private int editingIndex = -1;

    public Admin(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // ===== Page Content =====
        JPanel contentBody = new JPanel();
        contentBody.setLayout(new BoxLayout(contentBody, BoxLayout.Y_AXIS));
        contentBody.setBackground(BG_COLOR);
        contentBody.setBorder(new EmptyBorder(35, 60, 40, 60));

        // ===== Title row =====
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel title = new JLabel("Verwaltung");
        title.setFont(new Font("Serif", Font.BOLD, 46));
        title.setForeground(TEXT_DARK);

        JButton backTop = new PrimaryButton("Zurück");
        backTop.addActionListener(e -> {
            resetForm(); // Clear inputs when leaving
            onBack.run();
        });

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        backWrap.setOpaque(false);
        backWrap.add(backTop);

        topRow.add(title, BorderLayout.WEST);
        topRow.add(backWrap, BorderLayout.EAST);

        contentBody.add(topRow);
        contentBody.add(Box.createVerticalStrut(25));

        // ===== Card: Frage Editor =====
        JPanel addCard = createWhiteCard();
        addCard.setLayout(new BoxLayout(addCard, BoxLayout.Y_AXIS));
        addCard.setBorder(new EmptyBorder(30, 35, 30, 35));
        addCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCard.setMaximumSize(new Dimension(1100, 480));

        JLabel cardTitle = new JLabel("Frage bearbeiten / hinzufügen");
        cardTitle.setFont(new Font("Serif", Font.BOLD, 28));
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        addCard.add(cardTitle);
        addCard.add(Box.createVerticalStrut(20));

        // --- FRAGETEXT ---
        addCard.add(smallSectionLabel("FRAGETEXT"));
        addCard.add(Box.createVerticalStrut(5));

        questionArea = new JTextArea(3, 40);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        questionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        installPlaceholder(questionArea, "Geben Sie hier die Frage ein...");
        addCard.add(questionArea);
        addCard.add(Box.createVerticalStrut(20));

        // ===== Answers grid =====
        JPanel answersGrid = new JPanel(new GridLayout(2, 2, 20, 15));
        answersGrid.setOpaque(false);
        answersGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        ansA = createInput("ANTWORT A (KORREKT)", "Richtige Antwort");
        ansB = createInput("ANTWORT B", "Falsche Antwort 1");
        ansC = createInput("ANTWORT C", "Falsche Antwort 2");
        ansD = createInput("ANTWORT D", "Falsche Antwort 3");

        answersGrid.add(wrapField(ansA));
        answersGrid.add(wrapField(ansB));
        answersGrid.add(wrapField(ansC));
        answersGrid.add(wrapField(ansD));

        addCard.add(answersGrid);
        addCard.add(Box.createVerticalStrut(20));

        // Buttons Row (Save & Cancel)
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        JButton cancelBtn = new JButton("Abbrechen");
        cancelBtn.addActionListener(e -> resetForm());

        saveButton = new JButton("Speichern");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setBackground(new Color(25, 135, 84));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveQuestion());

        btnRow.add(cancelBtn);
        btnRow.add(saveButton);
        addCard.add(btnRow);

        contentBody.add(addCard);
        contentBody.add(Box.createVerticalStrut(30));

        // ===== Card: Vorhandene Fragen =====
        JPanel listCard = createWhiteCard();
        listCard.setLayout(new BorderLayout());
        listCard.setBorder(new EmptyBorder(24, 28, 24, 28));
        listCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        listCard.setMaximumSize(new Dimension(1100, 500));

        JLabel listTitle = new JLabel("Vorhandene Fragen");
        listTitle.setFont(new Font("Serif", Font.BOLD, 26));
        listTitle.setForeground(TEXT_DARK);
        listCard.add(listTitle, BorderLayout.NORTH);

        listBody = new JPanel();
        listBody.setLayout(new BoxLayout(listBody, BoxLayout.Y_AXIS));
        listBody.setOpaque(false);
        listBody.setBorder(new EmptyBorder(15, 0, 0, 0));

        JScrollPane innerScroll = new JScrollPane(listBody);
        innerScroll.setBorder(null);
        innerScroll.getVerticalScrollBar().setUnitIncrement(16);

        listCard.add(innerScroll, BorderLayout.CENTER);
        contentBody.add(listCard);

        JScrollPane pageScroll = new JScrollPane(contentBody);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);

        refreshQuestionList(); // Load initial data
    }

    // --- LOGIC ---

    private void saveQuestion() {
        String qText = questionArea.getText().trim();
        if (isPlaceholder(questionArea) || qText.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Bitte eine Frage eingeben."); return;
        }
        if (isPlaceholder(ansA) || isPlaceholder(ansB) || isPlaceholder(ansC) || isPlaceholder(ansD)) {
            JOptionPane.showMessageDialog(mainPanel, "Bitte alle Antworten ausfüllen."); return;
        }

        // Logic: Answer A is always the correct one in the FORM
        String[] options = { ansA.getText().trim(), ansB.getText().trim(), ansC.getText().trim(), ansD.getText().trim() };
        Question newQ = new Question(qText, options, 0); // 0 = A is correct

        if (editingIndex == -1) {
            // CREATE NEW
            DataManager.saveQuestion(newQ);
        } else {
            // UPDATE EXISTING
            DataManager.updateQuestion(editingIndex, newQ);
            JOptionPane.showMessageDialog(mainPanel, "Frage aktualisiert!");
        }

        resetForm();
        refreshQuestionList();
    }

    private void editQuestion(int index, Question q) {
        this.editingIndex = index;
        saveButton.setText("Aktualisieren"); // Change button text
        saveButton.setBackground(new Color(13, 110, 253)); // Blue for Update

        questionArea.setForeground(Color.BLACK);
        questionArea.setText(q.getText());

        // We need to put the CORRECT answer into Field A, and the rest in B, C, D
        String[] opts = q.getOptions();
        int correct = q.getCorrectIndex();

        setTextNoPlaceholder(ansA, opts[correct]);

        // Fill B, C, D with the remaining options
        List<String> wrongAnswers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correct) wrongAnswers.add(opts[i]);
        }

        setTextNoPlaceholder(ansB, wrongAnswers.get(0));
        setTextNoPlaceholder(ansC, wrongAnswers.get(1));
        setTextNoPlaceholder(ansD, wrongAnswers.get(2));

        // Scroll to top to see the edit form
        ((JScrollPane) mainPanel.getComponent(0)).getVerticalScrollBar().setValue(0);
    }

    private void deleteQuestion(int index) {
        int confirm = JOptionPane.showConfirmDialog(mainPanel,
                "Möchten Sie diese Frage wirklich löschen?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DataManager.deleteQuestion(index);
            if (editingIndex == index) resetForm(); // If we were editing this one, cancel edit
            refreshQuestionList();
        }
    }

    private void resetForm() {
        editingIndex = -1;
        saveButton.setText("Speichern");
        saveButton.setBackground(new Color(25, 135, 84)); // Green for Save

        clearInput(questionArea, "Geben Sie hier die Frage ein...");
        clearInput(ansA, "Richtige Antwort");
        clearInput(ansB, "Falsche Antwort 1");
        clearInput(ansC, "Falsche Antwort 2");
        clearInput(ansD, "Falsche Antwort 3");
    }

    private void refreshQuestionList() {
        listBody.removeAll();
        List<Question> all = DataManager.loadQuestions();

        if (all.isEmpty()) {
            listBody.add(new JLabel("Keine Fragen gefunden."));
        } else {
            for (int i = 0; i < all.size(); i++) {
                listBody.add(createListRow(i, all.get(i)));
                listBody.add(Box.createVerticalStrut(10));
            }
        }
        listBody.revalidate();
        listBody.repaint();
    }

    // --- UI HELPERS ---

    private JPanel createListRow(int index, Question q) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel("<html><b>" + (index + 1) + ".</b> " + q.getText() + "</html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(TEXT_DARK);

        // Buttons Panel
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        JButton edit = new JButton("✏️");
        edit.setToolTipText("Bearbeiten");
        edit.addActionListener(e -> editQuestion(index, q));

        JButton delete = new JButton("🗑️");
        delete.setToolTipText("Löschen");
        delete.setForeground(Color.RED);
        delete.addActionListener(e -> deleteQuestion(index));

        actions.add(edit);
        actions.add(delete);

        row.add(lbl, BorderLayout.CENTER);
        row.add(actions, BorderLayout.EAST);
        return row;
    }

    private JPanel createWhiteCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(225, 225, 225)); g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 20, 20);
                g2.setColor(Color.WHITE); g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 20, 20);
            }
        };
    }

    private JTextField createInput(String label, String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 16));
        installPlaceholder(f, placeholder);
        f.putClientProperty("LABEL", label);
        return f;
    }

    private JPanel wrapField(JTextField field) {
        JPanel w = new JPanel(); w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS)); w.setOpaque(false);
        JLabel l = smallSectionLabel((String) field.getClientProperty("LABEL"));
        l.setAlignmentX(Component.LEFT_ALIGNMENT); field.setAlignmentX(Component.LEFT_ALIGNMENT);
        w.add(l); w.add(Box.createVerticalStrut(5)); w.add(field);
        return w;
    }

    private JLabel smallSectionLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("SansSerif", Font.BOLD, 12)); l.setForeground(Color.GRAY); return l;
    }

    // Placeholder Logic
    private void installPlaceholder(JTextComponent c, String p) {
        c.setText(p); c.setForeground(Color.GRAY);
        c.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(c.getText().equals(p)) { c.setText(""); c.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if(c.getText().trim().isEmpty()) { c.setText(p); c.setForeground(Color.GRAY); } }
        });
    }
    private void clearInput(JTextComponent c, String p) { c.setText(p); c.setForeground(Color.GRAY); }
    private void setTextNoPlaceholder(JTextComponent c, String t) { c.setText(t); c.setForeground(Color.BLACK); }
    private boolean isPlaceholder(JTextComponent c) { return c.getForeground().equals(Color.GRAY); }

    public JPanel getMainPanel() { return mainPanel; }
}