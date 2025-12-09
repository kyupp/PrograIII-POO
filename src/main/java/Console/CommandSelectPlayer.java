/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandSelectPlayer extends Command {

    public CommandSelectPlayer(String[] parameters) {
        super(CommandType.SELECT_PLAYER, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("SELECT PLAYER");
        String[] parameters = this.getParameters();
        
        if (parameters.length != 2){
            sendResponse(threadServidor, """
                   Error: Formato incorrecto.
                   Uso: SELECT_PLAYER <name> 
                   Ejemplo: SELECT_PLAYER Mathias
                   """);
            return;
        }
        
        String name = parameters[1];
        
        for (ThreadServidor clientThread : threadServidor.getServer().getConnectedClients()) {
            if (clientThread != null) {
                if (name.equals(clientThread.getClientName())) {

                    if (!clientThread.isActive) {
                        sendResponse(threadServidor, "El usuario '" + name + "' no esta activo.");
                        return;
                    }

                    if (clientThread.getGamePlayer() == null) {
                        sendResponse(threadServidor, "El usuario '" + name + "' aún no ha entrado a la partida (no tiene datos).");
                        return;
                    }

                    int deaths = clientThread.getGamePlayer().getDeaths();
                    int defeats = clientThread.getGamePlayer().getDefeats();
                    int failedAttacks = clientThread.getGamePlayer().getFailedAttacks();
                    int successfulAttacks = clientThread.getGamePlayer().getSuccessfulAttacks();
                    int surrenders = clientThread.getGamePlayer().getSurrenders();
                    int wins = clientThread.getGamePlayer().getWins();
                    
                    String data = String.format("\n--- Estadísticas de %s ---\n  Victorias: %d\n  Derrotas: %d\n  Rendiciones: %d\n  Ataques Exitosos: %d\n  Ataques Fallidos: %d\n  Muertes de Guerreros: %d",
                        name, wins, defeats, surrenders, successfulAttacks, failedAttacks, deaths);
                    sendResponse(threadServidor, data);
                    return;
                }
            }
        }
        
        // Si el bucle termina, el jugador no fue encontrado
        sendResponse(threadServidor, "Error: Jugador '" + name + "' no encontrado.");
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
        // This command's response is already formatted as a message by the server,
        // so the default CommandMessage.processInClient will handle displaying it.
    }
}
