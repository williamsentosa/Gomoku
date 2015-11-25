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
import module.Request;
import module.Response;
import module.Room;
import module.User;


/**
 *
 * @author natanelia
 */
public class Client implements Observer {
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private Room currentRoom;
    private ClientListener clientListener;

    public Client(ClientListener clientListener) {
        this.clientListener = clientListener;
        (new Thread(this.clientListener)).start();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public ClientListener getClientListener() {
        return clientListener;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == clientListener) {
            Response resp = (Response)arg;
            switch (resp.getCommand()) {
                case "get-gomoku":
                    break;
                case "get-rooms":
                    this.rooms = (ArrayList<Room>)resp.getContent();
                    System.out.println(this.rooms);
                    break;
                case "get-users":
                    this.users = (ArrayList<User>)resp.getContent();
                    System.out.println(this.users);
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void main(String [] args)
      {
         String serverName = args[0];
         int port = Integer.parseInt(args[1]);
         String userName = args[2];
         

         try
         {
               Socket socket = new Socket(serverName, port);
               DataOutputStream out = new DataOutputStream(socket.getOutputStream());

               Request req = new Request("login");
               req.addParameter(userName);
               System.out.println(req.toString());
               out.writeUTF(req.toString());

               ClientListener clientListener = new ClientListener(socket, new User(userName));
               Client client = new Client(clientListener);
               
               clientListener.addObserver(client);

               Scanner sc = new Scanner(System.in);
               while (true) {
                   String s = sc.nextLine();
                   
                   out = new DataOutputStream(socket.getOutputStream());
                   out.writeUTF(s);
               }
         } catch(IOException e) {

         }
      }
}
