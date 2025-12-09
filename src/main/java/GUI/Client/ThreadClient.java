/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Client;
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
    private final Client client;
    private final controllerClient controller; // Reference to the controller
    
    // Removed DataOutputStream and DataInputStream as we are using Object streams

    private boolean isRunning = true;

    public ThreadClient(Client client, controllerClient controller) {
        this.client = client;
        this.controller = controller;
    }
    
    public void run (){
        
        Console.Command comandoRecibido; // Use fully qualified name to avoid import
        while (isRunning){
            try {
                comandoRecibido = (Console.Command) client.objectListener.readObject();
                controller.receivedCommand(comandoRecibido); // Pass the command to the controller
            } catch (IOException ex) {
                System.out.println("Error de conexión con el servidor: " + ex.getMessage());
                controller.view.appendLog("Se ha perdido la conexión con el servidor.");
                isRunning = false; // Detener el hilo si la conexión se pierde
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
