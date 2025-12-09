/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Client;

import Console.CommandFactory;
import Player.Player;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author mathi
 */
public class Client {
    private final int PORT = 35500;
    private final String IP_ADDRESS = "localhost";
    private Socket socket;
    private ClientFrame refFrame;
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    private controllerClient controller; // Reference to the controller
    private Player localPlayer;
    
    private String name;

    public Client(ClientFrame refFrame, String name) {
        this.refFrame = refFrame;
        this.name = name;
        this.localPlayer = new Player(name);
        this.connect();
        
    }
    
    private void connect (){
        try {
            socket = new Socket(IP_ADDRESS , PORT);
            objectSender =  new ObjectOutputStream (socket.getOutputStream());
            objectSender.flush();
            objectListener =  new ObjectInputStream (socket.getInputStream());
            // ThreadClient will be created and started by controllerClient
            // The controller will also set its reference here.
            // Initial NAME command will be sent by controllerClient
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public ObjectInputStream getObjectListener() {
        return objectListener; 
    }

    public ObjectOutputStream getObjectSender() {
        return objectSender;
    }
    
    public ClientFrame getRefFrame() {
        return refFrame;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setController(controllerClient controller) {
        this.controller = controller;
    }

    public String getName() {
        return name;
    }
    
    public Player getLocalPlayer() { return localPlayer; }
    public void setLocalPlayer(Player localPlayer) { this.localPlayer = localPlayer; }
}
