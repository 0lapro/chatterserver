/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatterserver.src.util;

import java.util.logging.Level;

/**
 *
 * @author Ola
 */
public class Logger {
    
    private final String classToLog;
    private String infoToLog;
     
    /**
     *
     * @param className
     * @param info
     */
    public Logger(String className, String info){
        this.classToLog = className;
        this.infoToLog = info;
    }
    
    public Logger(String className){
        this.classToLog = className;
    }
    
    public String getReinstatementInfo(){
        return "Serialized class \"chatserver.ser\" is successfully REINSTATED at "+new java.util.Date()+"\n";//reinstatement
    }
    
    public String getNewInstanceInfo(){
        return "Not Reinstated!: new chat server created at "+new java.util.Date()+"\n";//reinstatement
    }
    
    public String getStartButtonInfo(){
        return "SERVER START BUTTON clicked at "+new java.util.Date()+"\n";//reinstatement
    }
    
    public String getStopButtonInfo(){
        return "SERVER STOP BUTTON clicked at "+new java.util.Date()+"\n";//reinstatement
    }
    
    public String getThreadNameAndState(){
        return "Class: "+getClassToLog()+" | Current thread is "+Thread.currentThread().getName()+" and its state is " +Thread.currentThread().getState()+"\n";
    }
    
    public void log(){
        java.util.logging.Logger.getLogger(getClassToLog()).log(Level.INFO, getInfoToLog(), getClassToLog().getClass());
    }
    
    public void log(String info){
        java.util.logging.Logger.getLogger(getClassToLog()).log(Level.INFO, info);
    }
    
    public String getClassToLog() {
        return classToLog;
    }

    public String getInfoToLog() {
        return infoToLog;
    }
    
    public static void readFile(){
        String fileName = "src\\server\\ChatServerUI.java";
        TextFileReader file = new TextFileReader(fileName);
        String[] arrayLines = file.openFile();
//        for (String arrayLine : arrayLines) {
//            System.out.println(arrayLine);
//        }
        
        for (int i=0; i<arrayLines.length; i++) {
            System.out.println(i+"---"+arrayLines[i]);
        } 
    }
    
//    public static void main(String[] args){
//        readFile();
//    }
    
}
