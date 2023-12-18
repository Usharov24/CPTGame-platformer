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
    public static float fltclickx;
    public static float fltclicky;

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
        /*if(evt.getButton() == MouseEvent.BUTTON1){
            float fltDiffX = evt.getX() - (fltX + fltWidth/2);
            float fltDiffY = evt.getY() - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            // - 5 is for the width and height of the bullet
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler));
            //network.sendMessage("o,b,"+ (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + fltDiffX * 20 + "," + fltDiffY * 20 + "," + 10 + "," + 10);
        }
        if(evt.getButton() == MouseEvent.BUTTON3){
            float fltDiffX = evt.getX() - (fltX + fltWidth/2);
            float fltDiffY = evt.getY() - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            // - 5 is for the width and height of the bullet
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 + (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 + (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 + (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20 + (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 10, 10, ObjectId.BULLET, handler));
        }*/
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
            fltclickx = evt.getX();
            fltclicky = evt.getY();
          
        }
    }

    public void mouseDragged(MouseEvent evt) {
        if(mouseSet.contains(InputMouse.BUTTON1) || mouseSet.contains(InputMouse.BUTTON2) || mouseSet.contains(InputMouse.BUTTON3)){
            fltclickx = evt.getX();
            fltclicky = evt.getY();
            
        }
    }

    
    
    
    }
