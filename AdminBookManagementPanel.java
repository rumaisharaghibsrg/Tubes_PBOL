import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminBookManagementPanel extends JPanel {

    // Palet Warna Elegan (Sama dengan punya Syifa agar konsisten)
    static final Color COLOR_DUSTY_ROSE  = new Color(214, 153, 155);
    static final Color COLOR_SOFT_PINK   = new Color(254, 226, 228);
    static final Color COLOR_LIGHT_CREAM = new Color(254, 249, 219);
    static final Color COLOR_MUTED_GREEN = new Color(138, 159, 107);
    static final Color COLOR_CARD_BG     = new Color(255, 255, 255);
    static final Color TEXT_DARK         = new Color(31, 41, 55);
    static final Color TEXT_MUTED        = new Color(107, 114, 128);

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch, txtJudul, txtPenulis, txtHarga, txtStok;
    private JComboBox<String> cbKategori;
    private JLabel lblTotalBuku, lblStokMenipis, lblTotalKategori, lblNilaiInventaris;

    public AdminBookManagementPanel(JFrame parentFrame) {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_SOFT_PINK);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ================= 1. TOP HEADER & STAT CARDS =================
        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);

        JLabel titleLabel = new JLabel("Dashboard Admin — Manajemen Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subTitleLabel = new JLabel("Kelola katalog buku, perbarui stok, dan pantau ketersediaan inventaris BukaBuku.");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(TEXT_MUTED);

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBox.add(subTitleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        lblTotalBuku = new JLabel("0");
        lblStokMenipis = new JLabel("0");
        lblTotalKategori = new JLabel("0");
        lblNilaiInventaris = new JLabel("Rp 0");

        statsPanel.add(createStatCard("Total Judul Buku", lblTotalBuku, COLOR_LIGHT_CREAM, COLOR_DUSTY_ROSE));
        statsPanel.add(createStatCard("Stok Menipis (<5)", lblStokMenipis, COLOR_SOFT_PINK, new Color(239, 68, 68))); // Merah untuk warning
        statsPanel.add(createStatCard("Total Kategori", lblTotalKategori, COLOR_LIGHT_CREAM, COLOR_MUTED_GREEN));
        statsPanel.add(createStatCard("Estimasi Nilai Inventaris", lblNilaiInventaris, COLOR_LIGHT_CREAM, COLOR_DUSTY_ROSE));

        topSection.add(titleBox, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.SOUTH);
        add(topSection, BorderLayout.NORTH);

        // ================= 2. LEFT SIDEBAR: FORM CARD =================
        RoundedPanel formCard = new RoundedPanel(20, COLOR_CARD_BG);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setPreferredSize(new Dimension(320, 0));

        JLabel formTitle = new JLabel(" Form Data Buku");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtJudul = new JTextField();
        txtPenulis = new JTextField();
        txtHarga = new JTextField();
        txtStok = new JTextField();
        cbKategori = new JComboBox<>(new String[]{"Fiksi", "Non-Fiksi", "Pendidikan", "Komik", "Biografi"});

        JPanel judulWrapper = createInputWrapper("Judul Buku", txtJudul);
        JPanel penulisWrapper = createInputWrapper("Penulis", txtPenulis);
        JPanel kategoriWrapper = createComboWrapper("Kategori", cbKategori);
        JPanel hargaWrapper = createInputWrapper("Harga (Rp)", txtHarga);
        JPanel stokWrapper = createInputWrapper("Stok Tersedia", txtStok);

        RoundedButton btnAdd = new RoundedButton("+ Tambah Buku", COLOR_MUTED_GREEN, Color.WHITE);
        RoundedButton btnUpdate = new RoundedButton(" Simpan Perubahan", COLOR_DUSTY_ROSE, Color.WHITE);
        RoundedButton btnClear = new RoundedButton(" Reset Form", COLOR_LIGHT_CREAM, TEXT_DARK);
        RoundedButton btnDelete = new RoundedButton(" Hapus Buku", new Color(239, 68, 68), Color.WHITE);

        setMaxSizeAndAlign(btnAdd, btnUpdate, btnClear, btnDelete);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(judulWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(penulisWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(kategoriWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(hargaWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(stokWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(btnAdd); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(btnUpdate); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(btnDelete); formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(btnClear);

        add(formCard, BorderLayout.WEST);

        // ================= 3. CENTER: DATA TABLE CARD =================
        RoundedPanel tableCard = new RoundedPanel(20, COLOR_CARD_BG);
        tableCard.setLayout(new BorderLayout(15, 15));
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setOpaque(false);

        JPanel searchInputBox = new RoundedPanel(15, COLOR_SOFT_PINK);
        searchInputBox.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtSearch = new JTextField("Cari berdasarkan judul atau penulis...");
        txtSearch.setBorder(null);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(280, 25));
        txtSearch.setOpaque(false);
        txtSearch.setForeground(TEXT_DARK);

        searchInputBox.add(new JLabel("🔍"));
        searchInputBox.add(txtSearch);
        searchBarPanel.add(searchInputBox, BorderLayout.WEST);
        tableCard.add(searchBarPanel, BorderLayout.NORTH);

        String[] columns = {"ID Buku", "Judul", "Penulis", "Kategori", "Stok", "Harga"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        bookTable = new JTable(tableModel);
        setupTableStyle(bookTable);

        // Custom Renderer untuk Stok (Merah jika < 5)
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new StockBadgeRenderer());

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        loadDummyData();

        // Event Listeners
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row != -1) {
                txtJudul.setText(tableModel.getValueAt(row, 1).toString());
                txtPenulis.setText(tableModel.getValueAt(row, 2).toString());
                cbKategori.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                txtStok.setText(tableModel.getValueAt(row, 4).toString());
                txtHarga.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearForm());
    }

    // --- HELPER METHODS (Sama persis logic-nya dengan Syifa) ---
    private JPanel createStatCard(String title, JLabel valueLabel, Color bg, Color accent) {
        RoundedPanel card = new RoundedPanel(18, bg);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(TEXT_MUTED);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accent);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void updateStats() {
        int total = tableModel.getRowCount();
        int menipis = 0;
        long totalNilai = 0;
        
        for (int i = 0; i < total; i++) {
            int stok = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
            int harga = Integer.parseInt(tableModel.getValueAt(i, 5).toString().replace("Rp ", "").replace(".", ""));
            if (stok < 5) menipis++;
            totalNilai += (stok * harga);
        }
        
        lblTotalBuku.setText(String.valueOf(total));
        lblStokMenipis.setText(String.valueOf(menipis));
        lblTotalKategori.setText("5"); // Dummy kategori
        lblNilaiInventaris.setText("Rp " + String.format("%,d", totalNilai));
    }

    private void addBook() {
        if (txtJudul.getText().trim().isEmpty() || txtStok.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan Stok wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newId = "BK-" + (tableModel.getRowCount() + 100);
        tableModel.addRow(new Object[]{newId, txtJudul.getText(), txtPenulis.getText(), cbKategori.getSelectedItem(), txtStok.getText(), "Rp " + txtHarga.getText()});
        clearForm();
        updateStats();
    }

    private void updateBook() {
        int row = bookTable.getSelectedRow();
        if (row != -1) {
            tableModel.setValueAt(txtJudul.getText(), row, 1);
            tableModel.setValueAt(txtPenulis.getText(), row, 2);
            tableModel.setValueAt(cbKategori.getSelectedItem(), row, 3);
            tableModel.setValueAt(txtStok.getText(), row, 4);
            tableModel.setValueAt("Rp " + txtHarga.getText(), row, 5);
            clearForm();
            updateStats();
        }
    }

    private void deleteBook() {
        int row = bookTable.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
            clearForm();
            updateStats();
        }
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"BK-101", "Laskar Pelangi", "Andrea Hirata", "Fiksi", "12", "Rp 85.000"});
        tableModel.addRow(new Object[]{"BK-102", "Atomic Habits", "James Clear", "Non-Fiksi", "3", "Rp 108.000"}); // Stok menipis
        tableModel.addRow(new Object[]{"BK-103", "Matematika Dasar", "Tim Erlangga", "Pendidikan", "25", "Rp 75.000"});
        updateStats();
    }

    private void clearForm() {
        txtJudul.setText(""); txtPenulis.setText(""); txtHarga.setText(""); txtStok.setText("");
        cbKategori.setSelectedIndex(0);
        bookTable.clearSelection();
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
        inputField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_DUSTY_ROSE, 1, true), new EmptyBorder(5, 8, 5, 8)));
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
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 4)));
        wrapper.add(comboBox);
        return wrapper;
    }

    private void setMaxSizeAndAlign(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
    }

    private void setupTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(COLOR_MUTED_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.setSelectionBackground(COLOR_SOFT_PINK);
        table.setSelectionForeground(TEXT_DARK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }

    // --- MAIN METHOD UNTUK TESTING ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test UI - Manajemen Buku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1180, 720);
        frame.add(new AdminBookManagementPanel(frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// ================== BADGE RENDERER STOK ==================
class StockBadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        int stok = Integer.parseInt(value.toString());
        if (stok < 5) {
            label.setForeground(new Color(153, 27, 27));
            label.setBackground(new Color(254, 226, 226));
            label.setText("⚠️ " + stok);
        } else {
            label.setForeground(new Color(22, 101, 52));
            label.setBackground(new Color(220, 252, 231));
            label.setText("✅ " + stok);
        }
        label.setOpaque(true);
        return label;
    }
}

