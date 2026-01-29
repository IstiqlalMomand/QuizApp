package com.quizapp.view;

import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class Login {
    private JPanel mainPanel;
    private JTextField usernameField;
    private final Consumer<String> onLogin;

    // Brand Colors
    private static final Color BRAND_NAVY = new Color(13, 44, 94);
    private static final Color BG_GRAY = new Color(245, 247, 250);
    private static final Color CARD_SHADOW = new Color(200, 200, 200, 100);

    public Login(Consumer<String> onLogin) {
        this.onLogin = onLogin;

        // 1. Custom Background Panel (Split Color)
        mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int h = getHeight();
                int w = getWidth();

                // Top 40% Navy
                g2.setColor(BRAND_NAVY);
                g2.fillRect(0, 0, w, h * 40 / 100);

                // Bottom 60% Light Gray
                g2.setColor(BG_GRAY);
                g2.fillRect(0, h * 40 / 100, w, h * 60 / 100);
            }
        };

        // 2. The Floating Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 50, 40, 50)); // Internal padding

        // Add a shadow/border effect using a wrapper or compound border
        // (Simplified here: specific background painting for shadow effect is complex in Swing,
        // so we use a matte border to simulate depth or just keep it clean).
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));

        // --- Logo Icon (Drawn in code) ---
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Blue Circle
                g2.setColor(new Color(240, 245, 255));
                g2.fillOval(0, 0, 60, 60);

                // Draw "Q" text
                g2.setColor(BRAND_NAVY);
                g2.setFont(new Font("Serif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                String text = "Q";
                int x = (60 - fm.stringWidth(text)) / 2;
                int y = ((60 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
            }
        };
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setMaximumSize(new Dimension(60, 60));
        iconPanel.setBackground(Color.WHITE); // match card
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Texts ---
        JLabel title = new JLabel("QuizApp");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(BRAND_NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Bitte melden Sie sich an");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Input Field ---
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 40));
        usernameField.setMaximumSize(new Dimension(250, 40));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        installPlaceholder(usernameField, "Benutzername eingeben...");
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Button ---
        JButton loginBtn = new PrimaryButton("Starten");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(250, 40));
        loginBtn.addActionListener(e -> performLogin());

        // Allow "Enter" key to trigger login
        usernameField.addActionListener(e -> performLogin());

        // --- Assemble Card ---
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(15));

        JLabel footer = new JLabel("Offline Modus • Keine Registrierung");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(new Color(150, 150, 150));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footer);

        // Add Card to Main Panel (Centered)
        mainPanel.add(card);
    }

    private void performLogin() {
        String name = usernameField.getText().trim();
        if (name.isEmpty() || name.equals("Benutzername eingeben...")) {
            JOptionPane.showMessageDialog(mainPanel, "Bitte einen Namen eingeben.", "Info", JOptionPane.WARNING_MESSAGE);
        } else {
            onLogin.accept(name);
        }
    }

    // Reuse your placeholder logic
    private void installPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    public void focusUsername() {
        // Request focus but don't clear placeholder immediately logic is handled by listener
        usernameField.requestFocusInWindow();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}