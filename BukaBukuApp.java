import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BukaBukuApp {
    // Tema Warna (Didefinisikan persis tanpa perubahan warna)
    static final Color COLOR_PINK_SOFT = new Color(246, 226, 233);
    static final Color COLOR_PINK_SOLID = new Color(244, 114, 182);
    static final Color COLOR_CREAM = new Color(253, 251, 247);
    static final Color COLOR_SAGE = new Color(184, 206, 181);
    static final Color COLOR_DARK_GREEN = new Color(124, 156, 123);

    static final Color BG_MAIN = COLOR_PINK_SOFT;
    static final Color CARD_BG = COLOR_CREAM;
    static final Color TEXT_DARK = new Color(31, 41, 55);
    static final Color TEXT_MUTED = new Color(107, 114, 128);

    // State & Komponen Global
    static CardLayout cardLayout = new CardLayout();
    static JPanel cardPanel = new JPanel(cardLayout);
    static String activeMenu = "Katalog";
    
    static List<Book> wishlistData = new ArrayList<>();
    static List<CartItem> cartData = new ArrayList<>();
    static List<Order> orderHistory = new ArrayList<>();
    static Book[] allBooks;

    static JLabel navKatalog, navWishlist, navKeranjang, navRiwayat, navLogin, navDaftar;
    static JPanel katalogGridPanel, wishlistGridPanel, cartListPanel, riwayatListPanel;
    static JLabel labelTotalHarga;

    public static void main(String[] args) {
        initData();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    static void initData() {
        allBooks = new Book[] {
            new Book("Bumi", "Tere Liye", "Fiksi Petualangan", 92000, "assets/Bumi.jpeg", "Raib adalah remaja SMA yang memiliki kemampuan unik bisa menghilang hanya dengan menutup wajahnya dengan telapak tangan..."),
            new Book("Hujan", "Tere Liye", "Fiksi Ilmiah & Romansa", 90000, "assets/Hujan.jpeg", "Hujan karya Tere Liye menceritakan tentang Lail, seorang gadis yang kehilangan orang tuanya akibat bencana besar..."),
            new Book("Si Anak Cahaya", "Tere Liye", "Fiksi Inspiratif", 85000, "assets/SiAnakCahaya.jpg", "Mengisahkan Nurmas (Nung), seorang gadis kecil ceria dan tangguh dari pedalaman Sumatra..."),
            new Book("Guru Aini", "Andrea Hirata", "Fiksi Pendidikan", 92000, "assets/GuruAini.png", "Cerita berfokus pada Desi, seorang guru matematika idealis yang rela ditempatkan di pelosok demi mencari..."),
            new Book("Negeri 5 Menara", "A. Fuadi", "Fiksi Inspiratif", 110000, "assets/Negeri5Menara.jpg", "Alif adalah seorang remaja dari Maninjau yang terpaksa merantau ke Jawa untuk belajar di Pondok Madani..."),
            new Book("Loneliness Is My Best Friend", "Alvi Syahrin", "Self Improvement", 94000, "assets/LonelinessIsMyBestFriend.jpg", "Buku ini membahas fenomena kesepian yang sering dialami oleh orang dewasa, mulai dari perasaan tidak memiliki teman..."),
            new Book("Overthinking Is My Hobby", "Alvi Syahrin", "Self Improvement", 94000, "assets/OverthinkingIsMyHobby,AndIHateIt.jpg", "Buku ini hadir sebagai panduan untuk membantu pembaca meredam suara bising di dalam kepala akibat kebiasaan..."),
            new Book("Laskar Pelangi", "Andrea Hirata", "Fiksi Pendidikan", 90000, "assets/LaskarPelangi.jpg", "Cerita dimulai di kampung Belitong, di mana sebuah sekolah Muhammadiyah yang nyaris roboh sedang menunggu murid..."),
            new Book("Ayahku (Bukan) Pembohong", "Tere Liye", "Fiksi Inspiratif", 85000, "assets/AyahkuBukanPembohong.jpg", "Dam tumbuh besar dengan dongeng-dongeng luar biasa dari ayahnya tentang Lembah Bukhara, suku Penguasa Angin..."),
            new Book("Moga Bunda Disayang Allah", "Tere Liye", "Drama Inspiratif", 85000, "assets/MogaBundaDisayangAllah.jpeg", "Melati adalah seorang gadis kecil yang kehilangan penglihatan, pendengaran, serta kemampuan bicaranya akibat kecelakaan..."),
            new Book("Janji", "Tere Liye", "Fiksi Psikologis", 90000, "assets/Janji.jpeg", "Cerita berfokus pada tiga santri nakal bernama Hasan, Kaharuddin, dan Baso. Sebagai hukuman atas kenakalan mereka..."),
            new Book("Dia adalah Kakakku", "Tere Liye", "Drama Keluarga", 85000, "assets/DiaAdalahKakakku.jpg", "Mengisahkan perjuangan heroik Laisa, seorang gadis sederhana yang rela mengorbankan sekolah dan masa depannya...")
        };

        // Dummy Data Riwayat
        List<CartItem> dummyItems = new ArrayList<>();
        dummyItems.add(new CartItem(allBooks[3], 3));
        orderHistory.add(new Order("#1", "3 November 2026 pukul 18.19", dummyItems, 276000, "BERHASIL"));
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("BukaBuku - Toko Buku Online");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1240, 820);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(COLOR_CREAM);

        // 1. NAVBAR
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(COLOR_SAGE);
        navbar.setBorder(new EmptyBorder(12, 35, 12, 35));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel logoLabel = loadIcon("assets/bukabuku.png", 40, 40);
        if (logoLabel.getIcon() == null) {
            logoLabel.setText("📖");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        }
        JLabel titleLabel = new JLabel("BukaBuku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);
        
        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        navbar.add(logoPanel, BorderLayout.WEST);

        logoPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Katalog", frame); }
        });

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 5));
        menuPanel.setOpaque(false);
        
        navKatalog = createMenuLabel("Katalog", frame);
        navWishlist = createMenuLabel("Wishlist", frame);
        navKeranjang = createMenuLabel("Keranjang", frame);
        navRiwayat = createMenuLabel("Riwayat", frame);
        navLogin = createMenuLabel("Login", frame);
        navDaftar = createMenuLabel("Daftar", frame);
        
        menuPanel.add(navKatalog);
        menuPanel.add(navWishlist);
        menuPanel.add(navKeranjang);
        menuPanel.add(navRiwayat);
        
        JLabel separator = new JLabel(" | ");
        separator.setForeground(COLOR_DARK_GREEN);
        menuPanel.add(separator);
        
        menuPanel.add(navLogin);
        menuPanel.add(navDaftar);
        navbar.add(menuPanel, BorderLayout.EAST);

        // 2. SETUP CARD LAYOUT
        cardPanel.add(createKatalogPanel(frame), "Katalog");
        cardPanel.add(createWishlistPanel(frame), "Wishlist");
        cardPanel.add(createKeranjangPanel(frame), "Keranjang");
        cardPanel.add(createRiwayatPanel(frame), "Riwayat");
        cardPanel.add(createLoginPanel(frame), "Login");
        cardPanel.add(createRegisterPanel(frame), "Daftar");

        frame.add(navbar, BorderLayout.NORTH);
        frame.add(cardPanel, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        updateNavbar(); 
        frame.setVisible(true);
    }

    // ================== HELPER FOOTER MINIMALIS ==================
    static JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(COLOR_CREAM); 
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel footerLabel = new JLabel("© 2026 BukaBuku Kelompok");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_MUTED);
        footer.add(footerLabel);
        return footer;
    }

    // ================== LOGIKA NAVIGASI & UPDATE ==================
    static void navigateTo(String page, JFrame frame) {
        activeMenu = page;
        if (page.equals("Katalog") || page.equals("Wishlist") || page.equals("Keranjang") || page.equals("Riwayat")) {
            refreshGrids(frame);
        }
        cardLayout.show(cardPanel, page);
        updateNavbar();
    }

    static void updateNavbar() {
        navKatalog.setForeground(TEXT_DARK);
        navWishlist.setForeground(TEXT_DARK);
        navKeranjang.setForeground(TEXT_DARK);
        navRiwayat.setForeground(TEXT_DARK);
        navLogin.setForeground(TEXT_DARK);
        navDaftar.setForeground(TEXT_DARK);

        navWishlist.setText(wishlistData.isEmpty() ? "Wishlist" : "Wishlist (" + wishlistData.size() + ")");
        navKeranjang.setText(cartData.isEmpty() ? "Keranjang" : "Keranjang (" + cartData.size() + ")");

        if (activeMenu.equals("Katalog")) navKatalog.setForeground(COLOR_PINK_SOLID);
        else if (activeMenu.equals("Wishlist")) navWishlist.setForeground(COLOR_PINK_SOLID);
        else if (activeMenu.equals("Keranjang")) navKeranjang.setForeground(COLOR_PINK_SOLID);
        else if (activeMenu.equals("Riwayat")) navRiwayat.setForeground(COLOR_PINK_SOLID);
        else if (activeMenu.equals("Login")) navLogin.setForeground(COLOR_PINK_SOLID);
        else if (activeMenu.equals("Daftar")) navDaftar.setForeground(COLOR_PINK_SOLID);
    }

    static void refreshGrids(JFrame frame) {
        if (katalogGridPanel != null) {
            katalogGridPanel.removeAll();
            for (Book b : allBooks) katalogGridPanel.add(createBookCard(b, frame));
            katalogGridPanel.revalidate(); katalogGridPanel.repaint();
        }
        if (wishlistGridPanel != null) {
            wishlistGridPanel.removeAll();
            for (Book b : wishlistData) wishlistGridPanel.add(createBookCard(b, frame));
            wishlistGridPanel.revalidate(); wishlistGridPanel.repaint();
        }
        if (cartListPanel != null) {
            cartListPanel.removeAll();
            if (cartData.isEmpty()) {
                JLabel empty = new JLabel("Keranjang belanja masih kosong.");
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 15));
                empty.setForeground(TEXT_MUTED);
                empty.setBorder(new EmptyBorder(10, 0, 0, 0));
                cartListPanel.add(empty);
            } else {
                for (CartItem item : cartData) cartListPanel.add(createCartItemCard(item, frame));
            }
            cartListPanel.add(Box.createVerticalGlue()); 
            cartListPanel.revalidate(); cartListPanel.repaint();
            updateRingkasanBelanja();
        }
        if (riwayatListPanel != null) {
            riwayatListPanel.removeAll();
            if (orderHistory.isEmpty()) {
                JLabel empty = new JLabel("Belum ada riwayat belanja.");
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 15));
                empty.setForeground(TEXT_MUTED);
                empty.setBorder(new EmptyBorder(10, 0, 0, 0));
                riwayatListPanel.add(empty);
            } else {
                for (Order order : orderHistory) {
                    riwayatListPanel.add(createOrderCard(order));
                    riwayatListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
            riwayatListPanel.add(Box.createVerticalGlue());
            riwayatListPanel.revalidate(); riwayatListPanel.repaint();
        }
    }

    static void filterKatalog(String query, JFrame frame) {
        if (katalogGridPanel == null) return;
        katalogGridPanel.removeAll();
        
        String keyword = query.trim().toLowerCase();
        boolean foundAny = false;

        for (Book b : allBooks) {
            if (keyword.isEmpty() || 
                b.title.toLowerCase().contains(keyword) || 
                b.author.toLowerCase().contains(keyword) ||
                b.category.toLowerCase().contains(keyword)) {
                
                katalogGridPanel.add(createBookCard(b, frame));
                foundAny = true;
            }
        }

        if (!foundAny) {
            JLabel notFoundLabel = new JLabel("Tidak ada buku yang cocok dengan '" + query + "'");
            notFoundLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            notFoundLabel.setForeground(TEXT_MUTED);
            notFoundLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
            katalogGridPanel.add(notFoundLabel);
        }

        katalogGridPanel.revalidate();
        katalogGridPanel.repaint();
    }

    static void addToCart(Book book, JFrame frame) {
        boolean found = false;
        for (CartItem item : cartData) {
            if (item.book.title.equals(book.title)) {
                item.qty++;
                found = true; break;
            }
        }
        if (!found) cartData.add(new CartItem(book, 1));
        
        showNotificationToast(frame, book.title + " berhasil masuk keranjang!");
        updateNavbar();
    }

    static void showNotificationToast(JFrame frame, String message) {
        JWindow toast = new JWindow(frame);
        
        RoundedPanel panel = new RoundedPanel(18, COLOR_DARK_GREEN, Color.WHITE);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JLabel lblMsg = new JLabel(message);
        lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMsg.setForeground(Color.WHITE);
        panel.add(lblMsg);
        
        toast.setContentPane(panel);
        toast.pack();
        
        Point loc = frame.getLocationOnScreen();
        toast.setLocation(loc.x + frame.getWidth() - toast.getWidth() - 40, loc.y + 70);
        
        toast.setVisible(true);

        Timer timer = new Timer(2500, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    static void updateRingkasanBelanja() {
        if (labelTotalHarga != null) {
            int total = 0;
            for (CartItem item : cartData) {
                if (item.selected) total += (item.book.price * item.qty);
            }
            labelTotalHarga.setText(String.format("Rp %,d", total).replace(',', '.'));
        }
    }

    // ================== HALAMAN RIWAYAT (DIPERBAIKI) ==================
    static JScrollPane createRiwayatPanel(JFrame frame) {
        JPanel container = new JPanel(new BorderLayout(25, 0));
        container.setBackground(BG_MAIN);
        container.setBorder(new EmptyBorder(25, 40, 0, 40));

        // LEFT PANEL - Daftar Pesanan
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Riwayat Belanja");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        riwayatListPanel = new JPanel();
        riwayatListPanel.setLayout(new BoxLayout(riwayatListPanel, BoxLayout.Y_AXIS));
        riwayatListPanel.setOpaque(false);
        riwayatListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(riwayatListPanel);
        leftPanel.add(Box.createVerticalGlue());

        // RIGHT PANEL - Ringkasan (BARU!)
        RoundedPanel summaryPanel = new RoundedPanel(18, CARD_BG);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        summaryPanel.setPreferredSize(new Dimension(320, 400));
        
        JLabel sumTitle = new JLabel("Ringkasan Belanja");
        sumTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sumTitle.setForeground(TEXT_DARK);
        sumTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        summaryPanel.add(sumTitle);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Total Pesanan
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setOpaque(false);
        JLabel lblTotalOrder = new JLabel("Total Pesanan");
        lblTotalOrder.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTotalOrder.setForeground(TEXT_MUTED);
        JLabel valTotalOrder = new JLabel(String.valueOf(orderHistory.size()));
        valTotalOrder.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valTotalOrder.setForeground(COLOR_DARK_GREEN);
        row1.add(lblTotalOrder, BorderLayout.WEST);
        row1.add(valTotalOrder, BorderLayout.EAST);
        summaryPanel.add(row1);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Pesanan Berhasil
        JPanel row2 = new JPanel(new BorderLayout());
        row2.setOpaque(false);
        JLabel lblSuccess = new JLabel("Pesanan Berhasil");
        lblSuccess.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSuccess.setForeground(TEXT_MUTED);
        int successCount = 0;
        for(Order o : orderHistory) if(o.status.equals("BERHASIL")) successCount++;
        JLabel valSuccess = new JLabel(String.valueOf(successCount));
        valSuccess.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valSuccess.setForeground(new Color(16, 185, 129));
        row2.add(lblSuccess, BorderLayout.WEST);
        row2.add(valSuccess, BorderLayout.EAST);
        summaryPanel.add(row2);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Total Pengeluaran
        int totalSpent = 0;
        for(Order o : orderHistory) totalSpent += o.total;
        
        JPanel row3 = new JPanel(new BorderLayout());
        row3.setOpaque(false);
        JLabel lblSpent = new JLabel("Total Pengeluaran");
        lblSpent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSpent.setForeground(TEXT_MUTED);
        JLabel valSpent = new JLabel("Rp " + String.format("%,d", totalSpent).replace(',', '.'));
        valSpent.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valSpent.setForeground(COLOR_PINK_SOLID);
        row3.add(lblSpent, BorderLayout.WEST);
        row3.add(valSpent, BorderLayout.EAST);
        summaryPanel.add(row3);
        summaryPanel.add(Box.createVerticalGlue());
        
        // Info Card
        RoundedPanel infoCard = new RoundedPanel(12, COLOR_PINK_SOFT);
        infoCard.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        infoCard.setMaximumSize(new Dimension(280, 80));
        JLabel infoText = new JLabel("<html><div style='text-align: center;'>Terima kasih telah berbelanja di BukaBuku!</div></html>");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoText.setForeground(TEXT_DARK);
        infoCard.add(infoText);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        summaryPanel.add(infoCard);

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(summaryPanel, BorderLayout.EAST);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(container, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapperContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    static JPanel createOrderCard(Order order) {
        RoundedPanel card = new RoundedPanel(18, CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));
        card.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftHeader.setOpaque(false);
        
        RoundedPanel iconReceipt = new RoundedPanel(10, COLOR_PINK_SOFT, COLOR_PINK_SOLID);
        iconReceipt.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JLabel lblIcon = new JLabel("INV");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblIcon.setForeground(COLOR_PINK_SOLID);
        iconReceipt.add(lblIcon);
        
        JPanel textHeader = new JPanel();
        textHeader.setLayout(new BoxLayout(textHeader, BoxLayout.Y_AXIS));
        textHeader.setOpaque(false);
        
        JLabel lblOrder = new JLabel("Pesanan " + order.id);
        lblOrder.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblOrder.setForeground(TEXT_DARK);
        
        JLabel lblDate = new JLabel(order.date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(TEXT_MUTED);
        
        textHeader.add(lblOrder);
        textHeader.add(lblDate);
        leftHeader.add(iconReceipt);
        leftHeader.add(textHeader);

        RoundedPanel badge = new RoundedPanel(12, new Color(209, 250, 229)); 
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));
        JLabel lblStatus = new JLabel(order.status);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStatus.setForeground(new Color(16, 185, 129)); 
        badge.add(lblStatus);

        header.add(leftHeader, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(230, 230, 230));
        card.add(sep1);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        for (CartItem item : order.items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);

            JPanel leftItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            leftItem.setOpaque(false);
            
            JLabel imgLabel = loadIcon(item.book.imagePath, 45, 65);
            if(imgLabel.getIcon() == null) {
                imgLabel.setText("Foto");
                imgLabel.setPreferredSize(new Dimension(45, 65));
                imgLabel.setOpaque(true);
                imgLabel.setBackground(Color.WHITE);
            }

            JPanel textItem = new JPanel();
            textItem.setLayout(new BoxLayout(textItem, BoxLayout.Y_AXIS));
            textItem.setOpaque(false);
            
            JLabel title = new JLabel(item.book.title);
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            title.setForeground(TEXT_DARK);
            
            JLabel qtyPrice = new JLabel(item.qty + " x Rp " + String.format("%,d", item.book.price).replace(',', '.'));
            qtyPrice.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            qtyPrice.setForeground(TEXT_MUTED);
            
            textItem.add(title);
            textItem.add(Box.createRigidArea(new Dimension(0, 3)));
            textItem.add(qtyPrice);

            leftItem.add(imgLabel);
            leftItem.add(textItem);

            JLabel itemSubtotal = new JLabel("Rp " + String.format("%,d", item.book.price * item.qty).replace(',', '.'));
            itemSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
            itemSubtotal.setForeground(TEXT_DARK);

            itemPanel.add(leftItem, BorderLayout.WEST);
            itemPanel.add(itemSubtotal, BorderLayout.EAST);

            card.add(itemPanel);
            card.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(230, 230, 230));
        card.add(sep2);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel rightAlignPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightAlignPanel.setOpaque(false);
        
        JPanel innerRight = new JPanel();
        innerRight.setLayout(new BoxLayout(innerRight, BoxLayout.Y_AXIS));
        innerRight.setOpaque(false);
        
        JLabel lblTotalText = new JLabel("TOTAL PEMBAYARAN");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTotalText.setForeground(TEXT_MUTED);
        lblTotalText.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTotalSum = new JLabel("Rp " + String.format("%,d", order.total).replace(',', '.'));
        lblTotalSum.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalSum.setForeground(COLOR_PINK_SOLID);
        lblTotalSum.setAlignmentX(Component.RIGHT_ALIGNMENT);

        innerRight.add(lblTotalText);
        innerRight.add(lblTotalSum);
        rightAlignPanel.add(innerRight);

        card.add(rightAlignPanel);

        return card;
    }

    // ================== HALAMAN KERANJANG ==================
    static JScrollPane createKeranjangPanel(JFrame frame) {
        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.setBackground(BG_MAIN);
        container.setBorder(new EmptyBorder(25, 40, 0, 40));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Keranjang Belanja");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartListPanel.setOpaque(false);
        cartListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(cartListPanel);

        RoundedPanel summaryPanel = new RoundedPanel(18, CARD_BG);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        summaryPanel.setPreferredSize(new Dimension(340, 190));
        
        JLabel sumTitle = new JLabel("Ringkasan Belanja");
        sumTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        sumTitle.setForeground(TEXT_DARK);
        sumTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JPanel rowTotal = new JPanel(new BorderLayout());
        rowTotal.setOpaque(false);
        rowTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTeksTotal = new JLabel("Total Dipilih");
        lblTeksTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTeksTotal.setForeground(TEXT_MUTED);
        
        labelTotalHarga = new JLabel("Rp 0");
        labelTotalHarga.setFont(new Font("Segoe UI", Font.BOLD, 17));
        labelTotalHarga.setForeground(COLOR_PINK_SOLID);
        
        rowTotal.add(lblTeksTotal, BorderLayout.WEST);
        rowTotal.add(labelTotalHarga, BorderLayout.EAST);

        RoundedButton btnCheckout = new RoundedButton("Checkout Sekarang", COLOR_SAGE, TEXT_DARK);
        btnCheckout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckout.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnCheckout.addActionListener(e -> {
            List<CartItem> selectedItems = new ArrayList<>();
            int subtotal = 0;
            for (CartItem item : cartData) {
                if (item.selected) {
                    selectedItems.add(item);
                    subtotal += (item.book.price * item.qty);
                }
            }
            if (selectedItems.isEmpty()) {
                showNotificationToast(frame, "Pilih minimal 1 buku untuk di-checkout!");
                return;
            }
            
            new CheckoutDialog(frame, selectedItems, subtotal).setVisible(true);
        });

        summaryPanel.add(sumTitle);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        summaryPanel.add(rowTotal);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        summaryPanel.add(btnCheckout);

        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(summaryPanel, BorderLayout.NORTH);

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightWrapper, BorderLayout.EAST);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(container, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapperContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    static JPanel createCartItemCard(CartItem item, JFrame frame) {
        RoundedPanel card = new RoundedPanel(15, CARD_BG);
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(800, 130));
        card.setPreferredSize(new Dimension(800, 130));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftArea.setOpaque(false);
        
        JCheckBox chkBox = new JCheckBox();
        chkBox.setOpaque(false);
        chkBox.setSelected(item.selected);
        
        JLabel imgLabel = loadIcon(item.book.imagePath, 60, 90);
        if (imgLabel.getIcon() == null) {
            imgLabel.setText("Foto");
            imgLabel.setPreferredSize(new Dimension(60, 90));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(Color.WHITE);
        }
        
        leftArea.add(chkBox);
        leftArea.add(imgLabel);

        JPanel centerArea = new JPanel();
        centerArea.setLayout(new BoxLayout(centerArea, BoxLayout.Y_AXIS));
        centerArea.setOpaque(false);

        JLabel title = new JLabel(item.book.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT_DARK);

        JLabel price = new JLabel(String.format("Rp %,d", item.book.price).replace(',', '.'));
        price.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        price.setForeground(TEXT_MUTED);

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        qtyPanel.setOpaque(false);
        
        RoundedButton btnMin = new RoundedButton("−", COLOR_PINK_SOFT, TEXT_DARK);
        btnMin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMin.setPreferredSize(new Dimension(40, 40));
        
        JLabel lblQty = new JLabel(String.valueOf(item.qty));
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        RoundedButton btnPlus = new RoundedButton("+", COLOR_PINK_SOFT, TEXT_DARK);
        btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPlus.setPreferredSize(new Dimension(40, 40));

        qtyPanel.add(btnMin);
        qtyPanel.add(lblQty);
        qtyPanel.add(btnPlus);

        centerArea.add(Box.createRigidArea(new Dimension(0, 4)));
        centerArea.add(title);
        centerArea.add(price);
        centerArea.add(Box.createRigidArea(new Dimension(0, 8)));
        centerArea.add(qtyPanel);

        JPanel rightArea = new JPanel(new BorderLayout());
        rightArea.setOpaque(false);
        
        JLabel subtotal = new JLabel("<html><div style='text-align: right;'><span style='font-size:10px;color:gray;'>Subtotal</span><br/><b>" + 
                                     String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
        subtotal.setForeground(COLOR_PINK_SOLID);
        
        // TOMBOL HAPUS - Tanpa emoji, pakai RoundedButton
        RoundedButton btnDel = new RoundedButton("Hapus", new Color(220, 53, 69), Color.WHITE);
        btnDel.setPreferredSize(new Dimension(90, 35));
        btnDel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightArea.add(subtotal, BorderLayout.NORTH);
        rightArea.add(btnDel, BorderLayout.SOUTH);

        chkBox.addActionListener(e -> {
            item.selected = chkBox.isSelected();
            updateRingkasanBelanja();
        });

        btnMin.addActionListener(e -> {
            if (item.qty > 1) { 
                item.qty--; 
                lblQty.setText(String.valueOf(item.qty)); 
                subtotal.setText("<html><div style='text-align: right;'><span style='font-size:10px;color:gray;'>Subtotal</span><br/><b>" + 
                                 String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
                updateRingkasanBelanja(); 
            }
        });
        
        btnPlus.addActionListener(e -> {
            item.qty++; 
            lblQty.setText(String.valueOf(item.qty)); 
            subtotal.setText("<html><div style='text-align: right;'><span style='font-size:10px;color:gray;'>Subtotal</span><br/><b>" + 
                             String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
            updateRingkasanBelanja();
        });

        btnDel.addActionListener(e -> {
            cartData.remove(item);
            refreshGrids(frame);
            updateNavbar();
        });

        card.add(leftArea, BorderLayout.WEST);
        card.add(centerArea, BorderLayout.CENTER);
        card.add(rightArea, BorderLayout.EAST);
        
        JPanel cardWrapper = new JPanel();
        cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.Y_AXIS));
        cardWrapper.setOpaque(false);
        cardWrapper.add(card);
        cardWrapper.setBorder(new EmptyBorder(0, 0, 12, 0));
        return cardWrapper;
    }

    // ================== HALAMAN KATALOG & WISHLIST ==================
    static JScrollPane createKatalogPanel(JFrame frame) {
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setOpaque(false);
        heroPanel.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel welcomeLabel = new JLabel("Temukan Bacaan Favoritmu");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Jelajahi koleksi buku terbaik untuk menemanimu setiap hari.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchContainer = new RoundedPanel(20, Color.WHITE);
        searchContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        searchContainer.setMaximumSize(new Dimension(480, 42));
        searchContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String placeholderText = "Cari judul buku atau penulis...";
        JTextField searchField = new JTextField(placeholderText);
        searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(TEXT_MUTED);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(410, 22));

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholderText)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_DARK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(placeholderText);
                    searchField.setForeground(TEXT_MUTED);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void triggerSearch() {
                String txt = searchField.getText();
                if (!txt.equals(placeholderText)) {
                    filterKatalog(txt, frame);
                } else {
                    filterKatalog("", frame);
                }
            }
            public void insertUpdate(DocumentEvent e) { triggerSearch(); }
            public void removeUpdate(DocumentEvent e) { triggerSearch(); }
            public void changedUpdate(DocumentEvent e) { triggerSearch(); }
        });
        
        searchContainer.add(new JLabel("🔍"));
        searchContainer.add(searchField);

        heroPanel.add(welcomeLabel);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        heroPanel.add(subtitleLabel);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        heroPanel.add(searchContainer);

        katalogGridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        katalogGridPanel.setOpaque(false);
        katalogGridPanel.setBorder(new EmptyBorder(10, 35, 30, 35));
        for (Book b : allBooks) katalogGridPanel.add(createBookCard(b, frame));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BG_MAIN);
        container.add(heroPanel);
        container.add(katalogGridPanel);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(container, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapperContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    static JScrollPane createWishlistPanel(JFrame frame) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 10, 35));

        JLabel titleLabel = new JLabel("Wishlist Kamu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);
        headerPanel.add(titleLabel);

        wishlistGridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        wishlistGridPanel.setOpaque(false);
        wishlistGridPanel.setBorder(new EmptyBorder(10, 35, 30, 35));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BG_MAIN);
        container.add(headerPanel);
        container.add(wishlistGridPanel);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(container, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapperContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    static JPanel createBookCard(Book book, JFrame parentFrame) {
        RoundedPanel card = new RoundedPanel(18, CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setOpaque(false);
        imageContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        imageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel imgLabel = loadIcon(book.imagePath, 130, 185);
        if (imgLabel.getIcon() == null) {
            imgLabel.setText("Gambar Kosong");
            imgLabel.setPreferredSize(new Dimension(130, 185));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(Color.WHITE);
        }
        imageContainer.add(imgLabel);

        JLabel catLabel = new JLabel(book.category.toUpperCase());
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLabel.setForeground(COLOR_DARK_GREEN);
        
        JLabel titleLabel = new JLabel("<html><body style='width: 150px;'>" + book.title + "</body></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_DARK);

        JLabel authorLabel = new JLabel("Oleh " + book.author);
        authorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        authorLabel.setForeground(TEXT_MUTED);

        String shortDesc = book.description.length() > 60 ? book.description.substring(0, 60) + "..." : book.description;
        JLabel descLabel = new JLabel("<html><body style='width: 160px; color: #6B7280; font-size: 11px;'>" + shortDesc + "</body></html>");
        
        JLabel readMoreLabel = new JLabel("Baca Selengkapnya");
        readMoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        readMoreLabel.setForeground(COLOR_DARK_GREEN);
        readMoreLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readMoreLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new ModernDetailDialog(parentFrame, book).setVisible(true); }
        });

        RoundedButton btnAdd = new RoundedButton("Tambah ke Keranjang", COLOR_DARK_GREEN, Color.WHITE);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36)); 
        btnAdd.addActionListener(e -> addToCart(book, parentFrame));
        
        JPanel priceHeartPanel = new JPanel(new BorderLayout());
        priceHeartPanel.setOpaque(false);
        priceHeartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel priceLabel = new JLabel(String.format("Rp %,d", book.price).replace(',', '.'));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLabel.setForeground(TEXT_DARK);
        
        boolean isInWishlist = wishlistData.contains(book);
        JLabel heartLabel = new JLabel(isInWishlist ? "♥" : "♡");
        heartLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heartLabel.setForeground(isInWishlist ? COLOR_PINK_SOLID : Color.LIGHT_GRAY);
        heartLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        heartLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (wishlistData.contains(book)) {
                    wishlistData.remove(book);
                    showNotificationToast(parentFrame, "Dihapus dari Wishlist");
                } else {
                    wishlistData.add(book);
                    showNotificationToast(parentFrame, "Ditambahkan ke Wishlist!");
                }
                refreshGrids(parentFrame);
                updateNavbar();
            }
        });

        priceHeartPanel.add(priceLabel, BorderLayout.WEST);
        priceHeartPanel.add(heartLabel, BorderLayout.EAST);

        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        readMoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceHeartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(imageContainer);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(catLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(titleLabel);
        card.add(authorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 2)));
        card.add(readMoreLabel);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(btnAdd);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(priceHeartPanel);

        return card;
    }

    // ================== HALAMAN LOGIN & DAFTAR ==================
    static JPanel createLoginPanel(JFrame frame) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_MAIN);
        RoundedPanel loginCard = new RoundedPanel(22, CARD_BG);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(35, 45, 35, 45));
        loginCard.setPreferredSize(new Dimension(420, 430));

        JLabel title = new JLabel("Masuk ke Akunmu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Selamat datang kembali di BukaBuku!");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel emailWrapper = createInputWrapper("Email", new JTextField());
        JPanel passWrapper = createInputWrapper("Password", new JPasswordField());

        RoundedButton btnLogin = new RoundedButton("Login", COLOR_DARK_GREEN, Color.WHITE);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> navigateTo("Katalog", frame));

        JLabel linkRegister = new JLabel("Belum punya akun? Daftar sekarang.");
        linkRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        linkRegister.setForeground(COLOR_DARK_GREEN);
        linkRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Daftar", frame); }
        });

        loginCard.add(title);
        loginCard.add(Box.createRigidArea(new Dimension(0, 4)));
        loginCard.add(subtitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 25)));
        loginCard.add(emailWrapper);
        loginCard.add(Box.createRigidArea(new Dimension(0, 12)));
        loginCard.add(passWrapper);
        loginCard.add(Box.createRigidArea(new Dimension(0, 25)));
        loginCard.add(btnLogin);
        loginCard.add(Box.createRigidArea(new Dimension(0, 12)));
        loginCard.add(linkRegister);
        wrapper.add(loginCard);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(wrapper, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);
        return wrapperContent;
    }

    static JPanel createRegisterPanel(JFrame frame) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_MAIN);
        RoundedPanel registerCard = new RoundedPanel(22, CARD_BG);
        registerCard.setLayout(new BoxLayout(registerCard, BoxLayout.Y_AXIS));
        registerCard.setBorder(new EmptyBorder(30, 45, 30, 45));
        registerCard.setPreferredSize(new Dimension(420, 510));

        JLabel title = new JLabel("Daftar Akun Baru");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nameWrapper = createInputWrapper("Nama Lengkap", new JTextField());
        JPanel userWrapper = createInputWrapper("Username", new JTextField());
        JPanel emailWrapper = createInputWrapper("Email", new JTextField());
        JPanel passWrapper = createInputWrapper("Password", new JPasswordField());

        RoundedButton btnDaftar = new RoundedButton("Buat Akun", COLOR_DARK_GREEN, Color.WHITE);
        btnDaftar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnDaftar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnDaftar.addActionListener(e -> {
            showNotificationToast(frame, "Pendaftaran berhasil! Silakan login.");
            navigateTo("Login", frame);
        });

        JLabel linkLogin = new JLabel("Sudah punya akun? Masuk di sini.");
        linkLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        linkLogin.setForeground(COLOR_DARK_GREEN);
        linkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Login", frame); }
        });

        registerCard.add(title);
        registerCard.add(Box.createRigidArea(new Dimension(0, 20)));
        registerCard.add(nameWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 8)));
        registerCard.add(userWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 8)));
        registerCard.add(emailWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 8)));
        registerCard.add(passWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 25)));
        registerCard.add(btnDaftar);
        registerCard.add(Box.createRigidArea(new Dimension(0, 12)));
        registerCard.add(linkLogin);
        wrapper.add(registerCard);

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(wrapper, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);
        return wrapperContent;
    }

    // ================== HELPER UI METODE ==================
    static JPanel createInputWrapper(String labelText, JTextField inputField) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_SAGE, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 4)));
        wrapper.add(inputField);
        return wrapper;
    }

    static JLabel createMenuLabel(String text, JFrame frame) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo(text, frame); }
        });
        return lbl;
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

