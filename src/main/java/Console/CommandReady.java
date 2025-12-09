/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Fighters.Fighter;
import Fighters.Type;
import Fighters.Weapon;
import GUI.Server.Server;
import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author mathi & David
 */
public class CommandReady extends Command {

    public CommandReady(String[] parameters) {
        super(CommandType.READY, parameters);
        this.setIsBroadcast(false);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {

        Server server = threadServidor.getServer();

        // Validación original de tu compañero
        if (threadServidor.getGamePlayer() != null){
            sendResponse(threadServidor, "ERROR: Ya le dio a Ready");
            return;
        }

        // 1. Crear el Jugador Básico (Lógica original)
        Player newPlayer = new Player(threadServidor.name);

        // 2. Asociar el jugador (con equipo) al hilo
        threadServidor.setGamePlayer(newPlayer);
        threadServidor.sendConfirmation("Ahora estás registrado en la partida. " +
                "Crea tus guerreros con: CREATE_FIGHTER <nombre> <tipo>");


        // 3. Lógica de Host vs Invitado (Lógica original de tu compañero)
        if (server.getGame() == null) {

            // --- CASO 1: NO EXISTE PARTIDA (JUGADOR ES HOST) ---
            Match match = new Match(threadServidor.getGamePlayer());
            server.setGame(match);

            // Avisar al host
            CommandMessage msgHost = new CommandMessage(
                    new String[]{"MESSAGE",
                            "Eres el HOST. Esperando solicitudes de jugadores...",
                            "false"}
            );

            try {
                threadServidor.objectSender.writeObject(msgHost);
            } catch (IOException ex) {
                System.out.println("Error enviando mensaje al host");
            }

        } else {

            // --- CASO 2: YA EXISTE PARTIDA (JUGADOR ES INVITADO) ---
            Match match = server.getGame();

            // Nueva validación: No permitir unirse si la partida ya empezó.
            if (match.isGameStarted()) {
                sendResponse(threadServidor, "ERROR: La partida ya ha comenzado.");
                threadServidor.setGamePlayer(null); // Revertir la asignación del jugador
                return;
            }

            Player host = match.getOwner();
            ThreadServidor hostThread = server.buscarThreadServidor(host.getId());

            if (hostThread != null) {
                // Enviar solicitud de permiso al Host
                CommandReadyRequest request = new CommandReadyRequest(
                    threadServidor.getClientName()
                );
                try {
                    hostThread.objectSender.writeObject(request); // El Host recibe: "¿Puedo entrar?"
                    threadServidor.sendConfirmation("Solicitud enviada al Host. Espera aprobación.");
                } catch (IOException ex) {
                    threadServidor.sendError("No se pudo enviar la solicitud al Host.");
                }
            } else {
                threadServidor.sendError("El Host parece haberse desconectado.");
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