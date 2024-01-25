package Framework;
import java.awt.Graphics;
import java.util.LinkedList;

import Objects.GameObject;

public class ObjectHandler {

    /**
     * Object list that stores all game objects
     */
    public LinkedList<GameObject> objectList = new LinkedList<>();
    //holds all of the GameObjects

    /**
     * Update method responsible for calling the update methods of all GameObjects
     */
    public void update() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);
            //updates all of the gameobjects in the game
            object.update();
        }
    }

    /**
     * The method responsible for calling the draw methods of all GameObjects
     * 
     * @param g The graphics context
     */
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

    /**
     * Method to clear out certain objects from the list
     */
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
    
    /**
     * Method to clear the entire list
     */
    public void clearList() {
        objectList.clear();
    }

    //removes everything from the list

    /**
     * Method to add an object to the list
     * 
     * @param object The GameObject to be added
     */
    public void addObject(GameObject object) {
        objectList.add(object);
    }

    //adds an object to the handler
    /**
     * Method to add and an object to the list
     * 
     * @param object The GameObject to be added
     * @param intIndex The index to add the GameObject at
     */
    public void addObject(GameObject object, int intIndex) {
        objectList.add(intIndex, object);
    }

    //allows customization for the index of the object
    /**
     * Method to remove an object from the list
     * 
     * @param object The GameObject to be removed
     */
    public void removeObject(GameObject object) {
        objectList.remove(object);
    }
    //removes an object

    /**
     * Method to check if the list contains an object
     * 
     * @param object The GameObject to check for
     * @return Returns a boolean value indicating whether the object is in the list
     */
    public boolean containsObject(GameObject object) {
        return objectList.contains(object);
    }

    //checks to see if the handler contains a specific object and returns a boolean

    /**
     * A method to retrieve an object from the list
     * 
     * @param intIndex The index of the object
     * @return Returns the retrieved object
     */
    public GameObject getObject(int intIndex) {
        return objectList.get(intIndex);
    }
    //gets an object from the handler by inputting the index
}
