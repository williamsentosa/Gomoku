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
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import module.Room;
import module.User;

/**
 *
 * @author natanelia
 */
public class RoomsPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(RoomsPanel.class.getName());
    private static final String backgroundColor = "#f0f5f9";

    public JButton[] btnRooms;
    public JPanel pnlRooms;
    public JButton btnCreateRoom;
    
    public RoomsPanel(User user, ArrayList<Room> roomList) {
        super();
        
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode(backgroundColor));
        GridBagConstraints c = new GridBagConstraints();

        // JButton untuk membuat room baru
        btnCreateRoom = new JButton("Create new room");
        btnCreateRoom.setFont(new Font("Sniglet", Font.PLAIN, 25));
        btnCreateRoom.setOpaque(true);
        btnCreateRoom.setBackground(Color.decode("#2a4d69"));
        btnCreateRoom.setForeground(Color.decode("#e7eff6"));

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(50, 50, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(btnCreateRoom, c);

        // Label untuk menampilkan nama player
        JLabel label = new JLabel("Welcome, " + user.getName() + "!");
        label.setFont(new Font("Sniglet", Font.PLAIN, 25));
        label.setForeground(Color.decode("#2a4d69"));
        c.gridx = 2;
        c.gridy = 0;

        c.insets = new Insets(50, 0, 0, 50);
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        //c.fill = GridBagConstraints.BOTH;
        this.add(label, c);

        // JPanel untuk menampilkan rooms
        pnlRooms = new JPanel();
        pnlRooms.setBackground(Color.decode(backgroundColor));
        pnlRooms.setLayout(new GridLayout(1, 20, 50, 0));
        pnlRooms.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        
        updateRooms(roomList);
        
        JScrollPane scrollPane = new JScrollPane(pnlRooms);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //scrollPane.setBounds(50, 30, 300, 50);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, c);
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
    
    public void updateRooms(ArrayList<Room> rooms) {
        pnlRooms.removeAll();
        
        btnRooms = new JButton[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            ImageIcon icon = createImageIcon("Household-Room-icon-2.png", "Room Logo");
            btnRooms[i] = new JButton(rooms.get(i).getName(), icon);
            btnRooms[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            btnRooms[i].setHorizontalTextPosition(SwingConstants.CENTER);
            btnRooms[i].setFont(new Font("Sniglet", Font.PLAIN, 25));
            btnRooms[i].setMinimumSize(new Dimension(200, 300));
            btnRooms[i].setMaximumSize(new Dimension(200, 300));
            btnRooms[i].setPreferredSize(new Dimension(200, 300));
            btnRooms[i].setOpaque(true);
            btnRooms[i].setBackground(Color.decode("#f7fafc"));
            btnRooms[i].setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 3));
            btnRooms[i].setForeground(Color.decode("#2a4d69"));
            btnRooms[i].putClientProperty("roomName", rooms.get(i).getName());
            pnlRooms.add(btnRooms[i]);
        }
        
        pnlRooms.revalidate();
        pnlRooms.repaint();
    }
}
