package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import Framework.InputHandler;
import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Framework.SuperSocketMaster;
import Framework.InputHandler.InputButtons;

public class Sniper extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;
    private float fltRecoilX, fltRecoilY;

    private BufferedImage[] biSprite;
    private BufferedImage[] biBulletTextures;
    private BufferedImage[] biCountDown;

    private long[] lngTimer = {0, 0, 0, 0, 0, 0};

    private float fltHP = 1000;
    private float fltMaxHP = 1000;
    private float fltDmgMult = 1;
    private float fltRegen = 4;
    private float fltBSpeedMult = 1;
    private float fltPSpeedMult = 1;
    private float fltReflectDmg = 0;
    private float fltDef = 1;
    private float fltBurnDmg = 0;  
    private float fltAirDmgMult = 1;
    private float fltLifeSteal = 0;
    private float fltFireRateMult = 1;
    private float fltPastDmgMult = 1;

    private int intExplodeRad = 0;
    private int intShurikenCount = 0;
    private int intBleedCount = 0; 
    private int intCelebShot = 0;
    private int intJumpCap = 2;
    private int intPeirceCount = 0;
    private int intWungoosCount = 0;
    private int intPosition;    
    private int intJumpCount;
    private int intDirection = 1;

    private boolean blnHoming = false;
    private boolean blnLeft = false;
    private boolean blnFalling = true;
    private boolean blnBazooka = false;
    private boolean blnRapidFire = false;
    
    public Sniper(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.intPosition = intPosition;

        biSprite = resLoader.loadImages("/res\\Sniper.png");
        biBulletTextures = resLoader.loadImages("/res\\SniperBullet.png", "/res\\Rocket.png", "/res\\Shrapnel.png");
        biCountDown = resLoader.loadImages("/res\\M2.png","/res\\Shift.png","/res\\FKey.png");;
    }

    public void update() {
        if(intPosition != Main.intSessionId - 1 && camObject == null) camObject = handler.getObject(Main.intSessionId - 1);

        if(fltWorldX < -40 || fltWorldX > 1960 || fltWorldY < -40 || fltWorldY > 1480) {
            fltWorldX = 200;
            fltWorldY = 1400;
        }

        if(intPosition == Main.intSessionId - 1) {
            if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                input.buttonSet.remove(InputButtons.W);
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
                blnFalling = true;
                intJumpCount++;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                fltVelX -= fltAcc;
                intDirection = -1;
                blnLeft = true;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                fltVelX += fltAcc;
                intDirection = 1;
                blnLeft = false;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            } else {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 1000 * fltFireRateMult) {
                //Moving variables
                lngTimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                if(intDirection > 0){
                    fltDashVel = 50;
                } else if(intDirection < 0){
                    fltDashVel = -50;
                }
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 10000 * fltFireRateMult) {
                lngTimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                blnBazooka = true;
                //The Ultimate abilty
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 1000 * fltFireRateMult) {
                if(blnFalling){
                    fltPastDmgMult = fltDmgMult;
                    fltDmgMult *= fltAirDmgMult;
                }

                blnFalling = true;

                lngTimer[2] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                for(int intcount = 0; intcount < intShurikenCount; intcount++){
                    float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                    float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;
                
                    if(intPosition == 0) {
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    } else {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount + "," + fltBurnDmg + "," + 30 * fltDmgMult + "," + 4 + ","+ blnHoming + "," + intExplodeRad);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + intRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount + "," + fltBurnDmg + ","+ 30 * fltDmgMult + "," + 4 + ","+ blnHoming + "," + intExplodeRad);
                    }

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand1) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 0));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand2) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 0));
                }

                if(!blnBazooka){
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10,intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 120*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[0], blnHoming, intExplodeRad,0));

                    if(intPosition == 0) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                    
                    fltRecoilX += fltDiffX * -15;
                    fltRecoilY += fltDiffY * -15;
                } else {
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 150*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[1], blnHoming, 50 + intExplodeRad, 0));
                    
                    if(intPosition == 0) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                    
                    fltRecoilX += fltVelX + fltDiffX * -15;
                    fltRecoilY += fltVelY + fltDiffY * -15;
                }
                fltDmgMult = fltPastDmgMult;
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 5000 * fltFireRateMult) {
                lngTimer[3] = System.currentTimeMillis();
                blnRapidFire = true;
            }

            if(System.currentTimeMillis() - lngTimer[1] > 5000 && blnBazooka){
                blnBazooka = false;
            }

            if(blnRapidFire){
                if(System.currentTimeMillis() - lngTimer[3] < 500 && (System.currentTimeMillis() - lngTimer[3]) % 2 == 0){
                    if(blnFalling){
                        fltPastDmgMult = fltDmgMult;
                        fltDmgMult *= fltAirDmgMult;
                    }

                    blnFalling = true;

                    float fltDiffX = input.fltMouseX - 640;
                    float fltDiffY = input.fltMouseY - 360;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    
                    if(!blnBazooka){
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 120*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[0], blnHoming, intExplodeRad,0));
                        
                        if(intPosition == 0) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                        
                        fltRecoilX += fltDiffX * -3;
                        fltRecoilY += fltDiffY * -3;
                    } else {
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 150*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[1], blnHoming, 100 + intExplodeRad,0));
                        
                        if(intPosition == 0) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                        
                        fltRecoilX += fltDiffX * -3;
                        fltRecoilY += fltDiffY * -3;
                    }
                    fltDmgMult = fltPastDmgMult;
                }
            }
            if(System.currentTimeMillis() - lngTimer[4] > 200){
                lngTimer[4] = System.currentTimeMillis();
                if(fltVelX == 0 && fltVelY == 0 && intWungoosCount > 0){
                    fltHP += (fltRegen + (fltRegen*intWungoosCount*0.3));
                }
                else{
                    fltHP += fltRegen;
                }
                //regenerates the player health 
            }

            if(fltHP > fltMaxHP){
                fltHP = fltMaxHP;
            }
            
            fltVelY += 3;

            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            if(fltRecoilX > 0) fltRecoilX -= 1;
            else if(fltRecoilX < 0) fltRecoilX += 1;

            if(fltRecoilX < 0.2 || fltRecoilX > -0.2) fltRecoilX = (float)Math.floor(fltRecoilX);
            if(fltRecoilY < 0.2 || fltRecoilY > -0.2) fltRecoilY = (float)Math.floor(fltRecoilY);

            if(fltRecoilY > 0) fltRecoilY -= 1;
            else if(fltRecoilY < 0) fltRecoilY += 1;

            fltVelY += fltRecoilY;

            if(fltVelX > 10 * fltPSpeedMult) fltVelX = 10 * fltPSpeedMult;
            else if(fltVelX < -10 * fltPSpeedMult) fltVelX = -10 * fltPSpeedMult;

            fltVelX += fltRecoilX + fltDashVel;

            if(fltVelX > 35) fltVelX = 35;
            else if(fltVelX < -35) fltVelX = -35;

            if(fltVelY > 35) fltVelY = 35;
            else if(fltVelY < -35) fltVelY = -35;

            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;

            if(intPosition == 0) ssm.sendText("h>a>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + intPosition);
        }
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
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    blnFalling = false;
                    intJumpCount = 0;

                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
            } else if((object.getId() == ObjectId.ENEMY && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                Enemy enemy = (Enemy) object;
                fltHP -= enemy.getDmg() / fltDef;
                if(fltReflectDmg > 0){
                    enemy.setHP(enemy.getHP() - (float)(enemy.getDmg()*fltReflectDmg*0.1));
                }
                lngTimer[5] = System.currentTimeMillis();
            } else if((object.getId() == ObjectId.ENEMY_BULLET && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY_BULLET && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyBullet enemy = (EnemyBullet) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
            } else if((object.getId() == ObjectId.ENEMY_BOOM) && getBounds().intersects(object.getBounds()) || (object.getId() == ObjectId.ENEMY_BOOM && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyExplosion enemy = (EnemyExplosion) object; 
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
            } else if(object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds())) {  
                handler.removeObject(handler.getObject(intCount));
                Item item = (Item) object;
                if(item.getRarity() == 1){ 
                    if(item.getPlacement() == 1) {
                        fltDmgMult += 0.4;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP += 100;
                        fltHP += 100;
                    } else if(item.getPlacement() == 3) {
                        //add statement later using bln movement
                        intWungoosCount += 2;                       
                    } else if(item.getPlacement() == 4) {
                        fltBSpeedMult *= 1.2;
                    } else if(item.getPlacement() == 5) {
                        fltPSpeedMult *= 1.2;
                    } else if(item.getPlacement() == 6) {
                        fltReflectDmg += 1;
                        //reflect 10% of the dmg and then mult by this
                    } else if(item.getPlacement() == 7) {
                        intPeirceCount += 1;
                    } else if(item.getPlacement() == 8) {
                        fltDef += 0.3;
                    } else if(item.getPlacement() == 9) {
                        fltFireRateMult *= 0.9;
                    }
                } else if(item.getRarity() == 2) { 
                    if(item.getPlacement() == 1) {
                        fltAirDmgMult += 0.2;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP *= 0.3;
                        fltHP *= 0.3;
                    } else if(item.getPlacement() == 3) {
                        intExplodeRad += 25;
                    } else if(item.getPlacement() == 4) {
                        intJumpCap++;
                    } else if(item.getPlacement() == 5) {
                        intBleedCount += 1;
                    } else if(item.getPlacement() == 6) {
                        intShurikenCount += 1;
                    } else if(item.getPlacement() == 7) {
                        fltBurnDmg += 10;
                    }
                } else if(item.getRarity() == 3) { 
                    if(item.getPlacement() == 1) {
                        fltLifeSteal += 0.2;
                    } else if(item.getPlacement() == 2) {
                        //wont do anything for brute
                        blnHoming = true;
                    } else if(item.getPlacement() == 3) {
                        fltRegen *= 2;
                    } else if(item.getPlacement() == 4) {
                        fltFireRateMult *= 0.6;
                    } else if(item.getPlacement() == 5) {
                        intCelebShot += 1;
                    }
                }
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        if(intPosition == Main.intSessionId - 1) {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2 + 32, (int)-fltHeight/2, -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2, (int)-fltHeight/2, null);
            }
            if(System.currentTimeMillis() - lngTimer[0] > 1000){
                g2d.drawImage(biCountDown[1], 450, -325, null);

            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(450, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((1000-(System.currentTimeMillis()-lngTimer[0]))/1000))), 467, -302);
            }
            if(System.currentTimeMillis() - lngTimer[1] > 10000){
                g2d.drawImage(biCountDown[2], 500, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(500, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((10000-(System.currentTimeMillis()-lngTimer[1]))/1000))), 517, -302);
            }
            if(System.currentTimeMillis() - lngTimer[3] > 5000){
                g2d.drawImage(biCountDown[0], 400, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(400, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((5000-(System.currentTimeMillis()-lngTimer[3]))/1000))), 417, -302);
            }
        } else {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 32, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
            }
        }
    }

    public Rectangle getBounds() {
        if(intPosition == Main.intSessionId - 1) {
            float fltBoundsX = fltVelX - fltWidth/2;

            if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2;
            else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

            return new Rectangle((int)fltBoundsX, (int)-fltHeight/2 + 4, (int)fltWidth, (int)fltHeight - 8);
        } else {
            return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        }
    }

    public Rectangle getBounds2() {
        if(intPosition == Main.intSessionId - 1) {
            float fltBoundsY = fltVelY - fltHeight/2;

            if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
            else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

            return new Rectangle((int)-fltWidth/2 + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
        } else {
            return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        }
    }

    public float getHP(){
        return fltHP;
    }

    public float getMaxHP(){
        return fltMaxHP;
    }

    public void setHP(float fltHP){
        this.fltHP = fltHP;
    }

    public float getDef(){
        return fltDef;
    }

    public float getReflectDmg(){
        return fltReflectDmg;
    }

    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }

    public long[] getTimer(){
        return this.lngTimer;
    }
}
