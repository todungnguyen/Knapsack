import java.util.Arrays;

public class Node {
    // niveau du noeud dans l'arbbre de décision
    private final int level;
    // solution actuelle
    private final float[] currentSolution;
    // Upper Bound
    private final float ub;
    // solution final (pour vois quel objet a obtenu un valeur final) (contient que 3 valeur: -1, 0 ou 1)
    // par exemple: finalSolution[i] = -1 => la valeur de l'objet i n'a pas encore connu
    // finalSolution[i] = 1 => dans ce noeud, la valeur de l'objet i est 1
    private final float[] finalSolution;
    // Lower Bound
    private float lb;
    // état du noeud
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
