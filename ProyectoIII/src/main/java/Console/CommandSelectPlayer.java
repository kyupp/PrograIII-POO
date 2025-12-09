/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

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
                 if (name == null ? clientThread.getName() == null : name.equals(clientThread.getClientName())) {

                    if (!clientThread.isActive) {
                        sendResponse(threadServidor, "El usuario '" + name + "' no esta activo.");
                        return;
                    }
                    int deaths = clientThread.getGamePlayer().getDeaths();
                    int defeats = clientThread.getGamePlayer().getDefeats();
                    int failedAttacks = clientThread.getGamePlayer().getFailedAttacks();
                    int successfullAttacks = clientThread.getGamePlayer().getSuccessfulAttacks();
                    int surronders = clientThread.getGamePlayer().getSurrenders();
                    int wins = clientThread.getGamePlayer().getWins();
                    
                    String data = "\nJugador: " + name + "\n    Deaths: " + deaths + "\n    Defeats: " + defeats + "\n    FailedAttacks: " + failedAttacks + "\n    Successfull attacks: " + successfullAttacks + "\n    Surronders: " + surronders + "\n    Winss: " + wins;
                    sendResponse(threadServidor, data);
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
