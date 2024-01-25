package Components;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JPanel;

import Framework.Main;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Objects.*;

public class CustomPanel extends JPanel {

    // Resource Loader
    /**
     * The ResourceLoader object used to load resources
     */
    private ResourceLoader resLoader = new ResourceLoader();

    // Load Images
    /**
     * The images used as the help menu screens
     */
    private BufferedImage[] biHelpMenuScreens = resLoader.loadSpriteSheet("/res/HelpMenuScreens.png", 860, 720);
    /**
     * The images used as tile textures
     */
    private BufferedImage[] biTileTextures = resLoader.loadSpriteSheet("/res/TileTextures.png", 40, 40);
    /**
     * The image used for the title screen
     */
    private BufferedImage biTitleScreen = resLoader.loadImage("/res/Title.png");
    /**
     * The image used for the background of levels
     */
    private BufferedImage biRoomBackground = resLoader.loadImage("/res/RoomBackground.png");

    // Load Font
    /**
     * The Font object used for drawing text
     */
    private Font font = resLoader.loadFont("/res/bitwise.ttf", 28);

    // Maps
    /**
     * The array used to store the map data for all the levels
     */
    private String[][][] strMaps = new String[7][][];
    /**
     * The array used to store the data for the demo level
     */
    private String[][] strDemoMap = null;

    // Constructors
    /**
     * The default constructor
     */
    public CustomPanel() {
        super();
    }

    /**
     * The constructor used to set the layout of the panel
     * 
     * @param layout The LayoutManager object
     */
    public CustomPanel(LayoutManager layout) {
        super(layout);
    }

    /**
     * The constructor used to set the layout of the panel and make it focusable
     * 
     * @param layout The LayoutManager object
     * @param blnFocusable Boolean to determine if the panel is focusable
     */
    public CustomPanel(LayoutManager layout, boolean blnFocusable) {
        super(layout);
        setFocusable(blnFocusable);
    }

    // Display
    /**
     * Overridden paint method of the panel
     * Used to perform all graphics related tasks
     */
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
            if(Main.intRoomCount != 8 && Main.intAlivePlayers[0] + Main.intAlivePlayers[1] + Main.intAlivePlayers[2] + Main.intAlivePlayers[3] != Main.intServerSize) {
                if(Main.intRoomCount == 0) {
                    for(int intCount = 0; intCount < strMaps.length; intCount++) {
                        strMaps[intCount] = resLoader.loadCSV("/res/Room1.csv,/res/Room2.csv,/res/Room3.csv,/res/Room4.csv,/res/Room5.csv,/res/Room6.csv,/res/Room7.csv".split(",")[intCount]);
                    }
                    Main.intRoomCount++;
                }

                g.drawImage(biRoomBackground, 0, 0, null);

                Main.handler.update();

                // CAMERA START /////////////////////////////////////////////////////
                g.translate(getWidth()/2, getHeight()/2);

                if(Main.intRoomCount < 8) decodeMap(g, strMaps[Main.intRoomCount - 1]);
                Main.handler.draw(g);

                g.translate(-getWidth()/2, -getHeight()/2);
                // CAMERA END ///////////////////////////////////////////////////////
            
                // HUD CODE HERE ////////////////////////////////////////////////////
                for(int intCount = 0; intCount < 4; intCount++) {
                    GameObject object = Main.handler.getObject(intCount);
                    float fltPlayerHP = 0, fltPlayerMaxHP = 0;

                    if(!(object instanceof Sniper) && !(object instanceof Brute) && !(object instanceof Knight) && !(object instanceof Wizard)) continue;

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
                        g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), 175, 10);

