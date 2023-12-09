import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CPTMain implements ActionListener {
    public int[][] intplayercords = new int[4][4];
    //4 wide because of the 4 player maximum
    public JPanel mainpanel = new JPanel();
    //panel that contains the rest of the panels
    public JPanel hudpanel = new JPanel();
    public JPanel panel2 = new JPanel();
    private JFrame theFrame = new JFrame("CPT Game Proto");

    public JLabel[] playernames = {new JLabel("Player 1"), new JLabel("Player 2"), new JLabel("Player 3"), new JLabel("Player 4")};

    // Create Timer
    private Timer timer = new Timer(1000/60, this);

    // Override ActionPerformed Method
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == timer){
            mainpanel.repaint();
        }
    }
    
    public CPTMain() {
    
        // Frame
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setLocationRelativeTo(null);

        // Main Panel
        mainpanel.setPreferredSize(new Dimension(1280,720));
        mainpanel.setLayout(null);

        // Panel 2
        panel2.setBounds(0,100,1280,620);
        panel2.setBackground(Color.RED);
        panel2.setLayout(new BorderLayout());

        // HUD Panel
        hudpanel.setBounds(0,0,1280,100);
        hudpanel.setBackground(new Color(0,0,100));
        hudpanel.setLayout(new BorderLayout());

        for(int intplayer = 0; intplayer < 4; intplayer++){
            playernames[intplayer].setForeground(Color.BLACK);
            playernames[intplayer].setSize(80,20);
            playernames[intplayer].setLocation(8, 5+20*intplayer);
            playernames[intplayer].setVisible(true);
            mainpanel.add(playernames[intplayer]);
        }

        hudpanel.setVisible(true);

        // Add the panels
        theFrame.add(mainpanel);
        mainpanel.add(panel2);
        mainpanel.add(hudpanel);
        theFrame.setVisible(true);
        theFrame.pack();
        

       
    }

    public void shoot(){

    }
    public void jump(){

    }
    //potential methods needed in the future which are both non-static
    public static void main(String[] args) {
        new CPTMain();
    }
}