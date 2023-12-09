import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CPTMain {
    public int[][] intplayercords = new int[4][4];
    //4 wide because of the 4 player maximum
    public JPanel mainpanel = new JPanel();
    //panel that contains the rest of the panels
    public JPanel hudpanel = new JPanel();
    public JPanel panel2 = new JPanel();
    private JFrame theFrame = new JFrame("CPT Game Proto");
    
    public CPTMain() {
    
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        theFrame.setResizable(false);
        theFrame.setLocationRelativeTo(null);
        theFrame.setVisible(true);
        mainpanel.setPreferredSize(new Dimension(1280,720));
        theFrame.add(mainpanel);
        mainpanel.add(panel2);
        mainpanel.add(hudpanel);
        mainpanel.setLayout(null);
        panel2.setBounds(0,0,1080,720);
        panel2.setBackground(Color.RED);
        hudpanel.setBounds(1080,0,200,720);
        hudpanel.setBackground(new Color(0,0,100));
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