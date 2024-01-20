package framework;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import components.*;
import objects.*;
import java.awt.image.BufferedImage;

public class Main implements ActionListener{

    // Frame
    public static JFrame theFrame = new JFrame("CPT Game Proto");

    private CustomPanel[] thePanels = {new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, true), new CustomPanel(null, true)};

    // Game Layered Pane
    private JLayeredPane gameLayeredPane = new JLayeredPane();

    // Map
    private MapPanel mapPanel = new MapPanel();
    
    // Chat
    private ChatPanel chatPanel;

    public static ObjectHandler handler = new ObjectHandler();
    private InputHandler input = new InputHandler();
    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage[] biCharacterButtons = resLoader.loadSpriteSheet("/res\\CharacterButtons.png", 300, 300);
    // Main Menu Components
    private CustomButton[] mainMenuButtons = {new CustomButton(200, 100, "Host", null, this), new CustomButton(200, 100, "Join",null, this), 
                                              new CustomButton(200, 100, "Help", null, this), new CustomButton(200, 100, "Quit",null, this)};
 
    // Back Buttons
    private CustomButton[] backButtons = {new CustomButton(225, 100, "Back", null, this), new CustomButton(225, 100, "Back", null, this), new CustomButton(225, 100, "Back", null, this)};

    // Host & Join Components
    private JTextArea[] netTextAreas = {new JTextArea(), new JTextArea()};
    private JTextField[] netTextFields = {new JTextField(), new JTextField(), new JTextField(), new JTextField()};
    private CustomButton[] netButtons = {new CustomButton(500, 70, "Host Game", null, this), new CustomButton(500, 70, "Join Game", null, this)};
    private CustomButton netStartButton = new CustomButton(100, 100, "Start", null, this);
    private JLabel[] netLabels = {new JLabel("Enter Name"), new JLabel("Join Code"), new JLabel("Enter Name"), new JLabel("Enter Join Code")};
    private String[] strNameList = {"", "", "", "",};
    
    // WIP
    private CustomButton[] characterButtons = {new CustomButton(290, 290, null, biCharacterButtons, this), new CustomButton(290, 290, null, biCharacterButtons, this), new CustomButton(290, 290, null, biCharacterButtons, this), new CustomButton(290, 290, null, biCharacterButtons, this)};
    private CustomButton readyButton = new CustomButton(800, 80, "Ready", null, this);

    //All Sprites loaded
    private BufferedImage[] biBulletTextures = resLoader.loadImages("/res\\SniperBullet.png", "/res\\Rocket.png", "/res\\FireBall.png", "/res\\ElectricBall.png", "/res\\Shrapnel.png");    private ImageIcon ioLogo = new ImageIcon(getClass().getResource("/res/ioLogo.png"));
    private Timer timer = new Timer(1000/60, this);
    public static long startTime = System.nanoTime();

    private SuperSocketMaster ssm;

    public static State state = State.MAIN_MENU;

    public enum State {
        MAIN_MENU(0), HOST_MENU(1), JOIN_MENU(2), HELP(3), GAME(4), CHARACTER(5);

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
    private int[] intCharacterSelections = {-1, -1, -1, -1};
    private int intCharacterCheck = 0;
    private boolean[] availableIds = {true, true, true};
    
    public Main() {
        for(int intCount = 0; intCount < thePanels.length; intCount++) {
            thePanels[intCount].setPreferredSize(new Dimension(1280, 720));
        }

        // Listeners
        thePanels[4].addKeyListener(input);
        thePanels[4].addMouseListener(input);
        thePanels[4].addMouseMotionListener(input);

        gameLayeredPane.setPreferredSize(new Dimension(1280, 720));
        gameLayeredPane.setLayout(null);
        mapPanel.setPreferredSize(new Dimension(1280, 720));
        thePanels[5].setPreferredSize(new Dimension(1280, 720));

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
            netTextFields[intCount].addActionListener(this);
            thePanels[(intCount < 2) ? 1 : 2].add(netTextFields[intCount]);
        }

        for(int intCount = 0; intCount < netButtons.length; intCount++) {
            netButtons[intCount].setLocation(390, 540);
            netButtons[intCount].setEnabled(false);
            thePanels[intCount + 1].add(netButtons[intCount]);
        }

        for(int intCount = 0; intCount < netLabels.length; intCount++){
            netLabels[intCount].setFont(new Font("Dialog", Font.BOLD, 18));
            netLabels[intCount].setSize(150, 50);
            netLabels[intCount].setLocation(300, (intCount < 2) ? 330 + 70 * intCount : 330 + 70 * (intCount - 2));
            thePanels[(intCount < 2) ? 1 : 2].add(netLabels[intCount]);
        }

        netStartButton.setLocation(950, 175);
        netStartButton.setEnabled(false);
        thePanels[1].add(netStartButton);

        // Character Panel Components /////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            characterButtons[intCount].setLocation((intCount < 2) ? 345 + 300 * intCount : 345 + 300 * (intCount - 2), (intCount < 2) ? 15 : 315);
            thePanels[5].add(characterButtons[intCount]);
        }

        readyButton.setLocation(240, 620);
        readyButton.setEnabled(false);
        thePanels[5].add(readyButton);

        // Back Buttons
        for(int intCount = 0; intCount < backButtons.length; intCount++) {
            backButtons[intCount].setLocation(20, 20);
            backButtons[intCount].addActionListener(this);
            thePanels[intCount + 1].add(backButtons[intCount]);
        }

        theFrame.setContentPane(thePanels[0]);  
        theFrame.setIconImage(ioLogo.getImage());
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
                if(strMessage.substring(5, 6).equals("o")){
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    GameObject object = handler.getObject(Integer.parseInt(strPayload[2]));
                    System.out.println("1st " + object.getWorldX() + " " + object.getWorldY());
                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                    System.out.println("2nd " + object.getWorldX() + " " + object.getWorldY());
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oSNIPER~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oSNIPER~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oSNIPER~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Integer.parseInt(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),0,Integer.parseInt(strPayload[9]),Float.parseFloat(strPayload[10]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[11])], Boolean.parseBoolean(strPayload[12]), Float.parseFloat(strPayload[13]), -1));
                    
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBULLET~" + strMessage.split("~")[1]);
                }  else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Float.parseFloat(strPayload[7]),Integer.parseInt(strPayload[8]),Float.parseFloat(strPayload[9]),Integer.parseInt(strPayload[10]),Float.parseFloat(strPayload[11]),Integer.parseInt(strPayload[12]), ObjectId.WAVE, handler, ssm));

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aWAVE~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aWAVE~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aWAVE~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),System.currentTimeMillis(),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),Integer.parseInt(strPayload[9]),-1,Integer.parseInt(strPayload[10]), ObjectId.SLASH, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aSLASH~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),System.currentTimeMillis() + 200,Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),Integer.parseInt(strPayload[9]),-1,Integer.parseInt(strPayload[10]), ObjectId.SLASH, handler, ssm));

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBIGSLASH~" + strMessage.split("~")[1]);
                }else if(strMessage.contains("aBOOM")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Explosion(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), ObjectId.BOOM, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBOOM~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBOOM~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBOOM~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("mJOIN")) {
                    intServerSize++;
                    System.out.println("Player Joined\nServer Size: " + intServerSize);

                    strNameList[intServerSize-1] = strMessage.split("~")[1];
                    netTextAreas[0].append("\n " + strNameList[intServerSize-1]);
                    
                    for(int i = 0; i < intServerSize; i++){
                        ssm.sendText("h>c0>mNAME_LIST~" + (strNameList[i]) + "," + i);
                    }

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
                    System.out.println("Player Disconnected\nServer Size: " + intServerSize);
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
            } else if(!strMessage.substring(0, 1).equals("c") && (strMessage.split(">")[1].equals("a") || Integer.parseInt(strMessage.substring(3, 4)) == intSessionId) || strMessage.contains("NAME_LIST")) {
                if(strMessage.substring(4, 5).equals("o") || strMessage.substring(5, 6).equals("o")){
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    GameObject object = handler.getObject(Integer.parseInt(strPayload[2]));

                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                } else if(strMessage.contains("aSNIPER")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Sniper(Integer.parseInt(strPayload[0]), Integer.parseInt(strPayload[1]), Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), ObjectId.PLAYER, handler, ssm, input, Integer.parseInt(strPayload[4])));
                } else if(strMessage.contains("aWIZARD")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Wizard(Integer.parseInt(strPayload[0]), Integer.parseInt(strPayload[1]), Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), ObjectId.PLAYER, handler, ssm, input, Integer.parseInt(strPayload[4])));
                } else if(strMessage.contains("aKNIGHT")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Knight(Integer.parseInt(strPayload[0]), Integer.parseInt(strPayload[1]), Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), ObjectId.PLAYER, handler, ssm, input, Integer.parseInt(strPayload[4])));
                } else if(strMessage.contains("aBRUTE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Brute(Integer.parseInt(strPayload[0]), Integer.parseInt(strPayload[1]), Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), ObjectId.PLAYER, handler, ssm, input, Integer.parseInt(strPayload[4])));
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Integer.parseInt(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),0,Integer.parseInt(strPayload[9]),Float.parseFloat(strPayload[10]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[11])], Boolean.parseBoolean(strPayload[12]), Float.parseFloat(strPayload[13]), -1));
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),System.currentTimeMillis(),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),Integer.parseInt(strPayload[9]),-1,Integer.parseInt(strPayload[10]), ObjectId.SLASH, handler, ssm));
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),System.currentTimeMillis() + 200,Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),Integer.parseInt(strPayload[9]),-1,Integer.parseInt(strPayload[10]), ObjectId.SLASH, handler, ssm));
                } else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Float.parseFloat(strPayload[7]),Integer.parseInt(strPayload[8]),Float.parseFloat(strPayload[9]),Integer.parseInt(strPayload[10]),Float.parseFloat(strPayload[11]),Integer.parseInt(strPayload[12]), ObjectId.WAVE, handler, ssm));
                } else if(strMessage.contains("aBOOM")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Explosion(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), ObjectId.BOOM, handler, ssm));
                } else if(strMessage.contains("mSESSION_ID")) {
                    intSessionId = Integer.parseInt(strMessage.split("~")[1]);
                    System.out.println("Session Id: " + intSessionId);
                } else if(strMessage.contains("mNAME_LIST")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    strNameList[Integer.parseInt(strPayload[1])] = strPayload[0];
                    netTextAreas[1].setText(" ");
                    for(int i = 0; i < 4; i++){
                        if(i == 0){
                            netTextAreas[1].append(" " + strNameList[i]);
                        }
                        else{
                            netTextAreas[1].append(" \n" + strNameList[i]);
                        }
                    }
                    System.out.println("Session Id: " + intSessionId);
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);
                } else if(strMessage.contains("mCHARACTER_PANEL")) {
                    theFrame.setContentPane(thePanels[5]);
                    theFrame.pack();
                    state = State.CHARACTER;
                } else if(strMessage.contains("mGAME_PANEL")) {
                    state = State.GAME;

                    chatPanel = new ChatPanel(ssm);

                    handler.addObject(new Barrier(0, 1440, 1920, 30, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(0, -30, 1920, 30, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(-30, 0, 30, 1440, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(1920, 0, 30, 1440, ObjectId.BARRIER, handler, null));

                    thePanels[4].setVisible(true);
                    thePanels[4].setOpaque(true);
                    thePanels[4].setBounds(0, 0, 1280, 720);

                    mapPanel.setVisible(true);
                    mapPanel.setOpaque(true);
                    mapPanel.setBounds(0, 0, 1280, 720);

                    chatPanel.setVisible(true);
                    chatPanel.setOpaque(true);
                    chatPanel.setBounds(880, 0, 400, 720);

                    gameLayeredPane.add(thePanels[4], Integer.valueOf(100));
                    gameLayeredPane.add(mapPanel, Integer.valueOf(101));
                    gameLayeredPane.add(chatPanel, Integer.valueOf(102));
                    gameLayeredPane.repaint();

                    theFrame.setContentPane(thePanels[4]);
                    gameLayeredPane.requestFocus(true);
                    thePanels[4].requestFocus(true);
                    chatPanel.requestFocus(true);
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
            state = State.HELP;
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

        if(evt.getSource() == netTextFields[0]){
            if(netTextFields[0].getText().toString().isEmpty() == false){
                netButtons[0].setEnabled(true);
            }
        }

        if(evt.getSource() == netTextFields[2]){
            if(netTextFields[2].getText().toString().isEmpty() == false){
                netButtons[1].setEnabled(true);
            }
        }
        
        if(evt.getSource() == netButtons[0]) {
            backButtons[0].setVisible(false);
            ssm = new SuperSocketMaster(8080, this);
            ssm.connect();
            intSessionId = 1;
            netTextFields[0].setEnabled(false);
            intServerSize++;

            char[] chrCharacters = ssm.getMyAddress().toCharArray();

            for(int intCount = 0; intCount < chrCharacters.length; intCount++) {
                chrCharacters[intCount] += 55;
            }

            netTextFields[1].setText(new String(chrCharacters));
            netButtons[0].setEnabled(false);
            netTextAreas[0].append(" " + netTextFields[0].getText());
            strNameList[0] = netTextFields[0].getText().toString();
            netStartButton.setEnabled(true);
        } else if(evt.getSource() == netButtons[1]) {
            if(netTextFields[2].getText().toString().isEmpty() == false){
                netButtons[1].setEnabled(false);
                backButtons[1].setVisible(false);
                char[] chrJoinCode = netTextFields[3].getText().toCharArray();
                for(int intCount = 0; intCount < chrJoinCode.length; intCount++) {
                    chrJoinCode[intCount] -= 55;
                }
                netTextFields[2].setEnabled(false);

                ssm = new SuperSocketMaster(new String(chrJoinCode), 8080, this);
                ssm.connect();
                ssm.sendText("c0>h>mJOIN~"+netTextFields[2].getText().toString());
                netButtons[1].setEnabled(false);
            }
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

        for(int intCount = 0; intCount < 4; intCount++){
            if(intCharacterSelections[intCount] > -1){
                intCharacterCheck++;
            }
            
            if(intCharacterCheck == intServerSize && intServerSize > 0){
                readyButton.setEnabled(true);
            }
        }
        intCharacterCheck = 0;

        if(evt.getSource() == netStartButton) {
            ssm.sendText("h>a>mCHARACTER_PANEL");
            theFrame.setContentPane(thePanels[5]);
            theFrame.pack();
            state = State.CHARACTER;
        }

        if(evt.getSource() == readyButton) {
            startTime = System.nanoTime();
            chatPanel = new ChatPanel(ssm);
            chatPanel.setPreferredSize(new Dimension(400, 720));

            for(int intCount = 0; intCount < intServerSize; intCount++) {
                if(intCharacterSelections[intCount] == 0) {
                    handler.addObject(new Sniper(0 + 75 * intCount, 300, 32, 32, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aSNIPER~" + (0 + 75 * intCount) + "," + 300 + "," + 32 + "," + 32 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 1) {
                    handler.addObject(new Brute(0 + 75 * intCount, 300, 32, 32, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aBRUTE~" + (0 + 75 * intCount) + "," + 300 + "," + 32 + "," + 32 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 2) {
                    handler.addObject(new Knight(0 + 75 * intCount, 300, 32, 32, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aKNIGHT~" + (0 + 75 * intCount) + "," + 300 + "," + 32 + "," + 32 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 3) {
                    handler.addObject(new Wizard(0 + 75 * intCount, 300, 32, 32, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aWIZARD~" + (0 + 75 * intCount) + "," + 300 + "," + 32 + "," + 32 + "," + intCount);
                }
            }

            ssm.sendText("h>a>mGAME_PANEL");

            for(int intCount = 0; intCount < 2; intCount++) {
                handler.addObject(new Barrier(0, (intCount == 0) ? 1440 : -30, 1920, 30, ObjectId.BARRIER, handler, null));
                handler.addObject(new ItemObject(200, 200, 20, 20, ObjectId.ITEM, handler,ssm));
                handler.addObject(new Barrier((intCount == 0) ? -30 : 1920, 0, 30, 1440, ObjectId.BARRIER, handler, null));
            }

            handler.addObject(new Enemy(100,300,0,0,50,59, 10, 3, 1, ObjectId.ENEMY,handler,ssm));
            
            state = State.GAME;

            thePanels[4].setVisible(true);
            thePanels[4].setOpaque(true);
            thePanels[4].setBounds(0, 0, 1280, 720);

            mapPanel.setVisible(true);
            mapPanel.setOpaque(true);
            mapPanel.setBounds(0, 0, 1280, 720);

            chatPanel.setVisible(true);
            chatPanel.setOpaque(true);
            chatPanel.setBounds(880, 0, 400, 720);

            gameLayeredPane.add(thePanels[4], Integer.valueOf(100));
            gameLayeredPane.add(mapPanel, Integer.valueOf(101));
            gameLayeredPane.add(chatPanel, Integer.valueOf(102));
            gameLayeredPane.repaint();

            theFrame.setContentPane(thePanels[4]);
            gameLayeredPane.requestFocus(true);
            thePanels[4].requestFocus(true);
            chatPanel.requestFocus(true);
            theFrame.pack();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}