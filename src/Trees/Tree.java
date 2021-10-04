package Trees;

import Simulation.Simulator;

public class Tree {
    private int time;
    protected int condition;

    public Tree(int time) {
        this.time = time;
        this.condition = Simulator.PLAIN;
    }

    public Tree(int time, int condition) {
        this.time = time;
        this.condition = condition;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "time=" + time +
                ", condition=" + condition +
                '}';
    }

    public void incrementTime() {
        this.time++;
    }

    public void setFire() {
        this.condition = Simulator.RED;
    }

    public void setAsh() {
        this.condition = Simulator.GREY;
    }

}
