/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.Server;
import GUI.Server.ThreadServidor;
import Player.Player;
import java.io.IOException;

/**
 *
 * @author diego
 */
public class CommandGiveup  extends Command{

    public CommandGiveup(String[] args) {
        super(CommandType.GIVEUP, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Player player = threadServidor.getGamePlayer();
        if (player != null) {
            player.surrender();
            threadServidor.isActive = false;
            sendResponse(threadServidor, "Te has rendido. Ya no participas en la partida.");

            CommandMessage msg = new CommandMessage(
                    new String[]{"MESSAGE", "El jugador: " + threadServidor.getClientName() + " se ha rendido.", "true"}
            );

            threadServidor.getServer().broadcast(msg);
        } else {
            sendResponse(threadServidor, "ERROR: No estás en una partida para poder rendirte.");
        }
    }

    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }

    @Override
    public void processInClient(Client client) {
        // The server sends CommandMessage for feedback, so this method
        // can rely on the default behavior of logging if needed, or do nothing.
    }
}
