import java.util.Arrays;

public class Solution {
    private double valeur;
    private double[] solution;

    public Solution(int n) {
        this.valeur = 0;
        this.solution = new double[n];
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public double[] getSolution() {
        return solution;
    }

    public void setSolution(double[] solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        String s = "( ";
        for(int i = 0; i < solution.length; i++) {
            s += solution[i] + " ";
        }
        s += ") de valeur " + valeur;
        return s;
    }
}

