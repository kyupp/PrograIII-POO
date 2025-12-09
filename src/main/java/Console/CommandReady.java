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
        Player player = threadServidor.getGamePlayer();
        Match match = server.getGame();

        if (player == null) {
            sendResponse(threadServidor, "ERROR: Debes crear al menos un guerrero para unirte a una partida (ej: CREATE_FIGHTER <nombre> <tipo>)");
            return;
        }

        if (player.getTeam().size() < 4) {
            sendResponse(threadServidor, "ERROR: Debes tener 4 guerreros en tu equipo para poder estar listo. Tienes " + player.getTeam().size() + ".");
            return;
        }

        if (player.isReady()) {
            sendResponse(threadServidor, "Ya estás marcado como listo.");
            return;
        }
        
        player.setReady(true);
        sendResponse(threadServidor, "¡Estás listo! Esperando a los demás jugadores...");
        
        // Notificar a todos
        server.broadcast(new CommandMessage(new String[]{"MESSAGE", "El jugador " + player.getId() + " está listo.", "true"}));
        
        // Verificar si todos los jugadores están listos para empezar
        boolean allReady = true;
        for (Player p : match.getPlayers()) {
            if (!p.isReady()) {
                allReady = false;
                break;
            }
        }
        
        if (match.getPlayers().size() >= 2 && allReady) {
            server.broadcast(match.startGame());
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
        // The server sends CommandMessage for feedback, so this can be empty.
    }
}