/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import math.Vector2;
import utils2d.ScreenConverter;
import utils2d.ScreenPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

public class World implements IWorld {
    public CamAnchor getC() {
        return c;
    }

    private CamAnchor c;
    private PhysicalModel tgtModel;
    private Field f;
    private ForceSource externalForce;
    private LinkedList<PhysicalModel> models;
    private int counter;

    public World(Puck p, Field f) {
        this.tgtModel = p;
        this.f = f;
        this.externalForce = new ForceSource(f.getRectangle().getCenter());
        this.c = new CamAnchor(p.getM(), f.getRectangle().getCenter());
        this.models = new LinkedList<>();
        models.addAll(Arrays.asList(
                new Puck(p.getM(), p.getR(), new Vector2(10, 0)),
                new Puck(p.getM(), p.getR(), new Vector2(15, -10)),
                new PhysicalModel(p.getM(), p.getR(), new Vector2(20, -15)) {
                    @Override
                    public void draw(Graphics2D g, int i, int j) {
                        g.drawString("image", i, j);
                    }

                    @Override
                    public boolean isCollide(Vector2 pos) {
                        return false;
                    }
                },
                new PhysicalImage(p.getM(), p.getR(), new Vector2(35, -20))
        ));
        counter = 0;
        models.addFirst(p);
        tgtModel = models.get(counter);
    }

    /**
     * Метод обновления состояния мира за указанное время
     *
     * @param dt Промежуток времени, за который требуется обновить мир.
     */
    public void update(double dt) {
        Vector2 np = tgtModel.getPosition()
                .add(tgtModel.getVelocity().mul(dt))
                .add(tgtModel.getAcceleration().mul(dt * dt * 0.5));
        Vector2 nv = tgtModel.getVelocity()
                .add(tgtModel.getAcceleration().mul(dt));

        double vx = nv.getX(), vy = nv.getY();
        boolean reset = false;
        if (np.getX() - tgtModel.getR() < f.getRectangle().getLeft() || np.getX() + tgtModel.getR() > f.getRectangle().getRight()) {
            vx = -vx;
            reset = true;
        }
        if (np.getY() - tgtModel.getR() < f.getRectangle().getBottom() || np.getY() + tgtModel.getR() > f.getRectangle().getTop()) {
            vy = -vy;
            reset = true;
        }
        nv = new Vector2(vx, vy);
        if (nv.length() < 1e-10)
            nv = new Vector2(0, 0);
        if (reset)
            np = tgtModel.getPosition();

        Vector2 Fvn = externalForce.getForceAt(np);
        Vector2 Ftr = tgtModel.getVelocity().normolized().mul(-f.getMu() * tgtModel.getM() * f.getG());
        Vector2 F = Ftr.add(Fvn);

        tgtModel.setAcceleration(F.mul(1 / tgtModel.getM()));
        tgtModel.setVelocity(nv);
        tgtModel.setPosition(np);

        //camera

        if (tgtModel.getPosition().getLengthBetweenPoints(c.getPosition()) > 2) {
            c.setVelocity(new Vector2(c.getPosition(), tgtModel.getPosition()).mul(1));
        } else if (tgtModel.getPosition().getLengthBetweenPoints(c.getPosition()) < 1) {
            c.setVelocity(new Vector2(0, 0));
        }
        np = c.getPosition()
                .add(c.getVelocity().mul(dt))
                .add(c.getAcceleration().mul(dt * dt * 0.5));
        nv = c.getVelocity()
                .add(c.getAcceleration().mul(dt));
        Ftr = c.getVelocity().normolized().mul(-f.getMu() * c.getM() * f.getG());
        Ftr = Ftr.add(c.getAcceleration());

        //c.setAcceleration(Ftr.mul(1/c.getM()));
        c.setVelocity(nv);
        c.setPosition(np);

    }

    /**
     * Метод рисует ткущее состояние мира.
     * На самом деле всю логику рисования стоит вынести из этого класса
     * куда-нибудь в WroldDrawer, унаследованный от IDrawer
     *
     * @param g  Графикс, на котором надо нарисовать текущее состояние.
     * @param sc Актуальный конвертер координат.
     */
    public void draw(Graphics2D g, ScreenConverter sc) {
        ScreenPoint tl = sc.r2s(f.getRectangle().getTopLeft());
        int w = sc.r2sDistanceH(f.getRectangle().getWidth());
        int h = sc.r2sDistanceV(f.getRectangle().getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(tl.getI(), tl.getJ(), w, h);
        g.setColor(Color.RED);
        g.drawRect(tl.getI(), tl.getJ(), w, h);
        /*
        ScreenPoint pc = sc.r2s(tgtModel.getPosition());
        int rh = sc.r2sDistanceH(tgtModel.getR());
        int rv = sc.r2sDistanceV(tgtModel.getR());
        g.setColor(Color.BLACK);
        g.fillOval(pc.getI() - rh, pc.getJ() - rv, rh + rh, rv + rv);*/
        for (PhysicalModel m : models
        ) {
            m.draw(g, sc.r2s(m.getPosition()).getI(), sc.r2s(m.getPosition()).getJ());
        }

        g.drawString(String.format("Mu=%.2f", f.getMu()), 10, 30);
        g.drawString(String.format("F=%.0f", externalForce.getValue()), 10, 50);
    }

    public Field getF() {
        return f;
    }

    public void setF(Field f) {
        this.f = f;
    }

    public PhysicalModel getP() {
        return tgtModel;
    }

    public void setP(Puck p) {
        this.tgtModel = p;
    }

    public ForceSource getExternalForce() {
        return externalForce;
    }

    public void switchTgtModel() {
        if (counter == models.size() - 1) {
            counter = 0;
        } else
            counter++;
        tgtModel=models.get(counter);
    }
}
