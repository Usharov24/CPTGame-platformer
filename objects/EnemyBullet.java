
package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

import java.awt.Color;

public class EnemyBullet extends GameObject {

    /**
     * The explosion radius of the bullet
     */
    private float fltExplosionRadius;
    /**
     * Boolean indicating if the bullet is homing or not
     */
    private boolean blnHoming;
    /**
     * The amount of damage the bullet does
     */
    private float fltDmg;

    /**
     * The constructor for the EnemyBullet object
     * 
     * @param fltWorldX The world x position of the bullet
     * @param fltWorldY The world y position of the bullet
     * @param fltVelX The x velocity of the bullet
     * @param fltVelY The y velocity of the bullet
     * @param fltWidth The width of the bullet
     * @param fltHeight The height of the bullet
     * @param fltDmg The amount of damage the bullet does
     * @param id The ObjectId of the bullet
     * @param handler The ObjectHandler used for accessing other objects
     * @param ssm The SuperSocketMaster object used for network communication
     * @param biTexture The image texture of the object
     * @param blnHoming Boolean indicating if the bullet is homing
     * @param fltExplosionRadius The explosion radius of the bullet
     */
    public EnemyBullet(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltDmg, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biTexture, Boolean blnHoming, float fltExplosionRadius) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.blnHoming = blnHoming;
        this.fltDmg = fltDmg;
        this.fltExplosionRadius = fltExplosionRadius;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    /**
     * The update method of the bullet
     * It is responsible for updating its position and checking for collisions
     */
    public void update() {
        if(fltWorldX < 0 || fltWorldX > 1920 || fltWorldY < 0 || fltWorldY > 1440) handler.removeObject(this);

        if(blnHoming == false){
            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            collisions();
            //moves and checks forcollisions
        }   
        else{
            float fltTargetX = findNearestObject(fltWorldX, fltWorldY).getWorldX();
            float fltTargetY = findNearestObject(fltWorldX, fltWorldY).getWorldY();
            if(fltWorldX > fltTargetX){
                fltVelX -= 5; 
            }
            if(fltWorldX < fltTargetX){
                fltVelX += 5; 
            }
            if(fltWorldY > fltTargetY){
                fltVelY -= 5; 
            }
            if(fltWorldY < fltTargetY){
                fltVelY += 5; 
            }
            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            collisions();
            //homes in on the player and checks for collisions
        }
    }

    /**
     * The draw method of teh bullet
     * It is responsible for drawing the bullet's sprite
     * 
     * @param g The graphics context
     */
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
    //the drawing of the bullet

    /**
     * The bounds method of the bullet
     * 
     * @return Returns a rectangle representing the collisions box of the bullet
     */
    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
    //the hitbox of the bullet

    /**
     * Method used to find the nearest object to the bullet if the bullet is homing
     * 
     * @param fltWorldX The world x position of the bullet
     * @param fltWorldY The world y position of the bullet
     * @return Returns the nearest object to the bullet
     */
    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 9999999;
        //arbitary value to make sure the totaldist is less
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++) {
            if(handler.getObject(i).getId() == ObjectId.PLAYER) {
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist < fltpastTotal){
                    fltpastTotal = flttotaldist;
                    intreturn = i;
                    //finds the nearest object
                }
            }
        }
        return handler.getObject(intreturn);
    }

    /**
     * Method used to check the collisions of the bullet and react accordingly
     */
    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++){
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER){
                if(getBounds().intersects(object.getBounds())){
                    handler.removeObject(this);
                    if(fltExplosionRadius > 0){
                        handler.addObject(new EnemyExplosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, fltDmg, fltExplosionRadius*2, fltExplosionRadius*2,ObjectId.ENEMY_BOOM, handler, ssm));
                        ///if the bullet hits a barrier, remove it and then create an explosion if necessary
                    }        
                }
            }      
        }
    }

    /**
     * Getter used to get the amount of damage done by the buller
     * 
     * @return Returns the amount of damage done by the bullet
     */
    public float getDmg(){
        return fltDmg;
    }
}
