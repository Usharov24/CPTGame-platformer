package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class WaveAttacks extends GameObject {

    private float fltStartAngle;
    private float fltSpread = 1;

    public WaveAttacks(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltStartAngle, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.handler = handler;
    }
    
    public void update() {
        
        fltSpread += 15;
        fltWorldX+= fltVelX;
        fltWorldY += fltVelY;
                System.out.println("xplace  " + fltWorldX);
                System.out.println("yplace " + fltWorldY);

        if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0){
            handler.removeObject(this);
            System.out.println("out");
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        //g.drawArc((int)(fltWorldX - Math.abs(Math.cos(fltStartAngle))) , (int) (fltWorldY + Math.abs(Math.sin(fltStartAngle))),intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        //g.drawArc((int)fltWorldX, (int)fltWorldY, (int) (10 + Math.abs(intSpread*Math.cos(fltStartAngle))), (int) (10 + Math.abs(intSpread*Math.sin(fltStartAngle))), (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2), (int) (fltWorldY - fltSpread/2), (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2)-1, (int) (fltWorldY - fltSpread/2)-1, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2)-2, (int) (fltWorldY - fltSpread/2)-2, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2)-3, (int) (fltWorldY - fltSpread/2)-3, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2)-4, (int) (fltWorldY - fltSpread/2)-4, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }
}
