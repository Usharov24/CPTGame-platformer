package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;

public class Enemy extends GameObject {

    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage[] biSmallEnem = resLoader.loadImages("/res\\SmallChase.png", "/res\\SmallShoot.png");
    private BufferedImage[] biMedEnem = resLoader.loadImages("/res\\MedChase.png", "/res\\MedShoot.png");
    private BufferedImage[] biBigEnem = resLoader.loadImages("/res\\BigChase.png", "/res\\BigShoot.png");
    private BufferedImage[] biBullets = resLoader.loadImages("/res\\SniperBullet.png");
    private BufferedImage biImg;

    private double dblTimer = System.currentTimeMillis();

    private float fltDmg;
    private float fltHP;
    private float fltTargetX;
    private float fltTargetY;
    private float fltBurnDmg = 0;

    private int intBleedCount = 0;
    private int intPosition;
    private int intEnemyType;
    private int intEnemyClass;

    private boolean blnFalling = true;
    private boolean blnLeft = true;

    

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
                fltHP = 100;
                fltDmg = 40;
                biImg = biSmallEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltWidth = 30;
                this.fltHeight = 60;
                fltHP = 250;
                fltDmg = 100;
                biImg = biSmallEnem[1];
            }
        }
        if(intEnemyType == 2){
            if(intEnemyClass == 1){
                this.fltWidth = 60;
                this.fltHeight = 60;
                fltHP = 450;
                fltDmg = 150;
                biImg = biMedEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltWidth = 40;
                this.fltHeight = 60;
                fltHP = 600;
                fltDmg = 100;
                biImg = biMedEnem[1];
            }
        }
        if(intEnemyType == 3){
            if(intEnemyClass == 1){
                this.fltHeight = 100;
                this.fltWidth = 100;
                fltHP = 600;
                fltDmg = 300;
                biImg = biBigEnem[0];
            }
            if(intEnemyClass == 2){
                this.fltHeight = 70;
                this.fltWidth = 50;
                fltHP = 600;
                fltDmg = 150;
                biImg = biBigEnem[1];
            }
        }

        camObject = handler.getObject(Main.intSessionId - 1);

        //defines the enemy stats
    }

    public void update() {
        if(Main.intSessionId != 1) return;
        if(fltHP <= 0) return;

        if(fltWorldX < 0 || fltWorldX > 1919 || fltWorldY < 0 || fltWorldY > 1439) fltHP = 0;

        fltTargetX = findNearestObject(fltWorldX, fltWorldY).getWorldX();
        fltTargetY = findNearestObject(fltWorldX, fltWorldY).getWorldY();

        //finds which player the enemy should target
        //small enemies
        if(intEnemyType == 1) {
            //crawlers
            if(intEnemyClass == 1) {
                if(fltTargetX > fltWorldX) {
                    fltVelX = 8;
                } else {
                    fltVelX = -8;
                }

                if(fltTargetY < fltWorldY && !blnFalling) {
                    fltVelY = -30;
                    blnFalling = true;
                }
                //makes the enemy follow the player
            }
            //shooters
            if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX;
                float fltDiffY = fltTargetY - fltWorldY;
                
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 1000) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20 , fltDiffY * 20 , 10, 10,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, false, 0));
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
                if (System.currentTimeMillis() - dblTimer > 1000) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20 , fltDiffY * 20 , 50, 50,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, false, 250));
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
                    fltWorldX +=10;
                }
                else{
                    fltVelX -= 10;
                }
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 1000) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 30 , fltDiffY * 30 , 10, 10,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, true, 100));
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
        
        fltVelY += 3;

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

        ssm.sendText("h>a>oENEMY~" + fltWorldX + "," + fltWorldY + "," + fltHP + "," + blnLeft + "," + intPosition);
    }

    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.PLAYER){
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist > fltpastTotal){
                    fltpastTotal = flttotaldist;
                    intreturn = i;
                }
            }
        }
        //finds nearest player object
        return handler.getObject(intreturn);
    }
    
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
                    blnFalling = false;
                }
                //if the enemy collides witha  barrier, stop it
            } else if(object.getId() == ObjectId.BULLET) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(object);
                    Bullet bullet = (Bullet) object;
                    fltHP -= bullet.getDmg();

                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        //handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, bullet.getDmg(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = bullet.getBleed();
                        intBleedCount = (int) bullet.getBleed();
                    }    
                    if(bullet.getCelebShot() > 0){
                        for(int intcount = 0; intcount < bullet.getCelebShot(); intcount++){
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, bullet.getVelX(), bullet.getVelY(), 6, 6, 0, 0, 0, 0, bullet.getCelebShot(), bullet.getDmg(), ObjectId.BULLET, handler, ssm, biBullets[0], false, bullet.getBoom(), bullet.getChar()));
                        }
                    }     
                }
                //determine what to do with a bullet upon collision depending on its parameters
            }
            else if(object.getId() == ObjectId.WAVE) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    WaveAttacks wave = (WaveAttacks) object;
                    fltHP -= wave.getDmg();

                    if(wave.getBoom() > 0){
                        float fltExplosionRadius = wave.getBoom();
                        //handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, wave.getDmg(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = wave.getBleed();
                        intBleedCount = (int) wave.getBleed();
                    }
                    
                    if(wave.getCelebShot() > 0) {
                        for(int intcount = 0; intcount < wave.getCelebShot(); intcount++){
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, wave.getVelX(), wave.getVelY(), 6, 6, 0, 0, 0, 0, wave.getCelebShot(), wave.getDmg(), ObjectId.BULLET, handler, ssm, biBullets[0], false, wave.getBoom(), wave.getChar()));                        
                        }
                    } 
                }
                //takes damage from waves
            }
            else if(object.getId() == ObjectId.SLASH) {
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
                }
                //takes damage from slashes
            }
            else if(object.getId() == ObjectId.BOOM) {
                if(getBounds().intersects(object.getBounds())){
                    Explosion boom = (Explosion) object;
                    fltHP -= boom.getDmg();
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

    public void setHP(float fltHP){
        this.fltHP = fltHP;
    }

    public float getHP(){
        return fltHP;
    }

    public float getDmg(){
        return fltDmg;
    }

    public float getVelX(){
        return this.fltVelX;
    }

    public float getVelY(){
        return this.fltVelX;
    }

    public void setVelX(float fltVelX){
        this.fltVelX = fltVelX;
    }

    public void setVelY(float fltVelY){
        this.fltVelY = fltVelY;
    }

    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }
    //all used from other classes.
}
