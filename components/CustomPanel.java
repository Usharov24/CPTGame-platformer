package Components;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import Framework.Main;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Objects.*;

public class CustomPanel extends JPanel {

    // Resource Loader
    private ResourceLoader resLoader = new ResourceLoader();

    // Load Images
    private BufferedImage[] biHelpMenuScreens = resLoader.loadSpriteSheet("/res\\HelpMenuScreens.png", 860, 720);
    private BufferedImage[] biTileTextures = resLoader.loadSpriteSheet("/res\\TileTextures.png", 40, 40);
    private BufferedImage biTitleScreen = resLoader.loadImage("/res\\Title.png");
    private BufferedImage biRoomBackground = resLoader.loadImage("/res\\RoomBackground.png");

    // Load Font
    private Font font = resLoader.loadFont("/res\\bitwise.ttf", 28);

    // Maps
    private String[][][] strMaps = new String[7][][];
    private String[][] strDemoMap = null;

    // Constructors
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

    // Display
    public void paintComponent(Graphics g) {
        if(Main.state == Main.State.MAIN_MENU) {
            g.drawImage(biTitleScreen, 0, 0, null);
            g.setColor(Color.white);
            g.setFont(font.deriveFont(100f));
            FontMetrics fm = g.getFontMetrics();
            g.drawString("Annihilation Station", (getWidth() - fm.stringWidth("Annihilation Station"))/2, 130);
            //draws out the main menu background and draws the strings
        } else if(Main.state == Main.State.HOST_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Username", 340, 370);
            g.drawString("Join Code", 340, 445);
            //draws more strings while also switching the panel to the host menu
        } else if(Main.state == Main.State.JOIN_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Username", 340, 370);
            g.drawString("Join Code", 340, 445);
            //switches the panel to the join menu and draws more strings
        } else if(Main.state == Main.State.HELP) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.drawImage(biHelpMenuScreens[Main.intHelpScreenCount], 210, 0, null);
            //switches the panel to the help panel and draws the correct background
        } else if(Main.state == Main.State.CHARACTER){
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            //ensures that the game is in the character selection screen
        } else if(Main.state == Main.State.GAME) {
            if(Main.intRoomCount == 0) {
                for(int intCount = 0; intCount < strMaps.length; intCount++) {
                    strMaps[intCount] = resLoader.loadCSV("/res\\Room1.csv,/res\\Room2.csv,/res\\Room3.csv,/res\\Room4.csv,/res\\Room5.csv,/res\\Room6.csv,/res\\Room7.csv".split(",")[intCount]);
                }
                Main.intRoomCount++;
                //switches the panel to the game screen and loads out the drawings from the CSV for levels
            }

            g.drawImage(biRoomBackground, 0, 0, null);
            //draws out the background placeholder img
            Main.handler.update();
            //updates all of the objects
            // CAMERA START /////////////////////////////////////////////////////
            g.translate(getWidth()/2, getHeight()/2);
            //helps make the player in the center and everything else move around it
            decodeMap(g, strMaps[Main.intRoomCount - 1]);
            //decodes each tile of the map
            Main.handler.draw(g);

            g.translate(-getWidth()/2, -getHeight()/2);
            // CAMERA END ///////////////////////////////////////////////////////
            
            // HUD CODE HERE ////////////////////////////////////////////////////
            for(int intCount = 0; intCount < 4; intCount++) {
                GameObject object = Main.handler.getObject(intCount);
                float fltPlayerHP = 0, fltPlayerMaxHP = 0;

                if(!(object instanceof Sniper) && !(object instanceof Brute) && !(object instanceof Knight) && !(object instanceof Wizard)) continue;
                //checks if there are instances of the player objects
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
                //determines the health of each player

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
                    //draws out the healthbars for each plaer
                } else {
                    g.setColor(Color.white);
                    g.setFont(font.deriveFont(15f));
                    
                    g.drawString(Main.strNameList[intCount], 10, (intCount < Main.intSessionId - 1) ? 75 + 30 * intCount : 75 + 30 * (intCount - 1));

                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), 175, 10);

