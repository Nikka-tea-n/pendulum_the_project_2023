import java.awt.*;
import java.util.LinkedList;

import static java.lang.Math.sqrt;
public class Pendulum {
    double m = 50; // масса в килограммах

    double omega = 0;
    double beta = 0;
    double phi = 0;
    double r; // в пикселях
    double length; // в метрах ~ 100 пикселей
    public LinkedList<Point> trace; // или класс Trace с полями coordinates[] и int cursor

    double attachmentPointX; // точка крепления (х)
    double attachmentPointY; // точка крепления (у)

    private double ballX; // положение груза (Х)
    private double ballY; // положение груза (У)

    Pendulum dependentPendulum;

    public Pendulum(double x0, double y0, double x, double y) {
        this.attachmentPointX = x0;
        this.attachmentPointY = y0;
        this.ballX = x;
        this.ballY = y;
        recalculateLength();
        recalculatePhi();
        this.r = 13;
        this.trace = new LinkedList<>();
        omega = 0;
        beta = 0;
    }

    public void recalculateLength() {
        this.length = sqrt(Math.pow(ballX - attachmentPointX, 2) + Math.pow(ballY - attachmentPointY, 2)) / 20;
    }

    public void recalculatePhi() {
        phi = Math.atan2(ballX - attachmentPointX, ballY - attachmentPointY);
    }

    public double getBallX() {
        return ballX;
    }

    public void setNewBallPosition(double ballX, double ballY) {
        if (dependentPendulum != null) {
            dependentPendulum.attachmentPointX = ballX;
            dependentPendulum.attachmentPointY = ballY;
            dependentPendulum.recalculateLength();
            dependentPendulum.recalculatePhi();
        }
        this.ballX = ballX;
        this.ballY = ballY;
        recalculatePhi();
        recalculateLength();
    }
    public void setBallX(double ballX) {
        if (dependentPendulum != null)
            dependentPendulum.attachmentPointX = ballX;
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        if (dependentPendulum != null)
            dependentPendulum.attachmentPointY = ballY;
        this.ballY = ballY;
    }

    public Pendulum() {
    }

    public void setDependentPendulum(Pendulum dependentPendulum) {
        this.dependentPendulum = dependentPendulum;
        this.dependentPendulum.attachmentPointX = this.ballX;
        this.dependentPendulum.attachmentPointY = this.ballY;
    }

    public void paint(Graphics g) {
        // отрисовка следа
        Point prevPoint = null;
        int i = 0;
        for (Point p: trace) {
            i++;
            if (prevPoint == null) {
                prevPoint = p;
                continue;
            }
//            g.setColor(new Color(255-i/100,255-i/100,255-i/100));
            g.setColor(Color.BLUE);
            g.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
            prevPoint = p;
        }
        g.setColor(Color.BLACK);
        // отрисовка маятника
        g.drawLine((int) attachmentPointX, (int) attachmentPointY, (int) ballX, (int) ballY);
        g.fillOval((int) (ballX - r), (int) (ballY - r), (int) (2 * r), (int) (2 * r));
    }

}