package com.securepass.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.securepass.storage.DBConnection;

public class LoginWindow extends Application {

    private final String MASTER_PASSWORD = "admin123"; // You can hash this and store securely

    @Override
    public void start(Stage primaryStage) {
        DBConnection.initializeDatabase();

        Label label = new Label("Enter Master Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        VBox layout = new VBox(10, label, passwordField, loginButton, error);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 300, 180);

        loginButton.setOnAction(e -> {
            if (passwordField.getText().equals(MASTER_PASSWORD)) {
                DashboardWindow dashboard = new DashboardWindow();
                try {
                    dashboard.start(new Stage());
                    primaryStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                error.setText("Incorrect password!");
            }
        });

        primaryStage.setTitle("Password Manager - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
