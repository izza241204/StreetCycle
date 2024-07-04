package com.example.streetcycle.awal;

import com.example.streetcycle.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class desainController extends Helpers {

    @FXML
    private TextField txtUser4;

    @FXML
    private ImageView imageawal;

    @FXML
    private ImageView imageawal2;

    @FXML
    private PasswordField PwField;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCancel;

    @FXML
    private void initialize() {
        // Initialization code if necessary
    }

    private void handleCancel() {
        txtUser4.clear();
        PwField.clear();
    }

    private void navigateToDashboard(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBtnLogin(ActionEvent actionEvent) {
        String username = txtUser4.getText();
        String password = PwField.getText();

        if ("admin".equals(username) && "1234".equals(password)) {
            changePage(actionEvent, "DashboardAdmin");
        } else if ("manager".equals(username) && "bengkel2".equals(password)) {
            changePage(actionEvent, "DashboardManager");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("PASSWORD SALAH");
            alert.showAndWait();
        }
    }

    @FXML
    public void onBtnCancel(ActionEvent actionEvent) {
        txtUser4.clear();
        PwField.clear();
    }
}
