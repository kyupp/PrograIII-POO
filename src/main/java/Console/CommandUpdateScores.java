package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import javax.swing.SwingUtilities;

public class CommandUpdateScores extends Command {

    /**
     * Server to Client command.
     * args: [0] = successfulAttacks
     *       [1] = failedAttacks
     *       [2] = deaths
     *       [3] = wins
     *       [4] = defeats
     *       [5] = surrenders
     */
    public CommandUpdateScores(String[] parameters) {
        super(CommandType.UPDATE_SCORES, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // This is a client-only command, server should not process it.
    }

    @Override
    public void processInClient(Client client) {
        if (getParameters().length < 6) {
            client.getRefFrame().appendLog("ERROR: Invalid score update received.");
            return;
        }

        try {
            int successful = Integer.parseInt(getParameters()[0]);
            int failed = Integer.parseInt(getParameters()[1]);
            int deaths = Integer.parseInt(getParameters()[2]);
            int wins = Integer.parseInt(getParameters()[3]);
            int defeats = Integer.parseInt(getParameters()[4]);
            int surrenders = Integer.parseInt(getParameters()[5]);

            SwingUtilities.invokeLater(() -> {
                client.getRefFrame().updatePlayerScores(successful, failed, deaths, wins, defeats, surrenders);
            });
        } catch (NumberFormatException e) {
            client.getRefFrame().appendLog("ERROR: Could not parse score update from server.");
        }
    }
}