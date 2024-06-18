package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum ConectedPerson {
    INSTANCE;

    // Speichert welche Person gerade verbunden ist, um den Zugriff auf die Daten zu ermöglichen
    private String username = RegistrationController.getInputUsername();

    private final ConnectDB db = new ConnectDB();

    // Getter für den Benutzernamen
    public String getUsername() {
        return username;
    }

    // Getter für das Passwort
    public String getPassword() {
        return getUserAttribute("password", "");
    }

    // Weitere Getter für die anderen Attribute
    public String getName() {
        return getUserAttribute("name", "");
    }

    public double getWeight() {
        return Double.parseDouble(getUserAttribute("weight", "0"));
    }

    public double getHeight() {
        return Double.parseDouble(getUserAttribute("height", "0"));
    }

    public int getCaloriesGoal() {
        return Integer.parseInt(getUserAttribute("caloriesGoal", "0"));
    }

    public int getCaloriesEatenToday() {
        return Integer.parseInt(getUserAttribute("caloriesEatenToday", "0"));
    }

    public int getStepsGoal() {
        return Integer.parseInt(getUserAttribute("stepsGoal", "0"));
    }

    public int getStepsToday() {
        return Integer.parseInt(getUserAttribute("stepsToday", "0"));
    }

    // Setter für die Attribute
    public void setPassword(String password) {
        setUserAttribute("password", password);
    }

    public void setName(String name) {
        setUserAttribute("name", name);
    }

    public void setWeight(double weight) {
        setUserAttribute("weight", String.valueOf(weight));
    }

    public void setHeight(double height) {
        setUserAttribute("height", String.valueOf(height));
    }

    public void setCaloriesGoal(int caloriesGoal) {
        setUserAttribute("caloriesGoal", String.valueOf(caloriesGoal));
    }

    public void setCaloriesEatenToday(int caloriesEatenToday) {
        setUserAttribute("caloriesEatenToday", String.valueOf(caloriesEatenToday));
    }

    public void setStepsGoal(int stepsGoal) {
        setUserAttribute("stepsGoal", String.valueOf(stepsGoal));
    }

    public void setStepsToday(int stepsToday) {
        setUserAttribute("stepsToday", String.valueOf(stepsToday));
    }

    // Methode zum Abrufen eines Benutzerattributs aus der Datenbank
    private String getUserAttribute(String attribute, String defaultValue) {
        String value = defaultValue;
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT " + attribute + " FROM users WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    value = rs.getString(attribute);
                    if (value == null) {
                        value = defaultValue;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    // Methode zum Setzen eines Benutzerattributs in der Datenbank
    private void setUserAttribute(String attribute, String value) {
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                String update = "UPDATE users SET " + attribute + " = ? WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(update);
                pstmt.setString(1, value);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
