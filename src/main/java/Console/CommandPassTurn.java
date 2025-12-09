/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

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
            game.nextTurn();
            sendResponse(threadServidor, "Usted ha decidido pasar de turno");
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
    
}