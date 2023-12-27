package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class SniperBullet extends GameObject{
    private BufferedImage img = null;
    private ObjectHandler handler;
    
    public SniperBullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        try{
            img = ImageIO.read(new File("res/SniperBullet.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
        
    }
    
    public void update(LinkedList<GameObject> objectList) {
    
        fltX += fltVelX;
        fltY += fltVelY;

        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        //g.fillOval((int)fltX - 15, (int)fltY - 15, (int)fltWidth, (int)fltHeight);
        g.drawImage(img, (int)fltX -5, (int)fltY - 5, null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
