package com.quizapp.ui.components;

import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderBar extends JPanel {

    private final JLabel titleLabel;
    private final JLabel userLabel;
    private final JButton logoutButton;

    public HeaderBar(String appTitle) {
        setLayout(new BorderLayout());
        setBackground(new Color(11, 43, 90)); // navy
        setBorder(new EmptyBorder(18, 28, 18, 28));
        setPreferredSize(new Dimension(100, 85));

        titleLabel = new JLabel(appTitle);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // looks clickable
        add(titleLabel, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        right.setOpaque(false);

        userLabel = new JLabel("Angemeldet als: -");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        logoutButton = new JButton("Abmelden");

        // macOS consistent painting
        logoutButton.setUI(new BasicButtonUI());
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(false);

        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(60, 86, 140));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(new EmptyBorder(8, 16, 8, 16));

        right.add(userLabel);
        right.add(logoutButton);

        add(right, BorderLayout.EAST);
    }

    public void setUsername(String username) {
        userLabel.setText("Angemeldet als: " + (username == null ? "-" : username));
    }

    public void onLogout(Runnable action) {
        logoutButton.addActionListener(e -> action.run());
    }

    // ✅ NEW: clicking the title goes "home"
    public void onHome(Runnable action) {
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }
}