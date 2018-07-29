package me.mao.core.graphics;

import java.util.Random;

public class Screen {

    private final int MAP_SIZE = 4;
    private final int MAP_MASK = MAP_SIZE - 1;


    private int Width, Height;
    private int[] pixels, tiles;

    public Screen(int width, int height) {
        Random random = new Random();

        this.Width = width;
        this.Height = height;
        this.pixels = new int[getWidth() * getHeight()];

        //TESTING

        this.tiles = new int[getMAP_SIZE() * getMAP_SIZE()];

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = random.nextInt(0xff00ff);
        }
    }

    public void render(int xOffset, int yOffset) {
        for (int y = 0; y < getHeight(); y++) {
            int yy = y + yOffset;

          //  if (yy < 0 || yy >= getHeight()) return;
            for (int x = 0; x < getWidth(); x++) {
                int xx = x + xOffset;

                //if (xx < 0 || xx >= getWidth()) return;

                int tileIndex = ((xx >> 4) & getMAP_MASK()) + ((yy >> 4) & getMAP_MASK()) * MAP_SIZE;
                pixels[x + y * getWidth()] = tiles[tileIndex];
            }
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public int getMAP_SIZE() {
        return MAP_SIZE;
    }

    public int getMAP_MASK() {
        return MAP_MASK;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int[] getTiles() {
        return tiles;
    }
}
