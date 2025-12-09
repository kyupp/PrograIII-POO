/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.Server;
import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class CommandReady extends Command {

    public CommandReady(String[] parameters) {
        super(CommandType.READY, parameters);
        this.setIsBroadcast(false);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {

        Server server = threadServidor.getServer();
        //System.out.println(threadServidor.name + "SISISISI");
        
        if (threadServidor.getGamePlayer() != null){
            sendResponse(threadServidor, "ERROR: Ya le dio a Ready");
            return;
        }
        
        threadServidor.setGamePlayer(new Player (threadServidor.name));

        // Si no existe partida → este jugador es el host
        if (server.getGame() == null) {

            Match match = new Match(threadServidor.getGamePlayer());
            //System.out.println(threadServidor.name + "SISISISI");
            server.setGame(match);

            // Avisar al host que es host
            CommandMessage msgHost = new CommandMessage(
                new String[]{"MESSAGE", 
                    "Eres el host de la partida. Esperando jugadores...", 
                    "false"}
            );
            
            try {
                threadServidor.objectSender.writeObject(msgHost);
            } catch (IOException ex) {
                System.out.println("No te dejaron");
            }

        } else {

            // Ya existe partida → enviar solicitud al host
            Match match = server.getGame();
            Player host = match.getOwner();
            //System.out.println(host.getId() + "     SNOOOOO");

            ThreadServidor hostThread = server.buscarThreadServidor(host.getId());

            if (hostThread != null) {
                CommandReadyRequest request = new CommandReadyRequest(
                    threadServidor.getClientName()
                );
                try {
                    hostThread.objectSender.writeObject(request);
                } catch (IOException ex) {
                    System.out.println("No se encontro ningun cliente");
                }
            }
        }
    }
    
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
}

