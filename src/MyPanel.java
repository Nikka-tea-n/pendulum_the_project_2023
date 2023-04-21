

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MyPanel extends JPanel implements MouseListener, MouseMotionListener {
    Point point;
    public static long lastTime;
    public static long currentTime;

    public MyPanel(boolean isDoubleBuffered, Main main) {
        super(isDoubleBuffered);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    //метод рисования с таймером, за внутри которого сразу вызывается перерисовка.
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        currentTime = System.nanoTime();
        double deltaT = (currentTime - lastTime) / 1e9;
        lastTime = currentTime;
        if (Main.p1 == null || Main.p2 == null)
            return;
        g.setColor(new Color(23,28,35));
        Main.p1.paintTrace(g);
        Main.p2.paintTrace(g);
        if(!Main.unvisible) {
            g.setColor(Color.BLACK);
            Main.p1.paintLine(g);
            Main.p2.paintLine(g);
            g.setColor(new Color(66, 195, 201));
            Main.p1.paintBall(g);
            g.setColor(new Color(119, 94, 158));
            Main.p2.paintBall(g);
        }
        if (Main.animationStarted) {
            Main.updatePendulumSystem(deltaT);
            MyPanel.this.repaint();
        }
    }

    //обработчики
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        point = mouseEvent.getPoint();
        setDoublePendulum();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        point = mouseEvent.getPoint();
        setDoublePendulum();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
    public void mouseMoved(MouseEvent e) {

    }


//изменяет положение одного из шаров в зависимости от выбранного инструмента
    private void setDoublePendulum() {
        if (!Main.animationStarted) {
            if (Main.curBall == 1) {
                Main.p1.setNewBallPosition(point.x, point.y);
            } else if (Main.curBall == 2) {
                Main.p2.setNewBallPosition(point.x, point.y);
            }
        }
    }
}





