/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import module.Room;
import server.Client;

/**
 *
 * @author natanelia
 */
public class MainFrame extends JFrame implements WindowListener {
    private Client client;
    
    public MainFrame() throws HeadlessException {
        
    }

    public MainFrame(String title) throws HeadlessException {
        super(title);
    }
    
    public void registerClient(Client client) {
        this.client = client;
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

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
