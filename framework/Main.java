package framework;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import components.*;
import objects.*;
import java.awt.image.BufferedImage;

public class Main implements ActionListener, WindowListener {

    // Frame
    public static JFrame theFrame = new JFrame("Annihilation Station");
    private CustomPanel[] thePanels = {new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, true)};

    // Game Layered Pane
    private JLayeredPane gameLayeredPane = new JLayeredPane();
    
    // Chat
    private ChatPanel chatPanel;

    public static ObjectHandler handler = new ObjectHandler();
    private InputHandler input = new InputHandler();
    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage[] biMenuButtons = resLoader.loadSpriteSheet("/res\\MenuButtons.png", 210, 110);
    private BufferedImage[] biNetButtons = resLoader.loadSpriteSheet("/res\\NetButtons.png", 810, 90);
    private BufferedImage[][] biCharacterButtons = resLoader.loadSpriteSheet("/res\\CharacterButtons.png", 300, 300, 4, 7);
    
    // Main Menu Components
    private CustomButton[] mainMenuButtons = {new CustomButton(200, 100, "Host", biMenuButtons, this), new CustomButton(200, 100, "Join", biMenuButtons, this), 
                                              new CustomButton(200, 100, "Help", biMenuButtons, this), new CustomButton(200, 100, "Quit", biMenuButtons, this)};
    private CustomButton[] backButtons = {new CustomButton(200, 100, "Back", biMenuButtons, this), new CustomButton(200, 100, "Back", biMenuButtons, this), 
                                          new CustomButton(200, 100, "Back", biMenuButtons, this), new CustomButton(200, 100, "Back", biMenuButtons, this)};

    // Host & Join Components
    private JTextArea[] netTextAreas = {new JTextArea(), new JTextArea()};
    private JTextField[] netTextFields = {new JTextField(), new JTextField(), new JTextField(), new JTextField()};
    private CustomButton[] netButtons = {new CustomButton(800, 80, "Host Game", biNetButtons, this), new CustomButton(800, 80, "Join Game", biNetButtons, this)};
    private CustomButton netStartButton = new CustomButton(200, 100, "Start", biMenuButtons, this);
    private String[] strNameList = {"", "", "", "",};
    
    // WIP
    private CustomButton[] characterButtons = {new CustomButton(290, 290, null, biCharacterButtons[0], this), new CustomButton(290, 290, null, biCharacterButtons[1], this), 
                                               new CustomButton(290, 290, null, biCharacterButtons[2], this), new CustomButton(290, 290, null, biCharacterButtons[3], this)};
    private CustomButton readyButton = new CustomButton(800, 80, "Ready", biNetButtons, this);

    //All Sprites loaded
    private BufferedImage[] biBulletTextures = resLoader.loadImages("/res\\SniperBullet.png", "/res\\Rocket.png", "/res\\FireBall.png", "/res\\ElectricBall.png", "/res\\Shrapnel.png");
    private BufferedImage[] biVacTexture = resLoader.loadSpriteSheet("/res\\VacGrenade.png", 20, 20);
    private ImageIcon ioLogo = new ImageIcon(resLoader.loadImage("/res\\ioLogo.png"));
    
    // Timer
    private Timer timer = new Timer(1000/60, this);
    public static long startTime = System.nanoTime();

    // Networking
    public static SuperSocketMaster ssm;

    public static State state = State.MAIN_MENU;

    public enum State {
        MAIN_MENU(0), HOST_MENU(1), JOIN_MENU(2), HELP(3), CHARACTER(4), GAME(5);

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
    private int intReady = 0;
    private int[] intCharacterSelections = {-1, -1, -1, -1};
    private boolean[] blnAvailableIds = {true, true, true};
    
    public Main() {
        for(int intCount = 0; intCount < thePanels.length; intCount++) {
            thePanels[intCount].setPreferredSize(new Dimension(1280, 720));
        }

        // Listeners
        thePanels[5].addKeyListener(input);
        thePanels[5].addMouseListener(input);
        thePanels[5].addMouseMotionListener(input);

        gameLayeredPane.setPreferredSize(new Dimension(1280, 720));
        gameLayeredPane.setLayout(null);

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
            netTextAreas[intCount].setBorder(new LineBorder(new Color(205, 237, 253), 4));
            thePanels[intCount + 1].add(netTextAreas[intCount]);
        }

        for(int intCount = 0; intCount < netTextFields.length; intCount++) {
            netTextFields[intCount].setSize(465, 50);
            netTextFields[intCount].setLocation(475, (intCount < 2) ? 330 + 75 * intCount : 330 + 75 * (intCount - 2));
            netTextFields[intCount].setEditable((intCount == 1) ? false : true);
            netTextFields[intCount].setBorder(new LineBorder(new Color(205, 237, 253), 4));
            thePanels[(intCount < 2) ? 1 : 2].add(netTextFields[intCount]);
        }

        for(int intCount = 0; intCount < netButtons.length; intCount++) {
            netButtons[intCount].setLocation(240, 540);
            netButtons[intCount].setEnabled(false);
            thePanels[intCount + 1].add(netButtons[intCount]);
        }

        netStartButton.setLocation(950, 175);
        netStartButton.setEnabled(false);
        thePanels[1].add(netStartButton);

        // Character Panel Components /////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            characterButtons[intCount].setLocation((intCount < 2) ? 345 + 300 * intCount : 345 + 300 * (intCount - 2), (intCount < 2) ? 15 : 315);
            thePanels[4].add(characterButtons[intCount]);
        }

        readyButton.setLocation(240, 620);
        readyButton.setEnabled(false);
        thePanels[4].add(readyButton);

        for(int intCount = 0; intCount < backButtons.length; intCount++) {
            backButtons[intCount].setLocation(20, 20);
            thePanels[intCount + 1].add(backButtons[intCount]);
        }

        theFrame.setContentPane(thePanels[0]);  
        theFrame.addWindowListener(this);
        theFrame.setIconImage(ioLogo.getImage());
        theFrame.pack();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setVisible(true);
        timer.start();
    }

    // Override actionPerformed Method
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == timer) thePanels[state.getValue()].repaint();
        
        if(evt.getSource() == ssm) {
            String strMessage = ssm.readText();
            //System.out.println(strMessage);

            if(intSessionId == 1) {
                if(strMessage.substring(5, 6).equals("o")){
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    GameObject object = handler.getObject(Integer.parseInt(strPayload[2]));
                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                    if(strMessage.contains("SNIPER")) {
                        Sniper snip = (Sniper) object;
                        snip.setLeft(Boolean.parseBoolean(strPayload[3]));
                        System.out.println("2nd " + object.getWorldX() + " " + object.getWorldY());
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oSNIPER~" + strMessage.split("~")[1]);
                        if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oSNIPER~" + strMessage.split("~")[1]);
                        if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oSNIPER~" + strMessage.split("~")[1]);
                    }

                    else if(strMessage.contains("BRUTE")) {
                        Brute brut = (Brute) object;
                        brut.setLeft(Boolean.parseBoolean(strPayload[3]));
                        System.out.println("2nd " + object.getWorldX() + " " + object.getWorldY());
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oBRUTE~" + strMessage.split("~")[1]);
                        if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oBRUTE~" + strMessage.split("~")[1]);
                        if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oBRUTE~" + strMessage.split("~")[1]);
                    }

                    else if(strMessage.contains("KNIGHT")) {
                        Knight knit = (Knight) object;
                        knit.setLeft(Boolean.parseBoolean(strPayload[3]));
                        System.out.println("2nd " + object.getWorldX() + " " + object.getWorldY());
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oKNIGHT~" + strMessage.split("~")[1]);
                        if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oKNIGHT~" + strMessage.split("~")[1]);
                        if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oKNIGHT~" + strMessage.split("~")[1]);
                    }

                    else if(strMessage.contains("WIZARD")) {
                        Wizard wiz = (Wizard) object;
                        wiz.setLeft(Boolean.parseBoolean(strPayload[3]));
                        System.out.println("2nd " + object.getWorldX() + " " + object.getWorldY());
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oWIZARD~" + strMessage.split("~")[1]);
                        if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oWIZARD~" + strMessage.split("~")[1]);
                        if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oWIZARD~" + strMessage.split("~")[1]);
                    }
                    
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");    
                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Integer.parseInt(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),0,0,Float.parseFloat(strPayload[9]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[10])], Boolean.parseBoolean(strPayload[11]), Float.parseFloat(strPayload[12]), -1));
                 
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBULLET~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aHOMING_BULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Float.parseFloat(strPayload[6]),Float.parseFloat(strPayload[7]),Integer.parseInt(strPayload[8]),Float.parseFloat(strPayload[9]),Integer.parseInt(strPayload[10]),0,0, ObjectId.WAVE, handler, ssm));
                    
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aHOMING_BULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aHOMING_BULLET~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aHOMING_BULLET~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    //handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), ObjectId.BULLET, handler, ssm));

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aWAVE~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aWAVE~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aWAVE~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    //handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis(), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, handler, ssm));

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aSLASH~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    //handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis() + 300, Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, handler, ssm));

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBIGSLASH~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aBOOM")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Explosion(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), ObjectId.BOOM, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBOOM~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBOOM~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBOOM~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("aVAC")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new VacGrenade(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]),ObjectId.BOOM, handler, ssm, biVacTexture));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aVAC~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aVAC~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aVAC~" + strMessage.split("~")[1]);
                }else if(strMessage.contains("mJOIN")) {
                    intServerSize++;
                    System.out.println("Player Joined\nServer Size: " + intServerSize);

                    strNameList[intServerSize-1] = strMessage.split("~")[1];
                    netTextAreas[0].append("\n " + strNameList[intServerSize-1]);
                    
                    for(int i = 0; i <= intServerSize; i++){
                        for(int intcount = 0; intcount <= intServerSize; intcount++){
                            ssm.sendText("h>c"+intcount+ ">mNAME_LIST~" + (strNameList[i]) + "," + i);
                        }
                        ssm.sendText("h>c0>mNAME_LIST~" + (strNameList[i]) + "," + i);
                    }
                } else if(strMessage.contains("mCLIENT_JOIN")) {
                    intServerSize++;
                    System.out.println("Player Joined\nServer Size: " + intServerSize);

                    for(int intCount = 0; intCount < blnAvailableIds.length; intCount++) {
                        if(blnAvailableIds[intCount]) {
                            blnAvailableIds[intCount] = false;
                            ssm.sendText("h>c0>mSESSION_ID~" + (intCount + 2));
                            break;
                        }
                    }
                } else if(strMessage.contains("mCLIENT_DISCONNECT")) {
                    intServerSize--;
                    System.out.println("Player Disconnected\nServer Size: " + intServerSize);
                    blnAvailableIds[Integer.parseInt(strMessage.substring(1, 2)) - 2] = true;
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);

                    intCharacterSelections[Integer.parseInt(strMessage.substring(1, 2)) - 1] = Integer.parseInt(strPayload[0]);

                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(intServerSize > 2 && !strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(intServerSize > 3 && !strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                } else if(strMessage.contains("mREADY")) {
                    intReady++;
                }
            } else if(!strMessage.substring(0, 1).equals("c") && (strMessage.split(">")[1].equals("a") || Integer.parseInt(strMessage.substring(3, 4)) == intSessionId)) {
                if(strMessage.substring(4, 5).equals("o") || strMessage.substring(5, 6).equals("o")){
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    GameObject object = handler.getObject(Integer.parseInt(strPayload[2]));
                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                    if(strMessage.contains("KNIGHT")){
                        Knight knit = (Knight) object;
                        knit.setLeft(Boolean.parseBoolean(strPayload[3]));
                    }
                    else if(strMessage.contains("BRUTE")){
                        Brute brut = (Brute) object;
                        brut.setLeft(Boolean.parseBoolean(strPayload[3]));
                    }
                    else if(strMessage.contains("SNIPER")){
                        Sniper snip = (Sniper) object;
                        snip.setLeft(Boolean.parseBoolean(strPayload[3]));
                    }
                    else if(strMessage.contains("WIZARD")){
                        Wizard wiz = (Wizard) object;
                        wiz.setLeft(Boolean.parseBoolean(strPayload[3]));
                    }

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
                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Integer.parseInt(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),0,0,Float.parseFloat(strPayload[9]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[10])], Boolean.parseBoolean(strPayload[11]), Float.parseFloat(strPayload[12]), -1));
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    
                    //handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis(), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, handler, ssm));
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    //handler.addObject(new KnightSlashes(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis() +300, Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), ObjectId.BULLET, handler, ssm));
                } else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    //handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), ObjectId.BULLET, handler, ssm));
                } else if(strMessage.contains("aHOMING_BULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new Explosion(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), ObjectId.BOOM, handler, ssm));
                } else if(strMessage.contains("aVAC")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    handler.addObject(new VacGrenade(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]),ObjectId.BOOM, handler, ssm,biVacTexture));
                }else if(strMessage.contains("mSESSION_ID")) {
                    intSessionId = Integer.parseInt(strMessage.split("~")[1]);
                    System.out.println("Session Id: " + intSessionId);
                } else if(strMessage.contains("mHOST_DISCONNECT")) {
                    ssm = null;

                    netTextFields[2].setText("");
                    netTextFields[3].setText("");
                    netTextAreas[1].setText("");

                    intSessionId = 0;
                    intCurrentButton = -1;
                    intReady = 0;

                    for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                        characterButtons[intCount2].setEnabled(true);
                    }
                    readyButton.setEnabled(false);

                    state = State.JOIN_MENU;

                    theFrame.setContentPane(thePanels[2]);
                    theFrame.pack();
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");

                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);
                } else if(strMessage.contains("mCHARACTER_PANEL")) {
                    theFrame.setContentPane(thePanels[4]);
                    theFrame.pack();
                    state = State.CHARACTER;
                } else if(strMessage.contains("mGAME_PANEL")) {
                    state = State.GAME;

                    chatPanel = new ChatPanel(ssm);

                    handler.addObject(new Barrier(0, 1440, 1920, 30, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(0, -30, 1920, 30, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(-30, 0, 30, 1440, ObjectId.BARRIER, handler, null));
                    handler.addObject(new Barrier(1920, 0, 30, 1440, ObjectId.BARRIER, handler, null));

                    chatPanel.setVisible(true);
                    chatPanel.setOpaque(true);
                    chatPanel.setBounds(880, 0, 400, 720);

                    gameLayeredPane.add(thePanels[5], Integer.valueOf(100));
                    //gameLayeredPane.add(mapPanel, Integer.valueOf(101));
                    gameLayeredPane.add(chatPanel, Integer.valueOf(102));
                    gameLayeredPane.repaint();

                    theFrame.setContentPane(thePanels[5]);
                    thePanels[5].requestFocusInWindow();
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
        }

        for(int intCount = 0; intCount < backButtons.length; intCount++) {
            if(evt.getSource() == backButtons[intCount]) {
                if(intCount < 3) {
                    if(ssm != null) {
                        ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
                        ssm.disconnect();
                        ssm = null;

                        intSessionId = 0;
                        intServerSize = 0;

                        for(int intCount2 = 0; intCount2 < blnAvailableIds.length; intCount2++) {
                            blnAvailableIds[intCount2] = true;
                        }
                    }

                    if(state.getValue() == 1) {
                        netTextFields[0].setText("");
                        netTextFields[1].setText("");
                        netTextAreas[0].setText("");
                    } else if(state.getValue() == 2) {
                        netTextFields[2].setText("");
                        netTextFields[3].setText("");
                        netTextAreas[1].setText("");
                    }

                    state = State.MAIN_MENU;

                    theFrame.setContentPane(thePanels[0]);
                    theFrame.pack();
                } else if(intCount == 3) {
                    ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
                    ssm.disconnect();
                    ssm = null;

                    intSessionId = 0;
                    intServerSize = 0;
                    intCurrentButton = -1;
                    intReady = 0;

                    for(int intCount2 = 0; intCount2 < blnAvailableIds.length; intCount2++) {
                        blnAvailableIds[intCount2] = true;
                    }

                    for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                        characterButtons[intCount2].setEnabled(true);
                    }
                    readyButton.setEnabled(false);

                    for(int intCount2 = (intSessionId == 1) ? 0 : 2; intCount2 < netTextFields.length; intCount2++) {
                        netTextFields[intCount2].setText("");
                    }
        
                    if(intSessionId == 1) netTextAreas[0].setText("");
                    else netTextAreas[1].setText("");

                    state = (intSessionId == 1) ? State.HOST_MENU : State.JOIN_MENU;

                    theFrame.setContentPane((intSessionId == 1) ? thePanels[1] : thePanels[2]);
                    theFrame.pack();
                }
            }
        }

        if(ssm == null && !netTextFields[0].getText().equals("")) netButtons[0].setEnabled(true);
        else netButtons[0].setEnabled(false);

        if(ssm == null && !netTextFields[2].getText().equals("") && !netTextFields[3].getText().equals("")) netButtons[1].setEnabled(true);
        else netButtons[1].setEnabled(false);
        
        if(evt.getSource() == netButtons[0]) {
            ssm = new SuperSocketMaster(8080, this);
            ssm.connect();
            intSessionId = 1;
            intServerSize++;

            char[] chrCharacters = ssm.getMyAddress().toCharArray();

            for(int intCount = 0; intCount < chrCharacters.length; intCount++) {
                chrCharacters[intCount] += 55;
            }

            netTextFields[1].setText(new String(chrCharacters));
            netButtons[0].setEnabled(false);
            netStartButton.setEnabled(true);
            netTextAreas[0].append(" " + netTextFields[0].getText());
            strNameList[0] = netTextFields[0].getText();
        } else if(evt.getSource() == netButtons[1]) {
            if(netTextFields[2].getText().toString().isEmpty() == false){
                netButtons[1].setEnabled(false);

                char[] chrJoinCode = netTextFields[3].getText().toCharArray();
                
                for(int intCount = 0; intCount < chrJoinCode.length; intCount++) {
                    chrJoinCode[intCount] -= 55;
                }

                ssm = new SuperSocketMaster(new String(chrJoinCode), 8080, this);
                ssm.connect();
                ssm.sendText("c0>h>mCLIENT_JOIN~" + netTextFields[2].getText());
                netButtons[1].setEnabled(false);
            }
        }

        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            if(evt.getSource() == characterButtons[intCount]) {
                intPreviousButton = intCurrentButton;
                intCurrentButton = intCount;

                if(intSessionId == 1) ssm.sendText("h>a>mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);
                else ssm.sendText("c" + intSessionId + ">h>mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);

                if(intSessionId == 1) intCharacterSelections[0] = intCurrentButton;
                
                if(intPreviousButton != -1) characterButtons[intPreviousButton].setEnabled(true);
                characterButtons[intCount].setEnabled(false);

                if(intPreviousButton == -1) readyButton.setEnabled(true);
            }
        }

        if(ssm != null) netStartButton.setEnabled(true);
        else netStartButton.setEnabled(false);

        if(evt.getSource() == netStartButton) {
            state = State.CHARACTER;

            ssm.sendText("h>a>mCHARACTER_PANEL");

            theFrame.setContentPane(thePanels[4]);
            theFrame.pack();
        }

        if(evt.getSource() == readyButton) {
            if(intSessionId == 1) intReady++;
            else ssm.sendText("c" + intSessionId + ">h>mREADY");

            readyButton.setEnabled(false);
        }

        if(intSessionId == 1 && state.getValue() == 4 && intReady == intServerSize) {
            state = State.GAME;

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
                handler.addObject(new Barrier(0, (intCount == 0) ? 1440 : -40, 1920, 40, ObjectId.BARRIER, handler, null));
                handler.addObject(new Barrier((intCount == 0) ? -40 : 1920, 0, 40, 1440, ObjectId.BARRIER, handler, null));
            }
            handler.addObject(new ItemObject(200, 200, 20, 20, ObjectId.ITEM, handler, ssm));

            handler.addObject(new Enemy(100,300,0,0,50,59, 10, 1, 1, ObjectId.ENEMY,handler,ssm));

            theFrame.setContentPane(thePanels[5]);
            thePanels[5].requestFocusInWindow();
            theFrame.pack();
        }
    }

    public void windowClosing(WindowEvent evt) {
        if(ssm != null) {
            ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
            ssm.disconnect();
        }
    }

    public void windowClosed(WindowEvent evt) {}

    public void windowActivated(WindowEvent evt) {}

    public void windowDeactivated(WindowEvent evt) {}

    public void windowDeiconified(WindowEvent evt) {}

    public void windowIconified(WindowEvent evt) {}

    public void windowOpened(WindowEvent evt) {} 

    public static void main(String[] args) {
        new Main();
    }
}