package objects;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.ObjectId;
import framework.SuperSocketMaster;

public abstract class ItemObject extends GameObject {

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, ObjectId id, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, ssm);
    }
}
