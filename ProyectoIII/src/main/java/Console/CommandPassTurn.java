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
public class CommandPassTurn extends Command {

    public CommandPassTurn(String[] parameters) {
        super(CommandType.PASS_TURN, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("PASS TURN");
        //TODO: Logica
    }
    
}
