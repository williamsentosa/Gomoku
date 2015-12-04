/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    }
    
    public OptionPanel() {
        this.setLayout(null);
        users = new ArrayList<User>();
        users.add(new User("William"));
        users.add(new User("Natan"));
        users.add(new User("Devina"));
        this.setPreferredSize(new Dimension(width, length));
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
        this.setBackground(Color.decode(backgroundColor));
        JLabel header = new JLabel("What do you want to do in this room ?");
        header.setFont(new Font("Sniglet", Font.PLAIN, 15));
        add(header);
        header.setBounds(new Rectangle(new Point(65,60), header.getPreferredSize()));
        playButton = new JButton("Play");
        exitButton = new JButton("Watch");
        add(playButton);
        playButton.setBounds(new Rectangle(new Point(90,140), playButton.getPreferredSize()));
        add(exitButton);
        exitButton.setBounds(new Rectangle(new Point(220,140), exitButton.getPreferredSize()));
        JPanel panelUser = new JPanel();
        panelUser.setPreferredSize(new Dimension(120, 200));
        panelUser.setLayout(new GridLayout(7,1));
        JLabel label1 = new JLabel("User in this room :");
        label1.setHorizontalAlignment(JLabel.CENTER);
        panelUser.add(label1);
        
        for(User user : users) {
            JLabel label = new JLabel(user.getName());
            panelUser.add(label);
            label.setHorizontalAlignment(JLabel.CENTER);
        }
        add(panelUser);
        panelUser.setBounds(new Rectangle(new Point(350,20), panelUser.getPreferredSize()));
        
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        frame.setSize(500,300);
        OptionPanel panel = new OptionPanel();
        panel.initComponent();
        mainPanel.add(panel);
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
   
}
