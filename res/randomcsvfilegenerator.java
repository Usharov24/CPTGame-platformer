package res;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class randomcsvfilegenerator {
    public static void main(String[] args) {

        // Name of Generated File
        String csvFileName = "random_map.csv";
        
        // Dimensions of Map
        int intTilesX = 256;
        int intTilesY = 144;

        // Possible letters used in the CSV
        char[] letters = {'a', 'f', 'g', 'w'};
        char[][] data = new char[intTilesX][intTilesY];

        // Generate Random Data
        Random random = new Random();

        for (int intX = 0; intX < intTilesX; intX++) {
            for (int intY = 0; intY < intTilesY; intY++) {
                int randomIndex = random.nextInt(letters.length);
                data[intX][intY] = letters[randomIndex];
            }
        }

        // Write data to CSV file
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(csvFileName));
            for (int intY = 0; intY < intTilesY; intY++) {
                for (int intX = 0; intX < intTilesX; intX++) {
                    writer.print(data[intX][intY]);
                    writer.print(",");
                }
                writer.println();
            }

            System.out.println("Generated CSV File: " + csvFileName);
            writer.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}