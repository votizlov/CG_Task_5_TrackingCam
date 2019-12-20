/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import math.Rectangle;
import math.Vector2;
import model.Field;
import model.Puck;
import model.World;
import timers.AbstractWorldTimer;
import timers.UpdateWorldTimer;
import utils2d.ScreenConverter;
import utils2d.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.sqrt;

public class DrawPanel extends JPanel implements ActionListener,
        MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    private ScreenConverter sc;
    private World w;
    private AbstractWorldTimer uwt;
    private Timer drawTimer;
    private Field f;
    Rectangle view;

    public DrawPanel() {
        super();
        f = new Field(
                new Rectangle(0, 10, 50, 50),
                0.1, 9.8);
        w = new World(new Puck(1, 0.3, f.getRectangle().getCenter()), f);
        view = new Rectangle(25, 25, 25, 25);
        sc = new ScreenConverter(view, 450, 450);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);

        (uwt = new UpdateWorldTimer(w, 10)).start();
        drawTimer = new Timer(40, this);
        drawTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        double k = sqrt(new Vector2(w.getP().getPosition(), w.getC().getPosition()).length() / 4);
        sc.setHs(getHeight());
        sc.setWs(getWidth());
        if (new Vector2(w.getP().getPosition(), w.getC().getPosition()).length() > 4) {
            sc.setHr(view.getHeight() * k);
            sc.setWr(view.getWidth() * k);
            sc.setXr(w.getC().getPosition().getX() - f.getRectangle().getWidth() / 2*k + view.getWidth() / 2*k);
            sc.setYr(w.getC().getPosition().getY() + f.getRectangle().getHeight() / 2*k - view.getHeight() / 2*k);
        } else {
            sc.setHr(view.getHeight());
            sc.setWr(view.getWidth());
            sc.setXr(w.getC().getPosition().getX() - f.getRectangle().getWidth() / 2 + view.getWidth() / 2);
            sc.setYr(w.getC().getPosition().getY() + f.getRectangle().getHeight() / 2 - view.getHeight() / 2);
            //sc.setXr(view.getLeft());
            //sc.setYr(view.getTop());
        }
        w.draw((Graphics2D) bi.getGraphics(), sc);
        g.drawImage(bi, 0, 0, null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int direction = 0;
        if (e.getButton() == MouseEvent.BUTTON1)
            direction = 1;
        else if (e.getButton() == MouseEvent.BUTTON3)
            direction = -1;
        w.getExternalForce().setValue(10 * direction);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        w.getExternalForce().setValue(0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        w.getExternalForce().setLocation(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        w.getExternalForce().setLocation(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double oldMu = w.getF().getMu();
        oldMu = Math.round(oldMu * 100 + e.getWheelRotation()) * 0.01;

        if (oldMu < -1)
            oldMu = -1;
        else if (oldMu > 1)
            oldMu = 1;
        else if (Math.abs(oldMu) < 0.005)
            oldMu = 0;
        w.getF().setMu(oldMu);
    }

    boolean isUp, isDown, isRight, isLeft;

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        Vector2 dir = new Vector2(0, 0);
        double val = 10;
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                isUp = !isUp;
                dir = w.getP().getPosition().add(new Vector2(0, 5));
                break;
            case KeyEvent.VK_DOWN:
                isDown = !isDown;
                dir = w.getP().getPosition().add(new Vector2(0, -5));
                break;
            case KeyEvent.VK_LEFT:
                isLeft = !isLeft;
                dir = w.getP().getPosition().add(new Vector2(-5, 0));
                break;
            case KeyEvent.VK_RIGHT:
                isRight = !isRight;
                dir = w.getP().getPosition().add(new Vector2(5, 0));
                break;
            case KeyEvent.VK_SPACE:
                w.switchTgtModel();
                break;
            default:val = 0;
            break;
        }
        w.getExternalForce().setLocation(dir);
        w.getExternalForce().setValue(val);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        w.getExternalForce().setValue(0);
    }
}
