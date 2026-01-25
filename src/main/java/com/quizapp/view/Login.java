package com.quizapp.view;

import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login {
    private final JPanel mainPanel;
    private final JTextField usernameField;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(120, 120, 120);

    public Login(java.util.function.Consumer<String> onLoginSuccess) {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);

        // Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(520, 330));

        JLabel title = new JLabel("Willkommen");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Bitte geben Sie Ihren Namen ein, um zu starten.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        JLabel label = new JLabel("Benutzername");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        card.add(label);
        card.add(Box.createVerticalStrut(8));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(18));

        JButton loginBtn = new PrimaryButton("Anmelden  →");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Press Enter also logs in
        usernameField.addActionListener(e -> loginBtn.doClick());

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText() == null ? "" : usernameField.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Bitte geben Sie einen Benutzernamen ein.");
                usernameField.requestFocusInWindow();
                return;
            }

            // Optional: restrict very short names
            if (username.length() < 2) {
                JOptionPane.showMessageDialog(mainPanel, "Benutzername ist zu kurz (mind. 2 Zeichen).");
                usernameField.requestFocusInWindow();
                return;
            }

            onLoginSuccess.accept(username);
        });

        card.add(loginBtn);
        card.add(Box.createVerticalStrut(18));

        JLabel footer = new JLabel("Kein Passwort erforderlich • Offline Modus");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(new Color(150, 150, 150));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(footer);

        // Shadow wrapper
        JPanel shadowWrap = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 18, 18);
            }
        };
        shadowWrap.setOpaque(false);
        shadowWrap.setBorder(new EmptyBorder(0, 0, 0, 0));
        shadowWrap.add(card);

        mainPanel.add(shadowWrap);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void focusUsername() {
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
}