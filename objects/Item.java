package Objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Framework.SuperSocketMaster;

import java.awt.Rectangle;

public class Item extends GameObject {

    // Properties
     /**
    The int value for type of item
    **/
    private int intItem;
     /**
    The int value for rarity
    **/
    private int intRarity = 0;
     /**
    The boolean value for falling which starts off as true
    **/
    private boolean blnFalling = true;
     /**
    The boolean value for activity which starts off as true
    **/
    private boolean blnActive = true;

    // Images
    /**
    The BufferedImage which stores the item texturex
     **/
    private BufferedImage biImg = null;
    /**
    The loaderthat loads textures
     **/
    private ResourceLoader resLoader = new ResourceLoader();
    /**
    The BufferedImage which stores the common textures
     **/
    private BufferedImage[] biCommonItems = resLoader.loadImages("/res\\AGoo.png", "/res\\Cow.png", "/res\\Wungoos.png", "/res\\GunPowder.png", "/res\\AlienLeg.png", "/res\\PointBrush.png", "/res\\chalk.png", "/res\\ACream.png", "/res\\MoonDust.png");
    /**
    The BufferedImage which stores the rare textures
     **/
    private BufferedImage[] biRareItems = resLoader.loadImages("/res\\ConGrav.png", "/res\\Bornana.png", "/res\\Milk.png", "/res\\KneeCap.png", "/res\\ShotGun.png", "/res\\Slashes.png", "/res\\Fire.png");
    /**
    The BufferedImage which stores the legy texturex
     **/
    private BufferedImage[] biLegyItems = resLoader.loadImages("/res\\VampToes.png", "/res\\Magnet.png", "/res\\GGoo.png", "/res\\MHeart.png", "/res\\Celeb.png");
    /**
     * Constructor for the Knight
     * @param fltWorldX - float value for the X position of the knight
     * @param fltWorldY - float value for the Y position of the knight
     * @param fltVelX - float value for the velocity X
     * @param fltVelY - float value for the velocity Y
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param fltWidth - the width
     * @param fltHeight - the height
     */
    // Constructor
    public Item(float fltX, float fltY, float fltHeight, float fltWidth, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, handler, ssm);
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.id = id;

        intItem = (int)Math.floor(Math.random() * 9 + 1); 
        //randomly determines the rarity of the item
        if(intItem == 10) {
            intItem = (int)Math.floor(Math.random() * 5 + 1);
            biImg = biLegyItems[intItem-1];
            intRarity = 1;
            
            //when rarity has been discovered, randomly select an item from that rarity
        } else if(intItem > 6 && intItem != 10) {
            intItem = (int)Math.floor(Math.random() * 6 + 1);
            biImg = biRareItems[intItem-1];
            intRarity = 2;
            //when rarity has been discovered, randomly select an item from that rarity

            
        } else {
            intItem = (int)Math.floor(Math.random() * 8 + 1);
            biImg = biCommonItems[intItem-1];
            intRarity = 3;
            //when rarity has been discovered, randomly select an item from that rarity

        }

        camObject = handler.getObject(Main.intSessionId - 1);
        
    }

    // Handle Falling Items
    public void update(){
        if(!blnActive) return;

        if(blnFalling) {
            fltVelY = 7;
            //lucky number :)
        }

        collisions();
        //makes the item fall if needed
        fltWorldY += fltVelY;
    }

    // Collision Detection
    /**
     * @return void 
     * this checks for collisions
     */
    public void collisions(){
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds())) {
                    fltVelY = 0;
                    blnFalling = false;

                    fltWorldY = object.getWorldY() - fltHeight;
                }
                //does not pass through barriers
            }
        }
    }

    // Draw Items to Screen
    public void draw(Graphics g) {
        if(!blnActive) return;

        g.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        //draws the sprite of the image
    }

    public Rectangle getBounds() {
        if(blnActive) return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        else return new Rectangle();
        //if item is active, create its hitbox
    }
    /**
     * @return int 
     * this checks rarity
     */
    public int getRarity(){
     return this.intRarity;
    }
    /**
     * @return int 
     * this checks placement
     */
    public int getPlacement(){
     return this.intItem;
    }
    //used to get the specifics of the item
}