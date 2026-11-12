import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class AdminDashboardApp {

    // Palet Warna Elegan (Soft Pastel) Kelompok 6
    static final Color COLOR_DUSTY_ROSE  = new Color(214, 153, 155);
    static final Color COLOR_SOFT_PINK   = new Color(254, 226, 228);
    static final Color COLOR_LIGHT_CREAM = new Color(254, 249, 219);
    static final Color COLOR_MUTED_GREEN = new Color(138, 159, 107);
    static final Color COLOR_CARD_BG     = new Color(255, 255, 255);
    static final Color TEXT_DARK         = new Color(31, 41, 55);
    static final Color TEXT_MUTED        = new Color(107, 114, 128);
    static final Color SIDEBAR_BG        = new Color(200, 135, 137);

    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);
    static JLabel navUser, navBuku, navOrder;
    static JFrame mainFrame; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    static void createAndShowGUI() {
        mainFrame = new JFrame("BukaBuku - Admin Dashboard");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 800);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(COLOR_SOFT_PINK);

        // --- SIDEBAR NAVIGASI KIRI ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(40, 0, 20, 0)); // Padding atas, nol di sisi agar menu full width

        // Teks Judul Sidebar (Tanpa Logo Gambar)
        JLabel logoText = new JLabel("BukaBuku Admin");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata Tengah
        
        sidebar.add(logoText);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40))); // Jarak pas di bawah teks

        navUser = createMenuLabel("Manajemen Pengguna", "User");
        navBuku = createMenuLabel("Katalog & Stok Buku", "Buku");
        navOrder = createMenuLabel("Pesanan Masuk", "Order");

        sidebar.add(navUser);
        sidebar.add(navBuku);
        sidebar.add(navOrder);
        
        // Mendorong sidebar ke atas
        sidebar.add(Box.createVerticalGlue());

        // --- MENGISI CARD LAYOUT ---
        mainPanel.add(new UserPanel(), "User");
        mainPanel.add(new BookPanel(), "Buku");
        mainPanel.add(new OrderPanel(), "Order");

        mainFrame.add(sidebar, BorderLayout.WEST);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setLocationRelativeTo(null);
        
        switchMenu("Buku"); 
        mainFrame.setVisible(true);
    }

    // Perbaikan Glitch Background Menu
    static JLabel createMenuLabel(String text, String cardName) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(COLOR_SOFT_PINK);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Teks dan Label diposisikan rata tengah
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        label.setBorder(new EmptyBorder(15, 10, 15, 10));
        label.setOpaque(true); 
        label.setBackground(SIDEBAR_BG); // Warna solid untuk mencegah glitch berbayang
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Lebar penuh

        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchMenu(cardName); }
            public void mouseEntered(MouseEvent e) { 
                if(label.getBackground().equals(SIDEBAR_BG)) {
                    label.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(MouseEvent e) { 
                if(label.getBackground().equals(SIDEBAR_BG)) {
                    label.setForeground(COLOR_SOFT_PINK); 
                }
            }
        });
        return label;
    }

    static void switchMenu(String cardName) {
        cardLayout.show(mainPanel, cardName);
        resetMenu(navUser);
        resetMenu(navBuku);
        resetMenu(navOrder);
        
        if (cardName.equals("User")) setActiveMenu(navUser);
        if (cardName.equals("Buku")) setActiveMenu(navBuku);
        if (cardName.equals("Order")) setActiveMenu(navOrder);
    }
    
    static void resetMenu(JLabel lbl) {
        lbl.setForeground(COLOR_SOFT_PINK);
        lbl.setBackground(SIDEBAR_BG); // Kembali ke warna dasar sidebar
    }
    
    static void setActiveMenu(JLabel lbl) {
        lbl.setForeground(TEXT_DARK);          
        lbl.setBackground(COLOR_LIGHT_CREAM);  // Highlight Kuning Air Muda
    }

    // ==============================================================================
    // =========================== PANEL MANAJEMEN BUKU =============================
    // ==============================================================================
    static class BookPanel extends JPanel {
        DefaultTableModel tableModel;
        JTable table;
        JTextField txtID, txtJudul, txtPenulis, txtHarga, txtStok, txtGambar;
        JComboBox<String> cbKategori;
        JLabel lblTotal, lblMenipis, lblNilai;
        RoundedButton btnAdd, btnUpdate, btnClear, btnDelete;

        public BookPanel() {
            setLayout(new BorderLayout(30, 25));
            setBackground(COLOR_SOFT_PINK);
            setBorder(new EmptyBorder(30, 35, 30, 35));

            JPanel topPanel = new JPanel(new BorderLayout(0, 20));
            topPanel.setOpaque(false);
            
            RoundedPanel titleBanner = new RoundedPanel(20, COLOR_LIGHT_CREAM);
            titleBanner.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
            JLabel title = new JLabel("Katalog & Stok Buku");
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setForeground(TEXT_DARK);
            titleBanner.add(title);
            topPanel.add(titleBanner, BorderLayout.NORTH);

            JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
            stats.setOpaque(false);
            lblTotal = new JLabel("0"); lblMenipis = new JLabel("0"); lblNilai = new JLabel("Rp 0");
            
            stats.add(createStatCard("Total Judul Buku", lblTotal, COLOR_CARD_BG, COLOR_DUSTY_ROSE));
            stats.add(createStatCard("Stok Menipis (< 5)", lblMenipis, COLOR_CARD_BG, new Color(220, 38, 38)));
            stats.add(createStatCard("Estimasi Nilai Aset", lblNilai, COLOR_CARD_BG, COLOR_MUTED_GREEN));
            topPanel.add(stats, BorderLayout.SOUTH);
            add(topPanel, BorderLayout.NORTH);

            // --- KIRI: FORM INPUT BUKU DENGAN SCROLLPANE ---
            RoundedPanel formContainer = new RoundedPanel(20, COLOR_CARD_BG);
            formContainer.setLayout(new BorderLayout());
            formContainer.setBorder(new EmptyBorder(10, 5, 10, 5));
            formContainer.setPreferredSize(new Dimension(340, 0));

            JPanel formContent = new JPanel();
            formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
            formContent.setOpaque(false);
            formContent.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel formTitle = new JLabel("Detail Buku");
            formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            formTitle.setForeground(TEXT_DARK);
            formTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Form Title Rata Tengah
            formContent.add(formTitle);
            formContent.add(Box.createRigidArea(new Dimension(0, 20)));

            txtID = new JTextField(); txtID.setEditable(false); txtID.setBackground(new Color(243, 244, 246));
            txtJudul = new JTextField(); 
            txtPenulis = new JTextField(); 
            txtHarga = new JTextField(); 
            txtStok = new JTextField();
            txtGambar = new JTextField("assets/Bumi.jpeg"); 
            cbKategori = new JComboBox<>(new String[]{"Fiksi Petualangan", "Fiksi Ilmiah & Romansa", "Fiksi Inspiratif", "Fiksi Pendidikan", "Self Improvement", "Drama Keluarga", "Fiksi Psikologis"});

            // Komponen Form Rata Tengah
            formContent.add(createInputWrapper("ID Buku", txtID));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Judul Buku", txtJudul));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Penulis", txtPenulis));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createComboWrapper("Kategori", cbKategori));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Stok", txtStok));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Harga (Rp)", txtHarga));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createFilePickerWrapper("Foto Buku", txtGambar, mainFrame));
            formContent.add(Box.createRigidArea(new Dimension(0, 25)));

            JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            btnPanel.setOpaque(false);
            btnPanel.setMaximumSize(new Dimension(270, 130)); // Dibatasi lebarnya agar rapi di tengah
            btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata Tengah
            
            btnAdd = new RoundedButton("Tambah", COLOR_MUTED_GREEN, Color.WHITE);
            btnUpdate = new RoundedButton("Update", COLOR_DUSTY_ROSE, Color.WHITE);
            btnDelete = new RoundedButton("Hapus", new Color(220, 38, 38), Color.WHITE);
            btnClear = new RoundedButton("Bersihkan", COLOR_LIGHT_CREAM, TEXT_DARK);

            btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
            btnPanel.add(btnDelete); btnPanel.add(btnClear);
            formContent.add(btnPanel);
            
            JScrollPane formScroll = new JScrollPane(formContent);
            formScroll.setBorder(null);
            formScroll.setOpaque(false);
            formScroll.getViewport().setOpaque(false);
            formScroll.getVerticalScrollBar().setUnitIncrement(16);
            formContainer.add(formScroll, BorderLayout.CENTER);
            
            add(formContainer, BorderLayout.WEST);

            // --- TENGAH: TABEL BUKU DENGAN GAMBAR ---
            RoundedPanel tableCard = new RoundedPanel(20, COLOR_CARD_BG);
            tableCard.setLayout(new BorderLayout(0, 0));
            tableCard.setBorder(new EmptyBorder(15, 15, 15, 15));

            String[] columns = {"Gambar", "ID", "Judul", "Penulis", "Kategori", "Stok", "Harga", "Path"};
            tableModel = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            table = new JTable(tableModel);
            setupTableStyle(table);
            
            table.setRowHeight(80); 
            table.getColumnModel().getColumn(0).setPreferredWidth(70);
            table.getColumnModel().getColumn(1).setPreferredWidth(60);
            table.getColumnModel().getColumn(2).setPreferredWidth(180);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setPreferredWidth(50);
            table.getColumnModel().getColumn(6).setPreferredWidth(110);
            table.getColumnModel().getColumn(7).setMinWidth(0);
            table.getColumnModel().getColumn(7).setMaxWidth(0); 

            table.getColumnModel().getColumn(0).setCellRenderer(new ImageTableCellRenderer());
            table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    lbl.setHorizontalAlignment(CENTER);
                    if(Integer.parseInt(value.toString()) < 5) {
                        lbl.setForeground(new Color(220, 38, 38)); lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    } else {
                        lbl.setForeground(TEXT_DARK); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    }
                    return lbl;
                }
            });

            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                        int row = table.getSelectedRow();
                        txtID.setText(tableModel.getValueAt(row, 1).toString());
                        txtJudul.setText(tableModel.getValueAt(row, 2).toString());
                        txtPenulis.setText(tableModel.getValueAt(row, 3).toString());
                        cbKategori.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                        txtStok.setText(tableModel.getValueAt(row, 5).toString());
                        String hargaRaw = tableModel.getValueAt(row, 6).toString().replaceAll("[^0-9]", "");
                        txtHarga.setText(hargaRaw);
                        txtGambar.setText(tableModel.getValueAt(row, 7).toString());
                    }
                }
            });

            btnAdd.addActionListener(e -> {
                if(txtJudul.getText().isEmpty()) return;
                String id = "BK-" + (tableModel.getRowCount() + 101);
                String hargaFormat = "Rp " + String.format("%,d", Integer.parseInt(txtHarga.getText())).replace(',', '.');
                tableModel.addRow(new Object[]{txtGambar.getText(), id, txtJudul.getText(), txtPenulis.getText(), cbKategori.getSelectedItem(), txtStok.getText(), hargaFormat, txtGambar.getText()});
                clearForm(); updateStats();
            });

            btnUpdate.addActionListener(e -> {
                int row = table.getSelectedRow();
                if(row != -1) {
                    String hargaFormat = "Rp " + String.format("%,d", Integer.parseInt(txtHarga.getText())).replace(',', '.');
                    tableModel.setValueAt(txtGambar.getText(), row, 0); 
                    tableModel.setValueAt(txtJudul.getText(), row, 2);
                    tableModel.setValueAt(txtPenulis.getText(), row, 3);
                    tableModel.setValueAt(cbKategori.getSelectedItem(), row, 4);
                    tableModel.setValueAt(txtStok.getText(), row, 5);
                    tableModel.setValueAt(hargaFormat, row, 6);
                    tableModel.setValueAt(txtGambar.getText(), row, 7);
                    clearForm(); updateStats();
                    JOptionPane.showMessageDialog(this, "Buku berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            btnDelete.addActionListener(e -> {
                int row = table.getSelectedRow();
                if(row != -1) {
                    tableModel.removeRow(row);
                    clearForm(); updateStats();
                }
            });

            btnClear.addActionListener(e -> clearForm());

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            tableCard.add(scrollPane, BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            loadRealBookData();
        }

        private void clearForm() {
            txtID.setText(""); txtJudul.setText(""); txtPenulis.setText("");
            txtHarga.setText(""); txtStok.setText(""); txtGambar.setText("assets/");
            cbKategori.setSelectedIndex(0);
            table.clearSelection();
        }

        private void updateStats() {
            lblTotal.setText(String.valueOf(tableModel.getRowCount()));
            int nipis = 0; long uang = 0;
            for(int i=0; i<tableModel.getRowCount(); i++) {
                int s = Integer.parseInt(tableModel.getValueAt(i, 5).toString());
                int h = Integer.parseInt(tableModel.getValueAt(i, 6).toString().replaceAll("[^0-9]", ""));
                if(s < 5) nipis++;
                uang += ((long)s * h);
            }
            lblMenipis.setText(String.valueOf(nipis));
            lblNilai.setText("Rp " + String.format("%,d", uang).replace(',', '.'));
        }

        private void loadRealBookData() {
            Object[][] dataBuku = {
                {"assets/Bumi.jpeg", "BK-101", "Bumi", "Tere Liye", "Fiksi Petualangan", "15", 92000},
                {"assets/Hujan.jpeg", "BK-102", "Hujan", "Tere Liye", "Fiksi Ilmiah & Romansa", "8", 90000},
                {"assets/SiAnakCahaya.jpg", "BK-103", "Si Anak Cahaya", "Tere Liye", "Fiksi Inspiratif", "3", 85000}, 
                {"assets/GuruAini.png", "BK-104", "Guru Aini", "Andrea Hirata", "Fiksi Pendidikan", "20", 92000},
                {"assets/Negeri5Menara.jpg", "BK-105", "Negeri 5 Menara", "A. Fuadi", "Fiksi Inspiratif", "12", 110000},
                {"assets/LonelinessIsMyBestFriend.jpg", "BK-106", "Loneliness Is My Best Friend", "Alvi Syahrin", "Self Improvement", "4", 94000}, 
                {"assets/OverthinkingIsMyHobby,AndIHateIt.jpg", "BK-107", "Overthinking Is My Hobby", "Alvi Syahrin", "Self Improvement", "18", 94000},
                {"assets/LaskarPelangi.jpg", "BK-108", "Laskar Pelangi", "Andrea Hirata", "Fiksi Pendidikan", "25", 90000},
                {"assets/AyahkuBukanPembohong.jpg", "BK-109", "Ayahku (Bukan) Pembohong", "Tere Liye", "Fiksi Inspiratif", "10", 85000},
                {"assets/MogaBundaDisayangAllah.jpeg", "BK-110", "Moga Bunda Disayang Allah", "Tere Liye", "Fiksi Inspiratif", "7", 85000},
                {"assets/Janji.jpeg", "BK-111", "Janji", "Tere Liye", "Fiksi Psikologis", "14", 90000},
                {"assets/DiaAdalahKakakku.jpg", "BK-112", "Dia adalah Kakakku", "Tere Liye", "Drama Keluarga", "2", 85000} 
            };
            for (Object[] row : dataBuku) {
                String hargaFormat = "Rp " + String.format("%,d", (Integer)row[6]).replace(',', '.');
                tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], hargaFormat, row[0]});
            }
            updateStats();
        }
    }

    // ==============================================================================
    // =========================== PANEL MANAJEMEN USER =============================
    // ==============================================================================
    static class UserPanel extends JPanel {
        DefaultTableModel tableModel;
        JTable table;
        JTextField txtName, txtEmail;
        JComboBox<String> cbRole, cbStatus;
        JLabel lblTotal, lblActive, lblSeller, lblAdmin;

        public UserPanel() {
            setLayout(new BorderLayout(30, 25));
            setBackground(COLOR_SOFT_PINK);
            setBorder(new EmptyBorder(30, 35, 30, 35));

            JPanel topPanel = new JPanel(new BorderLayout(0, 20));
            topPanel.setOpaque(false);
            
            RoundedPanel titleBanner = new RoundedPanel(20, COLOR_LIGHT_CREAM);
            titleBanner.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
            JLabel title = new JLabel("Manajemen Pengguna");
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setForeground(TEXT_DARK);
            titleBanner.add(title);
            topPanel.add(titleBanner, BorderLayout.NORTH);

            JPanel stats = new JPanel(new GridLayout(1, 4, 20, 0));
            stats.setOpaque(false);
            lblTotal = new JLabel("0"); lblActive = new JLabel("0"); 
            lblSeller = new JLabel("0"); lblAdmin = new JLabel("0");
            
            stats.add(createStatCard("Total Pengguna", lblTotal, COLOR_CARD_BG, COLOR_DUSTY_ROSE));
            stats.add(createStatCard("Pengguna Aktif", lblActive, COLOR_CARD_BG, COLOR_MUTED_GREEN));
            stats.add(createStatCard("Total Penjual", lblSeller, COLOR_CARD_BG, COLOR_DUSTY_ROSE));
            stats.add(createStatCard("Total Admin", lblAdmin, COLOR_CARD_BG, COLOR_MUTED_GREEN));
            topPanel.add(stats, BorderLayout.SOUTH);
            
            add(topPanel, BorderLayout.NORTH);

            RoundedPanel formContainer = new RoundedPanel(20, COLOR_CARD_BG);
            formContainer.setLayout(new BorderLayout());
            formContainer.setBorder(new EmptyBorder(10, 5, 10, 5));
            formContainer.setPreferredSize(new Dimension(340, 0));

            JPanel formContent = new JPanel();
            formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
            formContent.setOpaque(false);
            formContent.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel formTitle = new JLabel("Detail User");
            formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            formContent.add(formTitle);
            formContent.add(Box.createRigidArea(new Dimension(0, 20)));

            txtName = new JTextField(); txtEmail = new JTextField();
            cbRole = new JComboBox<>(new String[]{"Pelanggan", "Penjual", "Admin"});
            cbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});

            formContent.add(createInputWrapper("Nama Lengkap", txtName));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Email", txtEmail));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createComboWrapper("Role", cbRole));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createComboWrapper("Status", cbStatus));
            formContent.add(Box.createRigidArea(new Dimension(0, 20)));

            JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            btnPanel.setOpaque(false);
            btnPanel.setMaximumSize(new Dimension(270, 130));
            btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            RoundedButton btnAdd = new RoundedButton("Tambah", COLOR_MUTED_GREEN, Color.WHITE);
            RoundedButton btnUpdate = new RoundedButton("Update", COLOR_DUSTY_ROSE, Color.WHITE);
            RoundedButton btnDel = new RoundedButton("Hapus", new Color(220,38,38), Color.WHITE);
            RoundedButton btnClear = new RoundedButton("Bersih", COLOR_LIGHT_CREAM, TEXT_DARK);

            btnAdd.addActionListener(e -> {
                tableModel.addRow(new Object[]{"USR-" + (tableModel.getRowCount()+1), txtName.getText(), txtEmail.getText(), cbRole.getSelectedItem(), cbStatus.getSelectedItem()});
                updateStats();
            });
            
            btnPanel.add(btnAdd); btnPanel.add(btnUpdate); 
            btnPanel.add(btnDel); btnPanel.add(btnClear);
            formContent.add(btnPanel);

            JScrollPane formScroll = new JScrollPane(formContent);
            formScroll.setBorder(null);
            formScroll.setOpaque(false);
            formScroll.getViewport().setOpaque(false);
            formScroll.getVerticalScrollBar().setUnitIncrement(16);
            formContainer.add(formScroll, BorderLayout.CENTER);

            add(formContainer, BorderLayout.WEST);

            RoundedPanel tableCard = new RoundedPanel(20, COLOR_CARD_BG);
            tableCard.setLayout(new BorderLayout(0, 10));
            tableCard.setBorder(new EmptyBorder(15, 15, 15, 15));

            tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Email", "Role", "Status"}, 0);
            table = new JTable(tableModel);
            setupTableStyle(table);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());
            
            table.getSelectionModel().addListSelectionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    cbRole.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                }
            });

            tableCard.add(new JScrollPane(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            tableModel.addRow(new Object[]{"USR-1", "Syifa Nazira", "syifa@bukabuku.com", "Admin", "Aktif"});
            tableModel.addRow(new Object[]{"USR-2", "Gramedia", "gramedia@seller.com", "Penjual", "Aktif"});
            tableModel.addRow(new Object[]{"USR-3", "Budi Santoso", "budi@gmail.com", "Pelanggan", "Nonaktif"});
            updateStats();
        }

        private void updateStats() {
            lblTotal.setText(String.valueOf(tableModel.getRowCount()));
            int act = 0, sel = 0, adm = 0;
            for(int i=0; i<tableModel.getRowCount(); i++) {
                if(tableModel.getValueAt(i, 4).equals("Aktif")) act++;
                if(tableModel.getValueAt(i, 3).equals("Penjual")) sel++;
                if(tableModel.getValueAt(i, 3).equals("Admin")) adm++;
            }
            lblActive.setText(String.valueOf(act));
            lblSeller.setText(String.valueOf(sel));
            lblAdmin.setText(String.valueOf(adm));
        }
    }

    // ==============================================================================
    // =========================== PANEL MANAJEMEN ORDER ============================
    // ==============================================================================
    static class OrderPanel extends JPanel {
        DefaultTableModel tableModel;
        JTable table;
        JTextField txtNoOrder, txtPelanggan, txtItem, txtTotal;
        JComboBox<String> cbStatus;
        JLabel lblTotal, lblPending, lblSelesai;

        public OrderPanel() {
            setLayout(new BorderLayout(30, 25));
            setBackground(COLOR_SOFT_PINK);
            setBorder(new EmptyBorder(30, 35, 30, 35));

            JPanel topPanel = new JPanel(new BorderLayout(0, 20));
            topPanel.setOpaque(false);
            
            RoundedPanel titleBanner = new RoundedPanel(20, COLOR_LIGHT_CREAM);
            titleBanner.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
            JLabel title = new JLabel("Manajemen Pesanan");
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setForeground(TEXT_DARK);
            titleBanner.add(title);
            topPanel.add(titleBanner, BorderLayout.NORTH);

            JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
            stats.setOpaque(false);
            lblTotal = new JLabel("0"); lblPending = new JLabel("0"); lblSelesai = new JLabel("0");
            
            stats.add(createStatCard("Total Pesanan", lblTotal, COLOR_CARD_BG, COLOR_DUSTY_ROSE));
            stats.add(createStatCard("Menunggu (Pending)", lblPending, COLOR_CARD_BG, new Color(220, 38, 38)));
            stats.add(createStatCard("Pesanan Selesai", lblSelesai, COLOR_CARD_BG, COLOR_MUTED_GREEN));
            topPanel.add(stats, BorderLayout.SOUTH);
            add(topPanel, BorderLayout.NORTH);

            RoundedPanel formContainer = new RoundedPanel(20, COLOR_CARD_BG);
            formContainer.setLayout(new BorderLayout());
            formContainer.setBorder(new EmptyBorder(10, 5, 10, 5));
            formContainer.setPreferredSize(new Dimension(340, 0));
            
            JPanel formContent = new JPanel();
            formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
            formContent.setOpaque(false);
            formContent.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel formTitle = new JLabel("Update Pesanan");
            formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            formContent.add(formTitle);
            formContent.add(Box.createRigidArea(new Dimension(0, 20)));

           txtNoOrder = new JTextField(); txtNoOrder.setEditable(false); txtNoOrder.setBackground(new Color(243, 244, 246));
            txtPelanggan = new JTextField(); txtPelanggan.setEditable(false); txtPelanggan.setBackground(new Color(243, 244, 246)); // BARU
            txtItem = new JTextField(); txtItem.setEditable(false); txtItem.setBackground(new Color(243, 244, 246)); // BARU
            txtTotal = new JTextField(); txtTotal.setEditable(false); txtTotal.setBackground(new Color(243, 244, 246));
            cbStatus = new JComboBox<>(new String[]{"Pending", "Diproses", "Dikirim", "Selesai", "Dibatalkan"}); // Ditambah 'Dikirim' & 'Dibatalkan'

            formContent.add(createInputWrapper("No Order", txtNoOrder));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Nama Pelanggan", txtPelanggan)); // BARU
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Detail Item", txtItem)); // BARU
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createInputWrapper("Total Harga", txtTotal));
            formContent.add(Box.createRigidArea(new Dimension(0, 10)));
            formContent.add(createComboWrapper("Ubah Status", cbStatus));
            formContent.add(Box.createRigidArea(new Dimension(0, 25)));

            RoundedButton btnUpdate = new RoundedButton("Simpan Status", COLOR_MUTED_GREEN, Color.WHITE);
            btnUpdate.setMaximumSize(new Dimension(270, 40));
            btnUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnUpdate.addActionListener(e -> {
                int row = table.getSelectedRow();
                if(row != -1) {
                    tableModel.setValueAt(cbStatus.getSelectedItem(), row, 4);
                    updateStats();
                }
            });
            formContent.add(btnUpdate);
            
            JScrollPane formScroll = new JScrollPane(formContent);
            formScroll.setBorder(null);
            formScroll.setOpaque(false);
            formScroll.getViewport().setOpaque(false);
            formScroll.getVerticalScrollBar().setUnitIncrement(16);
            formContainer.add(formScroll, BorderLayout.CENTER);
            
            add(formContainer, BorderLayout.WEST);

            RoundedPanel tableCard = new RoundedPanel(20, COLOR_CARD_BG);
            tableCard.setLayout(new BorderLayout(0, 10));
            tableCard.setBorder(new EmptyBorder(15, 15, 15, 15));

            tableModel = new DefaultTableModel(new String[]{"No Order", "Pelanggan", "Item", "Total", "Status"}, 0);
            table = new JTable(tableModel);
            setupTableStyle(table);
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());
            
            table.getSelectionModel().addListSelectionListener(e -> {
                int r = table.getSelectedRow();
                if (r != -1) {
                    txtNoOrder.setText(tableModel.getValueAt(r, 0).toString());
                    txtPelanggan.setText(tableModel.getValueAt(r, 1).toString()); // BARU
                    txtItem.setText(tableModel.getValueAt(r, 2).toString());      // BARU
                    txtTotal.setText(tableModel.getValueAt(r, 3).toString());
                    cbStatus.setSelectedItem(tableModel.getValueAt(r, 4).toString());
                }
            });
            
            tableCard.add(new JScrollPane(table), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            tableModel.addRow(new Object[]{"ORD-001", "Budi Santoso", "Laskar Pelangi (x1)", "Rp 90.000", "Pending"});
            tableModel.addRow(new Object[]{"ORD-002", "Siti Aminah", "Atomic Habits (x2)", "Rp 216.000", "Diproses"});
            tableModel.addRow(new Object[]{"ORD-003", "Jelita Hati", "Bumi (x1)", "Rp 92.000", "Selesai"}); // Tambah data Jelita 😄
            updateStats();
        }

        private void updateStats() {
            lblTotal.setText(String.valueOf(tableModel.getRowCount()));
            int p = 0, s = 0;
            for(int i=0; i<tableModel.getRowCount(); i++) {
                String st = tableModel.getValueAt(i, 4).toString();
                if(st.equals("Pending")) p++;
                if(st.equals("Selesai")) s++;
            }
            lblPending.setText(String.valueOf(p));
            lblSelesai.setText(String.valueOf(s));
        }
    }


    // ==============================================================================
    // ============================= METODE HELPER RATA TENGAH ======================
    // ==============================================================================

    static JPanel createStatCard(String title, JLabel lblValue, Color bgCard, Color accentColor) {
        RoundedPanel card = new RoundedPanel(15, bgCard);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(TEXT_MUTED);
        
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(accentColor);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    // Input Wrapper diubah menjadi Rata Tengah (Center Alignment)
    static JPanel createInputWrapper(String labelStr, JTextField field) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.CENTER_ALIGNMENT); // Rata Tengah Kontainer
        
        JLabel lbl = new JLabel(labelStr);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT); // Label Rata Tengah
        
        field.setMaximumSize(new Dimension(280, 38)); // Lebar Ditetapkan
        field.setPreferredSize(new Dimension(280, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_DUSTY_ROSE, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT); // Input Rata Tengah
        
        wrap.add(lbl); wrap.add(Box.createRigidArea(new Dimension(0, 5))); wrap.add(field);
        return wrap;
    }

    static JPanel createFilePickerWrapper(String labelStr, JTextField field, Component parent) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelStr);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel inputRow = new JPanel(new BorderLayout(5, 0));
        inputRow.setOpaque(false);
        inputRow.setMaximumSize(new Dimension(280, 38));
        inputRow.setPreferredSize(new Dimension(280, 38));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_DUSTY_ROSE, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        RoundedButton btnBrowse = new RoundedButton("Pilih", COLOR_MUTED_GREEN, Color.WHITE);
        btnBrowse.setPreferredSize(new Dimension(70, 38));
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(parent);
            if(res == JFileChooser.APPROVE_OPTION) {
                field.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        inputRow.add(field, BorderLayout.CENTER);
        inputRow.add(btnBrowse, BorderLayout.EAST);
        
        inputRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        wrap.add(lbl); 
        wrap.add(Box.createRigidArea(new Dimension(0, 5))); 
        wrap.add(inputRow);
        return wrap;
    }

    static JPanel createComboWrapper(String labelStr, JComboBox<String> cb) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelStr);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cb.setMaximumSize(new Dimension(280, 38));
        cb.setPreferredSize(new Dimension(280, 38));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        wrap.add(lbl); wrap.add(Box.createRigidArea(new Dimension(0, 5))); wrap.add(cb);
        return wrap;
    }

    static void setupTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(45);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(COLOR_DUSTY_ROSE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionBackground(COLOR_SOFT_PINK);
        table.setSelectionForeground(TEXT_DARK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
    }
    
    static JLabel loadIcon(String path, int width, int height) {
        JLabel label = new JLabel();
        if (new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
        return label;
    }
}

// ==============================================================================
// ============================= CUSTOM UI KELAS ================================
// ==============================================================================

class ImageTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        
        if (value != null) {
            String path = value.toString();
            File file = new File(path);
            if(file.exists()) {
                ImageIcon icon = new ImageIcon(path);
                // Ukuran gambar diperbaiki agar proporsional
                Image img = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                // Tampilan jika gambar tidak ditemukan (lebih rapi dari "No IMG")
                label.setText("📷 Gambar Tidak Ditemukan");
                label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                label.setForeground(new Color(150, 150, 150));
            }
        }
        
        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setOpaque(true);
        } else {
            label.setBackground(table.getBackground());
            label.setOpaque(true);
        }
        return label;
    }
}

class StatusBadgeRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean foc, int r, int c) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, val, isSel, foc, r, c);
        lbl.setHorizontalAlignment(CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        String status = val.toString();
        if (status.equals("Aktif") || status.equals("Selesai") || status.equals("Diproses")) {
            lbl.setForeground(new Color(22, 101, 52));
            lbl.setBackground(new Color(220, 252, 231)); 
        } else {
            lbl.setForeground(new Color(153, 27, 27));
            lbl.setBackground(new Color(254, 226, 226)); 
        }
        lbl.setOpaque(true);
        return lbl;
    }
}

class RoundedPanel extends JPanel {
    private int radius;
    private Color bg;
    public RoundedPanel(int r, Color c) { radius = r; bg = c; setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
    }
}

class RoundedButton extends JButton {
    private Color bg;
    public RoundedButton(String txt, Color bg, Color fg) {
        super(txt); this.bg = bg; setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        setAlignmentX(Component.CENTER_ALIGNMENT); // Teks & tombol terpusat
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() ? bg.darker() : bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.dispose(); super.paintComponent(g);
    }
}