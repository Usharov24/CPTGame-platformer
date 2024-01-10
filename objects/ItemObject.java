package objects;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import framework.ObjectId;
import framework.SuperSocketMaster;
import java.awt.Rectangle;

public class ItemObject extends GameObject {
    private BufferedImage biImg = null;
    private int intRarity = 0;
    private int intItemId = 0;

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, int intRarity, ObjectId id, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, ssm);
        this.intRarity = intRarity;
        if(intRarity == 1){
            int item = (int)Math.floor(Math.random() * 4);

            if(item == 0){

            } 

            if(item == 1){

            }
            if(item == 2){
                
            }
            if(item == 3){
                
            }
            if(item == 4){
                
            }
            if(item == 5){
                
            }
        }
    }

    public void update(LinkedList<GameObject> objectList){
        
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)fltX,(int)fltY, null);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
