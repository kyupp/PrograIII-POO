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
        //TODO: Logica
    }
    
}
