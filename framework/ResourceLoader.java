package framework;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ResourceLoader {
    
    public BufferedImage loadImage(String strPath) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        BufferedImage biImage = null;

        try(inputStream) {
            biImage = ImageIO.read(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return biImage;
    }

    public BufferedImage[] loadImages(String... strPaths) {
        BufferedImage[] biImages = new BufferedImage[strPaths.length];

        for(int intCount = 0; intCount < biImages.length; intCount++) {
            try(InputStream inputStream = getClass().getResourceAsStream(strPaths[intCount])) {
                biImages[intCount] = ImageIO.read(inputStream);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return biImages;
    }

    public BufferedImage[] loadSpriteSheet(String strPath, int intImgWidth, int intImgHeight) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        BufferedImage biSpriteSheet = null;

        try(inputStream) {
            biSpriteSheet = ImageIO.read(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        BufferedImage[] biImages = new BufferedImage[(biSpriteSheet.getWidth()/intImgWidth) * (biSpriteSheet.getHeight()/intImgHeight)];

        int intIndex = 0;
        for(int intCount1 = 0; intCount1 < biSpriteSheet.getHeight()/intImgHeight; intCount1++) {
            for(int intCount2 = 0; intCount2 < biSpriteSheet.getWidth()/intImgWidth; intCount2++) {
                biImages[intIndex] = biSpriteSheet.getSubimage(intImgWidth * intCount2, intImgHeight * intCount1, intImgWidth, intImgHeight);

                intIndex++;
            }
        }

        return biImages;
    }

    public BufferedImage[] loadSpriteSheet(String strPath, int intImgWidth, int intImgHeight, int intRemove) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        BufferedImage biSpriteSheet = null;

        try(inputStream) {
            biSpriteSheet = ImageIO.read(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        BufferedImage[] biSubimages = new BufferedImage[(biSpriteSheet.getWidth()/intImgWidth) * (biSpriteSheet.getHeight()/intImgHeight)];
        BufferedImage[] biImages = new BufferedImage[biSubimages.length - intRemove];

        int intIndex = 0;
        for(int intCount1 = 0; intCount1 < biSpriteSheet.getHeight()/intImgHeight; intCount1++) {
            for(int intCount2 = 0; intCount2 < biSpriteSheet.getWidth()/intImgWidth; intCount2++) {
                biSubimages[intIndex] = biSpriteSheet.getSubimage(intImgWidth * intCount2, intImgHeight * intCount1, intImgWidth, intImgHeight);

                intIndex++;
            }
        }

        for(int intCount = 0; intCount < biImages.length; intCount++) {
            biImages[intCount] = biSubimages[intCount];
        }

        return biImages;
    }

    public BufferedImage[][] loadSpriteSheet(String strPath, int intImgWidth, int intImgHeight, int intNumArrays, int intArrayLength) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        BufferedImage biSpriteSheet = null;
        
        try(inputStream) {
            biSpriteSheet = ImageIO.read(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        BufferedImage[][] biImages = new BufferedImage[intNumArrays][intArrayLength];

        int intIndex1 = 0, intIndex2 = 0;
        for(int intCount1 = 0; intCount1 < biSpriteSheet.getHeight()/intImgHeight; intCount1++) {
            for(int intCount2 = 0; intCount2 < biSpriteSheet.getWidth()/intImgWidth; intCount2++) {

                biImages[intIndex2][intIndex1] = biSpriteSheet.getSubimage(intImgWidth * intCount2, intImgHeight * intCount1, intImgWidth, intImgHeight);

                intIndex1++;
                if(intIndex1 == intArrayLength) {
                    intIndex1 = 0;
                    intIndex2++;
                }
            }
        }

        return biImages;
    }

    public String[][] loadCSV(String strPath) {
        InputStreamReader inputReader = new InputStreamReader(getClass().getResourceAsStream(strPath));
        BufferedReader br = new BufferedReader(inputReader);
        ArrayList<String[]> strLines = new ArrayList<>();
        String strLine = null;

        try(inputReader; br) {
            while((strLine = br.readLine()) != null) {
                strLines.add(strLine.split(","));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        String[][] strContents = new String[strLines.size()][strLines.get(0).length];

        for(int intCount = 0; intCount < strContents.length; intCount++) {
            strContents[intCount] = strLines.get(intCount);
        }

        return strContents;
    }

    public Font loadFont(String strPath) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        Font font = null;

        try(inputStream) {
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return font;
    }

    public Font loadFont(String strPath, float fltSize) {
        InputStream inputStream = getClass().getResourceAsStream(strPath);
        Font font = null;

        try(inputStream) {
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fltSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return font;
    }
}
