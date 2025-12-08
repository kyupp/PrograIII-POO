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
        } else{
            //TODO; Retornar mensaje al usuaruio indicando que no se pudo ya que todavia no hay match
        }
    }
    
}
