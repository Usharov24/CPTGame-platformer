package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class HomingBullet extends GameObject{

    private ObjectHandler handler;
    
    public HomingBullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, int intSender) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        
        if(Main.intSessionId == 1) ssm.sendText("h>aHOMING_BULLET~" + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + intSender);
        else ssm.sendText("c" + Main.intSessionId + ">aHOMING_BULLET~" + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + intSender);
    }
        
    public void update(LinkedList<GameObject> objectList) {
        float fltTargetX = findNearestObject(fltX, fltY).getX();
        float fltTargetY = findNearestObject(fltX, fltY).getY();
        if(fltX > fltTargetX){
            fltVelX -= 5; 
        }
         if(fltX < fltTargetX){
            fltVelX += 5; 
        }
         if(fltY > fltTargetY){
            fltVelY -= 5; 
        }
         if(fltY < fltTargetY){
            fltVelY += 5; 
        }
        fltX += fltVelX;
        fltY += fltVelY;

        
    }
   
    
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return null;
    }

    public GameObject findNearestObject(float fltX, float fltY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltPastX = 0;
        float fltPastY = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.sizeHandler(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                fltDistX = fltX - handler.getObject(i).getX();
                fltDistY = fltY - handler.getObject(i).getY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist > fltpastTotal){
                    fltpastTotal = flttotaldist;
                    fltPastX = fltDistX;
                    fltPastY = fltDistY;
                    intreturn = i;
                }
            }
        }
        return handler.getObject(intreturn);
    }
}
