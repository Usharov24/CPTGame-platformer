package framework;
import java.awt.Container;
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
import javax.swing.OverlayLayout;
import javax.swing.Timer;

import components.*;
import objects.Apple;
import objects.Bullet;
import objects.HomingBullet;
import objects.Mango;
import objects.Player;
import objects.Wizard;
import objects.Sniper;

public class Main implements ActionListener {

    public static JFrame theFrame = new JFrame("CPT Game Proto");

    private CustomPanel[] thePanels = {new CustomPanel(null), new CustomPanel(null), new CustomPanel(null), new CustomPanel(null), new CustomPanel(null)};
    private JPanel characterPanel = new JPanel(null);

    // Map
    private MapPanel mapPanel = new MapPanel();
    private Container gameContentPane = theFrame.getContentPane();

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
    private JButton netStartButton = new JButton("Start game");
    
    // WIP
    public static JButton[] characterButtons = {new JButton("Sniper"), new JButton("Brute"), new JButton("Knight"), new JButton("Wizard")};
    private JButton buttonReady = new JButton("Ready");

    // TEMPORARY ////////////////////////////////////////////////////////////////
    public static ObjectHandler handler = new ObjectHandler();
    private InputHandler input = new InputHandler();
    /////////////////////////////////////////////////////////////////////////////

    private Timer timer = new Timer(1000/60, this);

    public static long startTime = System.currentTimeMillis();

    private SuperSocketMaster ssm;

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
    private int intCurrentButton = -1, intPreviousButton = -1;
    private int[] intCharacterSelections = new int[4];

    private boolean[] availableIds = {true, true, true};
    
