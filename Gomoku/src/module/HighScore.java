/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import java.io.Serializable;

/**
 *
 * @author William Sentosa
 */
public class HighScore implements Serializable {
    private int score;
    private String user;
  
    public HighScore(String user, int score) {
        this.score = score;
        this.user = user;
    }
    
    public int getScore() {
        return score;
    }
    
    public String getUser() {
        return user;
    }
    
    public void incrScore() {
        this.score++;
    }
    
    @Override
    public String toString() {
        String result = user + " " + score;
        return result;
    }
}
