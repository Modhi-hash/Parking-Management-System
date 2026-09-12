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
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserProfileFrame extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public UserProfileFrame(int userId) {
        this.userId = userId;

        setTitle("User Profile & Bookings Management");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color bgColor = new Color(15, 32, 67);
        getContentPane().setBackground(bgColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel titleLabel = new JLabel("Manage Your Bookings & Services");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(bgColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] columnNames = {"Booking ID", "Spot", "Start Time", "End Time", "Status", "Services"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(225, 235, 245));
        table.getTableHeader().setForeground(bgColor);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(780, 240));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadUserBookings();

        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton modifyServicesBtn = new JButton("Modify Services");
        JButton cancelBookingBtn = new JButton("Cancel Booking");
        JButton newBookingBtn = new JButton("+ New Booking");
        JButton logoutBtn = new JButton("Logout");

        Color btnBgColor = new Color(225, 235, 245);
        styleButton(modifyServicesBtn, btnBgColor, bgColor);
        styleButton(cancelBookingBtn, new Color(255, 230, 230), new Color(150, 30, 30));
        styleButton(newBookingBtn, btnBgColor, bgColor);
        styleButton(logoutBtn, btnBgColor, bgColor);

        
        modifyServicesBtn.addActionListener(e -> modifySelectedServices());

       
        cancelBookingBtn.addActionListener(e -> cancelSelectedBooking());

        newBookingBtn.addActionListener(e -> {
            dispose();
            new FindParkingSpotPage(userId).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new MYSystem().setVisible(true);
        });

        actionPanel.add(modifyServicesBtn);
        actionPanel.add(cancelBookingBtn);
        actionPanel.add(newBookingBtn);
        actionPanel.add(logoutBtn);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(scrollPane);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(actionPanel);

        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadUserBookings() {
        model.setRowCount(0);
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT b.booking_id, s.spot_name, b.start_time, b.end_time, b.status, "
                    + "b.car_wash, b.car_charging, b.car_oil_change "
                    + "FROM Bookings b JOIN ParkingSpots s ON b.spot_id = s.spot_id "
                    + "WHERE b.user_id = ? ORDER BY b.booking_id DESC";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String spotName = rs.getString("spot_name");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String status = rs.getString("status");

                StringBuilder services = new StringBuilder();
                if (rs.getBoolean("car_wash")) services.append("Wash ");
                if (rs.getBoolean("car_charging")) services.append("Charge ");
                if (rs.getBoolean("car_oil_change")) services.append("Oil ");

                if (services.length() == 0) services.append("None");

                model.addRow(new Object[]{bookingId, spotName, startTime, endTime, status, services.toString().trim()});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching bookings:\n" + ex.getMessage());
        }
    }

    private void modifySelectedServices() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking from the table first!");
            return;
        }

        int bookingId = (int) model.getValueAt(selectedRow, 0);

        Object[] options = {"Update Wash Only", "Update Charging Only", "Update Oil Only", "Clear All Services"};
        int choice = JOptionPane.showOptionDialog(this, "Select Service Modification for Booking #" + bookingId,
                "Modify Services", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != JOptionPane.CLOSED_OPTION) {
            try {
                Connection con = DatabaseConnection.getConnection();
                String sql = "";
                if (choice == 0) sql = "UPDATE Bookings SET car_wash = NOT car_wash WHERE booking_id = ?";
                else if (choice == 1) sql = "UPDATE Bookings SET car_charging = NOT car_charging WHERE booking_id = ?";
                else if (choice == 2) sql = "UPDATE Bookings SET car_oil_change = NOT car_oil_change WHERE booking_id = ?";
                else if (choice == 3) sql = "UPDATE Bookings SET car_wash=0, car_charging=0, car_oil_change=0 WHERE booking_id = ?";

                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, bookingId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Services Updated Successfully!");
                loadUserBookings();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating services: " + ex.getMessage());
            }
        }
    }

    private void cancelSelectedBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel!");
            return;
        }

        int bookingId = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel booking #" + bookingId + "?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = DatabaseConnection.getConnection();
                String sql = "UPDATE Bookings SET status = 'Cancelled' WHERE booking_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, bookingId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Booking Cancelled.");
                loadUserBookings();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error cancelling booking: " + ex.getMessage());
            }
        }
    }

    private void styleButton(JButton btn, Color bgColor, Color textColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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