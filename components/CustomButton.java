package Components;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;

import Framework.ResourceLoader;

public class CustomButton extends AbstractButton implements MouseListener {

    private ResourceLoader resLoader = new ResourceLoader();

    private Dimension size = new Dimension();
    private Point location = new Point();

    private ActionListener listener;
    private BufferedImage[] biImages;
    private Font font;
    private String strText;

    private int intFrameCount = 0;

    private boolean blnMouseEntered = false;
    private boolean blnEnabled = true;

    public CustomButton(int intWidth, int intHeight, BufferedImage[] biImages, ActionListener listener) {
        this(intWidth, intHeight, null, biImages, listener);
    }

    public CustomButton(float fltWidth, float fltHeight, String strText, BufferedImage[] biImages, ActionListener listener) {
        super();
        size.setSize((int)fltWidth, (int)fltHeight);
        this.strText = strText;
        this.biImages = biImages;
        this.listener = listener;

        font = resLoader.loadFont("/res\\bitwise.ttf", fltHeight/2);

        enableInputMethods(true);
        addActionListener(this.listener);
        addMouseListener(this);
        setSize(size);
        setMaximumSize(new Dimension((int)fltWidth + 10, (int)fltHeight + 10));
        setMinimumSize(size);
        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(blnEnabled && blnMouseEntered) {
            if(blnEnabled && getWidth() < getMaximumSize().width && getHeight() < getMaximumSize().height) {
                setSize(new Dimension(getWidth() + 2, getHeight() + 2));
                setLocation(getX() - 1, getY() - 1);
                intFrameCount++;
            }
            
            if(biImages != null) g.drawImage(biImages[intFrameCount], intFrameCount - biImages.length + 1, intFrameCount - biImages.length + 1, null);

            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.white);
            if(strText != null) g.drawString(strText, (getWidth() - fm.stringWidth(strText))/2, (fm.getAscent() + (getHeight() - fm.getHeight())/2));
        } else {
            if(getWidth() > getMinimumSize().width && getHeight() > getMinimumSize().height) {
                setSize(new Dimension(getWidth() - 2, getHeight() - 2));
                setLocation(getX() + 1, getY() + 1);
                intFrameCount--;
            }
            g.setColor(Color.blue);
            if(biImages != null) g.drawImage((blnEnabled) ? biImages[intFrameCount] : biImages[biImages.length - 1], intFrameCount - biImages.length + 1, intFrameCount - biImages.length + 1, null);
            else g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.white);
            if(strText != null) g.drawString(strText, (getWidth() - fm.stringWidth(strText))/2, (fm.getAscent() + (getHeight() - fm.getHeight())/2));
        }
    }

    public void setLocation(int intX, int intY) {
        super.setLocation(intX, intY);
        
        if(location.x == 0 && location.y == 0) {
            location.x = intX;
            location.y = intY;
        }
    }

    public void setEnabled(boolean blnEnabled) {
        this.blnEnabled = blnEnabled;
    }

    public void mouseReleased(MouseEvent evt) {
        if(blnEnabled && listener != null) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), evt.getWhen(), evt.getModifiersEx()));
            setSize(size);
            setLocation(location.x, location.y);
            blnMouseEntered = false;
            intFrameCount = 0;
        }
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
