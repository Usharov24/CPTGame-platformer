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

public class MapPanel extends JPanel{{
	try{
		List<List<String> > strMap = new ArrayList<>();
		String strFile = "res/map1.csv";
		FileReader fileread = new FileReader(strFile);
		BufferedReader reader = new BufferedReader(fileread);
			
		String strLine = reader.readLine();
		while(strLine != null){
			List<String> lineData = Arrays.asList(strLine.split(","));
			strMap.add(lineData);
			strLine = reader.readLine();
		}

		for(List<String> list : strMap){
			for(String str : list)
			System.out.print(str + " ");
			System.out.println();
		}
		reader.close();
    }catch(FileNotFoundException e){
        e.printStackTrace();
    }catch(IOException f){
        f.printStackTrace();
    }
}
    //protected void paintComponent(Graphics g){
        //g.setColor(new Color(0,0,0));
        //Main.handler.update();
        //int intTiles = 1000;
		//for(int intCount = 0; intCount < intTiles; intCount++){
			//if(strMap[intCount])
		//}
    //}

    public MapPanel() {
        super();
    }
}
