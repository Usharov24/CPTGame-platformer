package framework;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import components.*;
import objects.Player;

public class Main implements ActionListener {

    public static JFrame theFrame = new JFrame("CPT Game Proto");

    private CustomPanel[] menuPanels = {new CustomPanel(null), new CustomPanel(null), new CustomPanel(null), new CustomPanel(null)};
    // TEMP public static
    public static CustomPanel gamePanel = new CustomPanel(null);
    public static JPanel characterPanel = new JPanel(null);
    public static int intjoinid;
    public static int inthostid;
    public static int intjoinhostid;

    // Main Menu Components
    private CustomButton[] mainMenuButtons = {new CustomButton(200, 100, null, this), new CustomButton(200, 100, null, this), 
                                              new CustomButton(200, 100, null, this), new CustomButton(200, 100, null, this)};

    // Host & Join Components
    private JTextArea[] netTextAreas = {new JTextArea(), new JTextArea()};
    private JTextField[] netTextFields = {new JTextField(), new JTextField(), new JTextField(), new JTextField()};
    private JButton[] netButtons = {new JButton("Host Game"), new JButton("Join Game")};
    private JLabel[] netLabels = {new JLabel("Enter Name"), new JLabel("Join Code"), new JLabel("Enter Name"), new JLabel("Enter Join Code")};
    
    // WIP
    public static JButton[] characterButtons = {new JButton("Sniper"), new JButton("Brute"), new JButton("Knight"), new JButton("Wizard")};
    private JButton buttonStart = new JButton("Start game");
    private JButton buttonReady = new JButton("Ready");

    // TEMPORARY ////////////////////////////////////////////////////////////////
    public static ObjectHandler handler = new ObjectHandler();
    private Player player = new Player(0, 0, 32, 32, ObjectId.PLAYER, handler);
    public static int[] intcharbutton = new int[4];
    private static int[] intpastcharbutton = new int[4];

    private SuperSocketMaster ssm;
    private Network network;

    /////////////////////////////////////////////////////////////////////////////

    // Timer
    private Timer timer = new Timer(1000/60, this);
    
    public Main() {
        for(int intCount = 0; intCount < menuPanels.length; intCount++) {
            menuPanels[intCount].setPreferredSize(new Dimension(1280, 720));
            menuPanels[intCount].setFocusable(true);
        }

        // TEMP ///////
        handler.addObject(player);
        gamePanel.addKeyListener(player);
        gamePanel.addMouseListener(player);
        ///////////////

        gamePanel.setPreferredSize(new Dimension(1280, 720));
        gamePanel.setFocusable(true);

        characterPanel.setPreferredSize(new Dimension(1280, 720));

        // Start Panel Components ///////////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < mainMenuButtons.length; intCount++) {
            mainMenuButtons[intCount].setLocation(540, 200 + 110 * intCount);
            menuPanels[0].add(mainMenuButtons[intCount]);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Host & Join Panels /////////////////////////////////////////////////////////////////////
        // May combine loops
        // Subject to change as menu is refined
        for(int intCount = 0; intCount < netTextAreas.length; intCount++) {
            netTextAreas[intCount].setSize(600, 250);
            netTextAreas[intCount].setLocation(340, 50);
            netTextAreas[intCount].setEditable(false);
            menuPanels[intCount + 1].add(netTextAreas[intCount]);
        }

        for(int intCount = 0; intCount < netTextFields.length; intCount++) {
            netTextFields[intCount].setSize(465, 50);
            netTextFields[intCount].setLocation(475, (intCount < 2) ? 330 + 75 * intCount : 330 + 75 * (intCount - 2));
            netTextFields[intCount].setEditable((intCount == 1) ? false : true);
            menuPanels[(intCount < 2) ? 1 : 2].add(netTextFields[intCount]);
        }

        for(int intCount = 0; intCount < netButtons.length; intCount++) {
            netButtons[intCount].setSize(500, 70);
            netButtons[intCount].setLocation(390, 540);
            netButtons[intCount].addActionListener(this);
            menuPanels[intCount + 1].add(netButtons[intCount]);
        }

        for(int intCount = 0; intCount < netLabels.length; intCount++){
            netLabels[intCount].setFont(new Font("Dialog", Font.BOLD, 18));
            netLabels[intCount].setSize(150, 50);
            netLabels[intCount].setLocation(300, (intCount < 2) ? 330 + 70 * intCount : 330 + 70 * (intCount - 2));
            menuPanels[(intCount < 2) ? 1 : 2].add(netLabels[intCount]);
        }

        // Will redo this
        buttonStart.setSize(100, 100);
        buttonStart.setLocation(950, 175);
        buttonStart.addActionListener(this);
        menuPanels[1].add(buttonStart);

        ///////////////////////////////////////////////////////////////////////////////////////////

        // Character Panel Components /////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            characterButtons[intCount].setSize(100,100);
            characterButtons[intCount].setLocation((intCount < 2) ? 100 + 100 * intCount : 100 + 100 * (intCount - 2), (intCount < 2) ? 100 : 200);
            characterButtons[intCount].addActionListener(this);
            characterPanel.add(characterButtons[intCount]);
        }

