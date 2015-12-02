/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 *
 * @author Devina
 */
public class UserInterface {
  JFrame frame; // Membuat frame
  JPanel mainPanel1;
  JPanel mainPanel2;
  JPanel mainPanel3;
  JButton[][] grid; //Grid untuk papan gomoku
  String backgroundColor = "#f0f5f9";
  ArrayList<String> symbol;
  
  public UserInterface() {
    frame = new JFrame("Gomoku"); // Membuat frame
    mainPanel1 = new JPanel();
    mainPanel2 = new JPanel();
    mainPanel3 = new JPanel();
    symbol = new ArrayList<String>(Arrays.asList(new String[]{"■", "▲", "●", "◆", "❤", "★", "✱", "▼", "♣", "♠"}));
    login();
    frame.pack();
    frame.getContentPane().setBackground(Color.white);
    frame.setSize(1366, 730);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true); //makes frame visible
  }
   
  public void login() {
    mainPanel1.setLayout(new GridBagLayout());
    mainPanel1.setBackground(Color.decode(backgroundColor));
    
    GridBagConstraints c = new GridBagConstraints();
    JLabel title = new JLabel("Gomoku");
    title.setFont(new Font("Roboto", Font.PLAIN, 120));
    title.setForeground(Color.decode("#2a4d69"));
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(100,0,0,0);
    c.anchor = GridBagConstraints.PAGE_START;
    mainPanel1.add(title, c);
    JTextField nickname = new JTextField(20);
    
    PromptSupport.setPrompt("Nickname", nickname);
    PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, nickname);
    nickname.setFont(new Font("Sniglet", Font.PLAIN, 25));
    nickname.setForeground(Color.decode("#2a4d69"));
    nickname.setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
    
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.insets = new Insets(0,0,0,0);
    mainPanel1.add(nickname, c);
    JButton login = new JButton("Login");
    login.setFont(new Font("Sniglet", Font.PLAIN, 25));
    login.setOpaque(true);
    login.setBackground(Color.decode("#2a4d69"));
    login.setForeground(Color.decode("#e7eff6"));
    //login.setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
    login.setFocusPainted(false);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets(0,0,250,0);
    //c.gridwidth = 2;   //2 columns wide
    c.anchor = GridBagConstraints.PAGE_END;
    mainPanel1.add(login, c);
    frame.add(mainPanel1);
    login.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (nickname.getText().length() > 0) {
          ArrayList<String> rooms = new ArrayList<String>(Arrays.asList(new String[]{"Room 1", "Room 2", "Room 3", "Room 4"}));
          showRooms(nickname.getText(), rooms);
          frame.setContentPane(mainPanel2);
          frame.invalidate();
          frame.validate();
        } else {
          JOptionPane.showMessageDialog(frame, "Please enter a nickname", "Login error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }
  
  public void showRooms(String nickname, ArrayList<String> roomList) {
    JButton[] rooms = new JButton[roomList.size()];
    mainPanel2.setLayout(new GridBagLayout());
    mainPanel2.setBackground(Color.decode(backgroundColor));
    GridBagConstraints c = new GridBagConstraints();
    
    // JButton untuk membuat room baru
    JButton createRoom = new JButton("Create new room");
    createRoom.setFont(new Font("Sniglet", Font.PLAIN, 25));
    createRoom.setOpaque(true);
    createRoom.setBackground(Color.decode("#2a4d69"));
    createRoom.setForeground(Color.decode("#e7eff6"));
    createRoom.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof Component) {
            
        }
      }
    });
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(50,50,0,0);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    mainPanel2.add(createRoom, c);
    
    // Label untuk menampilkan nama player
    JLabel label = new JLabel("Welcome, " + nickname + "!");
    label.setFont(new Font("Sniglet", Font.PLAIN, 25));
    label.setForeground(Color.decode("#2a4d69"));
    c.gridx = 2;
    c.gridy = 0;
    
    c.insets = new Insets(50,0,0,50);
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    //c.fill = GridBagConstraints.BOTH;
    mainPanel2.add(label, c);
    
    // JPanel untuk menampilkan rooms
    JPanel panel2 = new JPanel();
    panel2.setBackground(Color.decode(backgroundColor));
    panel2.setLayout(new GridLayout(1, 20, 50, 0));
    panel2.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
    for (int i = 0; i < roomList.size(); i++) {
      ImageIcon icon = createImageIcon("./../resources/Household-Room-icon-2.png", "Room Logo");
      rooms[i] = new JButton(roomList.get(i), icon);
      rooms[i].setVerticalTextPosition(SwingConstants.BOTTOM);
      rooms[i].setHorizontalTextPosition(SwingConstants.CENTER);
      rooms[i].setFont(new Font("Sniglet", Font.PLAIN, 25));
      rooms[i].setMinimumSize(new Dimension(200, 300));
      rooms[i].setMaximumSize(new Dimension(200, 300));
      rooms[i].setPreferredSize(new Dimension(200, 300));
      rooms[i].setOpaque(true);
      rooms[i].setBackground(Color.decode("#f7fafc"));
      rooms[i].setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 3));
      rooms[i].setForeground(Color.decode("#2a4d69"));
      rooms[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          Object source = e.getSource();
          if (source instanceof Component) {
              ArrayList<String> players = new ArrayList<String>(Arrays.asList(new String[]{"NamePlayer1", "NamePlayer2", "NamePlayer3", "NamePlayer4"}));
              JButton button = (JButton) source;
              playGame(20, 20, players, button.getText());
              frame.setContentPane(mainPanel3);
              frame.invalidate();
              frame.validate();
          }
        }
      });
      panel2.add(rooms[i]);
    }
    JScrollPane scrollPane = new JScrollPane(panel2);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    //scrollPane.setBounds(50, 30, 300, 50);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.gridwidth = 3;
    c.insets = new Insets(0,0,0,0);
    c.anchor = GridBagConstraints.LAST_LINE_START;
    c.fill = GridBagConstraints.BOTH;
    mainPanel2.add(scrollPane, c);
  }
  
  public void playGame(int width, int length, ArrayList<String>Player, String roomName){
    JPanel panel1 = new JPanel(); // Panel untuk grid
    JPanel panel2 = new JPanel(); //Panel untuk nama room dan info player
    JPanel panel3 = new JPanel(); // Panel untuk nama setiap player
    JPanel panel4 = new JPanel();
    mainPanel3.setLayout(new GridBagLayout()); //Set layout
    GridBagConstraints c = new GridBagConstraints();
    
    // Panel1
    panel1.setLayout(new GridLayout(width,length));
    panel1.setPreferredSize(new Dimension(400, 730));
    panel1.setMaximumSize(new Dimension(400, 730));
    panel1.setMinimumSize(new Dimension(400, 730));
    panel1.setBackground(Color.decode(backgroundColor));
    panel1.setOpaque(true);
    grid=new JButton[width][length]; //Alokasi ukuran grid
    for(int y=0; y<length; y++){
      for(int x=0; x<width; x++){
        grid[x][y]=new JButton();
        //grid[x][y].setBorderPainted(false);
        grid[x][y].setOpaque(true);
        grid[x][y].setBackground(Color.decode("#f7fafc"));
        grid[x][y].setForeground(Color.decode("#2a4d69"));
        grid[x][y].setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
        grid[x][y].setFocusPainted(false);
        //grid[x][y].setContentAreaFilled(false);
        grid[x][y].putClientProperty("row", y);
        grid[x][y].putClientProperty("col", x);
        grid[x][y].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source instanceof Component) {
                JButton button = (JButton) source;
                button.setText(symbol.get(0));
                System.out.println("(" + button.getClientProperty("row") + ", " + button.getClientProperty("col") + ")");
            }
          }
        });
        panel1.add(grid[x][y]); //Menambah JButton ke grid
      }
    }
    
    // Panel3
    panel3.setBackground(Color.decode(backgroundColor));
    float temp = Player.size()/2f;
    
    panel3.setLayout(new GridLayout((int)Math.ceil(temp), 2));
    for (int i = 0; i < Player.size(); i++) {
      ImageIcon icon = createImageIcon("./../resources/Player1.png", "Player Logo");
      JLabel playerNameLabel = new JLabel(Player.get(i), icon, JLabel.LEFT);
      playerNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
      //playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
      playerNameLabel.setFont(new Font("Sniglet", Font.PLAIN, 20));
      playerNameLabel.setForeground(Color.decode("#2a4d69"));
      panel3.add(playerNameLabel);
    }
    
    // Panel2
    panel2.setBackground(Color.decode(backgroundColor));
    panel1.setPreferredSize(new Dimension(700, 730));
    panel1.setMaximumSize(new Dimension(700, 730));
    panel1.setMinimumSize(new Dimension(700, 730));
    panel2.setLayout(new GridLayout(3,1));
    ImageIcon icon = createImageIcon("./../resources/Household-Room-icon-2.png", "Room Logo");
    
    JLabel roomNameLabel = new JLabel(("  " + roomName), icon, JLabel.CENTER);
    roomNameLabel.setFont(new Font("Roboto", Font.PLAIN, 50));
    roomNameLabel.setForeground(Color.decode("#2a4d69"));
    //roomNameLabel.setHorizontalAlignment(JLabel.CENTER);
    panel2.add(roomNameLabel);
    panel2.add(panel3);
    panel4.setBackground(Color.decode(backgroundColor));
    panel2.add(panel4);
    
    // Add panel1 and panel2 to JFrame
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.7;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    //c.anchor = GridBagConstraints.FIRST_LINE_START;
    mainPanel3.add(panel1, c);
    c.gridx = 1000;
    c.gridy = 0;
    c.weightx = 0.3;
    c.weighty = 0.8;
    c.fill = GridBagConstraints.BOTH;
    //c.anchor = GridBagConstraints.CENTER;
    mainPanel3.add(panel2, c);
    frame.add(mainPanel3);
  }
  
  /** Returns an ImageIcon, or null if the path was invalid. */
  protected ImageIcon createImageIcon(String path,String description) {
      java.net.URL imgURL = getClass().getResource(path);
      if (imgURL != null) {
          return new ImageIcon(imgURL, description);
      } else {
          System.err.println("Couldn't find file: " + path);
          return null;
      }
  }
  
  public static void main(String[] args) {
    
    new UserInterface();
    
    
  }
}
