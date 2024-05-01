package assignments.assignment3;

// Class representing a menu item in a restaurant
public class Menu {

    private String namaMakanan;
    private double harga;

    // Constructor to initialize a Menu object with a name and price
    public Menu(String namaMakanan, double harga){
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    // Getter method for the price of the menu item
    public double getHarga() {
        return harga;
    }

    // Getter method for the name of the menu item
    public String getNamaMakanan() {
        return namaMakanan;
    }
}