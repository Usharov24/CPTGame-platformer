package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;
import java.awt.Color;

public class Enemies extends GameObject {
    private int intEnemyType;
    private int intEnemyClass;
    private int intEnemyFloor;
    private float fltDmg;
    private float fltHP;
    private float fltTargetX;
    private float fltTargetY;
    private boolean blnFalling = true;
    private double dblTimer = System.currentTimeMillis();
    private int intJumpCap = 0;
    private int intBleedCount = 0;
    private float fltBurnDmg = 0;

    

    public Enemies(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltHealth, int intEnemyType, int intEnemyFloor, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.intEnemyFloor = intEnemyFloor;
        this.fltVelY = fltVelY;
        this.intEnemyClass = (int)Math.floor(Math.random() * 2 + 1);
        this.intEnemyType = intEnemyType;
        this.fltDmg = intEnemyFloor;
        //determine sprites off of intenemyclass and enemy type later on and enemy floor 18 total sprites
        //use the class to determine the size of the enemy
        if(intEnemyType == 1){
            if(intEnemyClass == 1){
                fltWidth = 40;
                fltHealth = 100;
                fltHeight = 40;
                fltDmg = 40;
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
                fltWidth = 60;
                fltHealth = 250;
                fltHeight = 60;
                fltDmg = 100;
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
                fltWidth = 70;
                fltHeight = 70;
                fltDmg = 150;
                fltHealth = 450;
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
                fltHeight = 40;
                fltDmg = 100;
                fltHealth = 600;
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
            if(intEnemyClass == 2){
                fltHeight = 100;
                fltWidth = 100;
                fltDmg = 300;
                fltHealth = 600;
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
            if(intEnemyClass == 1){
                fltHeight = 40;
                fltWidth = 40;
                fltDmg = 150;
                fltHealth = 600;
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
                    fltWorldX +=8;
                }
                else{
                    fltWorldX -= 8;
                }
            }
            //shooters
            if(intEnemyClass == 2){
                float fltDiffX = fltTargetX - fltWorldX - fltWidth/2;
                float fltDiffY = fltTargetY - fltWorldY - fltHeight/2;
                
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
                System.out.println("x" + fltDiffX);
                System.out.println("Y" + fltDiffY);
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                if (System.currentTimeMillis() - dblTimer > 1000) {
                    dblTimer = System.currentTimeMillis();
                    handler.addObject(new EnemyBullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20 , fltDiffY * 20 , 50, 50,  fltDmg, ObjectId.ENEMY_BULLET, handler, ssm, null, false, 250));
                }
            }   
        }
        //large enemies
        if(intEnemyType == 3){
            //big crawler
            if(intEnemyClass == 1){
                float fltDiffX = fltTargetX - fltWorldX - fltWidth/2;
                float fltDiffY = fltTargetY - fltWorldY - fltHeight/2;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                if(fltTargetX > fltWorldX){
                    fltWorldX +=10;
                }
                else{
                    fltWorldX -= 10;
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
            }
            //big homer
            if(intEnemyClass == 2){
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
        collisions();
        if(blnFalling){
            fltVelY += 3;
        } 
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
        //if(fltHP <= 0 ){
        //    handler.removeObject(this);
        //}
        if(intBleedCount >= 5){
            fltHP *= 0.6;
        }

        if(fltBurnDmg > 0){
            fltHP -= fltBurnDmg;
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

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.red);
        g2d.fillRect((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                    intJumpCap = 0;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                }else if(getBounds().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    blnFalling = false;
                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
            }
            if(object.getId() == ObjectId.BULLET){
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(object);
                    Bullet bullet = (Bullet) object;
                    fltHP -= bullet.getDMG();
                    if(bullet.getBoom()> 0){
                        float fltExplosionRadius = bullet.getBoom();
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, fltExplosionRadius*2, fltExplosionRadius*2,ObjectId.BOOM, handler, ssm));
                        //arbitary value to make sure bomb doesnt explode multiple times
                    }        
                }
            }
            
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltHeight, (int)fltWidth);
    }
    public void setHP(float fltHp){
        this.fltHP = fltHp;
    }

    public float getHP(){
        return fltHP;
    }

    public float getDMG(){
        return fltDmg;
    }


}
