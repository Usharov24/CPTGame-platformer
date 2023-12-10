import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;

public class CustomButton extends AbstractButton implements MouseListener {

    private Dimension size = new Dimension();

    private ActionListener listener;

    private boolean mouseEntered = false;

    public CustomButton() {
        this(0, 0, null);
    }

    public CustomButton(int intWidth, int intHeight) {
        this(intWidth, intHeight, null);
    }

    public CustomButton(int intWidth, int intHeight, ActionListener listener) {
        super();
        
        size.setSize(intWidth, intHeight);
        this.listener = listener;

        enableInputMethods(true);
        addActionListener(this.listener);
        addMouseListener(this);

        setSize(size);
        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(mouseEntered) {
            g.setColor(Color.green);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(Color.red);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void mouseClicked(MouseEvent evt) {}

    public void mousePressed(MouseEvent evt) {}

    public void mouseReleased(MouseEvent evt) {
        if(listener != null) listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), evt.getWhen(), evt.getModifiersEx()));
    }

    public void mouseEntered(MouseEvent evt) {
        mouseEntered = true;
        repaint();
    }

    public void mouseExited(MouseEvent evt) {
        mouseEntered = false;
        repaint();
    }
}
