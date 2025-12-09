package GUI.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import Player.Player;
import Fighters.Fighter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gabri
 */
public class ClientFrame extends JFrame {

    // Componentes lógicos
    private controllerClient controller;
    
    // Componentes visuales
    private JTextArea txtLog;
    private JTextField txtInput;
    private JButton btnSend;
    
    // Barras de estado (Salud/Porcentaje)
    private JProgressBar barMyFighter;
    private JProgressBar barEnemyFighter;
    private JLabel lblMyFighterName;
    private JLabel lblEnemyFighterName;
    
    // Panel de Stats (Scores del PDF)
    private JLabel lblScoreSuccess;
    private JLabel lblScoreFail;
    private JLabel lblScoreDeaths;
    private JLabel lblPlayerID;
    
    // Nuevos paneles para el área de juego
    private JPanel pnlMyTeam;
    private JPanel pnlOutgoingAttack;
    private JPanel pnlIncomingAttack;
    private Map<String, FighterCard> myFighterCards; // Para acceso rápido a las tarjetas de mis guerreros

    public ClientFrame() {
        initComponents();
        String name = JOptionPane.showInputDialog(this, "Ingrese su nombre: ");
        if (name == null || name.trim().isEmpty()) {
            name = "ID_" + System.currentTimeMillis();
        }
        this.controller = new controllerClient(this, name);
        this.setVisible(true);
    }

