package components;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import objects.*;

public class CustomPanel extends JPanel {

    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage AbilityButtons = resLoader.loadImage("/res\\AbilityButtons.png");
    private BufferedImage[] biTileTextures = resLoader.loadSpriteSheet("/res\\TileTextures.png", 40, 40);
    private BufferedImage biTitleScreen = resLoader.loadImage("/res\\Title.png");
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
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Dialog", Font.BOLD, 18));
            g.drawImage(AbilityButtons, 30, 320, null);
            g.drawString("Annihilation Station is a combat-focused platformer game. Players navigate through rooms to proceed in the game. Watch out for enemies", 30, 200);
            g.drawString("in your way. There are many types of enemies, some are more difficult than others. Defeat enemies by using your player's ability which", 30, 220);
            g.drawString("may be activated through the 4 buttons show below. Along the way, there are also items scattered throughout. Items boost the players'", 30, 240);
            g.drawString("abilities to make it easier to navigate through the rooms. ", 30, 260);
        } else if(Main.state == Main.State.DEMO) {
            g.setColor(new Color(36, 133, 151));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.CHARACTER){
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.GAME) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            Main.handler.update();

            // CAMERA START /////////////////////////////////////////////////////
            g.translate(getWidth()/2, getHeight()/2);

            decodeMap(g);
            Main.handler.draw(g);

            g.translate(-getWidth()/2, -getHeight()/2);
            // CAMERA END ///////////////////////////////////////////////////////
            
            // HUD CODE HERE ////////////////////////////////////////////////////
            
            // HUD Variables
            ObjectHandler handler = Main.handler;
            int intBarWidth = 400;
            int intBarHeight = 20;
            float fltSniperHP = 0;
            float fltBruteHP = 0;
            float fltKnightHP = 0;
            float fltWizardHP = 0;
            float fltSniperMaxHP = 0;
            float fltBruteMaxHP = 0;
            float fltKnightMaxHP = 0;
            float fltWizardMaxHP = 0;

            // HP Getter
            for(int i = 0; i < handler.objectList.size(); i++){
                GameObject object = handler.getObject(i);
                if(object.getId() == ObjectId.PLAYER){

                    // Current HP
                    if(object instanceof Sniper) fltSniperHP = ((Sniper)object).getHP();
                    if(object instanceof Brute) fltBruteHP = ((Brute)object).getHP();
                    if(object instanceof Knight) fltKnightHP = ((Knight)object).getHP();
                    if(object instanceof Wizard) fltWizardHP = ((Wizard)object).getHP();

                    // Max HP
                    if(object instanceof Sniper) fltSniperMaxHP = ((Sniper)object).getMaxHP();
                    if(object instanceof Brute) fltBruteMaxHP = ((Brute)object).getMaxHP();
                    if(object instanceof Knight) fltKnightMaxHP = ((Knight)object).getMaxHP();
                    if(object instanceof Wizard) fltWizardMaxHP = ((Wizard)object).getMaxHP();

                    // Set Lower Bound of HP to 0
                    if(fltSniperHP <= 0) fltSniperHP = 0;
                    if(fltBruteHP <= 0) fltBruteHP = 0;
                    if(fltKnightHP <= 0) fltKnightHP = 0;
                    if(fltWizardHP <= 0) fltWizardHP = 0;
                }
            }

            // Health Bar
            g.setColor(Color.red);
            g.fillRect(100, 16, (int)(intBarWidth*fltSniperHP/fltSniperMaxHP), intBarHeight);
            g.fillRect(100, 46, (int)(intBarWidth*fltBruteHP/fltBruteMaxHP), intBarHeight);
            g.fillRect(100, 76, (int)(intBarWidth*fltKnightHP/fltKnightMaxHP), intBarHeight);
            g.fillRect(100, 106, (int)(intBarWidth*fltWizardHP/fltWizardMaxHP), intBarHeight);

            // Health Bar Background
            g.setColor(new Color(64, 64, 64));
            g.fillRect(100+(int)(intBarWidth*fltSniperHP/fltSniperMaxHP), 16, intBarWidth-(int)(intBarWidth*fltSniperHP/fltSniperMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltBruteHP/fltBruteMaxHP), 46, intBarWidth-(int)(intBarWidth*fltBruteHP/fltBruteMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltKnightHP/fltKnightMaxHP), 76, intBarWidth-(int)(intBarWidth*fltKnightHP/fltKnightMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltWizardHP/fltWizardMaxHP), 106, intBarWidth-(int)(intBarWidth*fltWizardHP/fltWizardMaxHP), intBarHeight);

            // Health Bar Labels
            g.setColor(Color.white);
            g.setFont(new Font("Dialog", Font.BOLD, 18));
            g.drawString("Sniper", 20, 32);
            g.drawString("Brute", 20, 62);
            g.drawString("Knight", 20, 92);
            g.drawString("Wizard", 20, 122);

            // Health Bar Borders
            for(int intBar = 0; intBar <= 3; intBar++){
                g.setColor(Color.black);
                g.drawRect(100, 16+30*intBar, intBarWidth, intBarHeight);
            }

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
                } else if(Main.intSessionId == 1 && bytTileType == 2) {
                    Main.handler.addObject(new Door(intCount2 * 40, intCount1 * 40, 40, 40, new BufferedImage[]{biTileTextures[bytTileTexture], biTileTextures[bytTileTexture + 6]}, ObjectId.DOOR, Main.handler, Main.ssm));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile - 2);
                }

                if(Main.intSessionId == 1 && bytSpawnObject == 1) {
                    Main.handler.addObject(new Enemy(intCount2 * 40, intCount1 * 40, 0, 0, 0, 0, bytSpawnInfo & 3, bytSpawnInfo >> 2 & 3, Main.handler.objectList.size(), ObjectId.ENEMY, Main.handler, Main.ssm));
                    Main.ssm.sendText("h>a>aENEMY~" + (intCount2 * 40) + "," + (intCount1 * 40) + "," + (bytSpawnInfo & 3) + "," + (bytSpawnInfo >> 2 & 3));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile & 255);
                } else if(Door.blnRoomCleared && bytSpawnObject == 2) {
                    Main.handler.addObject(new Item(intCount2 * 40, intCount1 * 40, 20, 20, ObjectId.ITEM, Main.handler, null));
                    strMap[Main.intRoomCount][intCount1][intCount2] = "" + (shrtTile & 255);
                }
            }
        }
    }
}
