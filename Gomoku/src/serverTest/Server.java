/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTest;

import clientTest.Client;
import game.GomokuGame;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa
 */
public class Server {
    public static void main (String argv[]) {
        int row, col;
        GomokuGame game = new GomokuGame();
        try {
            System.out.println("Start Game");
            ServerSocket socket = new ServerSocket(1234);
            while (true) {
                System.out.println("Waiting for input");
                Socket connectionSocket = socket.accept();
                DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                System.out.println("Check 2");
                System.out.println("From client : " + in.readInt() );
                out.writeBytes("Success");
                connectionSocket.close();
            }
            
        } catch (IOException ex) {
            System.out.println("Gagal");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
