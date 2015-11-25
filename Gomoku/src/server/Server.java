/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Request;
import module.Response;
import module.Room;
import module.User;

/**
 * @author Natan
 * Server Codes
 */
public class Server implements Runnable
{
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    
    private Socket clientSocket;
   
    public Server(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        User user = null;
        try
        {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            Request req = new Request(in.readUTF());
            
            user = new User(req.getParameters().get(0));
            users.add(user);
            System.out.println(user.getName() + " connected.\nCurrent Active Players: " + users.size());
            
            Response resp = new Response("login", user);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(resp);
            
            while (true) {
                in = new DataInputStream(clientSocket.getInputStream());
                req = new Request(in.readUTF());
                resp = new Response("error", "unknown");
                
                switch (req.getCommand()) {
                    case "create-room":
                        String roomName = req.getParameters().get(0);
                        
                        boolean found = false;
                        for (Room r : rooms) {
                            if (r.getName().equals(roomName)) {
                                found = true;
                            }
                        }
                        
                        if (!found) {
                            Room room = new Room(roomName);
                            //Current room status is waiting for players
                            rooms.add(room);
                        }
                        
                        resp = new Response("get-rooms", rooms);
                        
                        break;
                    case "join-room":
                        String roomToJoin = req.getParameters().get(0);
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToJoin)) {
                                r.addUser(user);
                                
                                resp = new Response("get-rooms", rooms);
                                break;
                            }
                        }
                        break;
                    case "start-room":
                        String roomToStart = req.getParameters().get(0);
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToStart)) {
                                if (r.getStatus() == Room.IS_PLAYABLE) {
                                    r.setStatus(Room.IS_PLAYING);
                                }
                                
                                resp = new Response("get-rooms", rooms);
                                break;
                            }
                        }
                        break;
                    case "exit-room":
                        String roomToExit = req.getParameters().get(0);
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToExit)) {
                                r.removeUser(user);
                                
                                resp = new Response("get-rooms", rooms);
                                break;
                            }
                        }
                        break;
                    case "move":
                        
                        break;
                    case "get-board":
                        String roomToGet = req.getParameters().get(0);
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToGet)) {
                                resp = new Response("get-gomoku", r.getGomokuGame());
                            }
                        }
                        break;
                    default:
                        
                        resp = new Response("error", "unidentified-request");
                        break;
                }
                
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(resp);
            }
        } catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch(IOException e) {
            if (user != null) {
                users.remove(user);
                System.out.println(user.getName() + " disconnected.\nCurrent Active Players: " + users.size());
            } else {
                e.printStackTrace();
            }
        }
    }
   
    public static void main(String [] args)
    {
        int port = Integer.parseInt(args[0]);

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(30*60*60);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Server(clientSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}