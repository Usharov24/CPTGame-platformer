package objects;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public abstract class ItemObject extends GameObject {

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, ObjectId id) {
        super(fltX, fltY, fltHeight, fltWidth, id);
    }

    public abstract void update(LinkedList<GameObject> objectList);
    
    public abstract void draw(Graphics g);
    
    public void networkreceive(){        
    }

}
