package objects;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.ObjectId;

public abstract class ItemObject extends GameObject {

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, ObjectId id) {
        super(fltX, fltY, fltHeight, fltWidth, id);
    }
}
