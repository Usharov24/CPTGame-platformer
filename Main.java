import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Main implements ActionListener {
    public int[][] intplayercords = new int[4][4];
    //4 wide because of the 4 player maximum
    public DrawPanel mainPanel = new DrawPanel();
    //panel that contains the rest of the panels

    //Panels
    public JPanel hudPanel = new JPanel();
    public JPanel panel2 = new JPanel();
    public JPanel startPanel = new JPanel(null);
    public JPanel[] netPanels = {new JPanel(null), new JPanel(null)};
    public ChatPanel chatPanel = new ChatPanel();

    private JFrame theFrame = new JFrame("CPT Game Proto");
    private JButton hostbutton = new JButton("Host");
    private JButton joinbutton = new JButton("Join");
    private JButton settingbutton = new JButton("Settings");
    private JButton quitbutton = new JButton("quit");
    private JLabel startmenulabel = new JLabel("Game!");

    //Host & Join
    private JTextField port = new JTextField();
    private JTextField ip = new JTextField();
    private JButton host = new JButton("Host Network");
    private JButton join = new JButton("Join Network");
    private JLabel[] netLabels = {new JLabel("Port Number"), new JLabel("IP Address"), new JLabel("")};

    private JLabel[] playerNames = {new JLabel("Player 1"), new JLabel("Player 2"), new JLabel("Player 3"), new JLabel("Player 4")};

    // TEMPORARY ////////////////////////////////////////////////////////////////
    Player player = new Player(0, 0, 32, 32);
    public static ObjectHandler handler = new ObjectHandler();
    /////////////////////////////////////////////////////////////////////////////

    // Create Timer
    private Timer timer = new Timer(1000/60, this);
    
    public Main() {
        // TEMP ///////
        handler.addObject(player);
        mainPanel.addKeyListener(player);
        ///////////////

        // Main Panel
        mainPanel.setFocusable(true);
        mainPanel.requestFocus();
        mainPanel.setPreferredSize(new Dimension(1280,720));
        mainPanel.setLayout(null);

        //Buttons for Main Menu
        hostbutton.addActionListener(this);
        joinbutton.addActionListener(this);
        settingbutton.addActionListener(this);
        quitbutton.addActionListener(this);
        hostbutton.setLocation(600,200);
        joinbutton.setLocation(600,300);
        settingbutton.setLocation(600,400);
        quitbutton.setLocation(600,500);
        hostbutton.setSize(100,100);
        joinbutton.setSize(100,100);
        quitbutton.setSize(100,100);
        settingbutton.setSize(100,100);


        //title for main menu
        startPanel.add(startmenulabel);
        startmenulabel.setSize(100,100);
        startmenulabel.setLocation(600,20);


        //start panel settigns
        startPanel.add(hostbutton);
        startPanel.add(joinbutton);
        startPanel.add(settingbutton);
        startPanel.add(quitbutton);
        startPanel.setPreferredSize(new Dimension(1280,720));

        //Host & Join Network
        port.setSize(200, 50);
        port.setLocation(30, 300);

        ip.setSize(200, 50);
        ip.setLocation(350, 300);

        host.addActionListener(this);
        host.setSize(1220, 70);
        host.setLocation(30, 550);

        join.addActionListener(this);
        join.setSize(1220,70);
        join.setLocation(30, 550);

        netLabels[0].setLocation(30, 300);
        netLabels[1].setLocation(600, 300);
        netLabels[2].setLocation(30, 700);

        // Network Panel
        for(int intCount = 0; intCount < 2; intCount++){
            netPanels[intCount].setFocusable(true);
            netPanels[intCount].requestFocus();
            netPanels[intCount].setPreferredSize(new Dimension(1280, 720));
            netPanels[intCount].add(port);
            netPanels[intCount].add(ip);
            theFrame.add(netPanels[intCount]);
        }

        for(int intCount = 0; intCount < 3; intCount++){
            netLabels[intCount].setSize(100, 50);
            netPanels[0].add(netLabels[intCount]);
            netPanels[1].add(netLabels[intCount]);
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
        
        theFrame.add(startPanel);
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
        if(evt.getSource() == timer) mainPanel.repaint();
        if(evt.getSource() == hostbutton){
            theFrame.setContentPane(netPanels[0]);
            netPanels[0].setFocusable(true);
            theFrame.pack();   
            netPanels[0].setVisible(true);
        }
        if(evt.getSource() == joinbutton){
            theFrame.setContentPane(mainPanel);
            mainPanel.setFocusable(true);
            theFrame.pack();
            mainPanel.addKeyListener(player);
            mainPanel.requestFocus();
            
        }
        if(evt.getSource() == settingbutton){
           
        }
        if(evt.getSource() == quitbutton){
            System.exit(0);
           
        }

    }

    public void shoot(){

    }
    public void jump(){

    }
    //potential methods needed in the future which are both non-static
    public static void main(String[] args) {
        new Main();
    }
}