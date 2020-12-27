/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resizeImage;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Wahyu Imam
 */
public class resizeImage {
    
    private static BufferedImage loadImg(String ref){
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(ref));
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return bimg;
    }
    
    private static BufferedImage resize(BufferedImage img, int newW, int newH){
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
    
    public void tampilImg(JLabel label, String nmImg){
        String url = "src/img"+nmImg;
        BufferedImage loadImg = loadImg(url);
        ImageIcon imgIcon = new ImageIcon(resize(loadImg, label.getWidth(), label.getHeight()));
        label.setIcon(imgIcon);
    }
}
