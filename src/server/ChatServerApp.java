package chatterserver.src.server;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Chat server GUI.
 * @author 0laprogrmr@gmail.com
 */
public class ChatServerApp {

    static ChatServer chatServer;
    private static boolean serverSaved;
    private static boolean fileExists;
    Thread serverStarterThread;
    private static boolean reinstated;
    private static final chatterserver.src.util.Logger MYLOGGER = new chatterserver.src.util.Logger(ChatServerApp.class.getName());

    public ChatServerApp(ChatServer server) {
        ChatServerApp.chatServer = server;
    }

    public static void main(String args[]) {
        
        MYLOGGER.log(MYLOGGER.getThreadNameAndState());

        SwingUtilities.invokeLater(() -> {
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
            restoreServerState();
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
        });
        MYLOGGER.log(MYLOGGER.getThreadNameAndState());
    }//end main

    public void makeServer() {
        MYLOGGER.log(MYLOGGER.getThreadNameAndState());
        Thread.currentThread().getStackTrace();
        JFrame frame = new JFrame("Chat Server");
        JPanel panel1 = new JPanel();//create panel to use border layout
        frame.getContentPane().add(panel1);
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        JButton clearButton = new JButton("Clear");
        JButton addressButton = new JButton("Address");
        JButton portButton = new JButton("Port");
        JButton backlogButton = new JButton("Backlog");
        panel1.add(startButton);
        panel1.add(stopButton);
        panel1.add(addressButton);
        panel1.add(portButton);
        panel1.add(backlogButton);
        
        clearButton.addActionListener((ActionEvent e) -> {
        });

        startButton.addActionListener((ActionEvent e) -> {
            MYLOGGER.log(MYLOGGER.getStartButtonInfo());
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
            
            setServerStarterThread(new ServerStarterJob());
            nameServerStarterThread();
            getServerStarterThread().start();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO,
                "Expected Svr starter thread: {0}| state: {1} at: {2}\n", 
                new Object[]{getServerStarterThread().getName(),
                getServerStarterThread().getState().name(), new java.util.Date(e.getWhen())});
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
            } //End actionPerformed
        );//End addActionListener

        stopButton.addActionListener((ActionEvent e) -> {
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO, "SERVER STOP BUTTON clicked at: {0}", new java.util.Date(e.getWhen())+"\n");
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
            Thread serverStopperThread = new Thread(new ServerStopperJob());
            serverStopperThread.setName("serverStopperThread");
            serverStopperThread.start();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO,
                "Expected Svr stopper thread: {0}| state: {1} at: {2}\n", 
                new Object[]{getServerStarterThread().getName(),
                getServerStarterThread().getState().name(), new java.util.Date(e.getWhen())});
            MYLOGGER.log(MYLOGGER.getThreadNameAndState());
        }
        );

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setVisible(true);

        getChatServer().setServerOpened(true);
        getChatServer().setServerClosed(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveServerState();
                Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO, 
                    "Serialized data is successfully SAVED as \"chatserver.ser\" at {0}", new java.util.Date()+"\n");
                MYLOGGER.log(MYLOGGER.getThreadNameAndState());
                Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO, "SERVER GUI closed at: {0}", new java.util.Date()+"\n");
                getChatServer().setServerClosed(true);
                System.exit(0);
            } // end windowClosing
        } // end WindowAdapter
        ); // end addWindowListener
    }// End makeServer

    private class ServerStarterJob implements Runnable {
        @Override
        public void run() {
            getChatServer().startServer();
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO, "START SERVER METHOD called at: {0} by {1} state: {2}", new Object[]{new java.util.Date(), Thread.currentThread().getName(), Thread.currentThread().getState()+"\n"});
        }
    }

    private class ServerStopperJob implements Runnable {
        @Override
        public void run() {
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.INFO, "STOP SERVER METHOD called at: {0} by {1} state: {2}", new Object[]{new java.util.Date(), Thread.currentThread().getName(), Thread.currentThread().getState()+"\n"});
            getChatServer().stopServer();
        }
    }

    public static void saveServerState() {
        try {
            try (FileOutputStream fOs = new FileOutputStream(".\\chatterserver\\reinstatement\\chatserver.ser"); ObjectOutputStream oOs = new ObjectOutputStream(fOs)) {
                oOs.writeObject(getChatServer());
                setServerSaved(true);
            }
        } catch (IOException iOe) {
            iOe.getMessage();
        }
    }//End saveServerState()

    public static void restoreServerState() {
        System.out.println("Method Entrance: restoreServerState");
        File file = new File(".\\chatterserver\\reinstatement\\chatserver.ser");
        try (FileInputStream fIs = new FileInputStream(file); ObjectInputStream oIs = new ObjectInputStream(fIs)) {
            if (file.exists() && !file.isDirectory()) {
                new ChatServerApp((ChatServer) oIs.readObject()).makeServer();
                MYLOGGER.log(MYLOGGER.getReinstatementInfo());
                setReinstated(true);
                System.out.println("Reinstated: "+reinstated());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Method Exit: restoreServerState");
    }//End restoreServerState()

    public static ChatServer getChatServer() {
        return ChatServerApp.chatServer;
    }

    public void nameServerStarterThread() {
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
}//End class ChatServerApp
