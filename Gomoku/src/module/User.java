/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import java.io.Serializable;

/**
 *
 * @author natanelia
 */
public class User implements Serializable {
    private static int userCount = 0;
    private final int id;
    private String name;
    private String room;

    public User(String name) {
        this.id = userCount++;
        this.name = name;
        this.room = "";
    }
    
    public User(String name, String room) {
        this.id = userCount++;
        this.name = name;
        this.room = room;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public static int getUserCount() {
        return userCount;
    }

    public static void setUserCount(int userCount) {
        User.userCount = userCount;
    }
    
    @Override
    public String toString() {
        return id + " " + name;
    }
}
