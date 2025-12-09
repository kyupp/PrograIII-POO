/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Fighters.Fighter;
import Fighters.Weapon;
import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;

/**
 *
 * @author diego
 */
public class CommandAttack extends Command {

    /**
     * Protocolo esperado en args:
     * args[0] = "ATTACK"
     * args[1] = Nombre del Jugador Objetivo
     * args[2] = Índice de mi guerrero atacante (0-3)
     * args[3] = Índice del arma a usar (0-4)
     * args[4] = Índice del guerrero enemigo a atacar (0-3)
     */
    public CommandAttack(String[] args) {
        super(CommandType.ATTACK, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Match match = threadServidor.getServer().getGame();
        Player attacker = threadServidor.getGamePlayer();
        String[] params = this.getParameters();

        // 1. Validaciones básicas
        if (match == null) {
            threadServidor.sendError("No hay una partida iniciada.");
            return;
        }
        if (attacker == null) {
            threadServidor.sendError("Error: No estás registrado en la partida.");
            return;
        }
        if (!match.isMyTurn(attacker)) {
            threadServidor.sendError("No es tu turno. Espera al jugador: " + match.getPlayerInTurn().getId());
            return;
        }
        if (params.length < 5) {
            threadServidor.sendError("Comando inválido. Faltan parámetros.");
            return;
        }

        try {
            // 2. Extraer datos
            String targetName = params[1];
            int myFighterIdx = Integer.parseInt(params[2]);
            int weaponIdx = Integer.parseInt(params[3]);
            int targetFighterIdx = Integer.parseInt(params[4]);

            Player victim = match.getPlayer(targetName);
            if (victim == null) {
                threadServidor.sendError("El jugador objetivo no existe.");
                return;
            }

            // 3. Obtener objetos de juego (Guerreros y Armas)
            if (myFighterIdx >= attacker.getTeam().size() || targetFighterIdx >= victim.getTeam().size()) {
                threadServidor.sendError("Índice de guerrero fuera de rango.");
                return;
            }

            Fighter attackingFighter = attacker.getTeam().get(myFighterIdx);
            Fighter victimFighter = victim.getTeam().get(targetFighterIdx);

            if (attackingFighter.isDead()) {
                threadServidor.sendError("Tu guerrero está muerto y no puede atacar.");
                return;
            }
            if (victimFighter.isDead()) {
                threadServidor.sendError("No puedes atacar a un cadáver.");
                return;
            }

            if (weaponIdx >= attackingFighter.getWeapons().size()) {
                threadServidor.sendError("Índice de arma incorrecto.");
                return;
            }
            Weapon weapon = attackingFighter.getWeapons().get(weaponIdx);

            if (weapon.isUsed()) {
                threadServidor.sendError("Ya usaste esta arma. Elige otra.");
                return;
            }

            // 4. LÓGICA DE COMBATE
            int damage = weapon.getDamageVs(victimFighter.getType());
            victimFighter.takeDamage(damage);
            weapon.setUsed(); // Marcar arma como usada

            // 5. Construir mensaje de éxito para todos
            String resultMsg = String.format("ATAQUE: %s (%s) usó %s contra %s (%s) causando %d%% de daño. Vida restante: %d%%",
                    attacker.getId(), attackingFighter.getName(),
                    weapon.getName(),
                    victim.getId(), victimFighter.getName(),
                    damage, victimFighter.getLife());

            // Si mató al guerrero
            if (victimFighter.isDead()) {
                resultMsg += " ¡HA MUERTO!";
                // Aquí podrías sumar puntos de muerte al player
            }

            // 6. Broadcast del resultado como mensaje global
            CommandMessage broadcastCmd = new CommandMessage(new String[]{"MESSAGE", resultMsg, "true"});
            threadServidor.getServer().executeCommand(broadcastCmd);

            // 7. Pasar turno automáticamente (opcional, depende de reglas)
            match.nextTurn();
            CommandMessage turnMsg = new CommandMessage(new String[]{"MESSAGE", "Turno de: " + match.getPlayerInTurn().getId(), "true"});
            threadServidor.getServer().executeCommand(turnMsg);

        } catch (NumberFormatException e) {
            threadServidor.sendError("Error de formato numérico en ataque.");
        } catch (Exception e) {
            threadServidor.sendError("Error procesando ataque: " + e.getMessage());
            e.printStackTrace();
        }
    }
}