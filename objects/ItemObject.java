package objects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;
import java.awt.Rectangle;

public class ItemObject extends GameObject {
    private BufferedImage biImg = null;
    private int intItem;
    private int intRarity = 0;
    private boolean blnFalling = true;
    private ResourceLoader resLoader = new ResourceLoader();
    private BufferedImage[] biCommonItems = resLoader.loadImages("/res\\AGoo.png", "/res\\Cow.png", "/res\\Wungoos.png", "/res\\GunPowder.png", "/res\\AlienLeg.png", "/res\\PointBrush.png", "/res\\chalk.png", "/res\\ACream.png", "/res\\MoonDust.png");
    private BufferedImage[] biRareItems = resLoader.loadImages("/res\\ConGrav.png", "/res\\Bornana.png", "/res\\Milk.png", "/res\\KneeCap.png", "/res\\ShotGun.png", "/res\\Slashes.png", "/res\\Fire.png");
    private BufferedImage[] biLegyItems = resLoader.loadImages("/res\\VampToes.png", "/res\\Magnet.png", "/res\\GGoo.png", "/res\\MHeart.png", "/res\\Celeb.png");

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, handler, ssm);
        this.id = ObjectId.ITEM;
        this.fltWidth = 20;
        this.fltHeight = 20;

        intItem = (int)Math.floor(Math.random() * 9 + 1); 
        if(intItem == 10){
            intItem = (int)Math.floor(Math.random() * 5 + 1);
            biImg = biLegyItems[intItem-1];

            intRarity = 1;
            
            
        } else if(intItem > 6 && intItem != 10){
            intItem = (int)Math.floor(Math.random() * 6 + 1);
            biImg = biRareItems[intItem-1];
            intRarity = 2;
            
        }
        else{
            intItem = (int)Math.floor(Math.random() * 8 + 1);
            biImg = biCommonItems[intItem-1];
            intRarity = 3;
        }

        camObject = handler.getObject(Main.intSessionId - 1);
        
    }

    public void update(){
        if(blnFalling){
            fltVelY =7;
            //lucky number :)
        }
        collisions();
        fltWorldY += fltVelY;
    }

    public int getRarity(){
        return this.intRarity;
    }

    public int getPlacement(){
        return this.intItem;
    }
    public void collisions(){
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds())) {
                    fltVelY = 0;
                    blnFalling = false;

                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds().intersects(object.getBounds())) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                    blnFalling = false;
                }
            }
        }
    }
    public void draw(Graphics g) {
        g.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
}
