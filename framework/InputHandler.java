package Framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EnumSet;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    
    // Input Buttons

    public EnumSet<InputButtons> buttonSet = EnumSet.noneOf(InputButtons.class);

    public float fltMouseX, fltMouseY;

    public enum InputButtons {
        W, A, S, D, F, SHIFT, SPACE, ENTER, BUTTON1, BUTTON2, BUTTON3;
    }

    // Key Press
    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_W) buttonSet.add(InputButtons.W);
        if(evt.getKeyCode() == KeyEvent.VK_A) buttonSet.add(InputButtons.A);
        if(evt.getKeyCode() == KeyEvent.VK_S) buttonSet.add(InputButtons.S);
        if(evt.getKeyCode() == KeyEvent.VK_D) buttonSet.add(InputButtons.D);
        if(evt.getKeyCode() == KeyEvent.VK_F) buttonSet.add(InputButtons.F);
        if(evt.getKeyCode() == KeyEvent.VK_SHIFT) buttonSet.add(InputButtons.SHIFT);
        if(evt.getKeyCode() == KeyEvent.VK_SPACE) buttonSet.add(InputButtons.SPACE);
    }

    // Key Release
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
    }

    // Mouse Press
    public void mousePressed(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();

        if(evt.getButton() == MouseEvent.BUTTON1) buttonSet.add(InputButtons.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) buttonSet.add(InputButtons.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) buttonSet.add(InputButtons.BUTTON3);
    }

    // Mouse Release
    public void mouseReleased(MouseEvent evt) {
        if(evt.getButton() == MouseEvent.BUTTON1) buttonSet.remove(InputButtons.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) buttonSet.remove(InputButtons.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) buttonSet.remove(InputButtons.BUTTON3);
    }

    // Mouse Drag
    public void mouseDragged(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();
    }

    // Mouse Move
    public void mouseMoved(MouseEvent evt) {
        fltMouseX = evt.getX();
        fltMouseY = evt.getY();
    }

    // Unused Methods
    public void keyTyped(KeyEvent evt) {}

    public void mouseEntered(MouseEvent evt) {}

    public void mouseExited(MouseEvent evt) {}

    public void mouseClicked(MouseEvent evt) {}

}
