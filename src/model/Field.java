/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import math.Rectangle;

public class Field {
    private Rectangle rectangle;
    private double mu, g;

    /**
     * Создаёт игровое поле
     * @param rectangle Прямоугольник поля
     * @param mu Коэффициент трения
     * @param g Ускорение свободного падения
     */
    public Field(Rectangle rectangle, double mu, double g) {
        this.rectangle = rectangle;
        this.mu = mu;
        this.g = g;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
    
    
}
