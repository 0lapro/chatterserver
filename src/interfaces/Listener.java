/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatterserver.src.interfaces;

/**
 * Listener is an interface for classes that wish to receive new chat
 messages.
 * 
 * Listens to chat messages coming from clients.
 *
 * @author 0laprogrmr@gmail.com
 */
public interface Listener {
    void receiveMessage(String from, String message);// receive new chat message
} // end interface Listener
