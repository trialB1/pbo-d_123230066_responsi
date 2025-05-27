package controller;

import model.Produksi;
import dao.ProduksiDAO;
import exception.ProductionException;
import java.util.List;

public class ProduksiController {
    private ProduksiDAO produksiDAO;

    public ProduksiController() throws ProductionException {
        this.produksiDAO = new ProduksiDAO();
    }

    public String addProduksi(String namaProduk, int jumlahUnit, double jamKerja, int jumlahTenagaKerja, double biayaBahanBaku) {
        try {
            if (namaProduk == null || namaProduk.trim().isEmpty()) {
                throw new ProductionException("Nama produk tidak boleh kosong");
            }
            if (jumlahUnit <= 0) {
                throw new ProductionException("Jumlah unit harus lebih dari 0");
            }
            if (jamKerja <= 0) {
                throw new ProductionException("Jam kerja harus lebih dari 0");
            }
            if (jumlahTenagaKerja <= 0) {
                throw new ProductionException("Jumlah tenaga kerja harus lebih dari 0");
            }
            if (biayaBahanBaku < 0) {
                throw new ProductionException("Biaya bahan baku tidak boleh negatif");
            }

            Produksi produksi = new Produksi(namaProduk, jumlahUnit, jamKerja, jumlahTenagaKerja, biayaBahanBaku);
            produksiDAO.insertProduksi(produksi);
            
            return "Data produksi berhasil disimpan!\n" +
                   "Produk: " + produksi.getNamaProduk() + "\n" +
                   "Efisiensi Produksi: " + String.format("%.1f", produksi.getEfisiensiProduksi()) + "%\n" +
                   "Total Biaya Produksi: Rp " + String.format("%.0f", produksi.getTotalBiayaProduksi());

        } catch (ProductionException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<Produksi> getAllProduksi() throws ProductionException {
        return produksiDAO.getAllProduksi();
    }

    public String updateProduksi(int id, String namaProduk, int jumlahUnit, double jamKerja, int jumlahTenagaKerja, double biayaBahanBaku) {
        try {
            if (namaProduk == null || namaProduk.trim().isEmpty()) {
                throw new ProductionException("Nama produk tidak boleh kosong");
            }
            if (jumlahUnit <= 0) {
                throw new ProductionException("Jumlah unit harus lebih dari 0");
            }
            if (jamKerja <= 0) {
                throw new ProductionException("Jam kerja harus lebih dari 0");
            }
            if (jumlahTenagaKerja <= 0) {
                throw new ProductionException("Jumlah tenaga kerja harus lebih dari 0");
            }
            if (biayaBahanBaku < 0) {
                throw new ProductionException("Biaya bahan baku tidak boleh negatif");
            }

            Produksi produksi = new Produksi(namaProduk, jumlahUnit, jamKerja, jumlahTenagaKerja, biayaBahanBaku);
            produksi.setId(id);
            produksiDAO.updateProduksi(produksi);
            
            return "Data produksi berhasil diupdate!\n" +
                   "Produk: " + produksi.getNamaProduk() + "\n" +
                   "Efisiensi Produksi: " + String.format("%.1f", produksi.getEfisiensiProduksi()) + "%\n" +
                   "Total Biaya Produksi: Rp " + String.format("%.0f", produksi.getTotalBiayaProduksi());

        } catch (ProductionException e) {
            return "Error: " + e.getMessage();
        }
    }

    public Produksi getProduksiById(int id) throws ProductionException {
        return produksiDAO.getProduksiById(id);
    }

    public String deleteProduksi(int id) {
        try {
            produksiDAO.deleteProduksi(id);
            return "Data berhasil dihapus";
        } catch (ProductionException e) {
            return "Error: " + e.getMessage();
        }
    }

    public void close() {
        if (produksiDAO != null) {
            produksiDAO.close();
        }
    }
}