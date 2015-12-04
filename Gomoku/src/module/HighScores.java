/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa
 */
public class HighScores implements Serializable {
    private List<HighScore> scores; 
    private int defaultScore = 0;
    
    public HighScores(String file) {
        scores = new ArrayList<HighScore>();
        String temp;
        String[] splitted;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((temp = br.readLine()) != null) {
                splitted = temp.split(" ");
                HighScore hs = new HighScore(splitted[0], Integer.parseInt(splitted[1]));
                scores.add(hs);
            }
            br.close();
            sortScores();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HighScores.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HighScores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sortScores() {
        Collections.sort(scores, new Comparator<HighScore>(){
            public int compare(HighScore o1, HighScore o2) {
                return o1.getScore() > o2.getScore() ? -1
                    : o1.getScore() < o2.getScore() ? 1
                    : 0;
            }
        });
    }
    
    public void addHighScore(String user) {
        boolean found = false;
        for(HighScore hs : scores) {
            if(hs.getUser().compareTo(user) == 0) {
                found = true;
            }
        }
        if(!found) {
            HighScore hs = new HighScore(user, defaultScore);
            scores.add(hs);
        }
    }
    
    public void incrHighScore(String user) {
        for(HighScore hs : scores) {
            if(hs.getUser().compareTo(user) == 0) {
                hs.incrScore();
                break;
            }
        }
        sortScores();
    }
    
    public int getSize() {
        return scores.size();
    }
    
    public List<HighScore> getHighScore() {
        return scores;
    }
    
    public void writeToFile(String file) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8"));
            String result = new String();
            for(HighScore hs : scores) {
                writer.write(hs.toString() + "\n");
            }
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HighScores.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HighScores.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HighScores.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    @Override
    public String toString() {
        String result = new String();
        for(HighScore hs : scores) {
            result = result + hs + "\n";
        }
        return result;
    }
    
}
