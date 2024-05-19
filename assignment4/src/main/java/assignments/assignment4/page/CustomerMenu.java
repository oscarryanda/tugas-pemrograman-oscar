package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.OrderGenerator;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;
import assignments.assignment4.components.form.LoginForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter;
    private ComboBox<String> restaurantComboBox;
    private VBox menuBox;
    private Map<Menu, Integer> orderItems;
    private MainApp mainApp;
    private User user;
    private Label saldoLabel;
    private Label userNameLabel;

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user);
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
        this.orderItems = new HashMap<>();
    }

    @Override
    public Scene createBaseMenu() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Label titleLabel = new Label("Customer Menu");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        Button addOrderButton = new Button("Tambah Pesanan");
        addOrderButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button printBillButton = new Button("Cetak Bill");
        printBillButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button payBillButton = new Button("Bayar Bill");
        payBillButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button cekSaldoButton = new Button("Cek Saldo");
        cekSaldoButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        addOrderButton.setOnAction(e -> stage.setScene(addOrderScene));
        printBillButton.setOnAction(e -> stage.setScene(printBillScene));
        payBillButton.setOnAction(e -> stage.setScene(payBillScene));
        cekSaldoButton.setOnAction(e -> stage.setScene(cekSaldoScene));
        logoutButton.setOnAction(e -> {
            mainApp.logout();
            ((LoginForm) mainApp.getScene("Login").getUserData()).clearInputs(); // Clear inputs on logout
        });

        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(addOrderButton, 0, 1, 2, 1);
        grid.add(printBillButton, 0, 2, 2, 1);
        grid.add(payBillButton, 0, 3, 2, 1);
        grid.add(cekSaldoButton, 0, 4, 2, 1);
        grid.add(logoutButton, 0, 5, 2, 1);

        return new Scene(grid, 400, 600);
    }

    private Scene createTambahPesananForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Label titleLabel = new Label("Tambah Pesanan");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        restaurantComboBox = new ComboBox<>();
        updateRestaurantComboBox();
        restaurantComboBox.setPromptText("Pilih Restoran");
        restaurantComboBox.setStyle("-fx-background-color: #D8BFD8;");
        restaurantComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateMenuBox(newVal));

        TextField orderDateField = new TextField();
        orderDateField.setPromptText("Tanggal Pemesanan (DD/MM/YYYY)");
        orderDateField.setStyle("-fx-control-inner-background: #D8BFD8;");

        menuBox = new VBox(10);
        menuBox.setAlignment(Pos.CENTER_LEFT);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        submitButton.setOnAction(e -> {
            handleBuatPesanan(
                restaurantComboBox.getValue(),
                orderDateField.getText(),
                new ArrayList<>(orderItems.keySet())
            );
        });
        backButton.setOnAction(e -> stage.setScene(scene));

        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(new Label("Pilih Restoran:"), 0, 1);
        grid.add(restaurantComboBox, 1, 1);
        grid.add(new Label("Tanggal Pemesanan:"), 0, 2);
        grid.add(orderDateField, 1, 2);
        grid.add(menuBox, 0, 3, 2, 1);
        grid.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        grid.add(backButton, 0, 4);

        return new Scene(grid, 400, 600);
    }

    private Scene createBillPrinter() {
        return billPrinter.createBillPrinterForm();
    }

    private Scene createBayarBillForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Label titleLabel = new Label("Bayar Bill");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        TextField orderIDField = new TextField();
        orderIDField.setPromptText("Order ID");
        orderIDField.setStyle("-fx-control-inner-background: #D8BFD8;");

        ComboBox<String> paymentOptions = new ComboBox<>(FXCollections.observableArrayList("Credit Card", "Debit"));
        paymentOptions.setPromptText("Pilih Metode Pembayaran");
        paymentOptions.setStyle("-fx-background-color: #D8BFD8;");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        submitButton.setOnAction(e -> handleBayarBill(orderIDField.getText(), paymentOptions.getValue()));
        backButton.setOnAction(e -> stage.setScene(scene));

        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(new Label("Order ID:"), 0, 1);
        grid.add(orderIDField, 1, 1);
        grid.add(new Label("Metode Pembayaran:"), 0, 2);
        grid.add(paymentOptions, 1, 2);
        grid.add(submitButton, 1, 3);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        grid.add(backButton, 0, 3);

        return new Scene(grid, 400, 600);
    }

    private Scene createCekSaldoScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Label titleLabel = new Label("Cek Saldo");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        userNameLabel = new Label("Nama User: " + user.getNama());
        userNameLabel.setFont(new Font("Arial", 16));
        userNameLabel.setTextFill(Color.DARKSLATEBLUE);

        saldoLabel = new Label("Saldo Anda: " + user.getSaldo());
        saldoLabel.setFont(new Font("Arial", 16));
        saldoLabel.setTextFill(Color.DARKSLATEBLUE);

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(scene));

        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(userNameLabel, 0, 1, 2, 1);
        GridPane.setHalignment(userNameLabel, HPos.CENTER);
        grid.add(saldoLabel, 0, 2, 2, 1);
        GridPane.setHalignment(saldoLabel, HPos.CENTER);
        grid.add(backButton, 0, 3, 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);

        return new Scene(grid, 400, 600);
    }

    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<Menu> menuItems) {
        if (namaRestoran == null || tanggalPemesanan == null || menuItems == null || menuItems.isEmpty()) {
            showAlert("Error", "Pesanan gagal", "Field tidak boleh kosong", Alert.AlertType.ERROR);
            return;
        }

        if (!OrderGenerator.validateDate(tanggalPemesanan)) {
            showAlert("Error", "Pesanan gagal", "Tanggal tidak valid", Alert.AlertType.ERROR);
            return;
        }

        Restaurant restaurant = DepeFood.getRestaurantByName(namaRestoran);
        if (restaurant == null) {
            showAlert("Error", "Pesanan gagal", "Restoran tidak ditemukan", Alert.AlertType.ERROR);
            return;
        }

        List<String> validMenuItems = menuItems.stream()
            .map(Menu::getNamaMakanan)
            .collect(Collectors.toList());

        List<Integer> quantities = menuItems.stream()
            .map(menu -> orderItems.get(menu))
            .collect(Collectors.toList());

        String orderID = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, validMenuItems, quantities);
        if (orderID != null) {
            showAlert("Success", "Pesanan ditambahkan", "Pesanan berhasil ditambahkan dengan Order ID: " + orderID, Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Pesanan gagal", "Pesanan gagal ditambahkan", Alert.AlertType.ERROR);
        }
    }

    private void updateRestaurantComboBox() {
        restaurantComboBox.setItems(FXCollections.observableArrayList(
            DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList())
        ));
    }

    private void updateMenuBox(String restaurantName) {
        Restaurant restaurant = DepeFood.getRestaurantByName(restaurantName);
        menuBox.getChildren().clear();
        orderItems.clear();
        
        if (restaurant != null) {
            for (Menu menu : restaurant.getMenu()) {
                HBox menuItemBox = new HBox(10);
                menuItemBox.setAlignment(Pos.CENTER_LEFT);

                Label menuLabel = new Label(menu.getNamaMakanan() + " - Rp " + menu.getHarga());
                Button addButton = new Button("+");
                Button subtractButton = new Button("-");
                Label quantityLabel = new Label("0");
                quantityLabel.setStyle("-fx-background-color: white; -fx-padding: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                
                addButton.setOnAction(e -> {
                    int quantity = orderItems.getOrDefault(menu, 0) + 1;
                    orderItems.put(menu, quantity);
                    quantityLabel.setText(String.valueOf(quantity));
                });

                subtractButton.setOnAction(e -> {
                    int quantity = orderItems.getOrDefault(menu, 0);
                    if (quantity > 0) {
                        quantity -= 1;
                        if (quantity == 0) {
                            orderItems.remove(menu);
                        } else {
                            orderItems.put(menu, quantity);
                        }
                        quantityLabel.setText(String.valueOf(quantity));
                    }
                });

                menuItemBox.getChildren().addAll(menuLabel, addButton, subtractButton, quantityLabel);
                menuBox.getChildren().add(menuItemBox);
            }
        }
    }

    private void handleBayarBill(String orderID, String paymentOption) {
        if (orderID.isEmpty() || paymentOption == null) {
            showAlert("Error", "Pembayaran gagal", "Field tidak boleh kosong", Alert.AlertType.ERROR);
            return;
        }
    
        Order order = DepeFood.getOrderOrNull(orderID);
        if (order == null) {
            showAlert("Error", "Pembayaran gagal", "Order ID tidak ditemukan", Alert.AlertType.ERROR);
            return;
        }
    
        if (order.getOrderFinished()) {
            showAlert("Error", "Pembayaran gagal", "Order sudah dibayar", Alert.AlertType.ERROR);
            return;
        }
    
        DepeFoodPaymentSystem paymentSystem = DepeFood.getUserLoggedIn().getPaymentSystem();
    
        boolean isCreditCard = paymentSystem instanceof CreditCardPayment;
        if ((isCreditCard && paymentOption.equals("Debit")) || (!isCreditCard && paymentOption.equals("Credit Card"))) {
            showAlert("Error", "Pembayaran gagal", "User belum memiliki metode pembayaran ini", Alert.AlertType.ERROR);
            return;
        }
    
        try {
            DepeFood.handleBayarBill(orderID, paymentOption);
            showAlert("Success", "Pembayaran berhasil", "Pembayaran berhasil dilakukan", Alert.AlertType.INFORMATION);
            // Update the saldoLabel to reflect the new saldo
            saldoLabel.setText("Saldo Anda: " + DepeFood.getUserLoggedIn().getSaldo());
        } catch (Exception e) {
            showAlert("Error", "Pembayaran gagal", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white;"); // Set background color of alert to purple and text color to white
        alert.showAndWait();
    }

    @Override
    protected void refresh() {
        super.refresh();
        updateRestaurantComboBox();
        // Refresh the saldoLabel when returning to the main menu
        saldoLabel.setText("Saldo Anda: " + DepeFood.getUserLoggedIn().getSaldo());
        userNameLabel.setText("Nama User: " + DepeFood.getUserLoggedIn().getNama());
    }

    public Scene getScene() {
        return scene;
    }
}