// ================== DATA STRUCTURES ==================
class Book {
    String title, author, category, imagePath, description;
    int price;
    public Book(String title, String author, String category, int price, String imagePath, String description) {
        this.title = title; this.author = author; this.category = category;
        this.price = price; this.imagePath = imagePath; this.description = description;
    }
}

class CartItem {
    Book book;
    int qty;
    boolean selected = false; 
    public CartItem(Book book, int qty) {
        this.book = book; this.qty = qty;
    }
}

class Order {
    String id, date, status;
    List<CartItem> items;
    int total;
    public Order(String id, String date, List<CartItem> items, int total, String status) {
        this.id = id; this.date = date; this.items = new ArrayList<>(items); 
        this.total = total; this.status = status;
    }
}

// ================== 🟢 CLASS DIALOG CHECKOUT & PEMBAYARAN ==================
class CheckoutDialog extends JDialog {
    public CheckoutDialog(JFrame parent, List<CartItem> selectedItems, int subtotal) {
        super(parent, true);
        setUndecorated(true);
        setSize(500, 560);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        int ongkir = 10000;
        int grandTotal = subtotal + ongkir;

        RoundedPanel mainCard = new RoundedPanel(25, Color.WHITE, new Color(200, 200, 200));
        mainCard.setLayout(new BorderLayout(15, 15));
        mainCard.setBorder(new EmptyBorder(22, 25, 22, 25));

        // 1. Header Dialog
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("Pembayaran & Checkout");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(BukaBukuApp.TEXT_DARK);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(230, 230, 230));

