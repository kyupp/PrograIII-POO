/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Client;

import Console.Command;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author diego
 */
public class ThreadClient extends Thread{
    private Client client;
    private DataOutputStream escritor;
    private DataInputStream lector;
    
    private boolean isRunning = true;

    public ThreadClient(Client client) {
        this.client = client;

    }
    
    public void run (){
        
        Command comandoRecibido;
        while (isRunning){
            try {
                comandoRecibido = (Command) client.objectListener.readObject();
                //receivedMessage = client.getListener().readUTF(); //espera hasta recibir un String desde el cliente que tiene su socket
                comandoRecibido.processInClient(client);
            } catch (IOException ex) {
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void leerString(){
        try {
            lector = new DataInputStream(client.getSocket().getInputStream());
            String mensaje = lector.readUTF();
            client.getRefFrame().setTxtConsoleLog(mensaje + "\n");
        } catch (IOException ex) {
            client.getRefFrame().setTxtConsoleLog("Error al leer mensaje del server\n");
        }
        
    }
    
    public void leerStringDirecto(String message){
       client.getRefFrame().setTxtConsoleLog(message + "\n");
    }
    
    public void enviarString(String mensaje){
        try {
            escritor.writeUTF(mensaje);
        } catch (IOException ex) {
            client.getRefFrame().setTxtConsoleLog("Error al enviar mensaje al server\n");
        }
    }
    
    public void showPrivateMessage(String message){
        client.getRefFrame().setTxtConsoleLog("[PRIVADO] " + message);
    }

    /**
     * Mostrar mensaje público en UI
     */
    public void showPublicMessage(String message){
        client.getRefFrame().setTxtConsoleLog("[GLOBAL] " + message);
    }

}
