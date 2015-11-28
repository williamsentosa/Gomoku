/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientTest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa
 */
public class Client {
    public static void main (String argv[]) {
       int row, col;
       Scanner sc = new Scanner(System.in);
       
       col = sc.nextInt();
        try {
            Socket clientSocket = new Socket("localhost", 1234);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(col);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("From server : " + in.readLine());
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
