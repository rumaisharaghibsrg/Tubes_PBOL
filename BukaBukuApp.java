import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BukaBukuApp {
    // Tema Warna
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
            new Book("Si Anak Cahaya", "Tere Liye", "Fiksi Inspiratif", 85000, "assets/SiAnakCahaya.jpg", "novel si anak cahaya karya Tere Liye mengisahkan Nurmas (Nung), gadis kecil ceria dari pedalaman Sumatra..."),
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
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(COLOR_CREAM);

        // 1. NAVBAR
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(COLOR_SAGE);
        navbar.setBorder(new EmptyBorder(15, 40, 15, 40));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel logoLabel = loadIcon("assets/bukabuku.png", 45, 45);
        if (logoLabel.getIcon() == null) {
            logoLabel.setText("📖");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        }
        JLabel titleLabel = new JLabel("BukaBuku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        
        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        navbar.add(logoPanel, BorderLayout.WEST);

        logoPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Katalog", frame); }
        });

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
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
        menuPanel.add(new JLabel(" | "));
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

    // ================== HELPER FOOTER DINAMIS ==================
    static JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Mengubah warna dasar footer menjadi kuning air (COLOR_CREAM)
        footer.setBackground(COLOR_CREAM); 
        footer.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        JLabel footerLabel = new JLabel("© 2026 BukaBuku Kelompok 6");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
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
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                empty.setForeground(TEXT_MUTED);
                empty.setBorder(new EmptyBorder(10, 0, 0, 0));
                riwayatListPanel.add(empty);
            } else {
                for (Order order : orderHistory) {
                    riwayatListPanel.add(createOrderCard(order));
                    riwayatListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                }
            }
            riwayatListPanel.add(Box.createVerticalGlue());
            riwayatListPanel.revalidate(); riwayatListPanel.repaint();
        }
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
        
        new ModernAlertDialog(frame, "Sukses", book.title + " berhasil ditambahkan ke keranjang!").setVisible(true);
        updateNavbar();
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

    // ================== HALAMAN RIWAYAT ==================
    static JScrollPane createRiwayatPanel(JFrame frame) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BG_MAIN);
        container.setBorder(new EmptyBorder(30, 40, 0, 40));

        JLabel titleLabel = new JLabel("Riwayat Belanja");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        riwayatListPanel = new JPanel();
        riwayatListPanel.setLayout(new BoxLayout(riwayatListPanel, BoxLayout.Y_AXIS));
        riwayatListPanel.setOpaque(false);
        riwayatListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(riwayatListPanel);
        container.add(Box.createVerticalGlue());

        JPanel wrapperContent = new JPanel(new BorderLayout());
        wrapperContent.setBackground(BG_MAIN);
        wrapperContent.add(container, BorderLayout.CENTER);
        wrapperContent.add(createFooter(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapperContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    // Komponen Card Riwayat Pesanan
    static JPanel createOrderCard(Order order) {
        RoundedPanel card = new RoundedPanel(20, CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setOpaque(false);
        
        // Perbaikan kotak merah error: Diganti menjadi Badge "INV" yang elegan
        RoundedPanel iconReceipt = new RoundedPanel(10, COLOR_PINK_SOFT, COLOR_PINK_SOLID);
        iconReceipt.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel lblIcon = new JLabel("INV");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIcon.setForeground(COLOR_PINK_SOLID);
        iconReceipt.add(lblIcon);
        
        JPanel textHeader = new JPanel();
        textHeader.setLayout(new BoxLayout(textHeader, BoxLayout.Y_AXIS));
        textHeader.setOpaque(false);
        
        JLabel lblOrder = new JLabel("Pesanan " + order.id);
        lblOrder.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOrder.setForeground(TEXT_DARK);
        
        JLabel lblDate = new JLabel(order.date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDate.setForeground(TEXT_MUTED);
        
        textHeader.add(lblOrder);
        textHeader.add(lblDate);
        leftHeader.add(iconReceipt);
        leftHeader.add(textHeader);

        // Badge Status
        RoundedPanel badge = new RoundedPanel(15, new Color(209, 250, 229)); 
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JLabel lblStatus = new JLabel(order.status);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(16, 185, 129)); 
        badge.add(lblStatus);

        header.add(leftHeader, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(230, 230, 230));
        card.add(sep1);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // List Buku di Dalam Pesanan
        for (CartItem item : order.items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);

            JPanel leftItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
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
            title.setFont(new Font("Segoe UI", Font.BOLD, 15));
            title.setForeground(TEXT_DARK);
            
            JLabel qtyPrice = new JLabel(item.qty + " x Rp " + String.format("%,d", item.book.price).replace(',', '.'));
            qtyPrice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            qtyPrice.setForeground(TEXT_MUTED);
            
            textItem.add(title);
            textItem.add(Box.createRigidArea(new Dimension(0, 5)));
            textItem.add(qtyPrice);

            leftItem.add(imgLabel);
            leftItem.add(textItem);

            JLabel itemSubtotal = new JLabel("Rp " + String.format("%,d", item.book.price * item.qty).replace(',', '.'));
            itemSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
            itemSubtotal.setForeground(TEXT_DARK);

            itemPanel.add(leftItem, BorderLayout.WEST);
            itemPanel.add(itemSubtotal, BorderLayout.EAST);

            card.add(itemPanel);
            card.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(230, 230, 230));
        card.add(sep2);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Footer Card: Total Pembayaran
        JPanel rightAlignPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightAlignPanel.setOpaque(false);
        
        JPanel innerRight = new JPanel();
        innerRight.setLayout(new BoxLayout(innerRight, BoxLayout.Y_AXIS));
        innerRight.setOpaque(false);
        
        JLabel lblTotalText = new JLabel("TOTAL PEMBAYARAN");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalText.setForeground(TEXT_MUTED);
        lblTotalText.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTotalSum = new JLabel("Rp " + String.format("%,d", order.total).replace(',', '.'));
        lblTotalSum.setFont(new Font("Segoe UI", Font.BOLD, 22));
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
        container.setBorder(new EmptyBorder(30, 40, 0, 40));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Keranjang");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartListPanel.setOpaque(false);
        cartListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(cartListPanel);

        RoundedPanel summaryPanel = new RoundedPanel(20, CARD_BG);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        summaryPanel.setPreferredSize(new Dimension(350, 200));
        
        JLabel sumTitle = new JLabel("Ringkasan Belanja");
        sumTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sumTitle.setForeground(TEXT_DARK);
        sumTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JPanel rowTotal = new JPanel(new BorderLayout());
        rowTotal.setOpaque(false);
        rowTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTeksTotal = new JLabel("Total Dipilih");
        lblTeksTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTeksTotal.setForeground(TEXT_MUTED);
        
        labelTotalHarga = new JLabel("Rp 0");
        labelTotalHarga.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTotalHarga.setForeground(COLOR_PINK_SOLID);
        
        rowTotal.add(lblTeksTotal, BorderLayout.WEST);
        rowTotal.add(labelTotalHarga, BorderLayout.EAST);

        RoundedButton btnCheckout = new RoundedButton("Checkout", COLOR_SAGE, TEXT_DARK);
        btnCheckout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCheckout.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnCheckout.addActionListener(e -> {
            List<CartItem> selectedItems = new ArrayList<>();
            int total = 0;
            for (CartItem item : cartData) {
                if (item.selected) {
                    selectedItems.add(item);
                    total += (item.book.price * item.qty);
                }
            }
            if (selectedItems.isEmpty()) {
                new ModernAlertDialog(frame, "Peringatan", "Pilih minimal 1 buku untuk di-checkout.").setVisible(true);
                return;
            }
            
            String dateStr = new SimpleDateFormat("d MMMM yyyy 'pukul' HH.mm").format(new Date());
            Order newOrder = new Order("#" + (orderHistory.size() + 1), dateStr, selectedItems, total, "BERHASIL");
            orderHistory.add(0, newOrder);
            
            cartData.removeAll(selectedItems); 
            
            new ModernAlertDialog(frame, "Checkout Berhasil", "Pesanan kamu berhasil dibuat!").setVisible(true);
            navigateTo("Riwayat", frame);
        });

        summaryPanel.add(sumTitle);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        summaryPanel.add(rowTotal);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 25)));
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
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(800, 140));
        card.setPreferredSize(new Dimension(800, 140));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftArea.setOpaque(false);
        
        JCheckBox chkBox = new JCheckBox();
        chkBox.setOpaque(false);
        chkBox.setSelected(item.selected);
        
        JLabel imgLabel = loadIcon(item.book.imagePath, 70, 100);
        if (imgLabel.getIcon() == null) {
            imgLabel.setText("Foto");
            imgLabel.setPreferredSize(new Dimension(70, 100));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(Color.WHITE);
        }
        
        leftArea.add(chkBox);
        leftArea.add(imgLabel);

        JPanel centerArea = new JPanel();
        centerArea.setLayout(new BoxLayout(centerArea, BoxLayout.Y_AXIS));
        centerArea.setOpaque(false);

        JLabel title = new JLabel(item.book.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(TEXT_DARK);

        JLabel price = new JLabel(String.format("Rp %,d", item.book.price).replace(',', '.'));
        price.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        price.setForeground(TEXT_MUTED);

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        qtyPanel.setOpaque(false);
        
        RoundedButton btnMin = new RoundedButton("-", COLOR_PINK_SOFT, TEXT_DARK);
        btnMin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMin.setPreferredSize(new Dimension(35, 35));
        
        JLabel lblQty = new JLabel(String.valueOf(item.qty));
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        RoundedButton btnPlus = new RoundedButton("+", COLOR_PINK_SOFT, TEXT_DARK);
        btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPlus.setPreferredSize(new Dimension(35, 35));

        qtyPanel.add(btnMin);
        qtyPanel.add(lblQty);
        qtyPanel.add(btnPlus);

        centerArea.add(Box.createRigidArea(new Dimension(0, 5)));
        centerArea.add(title);
        centerArea.add(price);
        centerArea.add(Box.createRigidArea(new Dimension(0, 10)));
        centerArea.add(qtyPanel);

        JPanel rightArea = new JPanel(new BorderLayout());
        rightArea.setOpaque(false);
        
        JLabel subtotal = new JLabel("<html><div style='text-align: right;'><span style='font-size:11px;color:gray;'>Total</span><br/><b>" + 
                                     String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
        subtotal.setForeground(COLOR_PINK_SOLID);
        
        JLabel btnDel = new JLabel(" X Hapus");
        btnDel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDel.setForeground(new Color(220, 53, 69));
        btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDel.setHorizontalAlignment(SwingConstants.RIGHT);

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
                subtotal.setText("<html><div style='text-align: right;'><span style='font-size:11px;color:gray;'>Total</span><br/><b>" + 
                                 String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
                updateRingkasanBelanja(); 
            }
        });
        
        btnPlus.addActionListener(e -> {
            item.qty++; 
            lblQty.setText(String.valueOf(item.qty)); 
            subtotal.setText("<html><div style='text-align: right;'><span style='font-size:11px;color:gray;'>Total</span><br/><b>" + 
                             String.format("Rp %,d", item.book.price * item.qty).replace(',', '.') + "</b></div></html>");
            updateRingkasanBelanja();
        });

        btnDel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cartData.remove(item);
                refreshGrids(frame);
                updateNavbar();
            }
        });

        card.add(leftArea, BorderLayout.WEST);
        card.add(centerArea, BorderLayout.CENTER);
        card.add(rightArea, BorderLayout.EAST);
        
        JPanel cardWrapper = new JPanel();
        cardWrapper.setLayout(new BoxLayout(cardWrapper, BoxLayout.Y_AXIS));
        cardWrapper.setOpaque(false);
        cardWrapper.add(card);
        cardWrapper.setBorder(new EmptyBorder(0, 0, 15, 0));
        return cardWrapper;
    }

    // ================== HALAMAN KATALOG & WISHLIST ==================
    static JScrollPane createKatalogPanel(JFrame frame) {
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setOpaque(false);
        heroPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel welcomeLabel = new JLabel("Selamat Datang di BukaBuku");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Temukan jendela dunia dalam setiap halaman.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchContainer = new RoundedPanel(25, Color.WHITE);
        searchContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchContainer.setMaximumSize(new Dimension(500, 45));
        searchContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField searchField = new JTextField("Cari buku berdasarkan judul...");
        searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(TEXT_MUTED);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(400, 25));
        searchContainer.add(new JLabel("🔍"));
        searchContainer.add(searchField);

        heroPanel.add(welcomeLabel);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        heroPanel.add(subtitleLabel);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        heroPanel.add(searchContainer);

        katalogGridPanel = new JPanel(new GridLayout(0, 4, 25, 25));
        katalogGridPanel.setOpaque(false);
        katalogGridPanel.setBorder(new EmptyBorder(10, 40, 40, 40));
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
        headerPanel.setBorder(new EmptyBorder(30, 40, 10, 40));

        JLabel titleLabel = new JLabel("Wishlist Kamu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        headerPanel.add(titleLabel);

        wishlistGridPanel = new JPanel(new GridLayout(0, 4, 25, 25));
        wishlistGridPanel.setOpaque(false);
        wishlistGridPanel.setBorder(new EmptyBorder(10, 40, 40, 40));

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
        RoundedPanel card = new RoundedPanel(20, CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setOpaque(false);
        imageContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        imageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel imgLabel = loadIcon(book.imagePath, 140, 200);
        if (imgLabel.getIcon() == null) {
            imgLabel.setText("Gambar Kosong");
            imgLabel.setPreferredSize(new Dimension(140, 200));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(Color.WHITE);
        }
        imageContainer.add(imgLabel);

        JLabel catLabel = new JLabel(book.category.toUpperCase());
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLabel.setForeground(COLOR_DARK_GREEN);
        
        JLabel titleLabel = new JLabel("<html><body style='width: 160px;'>" + book.title + "</body></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_DARK);

        JLabel authorLabel = new JLabel("Oleh " + book.author);
        authorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        authorLabel.setForeground(TEXT_MUTED);

        String shortDesc = book.description.length() > 65 ? book.description.substring(0, 65) + "..." : book.description;
        JLabel descLabel = new JLabel("<html><body style='width: 170px; color: #6B7280; font-size: 11px;'>" + shortDesc + "</body></html>");
        
        JLabel readMoreLabel = new JLabel("Baca Selengkapnya");
        readMoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        readMoreLabel.setForeground(COLOR_DARK_GREEN);
        readMoreLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readMoreLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new ModernDetailDialog(parentFrame, book).setVisible(true); }
        });

        RoundedButton btnAdd = new RoundedButton("Tambah ke Keranjang", COLOR_DARK_GREEN, Color.WHITE);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38)); 
        btnAdd.addActionListener(e -> addToCart(book, parentFrame));
        
        JPanel priceHeartPanel = new JPanel(new BorderLayout());
        priceHeartPanel.setOpaque(false);
        priceHeartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel priceLabel = new JLabel(String.format("Rp %,d", book.price).replace(',', '.'));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(TEXT_DARK);
        
        boolean isInWishlist = wishlistData.contains(book);
        JLabel heartLabel = new JLabel(isInWishlist ? "♥" : "♡");
        heartLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heartLabel.setForeground(isInWishlist ? COLOR_PINK_SOLID : Color.LIGHT_GRAY);
        heartLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        heartLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (wishlistData.contains(book)) wishlistData.remove(book);
                else wishlistData.add(book);
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
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(catLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        card.add(authorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 2)));
        card.add(readMoreLabel);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnAdd);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(priceHeartPanel);

        return card;
    }

    // ================== HALAMAN LOGIN & DAFTAR ==================
    static JPanel createLoginPanel(JFrame frame) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_MAIN);
        RoundedPanel loginCard = new RoundedPanel(25, CARD_BG);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        loginCard.setPreferredSize(new Dimension(450, 450));

        JLabel title = new JLabel("Masuk ke Akunmu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Selamat datang kembali di BukaBuku!");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel emailWrapper = createInputWrapper("Email", new JTextField());
        JPanel passWrapper = createInputWrapper("Password", new JPasswordField());

        RoundedButton btnLogin = new RoundedButton("Login", COLOR_DARK_GREEN, Color.WHITE);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> navigateTo("Katalog", frame));

        JLabel linkRegister = new JLabel("Belum punya akun? Daftar sekarang.");
        linkRegister.setForeground(COLOR_DARK_GREEN);
        linkRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Daftar", frame); }
        });

        loginCard.add(title);
        loginCard.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCard.add(subtitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(emailWrapper);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(passWrapper);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(btnLogin);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
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
        RoundedPanel registerCard = new RoundedPanel(25, CARD_BG);
        registerCard.setLayout(new BoxLayout(registerCard, BoxLayout.Y_AXIS));
        registerCard.setBorder(new EmptyBorder(30, 50, 30, 50));
        registerCard.setPreferredSize(new Dimension(450, 550));

        JLabel title = new JLabel("Daftar Akun Baru");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nameWrapper = createInputWrapper("Nama Lengkap", new JTextField());
        JPanel userWrapper = createInputWrapper("Username", new JTextField());
        JPanel emailWrapper = createInputWrapper("Email", new JTextField());
        JPanel passWrapper = createInputWrapper("Password", new JPasswordField());

        RoundedButton btnDaftar = new RoundedButton("Buat Akun", COLOR_DARK_GREEN, Color.WHITE);
        btnDaftar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnDaftar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnDaftar.addActionListener(e -> {
            new ModernAlertDialog(frame, "Pendaftaran Sukses", "Akun berhasil dibuat! Silakan login untuk melanjutkan.").setVisible(true);
            navigateTo("Login", frame);
        });

        JLabel linkLogin = new JLabel("Sudah punya akun? Masuk di sini.");
        linkLogin.setForeground(COLOR_DARK_GREEN);
        linkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { navigateTo("Login", frame); }
        });

        registerCard.add(title);
        registerCard.add(Box.createRigidArea(new Dimension(0, 30)));
        registerCard.add(nameWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 10)));
        registerCard.add(userWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 10)));
        registerCard.add(emailWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 10)));
        registerCard.add(passWrapper);
        registerCard.add(Box.createRigidArea(new Dimension(0, 30)));
        registerCard.add(btnDaftar);
        registerCard.add(Box.createRigidArea(new Dimension(0, 15)));
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
        
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_SAGE, 2, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(inputField);
        return wrapper;
    }

    static JLabel createMenuLabel(String text, JFrame frame) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

