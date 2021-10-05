package Simulation;

import Trees.Bush;
import Trees.MediterraneanCypress;
import Trees.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Simulator {

    public static final int GREEN = 1;
    public static final int RED = 2;
    public static final int PLAIN = 0;
    public static final int GREY = 3;
    public static final int BUSH = 5;
    public static final int CYPRESS = 4;
    static final double spreadRate = 0.5;
    static final int CYPRESSTIME = 7;
    final int R;
    final int C;
    public ArrayList<MediterraneanCypress> inBurn = new ArrayList<>();
    Tree[][] forest;
    Tree[][] forestCopy;

    /**
     * Sets nearby trees on fire for one step. Makes copy of array in order to prevent all trees igniting in same time step
     **/


    public Simulator(int r, int c) {
        this.R = r;
        this.C = c;
        this.forest = new Tree[this.R][this.C];
    }

    public void justTreeInit(double treeDensity) {
        int totalForests = (int) (treeDensity * (R * C)) - 1;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < R * C; i++) {
            if (totalForests >= 0) {
                totalForests--;
                array.add(GREEN);
            } else {
                array.add(PLAIN);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
        this.forest[rows][col] = new Tree(0, RED);
    }

    public void initJustCypress(double treeDensity) {
        int totalForests = (int) (treeDensity * (R * C)) - 1;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < R * C; i++) {
            if (totalForests >= 0) {
                totalForests--;
                array.add(CYPRESS);
            } else {
                array.add(PLAIN);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
        this.forest[rows][col] = new Tree(0, RED);
    }

    public void initializeWBushes(double treeDensity) {
        int totalForests = (int) (treeDensity * (R * C)) - 1;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < R * C; i++) {
            if (totalForests >= 0) {
                totalForests--;
                double chance = Math.random();
                if (chance > 0.5) {
                    array.add(GREEN);

                } else {
                    array.add(BUSH);
                }
            } else {
                array.add(PLAIN);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
        this.forest[rows][col] = new Tree(0, RED);
    }

    public void initialize(double treeDensity) {
        int totalForests = (int) (treeDensity * (R * C)) - 1;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < R * C; i++) {
            if (totalForests >= 0) {
                totalForests--;
                double chance = Math.random();
                if (chance > 0.66) {
                    array.add(GREEN);

                } else if (chance < 0.66 && chance > 0.33) {
                    array.add(CYPRESS);
                } else {
                    array.add(BUSH);
                }
            } else {
                array.add(PLAIN);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
        this.forest[rows][col] = new Tree(0, RED);
    }

    private void fillGrid(ArrayList<Integer> array) {
        int count = 0;
        for (int i = 0; i < this.forest.length; i++) {
            for (int j = 0; j < this.forest[0].length; j++) {
                if (array.get(count) != 5 && array.get(count) != CYPRESS) {
                    this.forest[i][j] = new Tree(0, array.get(count));
                }
                //cypress
                else if (array.get(count) == 4) {
                    this.forest[i][j] = new MediterraneanCypress(0);
                }
                //bush
                else {
                    this.forest[i][j] = new Bush(0);
                }
                count++;
            }
        }
    }

    public void setFire(int r, int c) {
        if (this.forest[r][c] instanceof Bush && Math.random() > ((Bush) this.forest[r][c]).getMoisture()) {
            this.forest[r][c] = new Bush(0, RED, 0);
        } else if (this.forest[r][c] instanceof MediterraneanCypress) {
            ((MediterraneanCypress) this.forest[r][c]).setFire(this);
        } else if (spreadRate > Math.random()) {
            this.forest[r][c] = new Tree(0, RED);
        }
    }

    public void propagateFire() {
        this.forestCopy = arrayCopy(forest);
        for (int y = 0; y < this.forestCopy.length; y++) {
            for (int x = 0; x < this.forestCopy[y].length; x++) {
                if (this.forestCopy[y][x].getCondition() == RED) {
                    setNearbyTrees(x, y);
                    this.forest[y][x].incrementTime();
                }
                if (this.forestCopy[y][x].getTime() >= 1) {
                    this.forest[y][x].setAsh();
                }
            }
        }
        for (MediterraneanCypress mediterraneanCypress : inBurn) {
            mediterraneanCypress.incrementTime();
            if (mediterraneanCypress.getTime() == CYPRESSTIME) mediterraneanCypress.ignite();
        }
    }

    private Tree[][] arrayCopy(Tree[][] forest) {
        Tree[][] copy = new Tree[this.getHeight()][this.getWidth()];
        for (int i = 0; i < this.getWidth(); i++) {
            copy[i] = Arrays.copyOf(forest[i], forest[i].length);
        }
        return copy;
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= this.forest[0].length || y >= this.forest.length;
    }

    private void setNearbyTrees(int x, int y) {
        for (int r = y - 1; r <= y + 1; r++) {
            for (int c = x - 1; c <= x + 1; c++) {
                if (outOfBounds(c, r)) continue;
                if (this.forestCopy[r][c].getCondition() == GREEN) {
                    setFire(r, c);
                } else if (this.forestCopy[r][c] instanceof Bush && this.forestCopy[r][c].getCondition() != RED && this.forestCopy[r][c].getCondition() == BUSH) {
                    setFire(r, c);

                } else if (this.forestCopy[r][c] instanceof MediterraneanCypress && this.forestCopy[r][c].getCondition() == CYPRESS && this.forestCopy[r][c].getCondition() != RED) {
                    if (Math.random() > ((MediterraneanCypress) this.forestCopy[r][c]).chance) setFire(r, c);
                }

            }
        }
    }

    //continues while there is a tree that could be burned
    public boolean isBurnable() {
        this.forestCopy = arrayCopy(this.forest);
        for (int y = 0; y < this.forest.length; y++) {
            for (int x = 0; x < this.forest[0].length; x++) {
                if (this.forest[y][x].getCondition() == GREEN && burnNearby(y, x)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean burnNearby(int y, int x) {
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i <= x + 1; i++) {
                if (outOfBounds(i, j)) continue;
                if (this.forestCopy[j][i].getCondition() == RED) {
                    return true;
                }

            }

        }
        return false;
    }

    public int getWidth() {
        return this.forest[0].length;
    }

    public int getHeight() {
        return this.forest.length;
    }

    public int[][] getDisplayGrid() {
        int[][] display = new int[this.getHeight()][this.getWidth()];
        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[0].length; j++) {
                display[i][j] = this.forest[i][j].getCondition();
            }
        }
        return display;
    }

    public double calculateBurned() {
        int burned = 0;
        int alive = 0;
        for (int j = 0; j < this.getHeight(); j++) {
            for (int i = 0; i < this.getWidth(); i++) {
                if (this.forest[j][i].getCondition() == GREY) {
                    burned++;
                } else if (this.forest[j][i].getCondition() == GREEN || this.forest[j][i].getCondition() == BUSH || this.forest[j][i].getCondition() == CYPRESS)
                    alive++;
            }
        }
        return (double) burned / (alive + burned);
    }
}