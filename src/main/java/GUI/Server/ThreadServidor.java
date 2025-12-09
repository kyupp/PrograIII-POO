/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Server;

import Console.Command;
import Console.CommandMessage;
import Player.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author diego
 */
public class ThreadServidor extends Thread{
    private Server server;
    private Socket socket;
    
    // Streams para leer y escribir objetos
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    private DataOutputStream escritor;
    private DataInputStream lector;
    public String name;
    private Player gamePlayer;
    
    public boolean isActive = true;
    public boolean isRunning = true;

    public ThreadServidor(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            objectSender = new ObjectOutputStream(socket.getOutputStream());
            objectSender.flush();
            objectListener = new ObjectInputStream(socket.getInputStream());
            System.out.println(name + "     AHAHAHAHAH");
        } catch (IOException ex) {
            System.out.println("Error en ThreadServidor constructor: " + ex.getMessage());
        } catch (Exception ex){
            System.out.println("Error general en ThreadServidor: " + ex.getMessage());
        }
    }

    public Player getGamePlayer() {
        return gamePlayer;
    }
    
    public void run() {
        Command comando;
        while (isRunning) {
            try {
                comando = (Command) objectListener.readObject();
                server.refFrame.writeMessage("ThreadServer recibió: " + comando);
                comando.processForServer(this); // The command itself will handle any necessary broadcasting or responses.
                
            } catch (IOException ex) {
                System.out.println("Error IO en ThreadServidor run: " + ex.getMessage());
                isRunning = false; // Detener el hilo si hay error de conexión
            } catch (ClassNotFoundException ex) {
                System.out.println("Error ClassNotFound: " + ex.getMessage());
            }  
        }
        
        // Cleanup al terminar
        cleanup();
    }
    
    /**
     * Limpieza al cerrar conexión
     */
    private void cleanup() {
        try {
            if (objectListener != null) objectListener.close();
            if (objectSender != null) objectSender.close();
            if (socket != null) socket.close();
            server.refFrame.writeMessage("Cliente desconectado: " + name);
        } catch (IOException e) {
            System.out.println("Error en cleanup: " + e.getMessage());
        }
    }
    
    public void setClientName(String name) {
        this.name = name;
        this.setName("ClientThread-" + name); // Renombra el hilo en el sistema
    }
    
    public String getClientName(){
        return this.name;
    }

    
    public void showAllClients() {
        this.server.showAllNames();
    }
    
    /**
     * Obtiene referencia al servidor
     */
    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * Verifica si es el turno de este jugador
     */
    public boolean isMyTurn() {
        //TODO: Implementar
        return false;
    }
    
    /**
     * Envía un mensaje de error al cliente
     */
    public void sendError(String errorMessage) {
        String[] args = {"MESSAGE", "ERROR: " + errorMessage, "false"};
        CommandMessage errorCmd = new CommandMessage(args);
        try {
            server.refFrame.writeMessage("ERROR para " + name + ": " + errorMessage);
            // Enviar el comando de mensaje de error al cliente
            objectSender.writeObject(errorCmd);
            objectSender.flush();
        } catch (Exception e) {
            System.out.println("Error enviando mensaje de error: " + e.getMessage());
        }
    }
    
    public void sendPrivateMessage(String message) {
        try {
            // Formato: ["MESSAGE", texto, "false"]
            String[] args = new String[]{"MESSAGE",message, "false"};
            CommandMessage cmd = new CommandMessage(args);

            objectSender.writeObject(cmd);
            objectSender.flush();
        } catch (IOException e) {
            System.out.println("Error enviando mensaje privado: " + e.getMessage());
        }
    }
    
    /**
     * Envía un mensaje de confirmación al cliente
     */
    public void sendConfirmation(String message) {
        String[] args = {"MESSAGE", message, "false"};
        CommandMessage infoCmd = new CommandMessage(args);
        try {
            server.refFrame.writeMessage("INFO para " + name + ": " + message);
            // Enviar el comando de mensaje de confirmación al cliente
            objectSender.writeObject(infoCmd);
            objectSender.flush();
        } catch (Exception e) {
            System.out.println("Error enviando confirmación: " + e.getMessage());
        }
    }

    public void setGamePlayer(Player gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    
    
}
