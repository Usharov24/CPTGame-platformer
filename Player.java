import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.EnumSet;

public class Player extends GameObject implements KeyListener {

    private enum InputKeys {
        W, A, S, D;
    }

    private EnumSet<InputKeys> keySet = EnumSet.noneOf(InputKeys.class);

    private float acceleration = 1f, deceleration = 0.5f;

    public Player(float fltX, float fltY, float fltWidth, float fltHeight) {
        super(fltX, fltY, fltWidth, fltHeight);
    }

    public void update(LinkedList<GameObject> objectList) {
        if(keySet.contains(InputKeys.W)) fltVelY -= acceleration;
        else if(keySet.contains(InputKeys.S)) fltVelY += acceleration;
        else if(keySet.contains(InputKeys.W) && keySet.contains(InputKeys.S)) {
            if(fltVelY > 0) fltVelY -= deceleration;
            else if(fltVelY < 0) fltVelY += deceleration;
        } else {
            if(fltVelY > 0) fltVelY -= deceleration;
            else if(fltVelY < 0) fltVelY += deceleration;
        }

        if(keySet.contains(InputKeys.A)) fltVelX -= acceleration;
        else if(keySet.contains(InputKeys.D)) fltVelX += acceleration;
        else if(keySet.contains(InputKeys.A) && keySet.contains(InputKeys.D)) {
            if(fltVelX > 0) fltVelX -= deceleration;
            else if(fltVelX < 0) fltVelX += deceleration;
        } else {
            if(fltVelX > 0) fltVelX -= deceleration;
            else if(fltVelX < 0) fltVelX += deceleration;
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

    public void keyTyped(KeyEvent evt) {}
}
