package com.sudoku.view.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;


//Borrowed form GamesWithGabe and edited
public class CreateFont {
    

//     public String filePath;
//     public int fontSize;
//     public Map<Character, CharInfo> charMap;
//     //public Map<Integer, CharInfo> charMap;

    public int textureId;

    

    public char[] atlas = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M',
        'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        'æ','ø','å','Æ','Ø','Å',
        '0','1','2','3','4','5','6','7','8','9',
        '!', '"', '(', ')', '&', '%',
        '.', ',', ':', ';', '?', '-', '_', '+', '=', '/',
        ' '
    };

    

    public CreateFont(String filePath, int fontSize){
        this.filePath = filePath;
        this.fontSize = fontSize;
        this.charMap = new HashMap<>();
        createBitMap();
    }


    public CharInfo getChar(char character){
        return charMap.get(character);
    }

    public void createBitMapV2(){
        int estimatedWidth = (int)Math.sqrt(atlas.length) * fontSize + 1;

        BufferedImage image = new BufferedImage(estimatedWidth, estimatedWidth, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font(filePath, Font.PLAIN, fontSize);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        FontRenderContext frc = g2d.getFontRenderContext();
        float x = 0;
        float y = 0;
        for(char i : atlas){
            if(font.canDisplay(i)){
                GlyphVector gv = font.createGlyphVector(frc, new char[]{i});
                Rectangle2D bounds = gv.getVisualBounds();
                g2d.drawString("" + (char)i, x, (float)bounds.getHeight());
                //System.out.println(gv.getVisualBounds());
                //System.out.println(bounds.getY()+ " " + bounds.getHeight() + " " + i);
                if(x+bounds.getWidth()>estimatedWidth){
                    x = 0;
                    y+=-1* bounds.getHeight();
                }
                else {
                    x+=bounds.getWidth();
                }
            }
           
        }
        g2d.dispose();

    

        //Will create a picture of the bitmap. Use for Test
        try{
            File file = new File("tmp2.png");
            ImageIO.write(image, "png", file);
        }catch (IOException e){
            e.printStackTrace();
        }
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
               // System.out.println(fm.charWidth(i)+ " " +fm.getHeight() + " " + i);  
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


        //Will create a picture of the bitmap. Use for Test
        try{
            File file = new File("tmp.png");
            ImageIO.write(image, "png", file);
        }catch (IOException e){
            e.printStackTrace();
        }

        uploadTexture(image);
    }

    //this code is borrowed from GameWithGabe and originates from strackoverflow.
    private void uploadTexture(BufferedImage image) {
        // Taken from https://stackoverflow.com/questions/10801016/lwjgl-textures-and-strings

        int[] pixels = new int[image.getHeight() * image.getWidth()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y=0; y < image.getHeight(); y++) {
            for (int x=0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                byte alphaComponent = (byte)((pixel >> 24) & 0xFF);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
            }
        }
        buffer.flip();

        textureId = glGenTextures();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        buffer.clear();
    }
}
