import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

    public void paintComponent(Graphics g){
        // TEMP //////////
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        Main.handler.update();
        Main.handler.draw(g);
        //////////////////
    }
}
