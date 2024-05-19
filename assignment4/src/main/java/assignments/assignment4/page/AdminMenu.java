package assignments.assignment4.page;

import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private MainApp mainApp;
    private ComboBox<String> restaurantComboBox;
    private ComboBox<String> viewRestaurantComboBox;
    private ListView<String> menuListView;
    private ListView<String> restaurantListView; // ListView to display the list of restaurants

    // Constructor
    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
        this.restaurantListView = new ListView<>(); // Initialize the ListView
    }

    // Method to create base menu
    @Override
    public Scene createBaseMenu() {
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Create title label
        Label titleLabel = new Label("Admin Login Menu");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create buttons
        Button addRestaurantButton = new Button("Tambah Restoran");
        addRestaurantButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button addMenuButton = new Button("Tambah Menu");
        addMenuButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button viewRestaurantsButton = new Button("Lihat Restoran");
        viewRestaurantsButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        // Add event handlers
        addRestaurantButton.setOnAction(e -> stage.setScene(addRestaurantScene));
        addMenuButton.setOnAction(e -> {
            updateRestaurantComboBox();
            stage.setScene(addMenuScene);
        });
        viewRestaurantsButton.setOnAction(e -> {
            updateViewRestaurantComboBox(); // Ensure the combo box is updated before viewing
            stage.setScene(viewRestaurantsScene);
        });

        logoutButton.setOnAction(e -> mainApp.logout());

        // Add components to grid
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(addRestaurantButton, 0, 1, 2, 1);
        grid.add(addMenuButton, 0, 2, 2, 1);
        grid.add(viewRestaurantsButton, 0, 3, 2, 1);
        grid.add(logoutButton, 0, 4, 2, 1);

        return new Scene(grid, 400, 600);
    }

    // Method to create add restaurant form
    private Scene createAddRestaurantForm() {
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Create title label
        Label titleLabel = new Label("Tambah Restoran");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create text field and buttons
        TextField restaurantNameField = new TextField();
        restaurantNameField.setPromptText("Nama Restoran");
        restaurantNameField.setStyle("-fx-control-inner-background: #D8BFD8;");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        // Add event handlers
        submitButton.setOnAction(e -> {
            handleTambahRestoran(restaurantNameField.getText());
            updateRestaurantListView(); // Update the list of restaurants
        });
        backButton.setOnAction(e -> stage.setScene(scene));

        restaurantNameField.setOnAction(e -> {
            handleTambahRestoran(restaurantNameField.getText());
            updateRestaurantListView(); // Update the list of restaurants
        });

        // Add components to grid
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(new Label("Nama Restoran:"), 0, 1);
        grid.add(restaurantNameField, 1, 1);
        grid.add(submitButton, 1, 2);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        grid.add(backButton, 0, 2);

        return new Scene(grid, 400, 600);
    }

    // Method to create add menu form
    private Scene createAddMenuForm() {
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Create title label
        Label titleLabel = new Label("Tambah Menu Restoran");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create combo box, text fields, and buttons
        restaurantComboBox = new ComboBox<>();
        updateRestaurantComboBox();
        restaurantComboBox.setPromptText("Pilih Restoran");
        restaurantComboBox.setStyle("-fx-background-color: #D8BFD8;");

        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Nama Menu");
        itemNameField.setStyle("-fx-control-inner-background: #D8BFD8;");

        TextField priceField = new TextField();
        priceField.setPromptText("Harga");
        priceField.setStyle("-fx-control-inner-background: #D8BFD8;");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");

        // Add event handlers
        submitButton.setOnAction(e -> {
            // Validate input
            if (restaurantComboBox.getValue() == null || itemNameField.getText().isEmpty() || priceField.getText().isEmpty()) {
                showAlert("Error", "Textfield tidak boleh kosong", Alert.AlertType.ERROR);
                return;
            }
            try {
                double price = Double.parseDouble(priceField.getText());
                handleTambahMenuRestoran(
                        DepeFood.getRestaurantByName(restaurantComboBox.getValue()),
                        itemNameField.getText(),
                        price
                );
                updateRestaurantListView(); // Ensure the list is updated after adding menu
            } catch (NumberFormatException ex) {
                showAlert("Error", "Harga harus berupa angka", Alert.AlertType.ERROR);
            }
        });
        backButton.setOnAction(e -> stage.setScene(scene));

        // Add components to grid
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(new Label("Pilih Restoran:"), 0, 1);
        grid.add(restaurantComboBox, 1, 1);
        grid.add(new Label("Nama Menu:"), 0, 2);
        grid.add(itemNameField, 1, 2);
        grid.add(new Label("Harga:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        grid.add(backButton, 0, 4);

        return new Scene(grid, 400, 600);
    }

    private Scene createViewRestaurantsForm() {
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Create title label
        Label titleLabel = new Label("Lihat Daftar Restoran");
        titleLabel.setFont(Font.font("Times New Roman", 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Create combo box and list view
        viewRestaurantComboBox = new ComboBox<>();
        updateViewRestaurantComboBox();
        viewRestaurantComboBox.setPromptText("Pilih Restoran");
        viewRestaurantComboBox.setStyle("-fx-background-color: #D8BFD8;");
        viewRestaurantComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateMenuListView(newVal));

        Label menuTitleLabel = new Label("Daftar Menu Restoran");
        menuTitleLabel.setFont(Font.font("Times New Roman", 20));
        menuTitleLabel.setTextFill(Color.DARKSLATEBLUE);

        menuListView = new ListView<>();
        menuListView.setStyle("-fx-control-inner-background: #D8BFD8;");

        // Create back button
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(scene));

        // Add components to grid
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(viewRestaurantComboBox, 0, 1, 2, 1);
        GridPane.setHalignment(viewRestaurantComboBox, HPos.CENTER);
        grid.add(menuTitleLabel, 0, 2, 2, 1);
        GridPane.setHalignment(menuTitleLabel, HPos.CENTER);
        grid.add(menuListView, 0, 3, 2, 1);
        grid.add(backButton, 0, 4, 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);

        return new Scene(grid, 400, 600);
    }

    // Method to handle adding a restaurant
    private void handleTambahRestoran(String nama) {
        String validName = DepeFood.getValidRestaurantName(nama);
        // Check if the name is valid
        if (!validName.startsWith("Restoran dengan nama") && !validName.startsWith("Nama Restoran tidak valid")) {
            // Add the restaurant
            DepeFood.handleTambahRestoran(nama);
            updateRestaurantComboBox();
            updateViewRestaurantComboBox();
            showAlert("Success", "Restoran berhasil ditambahkan", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", validName, Alert.AlertType.ERROR);
        }
    }

    // Method to handle adding a menu to a restaurant
    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, double price) {
        if (restaurant != null && itemName != null && !itemName.isEmpty() && price > 0) {
            // Add the menu
            DepeFood.handleTambahMenuRestoran(restaurant, itemName, price);
            showAlert("Success", "Menu berhasil ditambahkan", Alert.AlertType.INFORMATION);
            updateMenuListView(restaurant.getNama()); // Ensure the menu list is updated
        } else {
            showAlert("Error", "Input tidak valid", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        // Create alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #D8BFD8;"); // Set background color of alert
        alert.showAndWait();
    }

    // Method to update the restaurant combo box
    private void updateRestaurantComboBox() {
        // Get restaurant names and update the combo box
        List<String> restaurantNames = DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());
        restaurantComboBox.setItems(FXCollections.observableArrayList(restaurantNames));
    }

    // Method to update the view restaurant combo box
    private void updateViewRestaurantComboBox() {
        List<String> restaurantNames = DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());
        viewRestaurantComboBox.setItems(FXCollections.observableArrayList(restaurantNames));
    }

    // Method to update the menu list view
    private void updateMenuListView(String restaurantName) {
        Restaurant restaurant = DepeFood.getRestaurantByName(restaurantName);
        if (restaurant != null) {
            menuListView.setItems(FXCollections.observableArrayList(
                    restaurant.getMenu().stream()
                            .map(menu -> menu.getNamaMakanan() + " - Rp " + menu.getHarga())
                            .collect(Collectors.toList())
            ));
        } else {
            menuListView.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateRestaurantListView() {
        // Example implementation to update restaurantListView
        List<String> restaurantNames = DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());
        restaurantListView.setItems(FXCollections.observableArrayList(restaurantNames));
    }

    public Scene getScene() {
        return scene;
    }
}
