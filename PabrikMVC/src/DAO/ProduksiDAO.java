package dao;

import model.Produksi;
import database.DatabaseConnection;
import exception.ProductionException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduksiDAO {
    private Connection connection;

    public ProduksiDAO() throws ProductionException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new ProductionException("Gagal menginisialisasi koneksi database: " + e.getMessage());
        }
    }

    public void insertProduksi(Produksi produksi) throws ProductionException {
        if (!produksi.isEfficiencyValid()) {
            throw new ProductionException("Data tidak dapat disimpan! Efisiensi produksi kurang dari 20% (" + 
                String.format("%.2f", produksi.getEfisiensiProduksi()) + "%)");
        }

        String sql = "INSERT INTO produksi (nama_produk, jumlah_unit, jam_kerja, jumlah_tenaga_kerja, " +
                    "biaya_bahan_baku, biaya_tenaga_kerja, efisiensi_produksi, total_biaya_produksi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produksi.getNamaProduk());
            stmt.setInt(2, produksi.getJumlahUnit());
            stmt.setDouble(3, produksi.getJamKerja());
            stmt.setInt(4, produksi.getJumlahTenagaKerja());
            stmt.setDouble(5, produksi.getBiayaBahanBaku());
            stmt.setDouble(6, produksi.getBiayaTenagaKerja());
            stmt.setDouble(7, produksi.getEfisiensiProduksi());
            stmt.setDouble(8, produksi.getTotalBiayaProduksi());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ProductionException("Gagal menyimpan data produksi");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produksi.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new ProductionException("Error saat menyimpan data: " + e.getMessage());
        }
    }

    public List<Produksi> getAllProduksi() throws ProductionException {
        List<Produksi> produksiList = new ArrayList<>();
        String sql = "SELECT * FROM produksi ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produksi produksi = new Produksi();
                produksi.setId(rs.getInt("id"));
                produksi.setNamaProduk(rs.getString("nama_produk"));
                produksi.setJumlahUnit(rs.getInt("jumlah_unit"));
                produksi.setJamKerja(rs.getDouble("jam_kerja"));
                produksi.setJumlahTenagaKerja(rs.getInt("jumlah_tenaga_kerja"));
                produksi.setBiayaBahanBaku(rs.getDouble("biaya_bahan_baku"));
                produksi.setBiayaTenagaKerja(rs.getDouble("biaya_tenaga_kerja"));
                produksi.setEfisiensiProduksi(rs.getDouble("efisiensi_produksi"));
                produksi.setTotalBiayaProduksi(rs.getDouble("total_biaya_produksi"));
                produksiList.add(produksi);
            }
        } catch (SQLException e) {
            throw new ProductionException("Error saat mengambil data: " + e.getMessage());
        }

        return produksiList;
    }

    public void updateProduksi(Produksi produksi) throws ProductionException {
        if (!produksi.isEfficiencyValid()) {
            throw new ProductionException("Data tidak dapat diupdate! Efisiensi produksi kurang dari 20% (" + 
                String.format("%.2f", produksi.getEfisiensiProduksi()) + "%)");
        }

        String sql = "UPDATE produksi SET nama_produk=?, jumlah_unit=?, jam_kerja=?, jumlah_tenaga_kerja=?, " +
                    "biaya_bahan_baku=?, biaya_tenaga_kerja=?, efisiensi_produksi=?, total_biaya_produksi=? WHERE id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produksi.getNamaProduk());
            stmt.setInt(2, produksi.getJumlahUnit());
            stmt.setDouble(3, produksi.getJamKerja());
            stmt.setInt(4, produksi.getJumlahTenagaKerja());
            stmt.setDouble(5, produksi.getBiayaBahanBaku());
            stmt.setDouble(6, produksi.getBiayaTenagaKerja());
            stmt.setDouble(7, produksi.getEfisiensiProduksi());
            stmt.setDouble(8, produksi.getTotalBiayaProduksi());
            stmt.setInt(9, produksi.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ProductionException("Data dengan ID " + produksi.getId() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new ProductionException("Error saat mengupdate data: " + e.getMessage());
        }
    }

    public Produksi getProduksiById(int id) throws ProductionException {
        String sql = "SELECT * FROM produksi WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produksi produksi = new Produksi();
                    produksi.setId(rs.getInt("id"));
                    produksi.setNamaProduk(rs.getString("nama_produk"));
                    produksi.setJumlahUnit(rs.getInt("jumlah_unit"));
                    produksi.setJamKerja(rs.getDouble("jam_kerja"));
                    produksi.setJumlahTenagaKerja(rs.getInt("jumlah_tenaga_kerja"));
                    produksi.setBiayaBahanBaku(rs.getDouble("biaya_bahan_baku"));
                    produksi.setBiayaTenagaKerja(rs.getDouble("biaya_tenaga_kerja"));
                    produksi.setEfisiensiProduksi(rs.getDouble("efisiensi_produksi"));
                    produksi.setTotalBiayaProduksi(rs.getDouble("total_biaya_produksi"));
                    return produksi;
                } else {
                    throw new ProductionException("Data dengan ID " + id + " tidak ditemukan");
                }
            }
        } catch (SQLException e) {
            throw new ProductionException("Error saat mengambil data: " + e.getMessage());
        }
    }

    public void deleteProduksi(int id) throws ProductionException {
        String sql = "DELETE FROM produksi WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new ProductionException("Data dengan ID " + id + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new ProductionException("Error saat menghapus data: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error saat menutup koneksi: " + e.getMessage());
        }
    }
}