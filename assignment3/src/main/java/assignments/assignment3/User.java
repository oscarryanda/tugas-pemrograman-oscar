package assignments.assignment3;

import assignments.assignment3.payment.DepeFoodPaymentSystem;

import java.util.ArrayList;

public class User {

    private String nama;
    private String nomorTelepon;
    private String email;
    private ArrayList<Order> orderHistory;
    public String role;
    private DepeFoodPaymentSystem payment;
    private long saldo;

    private String lokasi;
    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem payment, long saldo) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        orderHistory = new ArrayList<>();
        this.payment = payment;
        this.saldo = saldo;
    }
    public String getEmail() {
        return email;
    }
    public String getNama() {
        return nama;
    }
    public String getLokasi() {
        return lokasi;
    }
    public String getNomorTelepon() {
        return nomorTelepon;
    }
    public void addOrderHistory(Order order){
        orderHistory.add(order);
    }
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
    public boolean isOrderBelongsToUser(String orderId) {
        for (Order order : orderHistory) {
            if (order.getOrderId().equals(orderId)) {
                return true;
            }
        }
        return false;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

    public void bayar(long price) {
        this.saldo -= price;
    }

    public String getRole() {
        return role;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return String.format("User dengan nama %s dan nomor telepon %s", nama, nomorTelepon);
    }

}