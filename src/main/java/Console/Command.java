/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import javax.swing.SwingUtilities;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author diego
 */
public abstract class Command implements Serializable{
    private CommandType type;
    private String[] parameters;
    private boolean isBroadcast;

    public Command(CommandType type, String[] parameters) {
        this.type = type;
        this.parameters =  parameters; // Enemigo 4 5 6 7 9 10
    }
    
    public abstract void processForServer(ThreadServidor threadServidor);
    public void processInClient(Client client){
        SwingUtilities.invokeLater(() -> {
            // Por defecto, todos los comandos que llegan al cliente se loguean.
            // Las subclases pueden llamar a super.processInClient() y luego añadir su lógica de UI.
            if (!(this instanceof CommandMessage) && !(this instanceof CommandAttack)) { // CommandMessage y CommandAttack tienen su propio formato de log.
                client.getRefFrame().appendLog("SYS: " + this.toString());
            }
        });
    }

    public CommandType getType() {
        return type;
    }

    public String[] getParameters() {
        return parameters;
    }
    
    public String toString(){
        return type.toString() + "->" + Arrays.toString(parameters);
    }

    public boolean isIsBroadcast() {
        return isBroadcast;
    }

    public void setIsBroadcast(boolean isBroadcast) {
        this.isBroadcast = isBroadcast;
    }
}
