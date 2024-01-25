package Framework;
import java.awt.Graphics;
import java.util.LinkedList;

import Objects.GameObject;

public class ObjectHandler {

    public LinkedList<GameObject> objectList = new LinkedList<>();
    //holds all of the game objects

    public void update() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);
            //updates all of the gameobjects in the game
            object.update();
        }
    }

    public void draw(Graphics g) {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() != ObjectId.PLAYER) object.draw(g);
        }
        //draws non-player objects

        for(int intCount = 0; intCount < 4; intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() == ObjectId.PLAYER) object.draw(g);
        }
        //draws players
    }

    public void clearEntities() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);

            if(object.getId() != ObjectId.PLAYER && object.getId() != ObjectId.PERM_BARRIER) {
                objectList.remove(intCount);
                intCount = 0;
            }
            //clears all entities except players and permanent barriers
        }
    }

    public void clearList() {
        objectList.clear();
    }

    //removes everything from the list

    public void addObject(GameObject object) {
        objectList.add(object);
    }

    //adds an object to the handler

    public void addObject(GameObject object, int intIndex) {
        objectList.add(intIndex, object);
    }

    //allows customization for the index of the object

    public void removeObject(GameObject object) {
        objectList.remove(object);
    }
    //removes an object

    public boolean containsObject(GameObject object) {
        return objectList.contains(object);
    }

    //checks to see if the handler contains a specific object and returns a boolean

    public GameObject getObject(int intIndex) {
        return objectList.get(intIndex);
    }
    //gets an object from the handler by inputting the index
}
