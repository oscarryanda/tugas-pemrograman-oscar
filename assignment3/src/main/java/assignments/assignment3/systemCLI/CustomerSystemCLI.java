package assignments.assignment3.systemCLI;

import assignments.assignment3.*;

import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;

import java.text.*;
import java.util.*;

import static assignments.assignment3.MainMenu.*;
import static assignments.assignment3.payment.CreditCardPayment.countTransactionFee;


//TODO: Extends abstract class yang diberikan
public class CustomerSystemCLI extends UserSystemCLI {

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    @Override
    boolean handleMenu(int choice){
        switch(choice){
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }



    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    @Override
    void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void handleBuatPesanan(){
        System.out.println("--------------Buat Pesanan----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String restaurantName = input.nextLine().trim();
            currentResto = getRestaurantByName(restaurantName);
            if(currentResto == null){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            String tanggalPemesanan = input.nextLine().trim();
            if(!OrderGenerator.validateDate(tanggalPemesanan)){
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)");
                System.out.println();
                continue;
            }
            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = Integer.parseInt(input.nextLine().trim());
            System.out.println("Order: ");
            List<String> listMenuPesananRequest = new ArrayList<>();
            for(int i=0; i < jumlahPesanan; i++){
                listMenuPesananRequest.add(input.nextLine().trim());
            }
            if(! validateRequestPesanan(currentResto, listMenuPesananRequest)){
                System.out.println("Mohon memesan menu yang tersedia di Restoran!");
                continue;
            };
            Order order = new Order(
                    OrderGenerator.generateOrderID(restaurantName.toUpperCase(), tanggalPemesanan, userLoggedIn.getNomorTelepon()),
                    tanggalPemesanan,
                    OrderGenerator.calculateDeliveryCost(userLoggedIn.getLokasi()),
                    currentResto,
                    getMenuRequest(currentResto, listMenuPesananRequest));
            System.out.printf("Pesanan dengan ID %s diterima!", order.getOrderId());
            userLoggedIn.addOrderHistory(order);
            return;
        }
    }

    public static boolean validateRequestPesanan(Restaurant restaurant, List<String> listMenuPesananRequest){
        return listMenuPesananRequest.stream().allMatch(pesanan ->
                restaurant.getMenu().stream().anyMatch(menu -> menu.getNamaMakanan().equals(pesanan))
        );
    }

    public static Menu[] getMenuRequest(Restaurant restaurant, List<String> listMenuPesananRequest){
        Menu[] menu = new Menu[listMenuPesananRequest.size()];
        for(int i=0;i<menu.length;i++){
            for(Menu existMenu : restaurant.getMenu()){
                if(existMenu.getNamaMakanan().equals(listMenuPesananRequest.get(i))){
                    menu[i] = existMenu;
                }
            }
        }
        return menu;
    }

    public static String getMenuPesananOutput(Order order){
        StringBuilder pesananBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\u0000');
        decimalFormat.setDecimalFormatSymbols(symbols);
        for (Menu menu : order.getSortedMenu()) {
            pesananBuilder.append("- ").append(menu.getNamaMakanan()).append(" ").append(decimalFormat.format(menu.getHarga())).append("\n");
        }
        if (pesananBuilder.length() > 0) {
            pesananBuilder.deleteCharAt(pesananBuilder.length() - 1);
        }
        return pesananBuilder.toString();
    }

    public static String outputBillPesanan(Order order) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return String.format("Bill:%n" +
                        "Order ID: %s%n" +
                        "Tanggal Pemesanan: %s%n" +
                        "Restaurant: %s%n" +
                        "Lokasi Pengiriman: %s%n" +
                        "Status Pengiriman: %s%n"+
                        "Pesanan:%n%s%n"+
                        "Biaya Ongkos Kirim: Rp %s%n"+
                        "Total Biaya: Rp %s%n",
                order.getOrderId(),
                order.getTanggal(),
                currentResto.getNama(),
                userLoggedIn.getLokasi(),
                !order.getOrderFinished()? "Not Finished":"Finished",
                getMenuPesananOutput(order),
                decimalFormat.format(order.getOngkir()),
                decimalFormat.format(order.getTotalHarga())
        );
    }



    public static Restaurant getRestaurantByName(String name){
        Optional<Restaurant> restaurantMatched = restoList.stream().filter(restoran -> restoran.getNama().toLowerCase().equals(name.toLowerCase())).findFirst();
        if(restaurantMatched.isPresent()){
            return restaurantMatched.get();
        }
        return null;
    }

    public static void handleCetakBill(){
        System.out.println("--------------Cetak Bill----------------");
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine().trim();
            Order order = getOrderOrNull(orderId);
            if(order == null){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
            System.out.println("");
            System.out.print(outputBillPesanan(order));
            return;
        }

    }

    public static Order getOrderOrNull(String orderId) {
        for (User user : userList) {
            for (Order order : user.getOrderHistory()) {
                if (order.getOrderId().equals(orderId)) {
                    return order;
                }
            }
        }
        return null;
    }


    public static void handleLihatMenu(){
        System.out.println("--------------Lihat Menu----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String restaurantName = input.nextLine().trim();
            Restaurant restaurant = getRestaurantByName(restaurantName);
            if(restaurant == null){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.print(restaurant.printMenu());
            return;
        }
    }

    public static void handleUpdateStatusPesanan(Order order){
        order.setOrderFinished(true);
    }

    void handleBayarBill() {
        System.out.println("--------------Bayar Bill----------------");
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine().trim();
            Order order = getOrderOrNull(orderId);
            if (order == null) {
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }

            if (order.getOrderFinished()) {
                System.out.println("Pesanan dengan ID ini sudah lunas!");
                continue;
            }

            System.out.println("");
            System.out.print(outputBillPesanan(order));
            System.out.println("Opsi Pembayaran:");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit");
            System.out.print("Pilihan Metode Pembayaran: ");
            String pilihanPembayaran = input.nextLine().trim();
            DepeFoodPaymentSystem paymentMethod;

            switch (pilihanPembayaran) {
                case "1":
                    paymentMethod = new CreditCardPayment();
                    break;
                case "2":
                    paymentMethod = new DebitPayment();
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    continue;
            }
            if (!userLoggedIn.hasPaymentMethod(Integer.parseInt(pilihanPembayaran))) {
                System.out.println("User belum memiliki metode pembayaran ini!");
                continue;
            }

            long paymentResult = paymentMethod.processPayment(order.getTotalHarga());
            if (paymentResult > 0) {
                handleUpdateStatusPesanan(order);
                if (paymentMethod instanceof DebitPayment) {
                    userLoggedIn.bayar(paymentResult);
                    currentResto.transaksiResto(paymentResult);
                    System.out.println("\nBerhasil Membayar Bill sebesar Rp " + paymentResult);
                    return;
                } else if (paymentMethod instanceof CreditCardPayment) {
                    userLoggedIn.bayar(order.getTotalHarga() + paymentResult);
                    currentResto.transaksiResto(order.getTotalHarga());
                    System.out.println("\nBerhasil Membayar Bill sebesar Rp " +
                            order.getTotalHarga() + " dengan biaya transaksi sebesar Rp " + paymentResult);
                    return;
                }

            } else if (paymentResult == 0) {
                System.out.println("Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain");
            }
            else {
                System.out.println("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain");
            }
        }
    }


    void handleCekSaldo(){
        System.out.println("\nSisa saldo sebesar Rp " + userLoggedIn.getSaldo());
    }
}

