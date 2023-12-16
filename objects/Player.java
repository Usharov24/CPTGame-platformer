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
