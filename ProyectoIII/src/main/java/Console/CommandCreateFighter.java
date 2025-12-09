package Console;

import Fighters.Fighter;
import Fighters.Type;
import Fighters.Weapon;
import GUI.Server.ThreadServidor;
import Player.Player;

public class CommandCreateFighter extends Command {

    public CommandCreateFighter(String[] args) {
        // Formato: CREATE_FIGHTER <nombre> <tipo>
        // Ejemplo: CREATE_FIGHTER Bot-Zero HIELO
        super(CommandType.CREATE_FIGHTER, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Player player = threadServidor.getGamePlayer();
        
        if (player == null) {
            sendResponse(threadServidor, "ERROR: Debes estar registrado primero (usa READY)");
            return;
        }
        
        // Validar que no tenga ya 4 guerreros
        if (player.getTeam().size() >= 4) {
            sendResponse(threadServidor, "ERROR: Ya tienes 4 guerreros creados");
            return;
        }
        
        String[] params = getParameters();
        
        if (params.length < 3) {
            sendResponse(threadServidor, """
                ERROR: Formato incorrecto.
                Uso: CREATE_FIGHTER <nombre> <tipo>
                Tipos disponibles: FUEGO, AIRE, AGUA, MAGIA_BLANCA, MAGIA_NEGRA, 
                                  ELECTRICIDAD, HIELO, ACIDO, ESPIRITUALIDAD, HIERRO
                Ejemplo: CREATE_FIGHTER Bot-Zero HIELO
                """);
            return;
        }
        
        String fighterName = params[1];
        String typeStr = params[2].toUpperCase();
        
        // Validar tipo
        Type fighterType;
        try {
            fighterType = Type.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            sendResponse(threadServidor, "ERROR: Tipo inválido. Tipos disponibles: " + 
                String.join(", ", getTypeNames()));
            return;
        }
        
        // Crear guerrero
        Fighter newFighter = new Fighter(fighterName, fighterType);
        
        // Crear 5 armas para el guerrero
        createWeaponsForFighter(newFighter);
        
        // Añadir al equipo del jugador
        player.addFighter(newFighter);
        
        sendResponse(threadServidor, 
            String.format("✓ Guerrero '%s' creado exitosamente [%s]. Armas: 5/5 generadas", 
                fighterName, fighterType));
        
        // Mostrar info del guerrero
        sendResponse(threadServidor, getWeaponInfo(newFighter));
        
        // Si ya tiene 4, notificar que puede iniciar
        if (player.getTeam().size() == 4) {
            sendResponse(threadServidor, 
                "\n¡Equipo completo! (4/4 guerreros). El juego puede comenzar.");
        } else {
            sendResponse(threadServidor, 
                String.format("\nGuerreros creados: %d/4", player.getTeam().size()));
        }
    }
    
    /**
     * Crea 5 armas con porcentajes aleatorios para un guerrero
     */
    private void createWeaponsForFighter(Fighter fighter) {
        String[] weaponNames = {"Knife", "Gun", "Sword", "Axe", "Bow"};
        
        for (String weaponName : weaponNames) {
            Weapon weapon = new Weapon(weaponName, null, false);
            weapon.generateRandomPercentaje();
            fighter.addWeapon(weapon);
        }
    }
    
    /**
     * Genera información detallada de las armas del guerrero
     */
    private String getWeaponInfo(Fighter fighter) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Armas de ").append(fighter.getName()).append(" ---\n");
        
        Type[] types = Type.values();
        
        for (Weapon weapon : fighter.getWeapons()) {
            sb.append(String.format("%-10s: ", weapon.getName()));
            
            for (int i = 0; i < types.length; i++) {
                sb.append(weapon.getDamageVs(types[i])).append("%");
                if (i < types.length - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Obtiene nombres de todos los tipos disponibles
     */
    private String[] getTypeNames() {
        Type[] types = Type.values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].name();
        }
        return names;
    }
    
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
}
