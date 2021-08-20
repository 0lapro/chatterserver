package chatterserver.src.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import static chatterserver.src.interfaces.Constants.*;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import chatterserver.src.runner.Receiver;
import chatterserver.src.runner.Multicaster;
import chatterserver.src.interfaces.Listener;

/**
 * The messenger server which also has-a listener, is a multi-threaded, socket and
 * packet based chat server that listens constantly for and receives incoming 
 * client messages, and multicasts to all clients including the sender.
 * 
 * @author 0laprogrmr@gmail.com
 */
public class ChatServer implements Listener, Serializable
{
    private static final long serialVersionUID = 1L;
    private transient ExecutorService serverExecutor; //The Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
    private transient ServerSocket serverSocket;//The server socket that waits for client requests to come in over the network.
    private transient Socket clientSocket;//The client socket (called just socket) is the server endpoint used for communicating with the client.
    private String completeMessage;
    private boolean serverUp;
    private boolean serverStarted;
    private boolean serverStopped;
    private boolean serverClosed;
    private boolean serverOpened;
    private boolean msgComing;

    /**
     * Starts the chat messenger server.
     * Creates a cached pool of reusable threads for executing tasks.
     * Creates server and client sockets.
     * Creates and executes the message receiver.
     */
    public void startServer() {
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "START SERVER METHOD ENTRANCE\n");
        try{// create server and manage new clients
            if(!serverStarted()){//Ensures thread pool is created just before server socket is bound.
                setServerExecutor(Executors.newCachedThreadPool());//Create a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.
            }
            setServerSocket(new ServerSocket(SERVER_PORT, 100));//Create the server socket and indicate server is up. If it is closed, recreate it on server restart.
            
            while (serverStarted()){//while the server is on...
                setClientSocket(getServerSocket().accept());//constantly listen for a connection to be made to this socket and accept it. The method blocks until a connection is made.
                try{
                    getServerExecutor().execute(new Receiver(this, getClientSocket()));//Execute Receiver for receiving messages from client
                }catch (NullPointerException nPe){//server executor may not be created sometimes due to the fact that the threads are not serialized
                    nPe.getMessage();
                    setServerExecutor(Executors.newCachedThreadPool());//create new threads
                    getServerExecutor().execute(new Receiver(this, getClientSocket()));//get the threads to work
                }
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connection received from: {0}", getClientSocket().getInetAddress()+"\n");
            } // end while
        }//end try
        catch (IOException ioException) {
            ioException.getMessage();
        }//end catch
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "START SERVER METHOD EXIT\n");
    } // end method startServer

    /**
     * Closes the socket and Stops accepting client connection.
     */
    public void stopServer() {
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "STOP SERVER METHOD ENTRANCE\n");
        try {
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Running Tasks List before server socket was closed: {0}", getServerExecutor().toString()+"\n");
            getServerSocket().close();
            setServerStarted(false);
            setServerStopped(true);//i.e. server stopped.
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server socket was closed at {0}", new java.util.Date()+"\n");
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server down at {0}", new java.util.Date()+"\n");
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Running Tasks List before the shutdown of server executor: {0}", getServerExecutor().toString()+"\n");
            List<Runnable> unfinishedTasksList = getServerExecutor().shutdownNow();//stop all actively executing tasks and return a list of the tasks that were awaiting execution.
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server executor is shutting down. ---> {0}", getServerExecutor().toString()+"\n");
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Unfinished Tasks List of server executor: {0}", unfinishedTasksList.toString()+"\n");
        } catch (IOException | NullPointerException e) {
            e.getMessage();
        }
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "STOP SERVER METHOD EXIT\n");
    }//

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
            getServerExecutor().execute(new Multicaster(getCompleteMessage().getBytes()));//create and start Multicaster to broadcast messages
        }
    }//End sendMulticast()

    public ExecutorService getServerExecutor(){
        return this.serverExecutor;
    }
    
    public void setServerExecutor(ExecutorService serverExecutor) {
        this.serverExecutor = serverExecutor;
    }
    
    public ServerSocket getServerSocket() {
        return this.serverSocket;
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
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server is up listening and waiting for clients to connect at ", new java.util.Date()+"\n");
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server's client socket accepted connection made to it by client at ", new java.util.Date()+"\n");
    }

    public String getCompleteMessage() {
        return this.completeMessage;
    }

    public void setCompleteMessage(String completeMessage) {
        this.completeMessage = completeMessage;
    }

    public boolean serverIsUp() {
        return this.serverUp;
    }

    public void setServerUp(boolean serverStatus) {
        this.serverUp = serverStatus;
    }

    public boolean serverStopped() {
        return this.serverStopped;
    }

    public void setServerStopped(boolean serverStatus) {
        this.serverStopped = serverStatus;
    }

    public boolean serverClosed() {
        return this.serverClosed;
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
        return this.msgComing;
    }

    public void setMsgComing(boolean incomingMsg) {
        this.msgComing = incomingMsg;
    }

    public boolean serverStarted() {
        return this.serverStarted;
    }

    public void setServerStarted(boolean serverStarted) {
        this.serverStarted = serverStarted;
    }
} // end class ChatServer

