import java.util.Arrays;

public class Node {
    // Level of the node in the decision tree
    private final int level;

    private final float[] currentSolution;

    // Lower Bound: Worst case (0/1)
    private float lb;

    // Upper Bound: Best case (Fractional Knapsack)
    private final float ub;

    private final float[] finalSolution;

    private Flag flag;

    public Node(float lb, float ub, int level, float[] finalSolution, float[] currentSolution) {
        this.ub = ub;
        this.lb = lb;
        this.level = level;
        this.finalSolution = new float[finalSolution.length];
        System.arraycopy(finalSolution, 0, this.finalSolution, 0, finalSolution.length);
        this.currentSolution = new float[currentSolution.length];
        System.arraycopy(currentSolution, 0, this.currentSolution, 0, currentSolution.length);
        this.flag = Flag.CONTINUE;
    }

    public float getUb() {
        return ub;
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

    public float[] getFinalSolution() {
        return finalSolution;
    }

    public float[] getCurrentSolution() {
        return currentSolution;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Node{" +
                "level=" + level +
                ", currentSolution=" + Arrays.toString(currentSolution) +
                ", lb=" + lb +
                ", ub=" + ub +
                ", finalSolution=" + Arrays.toString(finalSolution) +
                ", flag=" + flag +
                '}';
    }
}
