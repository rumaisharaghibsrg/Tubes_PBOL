import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminUserManagementPanel extends JPanel {

    // Palet Warna Elegan (Soft Modern Theme)
    static final Color COLOR_DUSTY_ROSE  = new Color(214, 153, 155); // #D6999B
    static final Color COLOR_SOFT_PINK   = new Color(254, 226, 228); // #FEE2E4
    static final Color COLOR_LIGHT_CREAM = new Color(254, 249, 219); // #FEF9DB
    static final Color COLOR_MUTED_GREEN = new Color(138, 159, 107); // #8A9F6B
    
    static final Color COLOR_CARD_BG    = new Color(255, 255, 255);
    static final Color TEXT_DARK        = new Color(31, 41, 55);
    static final Color TEXT_MUTED       = new Color(107, 114, 128);

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch, txtName, txtEmail;
    private JComboBox<String> cbRole, cbStatus;
    private JLabel lblTotalUsers, lblActiveUsers, lblSellerUsers, lblAdminUsers;

    public AdminUserManagementPanel(JFrame parentFrame) {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_SOFT_PINK);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ================= 1. TOP HEADER & STAT CARDS =================
        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);

        // Header Title
        JLabel titleLabel = new JLabel("Dashboard Admin — Manajemen Pengguna");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subTitleLabel = new JLabel("Pantau statistik pengguna, kelola akun penjual & pelanggan, serta atur hak akses BukaBuku.");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(TEXT_MUTED);

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBox.add(subTitleLabel);

        // Stat Cards Panel (4 Kartu Ringkasan Data)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        lblTotalUsers = new JLabel("0");
        lblActiveUsers = new JLabel("0");
        lblSellerUsers = new JLabel("0");
        lblAdminUsers = new JLabel("0");

        statsPanel.add(createStatCard("Total Pengguna", lblTotalUsers, COLOR_LIGHT_CREAM, COLOR_DUSTY_ROSE));
        statsPanel.add(createStatCard("Pengguna Aktif", lblActiveUsers, COLOR_LIGHT_CREAM, COLOR_MUTED_GREEN));
        statsPanel.add(createStatCard("Total Penjual", lblSellerUsers, COLOR_LIGHT_CREAM, COLOR_DUSTY_ROSE));
        statsPanel.add(createStatCard("Total Admin", lblAdminUsers, COLOR_LIGHT_CREAM, COLOR_MUTED_GREEN));

        topSection.add(titleBox, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.SOUTH);
        add(topSection, BorderLayout.NORTH);

        // ================= 2. LEFT SIDEBAR: FORM CARD =================
        RoundedPanel formCard = new RoundedPanel(20, COLOR_CARD_BG);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setPreferredSize(new Dimension(310, 0));

        JLabel formTitle = new JLabel(" Form Kelola User");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField();
        txtEmail = new JTextField();

        // 🟢 PERUBAHAN: Menambahkan pilihan "Penjual" pada ComboBox Role
        cbRole = new JComboBox<>(new String[]{"Pelanggan", "Penjual", "Admin"});
        cbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});

        styleComboBox(cbRole);
        styleComboBox(cbStatus);

        JPanel nameWrapper = createInputWrapper("Nama Lengkap", txtName);
        JPanel emailWrapper = createInputWrapper("Email", txtEmail);
        JPanel roleWrapper = createComboWrapper("Role Akses", cbRole);
        JPanel statusWrapper = createComboWrapper("Status Akun", cbStatus);

        // Action Buttons
        RoundedButton btnAdd = new RoundedButton("+ Tambah User Baru", COLOR_MUTED_GREEN, Color.WHITE);
        RoundedButton btnUpdate = new RoundedButton(" Simpan Perubahan", COLOR_DUSTY_ROSE, Color.WHITE);
        RoundedButton btnClear = new RoundedButton(" Reset Form", COLOR_LIGHT_CREAM, TEXT_DARK);

        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnUpdate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(nameWrapper);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(emailWrapper);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(roleWrapper);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(statusWrapper);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(btnAdd);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(btnUpdate);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(btnClear);

        add(formCard, BorderLayout.WEST);

        // ================= 3. CENTER: DATA TABLE CARD =================
        RoundedPanel tableCard = new RoundedPanel(20, COLOR_CARD_BG);
        tableCard.setLayout(new BorderLayout(15, 15));
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Filter & Search Header
        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setOpaque(false);

        JPanel searchInputBox = new RoundedPanel(15, COLOR_SOFT_PINK);
        searchInputBox.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtSearch = new JTextField("Cari berdasarkan nama/email...");
        txtSearch.setBorder(null);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(260, 25));
        txtSearch.setOpaque(false);
        txtSearch.setForeground(TEXT_DARK);

        searchInputBox.add(new JLabel("🔍"));
        searchInputBox.add(txtSearch);

        RoundedButton btnDelete = new RoundedButton(" Hapus User", new Color(239, 68, 68), Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(120, 35));

        searchBarPanel.add(searchInputBox, BorderLayout.WEST);
        searchBarPanel.add(btnDelete, BorderLayout.EAST);

        tableCard.add(searchBarPanel, BorderLayout.NORTH);

        // Setup Tabel
        String[] columns = {"ID User", "Nama Lengkap", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userTable.setRowHeight(40);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(COLOR_MUTED_GREEN);
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        userTable.setSelectionBackground(COLOR_SOFT_PINK);
        userTable.setSelectionForeground(TEXT_DARK);
        userTable.setShowGrid(false);
        userTable.setIntercellSpacing(new Dimension(0, 0));

        // Center alignment untuk ID dan Role
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        userTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        userTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Status Badge Custom Renderer
        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        // Load Data Dummy & Updates Stats
        loadDummyData();

        // Event Listeners
        userTable.getSelectionModel().addListSelectionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row != -1) {
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                cbRole.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            }
        });

        btnAdd.addActionListener(e -> addNewUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearForm());
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color bg, Color accent) {
        RoundedPanel card = new RoundedPanel(18, bg);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accent);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // 🟢 PERUBAHAN: Menghitung statistik untuk Penjual juga
    private void updateStats() {
        int total = tableModel.getRowCount();
        int active = 0;
        int seller = 0;
        int admin = 0;

        for (int i = 0; i < total; i++) {
            if ("Aktif".equals(tableModel.getValueAt(i, 4))) active++;
            if ("Penjual".equals(tableModel.getValueAt(i, 3))) seller++;
            if ("Admin".equals(tableModel.getValueAt(i, 3))) admin++;
        }

        lblTotalUsers.setText(String.valueOf(total));
        lblActiveUsers.setText(String.valueOf(active));
        lblSellerUsers.setText(String.valueOf(seller));
        lblAdminUsers.setText(String.valueOf(admin));
    }

    private void addNewUser() {
        if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi nama dan email terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newId = "USR-00" + (tableModel.getRowCount() + 1);
        tableModel.addRow(new Object[]{newId, txtName.getText(), txtEmail.getText(), cbRole.getSelectedItem(), cbStatus.getSelectedItem()});
        clearForm();
        updateStats();
    }

    private void updateUser() {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            tableModel.setValueAt(txtName.getText(), row, 1);
            tableModel.setValueAt(txtEmail.getText(), row, 2);
            tableModel.setValueAt(cbRole.getSelectedItem(), row, 3);
            tableModel.setValueAt(cbStatus.getSelectedItem(), row, 4);
            clearForm();
            updateStats();
        }
    }

    private void deleteUser() {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
            clearForm();
            updateStats();
        }
    }

    // 🟢 PERUBAHAN: Menambahkan contoh data dummy dengan role "Penjual"
    private void loadDummyData() {
        tableModel.addRow(new Object[]{"USR-001", "Syifa Nazira", "syifa@bukabuku.com", "Admin", "Aktif"});
        tableModel.addRow(new Object[]{"USR-002", "Jelita Hati", "jelita@bukabuku.com", "Admin", "Aktif"});
        tableModel.addRow(new Object[]{"USR-003", "Toko Buku Gramedia", "gramedia@seller.com", "Penjual", "Aktif"});
        tableModel.addRow(new Object[]{"USR-004", "Penerbit Erlangga", "erlangga@seller.com", "Penjual", "Aktif"});
        tableModel.addRow(new Object[]{"USR-005", "Budi Santoso", "budi@gmail.com", "Pelanggan", "Aktif"});
        tableModel.addRow(new Object[]{"USR-006", "Siti Aminah", "siti@yahoo.com", "Pelanggan", "Nonaktif"});
        updateStats();
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        cbRole.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        userTable.clearSelection();
    }

    private JPanel createInputWrapper(String labelText, JTextField inputField) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);

        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_DUSTY_ROSE, 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));

        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 4)));
        wrapper.add(inputField);
        return wrapper;
    }

    private JPanel createComboWrapper(String labelText, JComboBox<String> comboBox) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);

        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 4)));
        wrapper.add(comboBox);
        return wrapper;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test UI Admin Dashboard - BukaBuku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1180, 720);
        frame.add(new AdminUserManagementPanel(frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// ================== BADGE RENDERER STATUS ==================
class StatusBadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));

        String status = String.valueOf(value);
        if ("Aktif".equalsIgnoreCase(status)) {
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

// ================== HELPER UI CUSTOM CLASSES ==================
class RoundedPanel extends JPanel {
    private int radius;
    private Color bgColor;

    public RoundedPanel(int radius, Color bgColor) {
        this.radius = radius;
        this.bgColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}

class RoundedButton extends JButton {
    private Color bg;

    public RoundedButton(String text, Color bg, Color fg) {
        super(text);
        this.bg = bg;
        setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.dispose();
        super.paintComponent(g);
    }
}