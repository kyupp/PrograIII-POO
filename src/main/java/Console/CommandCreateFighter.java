package Console;

import Fighters.Fighter;
import Fighters.Type;
import Fighters.Weapon;
import GUI.Server.Server;
import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;
import GUI.Client.Client;

public class CommandCreateFighter extends Command {

    public CommandCreateFighter(String[] args) {
        // Formato: CREATE_FIGHTER <nombre> <tipo>
        super(CommandType.CREATE_FIGHTER, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Server server = threadServidor.getServer();
        Player player = threadServidor.getGamePlayer();

        // --- Lógica de Unión a la Partida (movida aquí) ---
        if (player == null) {
            Player newPlayer = new Player(threadServidor.getClientName());
            threadServidor.setGamePlayer(newPlayer);
            player = newPlayer;

            if (server.getGame() == null) {
                // Es el primer jugador, se convierte en HOST
                Match match = new Match(player);
                server.setGame(match);
                threadServidor.sendConfirmation("Eres el HOST de la partida. Continúa creando tu equipo.");
            } else {
                // Es un jugador invitado, debe pedir permiso
                Match match = server.getGame();
                if (match.isGameStarted()) {
                    threadServidor.sendError("La partida ya ha comenzado. No puedes unirte.");
                    threadServidor.setGamePlayer(null); // Revertir
                    return;
                }

                Player host = match.getOwner();
                ThreadServidor hostThread = server.buscarThreadServidor(host.getId());

                if (hostThread != null) {
                    CommandReadyRequest request = new CommandReadyRequest(threadServidor.getClientName());
                    try {
                        hostThread.objectSender.writeObject(request);
                        threadServidor.sendConfirmation("Solicitud para unirte enviada al Host. Espera aprobación mientras creas tu equipo.");
                    } catch (Exception e) {
                        threadServidor.sendError("No se pudo enviar la solicitud al Host.");
                        threadServidor.setGamePlayer(null); // Revertir
                        return;
                    }
                } else {
                    threadServidor.sendError("El Host parece haberse desconectado. No se puede unir a la partida.");
                    threadServidor.setGamePlayer(null); // Revertir
                    return;
                }
            }
        }

        // --- Lógica de Creación de Guerrero ---
        Match match = server.getGame();
        if (match.isGameStarted()) {
            threadServidor.sendError("No puedes crear más guerreros, la partida ya ha comenzado.");
            return;
        }
        if (player.getTeam().size() >= 4) {
            sendResponse(threadServidor, "ERROR: Ya tienes 4 guerreros creados");
            return;
        }

        String[] params = getParameters();

        if (params.length < 3) {
            sendResponse(threadServidor,
                    "ERROR: Formato incorrecto.\n" +
                            "Uso: CREATE_FIGHTER <nombre> <tipo>\n" +
                            "Tipos disponibles: FUEGO, AIRE, AGUA, MAGIA_BLANCA, MAGIA_NEGRA,\n" +
                            "ELECTRICIDAD, HIELO, ACIDO, ESPIRITUALIDAD, HIERRO\n" +
                            "Ejemplo: CREATE_FIGHTER Bot-Zero HIELO");
            return;
        }

        String fighterName = params[1];
        String typeStr = params[2].toUpperCase();

        Type fighterType;
        try {
            fighterType = Type.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            sendResponse(threadServidor,
                    "ERROR: Tipo inválido. Tipos disponibles: " +
                            String.join(", ", getTypeNames()));
            return;
        }

        Fighter newFighter = new Fighter(fighterName, fighterType);

        createWeaponsForFighter(newFighter);

        player.addFighter(newFighter);

        sendResponse(threadServidor,
                String.format("✓ Guerrero '%s' creado exitosamente [%s]. Armas generadas.",
                        fighterName, fighterType));

        sendResponse(threadServidor, "Guerreros creados: " + player.getTeam().size() + "/4");

        // Notificar al cliente para que actualice su UI
        try {
            threadServidor.objectSender.writeObject(this);
        } catch (Exception e) {
            threadServidor.sendError("No se pudo notificar la creación del guerrero al cliente.");
        }
    }

    /** Crear 5 armas */
    private void createWeaponsForFighter(Fighter fighter) {
        String[] weaponNames = {"Knife", "Gun", "Sword", "Axe", "Bow"};

        for (String weaponName : weaponNames) {
            Weapon weapon = new Weapon(weaponName, null, false);
            weapon.generateRandomPercentaje();
            fighter.addWeapon(weapon);
        }
    }

    private String[] getTypeNames() {
        return java.util.Arrays.stream(Type.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    private void sendResponse(ThreadServidor thread, String message) {
        thread.sendPrivateMessage(message);
    }

    // -------------------------
    // CLIENT-SIDE IMPLEMENTATION
    // -------------------------

    @Override
    public void processInClient(Client client) {
        String[] params = getParameters();

        if (params.length < 3) {
            client.getRefFrame().appendLog("ERROR: Formato de CREATE_FIGHTER inválido recibido del servidor.");
            return;
        }

        String fighterName = params[1];
        Type fighterType = Type.valueOf(params[2].toUpperCase());

        Fighter newFighter = new Fighter(fighterName, fighterType);

        if (client.getRefFrame().getLocalPlayer() != null) {
            client.getRefFrame().getLocalPlayer().addFighter(newFighter);
            client.getRefFrame().addMyFighterCard(newFighter);
            client.getRefFrame().appendLog(
                    "Tu guerrero '" + fighterName + "' [" + fighterType.name() + "] ha sido creado."
            );
        }
    }
}
