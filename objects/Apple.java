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
    private long startTime = System.nanoTime();

    public Apple(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltWorldX, fltWorldY, fltVelX, fltVelY, fltWidth, fltHeight, fltRangeX, fltRangeY, fltHealth, fltDamage, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltRangeX = fltRangeX;
        this.fltRangeY = fltRangeY;
        this.handler = handler;

        this.fltMinX = fltWorldX;
        this.fltMaxX = fltWorldX + fltRangeX;
        this.fltMinY = fltWorldY;
        this.fltMaxY = fltWorldY + fltRangeY;

    }

    public void update(LinkedList<GameObject> objectList) {

        fltVelX = (float)(5*Math.sin(System.nanoTime()/1000000000.0-(float)framework.Main.startTime/1000000000.0));
        fltVelY = (float)(-5*Math.cos(System.nanoTime()/1000000000.0-(float)framework.Main.startTime/1000000000.0));

        fltWorldX += fltVelX;
        fltWorldY += fltVelY;

        if(fltWorldX < fltMinX || fltWorldX > fltMaxX || fltWorldY < fltMinY || fltWorldY > fltMaxY){
            fltVelX = -fltVelX;
            fltVelY = -fltVelY;
        }
    }

    public void draw(Graphics g){
        try{
            BufferedImage imgApple = ImageIO.read(new File("res/apple.png"));
            g.drawImage(imgApple, Math.round(fltWorldX), Math.round(fltWorldY), null);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltHeight, (int)fltWidth);
    }
}

