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
    /**
    The inputhandler which holds key values
     **/
    private ResourceLoader resLoader = new ResourceLoader();
    /**
    The resource loader which helps load files
     **/
    private BufferedImage[] biBulletTextures;
    /**
    The BufferedImage which stores the bullet textures
     **/
    private BufferedImage[] biSprite;
    /**
    The BufferedImage which stores the player texturex
     **/
    private BufferedImage[] biCountDown;
    /**
    The BufferedImage which stores the ability cooldowntextures
     **/


    private float fltAcc = 1f, fltDec = 0.5f;
    /**
    The float fltDec and atAcc which stores the values for acceleration and deceleration
     **/
    private float fltDmgMult = 1;
    /**
    The float which will store the value for the damage multiplied which is at 1 at the start
     **/
    private float fltRecoilX;
    /**
    The float which will store the value for the horizontal recoil
     **/
    private float fltRecoilY;
    /**
    The float which will store the value for the vertical recoil
     **/
    private float fltDashVel;
    /**
    The float which will store the value for the dash velocity
     **/
    private float fltRegen = 4;
    /**
    The float which will store the value for the regeneration values which is at 4 at the start
     **/
    private float fltBSpeedMult = 1;
    /**
    The float which will store the value for the bullet speed multiplier value which is at 1 at the start
     **/
    private float fltPSpeedMult = 1;
    /**
    The float which will store the value for the player speed multiplier value which is at 1 at the start
     **/
    private float fltReflectDmg = 0;
    /**
    The float which will store the value for the reflection damage value which is at 0 at the start
     **/
    private float fltMaxHP = 1000;
    /**
    The float which will store the value for the MaxHP values which is at 1000 at the start
     **/
    private float fltPastDmgMult = 1;
    /**
    The float which will store the value for the past damage multiplier values which is at 1 at the start
     **/
    private float fltBurnDmg = 0; 
    /**
    The float which will store the value for the burning damage values which is at 0 at the start
     **/ 
    private float fltAirDmgMult = 1;
     /**
    The float which will store the value for the air damage multiplier values which is at 1 at the start
     **/ 
    private float fltLifeSteal = 0; 
     /**
    The float which will store the value for the lifesteal values which is at 0 at the start
     **/ 
    private float fltHP = 1000;
     /**
    The float which will store the value for the HP values which is at 1000 at the start
     **/ 
    private float fltDef = 1;
     /**
    The float which will store the value for the defense values which is at 1 at the start
     **/ 
    private float fltFireRateMult = 1;
     /**
    The float which will store the value for the fire rate multiplier values which is at 1 at the start
     **/ 
    private long[] lngTimer = {0, 0, 0, 0, 0, 0};
     /**
    The long which will store the value for the time between abilities which is at 0 at the start
     **/ 

    private int intPosition;
     /**
    The int value for the position in handlers list
     **/
    private int intDirection;
     /**
    The int value for dashing direction
     **/
    private int intJumpCount;
    /**
    The int value for the number of jumps made
    **/
    private int intWungoosCount;
    /**
    The int value for the number of Wungoos picked up
    **/
    private int intPeirceCount;
    /**
    The int value for the peirce a bullet has
    **/
    private int intExplodeRad;
    /**
    The int value for the explosion radius for a projectile
    **/
    private int intShurikenCount;
    /**
    The int value for the number of bonus projectiles
    **/
    private int intBleedCount;
    /**
    The int value for the number of procs each projectile has
    **/
    private int intCelebShot;
    /**
    The int value for the number of bonus shots upon a bullets collisions
    **/
    private int intJumpCap = 2;
    /**
    The int value for the max number of jumps which is 2 in this character
    **/

    private boolean blnLeft = false;
    /**
    The boolean value direction the character is facing which starts off as false
    **/
    private boolean blnFalling = true;
     /**
    The boolean value for falling which starts off as true
    **/
    private boolean blnBazooka = false;
    /**
    The boolean value for Bazooka which starts off as false
    **/
    private boolean blnRapidFire = false;
    /**
    The boolean value for RapidFire which starts off as false
    **/
    private boolean blnHoming = false;
    /**
    The boolean value for homing which starts off as false
    **/
    /**
     * Constructor for the Sniper
     * @param fltWorldX - float value for the X position of the Sniper
     * @param fltWorldY - float value for the Y position of the Sniper
     * @param fltWidth - float value for the width of the Sniper
     * @param fltHeight - float value for the height of the Sniper
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param input - the input list of keys
     * @param intPosition - float value for the width of the Sniper
     **/
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

        if(fltHP <= 0) {
            Main.intAlivePlayers[intPosition] = 1;
            return;
        }

        if(Main.state == Main.State.GAME && (fltWorldX < -40 || fltWorldX > 1960 || fltWorldY < -40 || fltWorldY > 1480)) {
            fltWorldX = 200;
            fltWorldY = 1400;
        } else if(Main.state == Main.State.DEMO && (fltWorldX < -40 || fltWorldX > 1240 || fltWorldY < -40 || fltWorldY > 840)){
            fltWorldX = 200;
            fltWorldY = 800;
        }

        if(intPosition == Main.intSessionId - 1) {
            if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                input.buttonSet.remove(InputButtons.W);
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
                blnFalling = true;
                intJumpCount++;
            }
            //jumping for the character

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
                //movement for the character
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
                    //mutliples the dmg multiplied by the air dmg mult
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
                
                    if(intPosition == 0 && ssm != null) {
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    } else if(ssm != null) {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount + "," + fltBurnDmg + "," + 30 * fltDmgMult + "," + 4 + ","+ blnHoming + "," + intExplodeRad);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + intRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount + "," + fltBurnDmg + ","+ 30 * fltDmgMult + "," + 4 + ","+ blnHoming + "," + intExplodeRad);
                    }
                    //responsible for the shrapnel
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand1) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 0));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand2) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 0));
                }

                if(!blnBazooka){
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10,intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 120*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[0], blnHoming, intExplodeRad,0));

                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                    
                    fltRecoilX += fltDiffX * -8;
                    fltRecoilY += fltDiffY * -8;
                    //sends the bullet parameters
                    //propels the player in the opposite direction of the shot
                } else {
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 150*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[1], blnHoming, 50 + intExplodeRad, 0));
                    
                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                    
                    fltRecoilX += fltVelX + fltDiffX * -10;
                    fltRecoilY += fltVelY + fltDiffY * -10;
                    //sends the bullet parameters
                    //propels the player in the opposite direction of the shot
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
                    //determines the angle necessary to hit the target
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    
                    if(!blnBazooka){
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 120*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[0], blnHoming, intExplodeRad,0));
                        
                        if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                        else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 120*fltDmgMult +","+ 0 +","+ blnHoming +","+ intExplodeRad);
                        
                        fltRecoilX += fltDiffX * -4;
                        fltRecoilY += fltDiffY * -4;
                        //sends the bullet parameters
                        //propels the player in the opposite direction of the shot
                    } else {
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60 * fltBSpeedMult, fltDiffY * 60 * fltBSpeedMult, 10, 10, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 150*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[1], blnHoming, 100 + intExplodeRad,0));
                        
                        if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                        else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 1 +","+ blnHoming +","+ intExplodeRad + 50);
                        
                        fltRecoilX += fltDiffX * -6;
                        fltRecoilY += fltDiffY * -6;
                        //sends the bullet parameters
                        //propels the player in the opposite direction of the shot
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
            //sets recoil to 0
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
            //creates a top speed for each player

            collisions();
            //checks for collisions
            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            //moves the player

            if(intPosition == 0 && ssm != null) ssm.sendText("h>a>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + blnLeft + ","  + fltHP + ","+ fltMaxHP + "," + intPosition);
            else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + fltHP + ","+ fltMaxHP + "," + intPosition);
        }
    }
    /**
     * @return void 
     * this checks for collisions
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
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    blnFalling = false;
                    intJumpCount = 0;

                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
                //ensures the player does not fall through the barriers
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
            } 
            //when colliding with an enemy, take the dmg from the enemy and get hurt by it
            else if(object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds())) {  
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
                        //wont do anything for Sniper
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
        if(fltHP <= 0) return;

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
            //draws boxes with times for the ability cooldown for each attack
        } else {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 32, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
            }
            //flips the sprite around depending on the input
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
    //returns the x hitboxes

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
    /**
     * 
     * @return Rectange of the y bounds for a player
     *
     */

    public float getHP(){
        return fltHP;
    }
    /**
     * 
     * @return float hp value
     */

    public float getMaxHP(){
        return fltMaxHP;
    }
     /**
     * 
     * @return float Maxhp value
     */


    public void setHP(float fltHP){
        this.fltHP = fltHP;
    }

     /**
     * @param fltHP which is the health being set
     * @return void
     */


    public void setMaxHP(float fltMaxHP){
        this.fltMaxHP = fltMaxHP;
    }
    /**
     * @param fltMaxHP which is the health being set
     * @return void
     */

    public float getDef(){
        return fltDef;
    }

    /**
     * @return float fltDef which is the defense of the player
     */

    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }

    /**
     * @return boolean blnLeft which determines which direction the player is pointing
     */
    //all used in netwokring
}
