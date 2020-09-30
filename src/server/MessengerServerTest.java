/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author 0laprogrmr@gmail.com
 * Test the OlasMessenger class.
 */
public class MessengerServerTest implements Serializable
{
    static MessengerServer messengerServer;
    private static final long serialVersionUID = 1L;
    private static boolean serverSaved;
    private static boolean fileExists;
    Thread serverStarterThread;
    private static boolean reinstated;

    public MessengerServerTest(MessengerServer server) {
        messengerServer = server;
    }
    
    public static void main(String args[]) {
        System.out.println("\nMessengerServerTest: line 33-----------Current thread before SwingUtilities.invokeLater: "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
        SwingUtilities.invokeLater(() -> {
            System.out.println("\nMessengerServerTest: line 35-----------Current thread before makeSever: "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
            if(!serverStateExists()){
                return;
            }
            else{
                restoreServerState();
            }
            if(!reinstated()){
                new MessengerServerTest(new MessengerServer()).makeServer();
                System.out.println("Not Reinstated!: new server instance created");
            }
            System.out.println("\nMessengerServerTest: line 35-----------Current thread after makeServer: "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
        });
        System.out.println("\nMessengerServerTest: line 37-----------Current thread after SwingUtilities.invokeLater: "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
    }//end main
    
    public void makeServer() {
        System.out.println("\nMessengerServerTest: line 41-----------First current thread inside makeServer "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
        JFrame frame = new JFrame("MyMessenger Server");
        JPanel panel1 = new JPanel();//create panel to use border layout
        frame.getContentPane().add(panel1, BorderLayout.NORTH);//add panel to the center of frame 
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        JButton thirdButton = new JButton("Button 3");
        JButton fourthButton = new JButton("Button 4");
        JButton fifthButton = new JButton("Button 5");
        JButton sixthButton = new JButton("Button 6");
        JButton seventhButton = new JButton("Button 7");
        panel1.add(startButton);
        panel1.add(stopButton);

        startButton.addActionListener((ActionEvent e) -> {
                System.out.println("\nMessengerServerTest: line 57-----------SERVER START BUTTON clicked at: "+new java.util.Date(e.getWhen()));
                System.out.println("\nMessengerServerTest: line 58-----------Current thread ---> "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
                setServerStarterThread(new ServerStarterJob());
                nameServerStarterThread();
                getServerStarterThread().start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                System.out.println("\nMessengerServerTest: line 64-----------Expected Svr starter thread ---> "+getServerStarterThread().getName()+" state: "+getServerStarterThread().getState().name()+" at: "+new java.util.Date(e.getWhen()));
                System.out.println("\nMessengerServerTest: line 65-----------Current thread ---> "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
            } //End actionPerformed
        );//End addActionListener

        stopButton.addActionListener((ActionEvent e) -> {
                System.out.println("\nMessengerServerTest: line 70-----------SERVER STOP BUTTON clicked at: "+new java.util.Date(e.getWhen()));
                System.out.println("\nMessengerServerTest: line 71-----------Current thread ---> "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
                Thread serverStopperThread  = new Thread(new ServerStopperJob());
                serverStopperThread.setName("serverStopperThread");
                serverStopperThread.start();
                stopButton.setEnabled(false);
                startButton.setEnabled(true);//this statement calls the srv starter thread to do this task; maybe later, i'll find a way to let it be done inside the start button action listener.
                System.out.println("\nMessengerServerTest: line 77-----------Expected Svr stopper thread ---> "+serverStopperThread.getName()+" state:  "+serverStopperThread.getState().name()+" at: "+new java.util.Date(e.getWhen()));
                System.out.println("\nMessengerServerTest: line 78-----------Current thread ---> "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
            }
        );

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setVisible(true);
        
        getMessengerServer().setServerOpened(true);
        getMessengerServer().setServerClosed(false);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("\nMessengerServerTest: line 91-----------Current thread "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
                System.out.println("\nMessengerServerTest: line 92-----------Server GUI closed at: "+new java.util.Date());
                getMessengerServer().setServerClosed(true);
                saveServerState();
                System.exit(0);
            } // end method windowClosing
        } // end frame.addWindowListener
        ); // end call to addWindowListener
    }// End method makeServer
    
    private class ServerStarterJob implements Runnable{
        @Override
        public void run() {
            System.out.println("\nMessengerServerTest: line 120----------- START SERVER METHOD called at: "+new java.util.Date()+" by "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
            getMessengerServer().startServer();
        }
    }
    
    private class ServerStopperJob implements Runnable{
        @Override
        public void run() {
            System.out.println("\nMessengerServerTest: line 129----------- STOP SERVER METHOD called at: "+new java.util.Date()+" by "+Thread.currentThread().getName()+" state: "+Thread.currentThread().getState());
            getMessengerServer().stopServer();
        }
    }
    
    public static void saveServerState(){
        try {
            try (FileOutputStream fOs = new FileOutputStream("messengerserver.ser"); ObjectOutputStream oOs = new ObjectOutputStream(fOs)) {
                oOs.writeObject(getMessengerServer());
                setServerSaved(true);
                System.out.println("\nSerialized data is successfully SAVED as \"messengerserver.ser\" in the state folder "+new java.util.Date());
            }
        } catch (IOException iOe) {
            iOe.getMessage();
        }
    }//End saveServerState()
    
    public static void restoreServerState(){
        System.out.println("Method Entrance: restoreServerState");
        try (FileInputStream fIs = new FileInputStream("messengerserver.ser"); ObjectInputStream oIs = new ObjectInputStream(fIs)) {
            oIs.readObject();
            setReinstated(true);
            new MessengerServerTest(getMessengerServer()).makeServer();
            System.out.println("\nSerialized class \"messengerserver.ser\" is successfully REINSTATED at "+new java.util.Date());
        } catch (FileNotFoundException ex) {  
        Logger.getLogger(MessengerServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MessengerServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println("Method Exit: restoreServerState");
    }//End restoreServerState()
    
    public static MessengerServer getMessengerServer() {
        return MessengerServerTest.messengerServer;
    }

    public void nameServerStarterThread(){
        serverStarterThread.setName("ServerStarterThread");
    }
    
    public Thread getServerStarterThread() {
        return serverStarterThread;
    }

    public void setServerStarterThread(Runnable runnable) {
        this.serverStarterThread = new Thread(runnable);
    }
    
    public static void setServerSaved(boolean isServerSaved) {
        serverSaved = isServerSaved;
    }
    
    public static boolean serverSaved() {
        return serverSaved;
    }
    
    public static void setReinstated(boolean isRestored) {
        reinstated = isRestored;
    }
    
    public static boolean reinstated() {
        return reinstated;
    }
    
    public static void setFileExistence(boolean isFileFound) {
        fileExists = isFileFound;
    }
    
    public static boolean fileExists() {
        return fileExists;
    }
    
    public static boolean serverStateExists(){
        String relativePath = "\\Documents\\chatterserver\\messengerserver.ser";
        String absolutePath = System.getenv("USERPROFILE")+relativePath;
        File file = new File(absolutePath);
        
        if(file.exists() && !file.isDirectory()) { 
            setFileExistence(true);
        }
        if(!serverSaved() && !fileExists()){
            System.out.println("Saved server state NOT found");
            return false;
        }
        return true;
    }
}//End class MessengerServerTest
