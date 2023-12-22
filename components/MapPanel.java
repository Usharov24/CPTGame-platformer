package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.JPanel;

import framework.Main;

public class MapPanel extends JPanel{
	int intNumRows = 25;
	int intNumColumns = 40;
	String strMap[][] = new String[40][25];
	{
		try{
			String strFile = "res/map1.csv";
			FileReader fileread = new FileReader(strFile);
			BufferedReader reader = new BufferedReader(fileread);
			String strLine = reader.readLine();
			String strSplit[];
			while(strLine != null){
				for(int intRow = 0; intRow < intNumRows; intRow++){
					strLine = reader.readLine();
					try{
						strSplit = strLine.split(",");
						for(int intColumn = 0; intColumn < intNumColumns; intColumn++){
							strMap[intColumn][intRow] = strSplit[intColumn];
						}
					}catch(NullPointerException e){
						e.printStackTrace();
					}catch(ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
					}
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
		int intWidth = (int)Math.round(1280.0/intNumColumns);
		int intHeight = (int)Math.round(720.0/intNumRows);
		try{
			for(int intRow = 0; intRow < intNumRows; intRow++){
				for(int intColumn = 0; intColumn < intNumColumns; intColumn++){
					if(strMap[intColumn][intRow].equals("a")){
					}
					if(strMap[intColumn][intRow].equals("g")){
						g.setColor(new Color(0, 128, 0));
						g.fillRect((intColumn*intWidth), (intRow*intHeight), intWidth, intHeight);
					}
				}
			}
			Main.handler.update();
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
