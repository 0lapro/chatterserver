/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author 0laprogrmr@gmail.com
 * SocketMessengerConstants defines constants for the port numbers
 * and multi-cast address in DeitelMessenger
 */

public interface SocketMessengerConstants 
{   
   // address for multicast datagrams
   public static final String MULTICAST_ADDRESS = "239.0.0.1";
   
   // port for listening for multicast datagrams
   public static final int MULTICAST_LISTENING_PORT = 1111;
   
   // port for sending multicast datagrams
   public static final int MULTICAST_SENDING_PORT = 1112;
   
   // port for Socket connections to OlasMessenger
   public static final int SERVER_PORT = 65000;   
   
   // String that indicates disconnect
   public static final String DISCONNECT_STRING = "DISCONNECT";

   // String that separates the user name from the message body
   public static final String MESSAGE_SEPARATOR = ">>>";

   // message size (in bytes)
   public static final int MESSAGE_SIZE = 512;
} // end interface SocketMessengerConstants

