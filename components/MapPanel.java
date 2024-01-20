package components;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import framework.Main;

public class MapPanel extends JPanel{

	// Tile Configuration Variables
	private int intTilesX = 256;
	private int intTilesY = 144;
	private int intTileSize = 5;

	private int intRoomNumber = 2;
	String strMap[][] = new String[intTilesX][intTilesY];
	{
		try{
			String strFile[] = new String[]{"res/map0.csv", "res/map1.csv", "res/map2.csv", "res/map3.csv", "res/map4.csv", "res/map5.csv", "res/map6.csv", "res/map7.csv", "res/map8.csv", "res/map9.csv", "res/map10.csv", "res/map11.csv", "res/map12.csv", "res/map13.csv", "res/map14.csv", "res/map15.csv", "res/map16.csv", "res/map17.csv", "res/map18.csv", "res/map19.csv", "res/map20.csv", "res/map21.csv", "res/map22.csv", "res/map23.csv"};
			BufferedReader reader = new BufferedReader(new FileReader(strFile[intRoomNumber]));
			String strLine = reader.readLine();
			String strSplit[];
			while(strLine != null){
				try{
					for(int intY = 0; intY < intTilesY; intY++){
						for(int intX = 0; intX < intTilesX; intX++){
							strSplit = strLine.split(",");
							strMap[intX][intY] = strSplit[intX];
						}
						strLine = reader.readLine();
					}
				}catch(NullPointerException e){
					e.printStackTrace();
				}catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
				}
			}
			reader.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g){
        g.setColor(new Color(0,0,0));
		try{
			for(int intY = 0; intY < intTilesY; intY++){
				for(int intX = 0; intX < intTilesX; intX++){
					switch(strMap[intX][intY]){
						// Air
						case "a":
							g.setColor(new Color(0, 0, 0));
							g.fillRect((intX*intTileSize), (intY*intTileSize), intTileSize, intTileSize);
							break;

						// Fire
						case "f":
							g.setColor(new Color(128, 0, 0));
							g.fillRect((intX*intTileSize), (intY*intTileSize), intTileSize, intTileSize);
							break;

						// Grass
						case "g":
							g.setColor(new Color(0, 128, 0));
							g.fillRect((intX*intTileSize), (intY*intTileSize), intTileSize, intTileSize);
							break;

						// Water
						case "w":
							g.setColor(new Color(0, 0, 128));
							g.fillRect((intX*intTileSize), (intY*intTileSize), intTileSize, intTileSize);
							break;
					}
				}
			}
			Main.handler.update();
			Main.handler.draw(g);
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}

	public void setRoomNumber(Integer intRoomNumber){
		this.intRoomNumber = intRoomNumber;
	}

	public int getRoomNumber(){
		return this.intRoomNumber;
	}

    public MapPanel() {
        super();
    }
}
