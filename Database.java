import java.security.SecureRandom;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class Database {
    private static final int PASSWORD_ITERATIONS = 120000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Database() {}

    static Connection connect() throws SQLException {
        String url = setting("DB_URL",
                "jdbc:mysql://localhost:3306/tubespbol?useSSL=false&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=UTC&connectTimeout=5000&socketTimeout=10000");
        String user = setting("DB_USER", "umi");
        String password = setting("DB_PASSWORD", "umihebat");
        return DriverManager.getConnection(url, user, password);
    }

    private static String setting(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    static void initialize() throws SQLException {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS books ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "title VARCHAR(255) NOT NULL, author VARCHAR(255) NOT NULL,"
                    + "category VARCHAR(120) NOT NULL, price BIGINT NOT NULL,"
                    + "image_path VARCHAR(500) NOT NULL, description TEXT NOT NULL,"
                    + "stock INT NOT NULL DEFAULT 0)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "full_name VARCHAR(255) NOT NULL, username VARCHAR(80) NOT NULL UNIQUE,"
                    + "email VARCHAR(255) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL,"
                    + "role VARCHAR(40) NOT NULL DEFAULT 'Pelanggan',"
                    + "status VARCHAR(40) NOT NULL DEFAULT 'Aktif')");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS orders ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "user_id BIGINT NULL, customer_name VARCHAR(255) NOT NULL,"
                    + "phone VARCHAR(60) NOT NULL DEFAULT '', address TEXT NOT NULL,"
                    + "payment_method VARCHAR(120) NOT NULL DEFAULT '',"
                    + "total BIGINT NOT NULL, status VARCHAR(40) NOT NULL,"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS order_items ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "order_id BIGINT NOT NULL, book_id BIGINT NULL,"
                    + "title VARCHAR(255) NOT NULL, quantity INT NOT NULL,"
                    + "unit_price BIGINT NOT NULL,"
                    + "CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE)");
        }
        seedBooks();
    }

    private static void seedBooks() throws SQLException {
        Object[][] books = {
                {"Bumi", "Tere Liye", "Fiksi Petualangan", 92000, "assets/Bumi.jpeg", "Raib adalah remaja SMA yang memiliki kemampuan unik bisa menghilang dengan menutup wajahnya.", 15},
                {"Hujan", "Tere Liye", "Fiksi Ilmiah & Romansa", 90000, "assets/Hujan.jpeg", "Kisah Lail dan perjalanan hidupnya setelah bencana besar.", 8},
                {"Si Anak Cahaya", "Tere Liye", "Fiksi Inspiratif", 85000, "assets/SiAnakCahaya.jpg", "Kisah Nurmas, seorang gadis kecil ceria dan tangguh dari pedalaman Sumatra.", 3},
                {"Guru Aini", "Andrea Hirata", "Fiksi Pendidikan", 92000, "assets/GuruAini.png", "Kisah guru matematika idealis yang mengajar di pelosok.", 20},
                {"Negeri 5 Menara", "A. Fuadi", "Fiksi Inspiratif", 110000, "assets/Negeri5Menara.jpg", "Perjalanan Alif menuntut ilmu di Pondok Madani.", 12},
                {"Loneliness Is My Best Friend", "Alvi Syahrin", "Self Improvement", 94000, "assets/LonelinessIsMyBestFriend.jpg", "Buku tentang kesepian dan cara memahaminya.", 4},
                {"Overthinking Is My Hobby", "Alvi Syahrin", "Self Improvement", 94000, "assets/OverthinkingIsMyHobby,AndIHateIt.jpg", "Panduan untuk meredam kebiasaan overthinking.", 18},
                {"Laskar Pelangi", "Andrea Hirata", "Fiksi Pendidikan", 90000, "assets/LaskarPelangi.jpg", "Kisah perjuangan anak-anak Belitung meraih pendidikan.", 25},
                {"Ayahku (Bukan) Pembohong", "Tere Liye", "Fiksi Inspiratif", 85000, "assets/AyahkuBukanPembohong.jpg", "Dam tumbuh dengan kisah-kisah luar biasa dari ayahnya.", 10},
                {"Moga Bunda Disayang Allah", "Tere Liye", "Drama Inspiratif", 85000, "assets/MogaBundaDisayangAllah.jpeg", "Kisah Melati dan perjuangan untuk berkomunikasi.", 7},
                {"Janji", "Tere Liye", "Fiksi Psikologis", 90000, "assets/Janji.jpeg", "Kisah tiga santri yang menjalani perjalanan pencarian makna.", 14},
                {"Dia adalah Kakakku", "Tere Liye", "Drama Keluarga", 85000, "assets/DiaAdalahKakakku.jpg", "Kisah perjuangan Laisa untuk keluarganya.", 2}
        };
        String sql = "INSERT INTO books (title, author, category, price, image_path, description, stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect(); Statement lock = connection.createStatement()) {
            lock.execute("LOCK TABLES books WRITE");
            try {
                try (ResultSet rows = lock.executeQuery("SELECT COUNT(*) FROM books")) {
                    rows.next();
                    if (rows.getLong(1) != 0) return;
                }
                try (PreparedStatement insert = connection.prepareStatement(sql)) {
                    for (Object[] book : books) {
                        for (int i = 0; i < book.length; i++) insert.setObject(i + 1, book[i]);
                        insert.addBatch();
                    }
                    insert.executeBatch();
                }
            } finally {
                lock.execute("UNLOCK TABLES");
            }
        }
    }

    static List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, category, price, image_path, description, stock "
                + "FROM books ORDER BY id";
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                books.add(new Book(rows.getInt("id"), rows.getString("title"), rows.getString("author"),
                        rows.getString("category"), rows.getInt("price"), rows.getString("image_path"),
                        rows.getString("description"), rows.getInt("stock")));
            }
        }
        return books;
    }

    static int insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, category, price, image_path, description, stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindBook(insert, book);
            insert.executeUpdate();
            try (ResultSet keys = insert.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Database tidak mengembalikan ID buku.");
                return keys.getInt(1);
            }
        }
    }

    static void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, category=?, price=?, image_path=?, stock=? WHERE id=?";
        try (Connection connection = connect(); PreparedStatement update = connection.prepareStatement(sql)) {
            update.setString(1, book.title);
            update.setString(2, book.author);
            update.setString(3, book.category);
            update.setInt(4, book.price);
            update.setString(5, book.imagePath);
            update.setInt(6, book.stock);
            update.setInt(7, book.id);
            if (update.executeUpdate() != 1) throw new SQLException("Buku tidak ditemukan.");
        }
    }

    private static void bindBook(PreparedStatement statement, Book book) throws SQLException {
        statement.setString(1, book.title);
        statement.setString(2, book.author);
        statement.setString(3, book.category);
        statement.setInt(4, book.price);
        statement.setString(5, book.imagePath);
        statement.setString(6, book.description);
        statement.setInt(7, book.stock);
    }

    static void deleteBook(int id) throws SQLException {
        try (Connection connection = connect();
             PreparedStatement delete = connection.prepareStatement("DELETE FROM books WHERE id=?")) {
            delete.setInt(1, id);
            if (delete.executeUpdate() != 1) throw new SQLException("Buku tidak ditemukan.");
        }
    }

    static final class UserAccount {
        final int id;
        final String fullName;
        final String username;
        final String email;
        final String role;
        final String status;

        UserAccount(int id, String fullName, String username, String email, String role, String status) {
            this.id = id;
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.role = role;
            this.status = status;
        }
    }

    static List<UserAccount> getUsers() throws SQLException {
        List<UserAccount> users = new ArrayList<>();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(
                     "SELECT id, full_name, username, email, role, status FROM users ORDER BY id")) {
            while (rows.next()) {
                users.add(new UserAccount(rows.getInt("id"), rows.getString("full_name"),
                        rows.getString("username"), rows.getString("email"),
                        rows.getString("role"), rows.getString("status")));
            }
        }
        return users;
    }

    static int createUser(String fullName, String username, String email, String password,
                          String role, String status) throws SQLException {
        String sql = "INSERT INTO users (full_name, username, email, password_hash, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement insert = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, fullName);
            insert.setString(2, username);
            insert.setString(3, email);
            insert.setString(4, hashPassword(password));
            insert.setString(5, role);
            insert.setString(6, status);
            insert.executeUpdate();
            try (ResultSet keys = insert.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Database tidak mengembalikan ID pengguna.");
                return keys.getInt(1);
            }
        } catch (SQLException ex) {
            throw userWriteError(ex);
        }
    }

    static void updateUser(int id, String fullName, String email, String role, String status)
            throws SQLException {
        try (Connection connection = connect();
             PreparedStatement update = connection.prepareStatement(
                     "UPDATE users SET full_name=?, email=?, role=?, status=? WHERE id=?")) {
            update.setString(1, fullName);
            update.setString(2, email);
            update.setString(3, role);
            update.setString(4, status);
            update.setInt(5, id);
            if (update.executeUpdate() != 1) throw new SQLException("Pengguna tidak ditemukan.");
        } catch (SQLException ex) {
            throw userWriteError(ex);
        }
    }

    static void deleteUser(int id) throws SQLException {
        try (Connection connection = connect();
             PreparedStatement delete = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
            delete.setInt(1, id);
            if (delete.executeUpdate() != 1) throw new SQLException("Pengguna tidak ditemukan.");
        }
    }

    static UserAccount registerUser(String fullName, String username, String email, String password)
            throws SQLException {
        int id = createUser(fullName, username, email, password, "Pelanggan", "Aktif");
        return new UserAccount(id, fullName, username, email, "Pelanggan", "Aktif");
    }

    static UserAccount authenticate(String login, String password) throws SQLException {
        String sql = "SELECT id, full_name, username, email, password_hash, role, status "
                + "FROM users WHERE LOWER(email)=LOWER(?) OR LOWER(username)=LOWER(?)";
        try (Connection connection = connect(); PreparedStatement query = connection.prepareStatement(sql)) {
            query.setString(1, login);
            query.setString(2, login);
            try (ResultSet row = query.executeQuery()) {
                if (!row.next() || !"Aktif".equals(row.getString("status"))
                        || !verifyPassword(password, row.getString("password_hash"))) {
                    return null;
                }
                return new UserAccount(row.getInt("id"), row.getString("full_name"),
                        row.getString("username"), row.getString("email"),
                        row.getString("role"), row.getString("status"));
            }
        }
    }

    private static String hashPassword(String password) throws SQLException {
        if (password == null || password.length() < 8) {
            throw new SQLException("Password harus memiliki minimal 8 karakter.");
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = derivePassword(password.toCharArray(), salt, PASSWORD_ITERATIONS);
        return PASSWORD_ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    private static boolean verifyPassword(String password, String encoded) throws SQLException {
        if (password == null || encoded == null) return false;
        String[] parts = encoded.split(":", 3);
        if (parts.length != 3) return false;
        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            if (iterations < 1 || iterations > 1000000) return false;
            byte[] actual = derivePassword(password.toCharArray(), salt, iterations);
            return MessageDigest.isEqual(actual, expected);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] derivePassword(char[] password, byte[] salt, int iterations) throws SQLException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (java.security.GeneralSecurityException ex) {
            throw new SQLException("Tidak dapat memproses password.", ex);
        } finally {
            spec.clearPassword();
            java.util.Arrays.fill(password, '\0');
        }
    }

    private static SQLException userWriteError(SQLException ex) {
        if ("23000".equals(ex.getSQLState())) {
            return new SQLException("Username atau email sudah terdaftar.", ex);
        }
        return ex;
    }

    static List<Order> getOrders(Integer userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customer_name, created_at, total, status FROM orders "
                + (userId == null ? "" : "WHERE user_id=? ") + "ORDER BY created_at DESC, id DESC";
        try (Connection connection = connect(); PreparedStatement query = connection.prepareStatement(sql)) {
            if (userId != null) query.setInt(1, userId);
            try (ResultSet rows = query.executeQuery()) {
                while (rows.next()) {
                    int id = rows.getInt("id");
                    List<CartItem> items = getOrderItems(connection, id);
                    Timestamp createdAt = rows.getTimestamp("created_at");
                    String date = new java.text.SimpleDateFormat(
                            "d MMMM yyyy 'pukul' HH.mm", Locale.forLanguageTag("id-ID")).format(createdAt);
                    Order order = new Order(orderNumber(id), date, items,
                            rows.getLong("total"), rows.getString("status"));
                    order.customerName = rows.getString("customer_name");
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    private static List<CartItem> getOrderItems(Connection connection, int orderId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT title, quantity, unit_price FROM order_items WHERE order_id=? ORDER BY id";
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, orderId);
            try (ResultSet rows = query.executeQuery()) {
                while (rows.next()) {
                    Book book = new Book(0, rows.getString("title"), "", "", rows.getInt("unit_price"),
                            "", "", 0);
                    items.add(new CartItem(book, rows.getInt("quantity")));
                }
            }
        }
        return items;
    }

    static Order createOrder(String customerName, String phone, String address, String paymentMethod,
                             List<CartItem> items, Integer userId) throws SQLException {
        if (items.isEmpty()) throw new SQLException("Keranjang checkout kosong.");
        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                long subtotal = 0;
                List<Integer> currentPrices = new ArrayList<>();
                for (CartItem item : items) {
                    try (PreparedStatement lock = connection.prepareStatement(
                            "SELECT price, stock FROM books WHERE id=? FOR UPDATE")) {
                        lock.setInt(1, item.book.id);
                        try (ResultSet row = lock.executeQuery()) {
                            if (!row.next() || row.getInt("stock") < item.qty) {
                                throw new SQLException("Stok buku \"" + item.book.title + "\" tidak mencukupi.");
                            }
                            int price = row.getInt("price");
                            if (price != item.book.price) {
                                throw new SQLException("Harga buku \"" + item.book.title
                                        + "\" berubah. Muat ulang katalog sebelum checkout.");
                            }
                            currentPrices.add(price);
                            subtotal += (long) price * item.qty;
                        }
                    }
                }

                long total = subtotal + 10000;
                String sql = "INSERT INTO orders (user_id, customer_name, phone, address, payment_method, total, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
                long orderId;
                try (PreparedStatement insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    if (userId == null) insert.setNull(1, java.sql.Types.BIGINT);
                    else insert.setInt(1, userId);
                    insert.setString(2, customerName);
                    insert.setString(3, phone);
                    insert.setString(4, address);
                    insert.setString(5, paymentMethod);
                    insert.setLong(6, total);
                    insert.executeUpdate();
                    try (ResultSet keys = insert.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Database tidak mengembalikan ID pesanan.");
                        orderId = keys.getLong(1);
                    }
                }

                for (int i = 0; i < items.size(); i++) {
                    CartItem item = items.get(i);
                    try (PreparedStatement updateStock = connection.prepareStatement(
                            "UPDATE books SET stock=stock-? WHERE id=? AND stock>=?")) {
                        updateStock.setInt(1, item.qty);
                        updateStock.setInt(2, item.book.id);
                        updateStock.setInt(3, item.qty);
                        if (updateStock.executeUpdate() != 1) {
                            throw new SQLException("Stok buku \"" + item.book.title + "\" berubah. Muat ulang katalog.");
                        }
                    }
                    try (PreparedStatement insertItem = connection.prepareStatement(
                            "INSERT INTO order_items (order_id, book_id, title, quantity, unit_price) "
                                    + "VALUES (?, ?, ?, ?, ?)")) {
                        insertItem.setLong(1, orderId);
                        insertItem.setInt(2, item.book.id);
                        insertItem.setString(3, item.book.title);
                        insertItem.setInt(4, item.qty);
                        insertItem.setInt(5, currentPrices.get(i));
                        insertItem.executeUpdate();
                    }
                }
                connection.commit();
                return new Order(orderNumber((int) orderId),
                        new java.text.SimpleDateFormat("d MMMM yyyy 'pukul' HH.mm",
                                Locale.forLanguageTag("id-ID")).format(new java.util.Date()),
                        items, total, "Pending");
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    static String saveAdminOrder(String orderId, String customerName, String item, long amount, String status)
            throws SQLException {
        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                long id;
                if (orderId == null) {
                    String sql = "INSERT INTO orders (customer_name, address, total, status) VALUES (?, '', ?, ?)";
                    try (PreparedStatement insert = connection.prepareStatement(
                            sql, Statement.RETURN_GENERATED_KEYS)) {
                        insert.setString(1, customerName);
                        insert.setLong(2, amount);
                        insert.setString(3, status);
                        insert.executeUpdate();
                        try (ResultSet keys = insert.getGeneratedKeys()) {
                            if (!keys.next()) throw new SQLException("Database tidak mengembalikan ID pesanan.");
                            id = keys.getLong(1);
                        }
                    }
                } else {
                    id = parseId(orderId, "ORD-");
                    try (PreparedStatement update = connection.prepareStatement(
                            "UPDATE orders SET customer_name=?, total=?, status=? WHERE id=?")) {
                        update.setString(1, customerName);
                        update.setLong(2, amount);
                        update.setString(3, status);
                        update.setLong(4, id);
                        if (update.executeUpdate() != 1) throw new SQLException("Pesanan tidak ditemukan.");
                    }
                    try (PreparedStatement deleteItems = connection.prepareStatement(
                            "DELETE FROM order_items WHERE order_id=?")) {
                        deleteItems.setLong(1, id);
                        deleteItems.executeUpdate();
                    }
                }
                try (PreparedStatement itemInsert = connection.prepareStatement(
                        "INSERT INTO order_items (order_id, title, quantity, unit_price) VALUES (?, ?, 1, ?)")) {
                    itemInsert.setLong(1, id);
                    itemInsert.setString(2, item);
                    itemInsert.setLong(3, amount);
                    itemInsert.executeUpdate();
                }
                connection.commit();
                return orderNumber((int) id);
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    static void updateOrderStatus(String orderId, String status) throws SQLException {
        try (Connection connection = connect();
             PreparedStatement update = connection.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {
            update.setString(1, status);
            update.setLong(2, parseId(orderId, "ORD-"));
            if (update.executeUpdate() != 1) throw new SQLException("Pesanan tidak ditemukan.");
        }
    }

    static void deleteOrder(String orderId) throws SQLException {
        try (Connection connection = connect();
             PreparedStatement delete = connection.prepareStatement("DELETE FROM orders WHERE id=?")) {
            delete.setLong(1, parseId(orderId, "ORD-"));
            if (delete.executeUpdate() != 1) throw new SQLException("Pesanan tidak ditemukan.");
        }
    }

    private static int parseId(String value, String prefix) throws SQLException {
        try {
            return Integer.parseInt(value.substring(prefix.length()));
        } catch (RuntimeException ex) {
            throw new SQLException("ID data tidak valid.", ex);
        }
    }

    static String bookNumber(int id) {
        return String.format("BK-%03d", id);
    }

    static String userNumber(int id) {
        return String.format("USR-%03d", id);
    }

    static String orderNumber(int id) {
        return String.format("ORD-%03d", id);
    }
}
