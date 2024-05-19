package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;

public class CustomerSystemCLI extends UserSystemCLI {
    private Scanner input = new Scanner(System.in);

    @Override
    void displayMenu() {
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Lihat Pesanan");
        System.out.println("0. Logout");
    }

    @Override
    boolean handleMenu(int command) {
        switch (command) {
            case 1:
                buatPesanan();
                break;
            case 2:
                lihatPesanan();
                break;
            case 0:
                return false;
            default:
                System.out.println("Command tidak dikenal!");
        }
        return true;
    }

    public void buatPesanan() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama restoran: ");
        String namaRestoran = scanner.nextLine();
        System.out.print("Masukkan tanggal pemesanan (DD/MM/YYYY): ");
        String tanggalPemesanan = scanner.nextLine();
    
        List<String> listMenuPesananRequest = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        while (true) {
            System.out.print("Masukkan nama menu: ");
            String menu = scanner.nextLine();
            listMenuPesananRequest.add(menu);
    
            System.out.print("Masukkan jumlah: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            quantities.add(quantity);
    
            System.out.print("Apakah ingin menambah menu lagi? (y/n): ");
            String more = scanner.nextLine();
            if (more.equalsIgnoreCase("n")) {
                break;
            }
        }
    
        String orderId = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, listMenuPesananRequest, quantities);
        if (orderId != null) {
            System.out.println("Pesanan berhasil dibuat dengan ID: " + orderId);
        }
    }
    

    private void lihatPesanan() {
        System.out.print("Masukkan Order ID: ");
        String orderId = input.nextLine();
        DepeFood.printBill(orderId);
    }
}
