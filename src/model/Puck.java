/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import math.Vector2;
import utils2d.ScreenPoint;

import java.awt.*;


public class Puck extends PhysicalModel {

    /**
     * Создаём шайбу с нулевой скоростью и ускорением
     * @param m Масса шайбы [кг]
     * @param r Радиус шайбы [м]
     * @param position Положение шайбы относительно начала координат [м]
     */
    public Puck(double m, double r, Vector2 position) {
        super(m,r,position);
    }

    @Override
    public boolean isCollide(Vector2 pos) {
        return new Vector2(pos,getPosition()).length()<getR();
    }


    @Override
    public void draw(Graphics2D g, int i, int j) {
        g.setColor(Color.BLACK);
        g.fillOval(i - 10, j - 10, 10, 10);

    }
}
