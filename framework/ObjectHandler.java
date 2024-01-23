package framework;
import java.awt.Graphics;
import java.util.LinkedList;

import objects.GameObject;

public class ObjectHandler {

    public LinkedList<GameObject> objectList = new LinkedList<>();

    public void update() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);
            
            object.update();
        }
    }

    public void draw(Graphics g) {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() != ObjectId.PLAYER) object.draw(g);
        }

        for(int intCount = 0; intCount < 4; intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() == ObjectId.PLAYER) object.draw(g);
        }
    }

    public void clearEntities() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() != ObjectId.PLAYER && object.getId() != ObjectId.PERM_BARRIER) {
                objectList.remove(intCount);
                intCount = 0;
            }
        }
    }

    public void addObject(GameObject object) {
        objectList.add(object);
    }

    public void addObject(GameObject object, int intIndex) {
        objectList.add(intIndex, object);
    }

    public void removeObject(GameObject object) {
        objectList.remove(object);
    }

    public boolean containsObject(GameObject object) {
        return objectList.contains(object);
    }

    public GameObject getObject(int intIndex) {
        return objectList.get(intIndex);
    }
}
