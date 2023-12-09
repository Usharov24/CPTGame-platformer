import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main implements ActionListener {
    public int[][] intplayercords = new int[4][4];
    //4 wide because of the 4 player maximum
    public DrawPanel mainPanel = new DrawPanel();
    //panel that contains the rest of the panels
    public JPanel hudPanel = new JPanel();
    public JPanel panel2 = new JPanel();
    private JFrame theFrame = new JFrame("CPT Game Proto");

    public JLabel[] playerNames = {new JLabel("Player 1"), new JLabel("Player 2"), new JLabel("Player 3"), new JLabel("Player 4")};

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
        theFrame.add(mainPanel);
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