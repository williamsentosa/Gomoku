/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import module.HighScore;
import module.HighScores;

/**
 *
 * @author William Sentosa
 */
public class HighScorePanel extends JPanel {
    private final int maxDisplay = 10;
    private HighScores highScores;
    private String file;
    private String backgroundColor = "#f0f5f9";
    
    public HighScorePanel(String file) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));   
        this.file = file;
        highScores = new HighScores(file);
    }
    
    public void updateComponent() {
        this.removeAll();
        highScores = new HighScores(file);
        initComponent();
        this.revalidate();
        this.repaint();
    }
    
    public void initComponent() {
        this.setBackground(Color.decode(backgroundColor));
        this.setPreferredSize(new Dimension(800,800));
        JLabel header = new JLabel("HighScores");
        add(header);
        if(highScores.getSize() >= maxDisplay) {
            
        } else {
            for(HighScore hs : highScores.getHighScore()) {
                JLabel label = new JLabel(hs.toString());
                add(label);
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        HighScorePanel panel = new HighScorePanel("scores");
        panel.initComponent();
        mainPanel.add(panel);
        JButton button = new JButton("Refreshed");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.updateComponent();
            }
        });
        mainPanel.add(button);
        frame.add(mainPanel);
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
