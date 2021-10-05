package Simulation;

import processing.core.PApplet;

public class GUI extends PApplet {
    Simulator sim;
    DisplayWindow display;

    public static void main(String[] args) {
        PApplet.main("Simulation.GUI");
    }

    public void settings() {
        size(640, 550); // set the size of the screen.
    }

    public void setup() {
        // Create a simulator
        sim = new Simulator(100, 100);
        sim.justTreeInit(1);
//        sim.justTreeInit();
        // Create the display
        // parameters: (10,10) is upper left of display
        // (620, 530) is the width and height
        display = new DisplayWindow(this, 10, 10, 620, 530);
        display.setNumCols(sim.getWidth());        // NOTE:  these must match your simulator!!
        display.setNumRows(sim.getHeight());
        frameRate(144);
        // Set different grid values to different colors
        int red = color(255, 0, 0);          // pattern:  color(redAmount, greenAmount, blueAmount)
        int grey = color(180, 180, 180);
        int green = color(0, 255, 0);
        int white = color(255, 255, 255);
        int brown = color(128, 0, 128);//this is purple right now for debugging purposes since it is a lot easier to notice

        int lightGreen = color(144, 238, 144);
        display.setColor(Simulator.GREEN, green);          // 1 displays as green
        display.setColor(Simulator.RED, red);            // 2 displays as red
        display.setColor(Simulator.GREY, grey);           // 3 displays as grey
        display.setColor(Simulator.PLAIN, white);          // 0 displays as white
        display.setColor(Simulator.BUSH, brown);
        display.setColor(Simulator.CYPRESS, lightGreen);
        background(200);
        display.drawGrid(sim.getDisplayGrid());
    }

    @Override
    public void draw() {
        sim.propagateFire();
        display.drawGrid(sim.getDisplayGrid());
    }
}