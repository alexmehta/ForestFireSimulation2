package Trees;

import Simulation.Simulator;

public class Bush extends Tree {
    //the less moist, more dry, meaning higher chance of burn

    private double MOISTURE = 0.20;

    public Bush(int time) {
        super(time, Simulator.BUSH);

    }



    public double getMoisture() {
        return MOISTURE;
    }

    public void setMoisture(double moisture) {
        this.MOISTURE = moisture;
    }

    public Bush(int time, int condition, double moisture) {
        super(time, condition);
        this.MOISTURE = moisture;
    }

}
