package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class KnightSlashes extends GameObject {

    private ObjectHandler handler;
    private float fltStartAngle;
    private long lngbirth;
    public KnightSlashes(float fltWorldX, float fltWorldY, float fltVelX, long lngbirth, float fltWidth, float fltHeight, float fltStartAngle, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.handler = handler;
        this.lngbirth = lngbirth;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        
        fltWorldX+= fltVelX;
        fltWorldY += fltVelY;
        
        if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0 || System.currentTimeMillis() - lngbirth > 100){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        //g.drawArc((int)(fltWorldX - Math.abs(Math.cos(fltStartAngle))) , (int) (fltWorldY + Math.abs(Math.sin(fltStartAngle))),intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        //g.drawArc((int)fltWorldX, (int)fltWorldY, (int) (10 + Math.abs(intSpread*Math.cos(fltStartAngle))), (int) (10 + Math.abs(intSpread*Math.sin(fltStartAngle))), (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX - fltWidth/2), (int) (fltWorldY - fltHeight /2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX - fltWidth/2)-1, (int) (fltWorldY - fltHeight /2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX - fltWidth/2)-2, (int) (fltWorldY - fltHeight /2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX - fltWidth/2)+1, (int) (fltWorldY - fltHeight /2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX - fltWidth/2)+2, (int) (fltWorldY - fltHeight /2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45,  -90);
        
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }
}
