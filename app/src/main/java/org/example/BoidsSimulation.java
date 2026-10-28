package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class BoidsSimulation extends JFrame {

    public BoidsSimulation() {
        int width = Config.getInt("WINDOW_WIDTH", 800);
        int height = Config.getInt("WINDOW_HEIGHT", 600);

        setTitle("Boids Simulation - Fixed");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BoidPanel panel = new BoidPanel(width, height);
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BoidsSimulation().setVisible(true);
        });
    }
}
