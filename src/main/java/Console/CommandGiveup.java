/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Server.ThreadServidor;
import Player.Player;

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
        if (player == null) {
            threadServidor.sendError("No estás en una partida.");
            return;
        }

        // Marcar al jugador como inactivo y registrar la rendición
        player.surrender();
        threadServidor.isActive = false;

        // Enviar confirmación privada al jugador que se rinde
        threadServidor.sendPrivateMessage("Te has rendido. Ya no puedes participar en la partida.");

        // Crear y enviar un mensaje de broadcast para notificar a todos
        String broadcastMsg = "El jugador " + player.getId() + " se ha rendido.";
        CommandMessage broadcastCmd = new CommandMessage(new String[]{"MESSAGE", broadcastMsg, "true"});
        threadServidor.getServer().broadcast(broadcastCmd);

        // Si el turno era del jugador que se rindió, pasar al siguiente
        if (threadServidor.getServer().getGame().isMyTurn(player)) {
            threadServidor.getServer().getGame().nextTurn();
            String turnMsg = "Turno de: " + threadServidor.getServer().getGame().getPlayerInTurn().getId();
            CommandMessage turnCmd = new CommandMessage(new String[]{"MESSAGE", turnMsg, "true"});
            threadServidor.getServer().broadcast(turnCmd);
        }
    }
}
