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
    private int intEnemyType;
    private int intEnemyClass;
    private float fltDmg;
    private float fltHP;
    private float fltTargetX;
    private float fltTargetY;
    private double dblTimer = System.currentTimeMillis();
    private int intJumpCap = 1;
    private int intBleedCount = 0;
    private float fltBurnDmg = 0;
    private boolean blnFalling = true;
    private ResourceLoader resLoader = new ResourceLoader();
    private BufferedImage[] biSmallEnem = resLoader.loadImages("/res\\SmallChase.png", "/res\\SmallShoot.png");
    private BufferedImage[] biMedEnem = resLoader.loadImages("/res\\MedChase.png", "/res\\MedShoot.png");
    private BufferedImage[] biBigEnem = resLoader.loadImages("/res\\BigChase.png", "/res\\BigShoot.png");
    private BufferedImage[] biBullets = resLoader.loadImages("/res\\SniperBullet.png");
    private BufferedImage biImg = null;

    

    public Enemy(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltHealth, int intEnemyType, int intEnemyFloor, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX; 
        this.fltVelY = fltVelY;
        this.intEnemyClass = (int)Math.floor(Math.random() * 2 + 1);
        this.intEnemyType = intEnemyType;
        this.fltDmg *= intEnemyFloor;
        //determine sprites off of intenemyclass and enemy type later on and enemy floor 18 total sprites
        //use the class to determine the size of the enemy
        if(intEnemyType == 1){
            if(intEnemyClass == 1){
                fltWidth = 60;
                fltHP = 100;
                fltHeight = 60;
                fltDmg = 40;
                biImg = biSmallEnem[0];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }

            }
            if(intEnemyClass == 2){
                fltWidth = 30;
                fltHP = 250;
                fltHeight = 60;
                fltDmg = 100;
                biImg = biSmallEnem[1];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }
            }
        }
        if(intEnemyType == 2){
            if(intEnemyClass == 1){
                fltWidth = 60;
                fltHeight = 60;
                fltDmg = 150;
                fltHP = 450;
                biImg = biMedEnem[0];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }
            }
            if(intEnemyClass == 2){
                fltWidth = 40;
                fltHeight = 60;
                fltDmg = 100;
                fltHP = 600;
                biImg = biMedEnem[1];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }
            }
        }
        if(intEnemyType == 3){
            if(intEnemyClass == 1){
                fltHeight = 100;
                fltWidth = 100;
                fltDmg = 300;
                fltHP = 600;

                biImg = biBigEnem[0];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }
            }
            if(intEnemyClass == 2){
                fltHeight = 70;
                fltWidth = 50;
                fltDmg = 150;
                fltHP = 600;
                biImg = biBigEnem[1];
                if(intEnemyFloor == 1){
                    //load special img
                }
                if(intEnemyFloor == 2){
                    //load special img
                }
                if(intEnemyFloor == 3){
                    //load special img
                }
            }
        }

        camObject = handler.getObject(Main.intSessionId - 1);
    }

    public void update() {
        fltTargetX = findNearestObject(fltWorldX, fltWorldY).getWorldX();
        fltTargetY = findNearestObject(fltWorldX, fltWorldY).getWorldY();
        //small enemies
        if(intEnemyType == 1){
            //crawlers
            if(intEnemyClass == 1){
                if(fltTargetX > fltWorldX){
                    fltVelX = 8;
                }
                else{
                    fltVelX = -8;
                }
                if(fltTargetY > fltWorldY ){
                    
                }
            }
            //shooters
            if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX;
                float fltDiffY = fltTargetY - fltWorldY;
                
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 300) {
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
                blnFalling = true;
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
                if (System.currentTimeMillis() - dblTimer > 300) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 30 , fltDiffY * 30 , 10, 10,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, true, 350));
                }
                /*if(intJumpCap == 0 && fltWorldY > fltTargetY && blnFalling == false){
                    fltVelY = -15;
                    blnFalling = true;
                    intJumpCap = 1;
                } */
                blnFalling = true;
            }
            //big homer
            if(intEnemyClass == 1){
                blnFalling = false;
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
                
            }
        }
        if(fltVelY > 35) fltVelY = 35;
        else if(fltVelY < -35) fltVelY = -35; 

        if(fltVelX > 35) fltVelX = 35;
        else if(fltVelX < -35) fltVelX = -35; 

        collisions();
        if(blnFalling){
            fltVelY += 3;
        }
        
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;

        /*if(fltHP <= 0 ){
            handler.removeObject(this);
        }*/
        if(intBleedCount >= 5){
            fltHP *= 0.6;
        }
        if(fltBurnDmg > 0){
            fltHP -= fltBurnDmg;
        }
        if(fltHP <= 0){
            System.out.println("enemy dead");
            handler.removeObject(this);
        }
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
        return handler.getObject(intreturn);
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    intJumpCap = 0;
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                }else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
            } else if(object.getId() == ObjectId.BULLET) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(object);
                    Bullet bullet = (Bullet) object;
                    fltHP -= bullet.getDMG();

                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, bullet.getDMG(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = bullet.getBleed();
                        intBleedCount = (int) bullet.getBleed();
                    }    
                    if(bullet.getCelebShot() > 0){
                        for(int intcount = 0; intcount < bullet.getCelebShot(); intcount++){
                            ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (bullet.getVelX()) + "," + (bullet.getVelY()) + "," + 6 + "," + 6 + "," + 4);
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, bullet.getVelX(), bullet.getVelY(), 6, 6, 0, 0, 0, 0, bullet.getCelebShot(), bullet.getDMG(), ObjectId.BULLET, handler, ssm, biBullets[0], false, bullet.getBoom(), bullet.getChar()));
                        }
                    }     
                }
            }
            else if(object.getId() == ObjectId.WAVE) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    WaveAttacks bullet = (WaveAttacks) object;
                    fltHP -= bullet.getDMG();

                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, bullet.getDMG(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = bullet.getBleed();
                        intBleedCount = (int) bullet.getBleed();
                    }
                    
                    if(bullet.getCelebShot() > 0){
                        for(int intcount = 0; intcount < bullet.getCelebShot(); intcount++){
                            ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (bullet.getVelX()) + "," + (bullet.getVelY()) + "," + 6 + "," + 6 + "," + 4);
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, bullet.getVelX(), bullet.getVelY(), 6, 6, 0, 0, 0, 0, bullet.getCelebShot(), bullet.getDMG(), ObjectId.BULLET, handler, ssm, biBullets[0], false, bullet.getBoom(), bullet.getChar()));                        
                        }
                    } 
                }
            }
            else if(object.getId() == ObjectId.SLASH) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    KnightSlashes bullet = (KnightSlashes) object;
                    fltHP -= bullet.getDMG();

                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, bullet.getDMG(), fltExplosionRadius * 2, fltExplosionRadius * 2,ObjectId.BOOM, handler, ssm));
                        fltBurnDmg = bullet.getBleed();
                        intBleedCount = (int) bullet.getBleed();
                    } 
                    if(bullet.getCelebShot() > 0){
                        for(int intcount = 0; intcount < bullet.getCelebShot(); intcount++){
                            ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (bullet.getVelX()) + "," + (bullet.getVelY()) + "," + 6 + "," + 6 + "," + 4);
                            handler.addObject(new Bullet(fltWorldX + fltWidth/2, fltWorldY + fltHeight/2, bullet.getVelX(), bullet.getVelY(), 6, 6, 0, 0, 0, 0, bullet.getCelebShot(), bullet.getDMG(), ObjectId.BULLET, handler, ssm, biBullets[0], false, bullet.getBoom(), bullet.getChar()));
                        }
                    }      
                }
            }
            else if(object.getId() == ObjectId.BOOM) {
                Explosion boom = (Explosion) object;
                fltHP -= boom.getDMG();
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltWorldX + fltVelX - camObject.getWorldX() - camObject.getWidth()/2;

        if(fltBoundsX > fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2;
        else if(fltBoundsX < fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2;

        return new Rectangle((int)fltBoundsX, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltWorldY + fltVelY - camObject.getWorldY() - camObject.getHeight()/2;

        if(fltBoundsY > fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2;
        else if(fltBoundsY < fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2;

        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
    }

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
}
