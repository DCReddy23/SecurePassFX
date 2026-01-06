package com.securepass.storage;

import com.securepass.model.Credential;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CredentialDAO {

    public static void addCredential(Credential credential) {
        String sql = "INSERT INTO credentials(website, username, password) VALUES(?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, credential.getWebsite());
            pstmt.setString(2, credential.getUsername());
            pstmt.setString(3, credential.getPassword());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Credential> getAllCredentials() {
        List<Credential> creds = new ArrayList<>();
        String sql = "SELECT * FROM credentials";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Credential cred = new Credential(
                        rs.getString("website"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                creds.add(cred);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creds;
    }

    public static void deleteCredential(String website, String username) {
        String sql = "DELETE FROM credentials WHERE website = ? AND username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, website);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
