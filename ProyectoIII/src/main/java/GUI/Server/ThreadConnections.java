/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class ThreadConnections extends Thread{
    private Server server;

    public ThreadConnections(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Socket newSocket = null;
        while (  server.getConnectedClients().size() < server.getMaxConections() ){
            server.getRefFrame().writeMessage("Esperando conexión No.");
            try {
                System.out.println("tc1");
                newSocket = server.getServerSocket().accept();
                System.out.println("tc2");
                //una vez con socket recibido, se crea e inicia el thread que va a anteder y escuchar al clinete que los conectó
                ThreadServidor newServerThread = new ThreadServidor(server, newSocket);
                System.out.println("tc3");
                server.getConnectedClients().add (newServerThread);
                System.out.println("tc4");
                newServerThread.start();
                System.out.println("tc5");

                server.getRefFrame().writeMessage("cliente conectado");
                System.out.println("tc6");
            
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                //server.getRefFrame().writeMessage("Error: " +  ex.getMessage());
            }
        }
        
    }
    
}
