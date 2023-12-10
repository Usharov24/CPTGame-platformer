import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;


public class Bullet extends GameObject{
    
    public float fltdestx;
    public float fltdesty;
    public float fltlengthx;
    public float fltlengthy;
    public float fltratio;
    
    //finds the value that allows for the bullet to always move at 20pixels/s

    public Bullet(float fltX, float fltY, float fltWidth, float fltHeight, float fltdestx,  float fltdesty) {
        super(fltX, fltY, fltWidth, fltHeight);
        this.fltdestx = fltdestx;
        this.fltdesty = fltdesty;
        this.fltlengthx = fltdestx - fltX;
        this.fltlengthy = fltdesty - fltY;
        this.fltratio =  (fltlengthx*fltlengthx + fltlengthy*fltlengthy)/100;
    }
    int intcount = 1;
    
    
    
    
    

    public void update(LinkedList<GameObject> objectList) {
        //max velocity for a bullet will be 10 pixels for now
        //fltVelX^2 + fltVelY^2 = 10^2
        
        fltVelX = (float) Math.sqrt((fltlengthx*fltlengthx)/fltratio);
        fltVelY = (float) Math.sqrt((fltlengthy*fltlengthy)/fltratio);
        if(fltlengthx < 0){
            fltVelX = fltVelX*-1;
        }
        if(fltlengthy < 0){
            fltVelY = fltVelY*-1;
        }
        /*if(fltlengthx < 0){
            fltVelX = fltVelX*-1;
        }
        if(fltlengthy < 0){
            fltVelY = fltVelY*-1;
        }
        */
        //multiples the value by the ratio to ensure the 
        this.fltX += this.fltVelX;
        this.fltY += this.fltVelY;
        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            Main.handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)fltX, (int)fltY, 10, 10);
    }
    
}