// ================== KELAS UI CUSTOM ==================
class ModernAlertDialog extends JDialog {
    public ModernAlertDialog(JFrame parent, String titleText, String messageText) {
        super(parent, true);
        setUndecorated(true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        RoundedPanel panel = new RoundedPanel(25, Color.WHITE, new Color(50, 50, 50));
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(31, 41, 55));

        JLabel message = new JLabel("<html><center>" + messageText + "</center></html>", SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        message.setForeground(new Color(107, 114, 128));

        RoundedButton btnOk = new RoundedButton("OK", BukaBukuApp.COLOR_DARK_GREEN, Color.WHITE);
        btnOk.setPreferredSize(new Dimension(100, 35));
        btnOk.addActionListener(e -> dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(btnOk);

        panel.add(title, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(panel);
    }
}

class ModernDetailDialog extends JDialog {
    public ModernDetailDialog(JFrame parent, Book book) {
        super(parent, true);
        setUndecorated(true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        RoundedPanel panel = new RoundedPanel(25, Color.WHITE, new Color(50, 50, 50));
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel title = new JLabel(book.title);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(31, 41, 55));

        JLabel closeBtn = new JLabel("X");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeBtn.setForeground(new Color(156, 163, 175));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
        });

        header.add(title, BorderLayout.CENTER);
        header.add(closeBtn, BorderLayout.EAST);

        JLabel author = new JLabel("Oleh " + book.author);
        author.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(header);
        titlePanel.add(author);

        JTextArea desc = new JTextArea(book.description);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));

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
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() ? new Color(246, 226, 233) : bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        if (getModel().isRollover()) setForeground(new Color(124, 156, 123));
        else setForeground(Color.WHITE);
        g2.dispose();
        super.paintComponent(g);
    }
}