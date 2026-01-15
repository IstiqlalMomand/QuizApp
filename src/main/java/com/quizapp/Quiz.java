package com.quizapp;

import com.quizapp.ui.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Quiz {
    private JPanel mainPanel;
    private final Runnable onBackToMenu;

    // --- COLORS ---
    private static final Color HEADER_BG = new Color(13, 44, 94);       // navy
    private static final Color BG_COLOR = new Color(250, 251, 252);     // light background
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    // Joker colors
    private static final Color JOKER_RING_PURPLE = new Color(225, 200, 255);
    private static final Color JOKER_TEXT_PURPLE = new Color(120, 50, 180);
    private static final Color SKIP_RING_TEAL = new Color(180, 240, 230);
    private static final Color SKIP_TEXT_TEAL = new Color(0, 150, 130);

    public Quiz(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // CONTENT AREA
        JPanel contentBody = new JPanel();
        contentBody.setLayout(new BoxLayout(contentBody, BoxLayout.Y_AXIS));
        contentBody.setBackground(BG_COLOR);
        contentBody.setBorder(new EmptyBorder(0, 0, 40, 0));

        // --- A) STATS BAR ---
        JPanel statsBar = new JPanel(new BorderLayout());
        statsBar.setBackground(Color.WHITE);
        statsBar.setBorder(new EmptyBorder(15, 50, 15, 50));
        statsBar.setMaximumSize(new Dimension(2000, 70));

        JPanel pLeft = new JPanel(new GridLayout(2, 1));
        pLeft.setOpaque(false);
        JLabel l1 = new JLabel("PUNKTE");
        l1.setFont(new Font("SansSerif", Font.BOLD, 12));
        l1.setForeground(Color.GRAY);
        JLabel l2 = new JLabel("100");
        l2.setFont(new Font("SansSerif", Font.BOLD, 24));
        l2.setForeground(TEXT_DARK);
        pLeft.add(l1);
        pLeft.add(l2);

        JPanel pRight = new JPanel(new GridLayout(2, 1));
        pRight.setOpaque(false);
        JLabel r1 = new JLabel("FRAGE", SwingConstants.RIGHT);
        r1.setFont(new Font("SansSerif", Font.BOLD, 12));
        r1.setForeground(Color.GRAY);
        JLabel r2 = new JLabel("2 / 10", SwingConstants.RIGHT);
        r2.setFont(new Font("SansSerif", Font.BOLD, 24));
        r2.setForeground(TEXT_DARK);
        pRight.add(r1);
        pRight.add(r2);

        statsBar.add(pLeft, BorderLayout.WEST);
        statsBar.add(pRight, BorderLayout.EAST);

        contentBody.add(statsBar);
        contentBody.add(Box.createVerticalStrut(40));

        // --- B) QUESTION CARD ---
        JPanel qCardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        qCardWrapper.setOpaque(false);

        JPanel qCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // shadow
                g2.setColor(new Color(230, 230, 230));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);

                // white background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 20, 20);

                // left navy bar
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, 0, 10, getHeight() - 8, 20, 20);
                g2.fillRect(6, 0, 4, getHeight() - 8);
            }
        };
        qCard.setOpaque(false);
        qCard.setPreferredSize(new Dimension(900, 140));
        qCard.setBorder(new EmptyBorder(0, 50, 0, 20));

        JLabel lblQ = new JLabel("Was beschreibt die 'Usability' einer Software?");
        lblQ.setFont(new Font("Serif", Font.BOLD, 32));
        lblQ.setForeground(TEXT_DARK);
        qCard.add(lblQ, BorderLayout.CENTER);

        qCardWrapper.add(qCard);
        contentBody.add(qCardWrapper);
        contentBody.add(Box.createVerticalStrut(30));

        // --- C) ANSWERS GRID ---
        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gridWrapper.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(900, 180));

        grid.add(new AnswerButton("A.", "Die Geschwindigkeit"));
        grid.add(new AnswerButton("B.", "Die Fehlerfreiheit"));
        grid.add(new AnswerButton("C.", "Die Bedienbarkeit"));
        grid.add(new AnswerButton("D.", "Die Sicherheit"));

        gridWrapper.add(grid);
        contentBody.add(gridWrapper);
        contentBody.add(Box.createVerticalStrut(40));

        // --- D) JOKER BUTTONS ---
        JPanel jokerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        jokerPanel.setOpaque(false);

        jokerPanel.add(new CircularButton("50:50", "Joker", JOKER_RING_PURPLE, JOKER_TEXT_PURPLE));
        jokerPanel.add(new CircularButton("▶|", "Skip", SKIP_RING_TEAL, SKIP_TEXT_TEAL));

        contentBody.add(jokerPanel);

        // ✅ BACK BUTTON (new)
        contentBody.add(Box.createVerticalStrut(25));

        JButton back = new PrimaryButton("Zurück");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> onBackToMenu.run());

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backWrap.setOpaque(false);
        backWrap.add(back);

        contentBody.add(backWrap);

        mainPanel.add(contentBody, BorderLayout.CENTER);
    }

    // --- COMPONENTS ---

    /** Round joker buttons */
    static class CircularButton extends JPanel {
        CircularButton(String symbol, String label, Color ringColor, Color textColor) {
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(new Color(ringColor.getRed(), ringColor.getGreen(), ringColor.getBlue(), 50));
                    g2.fillOval(0, 0, getWidth(), getHeight());

                    g2.setColor(ringColor);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
                }
            };
            circle.setOpaque(false);
            circle.setPreferredSize(new Dimension(60, 60));
            circle.setLayout(new GridBagLayout());

            JLabel sym = new JLabel(symbol);
            sym.setFont(new Font("SansSerif", Font.BOLD, 14));
            sym.setForeground(textColor);
            circle.add(sym);

            JLabel lbl = new JLabel(label, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(Color.GRAY);
            lbl.setBorder(new EmptyBorder(5, 0, 0, 0));

            add(circle, BorderLayout.CENTER);
            add(lbl, BorderLayout.SOUTH);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    /** Answer cards */
    static class AnswerButton extends JPanel {
        private boolean isSelected = false;

        AnswerButton(String prefix, String text) {
            setLayout(new BorderLayout(15, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(0, 25, 0, 10));

            JLabel p = new JLabel(prefix);
            p.setFont(new Font("SansSerif", Font.BOLD, 16));
            p.setForeground(TEXT_DARK);

            JLabel t = new JLabel(text);
            t.setFont(new Font("SansSerif", Font.PLAIN, 16));
            t.setForeground(new Color(80, 80, 80));

            add(p, BorderLayout.WEST);
            add(t, BorderLayout.CENTER);

            if (prefix.equals("B.")) isSelected = true; // demo selected

            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isSelected = !isSelected;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - 1;
            int h = getHeight() - 1;

            g2.setColor(new Color(220, 220, 220));
            g2.fillRoundRect(2, 2, w, h, 20, 20);

            if (isSelected) g2.setColor(new Color(235, 245, 255));
            else g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            if (isSelected) {
                g2.setColor(new Color(100, 150, 255));
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setColor(new Color(230, 230, 230));
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawRoundRect(0, 0, w, h, 20, 20);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}