package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pabrik_bela_negara_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createTableIfNotExists(connection);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver tidak ditemukan: " + e.getMessage());
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS produksi (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nama_produk VARCHAR(100) NOT NULL,
                jumlah_unit INT NOT NULL,
                jam_kerja DOUBLE NOT NULL,
                jumlah_tenaga_kerja INT NOT NULL,
                biaya_bahan_baku DOUBLE NOT NULL,
                biaya_tenaga_kerja DOUBLE NOT NULL,
                efisiensi_produksi DOUBLE NOT NULL,
                total_biaya_produksi DOUBLE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }
}