                        g.setColor(new Color(126, 222, 80));
                        if(fltPlayerHP > 0) g.fillRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);

                        g.setColor(new Color(95, 173, 44));
                        if(fltPlayerHP > 0) g.drawRect(10, (intCount < Main.intSessionId - 1) ? 80 + 30 * intCount : 80 + 30 * (intCount - 1), (int)(175 * (fltPlayerHP/fltPlayerMaxHP)), 10);
                    }
                }
            } else if(Main.intAlivePlayers[0] + Main.intAlivePlayers[1] + Main.intAlivePlayers[2] + Main.intAlivePlayers[3] == Main.intServerSize) {
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.white);
                g.setFont(font.deriveFont(100f));
                FontMetrics fm = g.getFontMetrics();
                g.drawString("You've Been Annihilated!", (getWidth() - fm.stringWidth("You've Been Annihilated!"))/2, 130);

                g.setFont(font.deriveFont(40f));
                fm = g.getFontMetrics();
                g.drawString("Better luck next time!", (getWidth() - fm.stringWidth("Better luck next time!"))/2, 210);
            } else {
                long lngTimeToCompleteMin = 0;
                long lngTimeToCompleteSec = 0;

                if(Main.lngRunEndTime == 0) {
                    Main.lngRunEndTime = System.currentTimeMillis();

                    lngTimeToCompleteMin = (int)(Main.lngRunEndTime - Main.lngRunStartTime)/60000;
                    lngTimeToCompleteSec = (int)((Main.lngRunEndTime - Main.lngRunStartTime) % 60000)/1000;

                    try(BufferedWriter bw = new BufferedWriter(new FileWriter("highscores.csv", true))) {
                        bw.write(Main.strNameList[Main.intSessionId - 1] + "," + Long.toString((Main.lngRunEndTime - Main.lngRunStartTime)/1000));
                        bw.newLine();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                //determines the health of each player

                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.white);
                g.setFont(font.deriveFont(100f));
                FontMetrics fm = g.getFontMetrics();
                g.drawString("You Escaped!", (getWidth() - fm.stringWidth("You Escaped!"))/2, 130);

                g.setFont(font.deriveFont(40f));
                fm = g.getFontMetrics();
                g.drawString("Time taken to complete: " + Long.toString(lngTimeToCompleteMin) + "min " + Long.toString(lngTimeToCompleteSec) + "sec", (getWidth() - fm.stringWidth("Time taken to complete: " + Long.toString(lngTimeToCompleteMin) + "min " + Long.toString(lngTimeToCompleteSec) + "sec"))/2, 210);
                g.drawString("Leaderboard", (getWidth() - fm.stringWidth("Leaderboard"))/2, 300);

                g.setFont(font.deriveFont(25f));
                fm = g.getFontMetrics();
                g.drawString("Rank        Name        Time to Complete", (getWidth() - fm.stringWidth("Rank        Name        Time to Complete"))/2, 350);

                try(BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("highscores.csv")))) {
                    ArrayList<ArrayList<String>> strContents = new ArrayList<>();
                    String[][] strScores = null;

                    String strLine = null;
                    int intIndex = 0;

                    while((strLine = br.readLine()) != null) {
                        strContents.add(new ArrayList<String>());

                        for(int intCount = 0; intCount < strLine.split(",").length; intCount++) {
                            strContents.get(intIndex).add(strLine.split(",")[intCount]);
                        }

                        intIndex++;
                    }

                    strScores = new String[strContents.size()][strContents.get(0).size()];

                    for(int intCount1 = 0; intCount1 < strScores.length; intCount1++) {
                        for(int intCount2 = 0; intCount2 < strScores[0].length; intCount2++) {
                            strScores[intCount1][intCount2] = strContents.get(intCount1).get(intCount2);
                        }
                    }
                    
                    String[] strTemp;
                    boolean blnSwapped;
                    for(int intCount1 = 0; intCount1 < strScores.length - 1; intCount1++) {
                        blnSwapped = false;

                        for(int intCount2 = 0; intCount2 < strScores.length - intCount1 - 1; intCount2++) {
                            if(Integer.parseInt(strScores[intCount2][1]) < Integer.parseInt(strScores[intCount1 + 1][1])) {
                                strTemp = strScores[intCount2];
                                strScores[intCount2] = strScores[intCount1 + 1];
                                strScores[intCount2 + 1] = strTemp;
                                blnSwapped = true;
                            }
                        }

                        if(!blnSwapped) break;
                    }

                    intIndex = 0;
                    for(int intCount1 = 0; intCount1 < strScores.length; intCount1++) {
                        for(int intCount2 = 0; intCount2 < strScores[0].length; intCount2++) {
                            g.drawString(strScores[intCount1][intCount2], 416 + 100 * intCount2, 385 + 40 * intCount1);
                        }
                    }

                    for(int intCount = 0; intCount < 5; intCount++) {
                        g.drawString(Integer.toString(intCount + 1), 416, 385 + 40 * intCount);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(NullPointerException e) {
                    g.drawString("No saved scores", (getWidth() - fm.stringWidth("No saved scores"))/2, 400);
                }
            }
            // HUD CODE END ////////////////////////////////////////////////////
        } else if(Main.state == Main.State.DEMO) {
            if(Main.intRoomCount == 0) {
                strDemoMap = resLoader.loadCSV("/res/DemoRoom.csv");
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
    /**
     * Method used to decode the map data from CSV files
     * 
     * @param g The graphics context
     * @param strMap The map data to be decoded and processed
     */
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
