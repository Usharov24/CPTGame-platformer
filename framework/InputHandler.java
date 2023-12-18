package framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EnumSet;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener{
    
    public EnumSet<InputKeys> keySet = EnumSet.noneOf(InputKeys.class);
    public EnumSet<InputMouse> mouseSet = EnumSet.noneOf(InputMouse.class);
    public static float fltClickX;
    public static float fltClickY;

    public enum InputKeys {
        W, A, S, D;
    }

    public enum InputMouse {
        BUTTON1, BUTTON2, BUTTON3;
    }

    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_W) keySet.add(InputKeys.W);
        if(evt.getKeyCode() == KeyEvent.VK_A) keySet.add(InputKeys.A);
        if(evt.getKeyCode() == KeyEvent.VK_S) keySet.add(InputKeys.S);
        if(evt.getKeyCode() == KeyEvent.VK_D) keySet.add(InputKeys.D);
    }

    public void keyReleased(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_W) keySet.remove(InputKeys.W);
        if(evt.getKeyCode() == KeyEvent.VK_A) keySet.remove(InputKeys.A);
        if(evt.getKeyCode() == KeyEvent.VK_S) keySet.remove(InputKeys.S);
        if(evt.getKeyCode() == KeyEvent.VK_D) keySet.remove(InputKeys.D);
    }

    public void mousePressed(MouseEvent evt) {
        if(evt.getButton() == MouseEvent.BUTTON1) mouseSet.add(InputMouse.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) mouseSet.add(InputMouse.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) mouseSet.add(InputMouse.BUTTON3);
        
    }
    
    public void keyTyped(KeyEvent evt) {}

    public void mouseExited(MouseEvent evt) {}

    public void mouseClicked(MouseEvent evt) {}

    public void mouseReleased(MouseEvent evt) {
        if(evt.getButton() == MouseEvent.BUTTON1) mouseSet.remove(InputMouse.BUTTON1);
        if(evt.getButton() == MouseEvent.BUTTON2) mouseSet.remove(InputMouse.BUTTON2);
        if(evt.getButton() == MouseEvent.BUTTON3) mouseSet.remove(InputMouse.BUTTON3);
    }

    public void mouseEntered(MouseEvent evt) {}
    
    
    public void mouseMoved(MouseEvent evt) {
        if(!mouseSet.contains(InputMouse.BUTTON1) && !mouseSet.contains(InputMouse.BUTTON2) && !mouseSet.contains(InputMouse.BUTTON3)){
            fltClickX = evt.getX();
            fltClickY = evt.getY();
          
        }
    }

    public void mouseDragged(MouseEvent evt) {
        if(mouseSet.contains(InputMouse.BUTTON1) || mouseSet.contains(InputMouse.BUTTON2) || mouseSet.contains(InputMouse.BUTTON3)){
            fltClickX = evt.getX();
            fltClickY = evt.getY();
            
        }
    }

    
    
    
    }
