package chatterserver.src.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;
import static chatterserver.src.interfaces.Constants.*;
import chatterserver.src.server.ChatServer;
import chatterserver.src.interfaces.Listener;

/**
 * Used by the server. Receiver is a Runnable that listens for messages 
 * from a particular client and delivers messages to a Listener.
 *
 * @author 0laprogrmr@gmail.com
 */
public class Receiver implements Runnable {

    private BufferedReader input;
    private Listener messageListener;
    private boolean listening = true; // when false, ends runnable
    private ChatServer server;

    /**
     * MessageReceiver constructor Creates a new task|runnable|job with the
     * necessary tools needed to run the task.
     *
     * @param listener : one of the tools needed, a Listener object.
     * @param clientSocket : another tool needed, a Socket object.
     */
    public Receiver(Listener listener, Socket clientSocket) {
        server = new ChatServer();
        messageListener = listener;//set listener to which new messages should be sent
        try {
            clientSocket.setSoTimeout(5000);//set five seconds timeout for reading from client
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//create BufferedReader for reading incoming messages
        }//end try
        catch (IOException iOe) {
            iOe.getMessage();
        } // end catch
    } // end Receiver constructor

    /**
     * Listens for new messages and deliver them to Listener.
     */
    @Override
    public void run() {
        String message;//String for incoming messages

        while (listening) {//listen for client messages until stopped
            try {
                message = input.readLine();//Read a line of text and returns a String containing the contents of the line, not including any line-termination characters, or null if the end of the stream has been reached
            }//end try
            catch (SocketTimeoutException sTe) {
                sTe.getMessage();
                continue; // continue to next iteration to keep listening
            } // end catch
            catch (IOException iOe) {
                iOe.getMessage();
                break;
            }//end catch

            if (message != null) {// ensure non-empty readings
                StringTokenizer tokenizer = new StringTokenizer(message, MESSAGE_SEPARATOR);// tokenize message to retrieve user name and message body
                if (tokenizer.countTokens() == 2) {//ignore messages that do not contain a username and message body
                    messageListener.receiveMessage(tokenizer.nextToken(), tokenizer.nextToken());//receive the username and message body tokens respectively
                    getServer().setMsgComing(true);//alert server when there is incoming message.
                }//end if
                else {
                    if (message.equalsIgnoreCase(MESSAGE_SEPARATOR + DISCONNECT_STRING)) {//if disconnect message received, stop listening
                        stopListening();
                    }
                }//end else
            }//end if
        }//end while  
        //if not listening
        try {
            input.close(); // close BufferedReader (also closes Socket)/maybe it should do this.
        }   // end try
        catch (IOException iOe) {
            iOe.getMessage();
        } // end catch 
    } // end method run

    /**
     * Stops listening for incoming messages.
     */
    public void stopListening() {
        listening = false;
    } // end method stopListening

    public ChatServer getServer() {
        return server;
    }

    public void setServer(ChatServer server) {
        this.server = server;
    }
} // end class Receiver
