package objects;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import framework.ObjectId;
import framework.SuperSocketMaster;
import java.awt.Rectangle;
import java.io.*;

public class ItemObject extends GameObject {
    private BufferedImage biImg = null;
    private int intRarity = 0;
    private String strItemId = "";
    private FileReader itemList = null;

    public ItemObject(float fltX, float fltY, float fltHeight, float fltWidth, int intRarity, ObjectId id, SuperSocketMaster ssm) {
        super(fltX, fltY, fltHeight, fltWidth, id, ssm);
        int item = (int)Math.floor(Math.random() * 9 + 1);
        if(item == 10){
            this.intRarity = 3;
            try(BufferedReader brReader = new BufferedReader(new FileReader("Legy.csv"))) {

                int i = (int)Math.floor(Math.random() * 5 + 1);
                for(int intcount = 0; i > intcount; intcount++){
                    brReader.readLine();
                }
                strItemId = brReader.readLine();
                
            }
            catch(Exception e){
                System.out.println("file not found");
            }
        }

        else if(item > 6 && item != 10){
            this.intRarity = 2;
            try(BufferedReader brReader = new BufferedReader(new FileReader("Legy.csv"))) {

                int i = (int)Math.floor(Math.random() * 5 + 1);
                for(int intcount = 0; i > intcount; intcount++){
                    brReader.readLine();
                }
                strItemId = brReader.readLine();
                
            }
            catch(Exception e){
                System.out.println("file not found");
            }        
        }

        else {
            this.intRarity = 1;
            try{
                FileReader itemList = new FileReader("Common.csv");
                if(strItemId.equals("Aggressive Goo")){
                    
                }
                if(strItemId.equals("Space Cow Meat")){
                    
                }
                if(strItemId.equals("Wungoos")){
                    
                }
                if(strItemId.equals("Mutant Gunpowder")){
                    
                }

                if(strItemId.equals("Alien Leg")){
                    
                }

                if(strItemId.equals("Pointy Toothbrush")){
                    
                }

                if(strItemId.equals("Bullet Chalk")){
                    
                }

                if(strItemId.equals("Alien Cream")){
                    
                }

                if(strItemId.equals("Moon Dust")){
                    
                }
            }
            catch(Exception e){
                System.out.println("file not found");
            }
        }   
        

        if(intRarity == 1){
            item = (int)Math.floor(Math.random() * 4);

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

    public String getItemId(){
        return this.strItemId;
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)fltWorldX,(int)fltWorldY, null);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }
}
