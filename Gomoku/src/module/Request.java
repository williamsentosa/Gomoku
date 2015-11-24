/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

/**
 *
 * @author natanelia
 */
public class Request {
    private String command;
    private String[] parameters;

    public Request(String req) {
        String[] reqSplitted = req.split(" ");
        this.command = reqSplitted[0];
        this.parameters = req.substring(this.command.length() + 1).split(" ");
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
    
    
}
