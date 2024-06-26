package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Framework.SuperSocketMaster;

import java.awt.Graphics2D;

public class Enemy extends GameObject {
    /**
    The resource laoder which loads

     **/
    private ResourceLoader resLoader = new ResourceLoader();
    /**
    The BufferedImage which stores the small enemies
     **/
    private BufferedImage[] biSmallEnem = resLoader.loadImages("/res/SmallChase.png", "/res/SmallShoot.png");
    /**
    The BufferedImage which stores the medium enemies
     **/
    private BufferedImage[] biMedEnem = resLoader.loadImages("/res/MedChase.png", "/res/MedShoot.png");
    /**
    The BufferedImage which stores the big enemies
     **/
    private BufferedImage[] biBigEnem = resLoader.loadImages("/res/BigChase.png", "/res/BigShoot.png");
    /**
    The BufferedImage which bullets
     **/
    private BufferedImage[] biBullets = resLoader.loadImages("/res/SniperBullet.png");
    /**
    The BufferedImage which stores the eenemy texture
     **/
    private BufferedImage biImg;
    /**
    The BufferedImage which stores the common textures
     **/
    private String[][] strEnemValues = resLoader.loadCSV("/res/EnemValues.csv");
    //holds the values for all the enemies

    /**
    The double which stores timer
     **/
    private double dblTimer = System.currentTimeMillis();
    /**
    The float which stores the dmg
     **/
    private float fltDmg;
    /**
    The float which stores the hp
     **/
    private float fltHP;
    /**
    The float which stores the targter x
     **/
    private float fltTargetX;
    /**
    The float which stores the targter y
     **/
    private float fltTargetY;
    /**
    The float which stroes burn dmg
     **/
    private float fltBurnDmg = 0;
    /**
    The long which hit timer
     **/
    private long lngHitTimer = 0;
    /**
    The int which stores the bleed
     **/
    private int intBleedCount = 0;
    /**
    The int which stores the position
     **/
    private int intPosition;
    /**
    The int which stores the enemy type
     **/
    private int intEnemyType;
    /**
    The int which stores the class
     **/
    private int intEnemyClass;
    /**
    The boolean which determines if falling
     **/
    private boolean blnFalling = false;
    /**
    The boolean which determines if left
     **/
    private boolean blnLeft = true;

    
    /** @param fltWorldX - float value for the X position of the knight
     * @param fltWorldY - float value for the Y position of the knight
     * @param fltVelX - float value for the velocity X
     * @param fltVelY - float value for the velocity Y
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param fltWidth - the width
     * @param fltHeight - the height
     * @param intposition - position in handler
     * @param intnenemyclass - the teir of enemy
     * @param intenemytype - the type of enemy
    */
    public Enemy(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, int intEnemyType, int intEnemyClass, int intPosition, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.intPosition = intPosition;
        this.intEnemyType = intEnemyType;
        this.intEnemyClass = intEnemyClass;
        //determine sprites off of intenemyclass and enemy type later on and enemy floor 18 total sprites
        //use the class to determine the size of the enemy
        if(intEnemyType == 1){
            if(intEnemyClass == 1){
                this.fltWidth = 60;
                this.fltHeight = 60;
                fltHP = Integer.parseInt(strEnemValues[1][1]);
                fltDmg = Integer.parseInt(strEnemValues[1][2]);
                biImg = biSmallEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltWidth = 30;
                this.fltHeight = 60;
                fltHP = Integer.parseInt(strEnemValues[2][1]);
                fltDmg = Integer.parseInt(strEnemValues[2][2]);
                biImg = biSmallEnem[1];
            }
        }
        if(intEnemyType == 2){
            if(intEnemyClass == 1){
                this.fltWidth = 60;
                this.fltHeight = 60;
                fltHP = Integer.parseInt(strEnemValues[3][1]);
                fltDmg = Integer.parseInt(strEnemValues[3][2]);
                biImg = biMedEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltWidth = 40;
                this.fltHeight = 60;
                fltHP = Integer.parseInt(strEnemValues[4][1]);
                fltDmg = Integer.parseInt(strEnemValues[4][2]);
                biImg = biMedEnem[1];
            }
        }
        if(intEnemyType == 3){
            if(intEnemyClass == 1){
                this.fltHeight = 100;
                this.fltWidth = 100;
                fltHP = Integer.parseInt(strEnemValues[5][1]);
                fltDmg = Integer.parseInt(strEnemValues[5][2]);
                biImg = biBigEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltHeight = 70;
                this.fltWidth = 50;
                fltHP = Integer.parseInt(strEnemValues[6][1]);
                fltDmg = Integer.parseInt(strEnemValues[6][2]);
                biImg = biBigEnem[1];
            }
        }
        //uses the CSV from earlier to load stats into the enemy
        camObject = handler.getObject(Main.intSessionId - 1);

        //defines the enemy stats
    }
    public void update() {
        if(Main.intSessionId != 1) return;
        if(fltHP <= 0) return;

        if(fltWorldX < 0 || fltWorldX > 1919 || fltWorldY < 0 || fltWorldY > 1439) fltHP = 0;

        fltTargetX = findNearestObject().getWorldX();
        fltTargetY = findNearestObject().getWorldY();

        //finds which player the enemy should target
        //small enemies
        if(intEnemyType == 1) {
            //crawlers
            if(intEnemyClass == 1) {
                if(fltTargetX > fltWorldX) {
                    fltVelX += 2;
                } else {
                    fltVelX += -2;
                }
                if(fltVelX > 8) fltVelX = 8;
                else if(fltVelX < -8) fltVelX = -8; 

                if(fltTargetY < fltWorldY && !blnFalling) {
                    fltVelY = -40;
                    blnFalling = true;
                }

                //makes the enemy follow the player
            } else if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX;
                float fltDiffY = fltTargetY - fltWorldY;
                
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 1500) {
                    dblTimer = System.currentTimeMillis();
                    if(ssm != null) ssm.sendText("h>a>aENEMY_BULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10 + "," + fltDmg + "," + false + "," + 0);
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20 , fltDiffY * 20 , 10, 10,  fltDmg*5, ObjectId.ENEMY_BULLET, handler, ssm, null, false, 0));
                }
            }
        }
        //medium enemies
        if(intEnemyType == 2){
            //homes in on player
            if(intEnemyClass == 1){
                if(fltWorldX > fltTargetX){
                    fltVelX -= 1; 
                }
                if(fltWorldX < fltTargetX){
                    fltVelX += 1; 
                }
                if(fltWorldY > fltTargetY){
                    fltVelY -= 1; 
                }
                if(fltWorldY < fltTargetY){
                    fltVelY += 1; 
                }
                //homes slowly onto the player
            }
            //shooters
            if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX - fltWidth/2;
                float fltDiffY = fltTargetY - fltWorldY - fltHeight/2;
                
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 2000) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 15, fltWorldY + fltHeight/2 - 15, fltDiffX * 20 , fltDiffY * 20 , 30, 30,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, false, 150));
                    if(ssm != null) ssm.sendText("h>a>aENEMY_BULLET~" + (fltWorldX + fltWidth/2 - 15) + "," + (fltWorldY + fltHeight/2 - 15) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 30 + "," + 30 + "," + fltDmg + "," + false + "," + 150);
                }
                //shoots at the player
            }   
        }
        //large enemies
        if(intEnemyType == 3){
            //big crawler
            if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX - fltWidth/2;
                float fltDiffY = fltTargetY - fltWorldY - fltHeight/2;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                if(fltTargetX > fltWorldX){
                    fltVelX = 8;
                } else {
                    fltVelX = -8;
                }
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 500) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 30 , fltDiffY * 30 , 10, 10,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, true, 50));
                    if(ssm != null) ssm.sendText("h>a>aENEMY_BULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 30) + "," + (fltDiffY * 30) + "," + 10 + "," + 10 + "," + fltDmg + "," + true + "," + 50);
                }
                //cooldown on shooting at the player
            }
            //big homer
            if(intEnemyClass == 1){
                if(fltWorldX > fltTargetX){
                    fltVelX -= 3; 
                }
                if(fltWorldX < fltTargetX){
                    fltVelX += 3;
                }
                if(fltWorldY > fltTargetY){
                    fltVelY -= 3; 
                }
                if(fltWorldY < fltTargetY){
                    fltVelY += 3; 
                }

                //homes the enemy on a player
            }
        }
        if(fltVelY > 35) fltVelY = 35;
        else if(fltVelY < -35) fltVelY = -35; 

        if(fltVelX > 35) fltVelX = 35;
        else if(fltVelX < -35) fltVelX = -35; 
        //caps out the velocities
        
        if(!(intEnemyType == 2 && intEnemyClass == 1) && !(intEnemyType == 3 && intEnemyClass == 1)) fltVelY += 3;

        //gravity!
        if(fltVelX < 0) blnLeft = true;
        else if(fltVelX > 0) blnLeft = false;
        
        collisions();
        //checks what the enemy collides with

        fltWorldX += fltVelX;
        fltWorldY += fltVelY;

        if(intBleedCount >= 5){
            fltHP *= 0.6;
        }
        if(fltBurnDmg > 0){
            fltHP -= fltBurnDmg;
        }
        //damage interaction

        if(ssm != null) ssm.sendText("h>a>oENEMY~" + fltWorldX + "," + fltWorldY + "," + fltHP + "," + blnLeft + "," + intPosition);
    }
    /**
     * @return gameobject 
     * finds the nearest player
     */

    public GameObject findNearestObject() {
        float fltDistX;     
        float fltDistY;
        float flttotaldist;
        float fltpastTotal = 1000000000;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.PLAYER){
                fltDistX = handler.getObject(i).getWorldX() - this.fltWorldX;
                fltDistY = handler.getObject(i).getWorldY() - this.fltWorldY;
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist < fltpastTotal){
                    fltpastTotal = flttotaldist;
                    intreturn = i;
                }
            }
        }
        //finds nearest player object
        return handler.getObject(intreturn);
    }
    /**
     * @return void 
     * checks collisions
     */
    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                }else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() - fltHeight;
                    blnFalling = false;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
                //if the enemy collides witha  barrier, stop it
            } else if(object.getId() == ObjectId.BULLET && System.currentTimeMillis() - lngHitTimer > 100) {
                //lngtimer is the immunity frames for the enemies
                if(getBounds().intersects(object.getBounds())){
                    Bullet bullet = (Bullet) object;
                    if(bullet.getPeirce() == 0){
                        handler.removeObject(object);
                    }
                    else{
                        bullet.setPeirce(bullet.getPeirce()-1);
                        //removes the bullet if the bullet is out of peirce, lowers peirce by one
                    }
                    fltHP -= bullet.getDmg();
                    //takes dmg from the bullet
                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, bullet.getDmg(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = bullet.getBurn();
                        intBleedCount = (int) bullet.getBleed();
                    }    
                    if(bullet.getCelebShot() > 0){
                        for(int intcount = 0; intcount < bullet.getCelebShot()*3; intcount++){
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, bullet.getVelX(), bullet.getVelY(), 6, 6, 0, 0, 0, 0, bullet.getCelebShot(), bullet.getDmg(), ObjectId.BULLET, handler, ssm, biBullets[0], false, bullet.getBoom(), bullet.getChar()));
                        }
                    }     
                    lngHitTimer = System.currentTimeMillis();
                }
                //determine what to do with a bullet upon collision depending on its parameters
            }
            else if(object.getId() == ObjectId.WAVE && System.currentTimeMillis() - lngHitTimer > 100) {
                if(getBounds().intersects(object.getBounds())){
                    WaveAttacks wave = (WaveAttacks) object;
                    fltHP -= wave.getDmg();

                    if(wave.getBoom() > 0){
                        float fltExplosionRadius = wave.getBoom();
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, wave.getDmg(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = wave.getBleed();
                        intBleedCount = (int) wave.getBleed();
                        //gets the needed parameters
                    }
                    
                    if(wave.getCelebShot() > 0) {
                        for(int intcount = 0; intcount < wave.getCelebShot(); intcount++){
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, wave.getVelX(), wave.getVelY(), 6, 6, 0, 0, 0, 0, wave.getCelebShot(), wave.getDmg(), ObjectId.BULLET, handler, ssm, biBullets[0], false, wave.getBoom(), wave.getChar()));                        
                        }
                    } 
                    lngHitTimer = System.currentTimeMillis();
                }
                //takes damage from waves
            }
            else if(object.getId() == ObjectId.SLASH && System.currentTimeMillis() - lngHitTimer > 100) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    SlashAttacks slash = (SlashAttacks) object;
                    fltHP -= slash.getDmg();

                    if(slash.getBoom()> 0){
                        float fltExplosionRadius = slash.getBoom();
                        //handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, slash.getDmg(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = slash.getBleed();
                        intBleedCount = (int) slash.getBleed();
                    } 
                    if(slash.getCelebShot() > 0){
                        for(int intcount = 0; intcount < slash.getCelebShot(); intcount++){
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, slash.getVelX(), slash.getVelY(), 6, 6, 0, 0, 0, 0, slash.getCelebShot(), slash.getDmg(), ObjectId.BULLET, handler, ssm, biBullets[0], false, slash.getBoom(), slash.getChar()));
                        }
                    }      
                    lngHitTimer = System.currentTimeMillis();
                }
                //takes damage from slashes
            }
            else if(object.getId() == ObjectId.BOOM && System.currentTimeMillis() - lngHitTimer > 100) {
                if(getBounds().intersects(object.getBounds())){
                    Explosion boom = (Explosion) object;
                    fltHP -= boom.getDmg();
                    lngHitTimer = System.currentTimeMillis();
                }
            }
            //hurt by booms
        }
    }

    public void draw(Graphics g) {
        if(fltHP <= 0) return;

        Graphics2D g2d = (Graphics2D)g;

        if(blnLeft){
            g2d.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2 + fltWidth),(int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -(int)fltWidth, (int)fltHeight, null);
        }
        else{
            g2d.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        }
    }
    //draws the sprite

    public Rectangle getBounds() {
        if(fltHP > 0) {
            float fltBoundsX = fltWorldX + fltVelX - camObject.getWorldX() - camObject.getWidth()/2;

            if(fltBoundsX > fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2;
            else if(fltBoundsX < fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2;

            return new Rectangle((int)fltBoundsX, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2) + 4, (int)fltWidth, (int)fltHeight - 8);
        } else {
            return new Rectangle();
        }
    }
    //x bounds
    /**
     * @return rectangle 
     * the bounds of the enemy
     * */
    public Rectangle getBounds2() {
        if(fltHP > 0) {
            float fltBoundsY = fltWorldY + fltVelY - camObject.getWorldY() - camObject.getHeight()/2;

            if(fltBoundsY > fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2;
            else if(fltBoundsY < fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2;

            return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
        } else {
            return new Rectangle();
        }
    }
    //y bounds
    /**
     * @return void  sets hp
     */
    public void setHP(float fltHP){
        this.fltHP = fltHP;
    }
    /**
     * @return float  gets hp
     */
    public float getHP(){
        return fltHP;
    }
    /**
     * @return float  gets dmg
     */
    public float getDmg(){
        return fltDmg;
    }
    /**
     * @return float  gets velx
     */
    public float getVelX(){
        return this.fltVelX;
    }
    /**
     * @return float  gets vely
     */
    public float getVelY(){
        return this.fltVelX;
    }
    /**
     * @return void sets velx
     */
    public void setVelX(float fltVelX){
        this.fltVelX = fltVelX;
    }
    /**
     * @return void sets vely
     */
    public void setVelY(float fltVelY){
        this.fltVelY = fltVelY;
    }
    /**
     * @return void sets blnleft
     */
    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }
    //all used from other classes and mostly networking to ensure enemies are consistent
}
