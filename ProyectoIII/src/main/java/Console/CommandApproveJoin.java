/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.Server;
import GUI.Server.ThreadServidor;
import Match.Match;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class CommandApproveJoin extends Command {

    public CommandApproveJoin(String[] args) {
        // args[1] = nombre del que solicitó
        // args[2] = yes/no
        super(CommandType.APPROVE_JOIN, args);
    }

    @Override
    public void processForServer(ThreadServidor hostThread) {

        Server server = hostThread.getServer();
        Match match = server.getGame();

        String playerName = getParameters()[1];
        String answer = getParameters()[2];

        ThreadServidor requestThread = server.buscarThreadServidor(playerName);

        if (requestThread == null)
            return;

        if (answer.equalsIgnoreCase("yes")) {
            match.addPlayer(requestThread.getGamePlayer());
            if (match.getPlayers().size() == hostThread.getServer().getConnectedClients().size()){
                //TODO: Start Game
            }

            // Avisar al jugador que fue aceptado
            CommandMessage msg = new CommandMessage(
                new String[]{"MESSAGE", "Fuiste aceptado en la partida!", "false"}
            );
            
            if (match.getPlayers().size() == server.getConnectedClients().size()){
                
                hostThread.getServer().executeCommand(match.startGame());
            }
            
            try {
                requestThread.objectSender.writeObject(msg);
            } catch (IOException ex) {
                System.out.println("Fuiste aceptado pero hubo un error");
            }

        } else {
            // Rechazado
            CommandMessage msg = new CommandMessage(
                new String[]{"MESSAGE", "El host rechazó tu solicitud", "false"}
            );
            try {
                requestThread.objectSender.writeObject(msg);
            } catch (IOException ex) {
                System.out.println("No fuiste aceptado y hubo un error");
            }
        }
    }

    @Override
    public void processInClient(Client client) {
        // No se usa
    }
}
