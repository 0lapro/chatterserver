package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import interfaces.MessageListener;
import static interfaces.SocketMessengerConstants.*;
import runner.MessageReceiver;
import runner.MulticastSender;

/**
 * The messenger server which also is-a listener, is a multi-threaded, socket and
 * packet based chat server that listens constantly for and receives incoming 
 * client messages, and multicasts it to all clients including the sender.
 * 
 * @author 0laprogrmr@gmail.com
 */
public class MessengerServer implements MessageListener//, Serializable
{
    private static final long serialVersionUID = 1L;
    private ExecutorService serverExecutor; //The Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
    private ServerSocket serverSocket;//The server socket that waits for client requests to come in over the network.
    private Socket clientSocket;//The client socket (called just socket) is the server endpoint used for communicating with the client.
    private String completeMessage;
    private boolean serverUp;
    private boolean serverStarted;
    private boolean serverStopped;
    private boolean serverClosed;
    private boolean serverOpened;
    private boolean msgComing;
//    private Queue<String> msgQueue;

    /**
     * Starts the chat messenger server.
     * Creates a cached pool of reusable threads for executing tasks.
     * Creates server and client sockets.
     * Creates and executes the message receiver.
     */
    public void startServer() {
        System.out.println("\nMessengerServer: line 49-----------****************START SERVER METHOD ENTRANCE****************");
        try{// create server and manage new clients
            if(!serverStarted()){//Ensures thread pool is created just before server socket is bound.
                setServerExecutor(Executors.newCachedThreadPool());//Create a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.
            }
            setServerSocket(new ServerSocket(SERVER_PORT, 100));//Create the server socket and indicate server is up. If it is closed, recreate it on server restart.
            
            while (serverStarted()){//while the server is on...
                setClientSocket(getServerSocket().accept());//constantly listen for a connection to be made to this socket and accept it. The method blocks until a connection is made.
                getServerExecutor().execute(new MessageReceiver(this, getClientSocket()));//Execute MessageReceiver for receiving messages from client
                System.out.println("\nMessengerServer: line 59-----------Connection received from: "+getClientSocket().getInetAddress());//print connection information
            } // end while
        }//end try
        catch (IOException ioException) {
            ioException.getMessage();
        }//end catch
        System.out.println("\nMessengerServer: line 65-----------****************START SERVER METHOD EXIT****************");
    } // end method startServer

    /**
     * Closes the socket and Stops accepting client connection.
     */
    public void stopServer() {
        System.out.println("\nMessengerServer: line 72-----------****************STOP SERVER METHOD ENTRANCE****************");
        try {
            System.out.println("\nMessengerServer: line 74-----------Running Tasks List before server socket was closed: "+getServerExecutor().toString());
            getServerSocket().close();
            setServerStarted(false);
            setServerStopped(true);//i.e. server stopped.
            System.out.println("\nMessengerServer: line 78-----------Server socket was closed at "+new java.util.Date());
            System.out.println("\nMessengerServer: line 79-----------Server down at "+new java.util.Date());
            System.out.println("\nMessengerServer: line 80-----------Running Tasks List before the shutdown of server executor: "+getServerExecutor().toString());
            List<Runnable> unfinishedTasksList = getServerExecutor().shutdownNow();
            System.out.println("\nMessengerServer: line 81-----------Server executor is shutting down. ---> "+getServerExecutor().toString());
            System.out.println("\nMessengerServer: line 82-----------Unfinished Tasks List of server executor: "+unfinishedTasksList.toString());
        } catch (IOException ex) {
            ex.getMessage();
        }
        System.out.println("\nMessengerServer: line 87-----------****************STOP SERVER METHOD EXIT****************");
    }

    /**
     * Receive new message and form a complete one.
     * 
     * @param from
     * @param message
     */
    @Override
    public void receiveMessage(String from, String message) {
        setCompleteMessage(from + MESSAGE_SEPARATOR + message);
        sendMulticast();
    }//end method messageReceived

    /**
     * Multicast message to all clients only when server is on.
     */
    public void sendMulticast(){
        if(serverStarted()){//send only when server is up.
            //multicastMissedMsgs();//CLient should be the one to resend missed msgs when server is back on/also to avoid server overload
            getServerExecutor().execute(new MulticastSender(getCompleteMessage().getBytes()));//create and start MulticastSender to broadcast messages
        }
    }//End sendMulticast()
    
//    /**
//     * Multicast missed messages to all clients only when server is back on.
//     */
//    public void multicastMissedMsgs(){
//        System.out.println("\nMessengerServer: line 118-----------****************METHOD---multicastMissedMsgs()---ENTRANCE****************");
//        try{
//            msgQueue.forEach((String msg) -> {//Loop through the queued missed messages.
//                getServerExecutor().execute(new MulticastSender(msg.getBytes()));//create and start MulticastSender to broadcast messages
//            });
//        } catch(NullPointerException nPe){
//            System.out.println("\nMessengerServer: line 123-----------No Missed message in quueue");
//        }
//    }//end multicastMissedMsgs()


//    public boolean isFlag() {
//        return flag;
//    }
//
//    public void setFlag(boolean flag) {
//        this.flag = flag;
//    }

    public ExecutorService getServerExecutor(){
        return serverExecutor;
    }
    
    public void setServerExecutor(ExecutorService serverExecutor) {
        this.serverExecutor = serverExecutor;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    /**
     * Sets the server socket to the new server socket passed in as argument.
     * Whenever a new server socket is instantiated, it starts listening to 
     * clients requests immediately. For that reason, this method also sets the 
     * serverUp variable to true, indicating the server is up.
     * @param serverSocket
     */
    public void setServerSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        setServerStarted(true);//i.e. server starts.
        System.out.println("\nMessengerServer: line 160-----------Server is up listening and waiting for clients to connect. Time: "+new java.util.Date());
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        System.out.println("\nMessengerServer: line 169-----------Server's client socket accepted connection made to it by client. Time: "+new java.util.Date());
    }

    public String getCompleteMessage() {
        return completeMessage;
    }

    public void setCompleteMessage(String completeMessage) {
        this.completeMessage = completeMessage;
    }

    public boolean serverIsUp() {
        return serverUp;
    }

    public void setServerUp(boolean serverStatus) {
        this.serverUp = serverStatus;
    }

    public boolean serverStopped() {
        return serverStopped;
    }

    public void setServerStopped(boolean serverStatus) {
        this.serverStopped = serverStatus;
    }

    public boolean serverClosed() {
        return serverClosed;
    }

    public void setServerClosed(boolean serverClosed) {
        this.serverClosed = serverClosed;
    }

    public boolean serverOpened() {
        return serverOpened;
    }

    public void setServerOpened(boolean serverStatus) {
        this.serverOpened = serverStatus;
    }

    public boolean msgIsComing() {
        return msgComing;
    }

    public void setMsgComing(boolean incomingMsg) {
        this.msgComing = incomingMsg;
    }

    public boolean serverStarted() {
        return serverStarted;
    }

    public void setServerStarted(boolean serverStarted) {
        this.serverStarted = serverStarted;
    }
} // end class MessengerServer

