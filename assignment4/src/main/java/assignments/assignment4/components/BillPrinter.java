package assignments.assignment4.components;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment3.Menu; // Ensure this import is present
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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    public Scene createBillPrinterForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Label titleLabel = new Label("Cetak Bill");
        titleLabel.setFont(new Font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        Label orderIdLabel = new Label("Order ID:");
        orderIdLabel.setTextFill(Color.DARKSLATEBLUE);
        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Masukkan Order ID");

        Button printButton = new Button("Cetak");
        printButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        printButton.setOnAction(e -> printBill(orderIdField.getText()));

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(mainApp.getScene(user.getRole().equals("Admin") ? "AdminMenu" : "CustomerMenu")));

        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(orderIdLabel, 0, 1);
        grid.add(orderIdField, 1, 1);
        grid.add(printButton, 1, 2);
        grid.add(backButton, 0, 2);

        return new Scene(grid, 600, 400);
    }

    public void printBill(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            showAlert("Error", "Order ID tidak boleh kosong", Alert.AlertType.ERROR);
            return;
        }
    
        Order order = DepeFood.getOrderOrNull(orderId);
        if (order == null) {
            showAlert("Error", "Order ID tidak ditemukan", Alert.AlertType.ERROR);
            return;
        }
    
        VBox billLayout = new VBox(10);
        billLayout.setAlignment(Pos.CENTER);
        billLayout.setPadding(new Insets(25, 25, 25, 25));
        billLayout.setStyle("-fx-background-color: #E6E6FA;");
    
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
    
        VBox pesananBox = new VBox(5);
        pesananBox.setAlignment(Pos.CENTER);
    
        Menu[] items = order.getItems();
        int[] quantities = order.getQuantities();
    
        for (int i = 0; i < items.length; i++) {
            Label itemLabel = new Label("- " + items[i].getNamaMakanan() + " x" + quantities[i] + " Rp " + items[i].getHarga() * quantities[i]);
            itemLabel.setTextFill(Color.DARKSLATEBLUE);
            pesananBox.getChildren().add(itemLabel);
        }
    
        Label deliveryCostLabel = new Label("Biaya Ongkos Kirim: Rp " + order.getOngkir());
        Label totalLabel = new Label("Total Biaya: Rp " + order.getTotalHarga());
    
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(mainApp.getScene(user.getRole().equals("Admin") ? "AdminMenu" : "CustomerMenu")));
    
        billLayout.getChildren().addAll(titleLabel, orderIdLabel, userNameLabel, phoneLabel, restaurantLabel, locationLabel, statusLabel, pesananLabel, pesananBox, deliveryCostLabel, totalLabel, backButton);
    
        Scene billScene = new Scene(billLayout, 400, 600);
        stage.setScene(billScene);
    }
    
    

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
