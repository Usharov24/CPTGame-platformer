package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class WizardUlt extends GameObject{
    private BufferedImage img = null;
    private ObjectHandler handler;
    
    public WizardUlt(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        try{
            img = ImageIO.read(new File("res/ElectricBall.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
    }
        
    public void update(LinkedList<GameObject> objectList) {
        float fltTargetX = findNearestObject(fltX, fltY).getX();
        float fltTargetY = findNearestObject(fltX, fltY).getY();
        if(fltX > fltTargetX){
            fltVelX -= 10; 
        }
         if(fltX < fltTargetX){
            fltVelX += 10; 
        }
         if(fltY > fltTargetY){
            fltVelY -= 10; 
        }
         if(fltY < fltTargetY){
            fltVelY += 10; 
        }
        fltX += fltVelX;
        fltY += fltVelY;

        
    }
   
    
    public void draw(Graphics g) {
        g.setColor(Color.white);
        //g.fillOval((int)fltX - 15, (int)fltY - 15, (int)fltWidth, (int)fltHeight);
        g.drawImage(img, (int)fltX - 15, (int)fltY - 15, null);
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
