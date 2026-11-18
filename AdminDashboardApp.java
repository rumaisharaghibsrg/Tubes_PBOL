import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class AdminDashboardApp {

    // Palet warna elegan Kelompok 6
    static final Color DUSTY_ROSE  = new Color(200, 135, 137);
    static final Color HEADER_ROSE = new Color(214, 153, 155);
    static final Color SOFT_PINK   = new Color(254, 226, 228);
    static final Color LIGHT_CREAM = new Color(254, 249, 219);
    static final Color SAGE_GREEN  = new Color(138, 159, 107);
    static final Color WHITE       = Color.WHITE;
    static final Color DARK        = new Color(31, 41, 55);
    static final Color MUTED       = new Color(107, 114, 128);
    static final Color RED         = new Color(210, 65, 65);

    static JFrame frame;
    static CardLayout cardLayout = new CardLayout();
    static JPanel content = new JPanel(cardLayout);
    static JLabel navBooks, navUsers, navOrders;

    static String rupiah(long value) {
        return "Rp " + NumberFormat.getInstance(Locale.forLanguageTag("id-ID")).format(value);
    }

    static JLabel loadIcon(String path, int width, int height) {
        JLabel label = new JLabel();
        if (path != null && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
        return label;
    }

    static void styleTable(JTable table, int rowHeight) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(rowHeight);
        table.setForeground(DARK);
        table.setBackground(WHITE);
        table.setSelectionBackground(SOFT_PINK);
        table.setSelectionForeground(DARK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(245, 235, 235));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 44));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFoc, r, c);
                lbl.setBackground(HEADER_ROSE);
                lbl.setForeground(WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setBorder(new EmptyBorder(0, 6, 0, 6));
                if (c == 0 || c == 1 || c == 5 || c == 7 || "Status".equals(val) || "Aksi".equals(val)) {
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }
                lbl.setOpaque(true);
                return lbl;
            }
        });
    }

    static JPanel statCard(String title, JLabel value, Color accent) {
        AdminRoundedPanel panel = new AdminRoundedPanel(18, WHITE);
        panel.setLayout(new BorderLayout(5, 8));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 13));
        heading.setForeground(MUTED);
        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(accent);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }

    static JPanel pageHeader(String title, JPanel stats) {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);

        AdminRoundedPanel banner = new AdminRoundedPanel(18, LIGHT_CREAM);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(16, 22, 16, 22));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        heading.setForeground(DARK);
        banner.add(heading, BorderLayout.CENTER);

        panel.add(banner, BorderLayout.NORTH);
        panel.add(stats, BorderLayout.SOUTH);
        return panel;
    }

    static JPanel statsPanel(JPanel... cards) {
        JPanel panel = new JPanel(new GridLayout(1, cards.length, 18, 0));
        panel.setOpaque(false);
        for (JPanel card : cards) panel.add(card);
        return panel;
    }

    static AdminRoundedPanel whiteCard() {
        AdminRoundedPanel panel = new AdminRoundedPanel(20, WHITE);
        panel.setLayout(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(20, 22, 20, 22));
        return panel;
    }

    static JButton button(String text, Color background) {
        AdminRoundedButton btn = new AdminRoundedButton(text, background, WHITE);
        btn.setPreferredSize(new Dimension(Math.max(135, text.length() * 9 + 36), 38));
        return btn;
    }

    static JButton smallActionButton(String text, Color background) {
        AdminRoundedButton btn = new AdminRoundedButton(text, background, WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(68, 30));
        return btn;
    }

    static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DARK);
        return label;
    }

    // Lebar preferred diset 0 agar fleksibel mengikuti lebar pop-up tanpa memicu scrollbar samping
    static JTextField field() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 38));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(HEADER_ROSE, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        return field;
    }

    static JComboBox<String> combo(String... values) {
        JComboBox<String> combo = new JComboBox<>(values);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(WHITE);
        combo.setPreferredSize(new Dimension(0, 38));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return combo;
    }

    static JPanel formField(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height + 24));
        JLabel titleLabel = label(title);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // POP-UP DIALOG CANTIK TANPA BINGKAI KAKU & TANPA SCROLLBAR SAMPING
    static void showDialog(String title, JPanel form, BooleanSupplier onSave) {
        JDialog dialog = new JDialog(frame, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        AdminRoundedPanel root = new AdminRoundedPanel(24, WHITE, HEADER_ROSE);
        root.setLayout(new BorderLayout(0, 16));
        root.setBorder(new EmptyBorder(22, 28, 24, 28));

        // Header Pop-up dengan tombol Close (×) elegan
        JPanel header = new JPanel(new BorderLayout(0, 12));
        header.setOpaque(false);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(DARK);

        AdminRoundedPanel closeBtn = new AdminRoundedPanel(14, SOFT_PINK);
        closeBtn.setPreferredSize(new Dimension(36, 36));
        closeBtn.setLayout(new GridBagLayout());
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel closeIcon = new JLabel("×");
        closeIcon.setFont(new Font("Arial", Font.BOLD, 22));
        closeIcon.setForeground(RED);
        closeBtn.add(closeIcon);

        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dialog.dispose(); }
            public void mouseEntered(MouseEvent e) {
                closeBtn.setPanelBackground(RED);
                closeIcon.setForeground(WHITE);
            }
            public void mouseExited(MouseEvent e) {
                closeBtn.setPanelBackground(SOFT_PINK);
                closeIcon.setForeground(RED);
            }
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(heading, BorderLayout.WEST);
        topBar.add(closeBtn, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 225, 225));

        header.add(topBar, BorderLayout.CENTER);
        header.add(sep, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // Body Form langsung tanpa scrollbar samping
        root.add(form, BorderLayout.CENTER);

        // Footer Tombol Aksi
        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(6, 0, 0, 0));

        JButton cancel = button("Batal", new Color(160, 155, 155));
        cancel.setPreferredSize(new Dimension(0, 42));
        JButton save = button("Simpan Data", SAGE_GREEN);
        save.setPreferredSize(new Dimension(0, 42));

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            if (onSave.getAsBoolean()) dialog.dispose();
        });
        actions.add(cancel);
        actions.add(save);
        root.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.pack();
        dialog.setSize(new Dimension(460, Math.max(dialog.getPreferredSize().height, 360)));
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    static JPanel formPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    static void addGap(JPanel panel) {
        panel.add(Box.createVerticalStrut(12));
    }

    // POP-UP NOTIFIKASI CUSTOM
    static void showMessage(String message) {
        JDialog dialog = new JDialog(frame, "Informasi", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(380, 200);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        AdminRoundedPanel root = new AdminRoundedPanel(22, WHITE, HEADER_ROSE);
        root.setLayout(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(24, 26, 22, 26));

        JLabel title = new JLabel("Informasi", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(DARK);

        JLabel msg = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>", SwingConstants.CENTER);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setForeground(MUTED);

        JButton ok = button("Mengerti", SAGE_GREEN);
        ok.setPreferredSize(new Dimension(130, 38));
        ok.addActionListener(e -> dialog.dispose());

        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnWrap.setOpaque(false);
        btnWrap.add(ok);

        root.add(title, BorderLayout.NORTH);
        root.add(msg, BorderLayout.CENTER);
        root.add(btnWrap, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    // POP-UP KONFIRMASI HAPUS CANTIK
    static boolean showConfirm(String message) {
        final boolean[] confirmed = {false};
        JDialog dialog = new JDialog(frame, "Konfirmasi", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(400, 220);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        AdminRoundedPanel root = new AdminRoundedPanel(22, WHITE, HEADER_ROSE);
        root.setLayout(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(24, 28, 22, 28));

        JLabel title = new JLabel("Konfirmasi Hapus", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(RED);

        JLabel msg = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>", SwingConstants.CENTER);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msg.setForeground(DARK);

        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setOpaque(false);

        JButton cancel = button("Batal", new Color(160, 155, 155));
        cancel.setPreferredSize(new Dimension(0, 40));
        JButton yes = button("Ya, Hapus", RED);
        yes.setPreferredSize(new Dimension(0, 40));

        cancel.addActionListener(e -> dialog.dispose());
        yes.addActionListener(e -> {
            confirmed[0] = true;
            dialog.dispose();
        });

        actions.add(cancel);
        actions.add(yes);

        root.add(title, BorderLayout.NORTH);
        root.add(msg, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
        return confirmed[0];
    }

    static int idFromLabel(String value, String prefix) {
        return Integer.parseInt(value.substring(prefix.length()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Database.initialize();
                createAndShowGUI();
            } catch (SQLException ex) {
                BukaBukuApp.showDatabaseError(null, ex);
            }
        });
    }

    static void createAndShowGUI() {
        frame = new JFrame("BukaBuku - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1360, 820);
        frame.setMinimumSize(new Dimension(1100, 680));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(SOFT_PINK);

        // --- SIDEBAR DENGAN LOGO & TULISAN BUKABUKU ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DUSTY_ROSE);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(30, 0, 20, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        brandPanel.setOpaque(false);
        brandPanel.setMaximumSize(new Dimension(260, 55));
        brandPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoIcon = loadIcon("assets/bukabuku.png", 42, 42);
        if (logoIcon.getIcon() == null) {
            logoIcon.setText("📖");
            logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            logoIcon.setForeground(WHITE);
        }

        JLabel brandText = new JLabel("BukaBuku");
        brandText.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandText.setForeground(WHITE);

        brandPanel.add(logoIcon);
        brandPanel.add(brandText);

        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(35));

        navUsers = menuItem("Manajemen Pengguna", "Users");
        navBooks = menuItem("Katalog & Stok Buku", "Books");
        navOrders = menuItem("Pesanan Masuk", "Orders");

        sidebar.add(navUsers);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navBooks);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navOrders);
        sidebar.add(Box.createVerticalGlue());

        content.setBackground(SOFT_PINK);
        content.add(new UserPanel(), "Users");
        content.add(new BookPanel(), "Books");
        content.add(new OrderPanel(), "Orders");

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(content, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        switchMenu("Books");
        frame.setVisible(true);
    }

    static JLabel menuItem(String text, String page) {
        JLabel item = new JLabel(text, SwingConstants.CENTER);
        item.setFont(new Font("Segoe UI", Font.BOLD, 15));
        item.setForeground(SOFT_PINK);
        item.setOpaque(true);
        item.setBackground(DUSTY_ROSE);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setAlignmentX(Component.CENTER_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setPreferredSize(new Dimension(260, 50));
        item.setBorder(new EmptyBorder(14, 16, 14, 16));
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchMenu(page); }
            public void mouseEntered(MouseEvent e) {
                if (!item.getBackground().equals(LIGHT_CREAM)) {
                    item.setBackground(new Color(185, 120, 122));
                    item.setForeground(WHITE);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!item.getBackground().equals(LIGHT_CREAM)) {
                    item.setBackground(DUSTY_ROSE);
                    item.setForeground(SOFT_PINK);
                }
            }
        });
        return item;
    }

    static void switchMenu(String page) {
        cardLayout.show(content, page);
        for (Component component : content.getComponents()) {
            if ("Books".equals(page) && component instanceof BookPanel) {
                ((BookPanel) component).loadData();
                ((BookPanel) component).updateStats();
            } else if ("Users".equals(page) && component instanceof UserPanel) {
                ((UserPanel) component).loadData();
                ((UserPanel) component).updateStats();
            } else if ("Orders".equals(page) && component instanceof OrderPanel) {
                ((OrderPanel) component).loadData();
                ((OrderPanel) component).updateStats();
            }
        }
        resetMenu(navUsers);
        resetMenu(navBooks);
        resetMenu(navOrders);
        if (page.equals("Users")) activateMenu(navUsers);
        if (page.equals("Books")) activateMenu(navBooks);
        if (page.equals("Orders")) activateMenu(navOrders);
    }

    static void resetMenu(JLabel item) {
        item.setBackground(DUSTY_ROSE);
        item.setForeground(SOFT_PINK);
    }

    static void activateMenu(JLabel item) {
        item.setBackground(LIGHT_CREAM);
        item.setForeground(DARK);
    }

    // ============================= MANAJEMEN BUKU =============================

    static class BookPanel extends JPanel {
        DefaultTableModel model;
        JTable table;
        JLabel totalLabel = new JLabel("0"), lowLabel = new JLabel("0"), assetLabel = new JLabel("Rp 0");

        BookPanel() {
            setLayout(new BorderLayout(0, 20));
            setBackground(SOFT_PINK);
            setBorder(new EmptyBorder(25, 30, 25, 30));

            JPanel stats = statsPanel(
                    statCard("Total Judul Buku", totalLabel, HEADER_ROSE),
                    statCard("Stok Menipis (< 5)", lowLabel, RED),
                    statCard("Estimasi Nilai Aset", assetLabel, SAGE_GREEN)
            );
            add(pageHeader("Katalog & Stok Buku", stats), BorderLayout.NORTH);

            AdminRoundedPanel card = whiteCard();
            JPanel toolbar = new JPanel(new BorderLayout());
            toolbar.setOpaque(false);
            JLabel section = new JLabel("Daftar Buku");
            section.setFont(new Font("Segoe UI", Font.BOLD, 18));
            section.setForeground(DARK);
            JButton add = button("+ Tambah Buku", SAGE_GREEN);
            add.addActionListener(e -> openBookDialog(-1));
            toolbar.add(section, BorderLayout.WEST);
            toolbar.add(add, BorderLayout.EAST);
            card.add(toolbar, BorderLayout.NORTH);

            model = new DefaultTableModel(
                    new String[]{"Gambar", "ID Buku", "Judul Buku", "Penulis", "Kategori", "Stok", "Harga", "Aksi"}, 0) {
                public boolean isCellEditable(int row, int col) { return col == 7; }
            };
            table = new JTable(model);
            styleTable(table, 72);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            table.getColumnModel().getColumn(0).setPreferredWidth(85);
            table.getColumnModel().getColumn(1).setPreferredWidth(85);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(140);
            table.getColumnModel().getColumn(4).setPreferredWidth(160);
            table.getColumnModel().getColumn(5).setPreferredWidth(65);
            table.getColumnModel().getColumn(6).setPreferredWidth(120);
            table.getColumnModel().getColumn(7).setPreferredWidth(165);
            table.getColumnModel().getColumn(7).setMinWidth(160);

            table.getColumnModel().getColumn(0).setCellRenderer(new BookImageRenderer());
            table.getColumnModel().getColumn(5).setCellRenderer(new StockRenderer());
            table.getColumnModel().getColumn(7).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(7).setCellEditor(new BookActionEditor(this));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(WHITE);
            card.add(scrollPane, BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        private final List<Book> currentBooks = new ArrayList<>();

        void loadData() {
            new SwingWorker<List<Book>, Void>() {
                protected List<Book> doInBackground() throws Exception {
                    return Database.getBooks();
                }
                protected void done() {
                    try {
                        currentBooks.clear();
                        currentBooks.addAll(get());
                        model.setRowCount(0);
                        for (Book book : currentBooks) {
                            model.addRow(new Object[]{book.imagePath, Database.bookNumber(book.id), book.title,
                                    book.author, book.category, book.stock, rupiah(book.price), "Aksi"});
                        }
                        updateStats();
                    } catch (Exception ex) {
                        showMessage("Gagal memuat buku: " + ex.getMessage());
                    }
                }
            }.execute();
        }

        void openBookDialog(int row) {
            boolean editing = row >= 0;
            JTextField title = field(), author = field(), stock = field(), price = field(), image = field();

            JTextArea description = new JTextArea(3, 20);
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            description.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            description.setBorder(new EmptyBorder(6, 10, 6, 10));

            JScrollPane descScroll = new JScrollPane(description);
            descScroll.setPreferredSize(new Dimension(0, 72));
            descScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            descScroll.setBorder(BorderFactory.createLineBorder(HEADER_ROSE, 1, true));

            JComboBox<String> category = combo("Fiksi Petualangan", "Fiksi Ilmiah & Romansa",
                    "Fiksi Inspiratif", "Fiksi Pendidikan", "Self Improvement", "Drama Keluarga", "Fiksi Psikologis");

            if (editing) {
                title.setText(model.getValueAt(row, 2).toString());
                author.setText(model.getValueAt(row, 3).toString());
                category.setSelectedItem(model.getValueAt(row, 4).toString());
                stock.setText(model.getValueAt(row, 5).toString());
                price.setText(model.getValueAt(row, 6).toString().replaceAll("[^0-9]", ""));
                image.setText(model.getValueAt(row, 0).toString());
                if (row < currentBooks.size()) {
                    description.setText(currentBooks.get(row).description);
                }
            } else {
                image.setText("assets/");
            }

            JButton browse = button("Pilih Foto", SAGE_GREEN);
            browse.setPreferredSize(new Dimension(110, 38));
            browse.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    image.setText(chooser.getSelectedFile().getPath());
            });

            JPanel imageRow = new JPanel(new BorderLayout(8, 0));
            imageRow.setOpaque(false);
            imageRow.setPreferredSize(new Dimension(0, 38));
            imageRow.add(image, BorderLayout.CENTER);
            imageRow.add(browse, BorderLayout.EAST);

            // Baris Stok & Harga dibuat berdampingan (2 kolom) agar pop-up ringkas & rapi
            JPanel stockPriceRow = new JPanel(new GridLayout(1, 2, 12, 0));
            stockPriceRow.setOpaque(false);
            stockPriceRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            stockPriceRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            stockPriceRow.add(formField("Stok Buku", stock));
            stockPriceRow.add(formField("Harga (Rp)", price));

            JPanel form = formPanel();
            form.add(formField("Judul Buku", title)); addGap(form);
            form.add(formField("Penulis", author)); addGap(form);
            form.add(formField("Kategori", category)); addGap(form);
            form.add(stockPriceRow); addGap(form);
            form.add(formField("Deskripsi Singkat", descScroll)); addGap(form);
            form.add(formField("Path Foto Buku", imageRow));

            showDialog(editing ? "Edit Detail Buku" : "Tambah Buku Baru", form, () -> {
                try {
                    if (title.getText().trim().isEmpty() || author.getText().trim().isEmpty())
                        throw new IllegalArgumentException("Judul dan penulis wajib diisi.");
                    int s = Integer.parseInt(stock.getText().trim());
                    long p = Long.parseLong(price.getText().trim());
                    if (s < 0 || p < 0) throw new IllegalArgumentException("Stok dan harga tidak boleh negatif.");
                    if (p > Integer.MAX_VALUE) throw new IllegalArgumentException("Harga terlalu besar.");

                    int id = editing ? idFromLabel(model.getValueAt(row, 1).toString(), "BK-") : 0;
                    Book book = new Book(id, title.getText().trim(), author.getText().trim(),
                            category.getSelectedItem().toString(), (int) p, image.getText().trim(),
                            description.getText().trim(), s);

                    new SwingWorker<Void, Void>() {
                        protected Void doInBackground() throws Exception {
                            if (editing) {
                                Database.updateBook(book);
                            } else {
                                Database.insertBook(book);
                            }
                            return null;
                        }
                        protected void done() {
                            try {
                                get();
                                loadData();
                            } catch (Exception ex) {
                                showMessage("Gagal menyimpan buku: " + ex.getMessage());
                            }
                        }
                    }.execute();
                    return true;
                } catch (NumberFormatException ex) {
                    showMessage("Stok dan harga harus berupa angka.");
                } catch (IllegalArgumentException ex) {
                    showMessage(ex.getMessage());
                }
                return false;
            });
        }

        void editBook(int row) { openBookDialog(row); }

        void deleteBook(int row) {
            String bookTitle = model.getValueAt(row, 2).toString();
            if (showConfirm("Apakah kamu yakin ingin menghapus buku <b>\"" + bookTitle + "\"</b> dari katalog?")) {
                int id = idFromLabel(model.getValueAt(row, 1).toString(), "BK-");
                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        Database.deleteBook(id);
                        return null;
                    }
                    protected void done() {
                        try {
                            get();
                            loadData();
                        } catch (Exception ex) {
                            showMessage("Gagal menghapus buku: " + ex.getMessage());
                        }
                    }
                }.execute();
            }
        }

        void updateStats() {
            int low = 0;
            long assets = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                int s = Integer.parseInt(model.getValueAt(i, 5).toString());
                long p = Long.parseLong(model.getValueAt(i, 6).toString().replaceAll("[^0-9]", ""));
                if (s < 5) low++;
                assets += (long) s * p;
            }
            totalLabel.setText(String.valueOf(model.getRowCount()));
            lowLabel.setText(String.valueOf(low));
            assetLabel.setText(rupiah(assets));
        }
    }

    // =========================== MANAJEMEN PENGGUNA ===========================

    static class UserPanel extends JPanel {
        DefaultTableModel model;
        JLabel total = new JLabel("0"), active = new JLabel("0"), sellers = new JLabel("0"), admins = new JLabel("0");

        UserPanel() {
            setLayout(new BorderLayout(0, 20));
            setBackground(SOFT_PINK);
            setBorder(new EmptyBorder(25, 30, 25, 30));

            JPanel stats = statsPanel(
                    statCard("Total Pengguna", total, HEADER_ROSE),
                    statCard("Pengguna Aktif", active, SAGE_GREEN),
                    statCard("Total Penjual", sellers, HEADER_ROSE),
                    statCard("Total Admin", admins, SAGE_GREEN));
            add(pageHeader("Manajemen Pengguna", stats), BorderLayout.NORTH);

            AdminRoundedPanel card = whiteCard();
            JPanel toolbar = new JPanel(new BorderLayout());
            toolbar.setOpaque(false);
            JLabel heading = new JLabel("Daftar Pengguna");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
            heading.setForeground(DARK);
            JButton add = button("+ Tambah Pengguna", SAGE_GREEN);
            add.addActionListener(e -> openUserDialog(-1));
            toolbar.add(heading, BorderLayout.WEST);
            toolbar.add(add, BorderLayout.EAST);
            card.add(toolbar, BorderLayout.NORTH);

            model = new DefaultTableModel(new String[]{"ID", "Nama Lengkap", "Email", "Role", "Status", "Aksi"}, 0) {
                public boolean isCellEditable(int r, int c) { return c == 5; }
            };
            JTable table = new JTable(model);
            styleTable(table, 52);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setPreferredWidth(180);
            table.getColumnModel().getColumn(2).setPreferredWidth(220);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setPreferredWidth(110);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
            table.getColumnModel().getColumn(5).setPreferredWidth(165);
            table.getColumnModel().getColumn(5).setMinWidth(160);
            table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new UserActionEditor(this));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(WHITE);
            card.add(scrollPane, BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        void loadData() {
            new SwingWorker<List<Database.UserAccount>, Void>() {
                protected List<Database.UserAccount> doInBackground() throws Exception {
                    return Database.getUsers();
                }
                protected void done() {
                    try {
                        model.setRowCount(0);
                        for (Database.UserAccount user : get()) {
                            model.addRow(new Object[]{Database.userNumber(user.id), user.fullName, user.email,
                                    user.role, user.status, "Aksi"});
                        }
                        updateStats();
                    } catch (Exception ex) {
                        showMessage("Gagal memuat pengguna: " + ex.getMessage());
                    }
                }
            }.execute();
        }

        void openUserDialog(int row) {
            boolean editing = row >= 0;
            JTextField name = field(), email = field();
            JTextField username = field();
            JPasswordField password = new JPasswordField();
            password.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            password.setPreferredSize(new Dimension(0, 38));
            password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            password.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(HEADER_ROSE, 1, true),
                    new EmptyBorder(6, 12, 6, 12)));

            JComboBox<String> role = combo("Pelanggan", "Penjual", "Admin");
            JComboBox<String> status = combo("Aktif", "Nonaktif");

            if (editing) {
                name.setText(model.getValueAt(row, 1).toString());
                email.setText(model.getValueAt(row, 2).toString());
                role.setSelectedItem(model.getValueAt(row, 3).toString());
                status.setSelectedItem(model.getValueAt(row, 4).toString());
            }

            JPanel roleStatusRow = new JPanel(new GridLayout(1, 2, 12, 0));
            roleStatusRow.setOpaque(false);
            roleStatusRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            roleStatusRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            roleStatusRow.add(formField("Role Akses", role));
            roleStatusRow.add(formField("Status Akun", status));

            JPanel form = formPanel();
            form.add(formField("Nama Lengkap", name)); addGap(form);
            if (!editing) {
                form.add(formField("Username", username)); addGap(form);
                form.add(formField("Password (minimal 8 karakter)", password)); addGap(form);
            }
            form.add(formField("Alamat Email", email)); addGap(form);
            form.add(roleStatusRow);

            showDialog(editing ? "Edit Data Pengguna" : "Tambah Pengguna Baru", form, () -> {
                if (name.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
                    showMessage("Nama dan email wajib diisi.");
                    return false;
                }
                if (!editing && username.getText().trim().isEmpty()) {
                    showMessage("Username wajib diisi.");
                    return false;
                }
                if (!editing && password.getPassword().length < 8) {
                    showMessage("Password harus memiliki minimal 8 karakter.");
                    return false;
                }

                String fullName = name.getText().trim();
                String emailText = email.getText().trim();
                String roleText = role.getSelectedItem().toString();
                String statusText = status.getSelectedItem().toString();
                String usernameText = username.getText().trim();
                String passwordText = new String(password.getPassword());
                int userId = editing ? idFromLabel(model.getValueAt(row, 0).toString(), "USR-") : 0;

                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        if (editing) {
                            Database.updateUser(userId, fullName, emailText, roleText, statusText);
                        } else {
                            Database.createUser(fullName, usernameText, emailText, passwordText, roleText, statusText);
                        }
                        return null;
                    }
                    protected void done() {
                        try {
                            get();
                            loadData();
                        } catch (Exception ex) {
                            showMessage("Gagal menyimpan pengguna: " + ex.getMessage());
                        }
                    }
                }.execute();
                return true;
            });
        }

        void deleteUser(int row) {
            String userName = model.getValueAt(row, 1).toString();
            if (showConfirm("Apakah kamu yakin ingin menghapus akun pengguna <b>\"" + userName + "\"</b>?")) {
                int id = idFromLabel(model.getValueAt(row, 0).toString(), "USR-");
                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        Database.deleteUser(id);
                        return null;
                    }
                    protected void done() {
                        try {
                            get();
                            loadData();
                        } catch (Exception ex) {
                            showMessage("Gagal menghapus pengguna: " + ex.getMessage());
                        }
                    }
                }.execute();
            }
        }

        void updateStats() {
            int a = 0, s = 0, ad = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                if ("Aktif".equals(model.getValueAt(i, 4))) a++;
                if ("Penjual".equals(model.getValueAt(i, 3))) s++;
                if ("Admin".equals(model.getValueAt(i, 3))) ad++;
            }
            total.setText(String.valueOf(model.getRowCount()));
            active.setText(String.valueOf(a));
            sellers.setText(String.valueOf(s));
            admins.setText(String.valueOf(ad));
        }
    }

    // ============================= MANAJEMEN PESANAN ===========================

    static class OrderPanel extends JPanel {
        DefaultTableModel model;
        JLabel total = new JLabel("0"), pending = new JLabel("0"), completed = new JLabel("0");

        OrderPanel() {
            setLayout(new BorderLayout(0, 20));
            setBackground(SOFT_PINK);
            setBorder(new EmptyBorder(25, 30, 25, 30));

            JPanel stats = statsPanel(
                    statCard("Total Pesanan", total, HEADER_ROSE),
                    statCard("Menunggu (Pending)", pending, RED),
                    statCard("Pesanan Selesai", completed, SAGE_GREEN));
            add(pageHeader("Manajemen Pesanan", stats), BorderLayout.NORTH);

            AdminRoundedPanel card = whiteCard();
            JPanel toolbar = new JPanel(new BorderLayout());
            toolbar.setOpaque(false);
            JLabel heading = new JLabel("Daftar Pesanan");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
            heading.setForeground(DARK);
            JButton add = button("+ Tambah Pesanan", SAGE_GREEN);
            add.addActionListener(e -> openOrderDialog(-1));
            toolbar.add(heading, BorderLayout.WEST);
            toolbar.add(add, BorderLayout.EAST);
            card.add(toolbar, BorderLayout.NORTH);

            model = new DefaultTableModel(
                    new String[]{"No. Order", "Pelanggan", "Detail Item", "Total Harga", "Status", "Aksi"}, 0) {
                public boolean isCellEditable(int r, int c) { return c == 5; }
            };
            JTable table = new JTable(model);
            styleTable(table, 52);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setPreferredWidth(95);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setPreferredWidth(160);
            table.getColumnModel().getColumn(2).setPreferredWidth(250);
            table.getColumnModel().getColumn(3).setPreferredWidth(130);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
            table.getColumnModel().getColumn(5).setPreferredWidth(165);
            table.getColumnModel().getColumn(5).setMinWidth(160);
            table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new OrderActionEditor(this));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(WHITE);
            card.add(scrollPane, BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        void loadData() {
            new SwingWorker<List<Order>, Void>() {
                protected List<Order> doInBackground() throws Exception {
                    return Database.getOrders(null);
                }
                protected void done() {
                    try {
                        model.setRowCount(0);
                        for (Order order : get()) {
                            StringBuilder detail = new StringBuilder();
                            for (CartItem item : order.items) {
                                if (detail.length() > 0) detail.append(", ");
                                detail.append(item.book.title).append(" (x").append(item.qty).append(")");
                            }
                            model.addRow(new Object[]{order.id, order.customerName, detail.toString(),
                                    rupiah(order.total), order.status, "Aksi"});
                        }
                        updateStats();
                    } catch (Exception ex) {
                        showMessage("Gagal memuat pesanan: " + ex.getMessage());
                    }
                }
            }.execute();
        }

        void openOrderDialog(int row) {
            boolean editing = row >= 0;
            JTextField customer = field(), item = field(), price = field();
            JComboBox<String> status = combo("Pending", "Diproses", "Dikirim", "Selesai", "Dibatalkan");

            if (editing) {
                customer.setText(model.getValueAt(row, 1).toString());
                item.setText(model.getValueAt(row, 2).toString());
                item.setEditable(false);
                item.setBackground(new Color(245, 245, 245));
                item.setToolTipText("Detail item pesanan pelanggan terjaga otomatis.");
                price.setText(model.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""));
                status.setSelectedItem(model.getValueAt(row, 4).toString());
            }

            JPanel form = formPanel();
            form.add(formField("Nama Pelanggan", customer)); addGap(form);
            form.add(formField("Detail Item", item)); addGap(form);
            form.add(formField("Total Harga (Rp)", price)); addGap(form);
            form.add(formField("Status Pesanan", status));

            showDialog(editing ? "Update Pesanan" : "Tambah Pesanan Baru", form, () -> {
                try {
                    if (customer.getText().trim().isEmpty() || item.getText().trim().isEmpty())
                        throw new IllegalArgumentException("Nama pelanggan dan item wajib diisi.");
                    long amount = Long.parseLong(price.getText().trim());
                    if (amount < 0) throw new IllegalArgumentException("Harga tidak boleh negatif.");

                    String id = editing ? model.getValueAt(row, 0).toString() : null;
                    String customerName = customer.getText().trim();
                    String itemText = item.getText().trim();
                    String statusText = status.getSelectedItem().toString();

                    new SwingWorker<Void, Void>() {
                        protected Void doInBackground() throws Exception {
                            Database.saveAdminOrder(id, customerName, itemText, amount, statusText);
                            return null;
                        }
                        protected void done() {
                            try {
                                get();
                                loadData();
                            } catch (Exception ex) {
                                showMessage("Gagal menyimpan pesanan: " + ex.getMessage());
                            }
                        }
                    }.execute();
                    return true;
                } catch (NumberFormatException ex) {
                    showMessage("Total harga harus berupa angka.");
                } catch (IllegalArgumentException ex) {
                    showMessage(ex.getMessage());
                }
                return false;
            });
        }

        void deleteOrder(int row) {
            String orderId = model.getValueAt(row, 0).toString();
            if (showConfirm("Apakah kamu yakin ingin menghapus data pesanan <b>\"" + orderId + "\"</b>?")) {
                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        Database.deleteOrder(orderId);
                        return null;
                    }
                    protected void done() {
                        try {
                            get();
                            loadData();
                        } catch (Exception ex) {
                            showMessage("Gagal menghapus pesanan: " + ex.getMessage());
                        }
                    }
                }.execute();
            }
        }

        void updateStats() {
            int p = 0, done = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String status = model.getValueAt(i, 4).toString();
                if ("Pending".equals(status)) p++;
                if ("Selesai".equals(status)) done++;
            }
            total.setText(String.valueOf(model.getRowCount()));
            pending.setText(String.valueOf(p));
            completed.setText(String.valueOf(done));
        }
    }

    // =============================== RENDERER & EDITOR AKSI =============================

    static class ActionRenderer extends JPanel implements TableCellRenderer {
        JButton edit = smallActionButton("Edit", SAGE_GREEN);
        JButton delete = smallActionButton("Hapus", RED);

        ActionRenderer() {
            setLayout(new GridBagLayout());
            setBackground(WHITE);
            JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            box.setOpaque(false);
            box.add(edit);
            box.add(delete);
            add(box);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            setBackground(selected ? SOFT_PINK : WHITE);
            return this;
        }
    }

    static class BookActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton edit = smallActionButton("Edit", SAGE_GREEN), delete = smallActionButton("Hapus", RED);
        BookPanel owner;
        int row;

        BookActionEditor(BookPanel owner) {
            this.owner = owner;
            panel.setBackground(WHITE);
            JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            box.setOpaque(false);
            box.add(edit);
            box.add(delete);
            panel.add(box);
            edit.addActionListener(e -> { fireEditingStopped(); owner.editBook(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteBook(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r);
            panel.setBackground(SOFT_PINK);
            return panel;
        }
        public Object getCellEditorValue() { return "Aksi"; }
    }

    static class UserActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton edit = smallActionButton("Edit", SAGE_GREEN), delete = smallActionButton("Hapus", RED);
        UserPanel owner; int row;
        UserActionEditor(UserPanel owner) {
            this.owner = owner;
            panel.setBackground(WHITE);
            JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            box.setOpaque(false);
            box.add(edit);
            box.add(delete);
            panel.add(box);
            edit.addActionListener(e -> { fireEditingStopped(); owner.openUserDialog(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteUser(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r);
            panel.setBackground(SOFT_PINK);
            return panel;
        }
        public Object getCellEditorValue() { return "Aksi"; }
    }

    static class OrderActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton edit = smallActionButton("Edit", SAGE_GREEN), delete = smallActionButton("Hapus", RED);
        OrderPanel owner; int row;
        OrderActionEditor(OrderPanel owner) {
            this.owner = owner;
            panel.setBackground(WHITE);
            JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            box.setOpaque(false);
            box.add(edit);
            box.add(delete);
            panel.add(box);
            edit.addActionListener(e -> { fireEditingStopped(); owner.openOrderDialog(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteOrder(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r);
            panel.setBackground(SOFT_PINK);
            return panel;
        }
        public Object getCellEditorValue() { return "Aksi"; }
    }

    static class BookImageRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(selected ? SOFT_PINK : WHITE);
            if (value != null && new File(value.toString()).exists()) {
                ImageIcon icon = new ImageIcon(value.toString());
                Image scaled = icon.getImage().getScaledInstance(40, 56, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            } else {
                label.setText("No IMG");
                label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                label.setForeground(MUTED);
            }
            return label;
        }
    }

    static class StockRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, selected, focus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            int stock = Integer.parseInt(value.toString());
            label.setForeground(stock < 5 ? RED : DARK);
            label.setFont(new Font("Segoe UI", stock < 5 ? Font.BOLD : Font.PLAIN, 13));
            return label;
        }
    }

    static class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, selected, focus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            String status = value == null ? "" : value.toString();
            if (status.equals("Aktif") || status.equals("Selesai") || status.equals("Diproses") || status.equals("Dikirim")) {
                label.setForeground(new Color(22, 101, 52));
                label.setBackground(new Color(220, 252, 231));
            } else {
                label.setForeground(new Color(153, 27, 27));
                label.setBackground(new Color(254, 226, 226));
            }
            label.setOpaque(true);
            return label;
        }
    }
}

class AdminRoundedPanel extends JPanel {
    private final int radius;
    private Color background;
    private Color borderColor;

    AdminRoundedPanel(int radius, Color background) {
        this(radius, background, null);
    }

    AdminRoundedPanel(int radius, Color background, Color borderColor) {
        this.radius = radius;
        this.background = background;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    void setPanelBackground(Color bg) {
        this.background = bg;
        repaint();
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        if (borderColor != null) {
            g.setColor(borderColor);
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, radius, radius);
        }
        g.dispose();
        super.paintComponent(graphics);
    }
}

class AdminRoundedButton extends JButton {
    private final Color bg;

    AdminRoundedButton(String text, Color bg, Color fg) {
        super(text);
        this.bg = bg;
        setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 12, 0, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() ? bg.darker() : bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}