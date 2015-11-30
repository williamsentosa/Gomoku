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
    private String command;
    private Object content;
    private Boolean broadcast;

    public Response(String command, Object content) {
        this.command = command;
        this.content = content;
        this.broadcast = false;
    }
    
    public Response(String command, Object content, Boolean broadcast) {
        this.command = command;
        this.content = content;
        this.broadcast = broadcast;
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

    public Boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        this.broadcast = broadcast;
    }
    
    @Override
    public String toString() {
        return command + " " + content.toString();
    }
}
