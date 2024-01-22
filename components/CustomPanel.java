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
import objects.Barrier;
import objects.Brute;
import objects.Enemy;
import objects.GameObject;
import objects.Knight;
import objects.Sniper;
import objects.Wizard;

public class CustomPanel extends JPanel {
Color[] colors = {Color.blue, Color.red, Color.cyan, Color.pink, Color.white, Color.green, Color.magenta, Color.gray, Color.darkGray, Color.lightGray, Color.orange, new Color(34, 53, 122), new Color(65, 34, 255), new Color(87, 255, 245), new Color(255, 124, 64), new Color(98, 35, 123)};
    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage biTitleScreen = resLoader.loadImage("/res\\Title.png");
    private Font font = resLoader.loadFont("/res\\bitwise.ttf", 28);

    private BufferedImage[] biTileTextures = null;
    private String[][] strMap = resLoader.loadCSV("/res\\maptest.csv");

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
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            // CAMERA START /////////////////////////////////////////////////////
            g.translate(getWidth()/2, getHeight()/2);

            decodeMap(g);

            Main.handler.update();
            Main.handler.draw(g);

            g.translate(-getWidth()/2, -getHeight()/2);
            // CAMERA END ///////////////////////////////////////////////////////
            
            // HUD CODE HERE ////////////////////////////////////////////////////

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

            for(int i = 0; i < handler.objectList.size(); i++){
                GameObject object = handler.getObject(i);
                if(object.getId() == ObjectId.PLAYER){

                    if(object instanceof Sniper) fltSniperHP = ((Sniper)object).getHP();
                    if(object instanceof Brute) fltBruteHP = ((Brute)object).getHP();
                    if(object instanceof Knight) fltKnightHP = ((Knight)object).getHP();
                    if(object instanceof Wizard) fltWizardHP = ((Wizard)object).getHP();

                    if(object instanceof Sniper) fltSniperMaxHP = ((Sniper)object).getMaxHP();
                    if(object instanceof Brute) fltBruteMaxHP = ((Brute)object).getMaxHP();
                    if(object instanceof Knight) fltKnightMaxHP = ((Knight)object).getMaxHP();
                    if(object instanceof Wizard) fltWizardMaxHP = ((Wizard)object).getMaxHP();
                }
            }

            g.setColor(Color.red);
            g.fillRect(100, 16, (int)(intBarWidth*fltSniperHP/fltSniperMaxHP), intBarHeight);
            g.fillRect(100, 46, (int)(intBarWidth*fltBruteHP/fltBruteMaxHP), intBarHeight);
            g.fillRect(100, 76, (int)(intBarWidth*fltKnightHP/fltKnightMaxHP), intBarHeight);
            g.fillRect(100, 106, (int)(intBarWidth*fltWizardHP/fltWizardMaxHP), intBarHeight);

            g.setColor(new Color(64, 64, 64));
            g.fillRect(100+(int)(intBarWidth*fltSniperHP/fltSniperMaxHP), 16, intBarWidth-(int)(intBarWidth*fltSniperHP/fltSniperMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltBruteHP/fltBruteMaxHP), 46, intBarWidth-(int)(intBarWidth*fltBruteHP/fltBruteMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltKnightHP/fltKnightMaxHP), 76, intBarWidth-(int)(intBarWidth*fltKnightHP/fltKnightMaxHP), intBarHeight);
            g.fillRect(100+(int)(intBarWidth*fltWizardHP/fltWizardMaxHP), 106, intBarWidth-(int)(intBarWidth*fltWizardHP/fltWizardMaxHP), intBarHeight);

            g.setColor(Color.white);
            g.setFont(new Font("Dialog", Font.BOLD, 18));
            g.drawString("Sniper", 20, 32);
            g.drawString("Brute", 20, 62);
            g.drawString("Knight", 20, 92);
            g.drawString("Wizard", 20, 122);

            for(int intBar = 0; intBar <= 3; intBar++){
                g.setColor(Color.black);
                g.drawRect(100, 16+30*intBar, intBarWidth, intBarHeight);
            }

            // HUD CODE END ////////////////////////////////////////////////////
        }
    }

    private void decodeMap(Graphics g) {
        GameObject camObject = Main.handler.getObject(Main.intSessionId - 1);

        for(int intCount1 = 0; intCount1 < strMap.length; intCount1++) {
            for(int intCount2 = 0; intCount2 < strMap[0].length; intCount2++) {
                short shrtTile = Short.parseShort(strMap[intCount1][intCount2]);

                byte bytTileType = (byte)(shrtTile & 15);
                byte bytTileTexture = (byte)(shrtTile >> 4 & 15);
                byte bytSpawnObject = (byte)(shrtTile >> 8 & 15);
                byte bytSpawnInfo = (byte)(shrtTile >> 12 & 15);
                //System.out.println(bytTileType + " " + bytTileTexture + " " + bytSpawnObject + " " + bytSpawnInfo);
                
                if(bytTileType == 0) {
                    g.setColor(colors[bytTileTexture]);
                    g.fillRect((int)(intCount2 * 40 - camObject.getWorldX() - camObject.getWidth()/2), (int)(intCount1 * 40 - camObject.getWorldY() - camObject.getHeight()/2), 40, 40);
                    //g.drawImage(biTileTextures[bytTileTexture], intCount2 * 40, intCount1 * 40, null);
                } else if(bytTileType == 1) {
                    Main.handler.addObject(new Barrier(intCount2 * 40, intCount1 * 40, 40, 40, ObjectId.BARRIER, Main.handler, null));
                } else if(bytTileType == 2) {
                    // Add door object
                }

                if(bytSpawnObject == 1) {
                    Main.handler.addObject(new Enemy(intCount2 * 40, intCount1 * 40, 0, 0, 32, 64, bytSpawnInfo & 3, bytSpawnInfo >> 2 & 3, ObjectId.ENEMY, Main.handler, Main.ssm));
                } else if(bytSpawnObject == 2) {
                    // Add items
                }
            }
        }
    }
}
