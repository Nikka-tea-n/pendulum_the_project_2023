import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import static java.awt.GridBagConstraints.RELATIVE;
import static java.lang.Math.*;


public class Main extends JFrame {

    public static final double G = 9.81;

    public int x0;
    public int y0;
    public static int curBall = 2;

    public static Pendulum p1;
    public static Pendulum p2;

    public static boolean animationStarted;
    public static boolean unvisible;
    public static final int shortTail = 50;
    public static final int mediumTail = 100;
    public static final int longTail = 300;
    public static final int emptyTail = 0;
    public static final int drawing = -1;
    public static int trajectory = shortTail;

    JTextField ball1Input = new JTextField(20);

    Color background_color = Color.WHITE;
    Color foreground_color = Color.BLACK;

    public static void main(String[] args) {
        Main main = new Main("Симуляция двойного маятника");

        main.setBounds(100, 50, 1920, 1080);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //создание меню бара
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(main.createMenuControl());
        menuBar.add(main.createMenuTrace());
        main.setJMenuBar(menuBar);

        //создание панели
        MyPanel myPanel;
        myPanel = new MyPanel(true, main);
        main.add(myPanel);
        main.setVisible(true);

        main.addButtons(myPanel);

        //создаём двойной маятник
        main.x0 = myPanel.getWidth() / 2;
        main.y0 = myPanel.getHeight() / 2;
        Main.p1 = new Pendulum(main.x0, main.y0, main.x0 + 200, main.y0);
        Main.p2 = new Pendulum(main.x0 + 200, main.y0, main.x0 + 400, main.y0);
        Main.p1.setDependentPendulum(p2);
    }


    public Main(String title) {
        super(title);

    }

    private void addButtons(MyPanel myPanel) {
        //Создание кнопок шаров
        JButton ball1Button = new JButton("Голубой шар");
        JButton ball2Button = new JButton("Фиолетовый шар");
        ball1Button.setBackground(background_color);
        ball1Button.setForeground(foreground_color);
        ball2Button.setBackground(background_color);
        ball2Button.setForeground(foreground_color);

        ball2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("ball2");
                curBall = 2;
                ball1Button.setSelected(false);
                ball2Button.setSelected(true);
            }
        });
        ball1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("ball1");
                curBall = 1;
                ball1Button.setSelected(true);
                ball2Button.setSelected(false);
            }
        });


        //Добавление кнопок в контейнер

        //Создание контейнера заполнения окна элементами
        Container container = getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        //Добавление панели симуляции в контейнер
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        container.add(myPanel, constraints);
        constraints.gridwidth = 1;
        constraints.weighty = 0;
        constraints.gridy = 1;
        constraints.gridx = 1;
        container.add(ball1Button, constraints);

        constraints.gridx = 2;
        container.add(ball2Button, constraints);

        ball1Input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ball1InputText = ball1Input.getText();
                p1.setMass(Float.parseFloat(ball1InputText));
                p2.setMass(Float.parseFloat(ball1InputText));
                System.out.printf("l1 = %.1f " , p1.length);
                System.out.println(p1.getMass() + " " + p2.getMass());

            }
        });
        constraints.gridx = RELATIVE;
        myPanel.add(ball1Input, constraints);

