// HotelManagementSystem.java
// Core Java + AWT + Swing + MySQL-based Hotel Management System

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HotelManagementSystem extends JFrame implements ActionListener {
    JTextField tfName, tfRoom;
    JButton btnCheckIn, btnCheckOut, btnView;
    JTextArea textArea;

    Connection conn;
    Statement stmt;

    public HotelManagementSystem() {
        setTitle("Hotel Management System");
        setLayout(new FlowLayout());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tfName = new JTextField(20);
        tfRoom = new JTextField(5);
        btnCheckIn = new JButton("Check-In");
        btnCheckOut = new JButton("Check-Out");
        btnView = new JButton("View All");
        textArea = new JTextArea(10, 30);

        add(new JLabel("Guest Name:"));
        add(tfName);
        add(new JLabel("Room No:"));
        add(tfRoom);
        add(btnCheckIn);
        add(btnCheckOut);
        add(btnView);
        add(new JScrollPane(textArea));

        btnCheckIn.addActionListener(this);
        btnCheckOut.addActionListener(this);
        btnView.addActionListener(this);

        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "password");
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS guests (name VARCHAR(255), room INT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == btnCheckIn) {
                String name = tfName.getText();
                int room = Integer.parseInt(tfRoom.getText());
                stmt.executeUpdate("INSERT INTO guests VALUES ('" + name + "', " + room + ")");
                textArea.setText("Checked In: " + name + " to Room " + room);
            } else if (ae.getSource() == btnCheckOut) {
                int room = Integer.parseInt(tfRoom.getText());
                stmt.executeUpdate("DELETE FROM guests WHERE room = " + room);
                textArea.setText("Checked Out from Room " + room);
            } else if (ae.getSource() == btnView) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM guests");
                StringBuilder sb = new StringBuilder("Current Guests:\n");
                while (rs.next()) {
                    sb.append("Name: ").append(rs.getString("name"))
                      .append(" | Room: ").append(rs.getInt("room"))
                      .append("\n");
                }
                textArea.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new HotelManagementSystem().setVisible(true);
    }
}
