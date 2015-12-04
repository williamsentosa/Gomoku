/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import game.GomokuGame;
import game.Position;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import module.Room;
import module.User;

/**
 *
 * @author natanelia
 */
public class RoomPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(RoomPanel.class.getName());
    private static final String backgroundColor = "#f0f5f9";
    private static final ArrayList<String> symbols = new ArrayList<String>(Arrays.asList(new String[]{"", "■", "▲", "●", "◆", "❤", "★", "✱", "▼", "♣", "♠"}));
    private int width;
    private int length;
    
    private String roomName;
    
    
    public JPanel pnlBoard = new JPanel(); // Panel untuk btnCells
    public JButton[][] btnCells;
    
    public JButton btnExit;
    
    public JPanel pnlRightSide = new JPanel(); // Panel di sebelah kanan layar
    public JPanel pnlRightSideHeader = new JPanel(); // Panel untuk nama room dan tombol exit room
    public JPanel pnlInfo = new JPanel(); //Panel untuk nama room dan info player
    public JPanel pnlPlayers = new JPanel(); // Panel untuk nama setiap player
    public JLabel[] lblPlayers;
    public JLabel lblTurn;
    public JPanel pnlChat = new JPanel();
    
    public RoomPanel(String roomName) {
        this.roomName = roomName;
        this.width = GomokuGame.size;
        this.length = GomokuGame.size;
        this.setLayout(new FlowLayout(FlowLayout.CENTER)); //Set layout
        this.setPreferredSize(new Dimension(1250, 650));
        this.setBackground(Color.decode(backgroundColor));
        
        // Board Panel
        pnlBoard.setLayout(new GridLayout(width, length));
        pnlBoard.setPreferredSize(new Dimension(700, 650));
        pnlBoard.setBackground(Color.decode(backgroundColor));
        pnlBoard.setOpaque(true);

        // PanelPlayers
        pnlPlayers.setBackground(Color.decode(backgroundColor));
        
        // PanelRightSide
        pnlRightSide.setBorder(BorderFactory.createLineBorder(Color.decode(backgroundColor), 5));
        pnlRightSide.setLayout(new BoxLayout(pnlRightSide, BoxLayout.Y_AXIS));
        
        // PanelInfo
        pnlInfo.setBackground(Color.decode(backgroundColor));
        pnlInfo.setPreferredSize(new Dimension(500, 325));
        
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        ImageIcon icon = createImageIcon("Household-Room-icon-2.png", "Room Logo");

         // Label untuk nama room
        JLabel roomNameLabel = new JLabel(roomName, icon, JLabel.CENTER);
        roomNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomNameLabel.setFont(new Font("Roboto", Font.PLAIN, 30));
        roomNameLabel.setForeground(Color.decode("#2a4d69"));
        
        // Button untuk exit room
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Sniglet", Font.PLAIN, 25));
        btnExit.setOpaque(true);
        btnExit.setBackground(Color.decode("#2a4d69"));
        btnExit.setForeground(Color.decode("#e7eff6"));
        btnExit.setFocusPainted(false);
        
        
        // PanelRightSideHeader
        pnlRightSideHeader.setLayout(new BoxLayout(pnlRightSideHeader, BoxLayout.X_AXIS));
        pnlRightSideHeader.setBackground(Color.decode(backgroundColor));
        pnlRightSideHeader.add(roomNameLabel);
        pnlRightSideHeader.add(Box.createRigidArea(new Dimension(250,0)));
        pnlRightSideHeader.add(btnExit);
        
        pnlInfo.add(pnlRightSideHeader);
        
        lblTurn = new JLabel();
        lblTurn.setFont(new Font("Roboto", Font.PLAIN, 25));
        lblTurn.setForeground(Color.decode("#6e9ec3"));
        lblTurn.setAlignmentY(Component.CENTER_ALIGNMENT);
        lblTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInfo.add(lblTurn);
        pnlInfo.add(pnlPlayers);
        pnlInfo.setAlignmentX(CENTER_ALIGNMENT);
        pnlInfo.setAlignmentY(CENTER_ALIGNMENT);
        
        // pnlChat
        pnlChat.setBackground(Color.red);
        pnlChat.setOpaque(true);
        pnlChat.setPreferredSize(new Dimension(500,325));
        
        
        pnlRightSide.add(pnlInfo);
        pnlRightSide.add(pnlChat);
        // Add pnlBoard and pnlRightSide to JFrame
        this.add(pnlBoard);
        this.add(pnlRightSide);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected ImageIcon createImageIcon(String imgFileName, String description) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("resources/" + imgFileName);
        
        ImageIcon r = null;
        try {
            r = new ImageIcon(ImageIO.read(input));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
    public void updateRoom(Room room, User user) {
        pnlBoard.removeAll();
        btnCells = new JButton[width][length]; //Alokasi ukuran btnCells

        LOG.info("UPDATE ROOM CALLED");
        
        int[][] board = room.getGomokuGame().getBoard();
        
        
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                btnCells[i][j] = new JButton();
                btnCells[i][j].setText(symbols.get(board[i][j]));
                //btnCells[x][y].setBorderPainted(false);
                btnCells[i][j].setOpaque(true);
                btnCells[i][j].setBackground(Color.decode("#f7fafc"));
                btnCells[i][j].setForeground(Color.decode("#2a4d69"));
                btnCells[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
                btnCells[i][j].setFocusPainted(false);
                //btnCells[x][y].setContentAreaFilled(false);
                btnCells[i][j].putClientProperty("row", i);
                btnCells[i][j].putClientProperty("col", j);
                LOG.info("Row : " + Integer.toString(i) + ", Col : " + Integer.toString(j));
                pnlBoard.add(btnCells[i][j]); //Menambah JButton ke btnCells
            }
        }
        
        switch(room.getStatus()) {
            case Room.IS_WON:
                User currentTurnUser = room.getUserOfCurrentTurn();
                LOG.info("Id :" + Integer.toString(room.getTurn() + 1));
                LOG.info("Size :" + Integer.toString(room.getGomokuGame().checkWin(room.getTurn() + 1).size()));
                for (Position p : room.getGomokuGame().checkWin(room.getTurn() + 1)) {
                    btnCells[p.row][p.col].setBackground(Color.green);
                    LOG.info("masuk loop");
                }
                //btnCells[0][0].setBackground(Color.red);
                lblTurn.setText(currentTurnUser.getName() + " won!");
                String msg = "THE GAME HAS BEEN WON by " + currentTurnUser.getName() + "!";
                LOG.info(msg);
                //JOptionPane.showMessageDialog(this, msg, room.getName(), JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        
        pnlBoard.revalidate();
        pnlBoard.repaint();
        
        if (room.getStatus() == Room.IS_PLAYING) {
          lblTurn.setVisible(true);
          try {
            String currentUserTurn = room.getUserOfCurrentTurn().getName();
            if (currentUserTurn.compareTo(user.getName()) == 0) {
              lblTurn.setText("Your turn");
            } else {
              lblTurn.setText(currentUserTurn + "'s turn");
            }
          } catch (NullPointerException e) {
              LOG.log(Level.SEVERE, null, e);
          }
        } else {
          lblTurn.setVisible(false);
        }
        
        pnlPlayers.removeAll();
        lblPlayers = new JLabel[room.getUsers().size()];
        float temp = room.getUsers().size() / 2f;
        pnlPlayers.setLayout(new GridLayout((int) Math.ceil(temp), 2, 0, 0));
        for (int i = 0; i < room.getUsers().size(); i++) {
            ImageIcon icon = createImageIcon("Player1.png", "Player Logo");
            if (user.getName().compareTo(room.getUsers().get(i).getName()) == 0) {
                lblPlayers[i] = new JLabel("You", icon, JLabel.LEFT);
            } else {
                lblPlayers[i] = new JLabel(room.getUsers().get(i).getName(), icon, JLabel.LEFT);
            }
            //lblPlayers[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            //playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
            lblPlayers[i].setFont(new Font("Sniglet", Font.PLAIN, 20));
            lblPlayers[i].setForeground(Color.decode("#2a4d69"));
            
            pnlPlayers.add(lblPlayers[i]);
        }
        pnlPlayers.revalidate();
        pnlPlayers.repaint();
    }
}
