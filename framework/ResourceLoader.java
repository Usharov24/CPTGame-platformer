package framework;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResourceLoader {
    
    public BufferedImage loadImage(String strPath) {
        BufferedImage biImage = null;

        try {
            biImage = ImageIO.read(getClass().getResourceAsStream(strPath));
        } catch(IOException e) {
            e.printStackTrace();
        }

        return biImage;
    }

    public BufferedImage[] loadImages(String... strPaths) {
        BufferedImage[] biImages = new BufferedImage[strPaths.length];

        for(int intCount = 0; intCount < biImages.length; intCount++) {
            try {
                biImages[intCount] = ImageIO.read(getClass().getResourceAsStream(strPaths[intCount]));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return biImages;
    }

    public BufferedImage[] loadSpriteSheet(String strPath, int intImgWidth, int intImgHeight) {
        BufferedImage biSpriteSheet = null;

        try {
            biSpriteSheet = ImageIO.read(getClass().getResourceAsStream(strPath));
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
        BufferedImage biSpriteSheet = null;

        try {
            biSpriteSheet = ImageIO.read(getClass().getResourceAsStream(strPath));
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

    public Font loadFont(String strPath) {
        Font font = null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(strPath)).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return font;
    }

    public Font loadFont(String strPath, float fltSize) {
        Font font = null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(strPath)).deriveFont(fltSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return font;
    }
}
