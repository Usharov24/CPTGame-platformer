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

public class Brute extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage biBulletTexture[];
    private BufferedImage[] biVacTextures;
    private BufferedImage[] biSprite;
    private BufferedImage[] biCountDown;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltAngle = 270;

    private float fltPastDmgMult = 1;
    private float fltUltSpeed = 0;
    private float fltHP = 2500;
    private float fltBurnDmg = 0;  
    private float fltAirDmgMult = 1;
    private float fltLifeSteal = 0;
    private float fltMaxHP = 2500; 
    private float fltDmgMult = 1;
    private float fltRegen = 4;
    private float fltBSpeedMult = 1;
    private float fltDef = 1;
    private float fltFireRateMult = 1;
    private float fltPSpeedMult = 1;
    private float fltReflectDmg = 0;

    private long[] lngTimer = {0, 0, 0, 0, 0, 0};

    private int intPosition;
    private int intJumpCount;
    private int intWungoosCount = 0;
    private int intPeirceCount = 0;
    private int intExplodeRad = 0;
    private int intShurikenCount = 0;
    private int intBleedCount = 0;
    private int intCelebShot = 0;
    private int intJumpCap = 3;

    private boolean blnHoming = false;
    private boolean blnFalling = false;
    private boolean blnLeft = false;
    private boolean blnSlamming = false;
    private boolean blnUlt = false;

    public Brute(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.intPosition = intPosition;

        biSprite = resLoader.loadImages("/res\\Brute.png");
        biVacTextures = resLoader.loadSpriteSheet("/res\\VacGrenade.png", 20, 20);
        biBulletTexture = resLoader.loadImages("/res\\Shrapnel.png");
        biCountDown = resLoader.loadImages("/res\\M2.png","/res\\Shift.png","/res\\FKey.png");
    }

    public void update() {
        if(intPosition != Main.intSessionId - 1 && camObject == null) camObject = handler.getObject(Main.intSessionId - 1);

        if(fltWorldX < -40 || fltWorldX > 1960 || fltWorldY < -40 || fltWorldY > 1480) {
            fltWorldX = 200;
            fltWorldY = 1400;
        }

        if(intPosition == Main.intSessionId - 1) {
            if(blnFalling){
                fltPastDmgMult = fltDmgMult;
                fltDmgMult *= fltAirDmgMult;
                //responsible for multiplying dmg if the player is falling
            }
            if(!blnUlt) {
                //used to make sure the player is not a rocket 
                if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                    input.buttonSet.remove(InputButtons.W);
                    input.buttonSet.remove(InputButtons.SPACE);
                    fltVelY = -45;
                    intJumpCount++;
                }
                //allows the player to jump

                if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                    fltVelX -= fltAcc;
                    blnLeft = true;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                    fltVelX += fltAcc;
                    blnLeft = false;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
                    if(fltVelX > 0) fltVelX -= fltDec;
                    else if(fltVelX < 0) fltVelX += fltDec;
                } else {
                    if(fltVelX > 0) fltVelX -= fltDec;
                    else if(fltVelX < 0) fltVelX += fltDec;
                }
                //used to make the player move by changing the velocity

                if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 3000 * fltFireRateMult) {
                    lngTimer[0] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.SHIFT);
                    blnSlamming = true;
                    fltVelY = -30;
                    //player ability which sets the character into a ground pound like fall
                }

                if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 8000 * fltFireRateMult) {
                    lngTimer[1] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.F);
                    blnUlt = true;
                    blnSlamming = false;
                    fltWorldY -= 100;
                    //changes state and allows the player to fly like a rocket
                }

                if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 50 * fltFireRateMult) {
                    lngTimer[2] = System.currentTimeMillis();
                    for(int intcount = 0; intcount < intShurikenCount; intcount++){
                        float fltDiffX = input.fltMouseX - 640;
                        float fltDiffY = input.fltMouseY - 360;
                        float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                        float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                        float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                        fltDiffX /= fltLength;
                        fltDiffY /= fltLength;
                    
                        if(intPosition == 0) {
                            ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," +  (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult + "," + 6 + "," + 6  + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                            ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," +  (fltDiffX * 20 + intRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand4) * fltBSpeedMult + "," + 6 + "," + 6  + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        } else {
                            ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                            ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + intRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        }

                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture[0], blnHoming, intExplodeRad, 0));
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture[0], blnHoming, intExplodeRad, 0));
                    }
                    //specific code used if the player has any shot gun pellet items
                    
                    if(input.fltMouseX - 640 < 0) {
                        handler.addObject(new SlashAttacks(fltWorldX + 25, fltWorldY + 15, -20 * fltBSpeedMult, System.currentTimeMillis() - 75, 50, 50, 135, 80 * fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 * fltBSpeedMult + "," + 50 + "," + 50 + "," + 135 + "," + 80 * fltDmgMult + "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 * fltBSpeedMult + "," + 50 + "," + 50 + "," + 135 + "," + 80 * fltDmgMult + "," + intExplodeRad+ "," + fltBurnDmg + "," + intBleedCount);
                    } else {
                        handler.addObject(new SlashAttacks(fltWorldX, fltWorldY + 15 , 20 * fltBSpeedMult, System.currentTimeMillis() - 75, 50, 50, 270, 80 * fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 * fltBSpeedMult +"," + 50 + "," + 50 + "," + 270 + "," + 80 * fltDmgMult + "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 * fltBSpeedMult + "," + 50 + "," + 50 + "," + 270 + "," + 80 * fltDmgMult + "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount);
                    }
                    //determines the direction of the slash and then creates it and sends it over a network
                } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 2000) {
                    lngTimer[3] = System.currentTimeMillis();
                    //throws a vacuum grenade in the direction of the cursor
                    float fltDiffX = input.fltMouseX - 640;
                    float fltDiffY = input.fltMouseY - 360;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;

                    handler.addObject(new VacGrenade(fltWorldX + fltWidth/2 - 10, fltWorldY + fltHeight/2 - 10, fltDiffX * 40, fltDiffY * 40, 20, 20, ObjectId.BULLET, handler, ssm, biVacTextures));
                    //sends the grenade over a network
                    if(intPosition == 0) ssm.sendText("h>a>aVAC~" + (fltWorldX + fltWidth/2 - 10) + "," + (fltWorldY + fltHeight/2 - 10) + "," + (fltDiffX * 40) + "," + (fltDiffY * 40) + "," + 20 + "," + 20);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aVAC~" + (fltWorldX + fltWidth/2 - 10) + "," + (fltWorldY + fltHeight/2 - 10) + "," + (fltDiffX * 40) + "," + (fltDiffY * 40) + "," + 20 + "," + 20);
                }

                if(System.currentTimeMillis() - lngTimer[4] > 100){
                    lngTimer[4] = System.currentTimeMillis();
                    if(fltVelX == 0 && fltVelY == 0 && intWungoosCount > 0){
                        fltHP += (fltRegen + (fltRegen*intWungoosCount*0.3)) * 2;
                    }
                    else{
                        fltHP += fltRegen * 2;
                    }
                    //regenerates the player health over a one second frame
                }

                if(fltHP > fltMaxHP){
                    fltHP = fltMaxHP;
                }
                //makes sure the health does not go over max health

                fltVelY += 3;

                if(fltVelX > 15) fltVelX = 15;
                else if(fltVelX < -15) fltVelX = -15;

                if(fltVelY > 30) fltVelY = 30;
                else if(fltVelY < -30) fltVelY = -30;
                //caps out the x velocity for players
            } else {
                if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                    fltAngle -= 8;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                    fltAngle += 8;
                }

                fltUltSpeed += 0.3;
                fltVelY += (float)(fltUltSpeed*Math.sin(Math.toRadians(fltAngle)));
                fltVelX += (float)(fltUltSpeed*Math.cos(Math.toRadians(fltAngle)));

                if(fltVelX > 30 * fltPSpeedMult) fltVelX = 30 * fltPSpeedMult;
                else if(fltVelX < -30 * fltPSpeedMult) fltVelX = -30 * fltPSpeedMult;
                //caps out x velocities for players
            }

            //checks if the player collides with any other object
            collisions();

            if(!blnSlamming) fltWorldX += fltVelX;
            else fltWorldX += fltVelX * 2;

            fltWorldY += fltVelY;
            //adds the velocity ot the current player position
            
            if(intPosition == 0) ssm.sendText("h>a>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + intPosition);
            fltDmgMult = fltPastDmgMult;
            //sends player stats and sets dmg back to normal if changeg
        }
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
    
            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;

                    if(blnUlt) {
                        blnUlt = false;
                        fltUltSpeed = 0;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth, fltWorldY + fltHeight/2, fltDmgMult * 100, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + fltDmgMult * 100 + "," + 300 + "," + 300); 
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + fltDmgMult * 100 + "," + 300 + "," + 300);
                    }
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();

                    if(blnUlt) {
                        blnUlt = false;
                        fltUltSpeed = 0;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX, fltWorldY + fltHeight/2, fltDmgMult * 100, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + ","+ fltDmgMult * 100 + "," + 300 + "," + 300); 
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + fltDmgMult * 100 + "," + 300 + "," + 300);
                    }
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    intJumpCount = 0;
                    fltWorldY = object.getWorldY() - fltHeight;

                    if(blnSlamming) {
                        blnSlamming = false;

                        for(int intCount2 = 0; intCount2 < 2; intCount2++) {
                            handler.addObject(new Explosion(fltWorldX + fltWidth * intCount2, fltWorldY + fltHeight, fltDmgMult * 100, 300, 300, ObjectId.BOOM, handler, ssm));

                            if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth * intCount2) + "," + (fltWorldY + fltHeight) + ","+ fltDmgMult * 100 + "," + 300 + "," + 300); 
                            else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth * intCount2) + "," + (fltWorldY + fltHeight) + "," + fltDmgMult * 100 + "," + 300 + "," + 300);
                        }
                    } else if(blnUlt) {
                        blnUlt = false;
                        fltUltSpeed = 0;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth/2, fltWorldY + fltHeight, fltDmgMult*100, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth/2) + "," + (fltWorldY + fltHeight) + ","+ fltDmgMult*100 + "," + 300 + "," + 300); 
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth/2) + "," + (fltWorldY + fltHeight) + "," + fltDmgMult*100 + "," + 300 + "," + 300);
                    }
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();

                    if(blnUlt) {
                        blnUlt = false;
                        fltUltSpeed = 0;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth/2, fltWorldY, fltDmgMult*100, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + ","+ fltDmgMult*100 + "," + 300 + "," + 300); 
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + fltDmgMult*100 + "," + 300 + "," + 300);
                    }
                }
                //if the player collides with a barrier, stop the player
            }
            
            if((object.getId() == ObjectId.ENEMY && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                Enemy enemy = (Enemy) object;
                fltHP -= enemy.getDmg()/fltDef;
                if(fltReflectDmg > 0){
                    enemy.setHP(enemy.getHP() - (float)(enemy.getDmg() * fltReflectDmg * 0.1));
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
            }
            //if the player collides with any enemy object, take dmg and maybe remove the object
            else if((object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds()))) {  
                Item item = (Item) object;
                System.out.println("otuch");
                
                if(item.getRarity() == 1) { 
                    if(item.getPlacement() == 1) {
                        fltDmgMult += 0.2;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP += 100;
                        fltHP += 100;
                    } else if(item.getPlacement() == 3) {
                        //add statement later using bln movement
                        intWungoosCount += 1;                       
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
                        fltDef += 0.2;
                    } else if(item.getPlacement() == 9) {
                        fltFireRateMult *= 0.9;
                    }
                } else if(item.getRarity() == 2) { 
                    if(item.getPlacement() == 1) {
                        fltAirDmgMult += 0.2;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP *= 0.2;
                        fltHP *= 0.2;
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
                        fltFireRateMult *= 0.75;
                    } else if(item.getPlacement() == 5) {
                        intCelebShot += 1;
                    }
                }
                
            }
            //when an item gets picked up, change player stats to match it
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if(intPosition == Main.intSessionId - 1) {
            if(System.currentTimeMillis() - lngTimer[0] > 3000){
                g2d.drawImage(biCountDown[1], 450, -325, null);

            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(450, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((3000-(System.currentTimeMillis()-lngTimer[0]))/1000))), 467, -302);
            }
            if(System.currentTimeMillis() - lngTimer[1] > 8000){
                g2d.drawImage(biCountDown[2], 500, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(500, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((8000-(System.currentTimeMillis()-lngTimer[1]))/1000))), 517, -302);
            }
            if(System.currentTimeMillis() - lngTimer[3] > 2000){
                g2d.drawImage(biCountDown[0], 400, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(400, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((2000-(System.currentTimeMillis()-lngTimer[3]))/1000))), 417, -302);
            }

            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2 + 32, (int)-fltHeight/2, -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2, (int)-fltHeight/2, null);
            }
            
        } else {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 32, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
            }        
        }
        //draws the player and flips the sprite if necessary
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltVelX - fltWidth/2;

        if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2;
        else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

        return new Rectangle((int)fltBoundsX, (int)-fltHeight/2 + 4, (int)fltWidth, (int)fltHeight - 8);
        //player bounds for x
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltVelY - fltHeight/2;

        if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
        else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

        return new Rectangle((int)-fltWidth/2 + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
        //player bounds for y
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

    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }

    public long[] getTimer(){
        return this.lngTimer;
    }

    //methods used to change and grab values from the player when needed
}
