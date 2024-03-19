package assignments.assignment2;

public class Menu {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String namaMakanan;
    private double harga;

    public Menu(String namaMakanan, double harga) {
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    // TODO: tambahkan methods yang diperlukan untuk class ini

    @Override
    public String toString() {
        return namaMakanan + " " + String.format("%.0f",harga);
    }
}
