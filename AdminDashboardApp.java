import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class AdminDashboardApp {

    // Palet warna mengikuti gambar referensi
    static final Color DUSTY_ROSE = new Color(214, 163, 163);
    static final Color SOFT_PINK = new Color(255, 222, 222);
    static final Color LIGHT_CREAM = new Color(255, 249, 215);
    static final Color SAGE_GREEN = new Color(145, 166, 116);
    static final Color WHITE = Color.WHITE;
    static final Color DARK = new Color(55, 48, 48);
    static final Color MUTED = new Color(125, 112, 112);
    static final Color RED = new Color(190, 78, 78);

    static JFrame frame;
    static CardLayout cardLayout = new CardLayout();
    static JPanel content = new JPanel(cardLayout);
    static JLabel navBooks, navUsers, navOrders;

    static String rupiah(long value) {
        return "Rp " + NumberFormat.getInstance(Locale.forLanguageTag("id-ID")).format(value);
    }

    static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(48);
        table.setForeground(DARK);
        table.setBackground(WHITE);
        table.setSelectionBackground(SOFT_PINK);
        table.setSelectionForeground(DARK);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(242, 232, 232));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(DUSTY_ROSE);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);
    }

    static JPanel statCard(String title, JLabel value, Color accent) {
        AdminRoundedPanel panel = new AdminRoundedPanel(17, WHITE);
        panel.setLayout(new BorderLayout(5, 8));
        panel.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 12));
        heading.setForeground(MUTED);
        value.setFont(new Font("Segoe UI", Font.BOLD, 27));
        value.setForeground(accent);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }

    static JPanel pageHeader(String title, JPanel stats) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        AdminRoundedPanel banner = new AdminRoundedPanel(18, LIGHT_CREAM);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        heading.setForeground(DARK);
        banner.add(heading, BorderLayout.CENTER);

        panel.add(banner, BorderLayout.NORTH);
        panel.add(stats, BorderLayout.SOUTH);
        return panel;
    }

    static JPanel statsPanel(JPanel... cards) {
        JPanel panel = new JPanel(new GridLayout(1, cards.length, 15, 0));
        panel.setOpaque(false);
        for (JPanel card : cards) panel.add(card);
        return panel;
    }

    static AdminRoundedPanel whiteCard() {
        AdminRoundedPanel panel = new AdminRoundedPanel(18, WHITE);
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(17, 18, 17, 18));
        return panel;
    }

    static JButton button(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(112, 38));
        return button;
    }

    static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DARK);
        return label;
    }

    static JTextField field() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(280, 37));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DUSTY_ROSE, 1),
                new EmptyBorder(7, 10, 7, 10)));
        return field;
    }

    static JComboBox<String> combo(String... values) {
        JComboBox<String> combo = new JComboBox<>(values);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(WHITE);
        combo.setPreferredSize(new Dimension(280, 37));
        return combo;
    }

    static JPanel formField(String title, JComponent component) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel titleLabel = label(title);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(component);
        return panel;
    }

    static void showDialog(String title, JPanel form, BooleanSupplier onSave) {
        JDialog dialog = new JDialog(frame, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(440, 560);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);

        JPanel root = new JPanel(new BorderLayout(0, 15));
        root.setBackground(WHITE);
        root.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(DARK);
        root.add(heading, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WHITE);
        root.add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(WHITE);
        JButton cancel = button("Batal", new Color(160, 150, 150));
        JButton save = button("Simpan", SAGE_GREEN);
        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            if (onSave.getAsBoolean()) dialog.dispose();
        });
        actions.add(cancel);
        actions.add(save);
        root.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    static JPanel formPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(5, 3, 12, 3));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    static void addGap(JPanel panel) {
        panel.add(Box.createVerticalStrut(14));
    }

    static void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("BukaBuku - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350, 820);
        frame.setMinimumSize(new Dimension(1050, 680));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(SOFT_PINK);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(DUSTY_ROSE);
        sidebar.setPreferredSize(new Dimension(245, 0));
        sidebar.setBorder(new EmptyBorder(35, 0, 20, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("BukaBuku Admin");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(45));

        navUsers = menuItem("Manajemen Pengguna", "Users");
        navBooks = menuItem("Katalog & Stok Buku", "Books");
        navOrders = menuItem("Pesanan Masuk", "Orders");

        sidebar.add(navUsers);
        sidebar.add(navBooks);
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
        item.setFont(new Font("Segoe UI", Font.BOLD, 14));
        item.setForeground(WHITE);
        item.setOpaque(true);
        item.setBackground(DUSTY_ROSE);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        item.setBorder(new EmptyBorder(15, 8, 15, 8));
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchMenu(page); }
            public void mouseEntered(MouseEvent e) {
                if (!item.getBackground().equals(LIGHT_CREAM)) item.setBackground(DUSTY_ROSE.darker());
            }
            public void mouseExited(MouseEvent e) {
                if (!item.getBackground().equals(LIGHT_CREAM)) item.setBackground(DUSTY_ROSE);
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
        item.setForeground(WHITE);
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
            setBorder(new EmptyBorder(27, 30, 28, 30));

            JPanel stats = statsPanel(
                    statCard("Total Judul Buku", totalLabel, DUSTY_ROSE),
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
            styleTable(table);
            table.getColumnModel().getColumn(0).setPreferredWidth(75);
            table.getColumnModel().getColumn(1).setPreferredWidth(80);
            table.getColumnModel().getColumn(2).setPreferredWidth(190);
            table.getColumnModel().getColumn(3).setPreferredWidth(130);
            table.getColumnModel().getColumn(4).setPreferredWidth(160);
            table.getColumnModel().getColumn(5).setPreferredWidth(55);
            table.getColumnModel().getColumn(6).setPreferredWidth(115);
            table.getColumnModel().getColumn(7).setPreferredWidth(150);
            table.getColumnModel().getColumn(0).setCellRenderer(new BookImageRenderer());
            table.getColumnModel().getColumn(7).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(7).setCellEditor(new BookActionEditor(this));
            table.getColumnModel().getColumn(5).setCellRenderer(new StockRenderer());

            card.add(new JScrollPane(table), BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        void loadData() {
            model.setRowCount(0);
            try {
                for (Book book : Database.getBooks()) {
                    model.addRow(new Object[]{book.imagePath, Database.bookNumber(book.id), book.title,
                            book.author, book.category, book.stock, rupiah(book.price), "Aksi"});
                }
            } catch (SQLException ex) {
                showMessage("Gagal memuat buku: " + ex.getMessage());
            }
        }

         void openBookDialog(int row) {
            boolean editing = row >= 0;
            JTextField title = field(), author = field(), stock = field(), price = field(), image = field();
            JComboBox<String> category = combo("Fiksi Petualangan", "Fiksi Ilmiah & Romansa",
                    "Fiksi Inspiratif", "Fiksi Pendidikan", "Self Improvement", "Drama Keluarga", "Fiksi Psikologis");

            if (editing) {
                title.setText(model.getValueAt(row, 2).toString());
                author.setText(model.getValueAt(row, 3).toString());
                category.setSelectedItem(model.getValueAt(row, 4).toString());
                stock.setText(model.getValueAt(row, 5).toString());
                price.setText(model.getValueAt(row, 6).toString().replaceAll("[^0-9]", ""));
                image.setText(model.getValueAt(row, 0).toString());
            } else {
                image.setText("assets/");
            }

            JButton browse = button("Pilih Foto", SAGE_GREEN);
            browse.setPreferredSize(new Dimension(110, 37));
            browse.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    image.setText(chooser.getSelectedFile().getPath());
            });
            JPanel imageRow = new JPanel(new BorderLayout(7, 0));
            imageRow.setOpaque(false);
            imageRow.add(image, BorderLayout.CENTER);
            imageRow.add(browse, BorderLayout.EAST);

            JPanel form = formPanel();
            form.add(formField("Judul Buku", title)); addGap(form);
            form.add(formField("Penulis", author)); addGap(form);
            form.add(formField("Kategori", category)); addGap(form);
            form.add(formField("Stok", stock)); addGap(form);
            form.add(formField("Harga (Rp)", price)); addGap(form);
            form.add(formField("Path Foto Buku", imageRow));

            showDialog(editing ? "Edit Buku" : "Tambah Buku", form, () -> {
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
                            "", s);
                    if (editing) {
                        Database.updateBook(book);
                    } else {
                        id = Database.insertBook(book);
                    }
                    loadData();
                    updateStats();
                    return true;
                } catch (NumberFormatException ex) {
                    showMessage("Stok dan harga harus berupa angka.");
                } catch (IllegalArgumentException ex) {
                    showMessage(ex.getMessage());
                } catch (SQLException ex) {
                    showMessage("Gagal menyimpan buku: " + ex.getMessage());
                }
                return false;
            });
        }

        void editBook(int row) { openBookDialog(row); }

        void deleteBook(int row) {
            int answer = JOptionPane.showConfirmDialog(frame, "Hapus buku ini?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Database.deleteBook(idFromLabel(model.getValueAt(row, 1).toString(), "BK-"));
                    loadData();
                    updateStats();
                } catch (SQLException ex) {
                    showMessage("Gagal menghapus buku: " + ex.getMessage());
                }
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
            setBorder(new EmptyBorder(27, 30, 28, 30));

            JPanel stats = statsPanel(
                    statCard("Total Pengguna", total, DUSTY_ROSE),
                    statCard("Pengguna Aktif", active, SAGE_GREEN),
                    statCard("Total Penjual", sellers, DUSTY_ROSE),
                    statCard("Total Admin", admins, SAGE_GREEN));
            add(pageHeader("Manajemen Pengguna", stats), BorderLayout.NORTH);

            AdminRoundedPanel card = whiteCard();
            JPanel toolbar = new JPanel(new BorderLayout());
            toolbar.setOpaque(false);
            JLabel heading = new JLabel("Daftar Pengguna");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
            JButton add = button("+ Tambah Pengguna", SAGE_GREEN);
            add.addActionListener(e -> openUserDialog(-1));
            toolbar.add(heading, BorderLayout.WEST);
            toolbar.add(add, BorderLayout.EAST);
            card.add(toolbar, BorderLayout.NORTH);

            model = new DefaultTableModel(new String[]{"ID", "Nama Lengkap", "Email", "Role", "Status", "Aksi"}, 0) {
                public boolean isCellEditable(int r, int c) { return c == 5; }
            };
            JTable table = new JTable(model);
            styleTable(table);
            table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new UserActionEditor(this));
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
            card.add(new JScrollPane(table), BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        void loadData() {
            model.setRowCount(0);
            try {
                for (Database.UserAccount user : Database.getUsers()) {
                    model.addRow(new Object[]{Database.userNumber(user.id), user.fullName, user.email,
                            user.role, user.status, "Aksi"});
                }
            } catch (SQLException ex) {
                showMessage("Gagal memuat pengguna: " + ex.getMessage());
            }
        }

        void openUserDialog(int row) {
            boolean editing = row >= 0;
            JTextField name = field(), email = field();
            JTextField username = field();
            JPasswordField password = new JPasswordField();
            JComboBox<String> role = combo("Pelanggan", "Penjual", "Admin");
            JComboBox<String> status = combo("Aktif", "Nonaktif");

            if (editing) {
                name.setText(model.getValueAt(row, 1).toString());
                email.setText(model.getValueAt(row, 2).toString());
                role.setSelectedItem(model.getValueAt(row, 3).toString());
                status.setSelectedItem(model.getValueAt(row, 4).toString());
            }

            JPanel form = formPanel();
            form.add(formField("Nama Lengkap", name)); addGap(form);
            if (!editing) {
                form.add(formField("Username", username)); addGap(form);
                form.add(formField("Password (minimal 8 karakter)", password)); addGap(form);
            }
            form.add(formField("Email", email)); addGap(form);
            form.add(formField("Role", role)); addGap(form);
            form.add(formField("Status", status));

            showDialog(editing ? "Edit Pengguna" : "Tambah Pengguna", form, () -> {
                if (name.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
                    showMessage("Nama dan email wajib diisi.");
                    return false;
                }
                try {
                    if (editing) {
                        Database.updateUser(idFromLabel(model.getValueAt(row, 0).toString(), "USR-"),
                                name.getText().trim(), email.getText().trim(),
                                role.getSelectedItem().toString(), status.getSelectedItem().toString());
                    } else {
                        if (username.getText().trim().isEmpty()) {
                            showMessage("Username wajib diisi.");
                            return false;
                        }
                        Database.createUser(name.getText().trim(), username.getText().trim(),
                                email.getText().trim(), new String(password.getPassword()),
                                role.getSelectedItem().toString(), status.getSelectedItem().toString());
                    }
                    loadData();
                    updateStats();
                    return true;
                } catch (SQLException ex) {
                    showMessage("Gagal menyimpan pengguna: " + ex.getMessage());
                    return false;
                }
            });
        }

        void deleteUser(int row) {
            int answer = JOptionPane.showConfirmDialog(frame, "Hapus pengguna ini?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Database.deleteUser(idFromLabel(model.getValueAt(row, 0).toString(), "USR-"));
                    loadData();
                    updateStats();
                } catch (SQLException ex) {
                    showMessage("Gagal menghapus pengguna: " + ex.getMessage());
                }
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
            setBorder(new EmptyBorder(27, 30, 28, 30));

            JPanel stats = statsPanel(
                    statCard("Total Pesanan", total, DUSTY_ROSE),
                    statCard("Menunggu (Pending)", pending, RED),
                    statCard("Pesanan Selesai", completed, SAGE_GREEN));
            add(pageHeader("Manajemen Pesanan", stats), BorderLayout.NORTH);

            AdminRoundedPanel card = whiteCard();
            JPanel toolbar = new JPanel(new BorderLayout());
            toolbar.setOpaque(false);
            JLabel heading = new JLabel("Daftar Pesanan");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
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
            styleTable(table);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
            table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new OrderActionEditor(this));
            card.add(new JScrollPane(table), BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            loadData();
            updateStats();
        }

        void loadData() {
            model.setRowCount(0);
            try {
                for (Order order : Database.getOrders(null)) {
                    StringBuilder detail = new StringBuilder();
                    for (CartItem item : order.items) {
                        if (detail.length() > 0) detail.append(", ");
                        detail.append(item.book.title).append(" (x").append(item.qty).append(")");
                    }
                    model.addRow(new Object[]{order.id, order.customerName, detail.toString(),
                            rupiah(order.total), order.status, "Aksi"});
                }
            } catch (SQLException ex) {
                showMessage("Gagal memuat pesanan: " + ex.getMessage());
            }
        }

        void openOrderDialog(int row) {
            boolean editing = row >= 0;
            JTextField customer = field(), item = field(), price = field();
            JComboBox<String> status = combo("Pending", "Diproses", "Dikirim", "Selesai", "Dibatalkan");

            if (editing) {
                customer.setText(model.getValueAt(row, 1).toString());
                item.setText(model.getValueAt(row, 2).toString());
                price.setText(model.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""));
                status.setSelectedItem(model.getValueAt(row, 4).toString());
            }

            JPanel form = formPanel();
            form.add(formField("Nama Pelanggan", customer)); addGap(form);
            form.add(formField("Detail Item", item)); addGap(form);
            form.add(formField("Total Harga (Rp)", price)); addGap(form);
            form.add(formField("Status Pesanan", status));

            showDialog(editing ? "Edit Pesanan" : "Tambah Pesanan", form, () -> {
                try {
                    if (customer.getText().trim().isEmpty() || item.getText().trim().isEmpty())
                        throw new IllegalArgumentException("Nama pelanggan dan item wajib diisi.");
                    long amount = Long.parseLong(price.getText().trim());
                    if (amount < 0) throw new IllegalArgumentException("Harga tidak boleh negatif.");

                    String id = editing ? model.getValueAt(row, 0).toString() : null;
                    Database.saveAdminOrder(id, customer.getText().trim(), item.getText().trim(),
                            amount, status.getSelectedItem().toString());
                    loadData();
                    updateStats();
                    return true;
                } catch (NumberFormatException ex) {
                    showMessage("Total harga harus berupa angka.");
                } catch (IllegalArgumentException ex) {
                    showMessage(ex.getMessage());
                } catch (SQLException ex) {
                    showMessage("Gagal menyimpan pesanan: " + ex.getMessage());
                }
                return false;
            });
        }

        void deleteOrder(int row) {
            int answer = JOptionPane.showConfirmDialog(frame, "Hapus pesanan ini?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    Database.deleteOrder(model.getValueAt(row, 0).toString());
                    loadData();
                    updateStats();
                } catch (SQLException ex) {
                    showMessage("Gagal menghapus pesanan: " + ex.getMessage());
                }
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

    // =============================== RENDERER AKSI =============================

    static class ActionRenderer extends JPanel implements TableCellRenderer {
        JButton edit = button("Edit", SAGE_GREEN);
        JButton delete = button("Hapus", RED);

        ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 4));
            setBackground(WHITE);
            edit.setPreferredSize(new Dimension(62, 30));
            delete.setPreferredSize(new Dimension(65, 30));
            add(edit);
            add(delete);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            setBackground(selected ? SOFT_PINK : WHITE);
            return this;
        }
    }

    static class BookActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 4));
        JButton edit = button("Edit", SAGE_GREEN), delete = button("Hapus", RED);
        BookPanel owner;
        int row;

        BookActionEditor(BookPanel owner) {
            this.owner = owner;
            edit.setPreferredSize(new Dimension(62, 30));
            delete.setPreferredSize(new Dimension(65, 30));
            panel.setBackground(WHITE);
            panel.add(edit); panel.add(delete);
            edit.addActionListener(e -> { fireEditingStopped(); owner.editBook(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteBook(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r);
            return panel;
        }
        public Object getCellEditorValue() { return "Aksi"; }
    }

    static class UserActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 4));
        JButton edit = button("Edit", SAGE_GREEN), delete = button("Hapus", RED);
        UserPanel owner; int row;
        UserActionEditor(UserPanel owner) {
            this.owner = owner;
            edit.setPreferredSize(new Dimension(62, 30)); delete.setPreferredSize(new Dimension(65, 30));
            panel.setBackground(WHITE); panel.add(edit); panel.add(delete);
            edit.addActionListener(e -> { fireEditingStopped(); owner.openUserDialog(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteUser(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r); return panel;
        }
        public Object getCellEditorValue() { return "Aksi"; }
    }

    static class OrderActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 4));
        JButton edit = button("Edit", SAGE_GREEN), delete = button("Hapus", RED);
        OrderPanel owner; int row;
        OrderActionEditor(OrderPanel owner) {
            this.owner = owner;
            edit.setPreferredSize(new Dimension(62, 30)); delete.setPreferredSize(new Dimension(65, 30));
            panel.setBackground(WHITE); panel.add(edit); panel.add(delete);
            edit.addActionListener(e -> { fireEditingStopped(); owner.openOrderDialog(row); });
            delete.addActionListener(e -> { fireEditingStopped(); owner.deleteOrder(row); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = t.convertRowIndexToModel(r); return panel;
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
                Image scaled = icon.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            } else {
                label.setText("No image");
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
            if (status.equals("Aktif") || status.equals("Selesai") || status.equals("Diproses")) {
                label.setForeground(new Color(48, 105, 65));
                label.setBackground(new Color(226, 242, 224));
            } else {
                label.setForeground(new Color(155, 75, 75));
                label.setBackground(new Color(255, 231, 228));
            }
            label.setOpaque(true);
            return label;
        }
    }
}

class AdminRoundedPanel extends JPanel {
    private final int radius;
    private final Color background;

    AdminRoundedPanel(int radius, Color background) {
        this.radius = radius;
        this.background = background;
        setOpaque(false);
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g.dispose();
        super.paintComponent(graphics);
    }
}


    
