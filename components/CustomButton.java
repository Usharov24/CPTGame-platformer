package components;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;

import framework.ResourceLoader;

public class CustomButton extends AbstractButton implements MouseListener {

    private ResourceLoader resLoader = new ResourceLoader();

    private Dimension size = new Dimension();

    private ActionListener listener;

    private BufferedImage biImage;
    private Font font;

    private boolean blnMouseEntered = false;

    public CustomButton() {
        this(0, 0, null, null);
    }

    public CustomButton(int intWidth, int intHeight) {
        this(intWidth, intHeight, null, null);
    }

    public CustomButton(int intWidth, int intHeight, BufferedImage biImage) {
        this(intWidth, intHeight, biImage, null);
    }

    public CustomButton(int intWidth, int intHeight, BufferedImage biImage, ActionListener listener) {
        super();
        
        size.setSize(intWidth, intHeight);
        this.biImage = biImage;
        this.listener = listener;

        font = resLoader.loadFont("res\\bitwise.ttf");

        enableInputMethods(true);
        addActionListener(this.listener);
        addMouseListener(this);

        setSize(size);
        setMaximumSize(new Dimension(intWidth + 10, intHeight + 10));
        setMinimumSize(size);
        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(blnMouseEntered) {
            if(getWidth() < getMaximumSize().width && getHeight() < getMaximumSize().height) {
                setSize(new Dimension(getWidth() + 2, getHeight() + 2));
                setLocation(getX() - 1, getY() - 1);
            }

            g.setColor(Color.green);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            if(getWidth() > getMinimumSize().width && getHeight() > getMinimumSize().height) {
                setSize(new Dimension(getWidth() - 2, getHeight() - 2));
                setLocation(getX() + 1, getY() + 1);
            }

            g.setColor(Color.red);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void mouseReleased(MouseEvent evt) {
        if(listener != null) listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), evt.getWhen(), evt.getModifiersEx()));
    }

    public void mouseEntered(MouseEvent evt) {
        blnMouseEntered = true;
        repaint();
    }

    public void mouseExited(MouseEvent evt) {
        blnMouseEntered = false;
        repaint();
    }

    public void mouseClicked(MouseEvent evt) {}

    public void mousePressed(MouseEvent evt) {}
}
