package Trees;

import Simulation.Simulator;

//“When we got there we saw that all the common oaks, holm oaks, pines, and junipers had completely burnt. But only 1.27% of the Mediterranean cypresses had ignited,” study author Bernabé Moya told BBC Mundo.
//these trees burn slowly as well
public class MediterraneanCypress extends Tree {
    public final double chance = 0.9;

    public MediterraneanCypress(int time) {
        super(time);
        this.condition = Simulator.CYPRESS;
    }

    public MediterraneanCypress(int time, int condition) {
        super(time, condition);
    }

    public void setFire(Simulator simulator) {
        simulator.inBurn.add(this);
    }

    public void ignite() {
        this.setCondition(Simulator.RED);
    }
}
