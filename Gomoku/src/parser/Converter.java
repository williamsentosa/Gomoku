/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertr;

import game.Position;
import java.util.Scanner;

/**
 * Membangun kelas converter untuk mengubah tipe menjadi string 
 * agar mudah dikirimkan melalui socket.
 * @author William Sentosa
 */
public class Converter {
    /* Identifier */
    private final int matrixIdentifier = 1;
    private final int positionIdentifier = 2;
    
    public Converter() {
        // do nothing
    }
    
    /**
     * Mengubah matrix menjadi string
     * @param m matrix yang ingin diubah
     * @param size ukuran matrix
     * @return String hasil pengubahan
     */
    public String convertMatrixToString(int[][] m, int size) {
        String result = new String();
        result = result + matrixIdentifier + ", ";
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                result = result + m[i][j] + ", ";
            }
        }
        return result;
    }
    
    /**
     * Mengecek apabila s merupakan sebuah string hasil conversion dari matrix
     * @param s String yang ingin dicek
     * @return true bila s merupakan string hasil conversion dari matrix dan false bila tidak
     */
    public boolean isStringMatrix(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter("(, *)*");
        int id = sc.nextInt();
        sc.close();
        return id == matrixIdentifier; 
    }
    
    /**
     * Mengubah String menjadi matrix
     * @param s String yang ingin diubah
     * @param size ukuran matrix
     * @return matrix hasil pengubahan
     */
    public int[][] convertStringToMatrix(String s, int size) {
        int[][] result = new int[size][size];
        Scanner sc = new Scanner(s);
        sc.useDelimiter("(, *)*");
        try{
            if (sc.nextInt() == matrixIdentifier) { // check apakah String s bisa menjadi matrix atau tidak
                for(int i=0; i<size; i++) {
                    for(int j=0; j<size; j++) {
                        result[i][j] = sc.nextInt();
                    }
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("reading is completed");
        }
        return result;
    }
    
    /**
     * Mengecek apakah string s merupakan string hasil conversion dari posisi yang 
     * terdiri dari 3 elemen: userId, row, col
     * @param s String yang ingin dicek
     * @return true bila benar dan false bila tidak
     */
    public boolean isStringPosition(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter("(, *)*");
        int id = sc.nextInt();
        sc.close();
        return id == positionIdentifier;
    }
    
    /**
     * Mengubah posisi menjadi matrix dengan input userId, row dan col
     * @param userId identification dari user yang memasukan posisi tersebut
     * @param row baris pada board
     * @param col kolom pada board
     * @return String hasil pengubahan
     */
    public String convertPositionToString(int userId, int row, int col) {
        String result = new String();
        result = positionIdentifier + ", " + userId + ", " + row + ", " + col + ", ";
        return result;
    }
    
    /**
     * Mengubah String menjadi posisi yang terdiri dari 3 elemen: userId, row, dan col
     * @param s String yang ingin diubah
     * @return array yagn berisi 3 elemen: userId, row, dan col
     */
    public int[] convertStringToPosition(String s) {
        int[] result = new int[3];
        Scanner sc = new Scanner(s);
        sc.useDelimiter("(, *)*");
        try{
            if (sc.nextInt() == positionIdentifier) { // check apakah String s bisa menjadi matrix atau tidak
                for(int i=0; i<3; i++) {
                   result[i] = sc.nextInt();
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("reading is completed");
        }
        return result;
    }
    
    public static void main (String argv[]) {
       int id = 2;
       int row = 3;
       int col = 5;
       String s = new String();
       Converter convertr = new Converter();
       s = convertr.convertPositionToString(id, row, col);
       System.out.println(s);
       int[] result = convertr.convertStringToPosition(s);
       for(int i =0; i< 3; i++) {
           System.out.println(result[i]);
       }
    }
    
}
