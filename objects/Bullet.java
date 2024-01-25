package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class Bullet extends GameObject {

    /**
     * The image texture of the Bullet object
     */
    private BufferedImage biTexture;

    /**
     * The explosion radius the bullet will cause on impact
     */
    private float fltExplosionRadius;
    /**
     * Boolean to determine if the bullet is homing or not
     */
    private boolean blnHoming;
    /**
     * The amount of damage the bullet does
     */
    private float fltDmg;
    /**
     * The number of bleed effects the bullet applies
     */
    private int intBleedCount;
    /**
     * The amount of burn damage the buller applies
     */
    private float fltBurnDmg;
    /**
     * The number of times the buller can peirce before being removed
     */
    private int intPeirceCount;
    /**
     * The amount of healing recieved after the bullet hits
     */
    private float fltLifeSteal;
    /**
     * The position of the sender of the bullet in the object list
     */
    private int intSender;
    /**
     * The number of bullets that will spawn upon the original hitting a target
     */
    private int intCelebShot;

    /**
     * 
     * @param fltWorldX The world x position of the object
     * @param fltWorldY The world y position of the object
     * @param fltVelX The x velocity of the object
     * @param fltVelY The y velocity of the object
     * @param fltWidth The width of the object
     * @param fltHeight The height of the object
     * @param intPeirceCount The number of times the bullet can peirce
     * @param intBleedCount The amount of bleed effects the buller applies
     * @param fltBurnDmg The burn damage the bullet applies
     * @param fltLifeSteal The amount of health recovered from hitting with the bullet
     * @param intCelebShot The number of bullets spawned upon hitting a target
     * @param fltDmg The amount of damage done by the bullet
     * @param id The ObjectId of the object
     * @param handler The ObjectHandler used to access other objects
     * @param ssm The SuperSocketMaster object used for network communication
     * @param biTexture The image texture of the object
     * @param blnHoming Boolean determining whether the bullet is homing
     * @param fltExplosionRadius The radius of the explosion caused by the bullet
     * @param intSender The position of the sender of the bullet in the object list
     */
    public Bullet(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, int intPeirceCount, int intBleedCount, float fltBurnDmg, float fltLifeSteal, int intCelebShot, float fltDmg, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biTexture, Boolean blnHoming, float fltExplosionRadius, int intSender) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biTexture = biTexture;
        this.blnHoming = blnHoming;
        this.fltExplosionRadius = fltExplosionRadius;
        this.fltDmg = fltDmg;
        this.intBleedCount = intBleedCount;
        this.fltBurnDmg = fltBurnDmg;
        this.intPeirceCount = intPeirceCount;
        this.intSender = intSender;
        this.intCelebShot = intCelebShot;
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    /**
     * The update method of the bullet object
     * This method handles updating the bullets position and determining collisions
     */
    public void update() {
        if(fltWorldX < 0 || fltWorldX > 1920 || fltWorldY < 0 || fltWorldY > 1440) handler.removeObject(this);

        if(blnHoming == false) {
            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
        } else {
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

            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
        }
    }

    /**
     * The draw method of the bullet
     * This method is responsible for drawing the bullet's sprite
     */
    public void draw(Graphics g) {
        g.drawImage(biTexture, (int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    /**
     * The bounds method for the bullet
     * 
     * @return This method returns a rectangle representing the collision box of the bullet
     */
    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    /**
     * This method is used by the bullet to find the nearest object to it if it is a homing bullet
     * 
     * @param fltWorldX The world x position of the bullet
     * @param fltWorldY The world y position of the bullet
     */
    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 9000000;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++) {
            if(handler.getObject(i).getId() == ObjectId.ENEMY) {
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist < fltpastTotal){
                    fltpastTotal = flttotaldist;
                    intreturn = i;
                }
            }
        }
        return handler.getObject(intreturn);
    }

    /**
     * The collisions method for the bullet
     * This method is used to determine if the bullet collides with other objects
     */
    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++){
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(fltExplosionRadius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, fltDmg, fltExplosionRadius*2, fltExplosionRadius*2,ObjectId.BOOM, handler, ssm));
                    }        
                }
            }        
        }
    }

    /**
     * Getter for the number of times a bullet can peirce
     * 
     * @return Returns the number of times a bullet can peirce
     */
    public int getPeirce(){
        return intPeirceCount;
    }

    /**
     * Setter method for the number of times a bullet can peirce
     * 
     * @param intPeirceCount The new value to set peice count to
     */
    public void setPeirce(int intPeirceCount){
        this.intPeirceCount = intPeirceCount;
    }
    //the two functions are used to determine when a bullet should despawn

    /**
     * Getter for the explosion radius caused by a bullet
     * 
     * @return Returns the explosion radius
     */
    public int getBoom(){
        return (int)fltExplosionRadius;
    }
    //used to make sure enemies explode when needed
    /**
     * Getter for the amount of damage caused by the bullet
     * 
     * @return Returns the bullet's damage value
     */
    public float getDmg(){
        return fltDmg;
    }
    //afflicts dmg to enemies
    /**
     * Getter for the amount of burn damage applied by the bullet
     * 
     * @return Returns teh amount of burn damage
     */
    public float getBurn(){
        return fltBurnDmg;
    }
    //burn dmg which is a type of dmg that ticks every frame

    /**
     * Getter for the amount of bleed caused by the bullet
     * 
     * @return Returns the amount of bleed caused by the bullet
     */
    public float getBleed(){
        return intBleedCount;
    }
    //bleed dmg which does a burst of dmg after 5 strikes
    /**
     * Getter for the position of the sender in the object list
     * 
     * @return Returns the position of the sender in the object list
     */
    public int getChar(){
        return this.intSender;
    }
    //used for lifesteal
    /**
     * Getter for the amount of health gained on hit with the bullet
     * 
     * @return Returns the amount of health gained on hit with the bullet
     */
    public float getLifeSteal(){
        return this.fltLifeSteal;
    }
    //used for lifesteal as well\
    /**
     * Getter for the number of bullets spawned on hit with the bullet
     * 
     * @return Returns the amount of bullets spawned
     */
    public int getCelebShot(){
        return this.intCelebShot;
    }
    //used to spot in bullets from the item
    /**
     * Getter for the x velocity of the bullet
     * 
     * @return Returns the x velocity of the bullet
     */
    public float getVelX(){
        return this.fltVelX;
    }
    //returns x velocity
    /**
     * Getter for the y velocity of the bullet
     * 
     * @return Returns the y velocity of the bullet
     */
    public float getVelY(){
        return 0;
    }
    //returns y velocity
}
