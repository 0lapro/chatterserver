/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatterserver.src.runner;

/**
 * Used by the server to send multicasts.
 *
 * @author 0laprogrmr@gmail.com
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static chatterserver.src.interfaces.Constants.*;

public class Multicaster implements Runnable {

    private final byte[] messageBytes; // message data array

    /**
     * Constructs new object of this class with the message to send.
     *
     * @param bytes
     */
    public Multicaster(byte[] bytes) {
        messageBytes = bytes;//create the message
    }//end Multicaster constructor

    /**
     * The actual task to perform.
     * Delivers message to MULTICAST_ADDRESS over
     * DatagramSocket.
     */
    @Override
    public void run() {
        try {try ( // deliver message
                DatagramSocket datagramSocket = new DatagramSocket(MULTICAST_SENDING_PORT) // create DatagramSocket for sending message
            ) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);// use InetAddress reserved for multicast group
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_LISTENING_PORT);// create DatagramPacket containing message
            datagramSocket.send(packet); //send packet to multicast group
            } // use InetAddress reserved for multicast group
        } // end try // end try
        catch (IOException ioException) {
            ioException.getMessage();
        } // end catch
    } // end method run
} // end class Multicaster
