package assignments.assignment3.systemCLI;

import java.util.Scanner;
import java.util.ArrayList;
import assignments.assignment3.*;

public abstract class UserSystemCLI {
    // Scanner for user input
    public static final Scanner input = new Scanner(System.in);

    // Run CLI for user based on user type
    public void run(User user) { 

        System.out.print("Selamat datang " + user.getNama() + "!");
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }

    // Abstract methods to be overriden on Customer and Admin CLI
    abstract void displayMenu();
    abstract boolean handleMenu(int command);
}
