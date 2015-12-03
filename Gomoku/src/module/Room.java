/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import game.GomokuGame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author natanelia
 */
public class Room implements Serializable {
    public static final int MINIMUM_PLAYABLE_USER = 3;
    public static final int IS_WAITING = 0;
    public static final int IS_PLAYABLE = 1;
    public static final int IS_PLAYING = 2;
    public static final int IS_WON = 3;
    public static final int IS_STOPPED = 4;
    
    private String name;
    private GomokuGame gomokuGame = new GomokuGame();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Chat> chats = new ArrayList<>();
    
    private int turn = 0;
    private int status = IS_WAITING;
    
    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (this.status != status) {
            if (status == IS_PLAYING) {
                Random r = new Random();
                turn = r.nextInt(users.size());
            }
            this.status = status;
        }
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }
    
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
        if (users.size() >= MINIMUM_PLAYABLE_USER && this.status == IS_WAITING) {
            this.status = IS_PLAYABLE;
        }
    }
    
    public void removeUser(User user) {
        users.remove(user);
        if (users.size() < MINIMUM_PLAYABLE_USER && this.status == IS_PLAYABLE) {
            this.status = IS_WAITING;
        }
    }
    
    public void addChat(Chat chat) {
        if (users.contains(chat.getUser())) {
            chats.add(chat);
        }
    }
    
    public void removeChat(Chat chat) {
        chats.remove(chat);
    }
    
    public void getChatText() {
        String r = "";
        for (Chat chat : chats) {
            r += "\n" + chat.toString();
        }
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public GomokuGame getGomokuGame() {
        return gomokuGame;
    }

    public void setGomokuGame(GomokuGame gomokuGame) {
        this.gomokuGame = gomokuGame;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
    
    public void nextTurn() {
        this.turn = (turn + 1) % users.size();
    }
    
    public User getUserOfCurrentTurn() {
        return users.get(turn);
    }
    
    @Override
    public String toString() {
        return name + " " + status + " " + turn + ", " + gomokuGame + " " + users;
    }
}
