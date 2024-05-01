package assignments.assignment3;

import java.util.ArrayList;
import java.util.Scanner;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment3.systemCLI.AdminSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.systemCLI.UserSystemCLI;

// Main class for handling the main menu and user interaction
public class MainMenu {
    private final Scanner input;  // Scanner object for input operations
    private final LoginManager loginManager;  // Manages user login and role-based CLI
    public static ArrayList<Restaurant> restoList = new ArrayList<>();  // Static list of restaurants
    public static ArrayList<User> userList;  // Static list of users
    public static User userLoggedIn;  // Currently logged in user
    public static Restaurant currentResto;  // Currently selected restaurant

    // Constructor initializing MainMenu with a Scanner and a LoginManager
    public MainMenu(Scanner in, LoginManager loginManager) {
        this.input = in;
        this.loginManager = loginManager;
    }

    // Entry point of the application
    public static void main(String[] args) {
        initUser();
        MainMenu mainMenu = new MainMenu(new Scanner(System.in), new LoginManager(new AdminSystemCLI(), new CustomerSystemCLI()));
        mainMenu.run();
    }

    // Main loop handling menu selection and application flow
    public void run(){
        printHeader();
        boolean exit = false;
        while (!exit) {
            startMenu();
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1 -> login();
                case 2 -> exit = true;
                default -> System.out.println("Pilihan tidak valid, silakan coba lagi.");
            }
        }
        System.out.println("Terima kasih telah menggunakan DepeFood!");
        input.close();
    }

    // Handles user login
    private void login(){
    System.out.println("\nSilakan Login:");
    System.out.print("Nama: ");
    String nama = input.nextLine();
    System.out.print("Nomor Telepon: ");
    String noTelp = input.nextLine();

    for (User user : userList) {
        if (user.getNama().equalsIgnoreCase(nama) && user.getNomorTelepon().equals(noTelp)) {
            userLoggedIn = user;
            break;
        }
    }
    if (userLoggedIn == null) {
        System.out.println("Nama atau nomor telepon tidak ditemukan. Silakan coba lagi.");
        return;
    }


    UserSystemCLI systemCLI = loginManager.getSystem(userLoggedIn.getRole());
    systemCLI.run(userLoggedIn);
}

    // Prints the application header
    private static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    // Displays the main menu
    private static void startMenu(){
        userLoggedIn = null;
        System.out.println("\nSelamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Initializes the list of users
    public static void initUser(){
        userList = new ArrayList<User>();

        userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer", new DebitPayment(), 500000));
        userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer", new CreditCardPayment(), 2000000));
        userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer", new DebitPayment(), 750000));
        userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer", new CreditCardPayment(), 1800000));
        userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer", new DebitPayment(), 650000));

        userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
        userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin", new CreditCardPayment(), 0));
    }
}
