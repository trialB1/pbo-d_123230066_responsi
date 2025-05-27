package model;

public class Produksi {
    private int id;
    private String namaProduk;
    private int jumlahUnit;
    private double jamKerja;
    private int jumlahTenagaKerja;
    private double biayaBahanBaku;
    private double biayaTenagaKerja;
    private double efisiensiProduksi;
    private double totalBiayaProduksi;

    public Produksi() {}

    public Produksi(String namaProduk, int jumlahUnit, double jamKerja, int jumlahTenagaKerja, double biayaBahanBaku) {
        this.namaProduk = namaProduk;
        this.jumlahUnit = jumlahUnit;
        this.jamKerja = jamKerja;
        this.jumlahTenagaKerja = jumlahTenagaKerja;
        this.biayaBahanBaku = biayaBahanBaku;
        calculateBiayaTenagaKerja();
        calculateEfisiensiProduksi();
        calculateTotalBiayaProduksi();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public int getJumlahUnit() { return jumlahUnit; }
    public void setJumlahUnit(int jumlahUnit) { this.jumlahUnit = jumlahUnit; }

    public double getJamKerja() { return jamKerja; }
    public void setJamKerja(double jamKerja) { this.jamKerja = jamKerja; }

    public int getJumlahTenagaKerja() { return jumlahTenagaKerja; }
    public void setJumlahTenagaKerja(int jumlahTenagaKerja) { this.jumlahTenagaKerja = jumlahTenagaKerja; }

    public double getBiayaBahanBaku() { return biayaBahanBaku; }
    public void setBiayaBahanBaku(double biayaBahanBaku) { this.biayaBahanBaku = biayaBahanBaku; }

    public double getBiayaTenagaKerja() { return biayaTenagaKerja; }
    public void setBiayaTenagaKerja(double biayaTenagaKerja) { this.biayaTenagaKerja = biayaTenagaKerja; }

    public double getEfisiensiProduksi() { return efisiensiProduksi; }
    public void setEfisiensiProduksi(double efisiensiProduksi) { this.efisiensiProduksi = efisiensiProduksi; }

    public double getTotalBiayaProduksi() { return totalBiayaProduksi; }
    public void setTotalBiayaProduksi(double totalBiayaProduksi) { this.totalBiayaProduksi = totalBiayaProduksi; }

    public void calculateBiayaTenagaKerja() {
        this.biayaTenagaKerja = jamKerja * jumlahTenagaKerja * 15000;
    }

    public void calculateEfisiensiProduksi() {
        if (jamKerja != 0 && jumlahTenagaKerja != 0) {
            this.efisiensiProduksi = ((double) jumlahUnit / (jamKerja * jumlahTenagaKerja)) * 100;
        }
    }

    public void calculateTotalBiayaProduksi() {
        this.totalBiayaProduksi = biayaBahanBaku + biayaTenagaKerja;
    }

    public boolean isEfficiencyValid() {
        return efisiensiProduksi >= 20.0;
    }

    @Override
    public String toString() {
        return String.format("Produksi[ID=%d, Produk=%s, Unit=%d, Jam=%.2f, Tenaga=%d, Efisiensi=%.2f%%, Total=%.2f]",
                id, namaProduk, jumlahUnit, jamKerja, jumlahTenagaKerja, efisiensiProduksi, totalBiayaProduksi);
    }
}