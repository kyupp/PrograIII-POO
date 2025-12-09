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
        this.setIsBroadcast(false); // This command manages its own messaging.
    }

    @Override
    public void processForServer(ThreadServidor hostThread) {
        Server server = hostThread.getServer();
        Match match = server.getGame();

        // 1. Verificar que el comando lo envíe el jugador que es el Host.
        if (match == null || match.getOwner() != hostThread.getGamePlayer()) {
            hostThread.sendError("Error: Solo el host de la partida puede aprobar solicitudes.");
            return;
        }

        // 2. Validar que el comando tenga los parámetros necesarios.
        if (getParameters().length < 3 || getParameters()[1] == null || getParameters()[2] == null) {
            hostThread.sendError("Error: Comando inválido. Uso: APPROVE_JOIN <nombre> <yes|no>");
            return;
        }
        String guestPlayerName = getParameters()[1];
        String answer = getParameters()[2];

        // 3. Buscar el ThreadServidor del jugador invitado por su nombre.
        ThreadServidor guestThread = server.buscarThreadServidor(guestPlayerName);

        if (guestThread == null || guestThread.getGamePlayer() == null) {
            hostThread.sendError("Error: Jugador '" + guestPlayerName + "' no encontrado o no está listo.");
            return;
        }

        if (answer.equalsIgnoreCase("yes")) {
            handleApproval(hostThread, guestThread, match, guestPlayerName);
        } else {
            handleRejection(hostThread, guestThread, guestPlayerName);
        }
    }

    private void handleApproval(ThreadServidor hostThread, ThreadServidor guestThread, Match match, String guestPlayerName) {
        Server server = hostThread.getServer();
        
        // a. Añadir el Player invitado a la lista de jugadores de la partida.
        match.addPlayer(guestThread.getGamePlayer());

        // b. Enviar una confirmación privada al Host.
        hostThread.sendConfirmation("Has aprobado a " + guestPlayerName + " para unirse a la partida.");

        // c. Enviar una confirmación privada al Invitado.
        guestThread.sendConfirmation("Tu solicitud para unirte a la partida fue APROBADA por el host.");

        // d. Enviar un CommandMessage de broadcast a todos informando que el jugador se unió.
        String broadcastJoinMsg = guestPlayerName + " se ha unido a la partida.";
        Command broadcastJoin = new CommandMessage(new String[]{"MESSAGE", broadcastJoinMsg, "true"});
        server.broadcast(broadcastJoin);

        // e. Si la partida está llena (ej. 2 jugadores), iniciarla.
        if (match.getPlayers().size() == 2) { // Asumiendo 2 jugadores para iniciar
            String broadcastStartMsg = "La partida ha comenzado! Es el turno de " + match.getPlayerInTurn().getId() + ".";
            Command broadcastStart = new CommandMessage(new String[]{"MESSAGE", broadcastStartMsg, "true"});
            server.broadcast(broadcastStart);
        }
    }

    private void handleRejection(ThreadServidor hostThread, ThreadServidor guestThread, String guestPlayerName) {
        // a. Enviar confirmación de la acción al Host.
        hostThread.sendConfirmation("Has rechazado la solicitud de " + guestPlayerName + ".");

        // b. Enviar notificación de rechazo al Invitado.
        guestThread.sendError("Tu solicitud para unirte a la partida fue RECHAZADA por el host.");
    }

    @Override
    public void processInClient(Client client) {
        // No se usa
    }
}
