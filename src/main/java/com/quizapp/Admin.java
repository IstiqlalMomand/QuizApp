package com.quizapp;

import com.quizapp.ui.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Admin {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    public Admin(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // ===== Page Content (scrollable) =====
        JPanel contentBody = new JPanel();
        contentBody.setLayout(new BoxLayout(contentBody, BoxLayout.Y_AXIS));
        contentBody.setBackground(BG_COLOR);
        contentBody.setBorder(new EmptyBorder(35, 60, 40, 60));

        // ===== Title row (left title, right back button) =====
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel title = new JLabel("Verwaltung");
        title.setFont(new Font("Serif", Font.BOLD, 46));
        title.setForeground(TEXT_DARK);

        JButton backTop = new PrimaryButton("Zurück");
        backTop.addActionListener(e -> onBack.run());

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        backWrap.setOpaque(false);
        backWrap.add(backTop);

        topRow.add(title, BorderLayout.WEST);
        topRow.add(backWrap, BorderLayout.EAST);

        contentBody.add(topRow);
        contentBody.add(Box.createVerticalStrut(25));

        // ===== Card: Neue Frage hinzufügen =====
        JPanel addCard = createWhiteCard();
        addCard.setLayout(new BoxLayout(addCard, BoxLayout.Y_AXIS));
        addCard.setBorder(new EmptyBorder(30, 35, 30, 35));
        addCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCard.setMaximumSize(new Dimension(1100, 430));

        JLabel cardTitle = new JLabel("Neue Frage hinzufügen");
        cardTitle.setFont(new Font("Serif", Font.BOLD, 32));
        cardTitle.setForeground(TEXT_DARK);
        cardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        addCard.add(cardTitle);
        addCard.add(Box.createVerticalStrut(18));
        addCard.add(sep);
        addCard.add(Box.createVerticalStrut(18));

        // --- FRAGETEXT label + textarea
        JLabel qLabel = smallSectionLabel("FRAGETEXT");
        qLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCard.add(qLabel);
        addCard.add(Box.createVerticalStrut(10));

        JTextArea question = new JTextArea(3, 40);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(new Font("SansSerif", Font.PLAIN, 16));
        question.setBackground(Color.WHITE);
        question.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        question.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // ✅ Placeholder behaviour
        installPlaceholder(question, "Geben Sie hier die Frage ein...");

        addCard.add(question);
        addCard.add(Box.createVerticalStrut(22));

        // ===== Answers grid =====
        JPanel answersGrid = new JPanel(new GridLayout(2, 2, 22, 18));
        answersGrid.setOpaque(false);
        answersGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        JTextField a = createInput("ANTWORT A (KORREKT)", "Richtige Antwort");
        JTextField b = createInput("ANTWORT B", "Falsche Antwort 1");
        JTextField c = createInput("ANTWORT C", "Falsche Antwort 2");
        JTextField d = createInput("ANTWORT D", "Falsche Antwort 3");

        answersGrid.add(wrapField(a));
        answersGrid.add(wrapField(b));
        answersGrid.add(wrapField(c));
        answersGrid.add(wrapField(d));

        addCard.add(answersGrid);
        addCard.add(Box.createVerticalStrut(22));

        // Save button aligned right
        JPanel saveRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        saveRow.setOpaque(false);

        JButton save = new JButton("Speichern");
        save.setFont(new Font("SansSerif", Font.BOLD, 14));
        save.setBackground(new Color(25, 135, 84));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        save.setCursor(new Cursor(Cursor.HAND_CURSOR));

        save.addActionListener(e -> {
            // Example: read data without placeholder text
            String qText = question.getText().trim();
            if (qText.equals("Geben Sie hier die Frage ein...")) qText = "";

            // You can later validate and add to list model.
            JOptionPane.showMessageDialog(mainPanel,
                    "Gespeichert (Mockup)\n\nFrage: " + (qText.isEmpty() ? "(leer)" : qText),
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        saveRow.add(save);
        addCard.add(saveRow);

        contentBody.add(addCard);
        contentBody.add(Box.createVerticalStrut(28));

        // ===== Card: Vorhandene Fragen (Vorschau) =====
        JPanel listCard = createWhiteCard();
        listCard.setLayout(new BorderLayout());
        listCard.setBorder(new EmptyBorder(24, 28, 24, 28));
        listCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        listCard.setMaximumSize(new Dimension(1100, 520));

        JLabel listTitle = new JLabel("Vorhandene Fragen (Vorschau)");
        listTitle.setFont(new Font("Serif", Font.BOLD, 30));
        listTitle.setForeground(TEXT_DARK);

        JPanel listTitleRow = new JPanel(new BorderLayout());
        listTitleRow.setOpaque(false);
        listTitleRow.add(listTitle, BorderLayout.WEST);

        listCard.add(listTitleRow, BorderLayout.NORTH);

        // List content (scroll inside card)
        JPanel listBody = new JPanel();
        listBody.setLayout(new BoxLayout(listBody, BoxLayout.Y_AXIS));
        listBody.setOpaque(false);
        listBody.setBorder(new EmptyBorder(18, 0, 0, 0));

        listBody.add(questionRow("1. Was ist das Hauptziel des 'Waterfall Model'?", "Edit"));
        listBody.add(Box.createVerticalStrut(10));
        listBody.add(questionRow("2. Wofür steht SDLC?", "Edit"));
        listBody.add(Box.createVerticalStrut(10));
        listBody.add(questionRow("3. Welches ist KEINE agile Methode?", "Edit"));
        listBody.add(Box.createVerticalStrut(10));
        listBody.add(questionRow("4. Was bezeichnet 'White Box Testing'?", "Edit"));

        JScrollPane innerScroll = new JScrollPane(listBody);
        innerScroll.setBorder(BorderFactory.createEmptyBorder());
        innerScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        innerScroll.getVerticalScrollBar().setUnitIncrement(16);

        listCard.add(innerScroll, BorderLayout.CENTER);

        contentBody.add(listCard);
        contentBody.add(Box.createVerticalStrut(28));

        // Bottom back button (center)
        JButton backBottom = new PrimaryButton("Zurück");
        backBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBottom.addActionListener(e -> onBack.run());
        contentBody.add(backBottom);

        // ===== Page Scroll wrapper =====
        JScrollPane pageScroll = new JScrollPane(contentBody);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    // ================= Helpers =================

    private JPanel createWhiteCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // shadow
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, w - 6, h - 6, 20, 20);

                // background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);
            }
        };
    }

    private JLabel smallSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(new Color(120, 120, 120));
        return l;
    }

    private JTextField createInput(String label, String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235)),
                new EmptyBorder(12, 12, 12, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        installPlaceholder(field, placeholder);

        // store label in client property so wrapField can show it
        field.putClientProperty("LABEL", label);
        return field;
    }

    private JPanel wrapField(JTextField field) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);

        String label = (String) field.getClientProperty("LABEL");
        JLabel l = smallSectionLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrap.add(l);
        wrap.add(Box.createVerticalStrut(8));
        wrap.add(field);
        return wrap;
    }

    private JPanel questionRow(String text, String btnText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(245, 245, 245));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 235, 235)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 18));
        l.setForeground(new Color(30, 30, 30));

        JButton edit = new JButton(btnText);
        edit.setFont(new Font("SansSerif", Font.PLAIN, 14));
        edit.setBackground(new Color(220, 235, 255));
        edit.setForeground(new Color(30, 80, 180));
        edit.setFocusPainted(false);
        edit.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        edit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        row.add(l, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(edit);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    // ✅ placeholder behavior: clears on focus, restores if empty
    private void installPlaceholder(JTextComponent comp, String placeholder) {
        Color placeholderColor = new Color(140, 140, 140);
        Color textColor = new Color(30, 30, 30);

        comp.setText(placeholder);
        comp.setForeground(placeholderColor);

        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (comp.getText().equals(placeholder)) {
                    comp.setText("");
                    comp.setForeground(textColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (comp.getText().trim().isEmpty()) {
                    comp.setText(placeholder);
                    comp.setForeground(placeholderColor);
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}