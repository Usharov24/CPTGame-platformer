package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class Apple extends EnemyObject {

    private ObjectHandler handler;

    private float fltMinX = 0;
    private float fltMaxX = 0;
    private float fltMinY = 0;
    private float fltMaxY = 0;

    public Apple(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltVelX, fltVelY, fltWidth, fltHeight, fltRangeX, fltRangeY, fltHealth, fltDamage, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltRangeX = fltRangeX;
        this.fltRangeY = fltRangeY;
        this.handler = handler;

        this.fltMinX = fltX;
        this.fltMaxX = fltX + fltRangeX;
        this.fltMinY = fltY;
        this.fltMaxY = fltY + fltRangeY;

    }

    public void update(LinkedList<GameObject> objectList) {

        fltVelX = (float)(5*Math.sin(System.currentTimeMillis()/1000.0-framework.Main.startTime/1000.0));
        fltVelY = (float)(5*Math.cos(System.currentTimeMillis()/1000.0-framework.Main.startTime/1000.0));

        fltX += fltVelX;
        fltY += fltVelY;

        if(fltX < fltMinX || fltX > fltMaxX || fltY < fltMinY || fltY > fltMaxY){
            fltVelX = -fltVelX;
            fltVelY = -fltVelY;
        }
    }

    public void draw(Graphics g){
        try{
            BufferedImage imgApple = ImageIO.read(new File("res/apple.png"));
            g.drawImage(imgApple, Math.round(fltX), Math.round(fltY), null);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Rectangle getBounds() {
        return null;
    }
}

