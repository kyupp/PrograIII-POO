/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Server;

import Console.Command;
import GUI.Client.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * @author diego
 */
public class Server {
    private final int PORT = 35500;
    private int playersReady = 0;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients;
    FrameServer refFrame;
    private ThreadConnections connectionsThread;
    
    
    public Server(FrameServer refFrame) {
        connectedClients = new ArrayList<ThreadServidor>();
        this.refFrame = refFrame;
        
        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }

    public int getPlayersReady() {
        return playersReady;
    }

    public void setPlayersReady(int playersReady) {
        this.playersReady = playersReady;
    }
    
    // Método que inicializa el server
    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            refFrame.writeMessage("Server running!!!");
        } catch (IOException ex) {
            refFrame.writeMessage("Error: " + ex.getMessage());
        }
    }
    
    public ThreadServidor buscarThreadServidor(Client client){
        
        String nameClient = client.getName();
        
        for (ThreadServidor clientThread : this.connectedClients){
            if (nameClient.equals(clientThread.getClientName())){
                return clientThread;
            }
        }
        
        return null;
    }
    
    void executeCommand(Command comando) {
        if (comando.isIsBroadcast())
            this.broadcast(comando);
        else
            this.sendPrivate(comando);
    }
    
    public void broadcast(Command comando){
        for (ThreadServidor client : connectedClients) {
            try {
                client.objectSender.writeObject(comando);
            } catch (IOException ex) {
                refFrame.writeMessage("Error broadcast: " + ex.getMessage());
            }
        }
    }
    
    public void sendPrivate(Command comando){
        // Nombre del cliente en la posición 1
        if (comando.getParameters().length <= 1)
            return;
        
        String searchName = comando.getParameters()[1];
        
        for (ThreadServidor client : connectedClients) {
            if (client.name.equals(searchName)){
                try {
                    client.objectSender.writeObject(comando);
                    break;
                } catch (IOException ex) {
                    refFrame.writeMessage("Error private message: " + ex.getMessage());
                }
            }
        }
    }
    
    // GETTERS
    
    public int getMaxConections() {
        return maxConections;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ArrayList<ThreadServidor> getConnectedClients() {
        return connectedClients;
    }

    public FrameServer getRefFrame() {
        return refFrame;
    }

    public void showAllNames(){
        this.refFrame.writeMessage("Usuarios conectados");
        for (ThreadServidor client : connectedClients) {
            this.refFrame.writeMessage(client.name);
        }
    }

}