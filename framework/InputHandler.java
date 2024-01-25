package Framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EnumSet;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    
    // Input Buttons
    /**
     * EnumSet containing the input keys
     */
    public EnumSet<InputButtons> buttonSet = EnumSet.noneOf(InputButtons.class);

    /**
     * The x and y position of the mouse on the screen
     */
    public float fltMouseX, fltMouseY;

    /**
     * Enum of all the input keys
     */
    public enum InputButtons {
        W, A, S, D, F, SHIFT, SPACE, ENTER, BUTTON1, BUTTON2, BUTTON3;
    }

    // Key Press
    /**
     * The method to determine if a key has been pressed
     */
    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_W) buttonSet.add(InputButtons.W);
        if(evt.getKeyCode() == KeyEvent.VK_A) buttonSet.add(InputButtons.A);
        if(evt.getKeyCode() == KeyEvent.VK_S) buttonSet.add(InputButtons.S);
        if(evt.getKeyCode() == KeyEvent.VK_D) buttonSet.add(InputButtons.D);
        if(evt.getKeyCode() == KeyEvent.VK_F) buttonSet.add(InputButtons.F);
        if(evt.getKeyCode() == KeyEvent.VK_SHIFT) buttonSet.add(InputButtons.SHIFT);
        if(evt.getKeyCode() == KeyEvent.VK_SPACE) buttonSet.add(InputButtons.SPACE);
        //adds keys to the list if they are pressed
    }

    // Key Release
    /**
     * The method to determine if a key has been released
     */
    public void keyReleased(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_W) buttonSet.remove(InputButtons.W);
        if(evt.getKeyCode() == KeyEvent.VK_A) buttonSet.remove(InputButtons.A);
        if(evt.getKeyCode() == KeyEvent.VK_S) buttonSet.remove(InputButtons.S);
        if(evt.getKeyCode() == KeyEvent.VK_D) buttonSet.remove(InputButtons.D);
        if(evt.getKeyCode() == KeyEvent.VK_F) buttonSet.remove(InputButtons.F);
        if(evt.getKeyCode() == KeyEvent.VK_SHIFT) buttonSet.remove(InputButtons.SHIFT);
        if(evt.getKeyCode() == KeyEvent.VK_SPACE) buttonSet.remove(InputButtons.SPACE);
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(buttonSet.contains(InputButtons.ENTER)) buttonSet.remove(InputButtons.ENTER);
            else buttonSet.add(InputButtons.ENTER);
        }
        //removes certain keys from the list if they are released
    }

    // Mouse Press
    /**
     * The method to determine if a mouse button has been pressed
     */
    public void mousePressed(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();

        if(evt.getButton() == MouseEvent.BUTTON1) buttonSet.add(InputButtons.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) buttonSet.add(InputButtons.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) buttonSet.add(InputButtons.BUTTON3);
        //adds mouse presses to the list if pressed
    }

    // Mouse Release
    /**
     * The method to determine if a mouse button has been released
     */
    public void mouseReleased(MouseEvent evt) {
        if(evt.getButton() == MouseEvent.BUTTON1) buttonSet.remove(InputButtons.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) buttonSet.remove(InputButtons.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) buttonSet.remove(InputButtons.BUTTON3);
        //removes mouse presses when the button is released
    }

    // Mouse Drag
    /**
     * The method to determine if the mouse is being dragged
     */
    public void mouseDragged(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();
        //gets the x and y of the mouse
    }

    // Mouse Move
    /*The method to determine if a mouse is being moved */
    public void mouseMoved(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();
        //gets the x and y of the mouse
    }

    // Unused Methods
    public void keyTyped(KeyEvent evt) {}

    public void mouseEntered(MouseEvent evt) {}

    public void mouseExited(MouseEvent evt) {}

    public void mouseClicked(MouseEvent evt) {}

}
