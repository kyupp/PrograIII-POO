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
public class CommandMutualExit extends Command {

    public CommandMutualExit(String[] parameters) {
        super(CommandType.MUTUAL_EXIT, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("MUTUAL_EXIT");
        //TODO: Logica
    }
    
}
