package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class VacGrenade extends GameObject {

    private ObjectHandler handler;
    private BufferedImage biImg;
    private boolean blnFalling = true;
    private boolean blnSucking = false;
    private float fltexplosionradius;
    private float fltpastVelX;
    private long lngBirth;
    public VacGrenade(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, long lngBirth, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, BufferedImage biImg, float fltexplosionradius) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        this.biImg = biImg;
        this.fltexplosionradius = fltexplosionradius;
        this.lngBirth = lngBirth;
        fltpastVelX = fltVelX;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        if(blnSucking == false){
                collisions(); 
                if(blnFalling){
                    fltVelY+= 2;
                    
                }
                else{
                    if(fltVelX > 0)fltVelX -= fltpastVelX/8;
                    if(fltVelX < 0)fltVelX += fltpastVelX/-8;
                }
                fltX += fltVelX;
                fltY += fltVelY;
                System.out.println(fltVelX);
                
            if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
                handler.removeObject(this);
            }
            if(fltVelX == 0 && fltVelY == 0){
                blnSucking = true;
            }
        }
        else if(blnSucking){
            Suck(FindNear());
            if(System.currentTimeMillis() - lngBirth > 10000){
                handler.removeObject(this);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)(fltX - fltWidth/2),(int)(fltY - fltHeight/2), null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltX - fltWidth/2), (int)(fltY - fltHeight/2), (int)fltWidth, (int)fltHeight);
    }

    private ArrayList<Integer> FindNear(){
        float fltDistX = 0;
        float fltDistY = 0;
        float fltTotalDist = 0;
        ArrayList<Integer> arylist = new ArrayList<Integer>();
        for(int i = 0; i < handler.sizeHandler(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                fltDistX = fltX - handler.getObject(i).getX();
                fltDistY = fltY - handler.getObject(i).getY();
                fltTotalDist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(fltTotalDist < 400){
                    arylist.add(i);
                }
                
            }
        }
        return arylist;
    }

    private void Suck(ArrayList<Integer> arylist){
        float fltTargetX;
        float fltTargetY;
        for(int i = 0; arylist.size() > i; i++){
            fltTargetX = handler.getObject(i).getX();
            fltTargetY = handler.getObject(i).getY();

            if(fltX > fltTargetX){
                handler.getObject(i).setX(fltTargetX + 5);
            }
            if(fltX < fltTargetX){
                handler.getObject(i).setX(fltTargetX - 5);
            }
            if(fltY > fltTargetY){
                handler.getObject(i).setY(fltTargetY + 5);
            }
            if(fltY < fltTargetY){
                handler.getObject(i).setY(fltTargetY - 5);
            }
        }


    }
    
    private void collisions() {
        if(getBounds().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            fltY = (float)new Rectangle(0, 660, 1280, 10).getY();
        }
        else{
            blnFalling  = true;
        } 
        for(int i = 0; i < handler.sizeHandler(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(fltexplosionradius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltX - fltexplosionradius/2, fltY - fltexplosionradius/2,fltexplosionradius*2,fltexplosionradius*2,ObjectId.BOOM,ssm,handler));
                        //arbitary value to make sure bomb doesnt explode multiple times
                        fltX = 10000;

                    }        
                }
            }        
        }
    }
}
