package Objects;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Color;
import Framework.InputHandler;
import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Framework.SuperSocketMaster;
import Framework.InputHandler.InputButtons;

public class Knight extends GameObject {

    private InputHandler input;
    /**
    The inputhandler which holds key values
     **/
    private ResourceLoader resLoader = new ResourceLoader();
    /**
    The resource loader which helps load files
     **/
    private BufferedImage biBulletTexture;
    /**
    The BufferedImage which stores the bullet texture
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
    private float fltDashVelY;
    /**
    The float which will store the value for the speed of dashing up
     **/
    private float fltDashVelX;
    /**
    The float which will store the value for the speed of dashing sideways
     **/
    private float fltDmgMult = 1;
    /**
    The float which will store the value for the damage multiplied which is at 1 at the start
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
    private float fltMaxHP = 1500;
    /**
    The float which will store the value for the MaxHP values which is at 1500 at the start
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
    private float fltHP = 1500;
     /**
    The float which will store the value for the HP values which is at 1500 at the start
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
    private int intJumpCap = 3;
    /**
    The int value for the max number of jumps which is 3 in this character
    **/

    private boolean blnLeft = false;
    /**
    The boolean value direction the character is facing which starts off as false
    **/
    private boolean blnFalling = true;
     /**
    The boolean value for falling which starts off as true
    **/
    private boolean blnBoost = false;
    /**
    The boolean value for boosting which starts off as false
    **/
    private boolean blnHoming = false;
    /**
    The boolean value for homing which starts off as false
    **/
    //all variables used for items and player interactions

