package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

class BoidPanel extends JPanel implements ActionListener {

    private List<Boid> boids;
    private Timer timer;
    private int initWidth, initHeight;

    public BoidPanel(int width, int height) {
        setBackground(Color.BLACK);
        this.initWidth = width;
        this.initHeight = height;
        boids = new ArrayList<>();
        Random r = new Random();

        int boidCount = Config.getInt("BOID_COUNT", 100);
        for (int i = 0; i < boidCount; i++) {
            boids.add(new Boid(r.nextInt(width), r.nextInt(height)));
        }

        int fps = Config.getInt("FPS", 60);
        int delay = 1000 / fps;
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Передаем реальные размеры панели или дефолтные из конструктора
        int w = getWidth() > 0 ? getWidth() : initWidth;
        int h = getHeight() > 0 ? getHeight() : initHeight;

        for (Boid b : boids) {
            b.update(boids, w, h);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2d.setColor(Color.WHITE);
        for (Boid b : boids) {
            // Вычисляем угол движения в радианах
            double angle = Math.atan2(b.vy, b.vx);

            // Смещаем центр координат в точку бойда и поворачиваем на этот угол
            g2d.translate(b.x, b.y);
            g2d.rotate(angle);

            // Рисуем треугольник (нос направлен вправо по оси X)
            int[] xPoints = { 8, -6, -6 }; // Длина носа и тыл
            int[] yPoints = { 0, -4, 4 }; // Ширина крыльев

            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);

            // Возвращаем систему координат в исходное положение для следующего бойда
            g2d.rotate(-angle);
            g2d.translate(-b.x, -b.y);
        }
        g2d.dispose();
    }
}
