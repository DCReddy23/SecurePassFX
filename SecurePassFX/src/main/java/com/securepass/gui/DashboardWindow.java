package com.securepass.gui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import com.securepass.model.Credential;
import com.securepass.security.EncryptionUtil;
import com.securepass.security.PasswordGenerator;
import com.securepass.storage.CredentialDAO;
import com.securepass.util.ExportImportUtil;

import java.io.File;
import java.util.List;

public class DashboardWindow extends Application {
    private TableView<Credential> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label heading = new Label("Saved Credentials");
        heading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableColumn<Credential, String> siteCol = new TableColumn<>("Website");
        siteCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWebsite()));

        TableColumn<Credential, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));

        TableColumn<Credential, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                EncryptionUtil.decrypt(c.getValue().getPassword())
        ));

        table.getColumns().addAll(siteCol, userCol, passCol);
        refreshTable();

        // Add form
        TextField siteField = new TextField();
        siteField.setPromptText("Website");

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button generateBtn = new Button("🔑 Generate");
        generateBtn.setOnAction(e -> passField.setText(PasswordGenerator.generate(12)));

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            String encrypted = EncryptionUtil.encrypt(passField.getText());
            Credential cred = new Credential(siteField.getText(), userField.getText(), encrypted);
            CredentialDAO.addCredential(cred);
            refreshTable();
            siteField.clear();
            userField.clear();
            passField.clear();
        });

        HBox form = new HBox(10, siteField, userField, passField, generateBtn, addBtn);
        form.setAlignment(Pos.CENTER_LEFT);

        // Export/Import
        Button exportBtn = new Button("📤 Export");
        exportBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export to JSON");
            fileChooser.setInitialFileName("backup.json");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    ExportImportUtil.exportToJson(CredentialDAO.getAllCredentials(), file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button importBtn = new Button("📥 Import");
        importBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import from JSON");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    List<Credential> creds = ExportImportUtil.importFromJson(file);
                    for (Credential c : creds) {
                        CredentialDAO.addCredential(c);
                    }
                    refreshTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox buttons = new HBox(10, exportBtn, importBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(heading, table, form, buttons);

        Scene scene = new Scene(root, 800, 400);
        stage.setTitle("Password Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(CredentialDAO.getAllCredentials());
    }
}
