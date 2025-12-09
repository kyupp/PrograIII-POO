package GUI.Client;

import Console.Command;
import Console.CommandCreateFighter;
import Console.CommandMessage;
import Console.CommandName;
import Console.CommandReady;
import Console.CommandReadyRequest;
import Console.CommandSelectPlayer;
import Fighters.Fighter;
import Player.Player;
import javax.swing.SwingUtilities;

/**
 *
 * @author mathi
 */
public class controllerClient {
    
    private modelClient modelo;
    ClientFrame view;
    private Client client;

    public controllerClient(ClientFrame view, String name) {
        this.view = view;
        this.client = new Client(view, name);
        this.modelo = new modelClient(this.client);
        this.client.setController(this); // Set the controller reference in the client
        
        // Set initial player ID in the UI
        view.setPlayerID(name);

        // Automatically send the NAME command upon connection
        procesarComando("NAME " + name);

        // Start the thread that listens for server messages
        ThreadClient threadClient = new ThreadClient(this.client, this);
        threadClient.start();
    }
    
    public void procesarComando(String comando) {
        String resultado = modelo.comprobarComando(comando);
        if (!resultado.isEmpty()) {
            view.appendLog("ERROR: " + resultado); // Display errors from local command processing
        }
    }
    
    // Method called by ThreadClient when a Command object arrives from the server
    public void receivedCommand(Command command) {
        SwingUtilities.invokeLater(() -> {
            // Process the command on the client side to update UI
            command.processInClient(client);
            
            // Specific UI updates based on command type (if needed beyond processInClient)
            if (command instanceof CommandName) {
                // The CommandName.processInClient already sets the name in the Client object
                // and the ClientFrame.setPlayerID is called during controller init.
            } else if (command instanceof CommandCreateFighter) {
                // The server's CommandCreateFighter should ideally send back a command
                // with the created fighter's details for the client to add to its UI.
                // For now, the processInClient of CommandCreateFighter (if implemented)
                // would handle this.
            }
            // Add more specific UI updates here for other command types if their processInClient
            // doesn't fully cover the visual requirements.
        });
    }

    public Player getLocalPlayer() {
        return client.getLocalPlayer();
    }
}
