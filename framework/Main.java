package Framework;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import Components.*;
import Objects.*;

import java.awt.image.BufferedImage;

public class Main implements ActionListener, WindowListener {

    // Handlers
    public static ObjectHandler handler = new ObjectHandler();
    private InputHandler input = new InputHandler();
    private ResourceLoader resLoader = new ResourceLoader();
    
    // Network
    public static SuperSocketMaster ssm;

    // Button Images
    private BufferedImage[] biMenuButtons = resLoader.loadSpriteSheet("/res\\MenuButtons.png", 210, 110);
    private BufferedImage[] biNetButtons = resLoader.loadSpriteSheet("/res\\NetButtons.png", 810, 90);
    private BufferedImage[][] biArrowButtons = resLoader.loadSpriteSheet("/res\\ArrowButtons.png", 110, 210, 2, 7);
    private BufferedImage[][] biCharacterButtons = resLoader.loadSpriteSheet("/res\\CharacterButtons.png", 300, 300, 4, 7);
    private BufferedImage[] biTileTextures = resLoader.loadSpriteSheet("/res\\TileTextures.png", 40, 40);

    // Frame
    public static JFrame theFrame = new JFrame("Annihilation Station");

    private CustomPanel[] thePanels = {new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, false), 
                                       new CustomPanel(null, false), new CustomPanel(null, false), new CustomPanel(null, true), 
                                       new CustomPanel(null, true)};
    
    // Main Menu Components
    private CustomButton[] mainMenuButtons = {new CustomButton(200, 100, "Host", biMenuButtons, this), new CustomButton(200, 100, "Join", biMenuButtons, this), 
                                              new CustomButton(200, 100, "Help", biMenuButtons, this), new CustomButton(200, 100, "Quit", biMenuButtons, this)};
    private CustomButton[] helpMenuButtons = {new CustomButton(100, 200, biArrowButtons[1], this), new CustomButton(100, 200, biArrowButtons[0], this), 
                                              new CustomButton(200, 100, "Demo", biMenuButtons, this)};
    private CustomButton[] backButtons = {new CustomButton(200, 100, "Back", biMenuButtons, this), new CustomButton(200, 100, "Back", biMenuButtons, this), 
                                          new CustomButton(200, 100, "Back", biMenuButtons, this), new CustomButton(200, 100, "Back", biMenuButtons, this)};
    private CustomButton endScreenButton = new CustomButton(200, 100, "Quit", biMenuButtons, this);

    // Host & Join Components
    private JTextArea[] netTextAreas = {new JTextArea(), new JTextArea()};
    private JTextField[] netTextFields = {new JTextField(), new JTextField(), new JTextField(), new JTextField()};
    private CustomButton[] netButtons = {new CustomButton(800, 80, "Host Game", biNetButtons, this), new CustomButton(800, 80, "Join Game", biNetButtons, this)};
    private CustomButton netStartButton = new CustomButton(200, 100, "Start", biMenuButtons, this);
    
    // Character Selection Buttons
    private CustomButton[] characterButtons = {new CustomButton(290, 290, null, biCharacterButtons[0], this), new CustomButton(290, 290, null, biCharacterButtons[1], this), 
                                               new CustomButton(290, 290, null, biCharacterButtons[2], this), new CustomButton(290, 290, null, biCharacterButtons[3], this)};
    private CustomButton readyButton = new CustomButton(800, 80, "Ready", biNetButtons, this);
    
    // Chat Area
    private JTextArea chatTextArea = new JTextArea();
    private JTextField chatTextField = new JTextField();

    //All Sprites loaded
    private BufferedImage[] biBulletTextures = resLoader.loadImages("/res\\SniperBullet.png", "/res\\Rocket.png", "/res\\FireBall.png", "/res\\ElectricBall.png", "/res\\Shrapnel.png");
    private BufferedImage[] biVacTextures = resLoader.loadSpriteSheet("/res\\VacGrenade.png", 20, 20);
    private ImageIcon ioLogo = new ImageIcon(resLoader.loadImage("/res\\ioLogo.png"));
    
    // Timer
    private Timer timer = new Timer(1000/60, this);
    
    // Program Display States
    public static State state = State.MAIN_MENU;

    public enum State {
        MAIN_MENU(0), HOST_MENU(1), JOIN_MENU(2), HELP(3), CHARACTER(4), GAME(5), DEMO(6);

        private final int intPanelNumber;
        State(int intPanelNumber) {
            this.intPanelNumber = intPanelNumber;
        }
        public int getValue() {
            return intPanelNumber;
        }
    }

    // Networking Variables
    public static String[] strNameList = new String[4];
    public static int[] intAlivePlayers = {0, 0, 0, 0};
    public static int intSessionId;
    public static int intRoomCount;
    public static int intServerSize;
    public static int intHelpScreenCount = 0;
    public static long lngRunStartTime = 0, lngRunEndTime = 0;

    private int intCurrentButton = -1, intPreviousButton = -1;
    private int intReady;
    private int[] intCharacterSelections = {-1, -1, -1, -1};
    private boolean[] blnAvailableIds = {true, true, true};
    
    // Constructor
    public Main() {

        // Set Panel Size to 1280x720
        for(int intCount = 0; intCount < thePanels.length; intCount++) {
            thePanels[intCount].setPreferredSize(new Dimension(1280, 720));
        }

        // Listeners
        thePanels[5].addKeyListener(input);
        thePanels[5].addMouseListener(input);
        thePanels[5].addMouseMotionListener(input);
        
        thePanels[6].addKeyListener(input);
        thePanels[6].addMouseListener(input);
        thePanels[6].addMouseMotionListener(input);
        //adds essential listeners to the menu panels
        // Start Panel Components ///////////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < mainMenuButtons.length; intCount++) {
            mainMenuButtons[intCount].setLocation(540, 200 + 110 * intCount);
            thePanels[0].add(mainMenuButtons[intCount]);
            //adds all of the main menu buttons in one for loop
        }
        // Help Panel Components /////////////////////////////////////////////////////////////////////

        for(int intCount = 0; intCount < helpMenuButtons.length; intCount++) {
            helpMenuButtons[intCount].setLocation((intCount == 2) ? 535 : 30 + 1120 * intCount, (intCount == 2) ? 350 : 255);
            thePanels[3].add(helpMenuButtons[intCount]);
            //adds the help menu buttons
        }

        // Host & Join Panels /////////////////////////////////////////////////////////////////////
        // May combine loops
        // Subject to change as menu is refined
        for(int intCount = 0; intCount < netTextAreas.length; intCount++) {
            netTextAreas[intCount].setSize(600, 250);
            netTextAreas[intCount].setLocation(340, 50);
            netTextAreas[intCount].setEditable(false);
            netTextAreas[intCount].setBorder(new LineBorder(new Color(205, 237, 253), 4));
            thePanels[intCount + 1].add(netTextAreas[intCount]);
            //adds the netTextAreas which are responsible for connecting clients to hosts
        }
        
        for(int intCount = 0; intCount < netTextFields.length; intCount++) {
            netTextFields[intCount].setSize(465, 50);
            netTextFields[intCount].setLocation(475, (intCount < 2) ? 330 + 75 * intCount : 330 + 75 * (intCount - 2));
            netTextFields[intCount].setEditable((intCount == 1) ? false : true);
            netTextFields[intCount].setBorder(new LineBorder(new Color(205, 237, 253), 4));
            thePanels[(intCount < 2) ? 1 : 2].add(netTextFields[intCount]);
            //adds all the textFields which are where the player will input ip and other information
        }

        for(int intCount = 0; intCount < netButtons.length; intCount++) {
            netButtons[intCount].setLocation(240, 540);
            netButtons[intCount].setEnabled(false);
            thePanels[intCount + 1].add(netButtons[intCount]);
            //adds all the essential buttons to the menu
        }

        netStartButton.setLocation(950, 175);
        netStartButton.setEnabled(false);
        thePanels[1].add(netStartButton);
        //the start button for the game

        // Character Panel Components /////////////////////////////////////////////////////////////
        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            characterButtons[intCount].setLocation((intCount < 2) ? 345 + 300 * intCount : 345 + 300 * (intCount - 2), (intCount < 2) ? 15 : 315);
            thePanels[4].add(characterButtons[intCount]);
            //adds all the characterbuttons.
        }

        // Ready Button
        readyButton.setLocation(240, 620);
        readyButton.setEnabled(false);
        thePanels[4].add(readyButton);
        //used for communicating to the host when the player is ready and picked a character

        // Chat Message Text Area
        chatTextArea.setSize(225, 225);
        chatTextArea.setLocation(10, 450);
        chatTextArea.setBackground(new Color(0, 0, 0, 150));
        chatTextArea.setForeground(Color.white);
        chatTextArea.setBorder(new LineBorder(new Color(205, 237, 253), 3));
        chatTextArea.setEditable(false);
        chatTextArea.setVisible(false);
        thePanels[5].add(chatTextArea);
        //used to recieve msgs from one player to another

        // Send Message Text Field
        chatTextField.setSize(225, 30);
        chatTextField.setLocation(10, 680);
        chatTextField.setBackground(new Color(0, 0, 0, 150));
        chatTextField.setForeground(Color.white);
        chatTextField.setBorder(new LineBorder(new Color(205, 237, 253), 3));
        chatTextField.addActionListener(this);
        chatTextField.setVisible(false);
        thePanels[5].add(chatTextField);
        //sends the msgs

        // Back Buttons
        for(int intCount = 0; intCount < backButtons.length; intCount++) {
            backButtons[intCount].setLocation(20, 20);
            thePanels[intCount + 1].add(backButtons[intCount]);
            //used to go backwards to a different screen
        }

        // Frame Details
        theFrame.setContentPane(thePanels[0]);  
        theFrame.addWindowListener(this);
        theFrame.setIconImage(ioLogo.getImage());
        theFrame.pack();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setVisible(true);
        timer.start();
        //adds all essentials to the frame and starts the timer
    }

    // Override actionPerformed Method
    public void actionPerformed(ActionEvent evt) {

        // 60 fps Gameplay
        if(evt.getSource() == timer) thePanels[state.getValue()].repaint();
        
        // Networking
        if(evt.getSource() == ssm) {

            // Read Network Message
            String strMessage = ssm.readText();

            // Host
            if(intSessionId == 1) {
                if(strMessage.substring(5, 6).equals("o")){
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    GameObject object = handler.getObject(Integer.parseInt(strPayload[strPayload.length - 1]));
                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                    //sets the coordinates of the player using the message recieved
                    if(strMessage.contains("SNIPER")) {
                        Sniper sniper = (Sniper) object;
                        sniper.setLeft(Boolean.parseBoolean(strPayload[2]));
                        //sets up sprite
                        sniper.setHP(Float.parseFloat(strPayload[3]));
                        sniper.setMaxHP(Float.parseFloat(strPayload[4]));
                        //sets up the player health

                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oSNIPER~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oSNIPER~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oSNIPER~" + strMessage.split("~")[1]);
                        //sends to all the other clients
                    } else if(strMessage.contains("BRUTE")) {
                        Brute brute = (Brute) object;
                        brute.setLeft(Boolean.parseBoolean(strPayload[2]));
                        //sets up sprite
                        brute.setHP(Float.parseFloat(strPayload[3]));
                        brute.setMaxHP(Float.parseFloat(strPayload[4]));
                        //sets up the player health
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oBRUTE~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oBRUTE~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oBRUTE~" + strMessage.split("~")[1]);
                        //sends to all the other clients
                    } else if(strMessage.contains("KNIGHT")) {
                        Knight knight = (Knight) object;
                        knight.setLeft(Boolean.parseBoolean(strPayload[2]));
                        //responsible for flipping the sprite left and right
                        knight.setHP(Float.parseFloat(strPayload[3]));
                        knight.setMaxHP(Float.parseFloat(strPayload[4]));
                        //changes the health values
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oKNIGHT~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oKNIGHT~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oKNIGHT~" + strMessage.split("~")[1]);
                        //sends the values to all other connected clients
                    } else if(strMessage.contains("WIZARD")) {
                        Wizard wizard = (Wizard) object;
                        wizard.setLeft(Boolean.parseBoolean(strPayload[2]));
                        //turns the sprite left and right
                        wizard.setHP(Float.parseFloat(strPayload[3]));
                        wizard.setMaxHP(Float.parseFloat(strPayload[4]));
                        //changes player health
                        if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>oWIZARD~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>oWIZARD~" + strMessage.split("~")[1]);
                        if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>oWIZARD~" + strMessage.split("~")[1]);
                        //ensures all clients recieve the information as well
                    }
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");    
                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Integer.parseInt(strPayload[6]), Integer.parseInt(strPayload[7]), Float.parseFloat(strPayload[8]), 0, 0, Float.parseFloat(strPayload[9]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[10])], Boolean.parseBoolean(strPayload[11]), Float.parseFloat(strPayload[12]), -1));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBULLET~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBULLET~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBULLET~" + strMessage.split("~")[1]);
                    //create a bullet with the msg recieved and then send the same msg to all clients
                } else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Float.parseFloat(strPayload[7]), Integer.parseInt(strPayload[8]), Float.parseFloat(strPayload[9]), Integer.parseInt(strPayload[10]), 0, 0, ObjectId.WAVE, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aWAVE~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aWAVE~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aWAVE~" + strMessage.split("~")[1]);
                    //create a wave attack with the msg recieved and then send the same msg to all clients
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new SlashAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis(), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Integer.parseInt(strPayload[7]), Float.parseFloat(strPayload[8]), Integer.parseInt(strPayload[9]), 0, 0, ObjectId.SLASH, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aSLASH~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aSLASH~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aSLASH~" + strMessage.split("~")[1]);
                    //create a slash attack with the msg recieved and then send the same msg to all clients
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new SlashAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis() + 300, Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Integer.parseInt(strPayload[7]), Float.parseFloat(strPayload[8]), Integer.parseInt(strPayload[9]), 0, 0, ObjectId.BULLET, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBIGSLASH~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBIGSLASH~" + strMessage.split("~")[1]);
                    //create a wave attack with the msg recieved and then send the same msg to all clients, the difference is that this slash lasts longer because of the System.currentTimeMillis() + 300
                } else if(strMessage.contains("aBOOM")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new Explosion(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), ObjectId.BOOM, handler, ssm));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aBOOM~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aBOOM~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aBOOM~" + strMessage.split("~")[1]);
                    //creates an explosion and sends the msg to all clients
                } else if(strMessage.contains("aVAC")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new VacGrenade(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]),ObjectId.BOOM, handler, ssm, biVacTextures));
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>aVAC~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>aVAC~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>aVAC~" + strMessage.split("~")[1]);
                    //creates a vacuum grenade and sends the msg to all clients
                } else if(strMessage.contains("mCLIENT_JOIN")) {
                    if(intServerSize < 4){
                        intServerSize++;
                        //if the server size is 4, dont allow any more in, 
                        System.out.println("Player Joined\nServer Size: " + intServerSize);
                        netStartButton.setEnabled(true);
                        for(int intCount = 0; intCount < blnAvailableIds.length; intCount++) {
                            if(blnAvailableIds[intCount]) {
                                blnAvailableIds[intCount] = false;
                                ssm.sendText("h>c0>mSESSION_ID~" + (intCount + 2) + "," + intServerSize);
                                //sends the session ID of a client to the client 
                                strNameList[intCount + 1] = strMessage.split("~")[1];
                                netTextAreas[0].append("\n " + strNameList[intCount + 1] + " joined");
                                //writes the name and adds "Joined" to it, makes sure all named are added to all clients
                                break;
                            }
                        }
                        ssm.sendText("h>a>mPLAYER_NAMES~" + strNameList[0] + "," + strNameList[1] + "," + strNameList[2] + "," + strNameList[3]);
                        //sends all the names
                    }else{
                        ssm.sendText("h>c0>mDISCONNECT");
                        //if more than 4 players, tell the 5th player to disconnect
                    }
                    
                } else if(strMessage.contains("mCLIENT_DISCONNECT")) {
                    intServerSize--;
                    System.out.println("Player Disconnected\nServer Size: " + intServerSize);
                    blnAvailableIds[Integer.parseInt(strMessage.substring(1, 2)) - 2] = true;
                    //if a client disconnected, decrease the server size and find out which session id is available
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);
                    intCharacterSelections[Integer.parseInt(strMessage.substring(1, 2)) - 1] = Integer.parseInt(strPayload[0]);
                    if(!strMessage.substring(1, 2).equals("2")) ssm.sendText("h>c2>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("3")) ssm.sendText("h>c3>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    if(!strMessage.substring(1, 2).equals("4")) ssm.sendText("h>c4>mCHARACTER_SELECTED~" + strMessage.split("~")[1]);
                    //ensures that no other players pick the same character
                } else if(strMessage.contains("mREADY")) {
                    intReady++;
                    //used to see if all players are ready
                } else if(strMessage.contains("cCHAT")){
                    String strPayload = strMessage.split("~")[1];
                    
                    chatTextArea.append(strPayload + "\n");
                    //recieves messages
                }
            } else if(!strMessage.substring(0, 1).equals("c") && (strMessage.split(">")[1].equals("a") || Integer.parseInt(strMessage.substring(3, 4)) == intSessionId)) {
                //client side networking
                if(strMessage.substring(4, 5).equals("o") || strMessage.substring(5, 6).equals("o")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    GameObject object = handler.getObject(Integer.parseInt(strPayload[strPayload.length - 1]));
                    object.setWorldX(Float.parseFloat(strPayload[0]));
                    object.setWorldY(Float.parseFloat(strPayload[1]));
                    //similar to the hosts where it sets the x and y, recieves health, and ensures the sprite of the player is facing the right direction
                    if(strMessage.contains("KNIGHT")){
                        Knight knight = (Knight)object;
                        knight.setLeft(Boolean.parseBoolean(strPayload[2]));
                        knight.setHP(Float.parseFloat(strPayload[3]));
                        knight.setMaxHP(Float.parseFloat(strPayload[4]));
                    } else if(strMessage.contains("BRUTE")){
                        Brute brute = (Brute)object;
                        brute.setLeft(Boolean.parseBoolean(strPayload[2]));
                        brute.setHP(Float.parseFloat(strPayload[3]));
                        brute.setMaxHP(Float.parseFloat(strPayload[4]));
                    } else if(strMessage.contains("SNIPER")){
                        Sniper sniper = (Sniper)object;
                        sniper.setLeft(Boolean.parseBoolean(strPayload[2]));
                        sniper.setHP(Float.parseFloat(strPayload[3]));
                        sniper.setMaxHP(Float.parseFloat(strPayload[4]));
                    } else if(strMessage.contains("WIZARD")){
                        Wizard wizard = (Wizard)object;
                        wizard.setLeft(Boolean.parseBoolean(strPayload[2]));
                        wizard.setHP(Float.parseFloat(strPayload[3]));
                        wizard.setMaxHP(Float.parseFloat(strPayload[4]));
                    } else if(strMessage.contains("ENEMY")) {
                        Enemy enemy = (Enemy)object;
                        enemy.setHP(Float.parseFloat(strPayload[2]));
                        enemy.setLeft(Boolean.parseBoolean(strPayload[3]));
                    }
                    //does not send any msgs back
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
                } 
                //these lines of code are responsible for adding the players in at the correct spots
                else if(strMessage.contains("aENEMY_BULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new EnemyBullet(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), ObjectId.ENEMY_BULLET, handler, ssm, null, Boolean.parseBoolean(strPayload[7]), Float.parseFloat(strPayload[8])));
                } else if(strMessage.contains("aENEMY")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new Enemy(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), 0, 0, 0, 0, Integer.parseInt(strPayload[2]), Integer.parseInt(strPayload[3]), Integer.parseInt(strPayload[4]), ObjectId.ENEMY, handler, ssm), Integer.parseInt(strPayload[4]));
                } else if(strMessage.contains("aBULLET")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new Bullet(Float.parseFloat(strPayload[0]),Float.parseFloat(strPayload[1]),Float.parseFloat(strPayload[2]),Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]),Float.parseFloat(strPayload[5]),Integer.parseInt(strPayload[6]),Integer.parseInt(strPayload[7]),Float.parseFloat(strPayload[8]),0,0,Float.parseFloat(strPayload[9]), ObjectId.BULLET, handler, ssm, biBulletTextures[Integer.parseInt(strPayload[10])], Boolean.parseBoolean(strPayload[11]), Float.parseFloat(strPayload[12]), -1));  
                } else if(strMessage.contains("aSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new SlashAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis(), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Integer.parseInt(strPayload[7]), Float.parseFloat(strPayload[8]), Integer.parseInt(strPayload[9]), 0, 0, ObjectId.SLASH, handler, ssm));
                } else if(strMessage.contains("aBIGSLASH")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new SlashAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), System.currentTimeMillis() + 300, Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Integer.parseInt(strPayload[7]), Float.parseFloat(strPayload[8]), Integer.parseInt(strPayload[9]), 0, 0, ObjectId.BULLET, handler, ssm));
                } else if(strMessage.contains("aWAVE")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new WaveAttacks(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]), Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]), Float.parseFloat(strPayload[6]), Float.parseFloat(strPayload[7]), Integer.parseInt(strPayload[8]), Float.parseFloat(strPayload[9]), Integer.parseInt(strPayload[10]), 0, 0, ObjectId.WAVE, handler, ssm));
                } else if(strMessage.contains("aVAC")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    handler.addObject(new VacGrenade(Float.parseFloat(strPayload[0]), Float.parseFloat(strPayload[1]), Float.parseFloat(strPayload[2]), Float.parseFloat(strPayload[3]),Float.parseFloat(strPayload[4]), Float.parseFloat(strPayload[5]),ObjectId.BOOM, handler, ssm, biVacTextures));
                }
                //resonsible for creating objects with the messsages sent. all essential parameters are sent and clients create the objects with them
                else if(strMessage.contains("mNEXT_ROOM")) {
                    handler.clearEntities();

                    GameObject object = handler.getObject(Main.intSessionId - 1);

                    if(object instanceof Sniper) {
                        Sniper sniper = (Sniper)object;
                        sniper.setWorldX(150 + 40 * (Main.intSessionId - 1));
                        sniper.setWorldY(1400);
                        if(sniper.getHP() <= 0) sniper.setHP(sniper.getMaxHP()/2);
                    } else if(object instanceof Brute) {
                        Brute brute = (Brute)object;
                        brute.setWorldX(150 + 40 * (Main.intSessionId - 1));
                        brute.setWorldY(1400);
                        if(brute.getHP() <= 0) brute.setHP(brute.getMaxHP()/2);
                    } else if(object instanceof Knight) {
                        Knight knight = (Knight)object;
                        knight.setWorldX(150 + 40 * (Main.intSessionId - 1));
                        knight.setWorldY(1400);
                        if(knight.getHP() <= 0) knight.setHP(knight.getMaxHP()/2);
                    } else if(object instanceof Wizard) {
                        Wizard wizard = (Wizard)object;
                        wizard.setWorldX(150 + 40 * (Main.intSessionId - 1));
                        wizard.setWorldY(1400);
                        if(wizard.getHP() <= 0) wizard.setHP(wizard.getMaxHP()/2);
                    }

                    intRoomCount++;
                    //goes to the next room if the message is recieved
                } else if(strMessage.contains("mPLAYER_NAMES")) { 
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    netTextAreas[1].setText("");
                    for(int intCount = 0; intCount < strNameList.length; intCount++) {
                        strNameList[intCount] = strPayload[intCount];
                        if(!strNameList[intCount].equals("null")) netTextAreas[1].append(" " + strNameList[intCount] + " joined\n");
                    }
                    //recieves the player names from the host and then prints the names in the textArea
                } else if(strMessage.contains("mSESSION_ID")) {
                    intSessionId = Integer.parseInt(strMessage.split("~")[1].split(",")[0]);
                    intServerSize = Integer.parseInt(strMessage.split("~")[1].split(",")[1]);
                    //client recieves its own personalized session id!
                    System.out.println("Session Id: " + intSessionId);
                } else if(strMessage.contains("mDISCONNECT")) {
                    ssm.disconnect();
                    //host tells client to disconnet, client disconnects
                }else if(strMessage.contains("mHOST_DISCONNECT")) {
                    //incase the host disconnects
                    ssm = null;
                    //sets ssm to null
                    netTextFields[2].setText("");
                    netTextFields[3].setText("");
                    netTextAreas[1].setText("");
                    //removes everything
                    intSessionId = 0;
                    intCurrentButton = -1;
                    intReady = 0;
                    //resets every important variable
                    for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                        characterButtons[intCount2].setEnabled(true);
                        //resets buttons
                    }
                    readyButton.setEnabled(false);
                    state = State.JOIN_MENU;
                    theFrame.setContentPane(thePanels[2]);
                    theFrame.pack();
                    //restarts the experience
                } else if(strMessage.contains("mCHARACTER_SELECTED")) {
                    String[] strPayload = strMessage.split("~")[1].split(",");
                    characterButtons[Integer.parseInt(strPayload[0])].setEnabled(false);
                    if(Integer.parseInt(strPayload[1]) != -1) characterButtons[Integer.parseInt(strPayload[1])].setEnabled(true);
                    //disables a button if another player has already pressed it, lifts the button up that the other player pressed before
                } else if(strMessage.contains("mCHARACTER_PANEL")) {
                    theFrame.setContentPane(thePanels[4]);
                    theFrame.pack();
                    state = State.CHARACTER;
                    //host tells client to switch to a different panel
                } else if(strMessage.contains("mGAME_PANEL")) {
                    state = State.GAME;

                    lngRunStartTime = System.currentTimeMillis();

                    for(int intCount = 0; intCount < 2; intCount++) {
                        handler.addObject(new Barrier(0, (intCount == 0) ? 1440 : -40, 1920, 40, (intCount == 0) ? biTileTextures[4] : biTileTextures[6], ObjectId.PERM_BARRIER, handler, null));
                        handler.addObject(new Barrier((intCount == 0) ? -40 : 1920, 0, 40, 1440, (intCount == 0) ? biTileTextures[5] : biTileTextures[7], ObjectId.PERM_BARRIER, handler, null));
                    }
                    //host tells client to switch to a different panel, adds barriers from the game to ensure players don't fall
                    theFrame.setContentPane(thePanels[5]);
                    thePanels[5].requestFocusInWindow();
                    theFrame.pack();
                } else if(strMessage.contains("cCHAT")) {
                    String strPayload = strMessage.split("~")[1];
                    System.out.println(strPayload);

                    chatTextArea.append(strPayload + "\n");
                    //recives messages and adds them to the chat
                }
            }
        }

        // Main Menu Button Actions
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
        //switches between menus based on which button was pressed

        for(int intCount = 0; intCount < backButtons.length; intCount++) {
            if(evt.getSource() == backButtons[intCount]) {
                if(intCount < 3) {
                    if(ssm != null) {
                        handler.clearList();
                        ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
                        ssm.disconnect();
                        ssm = null;
                        intSessionId = 0;
                        intServerSize = 0;
                        //used to disconnect the host
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
                    //resets everything on the hosts end
                    intHelpScreenCount = 0;

                    state = State.MAIN_MENU;

                    theFrame.setContentPane(thePanels[0]);
                    theFrame.pack();
                } else if(intCount == 3) {
                    handler.clearList();
                    if(intSessionId == 1 && ssm != null) state = State.HOST_MENU;
                    else if(ssm != null) state = State.JOIN_MENU;
                    else state = State.HELP;

                    theFrame.setContentPane((ssm != null) ? (intSessionId == 1) ? thePanels[1] : thePanels[2] : thePanels[3]);
                    theFrame.pack();
                    
                    if(ssm != null) {
                        ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
                        ssm.disconnect();
                        ssm = null;
                    }

                    for(int intCount2 = 0; intCount2 < blnAvailableIds.length; intCount2++) {
                        blnAvailableIds[intCount2] = true;
                    }

                    for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                        characterButtons[intCount2].setEnabled(true);
                    }
                    //clears up all session ids and ensures the buttons should be working
                    readyButton.setEnabled(false);
                    for(int intCount2 = (intSessionId == 1) ? 0 : 2; intCount2 < netTextFields.length; intCount2++) {
                        netTextFields[intCount2].setText("");
                    }

                    if(intSessionId == 1) netTextAreas[0].setText("");
                    else netTextAreas[1].setText("");

                    intSessionId = 0;
                    intServerSize = 0;
                    intCurrentButton = -1;
                    intReady = 0;
                    // the host is ready to host another game 
                }
            }
        }

        if(ssm == null && !netTextFields[0].getText().equals("")) netButtons[0].setEnabled(true);
        else netButtons[0].setEnabled(false);
        if(ssm == null && !netTextFields[2].getText().equals("") && !netTextFields[3].getText().equals("")) netButtons[1].setEnabled(true);
        else netButtons[1].setEnabled(false);
        //ensures that if there is no connection, players cant join or host
        if(evt.getSource() == netButtons[0]) {
            ssm = new SuperSocketMaster(8080, this);
            ssm.connect();

            intSessionId = 1;
            intServerSize++;

            char[] chrCharacters = ssm.getMyAddress().toCharArray();

            for(int intCount = 0; intCount < chrCharacters.length; intCount++) {
                chrCharacters[intCount] += 55;
            }
            //creates the join code through char to int conversion
            netTextAreas[0].append(" " + netTextFields[0].getText() + " joined");
            strNameList[0] = netTextFields[0].getText();
            netTextFields[1].setText(new String(chrCharacters));
            netButtons[0].setEnabled(false);
            netStartButton.setEnabled(false);
            //adds a name to the name array while also preparing the game
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
                //sends a message when a client has joined a server
            }
        }

        for(int intCount = 0; intCount < characterButtons.length; intCount++) {
            if(evt.getSource() == characterButtons[intCount]) {
                intPreviousButton = intCurrentButton;
                intCurrentButton = intCount;

                if(intSessionId == 1 && ssm != null) ssm.sendText("h>a>mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);
                else if(ssm != null) ssm.sendText("c" + intSessionId + ">h>mCHARACTER_SELECTED~" + intCurrentButton + "," + intPreviousButton);

                if(intSessionId == 1) intCharacterSelections[0] = intCurrentButton;
                
                if(intPreviousButton != -1) characterButtons[intPreviousButton].setEnabled(true);
                characterButtons[intCount].setEnabled(false);
                if(intPreviousButton == -1) readyButton.setEnabled(true);
                //used to ensure when someone picks a character, that button is disabled and the past button would be enables
            }
        }

        if(evt.getSource() == netStartButton) {
            state = State.CHARACTER;
            ssm.sendText("h>a>mCHARACTER_PANEL");
            theFrame.setContentPane(thePanels[4]);
            theFrame.pack();
            //switches panels to the character selection panel
        }
        
        if(evt.getSource() == readyButton) {
            if(intSessionId == 1 && ssm != null) intReady++;
            else if(ssm != null) ssm.sendText("c" + intSessionId + ">h>mREADY");

            if(ssm == null) {
                if(intCharacterSelections[0] == 0) {
                    handler.addObject(new Sniper(150, 800, 32, 64, ObjectId.PLAYER, handler, null, input, 0), 0);
                } else if(intCharacterSelections[0] == 1) {
                    handler.addObject(new Brute(150, 800, 32, 64, ObjectId.PLAYER, handler, null, input, 0), 0);
                } else if(intCharacterSelections[0] == 2) {
                    handler.addObject(new Knight(150, 800, 32, 64, ObjectId.PLAYER, handler, null, input, 0), 0);
                } else if(intCharacterSelections[0] == 3) {
                    handler.addObject(new Wizard(150, 800, 32, 64, ObjectId.PLAYER, handler, null, input, 0), 0);
                }
                //adds in all of the players and barriers for the game to begin

                for(int intCount = 0; intCount < 2; intCount++) {
                    handler.addObject(new Barrier(0, (intCount == 0) ? 800 : -40, 1200, 40, (intCount == 0) ? biTileTextures[4] : biTileTextures[6], ObjectId.PERM_BARRIER, handler, null));
                    handler.addObject(new Barrier((intCount == 0) ? -40 : 1200, 0, 40, 800, (intCount == 0) ? biTileTextures[5] : biTileTextures[7], ObjectId.PERM_BARRIER, handler, null));
                }

                state = State.DEMO;
                //makes the panel switch to the demo panel
                theFrame.setContentPane(thePanels[6]);
                thePanels[6].requestFocusInWindow();
                theFrame.pack();
            }

            readyButton.setEnabled(false);
        }

        if(ssm != null && intSessionId == 1 && state.getValue() == 4 && intReady == intServerSize) {
            state = State.GAME;

            for(int intCount = 0; intCount < intServerSize; intCount++) {
                if(intCharacterSelections[intCount] == 0) {
                    handler.addObject(new Sniper(150 + 40 * intCount, 1400, 32, 64, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aSNIPER~" + (150 + 40 * intCount) + "," + 1400 + "," + 32 + "," + 64 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 1) {
                    handler.addObject(new Brute(150 + 40 * intCount, 1400, 32, 64, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aBRUTE~" + (150 + 40 * intCount) + "," + 1400 + "," + 32 + "," + 64 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 2) {
                    handler.addObject(new Knight(150 + 40 * intCount, 1400, 32, 64, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aKNIGHT~" + (150 + 40 * intCount) + "," + 1400 + "," + 32 + "," + 64 + "," + intCount);
                } else if(intCharacterSelections[intCount] == 3) {
                    handler.addObject(new Wizard(150 + 40 * intCount, 1400, 32, 64, ObjectId.PLAYER, handler, ssm, input, intCount), intCount);
                    ssm.sendText("h>a>aWIZARD~" + (150 + 40 * intCount) + "," + 1400 + "," + 32 + "," + 64 + "," + intCount);
                }
            }
            //if the state is game, add all the characters and ensure all clients add as well by senidng a message
            ssm.sendText("h>a>mGAME_PANEL");

            for(int intCount = 0; intCount < 2; intCount++) {
                handler.addObject(new Barrier(0, (intCount == 0) ? 1440 : -40, 1920, 40, (intCount == 0) ? biTileTextures[4] : biTileTextures[6], ObjectId.PERM_BARRIER, handler, null));
                handler.addObject(new Barrier((intCount == 0) ? -40 : 1920, 0, 40, 1440, (intCount == 0) ? biTileTextures[5] : biTileTextures[7], ObjectId.PERM_BARRIER, handler, null));
                //adds in all of the barriers to ensure players don't cross vertically or horizontally
            }

            lngRunStartTime = System.currentTimeMillis();

            theFrame.setContentPane(thePanels[5]);
            thePanels[5].requestFocusInWindow();
            theFrame.pack();
        }

        if(state == State.GAME && intRoomCount == 8 || intAlivePlayers[0] + intAlivePlayers[1] + intAlivePlayers[2] + intAlivePlayers[3] == intServerSize) endScreenButton.setVisible(true);

        if(evt.getSource() == endScreenButton) {
            handler.clearList();

            ssm.sendText((intSessionId == 1) ? "h>a>mHOST_DISCONNECT" : "c" + intSessionId + ">h>mCLIENT_DISCONNECT");
            ssm.disconnect();
            ssm = null;

            intSessionId = 0;
            intServerSize = 0;
            intRoomCount = 0;
            intReady = 0;
            intCurrentButton = -1;

            lngRunStartTime = 0;
            lngRunEndTime = 0;

            for(int intCount2 = 0; intCount2 < blnAvailableIds.length; intCount2++) {
                blnAvailableIds[intCount2] = true;
            }

            for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                characterButtons[intCount2].setEnabled(true);
            }

            for(int intCount = 0; intCount < strNameList.length; intCount++) {
                strNameList[intCount] = null;
            }

            for(int intCount = 0; intCount < intAlivePlayers.length; intCount++) {
                intAlivePlayers[intCount] = 0;
            }

            for(int intCount = 0; intCount < netTextAreas.length; intCount++) {
                netTextAreas[intCount].setText("");
            }

            for(int intCount = 0; intCount < netTextFields.length; intCount++) {
                netTextFields[intCount].setText("");
            }

            state = State.MAIN_MENU;

            theFrame.setContentPane(thePanels[0]);
            theFrame.pack();
        }

        chatTextArea.setVisible((input.buttonSet.contains(InputHandler.InputButtons.ENTER)) ? true : false);
        chatTextField.setVisible((input.buttonSet.contains(InputHandler.InputButtons.ENTER)) ? true : false);
        //turns the chat area on and off depending on if the enter key was pressed
        if(evt.getSource() == chatTextField) {
            input.buttonSet.remove(InputHandler.InputButtons.ENTER);
            chatTextArea.append(strNameList[intSessionId - 1] + ": " + chatTextField.getText() + "\n");

            if(intSessionId == 1) ssm.sendText("h>a>cCHAT~" + strNameList[intSessionId - 1] + ": " + chatTextField.getText());
            else ssm.sendText("c" + intSessionId + ">h>cCHAT~" + strNameList[intSessionId - 1] + ": " + chatTextField.getText());

            chatTextField.setText("");

            thePanels[5].requestFocusInWindow();
            //sends a message with the contents of the chat
        }

        if(intHelpScreenCount == 0) helpMenuButtons[0].setEnabled(false);
        else helpMenuButtons[0].setEnabled(true);
        //used for traversing across the helpscreen 

        if(intHelpScreenCount == 7) {
            helpMenuButtons[1].setEnabled(false);
            helpMenuButtons[2].setVisible(true);
        } else {
            helpMenuButtons[1].setEnabled(true);
            helpMenuButtons[2].setVisible(false);
        }
        //makes certain help menu buttons on or off

        if(intRoomCount > 1 && state == State.DEMO) {
            handler.clearList();

            for(int intCount2 = 0; intCount2 < characterButtons.length; intCount2++) {
                characterButtons[intCount2].setEnabled(true);
            }
            readyButton.setEnabled(false);

            intSessionId = 0;
            intServerSize = 0;
            intRoomCount = 0;
            intCurrentButton = -1;

            state = State.HELP;

            theFrame.setContentPane(thePanels[3]);
            theFrame.pack();
            //brings the player back to the help screen after the demo
        }

        if(evt.getSource() == helpMenuButtons[0]) {
            intHelpScreenCount--;
        } else if(evt.getSource() == helpMenuButtons[1]) {
            intHelpScreenCount++;
        } else if(evt.getSource() == helpMenuButtons[2]) {
            state = State.CHARACTER;

            intSessionId = 1;
            intServerSize = 1;

            theFrame.setContentPane(thePanels[4]);
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

    // Main Method
    public static void main(String[] args) {
        new Main();
    }
}