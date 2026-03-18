package com.sudoku.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class PrepareFont {
    
    /// ------------------------------------------------
    /// | Inspired by GameWithGabe                     |
    /// |  https://www.youtube.com/watch?v=URMbfBcOiN8 |
    /// ------------------------------------------------

    private String filePath; //the path to the .TTF file
    private int fontSize; // the bit size of the char

    private Map<Integer, CharInfo> charMap;

    private int width, height, lineHeight;

    public PrepareFont(String filePath, int fontSize){
        this.filePath = filePath;
        this.fontSize = fontSize;
        this.charMap = new HashMap<>();
        generateBitMap(); 
    }

    public void generateBitMap(){ //creates a bitmap of all the glyphs in .TTF
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();
        Font font = new Font(filePath, Font.PLAIN, fontSize);
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        //estimatas the width of the squard image and adds 1 
        //so we are sure that there are space for all the glyphs
        int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * fontSize + 1;
        width = 0;
        height = fontMetrics.getHeight();
        lineHeight = fontMetrics.getHeight(); 

        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);

        //there are glyphs we can't display. font.canDisplay can take a unicode and check
        //if it can be displayed
        for(int i = 0; i < font.getNumGlyphs(); i++){
            if (font.canDisplay(i)){
                CharInfo charInfo = new CharInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                charMap.put(i, charInfo);
                width = Math.max(x + fontMetrics.charWidth(i), width);
                x += charInfo.width;
                if(x>estimatedWidth){
                    x = 0;
                    y += fontMetrics.getHeight() * 1.4f;
                    height += fontMetrics.getHeight() * 1.4f;
                }
            }
        }
        height += fontMetrics.getHeight() * 1.4f;
        g2D.dispose();

        image = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        g2D = image.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        g2D.setColor(Color.WHITE);
        for(int i = 0; i < font.getNumGlyphs(); i++){
            if(font.canDisplay(i)){
                CharInfo info = charMap.get(i);
                info.calulateTextureCoord(width, height);
                g2D.drawString("" + (char)i, info.x, info.y);
            }
        }
        g2D.dispose();

        try{
            File file = new File("tmp.png");
            ImageIO.write(image, "png", file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
