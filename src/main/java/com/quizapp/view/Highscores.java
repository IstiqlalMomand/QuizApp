package com.quizapp.view;

import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Highscores {
    private final JPanel mainPanel;
    private final Runnable onBack;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    public Highscores(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Content wrapper
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Card container (white box)
        JPanel card = new JPanel(new BorderLayout()) {
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
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(1050, 600));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Top bar (Title only) =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(6, 6, 12, 6));

        JLabel title = new JLabel("🏆  Bestenliste");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT_DARK);

        topBar.add(title, BorderLayout.WEST);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(235, 235, 235));

        JPanel headerWrap = new JPanel();
        headerWrap.setLayout(new BoxLayout(headerWrap, BoxLayout.Y_AXIS));
        headerWrap.setOpaque(false);
        headerWrap.add(topBar);
        headerWrap.add(sep);

        card.add(headerWrap, BorderLayout.NORTH);

        // ===== Table =====
        String[] cols = {"RANG", "SPIELER", "PUNKTE", "DATUM"};
        Object[][] data = {
                {"#1", "Prof. Klatt", 1500, LocalDate.of(2025, 11, 1)},
                {"#2", "Student_A", 1200, LocalDate.of(2025, 11, 10)},
                {"#3", "QuizMaster", 950, LocalDate.of(2025, 11, 15)},
                {"#4", "Istiqlal", 820, LocalDate.of(2026, 1, 10)},
                {"#5", "Helal", 760, LocalDate.of(2026, 1, 12)}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class;   // Punkte sort numeric
                if (columnIndex == 3) return LocalDate.class; // Date sort
                return String.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(52);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setForeground(TEXT_DARK);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader th = table.getTableHeader();
        th.setReorderingAllowed(false);
        th.setFont(new Font("SansSerif", Font.BOLD, 12));
        th.setForeground(new Color(80, 80, 80));
        th.setBackground(new Color(245, 246, 248));

        // Right align Punkte
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(right);

        // Date formatting
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DefaultTableCellRenderer dateR = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof LocalDate d) setText(d.format(fmt));
                else super.setValue(value);
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(dateR);

        // Sorting (default: Punkte desc)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.toggleSortOrder(2); // asc
        sorter.toggleSortOrder(2); // desc

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setBackground(Color.WHITE);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setOpaque(false);
        tableWrap.setBorder(new EmptyBorder(14, 6, 6, 6));
        tableWrap.add(tableScroll, BorderLayout.CENTER);

        card.add(tableWrap, BorderLayout.CENTER);

        // Bottom bar (Zurück)
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(14, 6, 6, 6));

        JButton back = new PrimaryButton("Zurück");
        back.addActionListener(e -> onBack.run());
        bottom.add(back);

        card.add(bottom, BorderLayout.SOUTH);

        content.add(card);

        // Page scroll wrapper (for smaller windows)
        JScrollPane pageScroll = new JScrollPane(content);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}