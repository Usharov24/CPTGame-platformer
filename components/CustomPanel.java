package components;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import framework.Main;
import framework.ObjectId;
import framework.ResourceLoader;
import objects.*;

public class CustomPanel extends JPanel {

    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage[] biTileTextures = resLoader.loadSpriteSheet("/res\\TileTextures.png", 40, 40);
    private BufferedImage biTitleScreen = resLoader.loadImage("/res\\Title.png");
    private BufferedImage biRoomBackground = resLoader.loadImage("/res\\RoomBackground.png");
    private Font font = resLoader.loadFont("/res\\bitwise.ttf", 28);

    private String[][][] strMap = {resLoader.loadCSV("/res\\room1.csv"), resLoader.loadCSV("/res\\room2.csv")};

    public CustomPanel() {
        super();
    }

    public CustomPanel(LayoutManager layout) {
        super(layout);
    }

    public CustomPanel(LayoutManager layout, boolean blnFocusable) {
        super(layout);
        setFocusable(blnFocusable);
    }

    public void paintComponent(Graphics g) {
        if(Main.state == Main.State.MAIN_MENU) {
            g.drawImage(biTitleScreen, 0, 0, null);
            g.setColor(Color.white);
            g.setFont(font.deriveFont(100f));
            FontMetrics fm = g.getFontMetrics();
            g.drawString("Annihilation Station", (getWidth() - fm.stringWidth("Annihilation Station"))/2, 130);
        } else if(Main.state == Main.State.HOST_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Username", 340, 370);
            g.drawString("Join Code", 340, 445);
        } else if(Main.state == Main.State.JOIN_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Username", 340, 370);
            g.drawString("Join Code", 340, 445);
        } else if(Main.state == Main.State.HELP) {
            g.setColor(new Color(36, 133, 151));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(187, 143, 19));
            g.drawString("Annihilation Station is a platformer game that focuses on combat. Players navigate through rooms to proceed in the game. Watch out for enemies that will be in your way. There are many types of enemies, some are more difficult than others.", 30, 300);
            g.drawString("Defeat enemies by using your player's ability which may be activated through the 4 buttons show below. Along the way, there are also items scattered throughout. Items boost the players' abilities to make it easier to navigate through the rooms. ", 30, 320);
        } else if(Main.state == Main.State.CHARACTER){
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.GAME) {
            g.drawImage(biRoomBackground, 0, 0, null);

            Main.handler.update();

            // CAMERA START /////////////////////////////////////////////////////
            g.translate(getWidth()/2, getHeight()/2);

            decodeMap(g);
            Main.handler.draw(g);

            g.translate(-getWidth()/2, -getHeight()/2);
            // CAMERA END ///////////////////////////////////////////////////////
            
            // HUD CODE HERE ////////////////////////////////////////////////////
            for(int intCount = 0; intCount < Main.intServerSize; intCount++) {
                GameObject object = Main.handler.getObject(intCount);
                float fltPlayerHP = 0, fltPlayerMaxHP = 0;

                if(object instanceof Sniper) {
                    fltPlayerHP = ((Sniper)object).getHP();
                    fltPlayerMaxHP = ((Sniper)object).getMaxHP();
                } else if(object instanceof Brute) {
                    fltPlayerHP = ((Brute)object).getHP();
                    fltPlayerMaxHP = ((Brute)object).getMaxHP();
                } else if(object instanceof Knight) {
                    fltPlayerHP = ((Knight)object).getHP();
                    fltPlayerMaxHP = ((Knight)object).getMaxHP();
                } else if(object instanceof Wizard) {
                    fltPlayerHP = ((Wizard)object).getHP();
                    fltPlayerMaxHP = ((Wizard)object).getMaxHP();
                }

                if(intCount == Main.intSessionId - 1) {
                    g.setColor(Color.white);
                    g.setFont(font.deriveFont(20f));
                    g.drawString(Main.strNameList[intCount], 10, 25);
                    
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(10, 35, 325, 25);

                    g.setColor(new Color(126, 222, 80));
                    if(fltPlayerHP > 0) g.fillRect(10, 35, (int)(325 * (fltPlayerHP/fltPlayerMaxHP)), 25);

                    g.setColor(new Color(95, 173, 44));
                    if(fltPlayerHP > 0) g.drawRect(10, 35, (int)(325 * (fltPlayerHP/fltPlayerMaxHP)), 25);
                } else {
                    g.setColor(Color.white);
                    g.setFont(font.deriveFont(15f));
                    
                    g.drawString(Main.strNameList[intCount], 10, (intCount < Main.intSessionId - 1) ? 75 + 30 * intCount : 75 + 30 * (intCount - 1));

                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 75 + 30 * (intCount - 1), 175, 10);

                    g.setColor(new Color(126, 222, 80));
                    if(fltPlayerHP > 0) g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 75 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);

                    g.setColor(new Color(95, 173, 44));
                    if(fltPlayerHP > 0) g.drawRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 75 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);
                }
            }

            /*g.setFont(font.deriveFont(75));
            g.drawString("Other2", 10, 105);
            g.drawString("Other3", 10, 135);

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(10, 80, 175, 10);
            g.fillRect(10, 110, 175, 10);
            g.fillRect(10, 140, 175, 10);

            g.setColor(new Color(126, 222, 80));
            g.fillRect(10, 80, 175, 10);
            g.fillRect(10, 110, 175, 10);
            g.fillRect(10, 140, 175, 10);

            g.setColor(new Color(95, 173, 44));
            g.drawRect(10, 80, 175, 10);
            g.drawRect(10, 110, 175, 10);
            g.drawRect(10, 140, 175, 10);*/
            
            // HUD CODE END ////////////////////////////////////////////////////
        }
    }

    private void decodeMap(Graphics g) {
        GameObject camObject = Main.handler.getObject(Main.intSessionId - 1);

        for(int intCount1 = 0; intCount1 < strMap[0].length; intCount1++) {
            for(int intCount2 = 0; intCount2 < strMap[0][0].length; intCount2++) {
                short shrtTile = Short.parseShort(strMap[Main.intRoomCount][intCount1][intCount2]);

                byte bytTileType = (byte)(shrtTile & 15);
                byte bytTileTexture = (byte)(shrtTile >> 4 & 15);
                byte bytSpawnObject = (byte)(shrtTile >> 8 & 15);
                byte bytSpawnInfo = (byte)(shrtTile >> 12 & 15);
                //System.out.println(bytTileType + " " + bytTileTexture + " " + bytSpawnObject + " " + bytSpawnInfo);

                if(bytTileType == 0) {
                    g.drawImage(biTileTextures[bytTileTexture], intCount2 * 40 - (int)(camObject.getWorldX() + camObject.getWidth()/2), intCount1 * 40 - (int)(camObject.getWorldY() + camObject.getHeight()/2), null);
                } else if(bytTileType == 1) {
                    Main.handler.addObject(new Barrier(intCount2 * 40, intCount1 * 40, 40, 40, biTileTextures[bytTileTexture], ObjectId.BARRIER, Main.handler, null));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile - 1);
                } else if(bytTileType == 2) {
                    Main.handler.addObject(new Door(intCount2 * 40, intCount1 * 40, 40, 40, new BufferedImage[]{biTileTextures[bytTileTexture], biTileTextures[bytTileTexture + 6]}, ObjectId.DOOR, Main.handler, Main.ssm));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile - 2);
                }

                if(Main.intSessionId == 1 && bytSpawnObject == 1) {
                    Main.ssm.sendText("h>a>aENEMY~" + (intCount2 * 40) + "," + (intCount1 * 40) + "," + (bytSpawnInfo & 3) + "," + (bytSpawnInfo >> 2 & 3) + "," + Main.handler.objectList.size());
                    Main.handler.addObject(new Enemy(intCount2 * 40, intCount1 * 40, 0, 0, 0, 0, bytSpawnInfo & 3, bytSpawnInfo >> 2 & 3, Main.handler.objectList.size(), ObjectId.ENEMY, Main.handler, Main.ssm));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile & 255);
                } else if(Door.blnRoomCleared && bytSpawnObject == 2) {
                    Main.handler.addObject(new Item(intCount2 * 40, intCount1 * 40, 20, 20, ObjectId.ITEM, Main.handler, null));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile & 255);
                }
            }
        }
    }
}
