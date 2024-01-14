package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class KnightSlashes extends GameObject {

    private float fltStartAngle;
    private long lngbirth;
    
    public KnightSlashes(float fltWorldX, float fltWorldY, float fltVelX, long lngbirth, float fltWidth, float fltHeight, float fltStartAngle, float fltDmg, int intBoomRad, float fltBurnDmg, int intBleedCount, float fltLifeSteal, int intCelebShot, int intPeirceCount, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.lngbirth = lngbirth;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
        
        if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0 || System.currentTimeMillis() - lngbirth > 100){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);

        for(int intCount = -2; intCount < 3; intCount++) {
            g.drawArc((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) + intCount, (int) (fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45, -90);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
}
