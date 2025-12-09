/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;

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
        Player sender = threadServidor.getGamePlayer();

        if (game != null && sender != null) {
            // Validar que sea su turno antes de pasarlo
            if (game.isMyTurn(sender)) {
                game.nextTurn();
                Player nextPlayer = game.getPlayerInTurn();

                // Avisar a todos del cambio de turno
                String msg = "Turno pasado. Ahora es el turno de: " + nextPlayer.getId();
                CommandMessage cmd = new CommandMessage(new String[]{"MESSAGE", msg, "true"});
                threadServidor.getServer().executeCommand(cmd);
            } else {
                threadServidor.sendError("No puedes pasar turno, no es tu turno.");
            }
        } else {
            threadServidor.sendError("Error: No hay juego iniciado o no estás registrado.");
        }
    }
    
}
