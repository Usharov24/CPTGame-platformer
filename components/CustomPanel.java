package components;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.JPanel;

import framework.Main;

public class CustomPanel extends JPanel {

    public CustomPanel() {
        super();
    }

    public CustomPanel(LayoutManager layout) {
        super(layout);
    }

    public CustomPanel(LayoutManager layout, boolean blnFocusable) {
        super(layout);
        setFocusable(blnFocusable);
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
