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
    JDialog waitingDialog = new JDialog(frame);
    LoginPanel loginPanel;
    RoomsPanel roomsPanel;
    RoomPanel roomPanel;
    HighScorePanel highScorePanel;
    JButton[][] grid; //Grid untuk papan gomoku
    String backgroundColor = "#f0f5f9";
    OptionPanel optionPanel;
    Client client;
    ActionListener chatListener;

    public UserInterface() {
        frame = new MainFrame("Gomoku"); // Membuat frame
        waitingDialog = new JDialog(frame);
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
        waitingDialog = new JDialog(frame);
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
        client.sendCommand("get-rooms " + "test");
    }
    
    public void sendGetRoomCommand(String roomName) {
        client.sendCommand("get-room " + roomName);
    }
    
    public void sendGetUsersCommand() {
        client.sendCommand("get-users " + "test");
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
    
    public void sendChatCommand(String roomName, String content) {
        client.sendCommand("chat " + roomName + " " + content);
    }
    
    public void sendGetHighScoresCommand() {
        client.sendCommand("get-high-scores " + "test");
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
    
    public void initRooms() {
        for (JButton btnRoom : roomsPanel.btnRooms) {
            btnRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object source = e.getSource();
                    if (source instanceof Component) {
                        String roomName = (String)btnRoom.getClientProperty("roomName");
                        JDialog dialog = new JDialog();
                        dialog.setPreferredSize(new Dimension(550,300));
                        dialog.setLocation(450, 250);
                        client.getRoom(roomName);
                        optionPanel = new OptionPanel(client.getRoom(roomName).getUsers());
                        optionPanel.initComponent();
                        optionPanel.playButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String roomName = (String)btnRoom.getClientProperty("roomName");
                                sendJoinRoomCommand(roomName);
                                playGame(roomName);
                                if (roomPanel != null) {
                                    dialog.dispose();
                                    frame.setContentPane(roomPanel);
                                    frame.invalidate();
                                    frame.validate();
                                }
                            }
                        });
                        optionPanel.exitButton.addActionListener(new ActionListener() { // Watch only
                            public void actionPerformed(ActionEvent e) { 
                                String roomName = (String)btnRoom.getClientProperty("roomName");
                                System.out.println("Room name : " + roomName);
                                System.out.println("User : " + client.getMe().getName());
                                playGame(roomName);
                                if (roomPanel != null) {
                                    dialog.dispose();
                                    roomPanel.updateRoom(client.getRoom(roomName), client.getMe());
                                    frame.setContentPane(roomPanel);
                                    frame.invalidate();
                                    frame.validate();
                                }
                                dialog.dispose();
                            }
                        });
                        dialog.add(optionPanel);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }
            });
        }
         
    }
    
    public void showRooms() {
        roomsPanel = new RoomsPanel(client.getMe(), client.getRooms());
        roomsPanel.btnCreateRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof Component) {
                    String roomName = JOptionPane.showInputDialog("Room Name:");
                    if ((roomName.length() > 0) && (roomName.compareTo("null") != 0)) {
                        sendCreateRoomCommand(roomName);
                    }
                }
            }
        });
        roomsPanel.btnHighScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof Component) {
                    sendGetHighScoresCommand();
                    showHighScores();
                    frame.setContentPane(highScorePanel);
                    frame.invalidate();
                    frame.validate();
                }

            }
            
        });
        initRooms();
        frame.setContentPane(roomsPanel);
    }

    public void showHighScores() {
        highScorePanel = new HighScorePanel(client.getHighScores());
    }
    
    public void playGame(String roomName) {
        roomPanel = new RoomPanel(roomName);
        
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
        
        chatListener = new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof Component) {
                    System.out.println(roomName + " " + roomPanel.chatField.getText());
                    sendChatCommand(roomName, roomPanel.chatField.getText());
                } 
            }
        };
        
        frame.add(roomPanel);
    }
    
    public void initChatActionPerformed() {
        
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
                        initRooms();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (roomPanel != null) {
                        LOG.info("ROOM UPDATE");
                        Room currentRoom = client.getRoom(roomPanel.getRoomName());
                        roomPanel.updateRoom(currentRoom, client.getMe());
                        switch (currentRoom.getStatus()) {
                            case Room.IS_PLAYABLE:
                                waitingDialog.setVisible(false);
                                waitingDialog.dispose();
                                
                                roomPanel.btnExit.setEnabled(true);
                                sendStartRoomCommand(roomPanel.getRoomName());
                                LOG.info("YOU CAN NOW START THE GAME. CLICK THE BOARD ONCE TO START.");
                                break;
                            case Room.IS_WAITING:
                              LOG.info("WATING FOR OTHER PLAYER");
                              
                              roomPanel.btnExit.setEnabled(false);
                              
                              JPanel pnlDialog = new JPanel();
                              waitingDialog.setBackground(Color.decode(backgroundColor));
                              waitingDialog.setPreferredSize(new Dimension(250, 100));
                              waitingDialog.setLocationRelativeTo(frame);
                              
                              JProgressBar bar = new JProgressBar();
                              bar.setIndeterminate(true);
                              bar.setStringPainted(true);
                              bar.setString("Waiting for Other Players");
                              
                              pnlDialog.add(bar);
                              
                              waitingDialog.add(pnlDialog);
                              waitingDialog.pack();
                              waitingDialog.setVisible(true);
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
                                            }
                                        } 
                                    }
                                });
                            }
                            
                            roomPanel.chatField.removeActionListener(chatListener);
                            roomPanel.chatField.addActionListener(chatListener);
                        }
                        roomPanel.btnExit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Object source = e.getSource();
                                if (source instanceof Component) {
                                        if (roomPanel != null) {
                                            sendExitRoomCommand(currentRoom.getName());
                                            showRooms();
                                            roomPanel = null;
                                            frame.setContentPane(roomsPanel);
                                            frame.invalidate();
                                            frame.validate();
                                        }
                                    }
                                }
                        });
                        
                    }
                    break;
                case "update-high-scores":
                    if (highScorePanel != null) {
                        System.out.println("LKJLSDKJF");
                        highScorePanel.initComponent(client.getHighScores());

                        highScorePanel.btnBack.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Object source = e.getSource();
                                if (source instanceof Component) {
                                    frame.setContentPane(roomsPanel);
                                    frame.invalidate();
                                    frame.validate();
                                }
                            }
                        });
                        highScorePanel.revalidate();
                        highScorePanel.repaint();
                    }
                    break;
                default:
                    if (arg.toString().startsWith("error") && roomPanel != null) {
                        roomPanel.lblTurn.setText(arg.toString().replaceFirst("error", ""));
                    }
            }
        }
    }
}
