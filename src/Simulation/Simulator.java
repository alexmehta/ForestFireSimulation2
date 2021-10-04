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
    public static final int WHITE = 0;
    public static final int GREY = 3;
    public static final int BUSH = 5;
    public static final int CYPRESS = 4;
    static final double spreadRate = 0.5;
    final int R;
    final int C;
    public ArrayList<MediterraneanCypress> inBurn = new ArrayList<>();
    double treeDensity;
    Tree[][] Forest;
    Tree[][] refForest;
    /**
     * Sets nearby trees on fire for one step. Makes copy of array in order to prevent all trees igniting in same time step
     **/
    int t = 0;


    public Simulator(int r, int c, double treeDensity) {
        this.R = r;
        this.C = c;
        this.Forest = new Tree[this.R][this.C];
        this.treeDensity = treeDensity;
    }

    public void justTreeInit() {
        int totalForests = (int) (treeDensity * (R * C)) - 1;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < R * C; i++) {
            if (totalForests >= 0) {
                totalForests--;
                array.add(GREEN);
            } else {
                array.add(WHITE);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
//        System.out.println(col + " " + rows);
        this.Forest[rows][col] = new Tree(0, RED);
    }

    public void initializeWBushes() {
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
                array.add(WHITE);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
//        System.out.println(col + " " + rows);
        this.Forest[rows][col] = new Tree(0, RED);
    }

    public void initialize() {
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
                array.add(WHITE);
            }
        }
        Collections.shuffle(array);
        fillGrid(array);
        int rows = (int) (Math.random() * R);
        int col = (int) (Math.random() * C);
//        System.out.println(col + " " + rows);
        this.Forest[rows][col] = new Tree(0, RED);
    }

    private void fillGrid(ArrayList<Integer> array) {
        int count = 0;
        for (int i = 0; i < this.Forest.length; i++) {
            for (int j = 0; j < this.Forest[0].length; j++) {
                if (array.get(count) != 5 && array.get(count) != CYPRESS) {
                    this.Forest[i][j] = new Tree(0, array.get(count));
                }
                //cypress
                else if (array.get(count) == 4) {
                    this.Forest[i][j] = new MediterraneanCypress(0);
                }
                //bush
                else {
                    this.Forest[i][j] = new Bush(0);
                }
                count++;
            }
        }
    }

    public void setFire(int r, int c) {
        if (this.Forest[r][c] instanceof Bush && Math.random() > ((Bush) this.Forest[r][c]).getMoisture()) {
            this.Forest[r][c] = new Bush(0, RED, 0);

        } else if (this.Forest[r][c] instanceof MediterraneanCypress && Math.random() > ((MediterraneanCypress) this.Forest[r][c]).chance) {
            ((MediterraneanCypress) this.Forest[r][c]).setFire(this);
        } else if (1 - spreadRate < Math.random()) {
            this.Forest[r][c] = new Tree(0, RED);
        }

    }

    public void propagateFire() {
        this.refForest = arrayCopy(Forest);
        for (int y = 0; y < this.refForest.length; y++) {
            for (int x = 0; x < this.refForest[y].length; x++) {
                if (this.refForest[y][x].getCondition() == RED) {
                    setNearbyTrees(x, y);
                    this.Forest[y][x].incrementTime();
                }
                if (this.refForest[y][x].getTime() >= 1) {
                    this.Forest[y][x].setAsh();
                }
            }
        }
        for (int i = 0; i < inBurn.size(); i++) {
            inBurn.get(i).incrementTime();
            if (inBurn.get(i).getTime() == 7) inBurn.get(i).ignite();
        }
    }

    private Tree[][] arrayCopy(Tree[][] forest) {
        Tree[][] copy = new Tree[this.getHeight()][this.getWidth()];
        for (int i = 0; i < this.getWidth(); i++) {
            copy[i] = Arrays.copyOf(forest[i], forest[i].length);
        }
        return copy;
    }

    private void setNearbyTrees(int x, int y) {
        t++;
        for (int r = y - 1; r <= y + 1; r++) {
            for (int c = x - 1; c <= x + 1; c++) {
                try {
                    if (this.refForest[r][c].getCondition() == GREEN) {
                        setFire(r, c);
                    } else if (this.refForest[r][c].getCondition() == BUSH && this.refForest[r][c] instanceof Bush && this.refForest[r][c].getCondition() != RED) {
                        //separate setfire with diff probability
//                       if (t==1)System.out.println(c + " ," + r + "\t set off by " + this.refForest[y][x] + " at " + x + ", " + y) ;
                            setFire(r, c);

                    } else if (this.refForest[r][c].getCondition() == CYPRESS && this.refForest[r][c] instanceof MediterraneanCypress && this.refForest[r][c].getCondition() != RED) {
                        if (Math.random() > ((MediterraneanCypress) this.refForest[r][c]).chance)
                            setFire(r, c);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    //continues while there is a tree that could be burned
    public boolean isBurnable() {
        this.refForest = arrayCopy(this.Forest);
        for (int y = 0; y < this.Forest.length; y++) {
            for (int x = 0; x < this.Forest[0].length; x++) {
                if (this.Forest[y][x].getCondition() == GREEN && burnNearby(y, x)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean burnNearby(int y, int x) {
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i <= x + 1; i++) {
                try {
                    if (this.refForest[j][i].getCondition() == RED) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

            }

        }
        return false;
    }

    public int getWidth() {
        return this.Forest[0].length;
    }

    public int getHeight() {
        return this.Forest.length;
    }

    public int[][] getDisplayGrid() {
        int[][] display = new int[this.getHeight()][this.getWidth()];
        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[0].length; j++) {
                display[i][j] = this.Forest[i][j].getCondition();
            }
        }
        return display;
    }

    public double calculateBurned() {
        int burned = 0;
        int alive = 0;
        for (int j = 0; j < this.getHeight(); j++) {
            for (int i = 0; i < this.getWidth(); i++) {
                if (this.Forest[j][i].getCondition() == GREY) {
                    burned++;
                } else if (this.Forest[j][i].getCondition() == GREEN) alive++;
            }
        }
        return (double) burned / (alive + burned);
    }
}