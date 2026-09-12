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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FindParkingSpotPage extends JFrame {

    private int userId;
    private JComboBox<String> zoneCombo, floorCombo;
    private JTextField startTimeField, endTimeField;
    private JPanel spotsPanel;
    private int selectedSpotId = -1;
    private String selectedSpotName = "";
    private List<JButton> spotButtons = new ArrayList<>();

    public FindParkingSpotPage(int userId) {
        this.userId = userId;

        setTitle("Find Parking Spot");
        setSize(850, 700);
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

       
        JLabel titleLabel = new JLabel("Search & Select Parking Spot");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(bgColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

       
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel zoneLabel = new JLabel("Zone:");
        styleLabel(zoneLabel, bgColor);
        zoneCombo = new JComboBox<>(new String[]{"All", "Zone A", "Zone B", "Zone C"});

        JLabel floorLabel = new JLabel("Floor:");
        styleLabel(floorLabel, bgColor);
        floorCombo = new JComboBox<>(new String[]{"All", "Floor 1", "Floor 2"});

        JLabel startLabel = new JLabel("Start Time (YYYY-MM-DD HH:MM):");
        styleLabel(startLabel, bgColor);
        startTimeField = new JTextField(12);

        JLabel endLabel = new JLabel("End Time (YYYY-MM-DD HH:MM):");
        styleLabel(endLabel, bgColor);
        endTimeField = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0; filterPanel.add(zoneLabel, gbc);
        gbc.gridx = 1; filterPanel.add(zoneCombo, gbc);
        gbc.gridx = 2; filterPanel.add(floorLabel, gbc);
        gbc.gridx = 3; filterPanel.add(floorCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; filterPanel.add(startLabel, gbc);
        gbc.gridx = 1; filterPanel.add(startTimeField, gbc);
        gbc.gridx = 2; filterPanel.add(endLabel, gbc);
        gbc.gridx = 3; filterPanel.add(endTimeField, gbc);

        JButton searchBtn = new JButton("Search Spots");
        Color btnBgColor = new Color(225, 235, 245);
        styleButton(searchBtn, btnBgColor, bgColor);
        searchBtn.addActionListener(e -> loadSpots());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        filterPanel.add(searchBtn, gbc);

       
        spotsPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        spotsPanel.setBackground(Color.WHITE);

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton backBtn = new JButton("Back");
        JButton nextBtn = new JButton("Next (Choose Services)");

        styleButton(backBtn, btnBgColor, bgColor);
        styleButton(nextBtn, btnBgColor, bgColor);

        backBtn.setPreferredSize(new Dimension(120, 40));
        nextBtn.setPreferredSize(new Dimension(210, 40));

        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        nextBtn.addActionListener(e -> {
            if (selectedSpotId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an available parking spot!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                dispose();
                new ChooseServicesFrame(userId, selectedSpotId, selectedSpotName, startTimeField.getText().trim(), endTimeField.getText().trim()).setVisible(true);
            }
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(nextBtn);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(filterPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(spotsPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);

        loadSpots(); 
    }

    private void loadSpots() {
        spotsPanel.removeAll();
        spotButtons.clear();
        selectedSpotId = -1;

        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ParkingSpots";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int spotId = rs.getInt("spot_id");
                String spotName = rs.getString("spot_name");
                String status = rs.getString("status");

                JButton spotBtn = new JButton(spotName);
                spotBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                spotBtn.setPreferredSize(new Dimension(100, 45));
                spotBtn.setFocusPainted(false);

                if (status.equalsIgnoreCase("Available")) {
                    spotBtn.setBackground(new Color(230, 245, 230));
                    spotBtn.setForeground(new Color(20, 100, 30));
                    spotBtn.setBorder(BorderFactory.createLineBorder(new Color(140, 200, 140), 1));
                    spotBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                    spotBtn.addActionListener(e -> {
                        selectedSpotId = spotId;
                        selectedSpotName = spotName;
                        for (JButton btn : spotButtons) {
                            if (btn.isEnabled()) {
                                btn.setBackground(new Color(230, 245, 230));
                                btn.setForeground(new Color(20, 100, 30));
                            }
                        }
                        spotBtn.setBackground(new Color(15, 32, 67));
                        spotBtn.setForeground(Color.WHITE);
                    });
                } else {
                    spotBtn.setBackground(new Color(245, 225, 225));
                    spotBtn.setForeground(new Color(150, 30, 30));
                    spotBtn.setEnabled(false);
                }

                spotButtons.add(spotBtn);
                spotsPanel.add(spotBtn);
            }
            spotsPanel.revalidate();
            spotsPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading spots: " + e.getMessage());
        }
    }

    private void styleLabel(JLabel l, Color c) {
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(c);
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