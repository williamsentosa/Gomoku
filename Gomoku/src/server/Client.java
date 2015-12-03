/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.HighScores;
import module.Request;
import module.Response;
import module.Room;
import module.User;


/**
 *
 * @author natanelia
 */
public class Client extends Observable implements Observer  {
    private final String fileName = "scores";
    private Socket socket;
    private User me;
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private HighScores highScores = new HighScores(fileName);
    private Room currentRoom;
    private ClientListener clientListener;
    
    public Client(Socket socket) {
        this.clientListener = new ClientListener(socket);
        this.clientListener.addObserver(this);
        
        this.socket = socket;
        (new Thread(this.clientListener)).start();
    }

    public Client(Socket socket, User me) {
        this.clientListener = new ClientListener(socket, me);
        this.clientListener.addObserver(this);
        
        this.socket = socket;
        this.me = me;
        (new Thread(this.clientListener)).start();
    }
    
    public void sendCommand(String cmdLine) {
        DataOutputStream out;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(cmdLine);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
        setChanged();
        notifyObservers("update-rooms");
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        clientListener.setUser(me);
        this.me = me;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        setChanged();
        notifyObservers();
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
        setChanged();
        notifyObservers();
    }
    
    public void setHighScores(HighScores hs) {
        highScores = hs;
        setChanged();
        notifyObservers();
    }
    
    public ClientListener getClientListener() {
        return clientListener;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == clientListener) {
            Response resp = (Response)arg;
            switch (resp.getCommand()) {
                case "get-room":
                    Room room = (Room)resp.getContent();
                    int i = 0;
                    for (Room r : this.rooms) {
                        if (r.getName().equals(room.getName())) {
                            this.rooms.set(i, room);
                            
                            System.out.println(this.rooms);
                            break;
                        }
                        i++;
                    }
                    setChanged();
                    notifyObservers();
                    break;
                case "get-rooms":
                    this.setRooms((ArrayList<Room>)resp.getContent());
                    System.out.println(this.rooms);
                    break;
                case "get-users":
                    this.setUsers((ArrayList<User>)resp.getContent());
                    System.out.println(this.users);
                    break;
                case "get-high-scores":
                    this.setHighScores((HighScores)resp.getContent());
                    System.out.println(this.highScores);
                    break;
                case "error":
                    System.err.println("ERROR FOUND: " + resp.getContent());
                default:
                    break;
            }
        }
    }
    
    public static void main(String [] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Server name : ");
        String serverName = sc.nextLine();
        System.out.print("Input port : ");
        int port = Integer.parseInt(sc.nextLine());
        System.out.print("Username : ");
        String userName = sc.nextLine();

        try
        {
            Socket socket = new Socket(serverName, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Request req = new Request("login");
            req.addParameter(userName);
            System.out.println(req.toString());
            out.writeUTF(req.toString());

            Client client = new Client(socket, new User(userName));

            while (true) {
                String s = sc.nextLine();

                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(s);
            }
        } catch(IOException e) {

        }
    }
}
