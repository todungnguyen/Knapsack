import java.util.Arrays;

public class Node {
    // Level of the node in the decision tree
    private int level;

    private float[] currentSolution;

    // Lower Bound: Worst case (0/1)
    private float lb;

    // Upper Bound: Best case (Fractional Knapsack)
    private float ub;

    private int[] finalSolution;

    public Node(float ub, float lb, int level, int[] finalSolution, float[] currentSolution) {
        this.ub = ub;
        this.lb = lb;
        this.level = level;
        this.finalSolution = new int[finalSolution.length];
        for(int i = 0; i < finalSolution.length; i++) {
            this.finalSolution[i] = finalSolution[i];
        }
        this.currentSolution = new float[currentSolution.length];
        for(int i = 0; i < currentSolution.length; i++) {
            this.currentSolution[i] = currentSolution[i];
        }
    }

    public float getUb() {
        return ub;
    }

    public void setUb(float ub) {
        this.ub = ub;
    }

    public float getLb() {
        return lb;
    }

    public void setLb(float lb) {
        this.lb = lb;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getFinalSolution() {
        return finalSolution;
    }

    public void setFinalSolution(int[] finalSolution) {
        this.finalSolution = finalSolution;
    }

    public float[] getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(float[] currentSolution) {
        this.currentSolution = currentSolution;
    }

    @Override
    public String toString() {
        return  "Level=" + level +
                ", currentSolution=" + Arrays.toString(currentSolution) +
                ", LB=" + lb +
                ", UB=" + ub +
                ", finalSolution=" + Arrays.toString(finalSolution);
    }
}
