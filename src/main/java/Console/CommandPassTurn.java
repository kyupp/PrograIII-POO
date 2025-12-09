/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import Match.Match;

/**
 *
 * @author mathi
 */
public class CommandPassTurn extends Command {

    public CommandPassTurn(String[] parameters) {
        super(CommandType.PASS_TURN, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Match game = threadServidor.getServer().getGame();
        if (game != null){
            if (game.isMyTurn(threadServidor.getGamePlayer())) {
                game.nextTurn();
                sendResponse(threadServidor, "Has pasado tu turno.");
                // Notificar a todos de quién es el siguiente turno
                CommandMessage turnMsg = new CommandMessage(new String[]{"MESSAGE", "Turno de: " + game.getPlayerInTurn().getId(), "true"});
                threadServidor.getServer().broadcast(turnMsg);
            } else {
                sendResponse(threadServidor, "ERROR: No es tu turno.");
            }
        } else{
            sendResponse(threadServidor, "ERROR: Aun no ha iniciado el juego");
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
