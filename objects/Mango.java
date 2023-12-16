package objects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import framework.ObjectHandler;
import framework.ObjectId;

public class Mango extends EnemyObject {

    private ObjectHandler handler;

    private float fltMinX = 0;
    private float fltMaxX = 0;
    private float fltMinY = 0;
    private float fltMaxY = 0;

    public Mango(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, fltRangeX, fltRangeY, fltHealth, fltDamage, id);
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
        fltX += fltVelX;
        fltY += fltVelY;

        if(fltX < fltMinX || fltX > fltMaxX || fltY < fltMinY || fltY > fltMaxY){
            fltVelX = -fltVelX;
            fltVelY = -fltVelY;
        }
    }

    public void draw(Graphics g){
        try{
            BufferedImage imgMango = ImageIO.read(new File("res/mango.png"));
            g.drawImage(imgMango, Math.round(fltX), Math.round(fltY), null);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

