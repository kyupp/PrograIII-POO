/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Server;

import Console.Command;
import GUI.Client.Client;
import Match.Match;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * @author diego
 */
public class Server {
    private final int PORT = 35500;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients;
    FrameServer refFrame;
    private Match game;
    private ThreadConnections connectionsThread;
    
    
    public Server(FrameServer refFrame) {
        connectedClients = new ArrayList<ThreadServidor>();
        this.refFrame = refFrame;
        
        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }

    public Match getGame() {
        return game;
    }

    public void setGame(Match game) {
        this.game = game;
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
    
    public ThreadServidor buscarThreadServidor(String nameClient){
        
        for (ThreadServidor clientThread : this.connectedClients){
            if (nameClient.equals(clientThread.getClientName())){
                return clientThread;
            }
        }
        
        return null;
    }
    
    public void executeCommand(Command comando) {
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
        if (comando.getParameters() == null || comando.getParameters().length <= 1)
            return;

        String searchName = comando.getParameters()[1]; // ahora está garantizado por el contrato

        for (ThreadServidor client : connectedClients) {
            if (client.getClientName() != null && client.getClientName().equals(searchName)) {
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