package assignments.assignment4.page;

import assignments.assignment3.DepeFood;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public abstract class MemberMenu {
    // Initialize scene
    private Scene scene;

    // Method to be overridden by subclasses
    abstract protected Scene createBaseMenu();

    // Show alert
    protected void showAlert(String title, String header, String content, Alert.AlertType c) {
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Getters and setters
    public Scene getScene() {
        return this.scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    // Refresh data
    protected void refresh() {
        DepeFood.initUser();
        DepeFood.getRestoList().clear();
        DepeFood.getRestoList().addAll(DepeFood.getRestoList());
        System.out.println("Data refreshed!");
    }
}
