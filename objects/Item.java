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
    private int intItem;
    private int intRarity = 0;
    private boolean blnFalling = true;
    private boolean blnActive = true;

    // Images
    private BufferedImage biImg = null;
    private ResourceLoader resLoader = new ResourceLoader();
    private BufferedImage[] biCommonItems = resLoader.loadImages("/res\\AGoo.png", "/res\\Cow.png", "/res\\Wungoos.png", "/res\\GunPowder.png", "/res\\AlienLeg.png", "/res\\PointBrush.png", "/res\\chalk.png", "/res\\ACream.png", "/res\\MoonDust.png");
    private BufferedImage[] biRareItems = resLoader.loadImages("/res\\ConGrav.png", "/res\\Bornana.png", "/res\\Milk.png", "/res\\KneeCap.png", "/res\\ShotGun.png", "/res\\Slashes.png", "/res\\Fire.png");
    private BufferedImage[] biLegyItems = resLoader.loadImages("/res\\VampToes.png", "/res\\Magnet.png", "/res\\GGoo.png", "/res\\MHeart.png", "/res\\Celeb.png");

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

    public int getRarity(){
     return this.intRarity;
    }

    public int getPlacement(){
     return this.intItem;
    }
    //used to get the specifics of the item
}