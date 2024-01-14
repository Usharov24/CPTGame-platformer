package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class Mango extends EnemyObject {

    private float fltMinX = 0;
    private float fltMaxX = 0;
    private float fltMinY = 0;
    private float fltMaxY = 0;

    public Mango(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltVelX, fltVelY, fltWidth, fltHeight, fltRangeX, fltRangeY, fltHealth, fltDamage, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltRangeX = fltRangeX;
        this.fltRangeY = fltRangeY;

        this.fltMinX = fltWorldX;
        this.fltMaxX = fltWorldX + fltRangeX;
        this.fltMinY = fltWorldY;
        this.fltMaxY = fltWorldY + fltRangeY;
    }

    public void update() {
        
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;

        if(fltWorldX < fltMinX || fltWorldX > fltMaxX || fltWorldY < fltMinY || fltWorldY > fltMaxY){
            fltVelX = -fltVelX;
            fltVelY = -fltVelY;
        }
    }

    public GameObject findnear(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltPastX = 0;
        float fltPastY = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++){
            
            fltDistX = fltWorldX - handler.objectList.get(i).getWorldX();
            fltDistY = fltWorldY - handler.objectList.get(i).getWorldY();
            flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
            if(flttotaldist > fltpastTotal){
                fltpastTotal = flttotaldist;
                fltPastX = fltDistX;
                fltPastY = fltDistY;
                intreturn = i;
            }

           
        }
        return handler.objectList.get(intreturn);
    }

    public void draw(Graphics g){
        try{
            BufferedImage imgMango = ImageIO.read(new File("res/mango.png"));
            g.drawImage(imgMango, Math.round(fltWorldX), Math.round(fltWorldY), null);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltHeight, (int)fltWidth);
    }
}

