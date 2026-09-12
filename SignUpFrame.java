/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parkingmanagmentsystem;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author vip_1
 * Sign Up Frame - New user registration with car details
 */
public class SignUpFrame extends JFrame {
    private JTextField userName, phone, carPlate, carModel, carColor;
    private JPasswordField password, confirmPassword;

    public SignUpFrame() {
        super("Sign Up & Car Registration");
        setLayout(new BorderLayout());
        setSize(850, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color bgColor = new Color(15, 32, 67); 
        getContentPane().setBackground(bgColor);

        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);

      
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        JLabel title = new JLabel("Create New Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(bgColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(title, gbc);

    
        JLabel userHeader = new JLabel("--- User Information ---");
        userHeader.setForeground(new Color(60, 80, 110));
        userHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 1; gbc.insets = new Insets(12, 8, 6, 8);
        cardPanel.add(userHeader, gbc);

        gbc.insets = new Insets(5, 8, 5, 8);
        userName = addField(cardPanel, "* Full Name:", 2, gbc);
        phone = addField(cardPanel, "* Phone (05xxxxxxxx):", 3, gbc);

        
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 4;
        JLabel p1 = new JLabel("* Password:");
        p1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p1.setForeground(bgColor);
        cardPanel.add(p1, gbc);

        gbc.gridx = 1;
        password = new JPasswordField(15);
        styleTextField(password);
        cardPanel.add(password, gbc);

        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel p2 = new JLabel("* Confirm Password:");
        p2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p2.setForeground(bgColor);
        cardPanel.add(p2, gbc);

        gbc.gridx = 1;
        confirmPassword = new JPasswordField(15);
        styleTextField(confirmPassword);
        cardPanel.add(confirmPassword, gbc);

       
        JLabel carHeader = new JLabel("--- Car Information ---");
        carHeader.setForeground(new Color(60, 80, 110));
        carHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 6, 8);
        cardPanel.add(carHeader, gbc);

        gbc.insets = new Insets(5, 8, 5, 8);
        carPlate = addField(cardPanel, "* License Plate (ABC-123):", 7, gbc);
        carModel = addField(cardPanel, "* Car Model:", 8, gbc);
        carColor = addField(cardPanel, "* Car Color:", 9, gbc);

     
        JButton backBtn = new JButton("Back");
        JButton nextBtn = new JButton("Sign Up");

        Color btnBgColor = new Color(225, 235, 245);
        Color textColor = new Color(15, 32, 67);

        styleButton(backBtn, btnBgColor, textColor);
        styleButton(nextBtn, btnBgColor, textColor);

        Dimension btnSize = new Dimension(130, 40);
        backBtn.setPreferredSize(btnSize);
        nextBtn.setPreferredSize(btnSize);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backBtn);
        buttonPanel.add(nextBtn);

        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 5, 8);
        cardPanel.add(buttonPanel, gbc);

       
        backBtn.addActionListener(e -> {
            dispose();
            new MYSystem().setVisible(true);
        });

        nextBtn.addActionListener(e -> {
            if (validateInputs()) saveUserToDatabase();
        });

        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JTextField addField(JPanel p, String label, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(15, 32, 67));
        p.add(l, gbc);

        gbc.gridx = 1;
        JTextField tf = new JTextField(15);
        styleTextField(tf);
        p.add(tf, gbc);
        return tf;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private boolean validateInputs() {
        if (userName.getText().trim().isEmpty() ||
            phone.getText().trim().isEmpty() ||
            password.getPassword().length == 0 ||
            carPlate.getText().trim().isEmpty() ||
            carModel.getText().trim().isEmpty() ||
            carColor.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "All fields are required!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!phone.getText().matches("^05[0-9]{8}$")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid phone format!\nMust be: 05 followed by 8 digits",
                    "Phone Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.getPassword().length < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters!",
                    "Password Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!new String(password.getPassword()).equals(new String(confirmPassword.getPassword()))) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveUserToDatabase() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String userSql = "INSERT INTO Users (username, phone, password) VALUES (?, ?, ?)";
            PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);

            userStmt.setString(1, userName.getText().trim());
            userStmt.setString(2, phone.getText().trim());
            userStmt.setString(3, new String(password.getPassword()));
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            int userId;
            if (rs.next()) userId = rs.getInt(1);
            else throw new Exception("Failed to retrieve user ID");

            String carSql = "INSERT INTO Cars (user_id, plate_number, model, color) VALUES (?, ?, ?, ?)";
            PreparedStatement carStmt = conn.prepareStatement(carSql);

            carStmt.setInt(1, userId);
            carStmt.setString(2, carPlate.getText().trim());
            carStmt.setString(3, carModel.getText().trim());
            carStmt.setString(4, carColor.getText().trim());
            carStmt.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Account created successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new FindParkingSpotPage(userId).setVisible(true);

        } catch (Exception ex) {
            try { if (conn != null) conn.rollback(); } catch (Exception e) {}
            JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    private void styleButton(JButton btn, Color bgColor, Color textColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(205, 220, 238));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
    }
}