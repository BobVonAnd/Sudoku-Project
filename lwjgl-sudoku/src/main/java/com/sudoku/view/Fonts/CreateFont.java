package com.sudoku.view.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class CreateFont {
    

    public String filePath;
    public int fontSize;
    public Map<Character, CharInfo> charMap;
    //public Map<Integer, CharInfo> charMap;

  public char[] atlas = {
    // lowercase
    'a','b','c','d','e','f','g','h','i','j','k','l','m',
    'n','o','p','q','r','s','t','u','v','w','x','y','z',

    // uppercase
    'A','B','C','D','E','F','G','H','I','J','K','L','M',
    'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',

    // danish
    'æ','ø','å','Æ','Ø','Å',

    // numbers
    '0','1','2','3','4','5','6','7','8','9',

    // symbols
    '!', '"', '(', ')', '&', '%',

    // ekstra (meget brugt)
    ' ', '.', ',', ':', ';', '?', '-', '_', '+', '=', '/'
};

    

    public CreateFont(String filePath, int fontSize){
        this.filePath = filePath;
        this.fontSize = fontSize;
        this.charMap = new HashMap<>();
        createBitMap();
    }

    //Finds the size x,y of the bitmap and inserts all char that is displayable.
    public void createBitMap(){
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font(filePath, Font.PLAIN, fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();


        //estimate the size of bitmap
        //we want the bitmap to be squard 
        //int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * fontSize + 1;
        int estimatedWidth = (int)Math.sqrt(atlas.length) * fontSize + 1;

        int height = fm.getHeight();
        int width = 0;
        int x = 0;
        int y = (int)(fm.getHeight());

        //all chars
        // for(int i = 0; i < font.getNumGlyphs(); i++){
        //     if(font.canDisplay(i)){
        //         CharInfo charInfo = new CharInfo(x,y,fm.charWidth(i),fm.getHeight());
        //         charMap.put(i, charInfo);
        //         width = Math.max(fm.charWidth(i) + x, width);
        //         x += charInfo.width;
        //         if(x > estimatedWidth){
        //             x = 0;
        //             y += fm.getHeight();
        //             height += fm.getHeight();
        //         }
        //     }
        // }

        //specifec chars
        for(char i : atlas){
            if(font.canDisplay(i)){
                CharInfo charInfo = new CharInfo(x,y,fm.charWidth(i),fm.getHeight());
                charMap.put(i, charInfo);
                width = Math.max(fm.charWidth(i) + x, width);
                x += charInfo.width;
                if(x > estimatedWidth){
                    x = 0;
                    y += fm.getHeight();
                    height += fm.getHeight();
                }
            }
        }
        height += fm.getHeight();
        g2d.dispose();
        

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);

        g2d.setColor(Color.WHITE);

        //all chars
        // for(int i = 0; i < font.getNumGlyphs(); i++){
        //     if(font.canDisplay(i)){
        //         CharInfo info = charMap.get(i);
        //         info.calTextureCoord(width, height);
        //         g2d.drawString("" + (char)i, info.x, info.y);
        //     }
        // }

        //specific chars
        for(char i : atlas){
            if(font.canDisplay(i)){
                CharInfo info = charMap.get(i);
                info.calTextureCoord(width, height);
                g2d.drawString("" + (char)i, info.x, info.y);
            }
        }
        g2d.dispose();

        try{
            File file = new File("tmp.png");
            ImageIO.write(image, "png", file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
