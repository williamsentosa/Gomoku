/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
    public JButton btnBack;
    
    public HighScorePanel(String file) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));   
        this.file = file;
        highScores = new HighScores(file);
    }
    
    public HighScorePanel(HighScores hs) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));   
        this.file = file;
        highScores = hs;
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
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // Button Back
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("Sniglet", Font.PLAIN, 25));
        btnBack.setOpaque(true);
        btnBack.setBackground(Color.decode("#2a4d69"));
        btnBack.setForeground(Color.decode("#e7eff6"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(30, 50, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        add(btnBack, c);
        
        // Label untuk judul
        JLabel title = new JLabel("HighScores");
        title.setFont(new Font("Roboto", Font.PLAIN, 70));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.decode("#2a4d69"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.insets = new Insets(20, 0, 0, 0);
        c.anchor = GridBagConstraints.PAGE_START;
        add(title, c);
        
        
        JPanel highScoreTable = new JPanel();
        highScoreTable.setPreferredSize(new Dimension(500, 300));
        highScoreTable.setLayout(new GridLayout(highScores.getHighScore().size()+1, 2));

        JLabel nicknameLabel = new JLabel("Nickname");
        nicknameLabel.setHorizontalAlignment(JLabel.CENTER);
        nicknameLabel.setFont(new Font("Sniglet", Font.PLAIN, 25));
        nicknameLabel.setOpaque(true);
        nicknameLabel.setBackground(Color.decode("#2a4d69"));
        nicknameLabel.setForeground(Color.decode("#e7eff6"));
        highScoreTable.add(nicknameLabel);

        JLabel scoresLabel = new JLabel("Score");
        scoresLabel.setHorizontalAlignment(JLabel.CENTER);
        scoresLabel.setFont(new Font("Sniglet", Font.PLAIN, 25));
        scoresLabel.setOpaque(true);
        scoresLabel.setBackground(Color.decode("#2a4d69"));
        scoresLabel.setForeground(Color.decode("#e7eff6"));
        highScoreTable.add(scoresLabel);
        
        int highScoresSize = 0;
        if (highScores.getHighScore().size() < maxDisplay) {
            highScoresSize = highScores.getHighScore().size();
        } else {
            highScoresSize = maxDisplay;
        }
        
        for (int i = 0; i < highScoresSize; i++) {
            JLabel userLabel = new JLabel(highScores.getHighScore().get(i).getUser());
            userLabel.setHorizontalAlignment(JLabel.CENTER);
            userLabel.setBackground(Color.white);
            userLabel.setBorder(BorderFactory.createLineBorder(Color.decode("#2a4d69"), 1));
            userLabel.setFont(new Font("Sniglet", Font.PLAIN, 20));
            userLabel.setForeground(Color.decode("#2a4d69"));
            highScoreTable.add(userLabel);

            JLabel scoreLabel = new JLabel(Integer.toString(highScores.getHighScore().get(i).getScore()));
            scoreLabel.setHorizontalAlignment(JLabel.CENTER);
            scoreLabel.setFont(new Font("Sniglet", Font.PLAIN, 20));
            scoreLabel.setBackground(Color.white);
            scoreLabel.setBorder(BorderFactory.createLineBorder(Color.decode("#2a4d69"), 1));
            scoreLabel.setForeground(Color.decode("#2a4d69"));
            highScoreTable.add(scoreLabel);
        }

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;

        add(highScoreTable, c);
        
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
