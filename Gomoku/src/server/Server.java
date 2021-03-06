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
import module.HighScores;
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
    private static final String fileName = "scores";
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static HighScores highScores = new HighScores(fileName);
    
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
                                if (r.getStatus() == Room.IS_PLAYABLE || r.getStatus() == Room.IS_PLAYING) {
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
                        boolean foundInRoom = false;
                        for (Room room : rooms) {
                            if (room.getUsers().contains(user)) {

                                foundInRoom = true;
                            }
                        }
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToExit)) {
                                try {
                                    if (r.getUserOfCurrentTurn().getId() == user.getId()) {
                                        r.nextTurn();
                                    }
                                } catch (java.lang.IndexOutOfBoundsException e) {
                                    r.nextTurn();
                                }
                                r.removeUser(user);

                                if (r.getUsers().size() == 0) {
                                    r.getGomokuGame().resetBoard();
                                    r.setStatus(Room.IS_WAITING);
                                }
                                
                                resp = new Response("get-rooms", rooms, true);
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
                                    if (user.equals(r.getUserOfCurrentTurn())) {
                                        if (r.getGomokuGame().getBoard()[row][col] == GomokuGame.defaultId) {
                                            List<Position> result = r.getGomokuGame().insertToBoard(r.getTurn() + 1, new Position(row, col));
                                            boolean finished = result.size() >= 5;
                                            if (finished) {
                                                highScores.addHighScore(user.getName());
                                                highScores.incrHighScore(user.getName());
                                                highScores.writeToFile(fileName);
                                                r.setStatus(Room.IS_WON);
                                            } else {
                                                r.nextTurn();
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
                        String content = new String();
                        for(int i=1; i<req.getParameters().size(); i++) {
                            content = content + req.getParameters().get(i) + " ";
                        }        
                        resp = new Response("error", "room not found");
                        for (Room r : rooms) {
                            if (r.getName().equals(roomToChat)) {
                                r.addChat(new Chat(user, content));
                                
                                resp = new Response("get-room", r, true);
                            }
                        }
                        break;
                    case "get-high-scores":
                        resp = new Response("get-high-scores", highScores);
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
            if (user != null) {
                users.remove(user);
                
                boolean foundInRoom = false;
                for (Room room : rooms) {
                    if (room.getUsers().contains(user)) {
                        if (room.getUserOfCurrentTurn().getId() == user.getId()) {
                            room.nextTurn();
                        }
                        room.getUsers().remove(user);
                        
                        if (room.getUsers().size() == 0) {
                            room.getGomokuGame().resetBoard();
                            room.setStatus(Room.IS_WAITING);
                        }
                        
                        foundInRoom = true;
                    }
                }
                
                allSockets.remove(clientSocket);
                if (foundInRoom) {
                    Response resp = new Response("get-rooms", rooms, true);
                    for (Socket socket : allSockets) {
                        try {
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                            out.writeObject(resp);
                        } catch (IOException ex) {
                            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                Response resp = new Response("get-users", users, true);
                for (Socket socket : allSockets) {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(resp);
                    } catch (IOException ex) {
                        //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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