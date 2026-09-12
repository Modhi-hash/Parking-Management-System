/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parkingmanagmentsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChooseServicesFrame extends JFrame {

    private int userId, spotId;
    private String spotName, startTimeStr, endTimeStr;
    private JCheckBox washCheckBox, chargingCheckBox, oilCheckBox;

    public ChooseServicesFrame(int userId, int spotId, String spotName, String startTimeStr, String endTimeStr) {
        this.userId = userId;
        this.spotId = spotId;
        this.spotName = spotName;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;

        setTitle("Choose Additional Services");
        setSize(800, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color bgColor = new Color(15, 32, 67);
        getContentPane().setBackground(bgColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));

        JLabel titleLabel = new JLabel("Services for Spot: " + spotName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(bgColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel servicesPanel = new JPanel();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        servicesPanel.setBackground(Color.WHITE);
        servicesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        washCheckBox = new JCheckBox("Car Wash (+ $15)");
        chargingCheckBox = new JCheckBox("EV Charging (+ $20)");
        oilCheckBox = new JCheckBox("Oil Change (+ $35)");

        styleCheckBox(washCheckBox, bgColor);
        styleCheckBox(chargingCheckBox, bgColor);
        styleCheckBox(oilCheckBox, bgColor);

        servicesPanel.add(washCheckBox);
        servicesPanel.add(Box.createVerticalStrut(12));
        servicesPanel.add(chargingCheckBox);
        servicesPanel.add(Box.createVerticalStrut(12));
        servicesPanel.add(oilCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton backBtn = new JButton("Back");
        JButton confirmBtn = new JButton("Confirm Booking");

        Color btnBgColor = new Color(225, 235, 245);
        styleButton(backBtn, btnBgColor, bgColor);
        styleButton(confirmBtn, btnBgColor, bgColor);

        backBtn.setPreferredSize(new Dimension(130, 42));
        confirmBtn.setPreferredSize(new Dimension(180, 42));

        backBtn.addActionListener(e -> {
            dispose();
            new FindParkingSpotPage(userId).setVisible(true);
        });

        confirmBtn.addActionListener(e -> confirmBooking());

        buttonPanel.add(backBtn);
        buttonPanel.add(confirmBtn);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(servicesPanel);
        cardPanel.add(Box.createVerticalStrut(35));
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void styleCheckBox(JCheckBox cb, Color textColor) {
        cb.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cb.setForeground(textColor);
        cb.setBackground(Color.WHITE);
        cb.setFocusPainted(false);
    }

    private void confirmBooking() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String insertSql = "INSERT INTO Bookings (user_id, spot_id, start_time, end_time, status, car_wash, car_charging, car_oil_change) "
                    + "VALUES (?, ?, ?, ?, 'Confirmed', ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(insertSql);
            pst.setInt(1, userId);
            pst.setInt(2, spotId);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = now.plusHours(2);

            pst.setTimestamp(3, Timestamp.valueOf(now));
            pst.setTimestamp(4, Timestamp.valueOf(endTime));

            pst.setBoolean(5, washCheckBox.isSelected());
            pst.setBoolean(6, chargingCheckBox.isSelected());
            pst.setBoolean(7, oilCheckBox.isSelected());

            pst.executeUpdate();

            String updateSpot = "UPDATE ParkingSpots SET status = 'Reserved' WHERE spot_id = ?";
            PreparedStatement pstSpot = con.prepareStatement(updateSpot);
            pstSpot.setInt(1, spotId);
            pstSpot.executeUpdate();

            JOptionPane.showMessageDialog(this, "Booking Confirmed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new UserProfileFrame(userId).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error processing booking:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton btn, Color bgColor, Color textColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(205, 220, 238)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
    }
}