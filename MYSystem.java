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
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author vip_1
 */
public class MYSystem extends JFrame {

    public MYSystem() {
        super("Parking Management System");
        setLayout(new BorderLayout());

        Color bgColor = new Color(15, 32, 67); 
        getContentPane().setBackground(bgColor); 

       
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);

   
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE); 
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));

      
        JLabel titleLabel = new JLabel("Welcome to Parking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(bgColor); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

       
        ImageIcon icon = new ImageIcon(
                MYSystem.class.getResource("/images/PK.PNG")
        );

        Image img = icon.getImage().getScaledInstance(
                220, 150, Image.SCALE_SMOOTH
        );

        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

      
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

       
        Color btnBgColor = new Color(225, 235, 245);
        Color textColor = new Color(15, 32, 67);

        styleButton(loginBtn, btnBgColor, textColor);
        styleButton(signupBtn, btnBgColor, textColor);

        Dimension btnSize = new Dimension(140, 42);
        loginBtn.setPreferredSize(btnSize);
        signupBtn.setPreferredSize(btnSize);

        loginBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new SignUpFrame().setVisible(true);
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);

       
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(imageLabel);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(buttonPanel);

    
        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);

        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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