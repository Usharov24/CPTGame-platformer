import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Main implements ActionListener {

    public static JFrame theFrame = new JFrame("CPT Game Proto");

    //Panels
    public static DrawPanel mainPanel = new DrawPanel(null);
    private JPanel hudPanel = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel startPanel = new JPanel(null);
    private JPanel[] netPanels = {new JPanel(null), new JPanel(null)};
    private JPanel settingsPanel = new JPanel(null);
    private ChatPanel chatPanel = new ChatPanel();
    public static JPanel characterPanel = new JPanel(null);
    public static int intjoinid;
    public static int inthostid;
    public static int intjoinhostid;

    // Order of buttons:
    // {host, join, settings, quit}
    private CustomButton[] mainMenuButtons = {new CustomButton(100, 100, this), new CustomButton(100, 100, this), 
                                              new CustomButton(100, 100, this), new CustomButton(100, 100, this)};
    
    private JLabel mainMenuLabel = new JLabel("Game!");

    //Host & Join
    private JTextField[] port = {new JTextField(), new JTextField()};
    private JTextField[] ip = {new JTextField(), new JTextField()};
    private JTextField[] name = {new JTextField(), new JTextField()};
    private JTextArea[] players = {new JTextArea(), new JTextArea()};
    private JButton host = new JButton("Host Network");
    private JButton join = new JButton("Join Network");
    private JLabel[] hostLabels = {new JLabel("Enter Name"), new JLabel("Port Number"), new JLabel("IP Address"), new JLabel("")};
    private JLabel[] joinLabels = {new JLabel("Enter Name"), new JLabel("Port Number"), new JLabel("IP Address"), new JLabel("")};
    public static SuperSocketMaster ssm = null;
    public static JButton[] buttonchar = {new JButton("Sniper"), new JButton("Brute"), new JButton("Knight"), new JButton("Wizard")};
    private JButton buttonstart = new JButton("Start game");
    private JButton buttonready = new JButton("Ready");

    //HUD
    private JLabel[] playerNames = {new JLabel("Player 1"), new JLabel("Player 2"), new JLabel("Player 3"), new JLabel("Player 4")};

    // TEMPORARY ////////////////////////////////////////////////////////////////
    public static ObjectHandler handler = new ObjectHandler();
    public Player player = new Player(0, 0, 32, 32, ObjectId.PLAYER, handler);
    public Player player2 = new Player(-100, 0, 32, 32, ObjectId.PLAYER, handler);
    public Player player3 = new Player(-100, 0, 32, 32, ObjectId.PLAYER, handler);
    public Player player4 = new Player(-1000, 0, 32, 32, ObjectId.PLAYER, handler);
    public static int[] intcharbutton = new int[]{0,0,0,0};
    public static int[] intpastcharbutton = new int[]{0,0,0,0};
    public Network network = new Network();

    /////////////////////////////////////////////////////////////////////////////

    // Create Timer
    private Timer timer = new Timer(1000/60, this);
    
    public Main() {
        // TEMP ///////
        handler.addObject(player);
        mainPanel.addKeyListener(player);
        mainPanel.addMouseListener(player);
        ///////////////

        // Main Panel
        mainPanel.setPreferredSize(new Dimension(1280, 720));
        mainPanel.setFocusable(true);
        mainPanel.requestFocus();

        // Settings Panel
        settingsPanel.setPreferredSize(new Dimension(1280, 720));
        settingsPanel.setFocusable(true);
        settingsPanel.requestFocus();

        //start panel settings
        startPanel.setPreferredSize(new Dimension(1280, 720));

        //Buttons for Main Menu
        for(int intCount = 0; intCount < mainMenuButtons.length; intCount++) {
            mainMenuButtons[intCount].setLocation(600, 200 + 100 * intCount);
            startPanel.add(mainMenuButtons[intCount]);
        }

        //title for main menu
        mainMenuLabel.setSize(100, 100);
        mainMenuLabel.setLocation(600, 20);
        startPanel.add(mainMenuLabel);

        //Character selection panel
        for(int i = 0; i < 4; i++){
            characterPanel.add(buttonchar[i]);
        }
        
        characterPanel.add(buttonready);
        characterPanel.setPreferredSize(new Dimension(1280,720));
        buttonready.setSize(100,100);
        buttonready.setLocation(800,200);
        buttonready.addActionListener(this);
        buttonstart.setSize(100,100);
        buttonstart.setLocation(900,100);
        buttonstart.addActionListener(this);
        buttonstart.setEnabled(false);
        buttonchar[0].setSize(100,100);
        buttonchar[0].setLocation(100,100);
        buttonchar[0].addActionListener(this);
        buttonchar[1].setSize(100,100);
        buttonchar[1].setLocation(200,100);
        buttonchar[1].addActionListener(this);
        buttonchar[2].setSize(100,100);
        buttonchar[2].setLocation(100,200);
        buttonchar[2].addActionListener(this);
        buttonchar[3].setSize(100,100);
        buttonchar[3].setLocation(200,200);
        buttonchar[3].addActionListener(this);

        // Host & Join Network
        host.addActionListener(this);
        host.setSize(500, 70);
        host.setLocation(390, 540);

        join.addActionListener(this);
        join.setSize(500,70);
        join.setLocation(390, 540);

        // Labels
        for(int intCount = 0; intCount < 3; intCount++){
            hostLabels[intCount].setSize(100, 50);
            joinLabels[intCount].setSize(100, 50);
            hostLabels[intCount].setLocation(300, 330 + 70*intCount);
            joinLabels[intCount].setLocation(300, 330 + 70*intCount);
        }

        hostLabels[3].setFont(new Font("Dialog", Font.BOLD, 21));
        hostLabels[3].setSize(600, 50);
        hostLabels[3].setLocation(390, 630);
        
        joinLabels[3].setFont(new Font("Dialog", Font.BOLD, 21));
        joinLabels[3].setSize(600, 50);
        joinLabels[3].setLocation(390, 630);

        // Network Panel
        for(int intCount = 0; intCount < 2; intCount++){
            name[intCount].setSize(500, 50);
            name[intCount].setLocation(390, 330);

            port[intCount].setSize(500, 50);
            port[intCount].setLocation(390, 400);

            ip[intCount].setSize(500, 50);
            ip[intCount].setLocation(390, 470);

            players[intCount].setSize(500, 300);
            players[intCount].setLocation(390, 20);

            netPanels[intCount].setFocusable(true);
            netPanels[intCount].requestFocus();
            netPanels[intCount].setPreferredSize(new Dimension(1280, 720));
            netPanels[intCount].add(port[intCount]);
            netPanels[intCount].add(ip[intCount]);
            netPanels[intCount].add(name[intCount]);
            netPanels[intCount].add(players[intCount]);
            theFrame.add(netPanels[intCount]);
        }
        netPanels[0].add(buttonstart);


        for(int intCount = 0; intCount < 4; intCount++){
            netPanels[0].add(hostLabels[intCount]);
            netPanels[1].add(joinLabels[intCount]);
        }

        netPanels[0].add(host);
        netPanels[1].add(join);

        


        /* Panel 2
        panel2.setSize(new Dimension(1280,620));
        panel2.setBackground(Color.RED);
        panel2.setLayout(new BorderLayout());*/

        /* HUD Panel
        hudPanel.setSize(new Dimension(1280, 100));
        hudPanel.setBackground(new Color(0,0,100));
        hudPanel.setLayout(new BorderLayout());*/

        /*for(int intplayer = 0; intplayer < 4; intplayer++){
            playerNames[intplayer].setForeground(Color.BLACK);
            playerNames[intplayer].setSize(80,20);
            playerNames[intplayer].setLocation(8, 5+20*intplayer);
            playerNames[intplayer].setVisible(true);
            //mainPanel.add(playerNames[intplayer]);
        }*/

        //hudPanel.setVisible(true);

        // Add the panels
        
        theFrame.setContentPane(startPanel);
        //mainPanel.add(panel2);
        //mainPanel.add(hudPanel);
        // Frame
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setVisible(true);
        theFrame.pack();
        timer.start();
    }

    // Override ActionPerformed Method
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == timer){mainPanel.repaint();
            if( theFrame.getContentPane() == netPanels[0] && ssm!= null && inthostid == 1 ){
                network.senddata(ssm);
                intjoinid = 0;
            }
        }

        if(evt.getSource() == mainMenuButtons[0]){
            theFrame.setContentPane(netPanels[0]);
            theFrame.pack();
            netPanels[0].setVisible(true);
        } else if(evt.getSource() == mainMenuButtons[1]){
            theFrame.setContentPane(netPanels[1]);
            theFrame.pack();   
            netPanels[1].setVisible(true);
        } else if(evt.getSource() == mainMenuButtons[2]){
           theFrame.setContentPane(settingsPanel);
           theFrame.pack();
           settingsPanel.setVisible(true);
        } else if(evt.getSource() == mainMenuButtons[3]){
            System.exit(0);
        }
        
        if(evt.getSource() == host){
            // Write Host to File
            try (PrintWriter writehost = new PrintWriter(new FileWriter("Players.txt", false))) {
                writehost.println(name[0].getText());
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            // Display
            inthostid = 1;
            
        
            buttonstart.setEnabled(true);
            host.setEnabled(false);
            hostLabels[3].setText("Now hosting network from "+ip[0].getText()+" at port "+port[0].getText());
            players[0].setText(name[0].getText()+" 👑");
            netPanels[0].repaint();
            

            ssm = new SuperSocketMaster(Integer.parseInt(port[0].getText()), this);
            ssm.connect();
            System.out.println("ip : "  + ssm.getMyAddress());
            ip[0].setText(ssm.getMyAddress());
        }

        if(evt.getSource() == join){
            // Write Players to File
            inthostid = 1;
            try{
                BufferedReader readplayers = new BufferedReader(new FileReader("Players.txt"));
                PrintWriter writeplayers = new PrintWriter(new FileWriter("Players.txt", true));
                String strName = readplayers.readLine();
                players[1].setText(strName+" 👑");
                strName = readplayers.readLine();
                while(strName != null){
                    players[1].append("\n"+strName);
                    strName = readplayers.readLine();
                }
                writeplayers.append("\n"+name[1].getText());
                players[1].append("\n"+name[1].getText());
                readplayers.close();
                writeplayers.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            // Display
            join.setEnabled(false);
            joinLabels[3].setText("Joined network hosted from "+ip[1].getText()+" at port "+port[1].getText());
            netPanels[1].repaint();

            ssm = new SuperSocketMaster(ip[1].getText(), Integer.parseInt(port[1].getText()), this);
            
            ssm.connect();
            

        }

        if(evt.getSource() == buttonchar[1] || evt.getSource() == buttonchar[2] || evt.getSource() == buttonchar[3] || evt.getSource() == buttonchar[0]){
           
            for(int i = 0; i < 4; i++){
                if(evt.getSource() == buttonchar[i]){
                    intpastcharbutton[intjoinid] = intcharbutton[intjoinid];
                    buttonchar[intpastcharbutton[intjoinid]].setEnabled(true);
                    ssm.sendText("m,oldbutton," + intpastcharbutton[intjoinid]);
                    intcharbutton[intjoinid] = i;
                    buttonchar[i].setEnabled(false);

                    System.out.println(intjoinid);
                    ssm.sendText("m,charbutton," + intjoinid + "," + i + "," + intpastcharbutton[intjoinid]);
                }
                
            }
            
            
        }
        

        if(evt.getSource() == buttonstart){
            theFrame.setContentPane(characterPanel);
            theFrame.pack();
            ssm.sendText("m,start");
            if(inthostid == 1){
                intjoinid = 0;
            }
            
        }

        if(evt.getSource() == buttonready){
            mainPanel.setFocusable(true);
            mainPanel.requestFocus();
            theFrame.setContentPane(mainPanel);
            theFrame.pack();
            ssm.sendText("m,ready");
        }
        
        if(evt.getSource() == ssm){
            network.readdata(ssm);
            
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}