package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.LinkedList;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class WaveAttacks extends GameObject {

    private ObjectHandler handler;
    private float fltStartAngle;
    int intSpread = 1;
    public WaveAttacks(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltStartAngle, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.handler = handler;
        System.out.println(Math.toDegrees(Math.tan(fltVelY/fltVelX)));
    }
    
    public void update(LinkedList<GameObject> objectList) {
        
        intSpread += 10;
        System.out.println(fltX);
        fltX+= fltVelX;
        fltY += fltVelY;
        
        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        //g.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
        //g.drawArc((int)(fltX - Math.abs(Math.cos(fltStartAngle))) , (int) (fltY + Math.abs(Math.sin(fltStartAngle))),intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        //g.drawArc((int)fltX, (int)fltY, (int) (10 + Math.abs(intSpread*Math.cos(fltStartAngle))), (int) (10 + Math.abs(intSpread*Math.sin(fltStartAngle))), (int)fltStartAngle + 45,  -90);
        g.fillArc((int)fltX, (int)fltY,intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
