package objects;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.image.BufferedImage;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;
import java.awt.Rectangle;
import java.io.*;

public class ItemObject extends GameObject {
    private BufferedImage biImg = null;
    private int intRed = 0;
    private int intBlue = 0;
    private int intGreen = 0;
    private String strItemId = "";
    private int intPlacement = 0;
    private FileReader itemList = null;

    private ResourceLoader resLoader = new ResourceLoader();
    private BufferedImage biItemPhoto = null;
    private BufferedImage[] biCommonItems = resLoader.loadImages("/res\\AggroGoo.png", "/res\\SpaceMeat.png", "/res\\Wungoos.png", "/res\\MutantPowder.png", "/res\\ALeg.png", "/res\\ToothBrush.png", "/res\\BChalk.png", "/res\\ACream.png", "/res\\MoonDust.png");
    private BufferedImage[] biRareItems = resLoader.loadImages("/res\\ConGravity.png", "/res\\Ornana.png", "/res\\Milk.png", "/res\\ECap.png", "/res\\Shells.png", "/res\\Slashes.png", "/res\\SunChunk.png");
    private BufferedImage[] biLegyItems = resLoader.loadImages("/res\\VampToes.png", "/res\\MagneticE.png", "/res\\GreenGoo.png", "/res\\MHeart.png", "/res\\CelebShot.png");

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, int intRarity, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, handler, ssm);

        int intItem = (int)Math.floor(Math.random() * 9 + 1);
        if(intItem == 10){
            intRed = 255;
            intItem = (int)Math.floor(Math.random() * 5 + 1);
            biItemPhoto = biLegyItems[intItem-1];
            switch(intItem) {
                case 1: 
                    strItemId = "Vampire Toes";
                break;

                case 2: 
                    strItemId = "Magnetic Elyatazers";
                break;

                case 3:
                    strItemId = "Greener Goo";
                break;

                case 4: 
                    strItemId = "Monster Heart";
                break;

                case 5: 
                    strItemId = "Celabratory Shot";
                break;
            }
        } else if(intItem > 6 && intItem != 10){
            intItem = (int)Math.floor(Math.random() * 6 + 1);
            biItemPhoto = biRareItems[intItem-1];
            intBlue = 255;
            switch(intItem) {
                case 1: 
                    strItemId = "Condensed Gravity";
                break;

                case 2: 
                    strItemId = "Ornana";
                break;

                case 3:
                    strItemId = "Milk";
                break;

                case 4: 
                    strItemId = "Extra Kneecap";
                break;

                case 5: 
                    strItemId = "ShotGun Shells";
                break;

                case 6: 
                    strItemId = "Extra Slashes";
                break;

                case 7: 
                    strItemId = "Chunk of Sun";
                break;
            }
        }

        intGreen = 255;
        intItem = (int)Math.floor(Math.random() * 8 + 1);
        biItemPhoto = biCommonItems[intItem-1];
        switch(intItem) {
        case 1: 
            strItemId = "Aggressive Goo";
            break;
        case 2: 
            strItemId = "Space Cow Meat";
            break;
        case 3:
            strItemId = "Wungoos";
            break;
        case 4: 
            strItemId = "Mutant Gunpowder";
            break;
        case 5: 
            strItemId = "Alien Leg";
            break;
        case 6: 
            strItemId = "Pointy Toothbrush";
            break;
        case 7: 
            strItemId = "Bullet Chalk";
            break;
        case 8: 
            strItemId = "Alien Cream";
            break;
        case 9: 
            strItemId = "Moon Dust";
            break;
        } 
    }

    public void update(LinkedList<GameObject> objectList){
        
    }

    public String getItemId(){
        return this.strItemId;
    }

    public void draw(Graphics g) {
        g.drawRect((int)fltWorldX, (int)fltWorldY, 100, 100);
        g.drawImage(biImg, (int)fltWorldX,(int)fltWorldY, null);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }
}
