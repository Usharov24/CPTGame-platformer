package components;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.JPanel;

import framework.Main;

public class DrawPanel extends JPanel {

    public DrawPanel() {
        super();
    }

    public DrawPanel(LayoutManager layout) {
        super(layout);
    }

    public void paintComponent(Graphics g){
        // TEMP //////////
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        Main.handler.update();
        Main.handler.draw(g);
        //////////////////
    }
}