        // TOMBOL CLOSE - LEBIH BESAR & MERAH
        RoundedPanel closeBtnPanel = new RoundedPanel(15, new Color(254, 226, 228));
        closeBtnPanel.setPreferredSize(new Dimension(40, 40));
        closeBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        closeBtnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel closeIcon = new JLabel("×");
        closeIcon.setFont(new Font("Arial", Font.BOLD, 24));
        closeIcon.setForeground(new Color(220, 53, 69));
        closeBtnPanel.add(closeIcon);
        
        closeBtnPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
            public void mouseEntered(MouseEvent e) { 
                closeBtnPanel.setBackground(new Color(220, 53, 69));
                closeIcon.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) { 
                closeBtnPanel.setBackground(new Color(254, 226, 228));
                closeIcon.setForeground(new Color(220, 53, 69));
            }
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(closeBtnPanel, BorderLayout.EAST);
        
        header.add(titlePanel, BorderLayout.CENTER);
        header.add(separator, BorderLayout.SOUTH);

        // 2. Body Form
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form Pengiriman
        JTextField txtNama = new JTextField("Syifa Nazira");
        JTextField txtHp = new JTextField("081234567890");
        JTextArea txtAlamat = new JTextArea("Jl. Universitas No. 9, Medan", 2, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BukaBukuApp.COLOR_SAGE, 1, true),
            new EmptyBorder(4, 6, 4, 6)
        ));

        bodyPanel.add(BukaBukuApp.createInputWrapper("Nama Penerima", txtNama));
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        bodyPanel.add(BukaBukuApp.createInputWrapper("No. Telepon / WhatsApp", txtHp));
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // LABEL ALAMAT - PAKSA RATA KIRI
        JLabel lblAlamat = new JLabel("Alamat Lengkap Pengiriman");
        lblAlamat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAlamat.setForeground(BukaBukuApp.TEXT_DARK);
        lblAlamat.setHorizontalAlignment(SwingConstants.LEFT);

        bodyPanel.add(lblAlamat);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        bodyPanel.add(txtAlamat);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // LABEL METODE PEMBAYARAN - PAKSA RATA KIRI
        JLabel lblMethod = new JLabel("Pilih Metode Pembayaran");
        lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMethod.setForeground(BukaBukuApp.TEXT_DARK);
        lblMethod.setHorizontalAlignment(SwingConstants.LEFT);

        bodyPanel.add(lblMethod);

        String[] methods = {
            "Transfer Bank BCA (Virtual Account)",
            "Transfer Bank Mandiri (Virtual Account)",
            "QRIS / E-Wallet (GoPay, OVO, DANA)",
            "COD (Bayar Saat Barang Sampai)"
        };
        JComboBox<String> cbMethod = new JComboBox<>(methods);
        cbMethod.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbMethod.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbMethod.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfoPay = new JLabel("No. Virtual Account: 8801-2514-0212");
        lblInfoPay.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblInfoPay.setForeground(BukaBukuApp.COLOR_DARK_GREEN);
        lblInfoPay.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbMethod.addActionListener(e -> {
            String selected = (String) cbMethod.getSelectedItem();
            if (selected.contains("BCA")) {
                lblInfoPay.setText("No. Virtual Account BCA: 8801-2514-0212");
            } else if (selected.contains("Mandiri")) {
                lblInfoPay.setText("No. Virtual Account Mandiri: 8901-7788-9900");
            } else if (selected.contains("QRIS")) {
                lblInfoPay.setText("Silakan Scan Kode QRIS BukaBuku setelah konfirmasi");
            } else {
                lblInfoPay.setText("Siapkan uang tunai sesuai total harga saat kurir sampai");
            }
        });

        bodyPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        bodyPanel.add(cbMethod);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        bodyPanel.add(lblInfoPay);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Rincian Biaya
        JSeparator sep = new JSeparator();
        bodyPanel.add(sep);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel summaryBox = new JPanel(new GridLayout(3, 2, 0, 4));
        summaryBox.setOpaque(false);
        summaryBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        summaryBox.add(new JLabel("Subtotal Buku:"));
        JLabel lblSub = new JLabel("Rp " + String.format("%,d", subtotal).replace(',', '.'), SwingConstants.RIGHT);
        summaryBox.add(lblSub);

        summaryBox.add(new JLabel("Ongkos Kirim:"));
        JLabel lblOngkir = new JLabel("Rp " + String.format("%,d", ongkir).replace(',', '.'), SwingConstants.RIGHT);
        summaryBox.add(lblOngkir);

        JLabel lblTotalTeks = new JLabel("Total Bayar:");
        lblTotalTeks.setFont(new Font("Segoe UI", Font.BOLD, 13));
        summaryBox.add(lblTotalTeks);

        JLabel lblGrand = new JLabel("Rp " + String.format("%,d", grandTotal).replace(',', '.'), SwingConstants.RIGHT);
        lblGrand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGrand.setForeground(BukaBukuApp.COLOR_PINK_SOLID);
        summaryBox.add(lblGrand);

        bodyPanel.add(summaryBox);

        // 3. Footer Action Button
        RoundedButton btnPay = new RoundedButton("Konfirmasi & Bayar Sekarang", BukaBukuApp.COLOR_DARK_GREEN, Color.WHITE);
        btnPay.setPreferredSize(new Dimension(0, 55));
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        btnPay.addActionListener(e -> {
            if (txtNama.getText().trim().isEmpty() || txtAlamat.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mohon lengkapi Nama dan Alamat pengiriman!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String dateStr = new SimpleDateFormat("d MMMM yyyy 'pukul' HH.mm").format(new Date());
            Order newOrder = new Order("#" + (BukaBukuApp.orderHistory.size() + 1), dateStr, selectedItems, grandTotal, "BERHASIL");
            BukaBukuApp.orderHistory.add(0, newOrder);
            
            BukaBukuApp.cartData.removeAll(selectedItems);
            
            dispose();
            
            BukaBukuApp.showNotificationToast(parent, "Pembayaran Berhasil! Pesanan diproses.");
            BukaBukuApp.navigateTo("Riwayat", parent);
        });

        mainCard.add(header, BorderLayout.NORTH);
        mainCard.add(bodyPanel, BorderLayout.CENTER);
        mainCard.add(btnPay, BorderLayout.SOUTH);

        setContentPane(mainCard);
    }
}

