/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Response;
import module.Room;
import module.User;

/**
 *
 * @author natanelia
 */
public class ClientListener extends Observable implements Runnable {
    private Socket socket;
    private User user;
    
    public ClientListener(Socket serverSocket, User user) {
        this.socket = serverSocket;
        this.user = user;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Response resp = (Response)in.readObject();
                setChanged();
                this.notifyObservers(resp);
//                switch (resp.getCommand()) {
//                    case "get-gomoku":
//                        setChanged();
//                        this.notifyObservers(resp);
//                        break;
//                    case "get-rooms":
//                        this.notifyObservers(resp);
//                        break;
//                    case "get-users":
//                        this.notifyObservers(resp);
//                        break;
//                    default:
//                        this.notifyObservers(resp);
//                        break;
//                }
            } catch (ClassNotFoundException | IOException e) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, e);

            }
        }
    }
    
}
