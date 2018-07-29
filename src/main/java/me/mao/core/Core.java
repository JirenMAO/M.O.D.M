package me.mao.core;

import me.mao.core.controls.Keyboard;
import me.mao.core.graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Core extends Canvas implements Runnable {

    private static int Width = 300;
    private static int Height = Width / 16 * 9;
    private static int Scale = 3;

    private Screen screen;

    private Dimension dimension;

    private BufferedImage bufferedImage;

    private Thread gameThread;
    private JFrame frame;

    private Keyboard keyboard;


    private int[] pixels;

    private int x = 0;
    private int y = 0;


    private boolean running = false;


    public Core() {
        this.screen = new Screen(getWidth(), getHeight());
        this.dimension = new Dimension(getWidth(), getHeight());
        this.bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        this.gameThread = new Thread(this, "DISPLAY");
        this.frame = new JFrame();
        this.keyboard = new Keyboard();

        this.pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        // setup();
    }

    public void setup() {
        getFrame().setPreferredSize(dimension);
        getFrame().add(this);
        getFrame().pack();
        getFrame().setTitle("M.O.D.M");
        getFrame().setVisible(true);
        getFrame().setResizable(false);
        getFrame().setLocationRelativeTo(null);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().requestFocus();

        getFrame().addKeyListener(getKeyboard());
    }

    public synchronized void start() {
        running = true;
        if (getGameThread() == null) return;
        if (getGameThread().isAlive()) return;
        getGameThread().start();
    }

    public synchronized void stop() {
        running = false;
        if (getGameThread() == null) return;
        if (!getGameThread().isAlive()) return;
        try {
            getGameThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        getKeyboard().update();


        if (getKeyboard().isUp()) {
            y -= 2;
        }
        if (getKeyboard().isDown()) {
            y += 2;
        }
        if (getKeyboard().isLeft()) {
            x -= 2;
        }
        if (getKeyboard().isRight()) {
            x += 2;
        }
    }

    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }

        getScreen().clear();
        getScreen().render(x, y);

        for (int i = 0; i < getPixels().length; i++) {
            getPixels()[i] = getScreen().getPixels()[i];
        }

        Graphics graphics = getBufferStrategy().getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(getBufferedImage(), 0, 0, null);
        graphics.dispose();

        bufferStrategy.show();


    }

    public int getWidth() {
        return Width * getScale();
    }


    public int getHeight() {
        return Height * getScale();
    }

    public static int getScale() {
        return Scale;
    }

    public Screen getScreen() {
        return screen;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Thread getGameThread() {
        return gameThread;
    }

    public JFrame getFrame() {
        return frame;
    }

    public boolean isRunning() {
        return running;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void run() {

        while (isRunning()) {
            render();
            update();
        }

    }

    public static void main(String[] args) {
        Core core = new Core();
        core.setup();
        core.start();
    }
}
