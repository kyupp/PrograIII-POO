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
public class CommandReload extends Command {

    public CommandReload(String[] parameters) {
        super(CommandType.RELOAD, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("RELOAD");
        //TODO: Logica
    }
    
}
