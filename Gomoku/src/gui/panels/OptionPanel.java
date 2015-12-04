/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import module.User;

/**
 *
 * @author William Sentosa
 */
public class OptionPanel extends JPanel {
    private List<User> users;
    private String backgroundColor = "#f0f5f9";
    private final int width = 500;
    private final int length = 300;
    public JButton playButton;
    public JButton exitButton;
    
    public OptionPanel(List<User> users) {
        this.users = users;
        this.setPreferredSize(new Dimension(width, length));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.decode(backgroundColor));
    }
    
    public OptionPanel() {
        this.setLayout(null);
        users = new ArrayList<User>();
        users.add(new User("William"));
        users.add(new User("Natan"));
        users.add(new User("Devina"));
        this.setPreferredSize(new Dimension(width, length));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.decode(backgroundColor));
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public void updatePanel() {
        this.removeAll();
        initComponent();
        this.revalidate();
        this.repaint();
    }
    
    public void initComponent() {
        JPanel optionPnl = new JPanel();
        optionPnl.setLayout(new GridLayout(2,1));
        optionPnl.setBackground(Color.decode(backgroundColor));
        
        JLabel header = new JLabel("What do you want to do in this room ?");
        header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(new Font("Roboto", Font.PLAIN, 20));
        optionPnl.add(header);
        
        JPanel buttonsPnl = new JPanel();
        buttonsPnl.setBackground(Color.decode(backgroundColor));
        buttonsPnl.setLayout(new GridLayout(1,2,10,10));
        buttonsPnl.setBorder(BorderFactory.createEmptyBorder(30, 10, 70, 10));
        exitButton = new JButton("Watch");
        exitButton.setFont(new Font("Sniglet", Font.PLAIN, 15));
        exitButton.setOpaque(true);
        exitButton.setBackground(Color.decode("#2a4d69"));
        exitButton.setForeground(Color.decode("#e7eff6"));
        
        
        header.setBounds(new Rectangle(new Point(65,60), header.getPreferredSize()));
        playButton = new JButton("Play");
        playButton.setFont(new Font("Sniglet", Font.PLAIN, 15));
        playButton.setOpaque(true);
        playButton.setPreferredSize(exitButton.getPreferredSize());
        playButton.setBackground(Color.decode("#2a4d69"));
        playButton.setForeground(Color.decode("#e7eff6"));
        
        buttonsPnl.add(playButton);
        buttonsPnl.add(exitButton);
        
        optionPnl.add(buttonsPnl);
        
        add(optionPnl);
        
        JPanel panelUser = new JPanel();
        panelUser.setBackground(Color.decode(backgroundColor));
        panelUser.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        panelUser.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.decode("#2a4d69")));
        panelUser.setPreferredSize(new Dimension(120, 200));
        panelUser.setLayout(new GridLayout(7,1));
        JLabel label1 = new JLabel("User in this room :");
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setFont(new Font("Roboto", Font.BOLD, 15));
        label1.setForeground(Color.decode("#4b86b4"));
        panelUser.add(label1);
        
        for(User user : users) {
            JLabel label = new JLabel(user.getName());
            label.setForeground(Color.decode("#63ace5"));
            label.setFont(new Font("Sniglet", Font.PLAIN, 14));
            panelUser.add(label);
            label.setHorizontalAlignment(JLabel.CENTER);
        }
        add(panelUser);
        panelUser.setBounds(new Rectangle(new Point(350,20), panelUser.getPreferredSize()));
        
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        frame.setSize(550,300);
        OptionPanel panel = new OptionPanel();
        panel.initComponent();
        mainPanel.add(panel);
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
   
}
