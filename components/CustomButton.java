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
    //extends the abstractbutton class to create our own button
    // Properties
    /**
     * The ResourceLoader object used to load resources
     */
    private ResourceLoader resLoader = new ResourceLoader();

    /**
     * Dimension object representing the size of the button
     */
    private Dimension size = new Dimension();
    /**
     * Point object representing the position of the button
     */
    private Point location = new Point();

    /**
     * The ActionListener registered to the button
     */
    private ActionListener listener;
    /**
     * The images used as textures for the button
     */
    private BufferedImage[] biImages;
    /**
     * The Font object used by the button for drawing text
     */
    private Font font;
    /**
     * The text to be drawn by the button
     */
    private String strText;

    /**
     * The frame the button is currently on
     */
    private int intFrameCount = 0;

    /**
     * Boolean to determine if the mouse lies within the bounds of the button
     */
    private boolean blnMouseEntered = false;
    /**
     * Boolean to determine if the button is currently enabled
     */
    private boolean blnEnabled = true;

    // Constructors
    /**
     * The constructor that allows you to set the width, height, images, and ActionListener of the button
     * 
     * @param intWidth The width of the button
     * @param intHeight The height of the button
     * @param biImages The images used for the button
     * @param listener The ActionListener registered to the button
     */
    public CustomButton(int intWidth, int intHeight, BufferedImage[] biImages, ActionListener listener) {
        this(intWidth, intHeight, null, biImages, listener);
    }

    /**
     * The constructor that allows you to set the width, height, text, images, and ActionListener of the button
     * 
     * @param fltWidth The width of the button
     * @param fltHeight The height of the button
     * @param strText The text of the button
     * @param biImages The images used for the button
     * @param listener The ActionListener registered to the button
     */
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
        //adds mouse and action listeners to the button immidiatly while also setting the size right awat
    }

    /**
     * The paint method of the button
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(blnEnabled && blnMouseEntered) {
            if(blnEnabled && getWidth() < getMaximumSize().width && getHeight() < getMaximumSize().height) {
                setSize(new Dimension(getWidth() + 2, getHeight() + 2));
                setLocation(getX() - 1, getY() - 1);
                intFrameCount++;
            }
            //if the mouse entered the area of the button, enlarge it
            
            if(biImages != null) g.drawImage(biImages[intFrameCount], intFrameCount - biImages.length + 1, intFrameCount - biImages.length + 1, null);
            //draws the sprite for the button
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.white);
            //draws the words on the button
            if(strText != null) g.drawString(strText, (getWidth() - fm.stringWidth(strText))/2, (fm.getAscent() + (getHeight() - fm.getHeight())/2));
        } else {
            if(getWidth() > getMinimumSize().width && getHeight() > getMinimumSize().height) {
                setSize(new Dimension(getWidth() - 2, getHeight() - 2));
                setLocation(getX() + 1, getY() + 1);
                intFrameCount--;
            }
            //makes the buttons a certain size
            g.setColor(Color.blue);
            if(biImages != null) g.drawImage((blnEnabled) ? biImages[intFrameCount] : biImages[biImages.length - 1], intFrameCount - biImages.length + 1, intFrameCount - biImages.length + 1, null);
            else g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(font);
            //draws out more things for the buttons
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.white);
            if(strText != null) g.drawString(strText, (getWidth() - fm.stringWidth(strText))/2, (fm.getAscent() + (getHeight() - fm.getHeight())/2));
            //draws the string for the buttons
        }
    }

    /**
     * Method to set the location of the button
     */
    public void setLocation(int intX, int intY) {
        super.setLocation(intX, intY);
        
        if(location.x == 0 && location.y == 0) {
            location.x = intX;
            location.y = intY;
        }
        //sets the location of the button
    }

    /**
     * Method to set the button enabled is disabled
     */
    public void setEnabled(boolean blnEnabled) {
        this.blnEnabled = blnEnabled;
    }
    //sets if the button is enabled or not

    public void mouseReleased(MouseEvent evt) {
        if(blnEnabled && listener != null) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), evt.getWhen(), evt.getModifiersEx()));
            setSize(size);
            setLocation(location.x, location.y);
            blnMouseEntered = false;
            intFrameCount = 0;
        }
    }
    //checks to see if the button is no longer being pressed

    /**
     * Method to determine if the mouse has entered the bounds of the button
     */
    public void mouseEntered(MouseEvent evt) {
        blnMouseEntered = true;
        repaint();
    }
    //ensures that the mouse is inside the area of the button

    /**
     * Method to determine if the mouse has exited the bounds of the button
     */
    public void mouseExited(MouseEvent evt) {
        blnMouseEntered = false;
        repaint();
        //ensures that the mouse has left the area of the button
    }

    //unused
    public void mouseClicked(MouseEvent evt) {}

    public void mousePressed(MouseEvent evt) {}
}
