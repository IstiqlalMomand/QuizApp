package com.quizapp.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class PrimaryButton extends JButton {

    public PrimaryButton(String text) {
        super(text);

        // consistent on macOS
        setUI(new BasicButtonUI());
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);

        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBackground(new Color(60, 86, 140));
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(10, 18, 10, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}