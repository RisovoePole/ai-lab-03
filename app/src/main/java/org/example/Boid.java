package org.example;

import java.util.List;
import java.util.Random;

class Boid {

    double x, y;
    double vx, vy;

    double maxSpeed = Config.getDouble("MAX_SPEED", 3.5);
    double maxForce = Config.getDouble("MAX_FORCE", 0.15);
    double perceptionRadius = Config.getDouble("PERCEPTION_RADIUS", 60.0);
    double separationDist = Config.getDouble("SEPARATION_DISTANCE", 25.0);

    double wSep = Config.getDouble("WEIGHT_SEPARATION", 1.5);
    double wAlign = Config.getDouble("WEIGHT_ALIGNMENT", 0.05);
    double wCoh = Config.getDouble("WEIGHT_COHESION", 0.01);

    public Boid(double x, double y) {
        this.x = x;
        this.y = y;
        Random r = new Random();
        this.vx = r.nextDouble() * 4 - 2;
        this.vy = r.nextDouble() * 4 - 2;
    }

    public void update(List<Boid> boids, int boundWidth, int boundHeight) {
        double[] sep = { 0, 0 };
        double[] align = { 0, 0 };
        double[] coh = { 0, 0 };
        int count = 0;

        for (Boid other : boids) {
            double d = distance(this, other);
            if (other != this && d < perceptionRadius) {
                // 1. Separation (исправлено деление на d * d)
                if (d < separationDist && d > 0) {
                    double dx = x - other.x;
                    double dy = y - other.y;
                    sep[0] += dx / (d * d);
                    sep[1] += dy / (d * d);
                }
                // 2. Alignment
                align[0] += other.vx;
                align[1] += other.vy;
                // 3. Cohesion
                coh[0] += other.x;
                coh[1] += other.y;

                count++;
            }
        }

        if (count > 0) {
            align[0] /= count;
            align[1] /= count;
            coh[0] = coh[0] / count - x;
            coh[1] = coh[1] / count - y;
        }

        applyForce(sep[0] * wSep, sep[1] * wSep);
        applyForce(align[0] * wAlign, align[1] * wAlign);
        applyForce(coh[0] * wCoh, coh[1] * wCoh);

        x += vx;
        y += vy;

        // Безопасная проверка границ (если ширина еще 0, берем дефолт)
        int w = boundWidth > 0 ? boundWidth : 800;
        int h = boundHeight > 0 ? boundHeight : 600;

        if (x < 0) x = w;
        if (x > w) x = 0;
        if (y < 0) y = h;
        if (y > h) y = 0;
    }

    private void applyForce(double fx, double fy) {
        double forceMag = Math.sqrt(fx * fx + fy * fy);
        if (forceMag > maxForce) {
            fx = (fx / forceMag) * maxForce;
            fy = (fy / forceMag) * maxForce;
        }

        // Применяем силу к скорости (F = m * a, считаем массу равной 1)
        vx += fx;

        vy += fy;
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }

    private double distance(Boid b1, Boid b2) {
        return Math.sqrt(Math.pow(b1.x - b2.x, 2) + Math.pow(b1.y - b2.y, 2));
    }
}
