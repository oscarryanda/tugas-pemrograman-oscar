package assignments.assignment2;

import java.util.ArrayList;

public class User {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String nama;
    private String nomorTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory = new ArrayList<>();
    ArrayList<Double> orderedFoodPrice = new ArrayList<>();

        // TODO: buat constructor untuk class ini

    public User(String nama, String nomorTelepon, String email, String lokasi, String role) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
    }

    public User(String nama, String nomorTelepon) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public String pesananUser() {
        StringBuilder sb = new StringBuilder();
        for (Order order: orderHistory) {
            for (int i = 0; i < order.getItems().size(); i++) {
                sb.append("- " + order.getItems().get(i) + "\n");
            }
        } return sb.toString();
    }

    public double totalHargaMakanan() {
        double total = 0;
        for (Order order: orderHistory) {
            for (int i = 0; i < order.getItems().size(); i++) {
                total += order.getItems().get(i).getHarga();
            }
        } return total;
    }

    // TODO: tambahkan methods yang diperlukan untuk class ini
}
