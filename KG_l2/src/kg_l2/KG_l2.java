/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg_l2;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author Артем
 */
public class KG_l2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame mp = new JFrame();
        mp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mp.setSize(640, 480);
        mp.setVisible(true);
        mp.setResizable(false);
        final MyPainter mp1 = new MyPainter(
                new Point(10, 200),
                new Point(15, 15),
                new Point(200, 15),
                new Point(350, 200));

        mp1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p0 = e.getLocationOnScreen().getLocation(), p1 = new Point(p0.x, p0.y - 185),
                        p2 = new Point(p0.x + 185, p0.y - 185), p3 = new Point(p0.x + 340, p0.y);

                mp1.setCubicCurvePoint(p0, p1, p2, p3);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        mp.add(mp1);

    }
}
