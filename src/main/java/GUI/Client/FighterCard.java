/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Client;

import Fighters.Fighter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class FighterCard extends JPanel {
    private JLabel lblName;
    private JProgressBar barHP;
    private JLabel lblType; // Added for visual type display

    public FighterCard(Fighter fighter) {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), fighter.getName(), TitledBorder.CENTER, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));
        this.setBackground(new Color(40, 40, 40));
        this.setPreferredSize(new Dimension(120, 150));

        lblName = new JLabel(fighter.getName(), SwingConstants.CENTER);
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        lblType = new JLabel("Type: " + fighter.getType().name(), SwingConstants.CENTER);
        lblType.setForeground(Color.LIGHT_GRAY);
        lblType.setFont(new Font("SansSerif", Font.PLAIN, 10));

        barHP = new JProgressBar(0, 100);
        barHP.setValue(fighter.getLife());
        barHP.setStringPainted(true);
        barHP.setForeground(Color.GREEN);
        barHP.setBackground(Color.DARK_GRAY);

        this.add(lblName, BorderLayout.NORTH);
        this.add(lblType, BorderLayout.CENTER);
        this.add(barHP, BorderLayout.SOUTH);
    }

    public void updateHP(int newHP) {
        barHP.setValue(newHP);
        barHP.setString(newHP + "%");
        if (newHP < 30) {
            barHP.setForeground(Color.RED);
        } else if (newHP < 60) {
            barHP.setForeground(Color.ORANGE);
        } else {
            barHP.setForeground(Color.GREEN);
        }
    }

    public String getFighterName() {
        return lblName.getText();
    }
}