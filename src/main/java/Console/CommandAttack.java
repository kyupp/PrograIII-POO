/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 *
 */
package Console;

import Fighters.Fighter;
import Fighters.Weapon;
import GUI.Server.ThreadServidor;
import Match.Match;
import Player.Player;
import GUI.Client.Client;

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

            // 6. Enviar CommandAttack con todos los detalles a los clientes relevantes para actualización de UI
            // Parameters: [attackerPlayerName, targetPlayerName, attackingFighterName, weaponName, victimFighterName, damage, victimFighterNewHP]
            String[] clientParams = {
                attacker.getId(), victim.getId(), attackingFighter.getName(),
                weapon.getName(), victimFighter.getName(),
                String.valueOf(damage), String.valueOf(victimFighter.getLife())
            };
            CommandAttack clientAttackCmd = new CommandAttack(clientParams);

            // Enviar al atacante
            threadServidor.objectSender.writeObject(clientAttackCmd);
            threadServidor.objectSender.flush();

            // Enviar a la víctima
            ThreadServidor victimThread = threadServidor.getServer().buscarThreadServidor(victim.getId());
            victimThread.objectSender.writeObject(clientAttackCmd);
            victimThread.objectSender.flush();

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

    @Override
    public void processInClient(Client client) {
        String[] params = getParameters();
        // Expected parameters: [attackerPlayerName, targetPlayerName, attackingFighterName, weaponName, victimFighterName, damage, victimFighterNewHP]
        if (params.length < 7) {
            client.getRefFrame().appendLog("ERROR: Formato de ATTACK inválido recibido del servidor.");
            return;
        }

        // Parse parameters for UI update
        String attackerPlayerName = params[0];
        String targetPlayerName = params[1];
        String attackingFighterName = params[2];
        String weaponName = params[3];
        String victimFighterName = params[4];
        int damage = Integer.parseInt(params[5]);
        int victimFighterNewHP = Integer.parseInt(params[6]);

        // Update UI based on who is attacking/being attacked
        if (client.getName().equals(attackerPlayerName)) {
            // This client is the attacker
            client.getRefFrame().displayOutgoingAttack(
                    attackingFighterName, weaponName, targetPlayerName, victimFighterName, damage);
            // Update target's fighter health if it's the enemy currently displayed
            client.getRefFrame().updateEnemyFighterDisplay(victimFighterName, victimFighterNewHP);
        } else if (client.getName().equals(targetPlayerName)) {
            // This client is the victim
            client.getRefFrame().displayIncomingAttack(
                    attackerPlayerName, attackingFighterName, weaponName, victimFighterName, damage);
            // Update my fighter's health
            client.getRefFrame().updateMyFighterCard(victimFighterName, victimFighterNewHP);
        } else {
            // Neither attacker nor victim, just log the event
            client.getRefFrame().appendLog(String.format("INFO: %s (%s) atacó a %s (%s) con %s, causando %d%% de daño.",
                    attackerPlayerName, attackingFighterName, targetPlayerName, victimFighterName, weaponName, damage));
        }
    }
}