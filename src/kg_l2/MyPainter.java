/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg_l2;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Артем
 */
public class MyPainter extends Canvas {

    public void setCubicCurvePoint(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        update(getGraphics());
        System.out.println("ad");
    }
    
    @Override
    public void update(Graphics g){
        double n = 1000;
        for (double i = 0; i < n; i++) {
            getGraphics().drawOval(
                    (int) Math.round(n*
                    Math.pow((1 - i / n), 3) * p0.x + 3* (i/n) * Math.pow((1 - i / n), 2) * p2.x + 3*(i/n)* Math.pow(i/n, 2) * (1 - i / n) * p2.x + Math.pow(i / n, 3) * p3.x),
                    (int) Math.round(n*
                    Math.pow((1 - i / n), 3) * p0.y + 3* (i/n) * Math.pow((1 - i / n), 2) * p2.y + 3*(i/n)* Math.pow(i/n, 2) * (1 - i / n) * p2.y + Math.pow(i / n, 3) * p3.y),
                    1, 1);
        }
    }
    
    private Point p0, p1, p2, p3;

    public MyPainter(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, 640, 480);
        double n = 1000;
        for (double i = 0; i < n; i++) {
            g.drawOval(
                    (int) Math.round(n*
                    Math.pow((1 - i / n), 3) * p0.x + 3* (i/n) * Math.pow((1 - i / n), 2) * p2.x + 3*(i/n)* Math.pow(i/n, 2) * (1 - i / n) * p2.x + Math.pow(i / n, 3) * p3.x),
                    (int) Math.round(n*
                    Math.pow((1 - i / n), 3) * p0.y + 3* (i/n) * Math.pow((1 - i / n), 2) * p2.y + 3*(i/n)* Math.pow(i/n, 2) * (1 - i / n) * p2.y + Math.pow(i / n, 3) * p3.y),
                    1, 1);
        }
    }
}