    public Main() {
        for(int intCount = 0; intCount < thePanels.length; intCount++) {
            thePanels[intCount].setPreferredSize(new Dimension(1280, 720));
            thePanels[intCount].setFocusable((intCount == 4) ? true : false);
        }

        mapPanel.addKeyListener(input);
        mapPanel.addMouseListener(input);
        mapPanel.addMouseMotionListener(input);
        mapPanel.setFocusable(true);
        
        // TEMP ///////
        
        handler.addObject(new Mango(300, 200, 0, 4, 30, 30, 0, 300, 100, 5, ObjectId.ENEMY_MANGO, ssm, handler));
        handler.addObject(new Apple(600, 300, 2, 2, 100, 100, 1400, 1400, 100, 20, ObjectId.ENEMY_APPLE, ssm, handler));
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
        netStartButton.setSize(100, 100);
        netStartButton.setLocation(950, 175);
        netStartButton.setEnabled(false);
        netStartButton.addActionListener(this);
        thePanels[1].add(netStartButton);

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
        if(evt.getSource() == timer){
            thePanels[state.getValue()].repaint();
            mapPanel.repaint();
        }
        if(evt.getSource() == ssm) {
            String strMessage = ssm.readText();
            System.out.println(strMessage);

            if(intSessionId == 1) {
                if(strMessage.contains("oPLAYER")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    // Temporary setup until a better system for object identification is made
                    for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
                        Player object = null;
                        try {
                            object = (Player)handler.objectList.get(intCount);

                            if(object.getSessionId() == Integer.parseInt(strPayload[2])) {
                                object.setX(Float.parseFloat(strPayload[0]));
                                object.setY(Float.parseFloat(strPayload[1]));
                                break;
                            }
                        } catch(ClassCastException e) {
                            //System.out.println("Cannot cast class to Player...");
                        }
                    }

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oPLAYER~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oPLAYER~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oPLAYER~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, ssm, handler));
                
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBULLET~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aHOMING_BULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new HomingBullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.HOMING_BULLET, ssm, handler));
                
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aHOMING_BULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aHOMING_BULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aHOMING_BULLET~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("mJOIN")) {
                    intServerSize++;
                    System.out.println("Server Size: " + intServerSize);

                    if(availableIds[intServerSize - 2]) {
                        availableIds[intServerSize - 2] = false;
                        ssm.sendText("h>c0>mSESSION_ID~" + intServerSize);
                    } else if(availableIds[intServerSize - 3]) {
                        availableIds[intServerSize - 3] = false;
                        ssm.sendText("h>c0>mSESSION_ID~" + (intServerSize - 1));
                    } else if(availableIds[intServerSize - 4]) {
                        availableIds[intServerSize - 4] = false;
                        ssm.sendText("h>c0>mSESSION_ID~" + (intServerSize - 2));
                    }
                } else if(strMessage.contains("mPLAYER_DISCONNECT")) {
                    intServerSize--;
                    availableIds[Integer.parseInt(strMessage.substring(1, 2))] = true;
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);

                    intCharacterSelections[Integer.parseInt(strMessage.substring(1, 2)) - 1] = Integer.parseInt(strPayload[0]);

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                }
            } else if(!strMessage.substring(0, 1).equals("c") && (strMessage.split(">")[1].equals("a") || Integer.parseInt(strMessage.substring(3, 4)) == intSessionId)) {
                if(strMessage.contains("oPLAYER")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    // Temporary setup until a better system for object identification is made
                    for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
                        Player object = null;
                        try {
                            object = (Player)handler.objectList.get(intCount);

                            if(object.getSessionId() == Integer.parseInt(strPayload[2])) {
                                object.setX(Float.parseFloat(strPayload[0]));
                                object.setY(Float.parseFloat(strPayload[1]));
                                break;
                            }
                        } catch(ClassCastException e) {
                            //System.out.println("Cannot cast class to Player...");
                        }
                    }
                } else if(strMessage.contains("aPLAYER")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Player(Integer.parseInt(strPayload[0]), Integer.parseInt(strPayload[1]), Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), ObjectId.PLAYER, ssm, handler, input, Integer.parseInt(strPayload[4])));
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, ssm, handler));
                } else if(strMessage.contains("aHOMING_BULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new HomingBullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.HOMING_BULLET, ssm, handler));
                } else if(strMessage.contains("mSESSION_ID")) {
                    intSessionId = Integer.parseInt(strMessage.split("~")[1]);
                    System.out.println("Session Id: " + intSessionId);
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);
                } else if(strMessage.contains("mCHARACTER_PANEL")) {
                    theFrame.setContentPane(characterPanel);
                    theFrame.pack();
                } else if(strMessage.contains("mGAME_PANEL")) {
                    state = State.GAME;

                    gameContentPane = theFrame.getContentPane();
                    gameContentPane.setLayout(new OverlayLayout(gameContentPane));
                    gameContentPane.add(mapPanel);

                    theFrame.pack();
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
        } else if(evt.getSource() == backButtons[0]) {
            state = State.MAIN_MENU;
            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        } else if(evt.getSource() == backButtons[1]) {
            state = State.MAIN_MENU;
            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        } else if(evt.getSource() == backButtons[2]) {
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
        
            netStartButton.setEnabled(true);
            netButtons[0].setEnabled(false);
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

        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            if(evt.getSource() == characterButtons[intCount]) {
                intPreviousButton = intCurrentButton;
                intCurrentButton = intCount;

                if(intSessionId == 1) ssm.sendText("h>a>mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);
                else ssm.sendText("c" + intSessionId + ">mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);

                if(intSessionId == 1) intCharacterSelections[0] = intCurrentButton;
                
                if(intPreviousButton != -1) characterButtons[intPreviousButton].setEnabled(true);
                characterButtons[intCount].setEnabled(false);
            }
        }

        if(evt.getSource() == netStartButton) {
            ssm.sendText("h>a>mCHARACTER_PANEL");

            theFrame.setContentPane(characterPanel);
            theFrame.pack();
        }

        if(evt.getSource() == buttonReady) {
            ssm.sendText("h>a>mGAME_PANEL");
            startTime = System.currentTimeMillis();

            for(int intCount = 0; intCount < intServerSize; intCount++) {
                handler.addObject(new Sniper(0 + 75 * intCount, 300, 32, 32, ObjectId.PLAYER, ssm, handler, input, intCount + 1));
                // Need to find a way to specify character/class as well
                ssm.sendText("h>a>aPLAYER~" + (0 + 75 * intCount) + "," + 300 + "," + 32 + "," + 32 + "," + (intCount + 1));
            }

            state = State.GAME;

            gameContentPane = theFrame.getContentPane();
            gameContentPane.setLayout(new OverlayLayout(gameContentPane));
            gameContentPane.add(mapPanel);

            theFrame.pack();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}