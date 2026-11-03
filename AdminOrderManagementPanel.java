import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminOrderManagementPanel extends JPanel {

    static final Color COLOR_DUSTY_ROSE  = new Color(214, 153, 155);
    static final Color COLOR_SOFT_PINK   = new Color(254, 226, 228);
    static final Color COLOR_LIGHT_CREAM = new Color(254, 249, 219);
    static final Color COLOR_MUTED_GREEN = new Color(138, 159, 107);
    static final Color COLOR_CARD_BG     = new Color(255, 255, 255);
    static final Color TEXT_DARK         = new Color(31, 41, 55);
    static final Color TEXT_MUTED        = new Color(107, 114, 128);

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch, txtNoOrder, txtPelanggan, txtTotal;
    private JComboBox<String> cbStatus;
    private JLabel lblTotalOrder, lblPending, lblDiproses, lblSelesai;

    public AdminOrderManagementPanel(JFrame parentFrame) {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_SOFT_PINK);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ================= 1. TOP HEADER & STAT CARDS =================
        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);

        JLabel titleLabel = new JLabel("Dashboard Admin — Manajemen Pesanan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subTitleLabel = new JLabel("Pantau pesanan masuk, verifikasi pembayaran, dan perbarui status pengiriman.");
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

        lblTotalOrder = new JLabel("0");
        lblPending = new JLabel("0");
        lblDiproses = new JLabel("0");
        lblSelesai = new JLabel("0");

        statsPanel.add(createStatCard("Total Pesanan", lblTotalOrder, COLOR_LIGHT_CREAM, COLOR_DUSTY_ROSE));
        statsPanel.add(createStatCard("Menunggu (Pending)", lblPending, COLOR_SOFT_PINK, new Color(239, 68, 68)));
        statsPanel.add(createStatCard("Sedang Diproses", lblDiproses, COLOR_LIGHT_CREAM, new Color(217, 119, 6)));
        statsPanel.add(createStatCard("Pesanan Selesai", lblSelesai, COLOR_LIGHT_CREAM, COLOR_MUTED_GREEN));

        topSection.add(titleBox, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.SOUTH);
        add(topSection, BorderLayout.NORTH);

        // ================= 2. LEFT SIDEBAR: FORM CARD =================
        RoundedPanel formCard = new RoundedPanel(20, COLOR_CARD_BG);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setPreferredSize(new Dimension(320, 0));

        JLabel formTitle = new JLabel(" Detail & Update Status");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNoOrder = new JTextField();
        txtNoOrder.setEditable(false); 
        txtNoOrder.setBackground(new Color(245, 245, 245));
        txtPelanggan = new JTextField();
        txtPelanggan.setEditable(false);
        txtPelanggan.setBackground(new Color(245, 245, 245));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(245, 245, 245));
        
        cbStatus = new JComboBox<>(new String[]{"Pending", "Diproses", "Dikirim", "Selesai", "Dibatalkan"});

        JPanel noOrderWrapper = createInputWrapper("No. Order", txtNoOrder);
        JPanel pelangganWrapper = createInputWrapper("Nama Pelanggan", txtPelanggan);
        JPanel totalWrapper = createInputWrapper("Total Harga", txtTotal);
        JPanel statusWrapper = createComboWrapper("Ubah Status Pesanan", cbStatus);

        RoundedButton btnUpdate = new RoundedButton("💾 Simpan Perubahan Status", COLOR_MUTED_GREEN, Color.WHITE);
        RoundedButton btnClear = new RoundedButton("Batal Pilih", COLOR_LIGHT_CREAM, TEXT_DARK);

        btnUpdate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(noOrderWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(pelangganWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(totalWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(statusWrapper); formCard.add(Box.createRigidArea(new Dimension(0, 25)));
        formCard.add(btnUpdate); formCard.add(Box.createRigidArea(new Dimension(0, 10)));
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
        txtSearch = new JTextField("Cari No. Order atau Nama Pelanggan...");
        txtSearch.setBorder(null);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(280, 25));
        txtSearch.setOpaque(false);
        txtSearch.setForeground(TEXT_DARK);

        searchInputBox.add(new JLabel("🔍"));
        searchInputBox.add(txtSearch);
        searchBarPanel.add(searchInputBox, BorderLayout.WEST);
        tableCard.add(searchBarPanel, BorderLayout.NORTH);

        String[] columns = {"No. Order", "Tanggal", "Pelanggan", "Item", "Total Harga", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orderTable.setRowHeight(40);
        orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        orderTable.getTableHeader().setBackground(COLOR_MUTED_GREEN);
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        orderTable.setSelectionBackground(COLOR_SOFT_PINK);
        orderTable.setSelectionForeground(TEXT_DARK);
        orderTable.setShowGrid(false);
        orderTable.setIntercellSpacing(new Dimension(0, 0));

        orderTable.getColumnModel().getColumn(5).setCellRenderer(new OrderStatusBadgeRenderer());

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        loadDummyData();

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row != -1) {
                txtNoOrder.setText(tableModel.getValueAt(row, 0).toString());
                txtPelanggan.setText(tableModel.getValueAt(row, 2).toString());
                txtTotal.setText(tableModel.getValueAt(row, 4).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
            }
        });

        btnUpdate.addActionListener(e -> updateOrderStatus());
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

    private void updateStats() {
        int total = tableModel.getRowCount();
        int pending = 0, diproses = 0, selesai = 0;
        for (int i = 0; i < total; i++) {
            String status = tableModel.getValueAt(i, 5).toString();
            if ("Pending".equals(status)) pending++;
            else if ("Diproses".equals(status) || "Dikirim".equals(status)) diproses++;
            else if ("Selesai".equals(status)) selesai++;
        }
        lblTotalOrder.setText(String.valueOf(total));
        lblPending.setText(String.valueOf(pending));
        lblDiproses.setText(String.valueOf(diproses));
        lblSelesai.setText(String.valueOf(selesai));
    }

    private void updateOrderStatus() {
        int row = orderTable.getSelectedRow();
        if (row != -1) {
            tableModel.setValueAt(cbStatus.getSelectedItem(), row, 5);
            JOptionPane.showMessageDialog(this, "Status pesanan berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            updateStats();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pesanan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"ORD-2026001", "03 Nov 2026", "Budi Santoso", "Laskar Pelangi (x1)", "Rp 85.000", "Pending"});
        tableModel.addRow(new Object[]{"ORD-2026002", "02 Nov 2026", "Siti Aminah", "Atomic Habits (x2)", "Rp 216.000", "Diproses"});
        tableModel.addRow(new Object[]{"ORD-2026003", "01 Nov 2026", "Jelita Hati", "Matematika Dasar (x1)", "Rp 75.000", "Selesai"});
        updateStats();
    }

    private void clearForm() {
        txtNoOrder.setText(""); txtPelanggan.setText(""); txtTotal.setText("");
        cbStatus.setSelectedIndex(0);
        orderTable.clearSelection();
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test UI - Manajemen Pesanan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1180, 720);
        frame.add(new AdminOrderManagementPanel(frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// ================== BADGE RENDERER STATUS PESANAN ==================
class OrderStatusBadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setOpaque(true);

        String status = String.valueOf(value);
        switch (status) {
            case "Pending":
                label.setForeground(new Color(153, 27, 27));
                label.setBackground(new Color(254, 226, 226));
                break;
            case "Diproses":
            case "Dikirim":
                label.setForeground(new Color(146, 64, 14));
                label.setBackground(new Color(255, 237, 213));
                break;
            case "Selesai":
                label.setForeground(new Color(22, 101, 52));
                label.setBackground(new Color(220, 252, 231));
                break;
            default:
                label.setForeground(TEXT_MUTED);
                label.setBackground(COLOR_LIGHT_CREAM);
        }
        return label;
    }
}
