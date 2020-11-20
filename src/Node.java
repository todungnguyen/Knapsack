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

    private Flag flag;

    public Node(float ub, float lb, int level, int[] finalSolution, float[] currentSolution) {
        this.ub = ub;
        this.lb = lb;
        this.level = level;
        this.finalSolution = new int[finalSolution.length];
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

    public int getLevel() {
        return level;
    }

    public int[] getFinalSolution() {
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
