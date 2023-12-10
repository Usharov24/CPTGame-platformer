import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.EnumSet;

public class Player extends GameObject implements KeyListener, MouseListener {

    private enum InputKeys {
        W, A, S, D;
    }

    private EnumSet<InputKeys> keySet = EnumSet.noneOf(InputKeys.class);

    private float fltAcc = 1f, fltDec = 0.5f;
    public int intmousex;
    public int intmousey;


    public Player(float fltX, float fltY, float fltWidth, float fltHeight) {
        super(fltX, fltY, fltWidth, fltHeight);
    }

    public void update(LinkedList<GameObject> objectList) {
        if(keySet.contains(InputKeys.W)) fltVelY -= fltAcc;
        else if(keySet.contains(InputKeys.S)) fltVelY += fltAcc;
        else if(keySet.contains(InputKeys.W) && keySet.contains(InputKeys.S)) {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        } else {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        }

        if(keySet.contains(InputKeys.A)) fltVelX -= fltAcc;
        else if(keySet.contains(InputKeys.D)) fltVelX += fltAcc;
        else if(keySet.contains(InputKeys.A) && keySet.contains(InputKeys.D)) {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        } else {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        }

        if(fltVelX > 10) fltVelX = 10;
        else if(fltVelX < -10) fltVelX = -10;
        if(fltVelY > 10) fltVelY = 10;
        else if(fltVelY < -10) fltVelY = -10;

        fltX += fltVelX;
        fltY += fltVelY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
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

    public void mouseExited(MouseEvent evt){
    }
    public void mouseClicked(MouseEvent evt){
        
        Main.handler.addObject(new Bullet(this.fltX, this.fltY,10,10, (float) evt.getX(), (float) evt.getY()));
    }
    public void mouseReleased(MouseEvent evt){
        
    }
    public void mousePressed(MouseEvent evt){
        
    }
    public void mouseEntered(MouseEvent evt){
        
    }

    public void keyTyped(KeyEvent evt) {}
}
