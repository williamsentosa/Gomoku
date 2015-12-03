/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import java.util.ArrayList;

/**
 *
 * @author natanelia
 */
public class Request {
    private String command;
    private ArrayList<String> parameters = new ArrayList<>();

    public Request(String req) {
        if (req.contains(" ")) {
            String[] reqSplitted = req.split(" ");
            this.command = reqSplitted[0];

            String[] paramSplitted = req.substring(this.command.length() + 1).split(" ");
            for (String s : paramSplitted) {
                parameters.add(s);
            }
        } else {
            this.command = req;
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    public ArrayList<String> getParameters() {
        return parameters;
    }
    
    public void addParameter(String param) {
        parameters.add(param);
    }
    
    public void removeParameter(int index) {
        parameters.remove(index);
    }
    
    @Override
    public String toString() {
        String params = "";
        for (String s : this.getParameters()) {
            params += s + " ";
        }
        return this.command + " " + params.trim();
    }
}
