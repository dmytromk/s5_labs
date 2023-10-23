package task_b;

import java.util.Random;

public class Garden {
    // 0 - no plant, 1 - withered plant, 2 - good plant
    private final int[][] garden;
    private final int rows;
    private final int columns;
    private final Random random;

    public Garden(int rows, int columns) {
        this.random = new Random();
        this.rows = rows;
        this.columns = columns;
        this.garden = new int[rows][columns];
        randomizeGarden();
    }

    public void randomizeGarden() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                int randomValue = this.random.nextInt(3);
                this.garden[i][j] = randomValue;
            }
        }
    }

    public void waterWitheredPlants() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (this.garden[i][j] == 1) {
                    this.garden[i][j] = 2;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder gardenString = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gardenString.append(garden[i][j]).append(" ");
            }
            gardenString.append("\n");
        }

        return gardenString.toString();
    }
}
