/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parkingmanagmentsystem;

import javax.swing.JFrame;
import javax.swing.JTextField;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * @author vip_1
 */
public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        super("Login");
        setLayout(new BorderLayout());
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color bgColor = new Color(15, 32, 67); 
        getContentPane().setBackground(bgColor);

       
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);

      
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));

    
        JLabel infoLabel = new JLabel("Login to Your Account");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        infoLabel.setForeground(bgColor);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

     
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setBackground(Color.WHITE);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(bgColor);
        userLabel.setPreferredSize(new Dimension(85, 25));

        userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        userPanel.add(userLabel);
        userPanel.add(userField);

      
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        passPanel.setBackground(Color.WHITE);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(bgColor);
        passLabel.setPreferredSize(new Dimension(85, 25));

        passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        passPanel.add(passLabel);
        passPanel.add(passField);

     
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton back = new JButton("Back");
        JButton next = new JButton("Login");

     
        Color btnBgColor = new Color(225, 235, 245);
        Color textColor = new Color(15, 32, 67);

        styleButton(back, btnBgColor, textColor);
        styleButton(next, btnBgColor, textColor);

        Dimension btnSize = new Dimension(130, 42);
        back.setPreferredSize(btnSize);
        next.setPreferredSize(btnSize);

        buttonPanel.add(back);
        buttonPanel.add(next);

       
        back.addActionListener(e -> {
            dispose();
            new MYSystem().setVisible(true);
        });

        next.addActionListener(e -> {
            if (validateInputs()) performLogin();
        });

   
        cardPanel.add(infoLabel);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(userPanel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private boolean validateInputs() {
        if (userField.getText().trim().isEmpty() || passField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this,
                "Username and password cannot be empty!",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void performLogin() {
        try {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            String sql = "SELECT user_id FROM Users WHERE username = ? AND password = ?";
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                Object[] options = {"New Booking", "View Booking"};
                int choice = JOptionPane.showOptionDialog(this,
                        "Login Successful! What would you like to do?",
                        "Login Success",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     
                        options,  
                        options[0] 
                );

                if (choice == JOptionPane.YES_OPTION) { 
                    dispose(); 
                    new FindParkingSpotPage(userId).setVisible(true);
                } else if (choice == JOptionPane.NO_OPTION) { 
                    dispose(); 
                    new UserProfileFrame(userId).setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR:\n" + ex.getMessage(),
                    "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton btn, Color bgColor, Color textColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
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