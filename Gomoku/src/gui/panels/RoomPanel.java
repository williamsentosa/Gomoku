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
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    
    public JPanel pnlInfo = new JPanel(); //Panel untuk nama room dan info player
    
    public JPanel pnlPlayers = new JPanel(); // Panel untuk nama setiap player
    public JLabel[] lblPlayers;
    
    public JPanel pnlChat = new JPanel();
    
    public RoomPanel(String roomName) {
        this.roomName = roomName;
        this.width = GomokuGame.size;
        this.length = GomokuGame.size;
        
        this.setLayout(new GridBagLayout()); //Set layout
        GridBagConstraints c = new GridBagConstraints();

        // Board Panel
        pnlBoard.setLayout(new GridLayout(width, length));
        pnlBoard.setPreferredSize(new Dimension(400, 730));
        pnlBoard.setMaximumSize(new Dimension(400, 730));
        pnlBoard.setMinimumSize(new Dimension(400, 730));
        pnlBoard.setBackground(Color.decode(backgroundColor));
        pnlBoard.setOpaque(true);

        // Panel3
        pnlPlayers.setBackground(Color.decode(backgroundColor));
        

        // Panel2
        pnlInfo.setBackground(Color.decode(backgroundColor));
        pnlInfo.setPreferredSize(new Dimension(700, 730));
        pnlBoard.setMaximumSize(new Dimension(700, 730));
        pnlBoard.setMinimumSize(new Dimension(700, 730));
        pnlInfo.setLayout(new GridLayout(4, 1));
        ImageIcon icon = createImageIcon("Household-Room-icon-2.png", "Room Logo");

        JLabel roomNameLabel = new JLabel("  " + roomName, icon, JLabel.CENTER);
        roomNameLabel.setFont(new Font("Roboto", Font.PLAIN, 30));
        roomNameLabel.setForeground(Color.decode("#2a4d69"));
        //roomNameLabel.setHorizontalAlignment(JLabel.CENTER);
        pnlInfo.setPreferredSize(new Dimension(600, 500));
        pnlBoard.setMaximumSize(new Dimension(600, 500));
        pnlBoard.setMinimumSize(new Dimension(600, 500));
        pnlInfo.add(roomNameLabel);
        pnlInfo.add(new JLabel("Player 1's turn"));
        pnlInfo.add(pnlPlayers);
        pnlChat.setBackground(Color.decode(backgroundColor));
        pnlInfo.add(pnlChat);

        // Add pnlBoard and pnlInfo to JFrame
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        //c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(pnlBoard, c);
        c.gridx = 1000;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.8;
        c.fill = GridBagConstraints.BOTH;
        //c.anchor = GridBagConstraints.CENTER;
        this.add(pnlInfo, c);
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
    
    public void updateRoom(Room room) {
        pnlBoard.removeAll();
        btnCells = new JButton[width][length]; //Alokasi ukuran btnCells

        LOG.info("UPDATE ROOM CALLED");
        
        int[][] board = room.getGomokuGame().getBoard();
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                btnCells[x][y] = new JButton();
                btnCells[x][y].setText(symbols.get(board[x][y]));
                //btnCells[x][y].setBorderPainted(false);
                btnCells[x][y].setOpaque(true);
                btnCells[x][y].setBackground(Color.decode("#f7fafc"));
                btnCells[x][y].setForeground(Color.decode("#2a4d69"));
                btnCells[x][y].setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
                btnCells[x][y].setFocusPainted(false);
                //btnCells[x][y].setContentAreaFilled(false);
                btnCells[x][y].putClientProperty("row", x);
                btnCells[x][y].putClientProperty("col", y);
                pnlBoard.add(btnCells[x][y]); //Menambah JButton ke btnCells
            }
        }
        
        switch(room.getStatus()) {
            case Room.IS_WON:
                User currentTurnUser = room.getUserOfCurrentTurn();
                for (Position p : room.getGomokuGame().checkWin(room.getTurn())) {
                    btnCells[p.row][p.col].setBorder(BorderFactory.createLineBorder(Color.decode("#00ff00"), 1));
                    btnCells[p.row][p.col].revalidate();
                    btnCells[p.row][p.col].repaint();
                }
                
                String msg = "THE GAME HAS BEEN WON by " + currentTurnUser.getName() + "!";
                LOG.info(msg);
                JOptionPane.showMessageDialog(this, msg, room.getName(), JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        pnlBoard.revalidate();
        pnlBoard.repaint();
        
        pnlPlayers.removeAll();
        lblPlayers = new JLabel[room.getUsers().size()];
        float temp = room.getUsers().size() / 2f;

        pnlPlayers.setLayout(new GridLayout((int) Math.ceil(temp), 2));
        for (int i = 0; i < room.getUsers().size(); i++) {
            ImageIcon icon = createImageIcon("Player1.png", "Player Logo");
            lblPlayers[i] = new JLabel(room.getUsers().get(i).getName(), icon, JLabel.LEFT);
            lblPlayers[i].setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
            //playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
            lblPlayers[i].setFont(new Font("Sniglet", Font.PLAIN, 20));
            lblPlayers[i].setForeground(Color.decode("#2a4d69"));
            pnlPlayers.add(lblPlayers[i]);
        }
        pnlPlayers.revalidate();
        pnlPlayers.repaint();
    }
}
