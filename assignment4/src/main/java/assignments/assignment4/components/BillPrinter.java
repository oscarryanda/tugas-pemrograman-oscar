package assignments.assignment4.components;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment3.Menu;
import assignments.assignment4.MainApp;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;

    // Constructor
    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    // Method to create bill printer form
    public Scene createBillPrinterForm() {
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Create title label
        Label titleLabel = new Label("Cetak Bill");
        titleLabel.setFont(new Font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create order ID label and text field
        Label orderIdLabel = new Label("Order ID:");
        orderIdLabel.setTextFill(Color.DARKSLATEBLUE);
        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Masukkan Order ID");

        // Create print and back buttons
        Button printButton = new Button("Cetak");
        printButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        printButton.setOnAction(e -> printBill(orderIdField.getText()));

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(mainApp.getScene(user.getRole().equals("Admin") ? "AdminMenu" : "CustomerMenu")));

        // Add components to grid
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(orderIdLabel, 0, 1);
        grid.add(orderIdField, 1, 1);
        grid.add(printButton, 1, 2);
        grid.add(backButton, 0, 2);

        return new Scene(grid, 600, 400);
    }

    // Method to print bill
    public void printBill(String orderId) {
        // Validate order ID
        if (orderId == null || orderId.isEmpty()) {
            showAlert("Error", "Order ID tidak boleh kosong", Alert.AlertType.ERROR);
            return;
        }

        // Retrieve order by ID
        Order order = DepeFood.getOrderOrNull(orderId);
        if (order == null) {
            showAlert("Error", "Order ID tidak ditemukan", Alert.AlertType.ERROR);
            return;
        }

        // Create layout for bill
        VBox billLayout = new VBox(10);
        billLayout.setAlignment(Pos.CENTER);
        billLayout.setPadding(new Insets(25, 25, 25, 25));
        billLayout.setStyle("-fx-background-color: #E6E6FA;");

        // Create bill labels
        Label titleLabel = new Label("Bill");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        Label userNameLabel = new Label("Nama: " + user.getNama());
        Label phoneLabel = new Label("Nomor Telepon: " + user.getNomorTelepon());
        Label restaurantLabel = new Label("Restoran: " + order.getRestaurant().getNama());
        Label locationLabel = new Label("Lokasi Pengiriman: " + user.getLokasi());
        Label statusLabel = new Label("Status Pengiriman: " + (order.getOrderFinished() ? "Finished" : "Not Finished"));
        Label pesananLabel = new Label("Pesanan:");
        pesananLabel.setFont(Font.font("Times New Roman", 18));
        pesananLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create layout for order items
        VBox pesananBox = new VBox(5);
        pesananBox.setAlignment(Pos.CENTER);

        // Retrieve order items and quantities
        Menu[] items = order.getItems();
        int[] quantities = order.getQuantities();

        // Add order items to layout
        for (int i = 0; i < items.length; i++) {
            Label itemLabel = new Label("- " + items[i].getNamaMakanan() + " x" + quantities[i] + " Rp " + items[i].getHarga() * quantities[i]);
            itemLabel.setTextFill(Color.DARKSLATEBLUE);
            pesananBox.getChildren().add(itemLabel);
        }

        // Create labels for delivery cost and total cost
        Label deliveryCostLabel = new Label("Biaya Ongkos Kirim: Rp " + order.getOngkir());
        Label totalLabel = new Label("Total Biaya: Rp " + order.getTotalHarga());

        // Create back button
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(mainApp.getScene(user.getRole().equals("Admin") ? "AdminMenu" : "CustomerMenu")));

        // Add components to bill layout
        billLayout.getChildren().addAll(titleLabel, orderIdLabel, userNameLabel, phoneLabel, restaurantLabel, locationLabel, statusLabel, pesananLabel, pesananBox, deliveryCostLabel, totalLabel, backButton);

        // Create and set bill scene
        Scene billScene = new Scene(billLayout, 400, 600);
        stage.setScene(billScene);
    }

    // Method to show alert
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #D8BFD8;");
        alert.showAndWait();
    }
}
