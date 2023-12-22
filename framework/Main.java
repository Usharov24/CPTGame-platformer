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
import objects.Apple;
import objects.Player;
import objects.Mango;
import objects.Bullet;
import objects.HomingBullet;

public class Main implements ActionListener {

    public static JFrame theFrame = new JFrame("CPT Game Proto");

    private CustomPanel[] thePanels = {new CustomPanel(null), new CustomPanel(null), new CustomPanel(null), new CustomPanel(null), new CustomPanel(null)};
    // TEMP public static
    private JPanel characterPanel = new JPanel(null);
    //public static int intjoinid;
    //public static int inthostid;
    //public static int intjoinhostid;

    // Map
    private MapPanel mapPanel = new MapPanel();

    // Main Menu Components
    private CustomButton[] mainMenuButtons = {new CustomButton(200, 100, null, this), new CustomButton(200, 100, null, this), 
                                              new CustomButton(200, 100, null, this), new CustomButton(200, 100, null, this)};
 
    // Back Buttons
    private CustomButton[] backButtons = {new CustomButton(100, 100, null, this), new CustomButton(100, 100, null, this), new CustomButton(100, 100, null, this)};

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
    private InputHandler input = new InputHandler();
    public static int[] intcharbutton = new int[4];
    private static int[] intpastcharbutton = new int[4];
    private Player[] players = {new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input), new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input), new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input), new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input)};
    /////////////////////////////////////////////////////////////////////////////

    private Timer timer = new Timer(1000/60, this);

    public static final float startTime = (float)System.currentTimeMillis();

    public static SuperSocketMaster ssm;

    public State state = State.MAIN_MENU;

    public enum State {
        MAIN_MENU(0), HOST_MENU(1), JOIN_MENU(2), SETTINGS(3), GAME(4);

        private final int intPanelNumber;

        State(int intPanelNumber) {
            this.intPanelNumber = intPanelNumber;
        }

        public int getValue() {
            return intPanelNumber;
        }
    }

    public static int intSessionId;
    private int intServerSize = 0;
    private boolean[] availableIds = {true, true, true};
    
    public Main() {
        for(int intCount = 0; intCount < thePanels.length; intCount++) {
            thePanels[intCount].setPreferredSize(new Dimension(1280, 720));
            thePanels[intCount].setFocusable((intCount == 4) ? true : false);
        }

        thePanels[4].addKeyListener(input);
        thePanels[4].addMouseListener(input);
        thePanels[4].addMouseMotionListener(input);
        
        // TEMP ///////
        
        handler.addObject(new Mango(300, 200, 0, 4, 30, 30, 0, 300, 100, 5, ObjectId.ENEMY_MANGO, handler));
        handler.addObject(new Apple(600, 300, 2, 2, 100, 100, 1400, 1400, 100, 20, ObjectId.ENEMY_APPLE, handler));
        ///////////////

        characterPanel.setPreferredSize(new Dimension(1280, 720));
        mapPanel.setPreferredSize(new Dimension(1280, 720));

        // Start Panel Components ///////////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < mainMenuButtons.length; intCount++) {
            mainMenuButtons[intCount].setLocation(540, 200 + 110 * intCount);
            thePanels[0].add(mainMenuButtons[intCount]);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Host & Join Panels /////////////////////////////////////////////////////////////////////
        // May combine loops
        // Subject to change as menu is refined
        for(int intCount = 0; intCount < netTextAreas.length; intCount++) {
            netTextAreas[intCount].setSize(600, 250);
            netTextAreas[intCount].setLocation(340, 50);
            netTextAreas[intCount].setEditable(false);
            thePanels[intCount + 1].add(netTextAreas[intCount]);
        }

        for(int intCount = 0; intCount < netTextFields.length; intCount++) {
            netTextFields[intCount].setSize(465, 50);
            netTextFields[intCount].setLocation(475, (intCount < 2) ? 330 + 75 * intCount : 330 + 75 * (intCount - 2));
            netTextFields[intCount].setEditable((intCount == 1) ? false : true);
            thePanels[(intCount < 2) ? 1 : 2].add(netTextFields[intCount]);
        }

        for(int intCount = 0; intCount < netButtons.length; intCount++) {
            netButtons[intCount].setSize(500, 70);
            netButtons[intCount].setLocation(390, 540);
            netButtons[intCount].addActionListener(this);
            thePanels[intCount + 1].add(netButtons[intCount]);
        }

        for(int intCount = 0; intCount < netLabels.length; intCount++){
            netLabels[intCount].setFont(new Font("Dialog", Font.BOLD, 18));
            netLabels[intCount].setSize(150, 50);
            netLabels[intCount].setLocation(300, (intCount < 2) ? 330 + 70 * intCount : 330 + 70 * (intCount - 2));
            thePanels[(intCount < 2) ? 1 : 2].add(netLabels[intCount]);
        }

        // Will redo this
        buttonStart.setSize(100, 100);
        buttonStart.setLocation(950, 175);
        buttonStart.addActionListener(this);
        thePanels[1].add(buttonStart);

        ///////////////////////////////////////////////////////////////////////////////////////////

        // Character Panel Components /////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            characterButtons[intCount].setSize(100,100);
            characterButtons[intCount].setLocation((intCount < 2) ? 100 + 100 * intCount : 100 + 100 * (intCount - 2), (intCount < 2) ? 100 : 200);
            characterButtons[intCount].addActionListener(this);
            characterPanel.add(characterButtons[intCount]);
        }

        // Back Buttons
        for(int intCount = 0; intCount < 3; intCount++){
            backButtons[intCount].setLocation(20, 20);
            backButtons[intCount].addActionListener(this);
            thePanels[intCount+1].add(backButtons[intCount]);
        }

        // Will redo this
        buttonReady.setSize(100, 100);
        buttonReady.setLocation(950, 175);
        buttonReady.addActionListener(this);
        characterPanel.add(buttonReady);
        ///////////////////////////////////////////////////////////////////////////////////////////

        theFrame.setContentPane(thePanels[0]);
        theFrame.pack();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setVisible(true);
        timer.start();
    }

    // Override actionPerformed Method
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == timer) {
            thePanels[state.getValue()].repaint();
            // Line not done
            // Need to get player information from handler
            if(state == State.GAME && intSessionId != 1 && ssm!=null) ssm.sendText("c" + intSessionId + ">oPLAYER~");
        }
        
        if(evt.getSource() == ssm) {
            String strMessage = ssm.readText();
            String[] strSelection = strMessage.split(",");

            if(intSessionId == 1) {
                if(strMessage.equals("c0>mJOIN")) {
                    intServerSize++;
                    System.out.println("Server Size: " + intServerSize);

                    if(availableIds[intServerSize - 2]) {
                        availableIds[intServerSize - 2] = false;
                        ssm.sendText("h>mSESSION_ID~" + intServerSize);
                    } else if(availableIds[intServerSize - 3]) {
                        availableIds[intServerSize - 3] = false;
                        ssm.sendText("h>mSESSION_ID~" + (intServerSize - 1));
                    } else if(availableIds[intServerSize - 4]) {
                        availableIds[intServerSize - 4] = false;
                        ssm.sendText("h>mSESSION_ID~" + (intServerSize - 2));
                    }
                } else if(strMessage.contains("mPLAYER_DISCONNECT")) {
                    intServerSize--;
                    availableIds[Integer.parseInt(strMessage.substring(1, 2))] = true;
                }
            } else if(!strMessage.substring(0, 1).equals("c")) {
                if(strMessage.contains("mSESSION_ID")) {
                    intSessionId = Integer.parseInt(strMessage.split("~")[1]);
                    System.out.println("Session Id: " + intSessionId);
                }
            }
            if(strMessage.substring(0, 1).equals("o")) {
                
            
                if (strSelection[1].equals("BULLET") && Integer.parseInt(strSelection[10]) != intSessionId){
                    handler.addObject(new Bullet(Float.parseFloat(strSelection[2]), Float.parseFloat(strSelection[3]), Float.parseFloat(strSelection[4]), Float.parseFloat(strSelection[5]), Float.parseFloat(strSelection[6]), Float.parseFloat(strSelection[7]), ObjectId.BULLET, handler));
                    
                    
            
                }
                if (strSelection[1].equals("HOMINGBULLET") && Integer.parseInt(strSelection[10]) != intSessionId){
                    handler.addObject(new HomingBullet(Float.parseFloat(strSelection[2]), Float.parseFloat(strSelection[3]), Float.parseFloat(strSelection[4]), Float.parseFloat(strSelection[5]), Float.parseFloat(strSelection[6]), Float.parseFloat(strSelection[7]), ObjectId.BULLET, handler, Integer.parseInt(strSelection[10])));
                }

                if (strSelection[1].equals("PLAYER")){
                    if(!handler.containObject(players[Integer.parseInt(strSelection[4])])){
                        players[Integer.parseInt(strSelection[4])] = new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input);
                        handler.addObject(players[Integer.parseInt(strSelection[4])]);

                    }
                    players[Integer.parseInt(strSelection[4])].setX(Float.parseFloat(strSelection[2]));
                    players[Integer.parseInt(strSelection[4])].setY(Float.parseFloat(strSelection[3]));
                }
            }   
            if(strMessage.substring(0,1).equals("c")) {
                String[] strInput = strMessage.split(",");
                
                //chatbox.append(strmessage[1] + "\n");
            } else if(strMessage.substring(0, 1).equals("m")) {
                if(strSelection[1].equals("start")) {
                    theFrame.setContentPane(characterPanel);
                    theFrame.pack();
                } else if(strSelection[1].equals("ready")) {
                    state = State.GAME;
                    theFrame.setContentPane(thePanels[4]);
                    theFrame.pack();
                    thePanels[4].requestFocus();
                } else if(strSelection[1].equals("join")) {
                
                } else if(strSelection[1].equals("charbutton")) {
                    intcharbutton[Integer.parseInt(strSelection[2])] = Integer.parseInt(strSelection[3]);
                    characterButtons[Integer.parseInt(strSelection[3])].setEnabled(false);
                } else if(strSelection[1].equals("oldbutton")) {
                    characterButtons[Integer.parseInt(strSelection[2])].setEnabled(true);
                }
            }
         


        }


        if(evt.getSource() == mainMenuButtons[0]) {
            state = State.HOST_MENU;
            theFrame.setContentPane(thePanels[1]);
            theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[1]) {
            state = State.JOIN_MENU;
            theFrame.setContentPane(thePanels[2]);
            theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[2]) {
            state = State.SETTINGS;
            theFrame.setContentPane(thePanels[3]);
            theFrame.pack();
        } else if(evt.getSource() == mainMenuButtons[3]) {
            System.exit(0);
        } else if(evt.getSource() == backButtons[0]){
            state = State.MAIN_MENU;
            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        } else if(evt.getSource() == backButtons[1]){
            state = State.MAIN_MENU;
            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        } else if(evt.getSource() == backButtons[2]){
            state = State.MAIN_MENU;
            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        }
        
        if(evt.getSource() == netButtons[0]) {
            backButtons[0].setVisible(false);
            ssm = new SuperSocketMaster(8080, this);
            ssm.connect();
            intSessionId = 1;
            intServerSize++;

            char[] chrCharacters = ssm.getMyAddress().toCharArray();

            for(int intCount = 0; intCount < chrCharacters.length; intCount++) {
                chrCharacters[intCount] += 55;
            }

            netTextFields[1].setText(new String(chrCharacters));
        
            buttonStart.setEnabled(true);
            netButtons[0].setEnabled(false);
            //playerArea[0].setText(name[0].getText()+" 👑");
        } else if(evt.getSource() == netButtons[1]) {
            backButtons[1].setVisible(false);
            char[] chrJoinCode = netTextFields[3].getText().toCharArray();

            for(int intCount = 0; intCount < chrJoinCode.length; intCount++) {
                chrJoinCode[intCount] -= 55;
            }

            ssm = new SuperSocketMaster(new String(chrJoinCode), 8080, this);
            ssm.connect();

            ssm.sendText("c0>mJOIN");

            // Display
            netButtons[1].setEnabled(false);
        }

        for(int intCount = 0; intCount < 4; intCount++) {
            if(evt.getSource() == characterButtons[intCount]) {
                intpastcharbutton[intSessionId] = intcharbutton[intSessionId];
                characterButtons[intpastcharbutton[intSessionId]].setEnabled(true);
                ssm.sendText("m,oldbutton," + intpastcharbutton[intSessionId]);
                intcharbutton[intSessionId] = intCount;
                characterButtons[intCount].setEnabled(false);

                
                ssm.sendText("m,charbutton," + (intSessionId) + "," + intCount + "," + intpastcharbutton[intSessionId]);
                
            }
        }

        if(evt.getSource() == buttonStart) {
            if(ssm != null){
                ssm.sendText("m,start");
            }
            theFrame.setContentPane(characterPanel);
            theFrame.pack();
            //ssm.sendText("m,start");
        }

        if(evt.getSource() == buttonReady) {
            if(ssm != null){
                ssm.sendText("m,ready");
            }
            state = State.GAME;
            players[intSessionId] = new Player(0, 0, 32, 32, ObjectId.PLAYER_LOCAL, handler, input);
            handler.addObject(players[intSessionId]);

            theFrame.setContentPane(thePanels[4]);
            theFrame.pack();
            thePanels[4].requestFocus();

            //ssm.sendText("m,ready");

            
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}