// ================== KELAS UI CUSTOM ==================
class ModernDetailDialog extends JDialog {
    public ModernDetailDialog(JFrame parent, Book book) {
        super(parent, true);
        setUndecorated(true);
        setSize(420, 280);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        RoundedPanel panel = new RoundedPanel(22, Color.WHITE, new Color(200, 200, 200));
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(18, 22, 22, 22));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel title = new JLabel(book.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(31, 41, 55));

        JLabel closeBtn = new JLabel("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setForeground(new Color(156, 163, 175));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
        });

        header.add(title, BorderLayout.CENTER);
        header.add(closeBtn, BorderLayout.EAST);

        JLabel author = new JLabel("Oleh " + book.author);
        author.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        author.setForeground(BukaBukuApp.TEXT_MUTED);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(header);
        titlePanel.add(author);

        JTextArea desc = new JTextArea(book.description);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(BukaBukuApp.TEXT_DARK);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(desc, BorderLayout.CENTER);
        setContentPane(panel);
    }
}

class RoundedPanel extends JPanel {
    private int radius;
    private Color bgColor;
    private Color borderColor;

    public RoundedPanel(int radius, Color bgColor) {
        this.radius = radius;
        this.bgColor = bgColor;
        setOpaque(false);
    }
    public RoundedPanel(int radius, Color bgColor, Color borderColor) {
        this.radius = radius;
        this.bgColor = bgColor;
        this.borderColor = borderColor;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
        g2.dispose();
    }
}

class RoundedButton extends JButton {
    private Color bg;
    public RoundedButton(String text, Color bg, Color fg) {
        super(text);
        this.bg = bg;
        setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(0, 50));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() ? new Color(246, 226, 233) : bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        if (getModel().isRollover()) setForeground(new Color(124, 156, 123));
        else setForeground(Color.WHITE);
        g2.dispose();
        super.paintComponent(g);
    }
}