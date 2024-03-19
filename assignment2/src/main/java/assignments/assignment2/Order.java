package assignments.assignment2;

import java.util.ArrayList;

public class Order {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String orderID;
    private String tanggalPemesanan;
    private int biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished;

    public Order(String orderID, String tanggalPemesanan, int biayaOngkosKirim, Restaurant restaurant, ArrayList<Menu> items) {
        this.orderID = orderID;
        this.tanggalPemesanan = tanggalPemesanan;
        this.biayaOngkosKirim = biayaOngkosKirim;
        this.restaurant = restaurant;
        this.items = items;
    }

    public double countPrice(ArrayList<Menu> items) {
        double totalPrice = 0;
        for (Menu item : items) {
            totalPrice += item.getHarga();
        }
        return totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTanggalPemesanan() {
        return tanggalPemesanan;
    }

    public void setTanggalPemesanan(String tanggalPemesanan) {
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public int getBiayaOngkosKirim() {
        return biayaOngkosKirim;
    }

    public void setBiayaOngkosKirim(int biayaOngkosKirim) {
        this.biayaOngkosKirim = biayaOngkosKirim;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<Menu> getItems() {
        return items;
    }

    public void setItems(ArrayList<Menu> items) {
        this.items = items;
    }

    public String orderStatus() {
        if (orderFinished) {
            return "Finished";
        } else {
            return "Not Finished";
        }
    }

    public boolean isOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(boolean orderFinished) {
        this.orderFinished = orderFinished;
    }

    // TODO: tambahkan methods yang diperlukan untuk class ini
}
