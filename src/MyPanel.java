

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MyPanel extends JPanel implements MouseListener, MouseMotionListener {
    Point point;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Main.currentTime = System.nanoTime();
        double deltaT = (Main.currentTime - Main.lastTime) / 1e9;
        Main.lastTime = Main.currentTime;
       if (Main.p1 == null || Main.p2 == null)
            return;
        Main.p2.paint(g);
        Main.p1.paint(g);
        if (Main.AnimationStarted) {
            Main.updatePendulumSystem(deltaT);
            MyPanel.this.repaint();
        }
    }

    public MyPanel(boolean isDoubleBuffered, Main main) {
        super(isDoubleBuffered);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        point = mouseEvent.getPoint();
        if (!Main.AnimationStarted) {
            if (Main.curBall == 1) {
                Main.p1.setNewBallPosition(point.x, point.y);
            } else if (Main.curBall == 2) {
                Main.p2.setNewBallPosition(point.x, point.y);
            }
        }
        repaint();
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

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        point = mouseEvent.getPoint();
        if (!Main.AnimationStarted) {
            if (Main.curBall == 1) {
                Main.p1.setNewBallPosition(point.x, point.y);
            } else if (Main.curBall == 2) {
                Main.p2.setNewBallPosition(point.x, point.y);
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}





