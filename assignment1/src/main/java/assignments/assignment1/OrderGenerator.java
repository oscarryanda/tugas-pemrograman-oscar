package assignments.assignment1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class OrderGenerator {
    // Initialize Scanner to read user input
    private static final Scanner in = new Scanner(System.in);
    // Array to store code 39 that's going to be used for checksum
    private static final char[] CODE_39_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    // Flag to only display logo on the first run
    private static boolean isFirstRun = true;

    /**
     * Convert a character to its Code 39 value.
     * @param ch Character to convert
     * @return Code 39 value of the character
     */
    private static int charToCode39Value(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        } else if (ch >= 'A' && ch <= 'Z') {
            return ch - 'A' + 10;
        }
        return 0;
    }


    /**
     * Validate an order ID.
     * @param orderID The order ID to validate.
     * @return true if the order ID is valid, false otherwise.
     */
    public static boolean validateOrderID(String orderID) {
        // Basic length check
        if (orderID.length() < 16) {
            System.out.println("Order ID minimal 16 karakter");
            return false;
        }
        
        // Extract parts of the order ID
        String namePart = orderID.substring(0, 4);
        String datePart = orderID.substring(4, 12);
        String checksumPart = orderID.substring(orderID.length() - 2);

        if (!namePart.equals(namePart.toUpperCase()) || !namePart.matches("[A-Z]+")) {
            System.out.println("Silahkan masukkan order ID yang valid!");
            return false;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("ddMMyyyy");
        try {
            LocalDate.parse(datePart, dateFormat);
        } catch (DateTimeParseException e) {
            System.out.println("Silahkan masukkan order ID yang valid!");
            return false; 
        }

        // Recalculate checksum to validate
        String tempID = orderID.substring(0, orderID.length() - 2); // Exclude checksum for calculation
        int checksum1 = 0, checksum2 = 0;
        for (int i = 0; i < tempID.length(); i++) {
            int value = charToCode39Value(tempID.charAt(i));
            if (i % 2 == 0) {
                checksum1 += value;
            } else {
                checksum2 += value;
            }
        }
        char expectedChecksumChar1 = CODE_39_CHARS[checksum1 % 36];
        char expectedChecksumChar2 = CODE_39_CHARS[checksum2 % 36];
        String expectedChecksum = "" + expectedChecksumChar1 + expectedChecksumChar2;

        // Compare calculated checksum with the one in the order ID
        if (!checksumPart.equals(expectedChecksum)) {
            System.out.println("Silahkan masukkan order ID yang valid!");
            return false; // Checksum does not match
        }

        return true;
    }

    /**
     * Display the main menu.
     */
    public static void showMenu() {
        if (isFirstRun) {
            System.out.println(">>=======================================<<");
            System.out.println("|| ___                 ___             _ ||");
            System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
            System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
            System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
            System.out.println("||          |_|                          ||");
            System.out.println(">>=======================================<<\n");
            isFirstRun = false;
        }
        
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
    }

    /**
     * Generate an order ID from restaurant name, order date, and phone number.
     * @param namaRestoran Name of the restaurant
     * @param tanggalOrder Date of the order
     * @param noTelepon Phone number
     * @return Generated Order ID
     */
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        String[] dateSplit = tanggalOrder.split("/");
        StringBuilder mergedDate = new StringBuilder();
        for (String dates : dateSplit) {
            mergedDate.append(dates);
        }
        // Sum all the digits from the phone number and modulo by 100
        int numberSum = 0;
        for (int i = 0; i < noTelepon.length(); i++) {
            numberSum += noTelepon.charAt(i) - '0';
        }
        numberSum %= 100;
        StringBuilder phoneNumberCode = new StringBuilder(String.format("%02d", numberSum));
        String tempID = namaRestoran.toUpperCase().substring(0, 4) + mergedDate + phoneNumberCode;
        int checksum1 = 0, checksum2 = 0;
        // Count checksum based on the rules
        for (int i = 0; i < tempID.length(); i++) {
            int value = charToCode39Value(tempID.charAt(i));
            if (i % 2 == 0) {
                checksum1 += value;
            } else {
                checksum2 += value;
            }
        }
        char checksumChar1 = CODE_39_CHARS[checksum1 % 36];
        char checksumChar2 = CODE_39_CHARS[checksum2 % 36];
        return tempID + checksumChar1 + checksumChar2;
    }

    /**
     * Generate a bill from order ID and lokasi.
     * @param OrderID Order ID
     * @param lokasi lokasi
     * @return Generated bill
     */
    public static String generateBill(String OrderID, String lokasi) {
        // Get the date when the user ordered and format it
        String datePart = OrderID.substring(4, 12); 
        DateTimeFormatter idDateFormat = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate orderDate = LocalDate.parse(datePart, idDateFormat);
        DateTimeFormatter displayDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedOrderDate = orderDate.format(displayDateFormat);
        
        // Set price based on location
        StringBuilder price = new StringBuilder("Rp ");
        if (lokasi.equalsIgnoreCase("p")) {
            price.append("10.000");
        } else if (lokasi.equalsIgnoreCase("u")) {
            price.append("20.000");
        } else if (lokasi.equalsIgnoreCase("t")) {
            price.append("35.000");
        } else if (lokasi.equalsIgnoreCase("s")) {
            price.append("40.000");
        } else if (lokasi.equalsIgnoreCase("b")) {
            price.append("60.000");
        } 

        return String.format("Bill:%nOrder ID: %s%nTanggal Pemesanan: %s%nLokasi Pengiriman: %s%nBiaya Ongkos Kirim: %s%n",
                OrderID, formattedOrderDate, lokasi.toUpperCase(), price);
    }

    public static void main(String[] args) {
        while (true) {
            showMenu();
            System.out.println("-------------------------------------------");
            System.out.print("Pilihan menu: ");
            String menu = in.nextLine();
            switch (menu) {
                case "1":
                    String restoName, date, phoneNumber;
                    while (true) {
                        // Validate restoName, date and phoneNumber
                        System.out.print("\nNama Restoran: ");
                        restoName = in.nextLine();
                        if (restoName.length() < 4) {
                            System.out.println("Nama Restoran tidak valid!");
                            continue;
                        }
                        System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
                        date = in.nextLine();
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        try {
                            LocalDate.parse(date, dateFormat);

                        } catch (DateTimeParseException e) {
                            System.out.println("Tanggal Pemesanan dalam format DD/MM/YYYY!");
                            continue;
                        }
                        System.out.print("No. Telpon:  ");
                        boolean wrongInput = true;
                        phoneNumber = in.nextLine();
                        if (!phoneNumber.matches("[0-9]+")) {
                            System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.");
                            continue;
                        } else {
                            wrongInput = false;
                        }
                        if (wrongInput) {
                            continue;
                        }


                        System.out.println("Order ID " + generateOrderID(restoName, date, phoneNumber) + " diterima!");
                        break;
                    }
                    break;
                case "2":
                String id;
                // Validate order id and location
                while(true) {
                    System.out.print("ORDER ID: ");
                    id = in.nextLine();
                    if (!validateOrderID(id)) {
                        System.out.println();
                        continue;
                    }
                    String lokasi;
                    // Store valid locations using array
                    String[] validLocations = {"p", "u", "t", "s", "b"}; 
                    boolean isValidLocation = false;

                    System.out.print("Lokasi Pengiriman: ");
                    lokasi = in.nextLine().toLowerCase();
                    
                    for (String validLoc : validLocations) {
                        if (lokasi.equals(validLoc)) {
                            isValidLocation = true;
                        }
                    }
                    
                    if (isValidLocation) {
                        // Print bill
                        System.out.println("\n" + generateBill(id, lokasi)); 
                        break; 
                    } else {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!\n");
                    }
                }
                break;
            case "3":
            // Exit program
                System.out.println("Terima kasih telah menggunakan DepeFood!");
                return; 
            default:
                System.out.println("Pilihan tidak valid, silakan coba lagi.");
                break;
        }
        System.out.println("------------------------------");
    }
}
}
                    
                   