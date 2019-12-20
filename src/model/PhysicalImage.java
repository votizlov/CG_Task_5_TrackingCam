package model;

import math.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PhysicalImage extends PhysicalModel {
    private BufferedImage image = null;
    public PhysicalImage(double m, double r, Vector2 position) {
        super(m, r, position);
        try {
            URL url = new URL("https://sun9-45.userapi.com/c847020/v847020882/38c48/eVyALQCBk1c.jpg");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCollide(Vector2 pos) {
        return false;
    }

    @Override
    public void draw(Graphics2D g, int i, int j) {
        g.drawImage(image,i-image.getWidth()/2,j-image.getHeight()/2,null);
    }
}
