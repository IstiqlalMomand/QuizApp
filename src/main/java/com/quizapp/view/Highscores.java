package com.quizapp.view;

import com.quizapp.data.DataManager;
import com.quizapp.model.HighscoreEntry;
import com.quizapp.view.components.PrimaryButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Die Highscores-Klasse ist für die Anzeige der globalen Bestenliste zuständig.
 * <p>
 * Sie präsentiert die gespeicherten Spielergebnisse in einer stilisierten Tabelle ({@link JTable}).
 * Die Klasse kümmert sich um das Laden der Daten über den {@link DataManager},
 * die Sortierung der Einträge nach Punktzahl und das visuelle Styling der Tabellenzellen.
 * </p>
 *
 * @author Istiqlal Momand
 * @author Helal Storany
 * @version 1.0
 */
public class Highscores {
    private final JPanel mainPanel;
    private final Runnable onBack;
    private DefaultTableModel tableModel;

    private static final Color BG_COLOR = new Color(250, 251, 252);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TABLE_HEADER_BG = new Color(245, 247, 250);

    /**
     * Erstellt die Highscore-Ansicht und initialisiert das UI-Layout.
     *
     * @param onBack Callback-Funktion für den "Zurück"-Button.
     */
    public Highscores(Runnable onBack) {
        this.onBack = onBack;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Card Container
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.setColor(new Color(225, 225, 225)); g2.fillRoundRect(3, 3, w - 6, h - 6, 20, 20);
                g2.setColor(Color.WHITE); g2.fillRoundRect(0, 0, w - 6, h - 6, 20, 20);
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(1050, 600));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 10, 15, 10));

        JLabel title = new JLabel("🏆  Bestenliste");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        card.add(topBar, BorderLayout.NORTH);

        // ===== Table Setup =====
        String[] cols = {"RANG", "SPIELER", "PUNKTE", "DATUM"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(55); // Taller rows
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setForeground(TEXT_DARK);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        // Disable that ugly blue selection highlight
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        // --- Column Styling ---
        TableColumnModel cm = table.getColumnModel();

        // 1. RANK (Small, Centered, Gray)
        cm.getColumn(0).setPreferredWidth(60);
        cm.getColumn(0).setMaxWidth(80);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setForeground(Color.GRAY);
        cm.getColumn(0).setCellRenderer(centerRenderer);

        // 2. PLAYER (Takes remaining space, Bold)
        cm.getColumn(1).setPreferredWidth(400);
        cm.getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                l.setBorder(new EmptyBorder(0, 10, 0, 0)); // Padding Left
                return l;
            }
        });

        // 3. POINTS (Right aligned, Blue)
        cm.getColumn(2).setPreferredWidth(120);
        cm.getColumn(2).setMaxWidth(150);
        DefaultTableCellRenderer pointsRenderer = new DefaultTableCellRenderer();
        pointsRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        pointsRenderer.setForeground(new Color(60, 86, 140)); // Corporate Blue
        pointsRenderer.setFont(new Font("SansSerif", Font.BOLD, 16));
        cm.getColumn(2).setCellRenderer(pointsRenderer);

        // 4. DATE (Right aligned, Gray)
        cm.getColumn(3).setPreferredWidth(150);
        cm.getColumn(3).setMaxWidth(200);
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer();
        dateRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        dateRenderer.setForeground(Color.GRAY);
        cm.getColumn(3).setCellRenderer(dateRenderer);

        // --- Header Styling ---
        JTableHeader th = table.getTableHeader();
        th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0, 45)); // Taller Header
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setForeground(new Color(100, 100, 100));
        th.setBackground(TABLE_HEADER_BG);
        ((DefaultTableCellRenderer)th.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230))); // Top line only
        tableScroll.getViewport().setBackground(Color.WHITE);

        card.add(tableScroll, BorderLayout.CENTER);

        // Initial Load
        refresh();

        // Bottom bar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton back = new PrimaryButton("Zurück");
        back.addActionListener(e -> onBack.run());
        bottom.add(back);
        card.add(bottom, BorderLayout.SOUTH);

        content.add(card);
        JScrollPane pageScroll = new JScrollPane(content);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(pageScroll, BorderLayout.CENTER);
    }

    /**
     * Lädt die aktuellen Highscore-Daten neu und aktualisiert die Tabelle.
     * <p>
     * Diese Methode:
     * </p>
     * <ol>
     * <li>Leert das aktuelle Tabellenmodell.</li>
     * <li>Lädt die Liste aller Einträge vom {@link DataManager}.</li>
     * <li>Sortiert die Liste absteigend nach Punkten (Höchster Score zuerst).</li>
     * <li>Fügt die sortierten Einträge zeilenweise der Tabelle hinzu.</li>
     * </ol>
     */
    public void refresh() {
        tableModel.setRowCount(0);
        List<HighscoreEntry> entries = DataManager.loadHighscores();

        // Sort: Highest score first
        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Limit to Top 50 to keep it clean? (Optional, currently shows all)
        for (int i = 0; i < entries.size(); i++) {
            HighscoreEntry e = entries.get(i);
            tableModel.addRow(new Object[]{
                    (i + 1) + ".",   // Rank format "1."
                    e.getPlayerName(),
                    e.getScore(),
                    e.getDate()
            });
        }
    }

    /**
     * Gibt das Haupt-Panel zurück.
     *
     * @return Das JPanel für das CardLayout.
     */
    public JPanel getMainPanel() { return mainPanel; }
}