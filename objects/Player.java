package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.InputHandler;
import framework.ObjectHandler;
import framework.ObjectId;

public class Player extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;

    public Player(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, InputHandler input) {
        super(fltX, fltY, fltWidth, fltHeight, id);
        this.handler = handler;
        this.input = input;
    }

    public void update(LinkedList<GameObject> objectList) {
        if(input.keySet.contains(InputHandler.InputKeys.W)) fltVelY -= fltAcc;
        else if(input.keySet.contains(InputHandler.InputKeys.S)) fltVelY += fltAcc;
        else if(input.keySet.contains(InputHandler.InputKeys.W) && input.keySet.contains(InputHandler.InputKeys.S)) {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        } else {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        }

        if(input.keySet.contains(InputHandler.InputKeys.A)) fltVelX -= fltAcc;
        else if(input.keySet.contains(InputHandler.InputKeys.D)) fltVelX += fltAcc;
        else if(input.keySet.contains(InputHandler.InputKeys.A) && input.keySet.contains(InputHandler.InputKeys.D)) {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        } else {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        }
        if(input.mouseSet.contains(InputHandler.InputMouse.BUTTON1)){
            float fltDiffX = InputHandler.fltclickx - (fltX + fltWidth/2);
            float fltDiffY = InputHandler.fltclicky - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
            fltDiffX /= fltLength;
            fltDiffY /= fltLength;
            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler));
        }
        
        if(input.mouseSet.contains(InputHandler.InputMouse.BUTTON3)){
            float fltDiffX = InputHandler.fltclickx - (fltX + fltWidth/2);
            float fltDiffY = InputHandler.fltclicky - (fltY + fltHeight/2);
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
}
