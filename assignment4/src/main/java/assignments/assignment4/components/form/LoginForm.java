package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp;
    private TextField nameInput;
    private TextField phoneInput;

    public LoginForm(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    private Scene createLoginForm() {
        // Create the login form layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        // Add title
        Label titleLabel = new Label("Depefood Delivery");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);
        grid.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);

        // Add input fields
        Label nameLabel = new Label("Nama:");
        nameLabel.setFont(new Font("Arial", 16));
        nameLabel.setTextFill(Color.DARKSLATEBLUE);
        grid.add(nameLabel, 0, 1);

        // Add name input
        nameInput = new TextField();
        nameInput.setPromptText("Masukkan nama Anda");
        grid.add(nameInput, 1, 1);

        // Add phone input
        Label phoneLabel = new Label("Nomor Telepon:");
        phoneLabel.setFont(new Font("Arial", 16));
        phoneLabel.setTextFill(Color.DARKSLATEBLUE);
        grid.add(phoneLabel, 0, 2);
        phoneInput = new TextField();
        phoneInput.setPromptText("Masukkan nomor telepon Anda");
        grid.add(phoneInput, 1, 2);

        // Add phone pad
        VBox phonePad = createPhonePad();
        grid.add(phonePad, 1, 3);

        // Add login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
        grid.add(loginButton, 1, 5);
        GridPane.setHalignment(loginButton, HPos.RIGHT);

        // Add event handler for login button
        loginButton.setOnAction(e -> handleLogin());

        // Add event handler for Enter key
        nameInput.setOnAction(e -> phoneInput.requestFocus());
        phoneInput.setOnAction(e -> handleLogin());

        return new Scene(grid, 400, 600);
    }

    // Create the phone pad
    private VBox createPhonePad() {
        VBox phonePad = new VBox(10);
        phonePad.setAlignment(Pos.CENTER);

        String[][] padLayout = {
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"},
                {"0", "Delete"}
        };

        // Add buttons to the phone pad
        for (String[] row : padLayout) {
            HBox rowBox = new HBox(10);
            rowBox.setAlignment(Pos.CENTER);
            for (String key : row) {
                Button button = new Button(key);
                if (key.equals("Delete")) {
                    button.setPrefSize(110, 50); // Adjust size of delete button to cover 2 size
                    button.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
                    button.setOnAction(e -> phoneInput.setText(""));
                } else {
                    // Adjust size of other buttons
                    button.setPrefSize(50, 50);
                    button.setStyle("-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 16px;");
                    button.setOnAction(e -> phoneInput.setText(phoneInput.getText() + key));
                }
                rowBox.getChildren().add(button);
            }
            phonePad.getChildren().add(rowBox);
        }

        return phonePad;
    }

    // Event handler for login button
    private void handleLogin() {
        String name = nameInput.getText();
        String phone = phoneInput.getText();

        // Validate input
        User user = DepeFood.getUser(name, phone);
        // Login user validation
        if (user != null) {
            mainApp.setUser(user);
            DepeFood.userLoggedIn = user;
            // Check if user is admin or customer
            if (user.getRole().equals("Admin")) {
                AdminMenu adminMenu = new AdminMenu(stage, mainApp, user);
                mainApp.addScene("AdminMenu", adminMenu.getScene());
                mainApp.setScene(adminMenu.getScene());
            } else {
                CustomerMenu customerMenu = new CustomerMenu(stage, mainApp, user);
                mainApp.addScene("CustomerMenu", customerMenu.getScene());
                mainApp.setScene(customerMenu.getScene());
            }
        } else {
            showAlert("Login Failed", "Akun tidak ditemukan", "User not found. Please check your name and phone number.");
        }
    }

    // Show alert dialog
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #D8BFD8;"); // Set background color of alert

        alert.showAndWait();
    }

    // Get scene
    public Scene getScene() {
        Scene loginScene = createLoginForm();
        loginScene.setUserData(this);
        return loginScene;
    }

    // Clear inputs for when logout
    public void clearInputs() {
        nameInput.clear();
        phoneInput.clear();
    }
}
