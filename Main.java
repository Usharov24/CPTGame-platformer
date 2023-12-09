import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

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
    private JTextField[] port = {new JTextField(), new JTextField()};
    private JTextField[] ip = {new JTextField(), new JTextField()};
    private JTextField[] name = {new JTextField(), new JTextField()};
    private JTextArea[] players = {new JTextArea(), new JTextArea()};
    private JButton host = new JButton("Host Network");
    private JButton join = new JButton("Join Network");
    private JLabel[] hostLabels = {new JLabel("Enter Name"), new JLabel("Port Number"), new JLabel("IP Address"), new JLabel("")};
    private JLabel[] joinLabels = {new JLabel("Enter Name"), new JLabel("Port Number"), new JLabel("IP Address"), new JLabel("")};

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
        host.addActionListener(this);
        host.setSize(500, 70);
        host.setLocation(390, 540);

        join.addActionListener(this);
        join.setSize(500,70);
        join.setLocation(390, 540);

        // These will most likely be moved into a for loop in the future after the layout design has been finalized
        hostLabels[3].setFont(new Font("Dialog", Font.BOLD, 21));
        hostLabels[0].setSize(100, 50);
        hostLabels[1].setSize(100, 50);
        hostLabels[2].setSize(100, 50);
        hostLabels[3].setSize(600, 50);
        hostLabels[0].setLocation(300, 330);
        hostLabels[1].setLocation(300, 400);
        hostLabels[2].setLocation(300, 470);
        hostLabels[3].setLocation(390, 630);
        
        joinLabels[3].setFont(new Font("Dialog", Font.BOLD, 21));
        joinLabels[0].setSize(100, 50);
        joinLabels[1].setSize(100, 50);
        joinLabels[2].setSize(100, 50);
        joinLabels[3].setSize(600, 50);
        joinLabels[0].setLocation(300, 330);
        joinLabels[1].setLocation(300, 400);
        joinLabels[2].setLocation(300, 470);
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
            theFrame.pack();   
            netPanels[0].setVisible(true);
        }
        if(evt.getSource() == host){
            hostLabels[3].setText("Now hosting network from "+ip[0].getText()+" at port "+port[0].getText());
            players[0].setText(name[0].getText()+" 👑");
            playerNames[0].setText(name[0].getText());
            netPanels[0].repaint();
        }
        if(evt.getSource() == joinbutton){
            theFrame.setContentPane(netPanels[1]);
            theFrame.pack();   
            netPanels[1].setVisible(true);

            //theFrame.setContentPane(mainPanel);
            //mainPanel.setFocusable(true);
            //theFrame.pack();
            //mainPanel.addKeyListener(player);
            //mainPanel.requestFocus();
            
        }
        if(evt.getSource() == join){
            joinLabels[3].setText("Joined network hosted from "+ip[1].getText()+" at port "+port[1].getText());
            players[1].setText(name[1].getText());
            playerNames[1].setText(name[1].getText());
            netPanels[1].repaint();
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