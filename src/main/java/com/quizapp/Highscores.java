package com.quizapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.quizapp.ui.components.PrimaryButton;
import java.awt.*;

public class Highscores {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    public Highscores(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(40, 0, 0, 0));

        JLabel title = new JLabel("Highscores");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(30));

        // Card container (white box)
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 1;
                int h = getHeight() - 1;

                // shadow
                g2.setColor(new Color(225, 225, 225));
                g2.fillRoundRect(3, 3, w - 6, h - 6, 20, 20);

                // background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(950, 420));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] cols = {"Rang", "Name", "Punkte"};
        Object[][] data = {
                {"1", "Istiqlal", "120"},
                {"2", "Anna", "110"},
                {"3", "Max", "95"},
                {"4", "Sara", "80"},
                {"5", "Tom", "60"}
        };

        JTable table = new JTable(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }
        };
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        card.add(scroll, BorderLayout.CENTER);

        content.add(card);
        content.add(Box.createVerticalStrut(25));


        JButton back = new PrimaryButton("Zurück");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> onBack.run());
        content.add(back);

        content.add(back);

        mainPanel.add(content, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}