//        JTextField ball2Input = new JTextField(20);
//        constraints.gridx = 4;
//        myPanel.add(ball2Input, constraints);
    }

    //создаем меню управления
    private JMenu createMenuControl() {
        JMenu menuControl = new JMenu("Управление");
        JMenuItem start = new JMenuItem("Пуск");
        JMenuItem pause = new JMenuItem("Пауза");
        JMenuItem reset = new JMenuItem("Завершить");
        menuControl.add(start);
        menuControl.add(pause);
        menuControl.add(reset);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                animationStarted = true;
                MyPanel.lastTime = System.nanoTime();
                Main.this.repaint();
            }
        });
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                animationStarted = false;
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                p1.trace.clear();
                p2.trace.clear();
                p1.beta = 0;
                p1.omega = 0;
                p2.beta = 0;
                p2.omega = 0;
                animationStarted = false;
                Main.this.repaint();
            }
        });
        return menuControl;
    }


    //создаем меню траектории
    private JMenu createMenuTrace() {
        JMenu menuTrace = new JMenu("Траектория");
        JMenuItem emptyTail = new JMenuItem("Без следа");
        JMenuItem shortTail = new JMenuItem("Короткий хвост");
        JMenuItem mediumTail = new JMenuItem("Средний хвост");
        JMenuItem longTail = new JMenuItem("Длинный хвост");
        JMenuItem clearTrace = new JMenuItem("Очистить траекторию");
        JMenuItem drawing = new JMenuItem("Рисование");
        JMenuItem unvisibleMode = new JMenuItem("Невидимый режим");
        menuTrace.add(emptyTail);
        menuTrace.add(shortTail);
        menuTrace.add(longTail);
        menuTrace.add(clearTrace);
        menuTrace.add(drawing);
        menuTrace.add(unvisibleMode);

        emptyTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.trajectory = Main.emptyTail;
                cleanupTail();
            }
        });
        shortTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.trajectory = Main.shortTail;
                cleanupTail();
            }
        });
        mediumTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.trajectory = Main.mediumTail;
                cleanupTail();
            }
        });
        longTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.trajectory = Main.longTail;
                cleanupTail();
            }
        });
        clearTrace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                p1.trace.clear();
                p2.trace.clear();
                Main.this.repaint();
            }
        });
        drawing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.trajectory = Main.drawing;
            }
        });
        unvisibleMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                unvisible = !unvisible;
            }
        });
        return menuTrace;
    }

    //очистка следа до размера выбранной траектории
    private void cleanupTail() {
        while (p1.trace.size() > trajectory) {
            p1.trace.pop();
            p2.trace.pop();
        }
    }


    //метод, определяющий изменение состояния маятника за малое время
    public static void updatePendulumSystem(double delta) {
        // пауза 10 миллисекунды, чтобы не перегружать мощности
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //расчет углового ускорения внутреннего шара beta1
        double comp_1, comp_2, comp_3;
        double m1 = p1.getMass();
        double m2 = p2.getMass();

        comp_1 = -G * (2f * m1 + m2) * sin(p1.phi) - m2 * G * sin(p1.phi - 2f * p2.phi);
        comp_2 = 2f * sin(p1.phi - p2.phi) * m2 * (pow(p2.omega, 2) * p2.length + pow(p1.omega, 2) * p1.length * cos(p1.phi - p2.phi));
        comp_3 = p1.length * (2f * m1 + m2 - m2 * cos(2f * p1.phi - 2 * p2.phi));
        if (Math.abs(comp_3) < 0.0000001)
            System.out.println("Zero divide 1");
        p1.beta = ((comp_1 - comp_2) / comp_3);

        //расчет углового ускорения внешнего шара beta2
        comp_1 = 2 * sin(p1.phi - p2.phi);
        comp_2 = pow(p1.omega, 2) * p1.length * (m1 + m2) + G * (m1 + m2) * cos(p1.phi) + pow(p2.omega, 2) * p2.length * m2 * cos(p1.phi - p2.phi);
        comp_3 = p2.length * (2f * m1 + m2 - m2 * cos(2f * p1.phi - 2f * p2.phi));
        if (Math.abs(comp_3) < 0.0000001)
            System.out.println("Zero divide 2");
        p2.beta = ((comp_1 * comp_2) / comp_3);

        //определяем изменение угловых скоростей и координат
        p1.phi += p1.omega * delta + p1.beta * pow(delta, 2) /2;
        p2.phi += p2.omega * delta + p2.beta * pow(delta, 2) /2;
        p1.omega += p1.beta * delta;
        p2.omega += p2.beta * delta;

        //вычисляем новые координаты в декартовой системе
        double newBall1X = p1.attachmentPointX + p1.length * 100f * sin(p1.phi);
        double newBall1Y = p1.attachmentPointY + p1.length * 100f * cos(p1.phi);
        double newBall2X = p1.getBallX() + p2.length * 100f * sin(p2.phi);
        double newBall2Y = p1.getBallY() + p2.length * 100f * cos(p2.phi);
        p1.setBallX(newBall1X);
        p1.setBallY(newBall1Y);
        p2.setBallX(newBall2X);
        p2.setBallY(newBall2Y);

        Point pointOfP1 = new Point((int) p1.getBallX(), (int) p1.getBallY());
        Point pointOfP2 = new Point((int) p2.getBallX(), (int) p2.getBallY());

        //запоминаем координаты траектории
        p1.trace.add(pointOfP1);
        p2.trace.add(pointOfP2);
        if ((p1.trace.size() >= trajectory || p2.trace.size() >= trajectory) && trajectory != drawing) {
            p2.trace.pop();
            p1.trace.pop();
        }
    }
}







