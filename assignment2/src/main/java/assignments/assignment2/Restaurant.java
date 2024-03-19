package assignments.assignment2;

import java.util.ArrayList;

public class Restaurant {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String nama;
    private ArrayList<Menu> menu;

    public Restaurant(String nama) {
        menu = new ArrayList<>();
        this.nama = nama;
    }
    // TODO: tambahkan methods yang diperlukan untuk class ini


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public double getPrice(String menuName) {
        for (Menu item: menu) {
            if (item.getNamaMakanan().equals(menuName)) {
                return item.getHarga();
            }
        } return 0;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    public void addMenu(Menu menu) {
        this.menu.add(menu);
    }



    public String showMenu() {
        sortMenuByPrice();
        String temp = "Menu: ";
        for (int i = 1; i <= menu.size(); i++) {
            temp += "\n" + i + ". " + menu.get(i - 1);
        }
        return temp;
    }

    public void sortMenuByPrice() {
        int n = menu.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (menu.get(j).getHarga() > menu.get(j + 1).getHarga() ||
                        (menu.get(j).getHarga() == menu.get(j + 1).getHarga() &&
                                menu.get(j).getNamaMakanan().compareTo(menu.get(j + 1).getNamaMakanan()) > 0)) {
                    Menu temp = menu.get(j);
                    menu.set(j, menu.get(j + 1));
                    menu.set(j + 1, temp);
                }
            }
        }
    }
}
