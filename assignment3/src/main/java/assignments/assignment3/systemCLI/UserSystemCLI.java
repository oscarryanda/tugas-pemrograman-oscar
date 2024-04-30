package assignments.assignment3.systemCLI;

import java.util.Scanner;
import java.util.ArrayList;
import assignments.assignment3.*;

public abstract class UserSystemCLI {
    public static final Scanner input = new Scanner(System.in);
    public static ArrayList<Restaurant> restoList = new ArrayList<>();
    public static ArrayList<User> userList = new ArrayList<>();
    public static User userLoggedIn; 

    public void run(User user) { 
        this.userLoggedIn = user; 
        System.out.print("Selamat datang " + userLoggedIn.getNama() + "!");
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }

    abstract void displayMenu();
    abstract boolean handleMenu(int command);
}
