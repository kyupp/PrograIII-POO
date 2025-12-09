/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Server;

import Console.Command;
import Match.Match;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author diego, David, Mathi, Gabriel
 */
public class Server {
    private final int PORT = 35500;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    // IMPORTANTE: Usar lista segura para hilos
    private CopyOnWriteArrayList<ThreadServidor> connectedClients;
    FrameServer refFrame;
    private Match game;
    private ThreadConnections connectionsThread;

    public Server(FrameServer refFrame) {
        // Inicialización segura
        connectedClients = new CopyOnWriteArrayList<>();
        this.refFrame = refFrame;

        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }
    
    public Server() {
        // Inicialización segura
        connectedClients = new CopyOnWriteArrayList<>();
        this.refFrame = null;

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

    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            if (refFrame != null) {
                refFrame.writeMessage("Server running!!! Puerto: " + PORT);
            } else {
                System.out.println("Server running!!! Puerto: " + PORT);
            }
        } catch (IOException ex) {
            if (refFrame != null) {
                refFrame.writeMessage("Error al iniciar server: " + ex.getMessage());
            } else {
                System.out.println("Error al iniciar server: " + ex.getMessage());
            }
        }
    }

    public ThreadServidor buscarThreadServidor(String nameClient){
        for (ThreadServidor clientThread : this.connectedClients){
            // Verificamos que el nombre no sea null para evitar crash
            if (clientThread.getClientName() != null && clientThread.getClientName().equals(nameClient)){
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
                client.objectSender.flush(); // Asegurar envío inmediato
            } catch (IOException ex) {
                if (refFrame != null) {
                    refFrame.writeMessage("Error broadcast a " + client.getName() + ": " + ex.getMessage());
                } else {
                    System.out.println("Error broadcast a " + client.getName() + ": " + ex.getMessage());
                }
            }
        }
    }

    public void sendPrivate(Command comando){
        if (comando.getParameters().length <= 1) return;

        String searchName = comando.getParameters()[1];
        ThreadServidor target = buscarThreadServidor(searchName);

        if (target != null) {
            try {
                target.objectSender.writeObject(comando);
                target.objectSender.flush();
            } catch (IOException ex) {
                if (refFrame != null) {
                    refFrame.writeMessage("Error private message: " + ex.getMessage());
                } else {
                    System.out.println("Error private message: " + ex.getMessage());
                }
            }
        }
    }

    // GETTERS Y SETTERS
    public int getMaxConections() { return maxConections; }
    public ServerSocket getServerSocket() { return serverSocket; }
    public CopyOnWriteArrayList<ThreadServidor> getConnectedClients() { return connectedClients; }
    public FrameServer getRefFrame() { return refFrame; }

    public void showAllNames(){
        if (refFrame != null) {
            this.refFrame.writeMessage("--- Usuarios conectados ---");
            for (ThreadServidor client : connectedClients) {
                this.refFrame.writeMessage(client.getClientName());
            }
        } else {
            System.out.println("--- Usuarios conectados ---");
            for (ThreadServidor client : connectedClients) {
                System.out.println(client.getClientName());
            }
        }
    }
}