                    g.setColor(new Color(126, 222, 80));
                    if(fltPlayerHP > 0) g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);

                    g.setColor(new Color(95, 173, 44));
                    if(fltPlayerHP > 0) g.drawRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);
                    //draws our health bars
                }
            }
            // HUD CODE END ////////////////////////////////////////////////////
        } else if(Main.state == Main.State.DEMO) {
            if(Main.intRoomCount == 0) {
                strDemoMap = resLoader.loadCSV("/res\\DemoRoom.csv");
                Main.intRoomCount++;
            }

            g.drawImage(biRoomBackground, 0, 0, null);

            Main.handler.update();
            //updates all objects in the demo
            g.translate(getWidth()/2, getHeight()/2);

            decodeMap(g, strDemoMap);
            Main.handler.draw(g);

            g.translate(-getWidth()/2, -getHeight()/2);

            GameObject object = Main.handler.getObject(0);
            float fltPlayerHP = 0, fltPlayerMaxHP = 0;

            if((object instanceof Sniper) || (object instanceof Brute) || (object instanceof Knight) || (object instanceof Wizard)) {
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
                    
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(10, 35, 325, 25);

                g.setColor(new Color(126, 222, 80));
                if(fltPlayerHP > 0) g.fillRect(10, 35, (int)(325 * (fltPlayerHP/fltPlayerMaxHP)), 25);

                g.setColor(new Color(95, 173, 44));
                if(fltPlayerHP > 0) g.drawRect(10, 35, (int)(325 * (fltPlayerHP/fltPlayerMaxHP)), 25);
            }
        }
    }

    // Draw Map to Screen
    private void decodeMap(Graphics g, String[][] strMap) {
        GameObject camObject = Main.handler.getObject(Main.intSessionId - 1);

        for(int intCount1 = 0; intCount1 < strMap.length; intCount1++) {
            for(int intCount2 = 0; intCount2 < strMap[0].length; intCount2++) {
                short shrtTile = Short.parseShort(strMap[intCount1][intCount2]);

                byte bytTileType = (byte)(shrtTile & 15);
                byte bytTileTexture = (byte)(shrtTile >> 4 & 15);
                byte bytSpawnObject = (byte)(shrtTile >> 8 & 15);
                byte bytSpawnInfo = (byte)(shrtTile >> 12 & 15);
                //responsible for holding all of the data for a tile
                if(bytTileType == 0) {
                    g.drawImage(biTileTextures[bytTileTexture], intCount2 * 40 - (int)(camObject.getWorldX() + camObject.getWidth()/2), intCount1 * 40 - (int)(camObject.getWorldY() + camObject.getHeight()/2), null);
                } else if(bytTileType == 1) {
                    Main.handler.addObject(new Barrier(intCount2 * 40, intCount1 * 40, 40, 40, biTileTextures[bytTileTexture], ObjectId.BARRIER, Main.handler, null));
                    strMap[intCount1][intCount2] = "" + (shrtTile - 1);
                } else if(bytTileType == 2) {
                    Main.handler.addObject(new Door(intCount2 * 40, intCount1 * 40, 40, 40, new BufferedImage[]{biTileTextures[bytTileTexture], biTileTextures[bytTileTexture + 6]}, ObjectId.DOOR, Main.handler, Main.ssm));
                    strMap[intCount1][intCount2] = "" + (shrtTile - 2);
                }
                //draws out certain tiles depending on the preceding information

                if(Main.intSessionId == 1 && bytSpawnObject == 1) {
                    if(Main.ssm != null) Main.ssm.sendText("h>a>aENEMY~" + (intCount2 * 40) + "," + (intCount1 * 40) + "," + (bytSpawnInfo & 3) + "," + (bytSpawnInfo >> 2 & 3) + "," + Main.handler.objectList.size());
                    Main.handler.addObject(new Enemy(intCount2 * 40, intCount1 * 40, 0, 0, 0, 0, bytSpawnInfo & 3, bytSpawnInfo >> 2 & 3, Main.handler.objectList.size(), ObjectId.ENEMY, Main.handler, Main.ssm));
                    //if the tile was determined to have an enemy, add an enemy and ensures that the enemy has the correct spawn information
                    strMap[intCount1][intCount2] = "" + (shrtTile & 255);
                    
                } else if(Door.blnRoomCleared && bytSpawnObject == 2) {
                    Main.handler.addObject(new Item(intCount2 * 40, intCount1 * 40, 20, 20, ObjectId.ITEM, Main.handler, null));
                    strMap[intCount1][intCount2] = "" + (shrtTile & 255);
                    //drops the item upon completion
                }
            }
        }
    }
}
