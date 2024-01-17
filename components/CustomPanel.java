package components;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import objects.Brute;
import objects.GameObject;
import objects.Knight;
import objects.Sniper;
import objects.Wizard;

public class CustomPanel extends JPanel {

    private ResourceLoader resLoader = new ResourceLoader();
    private BufferedImage biTitle = resLoader.loadImage("/res\\Title.png");
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

    public void paintComponent(Graphics g){
        if(Main.state == Main.State.MAIN_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(biTitle, 0, 0, null);
        } else if(Main.state == Main.State.HOST_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.JOIN_MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.HELP) {
            g.setColor(new Color(36, 133, 151));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(187, 143, 19));
            g.drawString("help", 400, 300);
        } else if(Main.state == Main.State.CHARACTER){
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        } else if(Main.state == Main.State.GAME) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            // CAMERA START /////////////////////////////////////////////////////
            g.translate(getWidth()/2, getHeight()/2);

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
}
