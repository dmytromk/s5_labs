package task_a;

import java.util.Random;

public class Manager {
    private boolean[][] forest;
    private boolean foundBear = false;
    private int curTasksAmount = -1;

    public Manager(int x, int y) {
        forest = new boolean[x][y];
        setBearLocation();
    }

    private void setBearLocation() {
        Random r = new Random();
        int bearX = r.nextInt(forest.length - 1);
        int bearY = r.nextInt(forest[0].length - 1);
        forest[bearX][bearY] = true;
    }

    public boolean isFoundBear() {
        return foundBear;
    }

    public void setFoundBear(boolean foundBear) {
        this.foundBear = foundBear;
    }

    public synchronized BeeTask getNextTask() {
        if(curTasksAmount + 1 < forest.length) {
            return new BeeTask(forest[++curTasksAmount], curTasksAmount);
        }
        return null;
    }

    public class BeeTask{
        private boolean [] area;
        private int y;

        public BeeTask(boolean[] area, int y) {
            this.area = area;
            this.y = y;
        }

        public boolean[] getArea() {
            return area;
        }

        public int getY() {
            return y;
        }
    }
}
