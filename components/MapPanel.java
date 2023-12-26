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
	int intTilesX = 256;
	int intTilesY = 144;
	int intTileSize = 5;
	String strMap[][] = new String[intTilesX][intTilesY];
	{
		try{
			String strFile = "res/map1.csv";
			BufferedReader reader = new BufferedReader(new FileReader(strFile));
			String strLine = reader.readLine();
			String strSplit[];
			while(strLine != null){
				try{
					for(int intRow = 0; intRow < intTilesY; intRow++){
						for(int intColumn = 0; intColumn < intTilesX; intColumn++){
							strSplit = strLine.split(",");
							strMap[intColumn][intRow] = strSplit[intColumn];
							System.out.println(strMap[intColumn][intRow]+" -> ("+intColumn+", "+intRow+")");
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

						// Grass
						case "g":
							g.setColor(new Color(0, 128, 0));
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

    public MapPanel() {
        super();
    }
}
