/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author William Sentosa
 */
public class Position {
    public int row;
    public int col;
    
    public Position() {
        row = 0;
        col = 0;
    }
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
