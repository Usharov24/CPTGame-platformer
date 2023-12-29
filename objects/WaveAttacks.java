package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class WaveAttacks extends GameObject {

    private ObjectHandler handler;
    private float fltStartAngle;
    float fltSpread = 1;
    public WaveAttacks(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltStartAngle, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.handler = handler;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        
        fltSpread += 15;
        fltX+= fltVelX;
        fltY += fltVelY;
        
        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        //g.drawArc((int)(fltX - Math.abs(Math.cos(fltStartAngle))) , (int) (fltY + Math.abs(Math.sin(fltStartAngle))),intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        //g.drawArc((int)fltX, (int)fltY, (int) (10 + Math.abs(intSpread*Math.cos(fltStartAngle))), (int) (10 + Math.abs(intSpread*Math.sin(fltStartAngle))), (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltX-fltSpread/2), (int) (fltY - fltSpread/2), (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltX-fltSpread/2)-1, (int) (fltY - fltSpread/2)-1, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltX-fltSpread/2)-2, (int) (fltY - fltSpread/2)-2, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltX-fltSpread/2)-3, (int) (fltY - fltSpread/2)-3, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltX-fltSpread/2)-4, (int) (fltY - fltSpread/2)-4, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
