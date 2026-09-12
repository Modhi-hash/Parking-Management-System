/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.parkingmanagmentsystem;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author vip_1
 */
public class Parkingmanagmentsystem {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

       
        SwingUtilities.invokeLater(() -> {
            MYSystem m = new MYSystem();
            m.setSize(800, 500);
            m.setLocationRelativeTo(null); 
            m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            m.getContentPane().setBackground(new java.awt.Color(10, 25, 74)); 
            m.setVisible(true);
        });
    }
}