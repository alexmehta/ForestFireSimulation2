package Simulation;

import java.util.LinkedHashMap;
import java.util.Map;

import processing.core.*;

public class DisplayWindow {
    // Colors used for empty locations.
    private static final int EMPTY_COLOR = 0xFFFFFFFF;

    // Color used for objects that have no defined color.
    private static final int UNKNOWN_COLOR = 0x66666666;

    private PApplet p; // the applet we want to display on

    private int x, y, w, h; // (x, y) of upper left corner of display
    // the width and height of the display

    private float dx, dy; // calculate the width and height of each box
    // in the field display using the size of the field
    // and the width and height of the display

    private int rows, cols;

    // A map for storing colors for participants in the simulation
    private Map<Object, Integer> colors;
    private Map<Object, PImage> images;

    // (x, y) is the upper-left corner of the display in pixels
    // w and h are the width and height of the display in pixels
    public DisplayWindow(PApplet p, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.p = p;

        colors = new LinkedHashMap<Object, Integer>();
        images = new LinkedHashMap<Object, PImage>();
    }

    public void drawGrid(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            p.textAlign(p.CENTER);
            p.textSize(34);
            p.fill(0);
            p.stroke(0);
            p.text("Your grid is null or has size 0", p.width/2, p.height/2);
        }
        Integer displayColor;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int thing = grid[i][j];

                displayColor = getColor(thing);
                p.fill(displayColor);

                p.rect(x + i * dx, y + j * dy, dx, dy);
            }
        }
    }

    /**
     * Define a color to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param color     The color to be used for the given type of piece.
     */
    public void setColor(Object pieceType, Integer color) {
        colors.put(pieceType, color);
    }

    /**
     * Define an Image to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param img       The image to be used for the given type of piece.
     */
    public void setImage(Object pieceType, PImage img) {
        images.put(pieceType, img);
    }

    /**
     * Define a color to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param filename  The file path to the image to be used for the given type of piece.
     */
    public void setImage(Object pieceType, String filename) {
        PImage img = p.loadImage(filename);
        setImage(pieceType, img);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Integer getColor(Object pieceType) {
        Integer col = colors.get(pieceType);
        if (col == null) { // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    private PImage getImage(Object pieceType) {
        PImage img = images.get(pieceType);
        return img;
    }

    // return the y pixel value of the upper-left corner of location l
    private float yCoordOf(Location l) {
        return y + l.getRow() * dy;
    }

    // return the x pixel value of the upper-left corner of location l
    private float xCoordOf(Location l) {
        return x + l.getCol() * dx;
    }

    // Return location at coordinates x, y on the screen
    public Location gridLocationAt(float mousex, float mousey) {
        Location l = new Location((int) Math.floor((mousey - y) / dy),
                (int) Math.floor((mousex - x) / dx));
        return l;
    }

    // Return whether (x, y) is over the board or not
    public boolean overBoard(float mx, float my) {
        return (mx >= x && mx <= x + w && my > y && my < y + h);
    }

    public void setNumCols(int numCols) {
        rows = numCols;
        dx = w / rows;
    }

    public void setNumRows(int numRows) {
        cols = numRows;
        dy = h / cols;
    }
}