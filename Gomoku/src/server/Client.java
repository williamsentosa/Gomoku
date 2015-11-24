/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natanelia
 */
public class Client {
 public static void main(String [] args)
   {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      String userName = args[2];
      String roomName = args[3];
      try
      {
         Socket client = new Socket(serverName, port);
         OutputStream outToServer = client.getOutputStream();
         
         DataOutputStream out = new DataOutputStream(outToServer);
         out.writeUTF("login " + userName + " " + roomName);
         
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         System.out.println(in.readUTF());
         
          try {
              Thread.sleep(5000);
          } catch (InterruptedException ex) {
              Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
          }
         client.close();
      }catch(IOException e) {
         e.printStackTrace();
      }
   }
}
