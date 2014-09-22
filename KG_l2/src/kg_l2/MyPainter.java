/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg_l2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Артем
 */
public class MyPainter extends JPanel {

    public void setCubicCurvePoint(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        paintComponent(getGraphics());
    }
    private CubicCurve2D.Double curve;
    
    private Point p0, p1, p2, p3;
    private Rectangle2D.Double b0, b1, b2, b3;
    
    private static final int WIDTH = 5;// длина для прямоугольников, которые обозначают точки для кривой безье
    private static final int HEIGHT = 5;

    public MyPainter(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        b0 = new Rectangle2D.Double(p0.getX(), p0.getY(), WIDTH, HEIGHT);
        b1 = new Rectangle2D.Double(p1.getX(), p1.getY(), WIDTH, HEIGHT);
        b2 = new Rectangle2D.Double(p2.getX(), p2.getY(), WIDTH, HEIGHT);
        b3 = new Rectangle2D.Double(p3.getX(), p3.getY(), WIDTH, HEIGHT);

        curve = new CubicCurve2D.Double(p0.getX(),
                p0.getY(),
                p1.getX(),
                p1.getY(),
                p2.getX(),
                p2.getY(),
                p3.getX(),
                p3.getY());
    }

    private void doDrawing(Graphics g){
        g.clearRect(0, 0, getWidth(), getHeight());
        ((Graphics2D) g).draw(curve);
        ((Graphics2D) g).draw(b0);
        ((Graphics2D) g).draw(b1);
        ((Graphics2D) g).draw(b2);
        ((Graphics2D) g).draw(b3);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