    public Knight(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.intPosition = intPosition;
        //informs the program how to handle this object
        biSprite = resLoader.loadImages("/res\\Knight.png");
        biBulletTexture = resLoader.loadImage("/res\\Shrapnel.png");
        biCountDown = resLoader.loadImages("/res\\M2.png","/res\\Shift.png","/res\\FKey.png");;
        //defines the sprites and hitboxes for the player,
    }
    /**
     * Constructor for the Knight
     * @param fltWorldX - float value for the X position of the knight
     * @param fltWorldY - float value for the Y position of the knight
     * @param fltWidth - float value for the width of the knight
     * @param fltHeight - float value for the height of the knight
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param input - the input list of keys
     * @param intPosition - float value for the width of the knight
     **/

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
            if(blnFalling){
                fltPastDmgMult = fltDmgMult;
                fltDmgMult *= fltAirDmgMult;
            }
            //used to multiply the damage from falling
            if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                input.buttonSet.remove(InputButtons.W);
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
                intJumpCount++;
                blnFalling = true;
            }
            //used to allow players to jump
            //jump count is used to make sure the player jumps a certain amount of times

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
            //general movement for the player, moves the player from left to right

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 800 * fltFireRateMult && blnBoost == false) {
                //Moving variables
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                blnFalling = true;
                //used to find the direction of the mouse
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                fltDashVelX = Math.round(fltDiffX * 20);
                fltDashVelY = Math.round(fltDiffY * 20);
                lngTimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                //movement ability for the knight which just propels the knight in the direction where his cursor is
                
            }
            else if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 400 * fltFireRateMult && blnBoost) {
                //Moving variables
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                //used to find the direction of the mouse
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                fltDashVelX = Math.round(fltDiffX * 20);
                fltDashVelY = Math.round(fltDiffY * 20);
                lngTimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                //another movement statement in order to use the cooldown remover from the ultimate effectively
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 10000 * fltFireRateMult) {
                lngTimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                blnBoost = true;
                //The Ultimate abilty
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 200 * fltFireRateMult && blnBoost == false) {
                for(int intcount = 0; intcount < intShurikenCount; intcount++){
                    float fltDiffX = input.fltMouseX - 640;
                    float fltDiffY = input.fltMouseY - 360;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    float fltRand1 = (float)Math.random() * 3, fltRand2 = (float)Math.random() * 3;
                    float fltRand3 = (float)Math.random() * 3, fltRand4 = (float)Math.random() * 3;

                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;

                
                    if(intPosition == 0 && ssm != null) {
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    } else if(ssm != null) {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + fltRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    }

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - fltRand1, fltDiffY * 20 + fltRand3, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad   ,2));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - fltRand2, fltDiffY * 20 - fltRand4, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad,2));
    
                }
                //the for statement above shoots out the shotgun pellets from the shotgun item.
                lngTimer[2] = System.currentTimeMillis();
                if(input.fltMouseX - 640 < 0){
                    handler.addObject(new SlashAttacks(fltWorldX + 25, fltWorldY + 15, -20, System.currentTimeMillis(), 50, 50, 135, 50 * fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));

                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + 50 + "," + 50 + "," + 135 + "," + 50 * fltDmgMult+ "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount+ "," + intCelebShot);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + 50 + "," + 50 + "," + 135+ "," + 50 * fltDmgMult+ "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount+ "," + intCelebShot);
                }
                else{
                    handler.addObject(new SlashAttacks(fltWorldX, fltWorldY + 15 , 20, System.currentTimeMillis(), 50, 50, 270, 50*fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));

                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 +"," + 50 + "," + 50 + "," + 270 + "," + 50 * fltDmgMult+ "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 +"," + 50 + "," + 50 + "," + 270 + "," + 50 * fltDmgMult+ "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                //shoots the slashes either left or right and then sends the slash over a network
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 100 * fltFireRateMult && blnBoost) {
                lngTimer[2] = System.currentTimeMillis();
                //shoots slash left or right depending on mouse
                if(input.fltMouseX - 640 < 0){
                    handler.addObject(new SlashAttacks(fltWorldX + 25, fltWorldY + 15, -20, System.currentTimeMillis() + 300, 50, 50, 135,  60*fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));
                    
                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + 50 + "," + 50 + "," + 135  + "," + 60 * fltDmgMult + "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + 50 + "," + 50 + "," + 135 + "," + 60 * fltDmgMult + "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                }
                else{
                    handler.addObject(new SlashAttacks(fltWorldX, fltWorldY + 15 , 20, System.currentTimeMillis() + 300, 50, 50, 270,  60*fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.SLASH, handler, ssm));
                    
                    if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBIGSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 +"," + 50 + "," + 50 + "," + 270 + "," + 60 * fltDmgMult + "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                    else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + fltWorldX + "," + (fltWorldY + 15) + "," + 20 +"," + 50 + "," + 50 + "," + 270 + "," + 60 * fltDmgMult + "," + intExplodeRad + "," + fltBurnDmg + "," + intBleedCount + "," + intCelebShot);
                }
                //used for the ultimate to ensure the slashes last longer
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 3000 * fltFireRateMult) {
                lngTimer[3] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                for(int intCount = 0; intCount < 4; intCount++) {
                    // Might slightly change how this works in the future
                    float fltRand1 = (float)Math.random() * 3, fltRand2 = (float)Math.random() * 3;
                    float fltRand3 = (float)Math.random() * 3, fltRand4 = (float)Math.random() * 3;

                    if(intPosition == 0 && ssm != null) {
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 60*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 60*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    } else if(ssm != null) {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - fltRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 60*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + fltRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + fltRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 60*fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    }

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - fltRand1) * fltBSpeedMult, (fltDiffY * 20 + fltRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 60*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad,2));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - fltRand2) * fltBSpeedMult, (fltDiffY * 20 + fltRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 60*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad,2));
                }
            }

            if(System.currentTimeMillis() - lngTimer[1] > 8000 && blnBoost == true){
                blnBoost = false;
                //turns off the ultimate ability after a set duration
            }

            if(System.currentTimeMillis() - lngTimer[4] > 100){
                lngTimer[4] = System.currentTimeMillis();
                if(fltVelX == 0 && fltVelY == 0 && intWungoosCount > 0){
                    fltHP += (fltRegen + (fltRegen*intWungoosCount*0.3)) * 1.5;
                }
                else{
                    fltHP += fltRegen * 1.5;
                }
                //regenerates the player health over a one second frame
            }

            if(fltHP > fltMaxHP){
                fltHP = fltMaxHP;
            }
            //caps out player hp
            
            fltVelY += 3;

            if(fltVelX > 10 * fltPSpeedMult) fltVelX = 10 * fltPSpeedMult;
            else if(fltVelX < -10 * fltPSpeedMult) fltVelX = -10 * fltPSpeedMult;

            if(fltVelY > 35) fltVelY = 35;
            else if(fltVelY < -35) fltVelY = -35;

            if(fltDashVelY > 0) fltDashVelY -= 1;
            else if(fltDashVelY < 0) fltDashVelY += 1;

            if(fltDashVelX > 0) fltDashVelX -= 1;
            else if(fltDashVelX < 0) fltDashVelX += 1;

            fltVelX += fltDashVelX;
            fltVelY += fltDashVelY;
            //determines the direction of the player after all calculations
            collisions();
            //collisions are responsible for checking if a player has collided with an object

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            
            fltDmgMult = fltPastDmgMult;
            //resets the damage mult back to standard
            if(intPosition == 0 && ssm != null) ssm.sendText("h>a>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + fltHP + ","+ fltMaxHP + "," + intPosition);
            else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + blnLeft + ","  + fltHP + ","+ fltMaxHP + "," + intPosition);
            //sends player position and parameters over a network
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
                    fltDashVelY = 0;

                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
                //if the player collides with a barrier, push the player back and stop him
            }else if( (object.getId() == ObjectId.ENEMY && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                Enemy enemy = (Enemy) object;
                fltHP -= enemy.getDmg() / fltDef;
                if(fltReflectDmg > 0){
                    enemy.setHP(enemy.getHP() - (float)(enemy.getDmg()*fltReflectDmg*0.1));
                }
                lngTimer[5] = System.currentTimeMillis();
                //if the player collides with an enemy, reflect dmg if possible and then make sure the invincibility frames allow no other hits
            } else if((object.getId() == ObjectId.ENEMY_BULLET && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY_BULLET && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyBullet enemy = (EnemyBullet) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
                //if the player collides with an enemy bullet, take dmg.
            } else if((object.getId() == ObjectId.ENEMY_BOOM) && getBounds().intersects(object.getBounds()) || (object.getId() == ObjectId.ENEMY_BULLET && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyBullet enemy = (EnemyBullet) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
            } else if((object.getId() == ObjectId.ENEMY_BOOM) && getBounds().intersects(object.getBounds()) || (object.getId() == ObjectId.ENEMY_BOOM && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyExplosion enemy = (EnemyExplosion) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
                //if the player collides with an explosion, take damage
            }else if(object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds())) {  
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
                //if the player collides with an item, pick up the item and change statistics for the player
            }
        }
    }
    /**
     this method checks all the player collisons and
     @return void
     */

    public void draw(Graphics g) {
        if(fltHP <= 0) return;

        Graphics2D g2d = (Graphics2D)g;
        
        if(intPosition == Main.intSessionId - 1) {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2 + 32, (int)-fltHeight/2, -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2, (int)-fltHeight/2, null);
            }

            if(System.currentTimeMillis() - lngTimer[0] > 600){
                g2d.drawImage(biCountDown[1], 450, -325, null);

            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(450, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((600-(System.currentTimeMillis()-lngTimer[0]))/1000))), 467, -302);
            }
            if(System.currentTimeMillis() - lngTimer[1] > 10000){
                g2d.drawImage(biCountDown[2], 500, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(500, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((10000-(System.currentTimeMillis()-lngTimer[1]))/1000))), 517, -302);
            }
            if(System.currentTimeMillis() - lngTimer[3] > 3000){
                g2d.drawImage(biCountDown[0], 400, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(400, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((3000-(System.currentTimeMillis()-lngTimer[3]))/1000))), 417, -302);
            }
            //used to tell the player the cooldown times
        } else {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 32, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
            }    
            //responsible for drawing the player locally and over a network and also flips the sprite of the character   
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

    //methods used over a network or locally to determine what happens.
}
