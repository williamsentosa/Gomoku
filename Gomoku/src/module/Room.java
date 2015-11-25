/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import game.GomokuGame;
import java.io.Serializable;
import java.util.ArrayList;

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
        this.status = status;
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }
    
    public void addUser(User user) {
        users.add(user);
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

    public GomokuGame getGomokuGame() {
        return gomokuGame;
    }

    public void setGomokuGame(GomokuGame gomokuGame) {
        this.gomokuGame = gomokuGame;
    }
    
    @Override
    public String toString() {
        return name + " " + status + ", " + gomokuGame + " " + users;
    }
}
