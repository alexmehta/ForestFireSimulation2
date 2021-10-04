package Simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MonteCarloOutput {

    private static final int trials = 100;

    public static void main(String[] args) throws FileNotFoundException {
        runExperiment();
    }

    private static void runExperiment() throws FileNotFoundException {
        PrintWriter fout = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output.csv")));
        fout.println("percent, burned");
        for (int i = 1; i <= 100; i++) {
            String msg = i + "%, ";
            fout.print(msg);
            fout.printf("%f\n", (runTest((double) i / 100)));
//            System.out.println((double)i/100);
//            System.out.println(i + "%, " + runTest((double) i/100));
        }
        fout.close();
    }

    private static double runTest(double i) {
        double avgBurned = 0;
        for (int j = 0; j < trials; j++) {
            avgBurned += runSingleFire(i);
        }
        return avgBurned / trials;
    }

    private static double runSingleFire(double i) {
        Simulator simulator = new Simulator(100, 100, i);
        simulator.initializeWBushes();
        while (simulator.isBurnable()) {
            simulator.propagateFire();
        }
        return simulator.calculateBurned();
    }
}
