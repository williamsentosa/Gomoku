/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.*;

/**
 * @author William Sentosa
 * Membuat kelas Gomoku game
 */
public class GomokuGame {
    private final int size = 8;
    private final int defaultId = 0; // Isi dari setiap matrix pada awalnya
    private final int threshold = 5;
    private int[][] board; // Terdiri dari matrix of id pemain.
    
    public GomokuGame() {
        board = new int [20][20];
        resetBoard();
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getSize() {
        return size;
    }
    
    public void resetBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                board[i][j] = defaultId;
            }
        }
    }
    
    public void displayBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
    
    public void displayGameBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(board[i][j] == defaultId) {
                    System.out.print( "[" + i + "," + j + "]" + "   ");
                } else {
                    System.out.print("  " + board[i][j] + " " + "    ");
                }
                
            }
            System.out.print("\n");
        }
    }
    
    private boolean isIdxValid(int idx) {
        return idx >= 0 && idx < 20;
    }
    
    /**
     * menghitung jumlah elemen secara vertikal yang sama dengan id
     * @param id identifier yang ingin dicari jumlahnya
     * @param p posisi identifier tersebut
     * @return list of position untuk id yang sama
     */
    private List<Position> checkVertically(int id, Position p) {
        boolean found;
        int i, increment;
        List<Position> result = new ArrayList<Position>();
        result.add( new Position(p.row,p.col));
        increment = 1;
        i = p.row + increment;
        found = false;
        for(int j = 0 ; j < 2; j++) {
            while( isIdxValid(i) && !found) {
                if( board[i][p.col] != id ) {
                    found = true;
                } else {
                    result.add( new Position(i, p.col) );
                }
                i = i + increment;
            }
            increment = -1;
            i = p.row + increment;
            found = false;
        }
        return result;
    }
    
    /**
     * menghitung jumlah elemen secara horizontal yang sama dengan id
     * @param id identifier yang ingin dicari jumlahnya
     * @param p posisi identifier tersebut
     * @return list of point untuk id yang sama
     */
    private List<Position> checkHorizontally(int id, Position p) {
        boolean found;
        int i, increment;
        List<Position> result = new ArrayList<Position>();
        result.add( new Position(p.row,p.col));
        increment = 1;
        i = p.col + increment;
        found = false;
        for(int j = 0 ; j < 2; j++) {
            while( isIdxValid(i) && !found) {
                if( board[p.row][i] != id ) {
                    found = true;
                } else {
                    result.add( new Position(p.row, i) );
                }
                i = i + increment;
            }
            increment = -1;
            i = p.col + increment;
            found = false;
        }
        return result;
    }
    
    /**
     * menghitung jumlah elemen secara diagonal menurun yang sama dengan id
     * @param id identifier yang ingin dicari jumlahnya
     * @param p posisi identifier tersebut
     * @return list of point untuk id yang sama
     */
    private List<Position> checkDecreasingDiagonally(int id, Position p) {
        boolean found;
        int col, row, increment;
        List<Position> result = new ArrayList<Position>();
        result.add( new Position(p.row,p.col));
        increment = 1;
        row = p.row + increment;
        col = p.col + increment; 
        found = false;
        for(int i = 0 ; i < 2; i++) {
            while( isIdxValid(col) && isIdxValid(row) && !found) {
                if( board[row][col] != id ) {
                    found = true;
                } else {
                    result.add( new Position(row, col) );
                }
                row = row + increment;
                col = col + increment;
            }
            increment = -1;
            row = p.row + increment;
            col = p.col + increment; 
            found = false;
        }
        return result;
    }
    
    /**
     * menghitung jumlah elemen secara diagonal menurun yang sama dengan id
     * @param id identifier yang ingin dicari jumlahnya
     * @param p posisi identifier tersebut
     * @return list of point untuk id yang sama
     */
    private List<Position> checkIncreasingDiagonally(int id, Position p) {
        boolean found;
        int col, row, increment;
        List<Position> result = new ArrayList<Position>();
        result.add( new Position(p.row,p.col));
        increment = 1;
        row = p.row - increment;
        col = p.col + increment; 
        found = false;
        for(int i = 0 ; i < 2; i++) {
            while( isIdxValid(col) && isIdxValid(row) && !found) {
                if( board[row][col] != id ) {
                    found = true;
                } else {
                    result.add( new Position(row, col) );
                }
                row = row - increment;
                col = col + increment;
            }
            increment = -1;
            row = p.row - increment;
            col = p.col + increment; 
            found = false;
        }
        return result;
    }
    
    /**
     * Menaruh id(Batu pengguna) pada board
     * @param id identitas dari pengguna
     * @param p posisi board yang ingin diletakkan batu
     * @return List kosong bila penambahan tersebut tidak menghasilkan kemenangan dan List yang berisi 
     * posisi letak batu yang membawa kemenangan.
     */
    public List<Position> insertToBoard(int id, Position p) {
        int i, temp;
        List<Position> result;
        board[p.row][p.col] = id;
        if ( checkVertically(id,p).size() >= 5 ) {
            result = checkVertically(id,p);
        } else if ( checkHorizontally(id,p).size() >= 5) {
            result = checkHorizontally(id,p);
        } else if ( checkIncreasingDiagonally(id,p).size() >= 5) {
            result = checkIncreasingDiagonally(id,p);
        } else if ( checkDecreasingDiagonally(id,p).size() >= 5) {
            result = checkDecreasingDiagonally(id,p);
        } else {
            result = new ArrayList<Position>();
        }
        return result;
    }
    
    public static void main(String[] args) {
        GomokuGame game = new GomokuGame();
        game.displayGameBoard();
        Scanner sc = new Scanner(System.in);
        int row, col;
        boolean finished = false;
        int id = 0;
        List<Position> result = new ArrayList<Position>();
        int player = 3;
        while(!finished) {
            id = (id % player) + 1;
            System.out.print("Pemain " + id + " input : ");
            row = sc.nextInt();
            col = sc.nextInt();
            result = game.insertToBoard(id, new Position(row, col));
            game.displayGameBoard();
            finished = result.size() >= 5;
        }
        System.out.println("Pemenangnya adalah Player " + id);
        System.out.println("Langkah menangnya : ");
        for(Position p : result) {
            System.out.println("[" + p.row + "," + p.col + "]");
        }
    }
    
}
