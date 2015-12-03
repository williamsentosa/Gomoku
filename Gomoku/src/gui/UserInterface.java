/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Position;
import gui.panels.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(UserInterface.class.getName());

    MainFrame frame; // Membuat frame
    LoginPanel loginPanel;
    RoomsPanel roomsPanel;
    RoomPanel roomPanel;
    JButton[][] grid; //Grid untuk papan gomoku
    String backgroundColor = "#f0f5f9";

    Client client;

    public UserInterface() {
        frame = new MainFrame("Gomoku"); // Membuat frame
        login();
        frame.pack();
        frame.getContentPane().setBackground(Color.white);
        frame.setSize(1300, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //makes frame visible
    }

    public UserInterface(server.Client client) {
        this.client = client;

        frame = new MainFrame("Gomoku"); // Membuat frame
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
                    if (!"".equals(roomName)) {
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
                        String roomName = (String)btnRoom.getClientProperty("roomName");
                        sendJoinRoomCommand(roomName);
                        playGame(roomName);
                        if (roomPanel != null) {
                            frame.setContentPane(roomPanel);
                            frame.invalidate();
                            frame.validate();
                        }
                    }
                }
            });
        }
        
        frame.setContentPane(roomsPanel);
    }

    public void playGame(String roomName) {
        roomPanel = new RoomPanel(roomName, client.getMe());
        
        if (roomPanel.btnCells != null) {
            for (JButton[] btnRow : roomPanel.btnCells) {
                for (JButton btnCell : btnRow) {
                    btnCell.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Object source = e.getSource();
                            if (source instanceof Component) {
                                JButton button = (JButton) source;
                                sendMoveCommand(roomName, (int) button.getClientProperty("row"), (int) button.getClientProperty("col"));
                            }
                        }
                    });
                }
            }
        }
        
        frame.add(roomPanel);
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

        try {
            Socket socket = new Socket(serverName, port);
            Client client = new Client(socket);
            UserInterface userInterface = new UserInterface(client);
            client.addObserver(userInterface);
            while(true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == client && arg != null) {
            switch (arg.toString()) {
                case "update-rooms":
                    if (roomsPanel != null) {
                        roomsPanel.updateRooms(client.getRooms());
                        for (JButton btnRoom : roomsPanel.btnRooms) {
                            btnRoom.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Object source = e.getSource();
                                    if (source instanceof Component) {
                                        String roomName = (String)btnRoom.getClientProperty("roomName");
                                        for (Room room : client.getRooms()) {
                                            if (room.getName().equals(roomName)) {
                                                sendJoinRoomCommand(roomName);
                                                playGame(roomName);
                                                if (roomPanel != null) {
                                                    frame.setContentPane(roomPanel);
                                                    frame.invalidate();
                                                    frame.validate();
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (roomPanel != null) {
                        Room currentRoom = client.getRoom(roomPanel.getRoomName());
                        roomPanel.updateRoom(currentRoom);
                        switch (currentRoom.getStatus()) {
                            case Room.IS_PLAYABLE:
                                LOG.info("YOU CAN NOW START THE GAME. CLICK THE BOARD ONCE TO START.");
//                                if (JOptionPane.showConfirmDialog(roomPanel, "The game can now be started. Start now?", currentRoom.getName(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                                    sendStartRoomCommand(currentRoom.getName());
//                                }
                                break;
                        }
                        
                        for (JButton[] btnRow : roomPanel.btnCells) {
                            for (JButton btnCell : btnRow) {
                                btnCell.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Object source = e.getSource();
                                        if (source instanceof Component) {
                                            switch (currentRoom.getStatus()) {
                                                case Room.IS_PLAYING:
                                                    JButton button = (JButton) source;
                                                    sendMoveCommand(currentRoom.getName(), (int) button.getClientProperty("row"), (int) button.getClientProperty("col"));
                                                    break;
                                                case Room.IS_PLAYABLE:
                                                    sendStartRoomCommand(currentRoom.getName());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                        
                    }
                    break;
            }
        }
    }
}
