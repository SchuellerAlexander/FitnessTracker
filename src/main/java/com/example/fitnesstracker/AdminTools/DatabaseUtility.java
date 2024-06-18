package com.example.fitnesstracker.AdminTools;

import com.example.fitnesstracker.models.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseUtility {

    private static final ConnectDB db = new ConnectDB();
    private static Connection connection;

    public static void main(String[] args) {
        connection = db.getConnection();
        if (connection == null) {
            System.out.println("Failed to connect to the database.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nFitness Tracker Database Utility");
            System.out.println("1. View all users");
            System.out.println("2. View user by username");
            System.out.println("3. Update user data");
            System.out.println("4. Delete user");
            System.out.println("5. Delete all users");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    viewUserByUsername(username);
                    break;
                case 3:
                    System.out.print("Enter username to update: ");
                    String usernameToUpdate = scanner.nextLine();
                    updateUser(usernameToUpdate, scanner);
                    break;
                case 4:
                    System.out.print("Enter username to delete: ");
                    String usernameToDelete = scanner.nextLine();
                    deleteUser(usernameToDelete);
                    break;
                case 5:
                    deleteAllUsers();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    closeConnection();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAllUsers() {
        if (checkConnection()) {
            try {
                String query = "SELECT * FROM users";
                PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    printUserDetails(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void viewUserByUsername(String username) {
        if (checkConnection()) {
            try {
                String query = "SELECT * FROM users WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    printUserDetails(rs);
                } else {
                    System.out.println("User not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void updateUser(String username, Scanner scanner) {
        if (checkConnection()) {
            try {
                String query = "UPDATE users SET password = ?, name = ?, weight = ?, height = ?, caloriesGoal = ?, caloriesEatenToday = ?, stepsGoal = ?, stepsToday = ? WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);

                System.out.print("Enter new password: ");
                String password = scanner.nextLine();
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new weight: ");
                double weight = scanner.nextDouble();
                System.out.print("Enter new height: ");
                double height = scanner.nextDouble();
                System.out.print("Enter new calories goal: ");
                int caloriesGoal = scanner.nextInt();
                System.out.print("Enter new calories eaten today: ");
                int caloriesEatenToday = scanner.nextInt();
                System.out.print("Enter new steps goal: ");
                int stepsGoal = scanner.nextInt();
                System.out.print("Enter new steps today: ");
                int stepsToday = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                pstmt.setString(1, password);
                pstmt.setString(2, name);
                pstmt.setDouble(3, weight);
                pstmt.setDouble(4, height);
                pstmt.setInt(5, caloriesGoal);
                pstmt.setInt(6, caloriesEatenToday);
                pstmt.setInt(7, stepsGoal);
                pstmt.setInt(8, stepsToday);
                pstmt.setString(9, username);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("User data updated successfully.");
                } else {
                    System.out.println("User not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteUser(String username) {
        if (checkConnection()) {
            try {
                String query = "DELETE FROM users WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("User not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteAllUsers() {
        if (checkConnection()) {
            try {
                String query = "DELETE FROM users";
                PreparedStatement pstmt = connection.prepareStatement(query);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("All users deleted successfully.");
                } else {
                    System.out.println("No users to delete.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printUserDetails(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Username: " + rs.getString("username"));
        System.out.println("Password: " + rs.getString("password"));
        System.out.println("Name: " + rs.getString("name"));
        System.out.println("Weight: " + rs.getDouble("weight"));
        System.out.println("Height: " + rs.getDouble("height"));
        System.out.println("Calories Goal: " + rs.getInt("caloriesGoal"));
        System.out.println("Calories Eaten Today: " + rs.getInt("caloriesEatenToday"));
        System.out.println("Steps Goal: " + rs.getInt("stepsGoal"));
        System.out.println("Steps Today: " + rs.getInt("stepsToday"));
        System.out.println("----------------------------");
    }

    private static boolean checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = db.getConnection();
            }
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection Closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
