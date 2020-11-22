import java.util.Arrays;

public class Solution {
    private final float value;
    private final float[] solution;

    public Solution(float value, float[] solution) {
        this.value = value;
        this.solution = solution;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Optimal solution{" +
                "valeur=" + value +
                ", solution=" + Arrays.toString(solution) +
                '}';
    }
}
