package assignments.assignment2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;


public class MainMenu {
    private static final Scanner in = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;
    private static User userLoggedIn;
    private static Restaurant currentRestaurant;

    public static void main(String[] args) {
        boolean firstRun = true;
        restoList = new ArrayList<>();
        boolean programRunning = true;
        while (programRunning) {
            if (firstRun) {
                printHeader();
                firstRun = false;
            }
            startMenu();
            int command = in.nextInt();
            in.nextLine();

            if (command == 1) {
                boolean userValid = false;
                String nama, noTelp;
                while (true) {
                    System.out.println("\nSilakan Login:");
                    System.out.print("Nama: ");
                    nama = in.nextLine();
                    System.out.print("Nomor Telepon: ");
                    noTelp = in.nextLine();
                    userLoggedIn = new User(nama, noTelp);
                    initUser();
                    if (getUser(nama, noTelp) != null) {
                        userLoggedIn = getUser(nama, noTelp);
                        userValid = true;
                    }
                    if (userValid) {
                        System.out.printf("Selamat Datang %s!", nama);
                        break;
                    }
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                }


                boolean isLoggedIn = true;

                if (userLoggedIn.getRole().equals("Customer")) {
                    while (isLoggedIn) {
                        menuCustomer();
                        int commandCust = in.nextInt();
                        in.nextLine();

                        switch (commandCust) {
                            case 1 -> handleBuatPesanan();
                            case 2 -> handleCetakBill();
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan();
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                } else {
                    while (isLoggedIn) {
                        menuAdmin();
                        int commandAdmin = in.nextInt();
                        in.nextLine();


                        switch (commandAdmin) {
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            } else if (command == 2) {
                programRunning = false;
            } else {
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon) {
        // TODO: Implementasi method untuk mendapat user dari userList
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    public static void handleBuatPesanan() {
        // TODO: Implementasi method untuk handle ketika customer membuat pesanan
        String restoName, date = null;
        boolean validResto = false;
        System.out.println("--------------Buat Pesanan--------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            restoName = in.nextLine();
            if (restoName.length() < 4) {
                System.out.println("Nama Restoran tidak valid!");
                continue;
            }
            for (Restaurant resto : restoList) {
                if (resto.getNama().equals(restoName)) {
                    validResto = true;
                    currentRestaurant = resto;
                }
            }
            if (!validResto) {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            date = in.nextLine();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {
                LocalDate.parse(date, dateFormat);

            } catch (DateTimeParseException e) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }
            String lokasi = userLoggedIn.getLokasi();
            int deliveryPrice = 0;
            if (lokasi.equalsIgnoreCase("p")) {
                deliveryPrice = 10000;
            } else if (lokasi.equalsIgnoreCase("u")) {
                deliveryPrice = 20000;
            } else if (lokasi.equalsIgnoreCase("t")) {
                deliveryPrice = 35000;
            } else if (lokasi.equalsIgnoreCase("s")) {
                deliveryPrice = 40000;
            } else if (lokasi.equalsIgnoreCase("b")) {
                deliveryPrice = 60000;
            }
            ArrayList<Menu> orderedFood = new ArrayList<>();


            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = in.nextInt();
            in.nextLine();
            String orderName;
            System.out.println("Order:");
            for (int i = 0; i < jumlahPesanan; i++) {
                boolean validOrder = false;
                while (true) {
                    orderName = in.nextLine();
                    for (int j = 0; j < currentRestaurant.getMenu().size(); j++) {
                        String currentNamaMakanan = currentRestaurant.getMenu().get(j).getNamaMakanan();
                        if (currentNamaMakanan.equalsIgnoreCase(orderName)) {
                            validOrder = true;
                            break; 
                        }
                    }
                    if (!validOrder) {
                        System.out.println("Mohon memesan menu yang tersedia di Restoran!");
                        continue;
                    }
                    double currentFoodPrice = currentRestaurant.getPrice(orderName);
                    orderedFood.add(new Menu(orderName, currentFoodPrice));
                    break;
                }
            }

            String orderID = OrderGenerator.generateOrderID(restoName, date, userLoggedIn.getNomorTelepon());
            userLoggedIn.addOrder(new Order(orderID, date, deliveryPrice, currentRestaurant, orderedFood));
            System.out.printf("Pesanan dengan ID %s diterima!", orderID);
            break;
        }
    }

    public static void handleCetakBill() {
        // TODO: Implementasi method untuk handle ketika customer ingin cetak bill
        System.out.println("--------------Cetak Bill--------------");
        String id;
        Order currentOrder = null;
        boolean validID = false;
        while (true) {
            System.out.print("Masukkan Order ID: ");
            id = in.nextLine();
            for (Order order : userLoggedIn.getOrderHistory()) {
                if (order.getOrderID().equals(id)) {
                    currentOrder = order;
                    validID = true;
                    break;
                }
            }
            if (validID) {
                System.out.print(OrderGenerator.generateBill(id, userLoggedIn.getLokasi(), currentRestaurant.getNama(),
                        currentOrder.orderStatus(), userLoggedIn.pesananUser(), currentOrder.getBiayaOngkosKirim() + userLoggedIn.totalHargaMakanan()));
                break;
            }
        }
    }

    public static void handleLihatMenu() {
        // TODO: Implementasi method untuk handle ketika customer ingin melihat menu
        System.out.println("--------------Lihat Menu--------------");
        boolean validResto = false;
        while (true) {
            System.out.print("Nama Restoran: ");
            String restoName = in.nextLine();
            for (Restaurant resto : restoList) {
                if (resto.getNama().equals(restoName)) {
                    validResto = true;
                    currentRestaurant = resto;
                }
            }
            if (!validResto) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }
            break;
        }
        System.out.print(currentRestaurant.showMenu());
    }


    public static void handleUpdateStatusPesanan() {
        // TODO: Implementasi method untuk handle ketika customer ingin update status pesanan
        System.out.println("--------------Update Status Pesanan--------------");
        String id;
        Order currentOrder = null;
        boolean validID = false;
        while (true) {
            System.out.print("Order ID: ");
            id = in.nextLine();
            for (Order order : userLoggedIn.getOrderHistory()) {
                if (order.getOrderID().equals(id)) {
                    currentOrder = order;
                    validID = true;
                    break;
                }
            }
            if (validID) {
                System.out.print("Status: ");
                String updatedStatus = in.nextLine();
                if (updatedStatus.equals("Selesai")) {
                    currentOrder.setOrderFinished(true);
                }
                System.out.printf("Status pesanan dengan ID %s berhasil diupdate!%n", id);
                break;
            } else {
                System.out.println("Order ID tidak dapat ditemukan.");
                continue;
            }
        }
    }


    public static void handleTambahRestoran(){
        // TODO: Implementasi method untuk handle ketika admin ingin tambah restoran
        System.out.println("--------------Tambah Restoran--------------");
        while (true) {
            System.out.print("Nama: ");
            boolean restoListed = false;
            String namaResto = in.nextLine();
            if (namaResto.length() < 4) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }
            for (Restaurant resto : restoList) {
                if (resto.getNama().equals(namaResto)) {
                    restoListed = true;
                    break;
                }
            }
            if (restoListed) {
                System.out.printf("Restoran dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!%n%n", namaResto);
                continue;
            }

            Restaurant currentRestaurant = new Restaurant(namaResto);

            System.out.print("Jumlah Makanan: ");
            int jumlahMakanan = in.nextInt();
            in.nextLine();
            String item, price;
            boolean invalidPrice = false;

            for (int i = 0; i < jumlahMakanan; i++) {
                String line = in.nextLine();
                item = "";
                price = "";

                boolean readingPrice = false;
                for (int j = 0; j < line.length(); j++) {
                    char currentChar = line.charAt(j);
                    if ((Character.isWhitespace(currentChar) || Character.isAlphabetic(currentChar)) && !readingPrice) {
                        item += currentChar;
                    } else if (Character.isDigit(currentChar)) {
                        readingPrice = true;
                        price += currentChar;
                    } else if (readingPrice && Character.isAlphabetic(currentChar) ) {
                        invalidPrice = true;

                    }
                }
                readingPrice = false;

                Menu menu = new Menu(item.trim(), Integer.parseInt(price));
                currentRestaurant.addMenu(menu);
            }

            if (invalidPrice) {
                System.out.println("Harga menu harus bilangan bulat!\n");
                continue;
            }

            restoList.add(currentRestaurant);
            System.out.printf("Restaurant %s Berhasil terdaftar.", namaResto);
            break; // Exit the loop after successfully registering the restaurant
        }


    }

    public static void handleHapusRestoran(){
        System.out.println("--------------Hapus Restoran--------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            boolean restoListed = false;
            String namaResto = in.nextLine();
            if (namaResto.length() < 4) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }
            for (Restaurant resto:restoList) {
                if(resto.getNama().equalsIgnoreCase(namaResto)) {
                    restoListed = true;
                    restoList.remove(resto);
                    break;
                }
            }
            if (!restoListed) {
                System.out.print("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.print("Restoran berhasil dihapus.");
            break;

        }

    }

    public static void initUser(){
       userList = new ArrayList<>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    public static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }
}