    private void initComponents() {
        setTitle("Proyecto III - Battle Console");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30));
        
        // --- 1. PANEL SUPERIOR (Estado de Batalla) ---
        JPanel panelTop = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelTop.setBackground(new Color(50, 50, 50));

        // Panel Jugador (Izquierda)
        JPanel pnlMyFighter = createFighterPanel("Mi Guerrero", Color.CYAN);
        barMyFighter = (JProgressBar) pnlMyFighter.getClientProperty("bar");
        lblMyFighterName = (JLabel) pnlMyFighter.getClientProperty("label");
        
        // Panel Enemigo (Derecha)
        JPanel pnlEnemyFighter = createFighterPanel("Enemigo", Color.RED);
        barEnemyFighter = (JProgressBar) pnlEnemyFighter.getClientProperty("bar");
        lblEnemyFighterName = (JLabel) pnlEnemyFighter.getClientProperty("label");

        panelTop.add(pnlMyFighter);
        panelTop.add(pnlEnemyFighter);
        add(panelTop, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Log de Batalla tipo Consola) ---
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.BOLD, 14));
        txtLog.setBackground(new Color(20, 20, 20));
        txtLog.setForeground(new Color(0, 255, 0)); // Verde Matrix
        txtLog.setMargin(new Insets(10, 10, 10, 10));
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        
        JScrollPane scrollLog = new JScrollPane(txtLog);
        TitledBorder logBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), " Battle Log ", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), Color.WHITE);
        scrollLog.setBorder(logBorder);
        scrollLog.setBackground(new Color(20, 20, 20));
        
        add(scrollLog, BorderLayout.CENTER);

        // --- 3. PANEL IZQUIERDO (Área de Juego: Mis Guerreros, Ataques) ---
        // --- 3. PANEL DERECHO (Scores y Stats del PDF) ---
        JPanel panelRight = new JPanel();
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelRight.setPreferredSize(new Dimension(200, 0));
        
        lblPlayerID = createStatLabel("Player ID:", "---");
        panelRight.add(lblPlayerID);
        
        panelRight.add(Box.createVerticalStrut(20));
        panelRight.add(new JSeparator());
        panelRight.add(Box.createVerticalStrut(10));
        
        JLabel scoresTitle = new JLabel("SCORES", SwingConstants.CENTER);
        scoresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresTitle.setFont(new Font(scoresTitle.getFont().getName(), Font.BOLD, scoresTitle.getFont().getSize()));
        panelRight.add(scoresTitle);
        panelRight.add(Box.createVerticalStrut(10));
        
        lblScoreSuccess = createStatValue("Ataques Exitosos:", "0");
        panelRight.add(lblScoreSuccess);
        
        lblScoreFail = createStatValue("Ataques Fallidos:", "0");
        panelRight.add(lblScoreFail);
        
        lblScoreDeaths = createStatValue("Muertes:", "0");
        panelRight.add(lblScoreDeaths);

        // --- Game Area Panel (Left Side) ---
        JPanel pnlGameArea = new JPanel(new BorderLayout(10, 10));
        pnlGameArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlGameArea.setBackground(new Color(30, 30, 30));
        pnlGameArea.setPreferredSize(new Dimension(400, 0)); // Adjust width as needed

        // My Team Panel
        pnlMyTeam = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        TitledBorder myTeamBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), " Mi Equipo ", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), Color.WHITE);
        pnlMyTeam.setBorder(myTeamBorder);
        pnlMyTeam.setBackground(new Color(40, 40, 40));
        myFighterCards = new HashMap<>();
        
        // Attack Details Panel (split into outgoing and incoming)
        JPanel pnlAttackDetails = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlAttackDetails.setBackground(new Color(30, 30, 30));

        // Outgoing Attack Display
        pnlOutgoingAttack = new JPanel(new BorderLayout(5, 5));
        TitledBorder outgoingBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN), " Ataque Realizado ", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), Color.GREEN);
        pnlOutgoingAttack.setBorder(outgoingBorder);
        pnlOutgoingAttack.setBackground(new Color(40, 40, 40));
        JLabel lblOutgoing = new JLabel("Esperando tu ataque...", SwingConstants.CENTER);
        lblOutgoing.setForeground(Color.WHITE);
        pnlOutgoingAttack.add(lblOutgoing, BorderLayout.CENTER);

        // Incoming Attack Display
        pnlIncomingAttack = new JPanel(new BorderLayout(5, 5));
        TitledBorder incomingBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), " Ataque Recibido ", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), Color.RED);
        pnlIncomingAttack.setBorder(incomingBorder);
        pnlIncomingAttack.setBackground(new Color(40, 40, 40));
        JLabel lblIncoming = new JLabel("Esperando ataque enemigo...", SwingConstants.CENTER);
        lblIncoming.setForeground(Color.WHITE);
        pnlIncomingAttack.add(lblIncoming, BorderLayout.CENTER);

        pnlAttackDetails.add(pnlOutgoingAttack);
        pnlAttackDetails.add(pnlIncomingAttack);

        // Add My Team and Attack Details to Game Area
        pnlGameArea.add(pnlMyTeam, BorderLayout.NORTH);
        pnlGameArea.add(pnlAttackDetails, BorderLayout.CENTER);

        // Add all main panels to the frame
        add(pnlGameArea, BorderLayout.WEST); // Game Area on the left
        add(panelRight, BorderLayout.EAST); // Stats on the right

        // --- 4. PANEL INFERIOR (Entrada de Comandos) ---
        JPanel panelBottom = new JPanel(new BorderLayout(5, 5));
        panelBottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtInput = new JTextField();
        txtInput.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnSend = new JButton("ENVIAR COMANDO");
        
        // Acción al enviar
        ActionListener sendAction = e -> {
            String command = txtInput.getText();
            if (!command.trim().isEmpty()) {
                controller.procesarComando(command);
                appendLog("> " + command); 
                txtInput.setText("");
            }
        };
        
        txtInput.addActionListener(sendAction);
        btnSend.addActionListener(sendAction);

        panelBottom.add(txtInput, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);
    }

    // Método auxiliar para crear paneles de guerreros
    private JPanel createFighterPanel(String title, Color barColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title, TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE));
        
        JLabel lblName = new JLabel("Esperando selección...");
        lblName.setForeground(Color.WHITE);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(100);
        bar.setStringPainted(true);
        bar.setForeground(barColor);
        bar.setBackground(Color.DARK_GRAY);
        
        p.add(lblName, BorderLayout.NORTH);
        p.add(bar, BorderLayout.SOUTH);
        
        // Guardar referencias en propiedades para acceso rápido
        p.putClientProperty("bar", bar);
        p.putClientProperty("label", lblName);
        return p;
    }

    private JLabel createStatValue(String title, String initialVal) {
        JLabel l = new JLabel(title + " " + initialVal);
        return l; // Simplificado para este ejemplo
    }
    
    private JLabel createStatLabel(String text, String val) {
         return new JLabel(text + " " + val);
    }

    // --- MÉTODOS PÚBLICOS PARA EL CONTROLADOR ---

    // Agrega texto al log central con timestamp
    public void appendLog(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        txtLog.append("[" + timeStamp + "] " + message + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength()); // Auto-scroll
    }

    // Actualiza la barra de vida (si llega info del servidor)
    public void updateMyHealth(int percentage) {
        barMyFighter.setValue(percentage);
        barMyFighter.setString(percentage + "%");
    }
    
    public void updateEnemyHealth(int percentage) {
        barEnemyFighter.setValue(percentage);
        barEnemyFighter.setString(percentage + "%");
    }

    public void setMyFighterName(String name) {
        lblMyFighterName.setText(name);
    }

    public void writeMessage(String msg) {
        appendLog(msg);
    }
    
    // --- Métodos para el Área de Juego ---
    
    public void setPlayerID(String id) {
        lblPlayerID.setText("Player ID: " + id);
    }
    
    public void addMyFighterCard(Fighter fighter) {
        FighterCard card = new FighterCard(fighter);
        pnlMyTeam.add(card);
        myFighterCards.put(fighter.getName(), card);
        pnlMyTeam.revalidate();
        pnlMyTeam.repaint();
    }
    
    public void updateMyFighterCard(String fighterName, int newHP) {
        FighterCard card = myFighterCards.get(fighterName);
        if (card != null) {
            card.updateHP(newHP);
        } else {
            appendLog("ERROR: No se encontró la tarjeta del guerrero '" + fighterName + "' para actualizar HP.");
        }
    }
    
    public void updateEnemyFighterDisplay(String fighterName, int newHP) {
        lblEnemyFighterName.setText(fighterName);
        barEnemyFighter.setValue(newHP);
        barEnemyFighter.setString(newHP + "%");
        if (newHP < 30) {
            barEnemyFighter.setForeground(Color.RED);
        } else if (newHP < 60) {
            barEnemyFighter.setForeground(Color.ORANGE);
        } else {
            barEnemyFighter.setForeground(Color.CYAN); // Or initial color
        }
    }
    
    public void displayOutgoingAttack(String myFighter, String weapon, String targetPlayer, String targetFighter, int damage) {
        JLabel lbl = (JLabel) ((BorderLayout)pnlOutgoingAttack.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (lbl != null) {
            lbl.setText(String.format("<html><center>Tu %s atacó con %s<br>a %s (%s)<br>Daño: %d%%</center></html>",
                    myFighter, weapon, targetPlayer, targetFighter, damage));
        }
    }
    
    public void displayIncomingAttack(String enemyPlayer, String enemyFighter, String weapon, String myFighter, int damage) {
        JLabel lbl = (JLabel) ((BorderLayout)pnlIncomingAttack.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (lbl != null) {
            lbl.setText(String.format("<html><center>%s (%s) te atacó con %s<br>a tu %s<br>Daño recibido: %d%%</center></html>",
                    enemyPlayer, enemyFighter, weapon, myFighter, damage));
        }
    }
    
    public void updatePlayerScores(int successful, int failed, int deaths, int wins, int defeats, int surrenders) {
        lblScoreSuccess.setText("Ataques Exitosos: " + successful);
        lblScoreFail.setText("Ataques Fallidos: " + failed);
        lblScoreDeaths.setText("Muertes: " + deaths);
        // Add updates for wins, defeats, surrenders if you add them to the UI
    }
    
    public void clearAttackDisplays() {
        ((JLabel) ((BorderLayout)pnlOutgoingAttack.getLayout()).getLayoutComponent(BorderLayout.CENTER)).setText("Esperando tu ataque...");
        ((JLabel) ((BorderLayout)pnlIncomingAttack.getLayout()).getLayoutComponent(BorderLayout.CENTER)).setText("Esperando ataque enemigo...");
    }

    public Player getLocalPlayer() {
        return controller.getLocalPlayer();
    }
}
