/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Observer;
import java.util.Observable;
import module.Request;
import module.User;

/**
 * @author Natan
 * Server Codes
 */
public class Server implements Runnable
{
    public static int activePlayer = 0;
    
    private Socket clientSocket;
    private User user;
   
    public Server(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            Request req = new Request(in.readUTF());
            String resp = "success connection-established";
            
            user = new User(req.getParameters()[0], req.getParameters()[1]);
            System.out.println("ROOM " + user.getRoom() + ": " + user.getName() + " connected.\nCurrent Active Players: " + ++activePlayer);
            
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF(resp);
            
            while (true) {
                in = new DataInputStream(clientSocket.getInputStream());
                req = new Request(in.readUTF());
                resp = "SUCCESS";
                
                if (req.getCommand().equals("create-room")) {
                    
                } else if (req.getCommand().equals("join-room")) {
                    
                } else if (req.getCommand().equals("start-room")) {
                    
                } else if (req.getCommand().equals("exit-room")) {
                    
                } else if (req.getCommand().equals("move")) {
                    
                } else {
                    resp = "error unidentified-command";
                }

                out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeUTF(resp);
            }
        } catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch(IOException e) {
            System.out.println("ROOM " + user.getRoom() + ": " + user.getName() + " disconnected.\nCurrent Active Players: " + --activePlayer);
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