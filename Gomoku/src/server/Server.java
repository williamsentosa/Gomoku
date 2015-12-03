/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import game.GomokuGame;
import game.Position;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Chat;
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
    private ArrayList<Socket> allSockets;
   
    public Server(Socket clientSocket, ArrayList<Socket> allSockets) throws IOException
    {
        this.clientSocket = clientSocket;
        this.allSockets = allSockets;
    }

    @Override
    public void run()
    {
        User user = null;
        try
        {     
            
            while (true) {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                Request req = new Request(in.readUTF());
                Response resp = new Response("error", "unknown");
                
                switch (req.getCommand()) {
                    case "login":
                        user = new User(req.getParameters().get(0));
                        users.add(user);
                        System.out.println(user.getName() + " connected.\nCurrent Active Players: " + users.size());

                        resp = new Response("login", user);
                        break;
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
                            resp = new Response("get-rooms", rooms, true);
                        } else {
                            resp = new Response("error", "room with same name is exist");
                        }
                        
                        break;
                    case "join-room":
                        String roomToJoin = req.getParameters().get(0);
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToJoin)) {
                                r.addUser(user);
                                
                                resp = new Response("get-rooms", rooms, true);
                                break;
                            }
                        }
                        break;
                    case "start-room":
                        String roomToStart = req.getParameters().get(0);
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToStart)) {
                                if (r.getStatus() == Room.IS_PLAYABLE) {
                                    r.setStatus(Room.IS_PLAYING);
                                    resp = new Response("get-rooms", rooms, true);
                                } else {
                                    resp = new Response("error", "room is not playable");
                                }
                                
                                break;
                            }
                        }
                        break;
                    case "exit-room":
                        String roomToExit = req.getParameters().get(0);
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToExit)) {
                                r.removeUser(user);
                                
                                resp = new Response("get-rooms", r, true);
                                break;
                            }
                        }
                        break;
                    case "move":
                        roomName = req.getParameters().get(0);
                        int row = Integer.parseInt(req.getParameters().get(1));
                        int col = Integer.parseInt(req.getParameters().get(2));
                        
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomName)) {
                                if (r.getStatus() == Room.IS_PLAYING) {
                                    if (user.getId() == r.getTurn()) {
                                        if (r.getGomokuGame().getBoard()[row][col] == GomokuGame.defaultId) {
                                            List<Position> result = r.getGomokuGame().insertToBoard(user.getId() + 1, new Position(row, col));
                                            r.nextTurn();

                                            boolean finished = result.size() >= 5;
                                            if (finished) {
                                                r.setStatus(Room.IS_WON);
                                            }

                                            resp = new Response("get-room", r, true);
                                        } else {
                                            resp = new Response("error", "pressed on filled cell");
                                        }
                                    } else {
                                        resp = new Response("error", "not your turn");
                                    }
                                } else {
                                    resp = new Response("error", "room is not playing");
                                }
                                break;
                            }
                        }
                        break;
                    case "get-rooms":
                        resp = new Response("get-rooms", rooms);
                        break;
                    case "get-room":
                        String roomToGet = req.getParameters().get(0);
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToGet)) {
                                resp = new Response("get-room", r);
                            }
                        }
                        break;
                    case "get-users":
                        resp = new Response("get-users", users);
                        break;
                    case "chat":
                        String roomToChat = req.getParameters().get(0);
                        String content = req.getParameters().get(1);
                        
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToChat)) {
                                r.addChat(new Chat(user, content));
                                
                                resp = new Response("get-room", r, true);
                            }
                        }
                        break;
                    default:
                        resp = new Response("error", "unidentified-request");
                        break;
                }
                
                ObjectOutputStream out;
                if (!resp.isBroadcast()) {
                    out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject(resp);
                } else {
                    for (Socket socket : allSockets) {
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(resp);
                    }
                }
            }
        } catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch(IOException e) {
            e.printStackTrace();
            if (user != null) {
                users.remove(user);
                allSockets.remove(clientSocket);
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println(user.getName() + " disconnected.\nCurrent Active Players: " + users.size());
            } else {
                //e.printStackTrace();
            }
        }
    }
   
    public static void main(String [] args)
    {
        int port = Integer.parseInt(args[0]);

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(30*60*60*1000);
            
            ArrayList<Socket> clientSockets = new ArrayList<>();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(30*60*60*1000);
                clientSockets.add(clientSocket);
                new Thread(new Server(clientSocket, clientSockets)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}