import java.awt.Dimension;
import javax.swing.JFrame;

public class CPTMain {

    private JFrame theFrame = new JFrame("CPT Game Proto");

    public CPTMain() {
        theFrame.setPreferredSize(new Dimension(1920, 1080));
        theFrame.pack();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setLocationRelativeTo(null);
        theFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new CPTMain();
    }
}