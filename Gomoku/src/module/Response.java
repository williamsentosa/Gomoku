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
public class Response implements Serializable {
    public String command;
    public Object content;

    public Response(String command, Object content) {
        this.command = command;
        this.content = content;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public Object getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return command + " " + content.toString();
    }
}
