package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.InputHandler;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.InputHandler.InputButtons;

public class Player extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;

    private int intJumpCount;
    private int intDirection = 1;

    private boolean blnFalling = true;

    public Player(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, InputHandler input) {
        super(fltX, fltY, fltWidth, fltHeight, id);
        this.handler = handler;
        this.input = input;
    }

    public void update(LinkedList<GameObject> objectList) {
        if(input.buttonSet.contains(InputHandler.InputButtons.W) && intJumpCount < 2) {
            input.buttonSet.remove(InputButtons.W);
            fltVelY -= 50;
            blnFalling = true;
            intJumpCount++;
        } else if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intJumpCount < 2) {
            input.buttonSet.remove(InputButtons.SPACE);
            fltVelY -= 50;
            blnFalling = true;
            intJumpCount++;
        }

        if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
            fltVelX -= fltAcc;
            intDirection = -1;
        }
        else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
            fltVelX += fltAcc;
            intDirection = 1;
        }
        else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        } else {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        }

        if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT)){
            input.buttonSet.remove(InputButtons.SHIFT);
            if(intDirection > 0){
                fltDashVel = 50;
            } else if(intDirection < 0){
                fltDashVel = -50;
            }
        }

        if(blnFalling) fltVelY += 5;

        if(fltVelX > 10) fltVelX = 10;
        else if(fltVelX < -10) fltVelX = -10;

        if(fltVelY > 30) fltVelY = 30;

        if(fltDashVel > 0) fltDashVel -= 5;
        else if(fltDashVel < 0) fltDashVel += 5;

        fltX += fltVelX + fltDashVel;
        fltY += fltVelY;

        collisions();

        if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
            
            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler));
        } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON2)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            handler.addObject(new HomingBullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler, framework.Main.intSessionId));
        } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            for(int intCount = 0; intCount < 2; intCount++) {
                handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 6, 6, ObjectId.BULLET, handler));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 6, 6, ObjectId.BULLET, handler));
            }
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltY = (float)new Rectangle(0, 660, 1280, 10).getY() - fltHeight;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.blue);
        g2d.fill(getBounds());
        g2d.setColor(Color.red);
        g2d.fill(getBounds2());
        g2d.setColor(Color.white);
        g2d.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltX + fltVelX), (int)fltY + 2, (int)fltWidth, (int)fltHeight - 4);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)fltX + 2, (int)(fltY + fltVelY), (int)fltWidth - 4, (int)fltHeight);
    }
}
