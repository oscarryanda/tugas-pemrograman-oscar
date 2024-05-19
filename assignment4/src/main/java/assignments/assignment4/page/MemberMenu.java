package assignments.assignment4.page;

import assignments.assignment3.DepeFood;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public abstract class MemberMenu {
    private Scene scene;

    abstract protected Scene createBaseMenu();

    protected void showAlert(String title, String header, String content, Alert.AlertType c) {
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Scene getScene() {
        return this.scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    protected void refresh() {
        DepeFood.initUser();
        DepeFood.getRestoList().clear();
        DepeFood.getRestoList().addAll(DepeFood.getRestoList());
        System.out.println("Data refreshed!");
    }
}
