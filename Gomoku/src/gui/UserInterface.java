/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.panels.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import java.util.*;
import module.Request;
import module.Room;
import module.User;
import server.Client;

/**
 *
 * @author Devina
 * @author Natan
 */
public class UserInterface implements Observer {

    JFrame frame; // Membuat frame
    LoginPanel loginPanel;
    RoomsPanel roomsPanel;
    JPanel mainPanel3;
    JButton[][] grid; //Grid untuk papan gomoku
    String backgroundColor = "#f0f5f9";
    ArrayList<String> symbol;

    Client client;

    public UserInterface() {
        frame = new JFrame("Gomoku"); // Membuat frame
        mainPanel3 = new JPanel();
        symbol = new ArrayList<String>(Arrays.asList(new String[]{"■", "▲", "●", "◆", "❤", "★", "✱", "▼", "♣", "♠"}));
        login();
        frame.pack();
        frame.getContentPane().setBackground(Color.white);
        frame.setSize(1300, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //makes frame visible
    }

    public UserInterface(server.Client client) {
        this.client = client;

        frame = new JFrame("Gomoku"); // Membuat frame
        mainPanel3 = new JPanel();
        symbol = new ArrayList<String>(Arrays.asList(new String[]{"■", "▲", "●", "◆", "❤", "★", "✱", "▼", "♣", "♠"}));
        login();
        frame.pack();
        frame.getContentPane().setBackground(Color.white);
        frame.setSize(1300, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //makes frame visible
    }
    
    public void sendLoginCommand(String userName) {
        client.setMe(new User(userName));
        client.sendCommand("login " + userName);
        this.sendGetRoomsCommand();
        this.sendGetUsersCommand();
    }
    
    public void sendGetRoomsCommand() {
        client.sendCommand("get-rooms");
    }
    
    public void sendGetRoomCommand(String roomName) {
        client.sendCommand("get-room " + roomName);
    }
    
    public void sendGetUsersCommand() {
        client.sendCommand("get-users");
    }
    
    public void sendCreateRoomCommand(String roomName) {
        client.sendCommand("create-room " + roomName);
    }
    
    public void sendJoinRoomCommand(String roomName) {
        client.sendCommand("join-room " + roomName);
    }
    
    public void sendStartRoomCommand(String roomName) {
        client.sendCommand("start-room " + roomName);
    }
    
    public void sendExitRoomCommand(String roomName) {
        client.sendCommand("exit-room " + roomName);
    }
    
    public void sendMoveCommand(String roomName, int row, int col) {
        client.sendCommand("move " + roomName + " " + row + " " + col);
    }

    public void login() {
        loginPanel = new LoginPanel();
        
        loginPanel.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginPanel.txtUserName.getText().length() > 0) {
                    sendLoginCommand(loginPanel.txtUserName.getText());

                    //ArrayList<String> rooms = new ArrayList<String>(Arrays.asList(new String[]{"Room 1", "Room 2", "Room 3", "Room 4"}));
                    //showRooms(nickname.getText(), rooms);
                    showRooms();
                    frame.setContentPane(roomsPanel);
                    frame.invalidate();
                    frame.validate();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a nickname", "Login error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.txtUserName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.btnLogin.getActionListeners()[0].actionPerformed(e);
            }
        });
        
        frame.setContentPane(loginPanel);
    }

    public void showRooms() {
        roomsPanel = new RoomsPanel(client.getMe(), client.getRooms());
        
        roomsPanel.btnCreateRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof Component) {
                    String roomName = JOptionPane.showInputDialog("Room Name:");
                    if (roomName != "") {
                        sendCreateRoomCommand(roomName);
                    }
                }
            }
        });
        
        for (JButton btnRoom : roomsPanel.btnRooms) {
            btnRoom.addActionListener(new ActionListener() {
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
        }
        
        frame.setContentPane(roomsPanel);
    }

    public void playGame(int width, int length, ArrayList<String> Player, String roomName) {
        JPanel panel1 = new JPanel(); // Panel untuk grid
        JPanel panel2 = new JPanel(); //Panel untuk nama room dan info player
        JPanel panel3 = new JPanel(); // Panel untuk nama setiap player
        JPanel panel4 = new JPanel();
        mainPanel3.setLayout(new GridBagLayout()); //Set layout
        GridBagConstraints c = new GridBagConstraints();

        // Panel1
        panel1.setLayout(new GridLayout(width, length));
        panel1.setPreferredSize(new Dimension(400, 730));
        panel1.setMaximumSize(new Dimension(400, 730));
        panel1.setMinimumSize(new Dimension(400, 730));
        panel1.setBackground(Color.decode(backgroundColor));
        panel1.setOpaque(true);
        grid = new JButton[width][length]; //Alokasi ukuran grid
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = new JButton();
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
        float temp = Player.size() / 2f;

        panel3.setLayout(new GridLayout((int) Math.ceil(temp), 2));
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
        panel2.setLayout(new GridLayout(3, 1));
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

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        String userName = args[2];

        try {
            Socket socket = new Socket(serverName, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Request req = new Request("login");
            req.addParameter(userName);
            out.writeUTF(req.toString());

            Client client = new Client(socket);

            UserInterface userInterface = new UserInterface(client);
            client.addObserver(userInterface);

            Scanner sc = new Scanner(System.in);
            while (true) {
                String s = sc.nextLine();

                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == client && arg != null) {
            if (roomsPanel != null) {
                switch (arg.toString()) {
                    case "update-rooms":
                        roomsPanel.updateRooms(client.getRooms());
                        for (JButton btnRoom : roomsPanel.btnRooms) {
                            btnRoom.addActionListener(new ActionListener() {
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
                        }
                        break;
                    
                }
                
            }
        }
    }
}