        // Will redo this
        buttonReady.setSize(100, 100);
        buttonReady.setLocation(950, 175);
        buttonReady.addActionListener(this);
        characterPanel.add(buttonReady);
        ///////////////////////////////////////////////////////////////////////////////////////////

        theFrame.setContentPane(menuPanels[0]);
        theFrame.pack();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setVisible(true);
        timer.start();
    }

    // Override actionPerformed Method
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == timer) {
            menuPanels[0].repaint();

            if(theFrame.getContentPane() == menuPanels[1] && ssm != null && inthostid == 1) {
                network.sendMessage("m,join," + intjoinhostid);
                intjoinid = 0;
            }
        }

        if(evt.getSource() == ssm) {
            network.readMessage();
        }

        if(evt.getSource() == mainMenuButtons[0]) {
            theFrame.setContentPane(menuPanels[1]);
            theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[1]) {
            theFrame.setContentPane(menuPanels[2]);
            theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[2]) {
           theFrame.setContentPane(menuPanels[3]);
           theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[3]) {
            System.exit(0);
        }
        
        if(evt.getSource() == netButtons[0]) {
            ssm = new SuperSocketMaster(8080, this);
            network = new Network(ssm);

            char[] chrCharacters = ssm.getMyAddress().toCharArray();

            for(int intCount = 0; intCount < chrCharacters.length; intCount++) {
                chrCharacters[intCount] += 55;
            }

            netTextFields[1].setText(new String(chrCharacters));

            inthostid = 1;
        
            buttonStart.setEnabled(true);
            netButtons[0].setEnabled(false);
            //playerArea[0].setText(name[0].getText()+" 👑");
            menuPanels[1].repaint();
            
            ssm.connect();
        } else if(evt.getSource() == netButtons[1]) {
            char[] chrJoinCode = netTextFields[3].getText().toCharArray();

            for(int intCount = 0; intCount < chrJoinCode.length; intCount++) {
                chrJoinCode[intCount] -= 55;
            }

            ssm = new SuperSocketMaster(new String(chrJoinCode), 8080, this);
            network = new Network(ssm);
            
            inthostid = 1;

            // Display
            netButtons[1].setEnabled(false);
            menuPanels[2].repaint();
            
            ssm.connect();
        }

        for(int intCount = 0; intCount < 4; intCount++) {
            if(evt.getSource() == characterButtons[intCount]) {
                intpastcharbutton[intjoinid] = intcharbutton[intjoinid];
                characterButtons[intpastcharbutton[intjoinid]].setEnabled(true);
                ssm.sendText("m,oldbutton," + intpastcharbutton[intjoinid]);
                intcharbutton[intjoinid] = intCount;
                characterButtons[intCount].setEnabled(false);

                System.out.println(intjoinid);
                ssm.sendText("m,charbutton," + intjoinid + "," + intCount + "," + intpastcharbutton[intjoinid]);
            }
        }

        if(evt.getSource() == buttonStart) {
            theFrame.setContentPane(characterPanel);
            theFrame.pack();
            ssm.sendText("m,start");
            if(inthostid == 1){
                intjoinid = 0;
            }
        }

        if(evt.getSource() == buttonReady) {
            gamePanel.requestFocus();
            theFrame.setContentPane(gamePanel);
            theFrame.pack();
            ssm.sendText("m,ready");
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}