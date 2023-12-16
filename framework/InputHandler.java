package framework;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;

public class InputHandler implements KeyListener, MouseListener {
    
    public EnumSet<InputKeys> keySet = EnumSet.noneOf(InputKeys.class);

    public enum InputKeys {
        W, A, S, D;
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

    public void mouseReleased(MouseEvent evt) {}

    public void mouseEntered(MouseEvent evt) {}
}
