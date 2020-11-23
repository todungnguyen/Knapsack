import java.util.ArrayList;
import java.util.Arrays;

public class App {
    // nombre d'objet (nombre de variable de décision)
    private static int n;
    // capacité maximum du sac
    private static float B;
    // liste de poid (les coefficients de la contraintes)
    private static float[] weights;
    // liste d'utilité (les coefficients objectifs)
    private static float[] utilities;
    // liste d'objet
    private static Item[] items;
    // solution actuelle (après le calcul)
    private static float[] currentSolution;
    // solution final (pour vois quel objet a obtenu un valeur final) (contient que 3 valeur: -1, 0 ou 1)
    // par exemple: finalSolution[i] = -1 => la valeur de l'objet i n'a pas encore connu
    // finalSolution[i] = 1 => la valeur de l'objet i est 1
    private static float[] finalSolution;
    // variable qui est la plus fractionnaire dans la solution fournie
    private static int xi;
    // Lower Bound
    private static float LB;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    // Déterminer une solution admissible (en entier) à l'aide de Heuristique Gloutonne
    public static float lowerBound(Item[] items) {
        float weight = 0;
        float utility = 0;
        currentSolution = new float[n];

        for (Item item : items) {
            if (weight + item.getWeight() <= B) {
                currentSolution[item.getIndex()] = 1;
                weight += item.getWeight();
                utility += item.getUtility();
            } else {
                return utility;
            }
        }
        return utility;
    }

    // Déterminer la résolution (en variables continues) en chaque noeud à l'aide de Fayard et Plateau
    public static float upperBound(Item[] items) {
        float weight = 0;
        float utility = 0;
        currentSolution = new float[n];

        // si la valeur de l'objet i est connu (0 ou 1)
        for (Item item : items) {
            int i = item.getIndex();
            if (finalSolution[i] != -1) {
                currentSolution[i] = finalSolution[i];
                weight += item.getWeight() * finalSolution[i];
                utility += item.getUtility() * finalSolution[i];
            }
        }

        // sinon, continue le calcul avec les objets qu'on a pas encore connu ses valeurs
        for (Item item : items) {
            int i = item.getIndex();
            if (finalSolution[i] == -1) {
                if (weight + item.getWeight() <= B) {
                    currentSolution[i] = 1;
                    weight += item.getWeight();
                    utility += item.getUtility();
                } else {
                    currentSolution[i] = (B - weight) / item.getWeight();
                    utility += item.getUtility() * currentSolution[i];
                    return utility;
                }
            }
        }
        return utility;
    }

    // Vérifier si la solution est irréalisable
    public static boolean isUnachievable(float[] solution, Item[] items) {
        float total = 0;
        for (int i = 0; i < n; i++) {
            if (solution[i] == 1) {
                for (Item item : items) {
                    if (item.getIndex() == i) {
                        total = total + item.getWeight();
                    }
                }
            }
        }
        return total > B;
    }

    // Vérifier si les valeurs de la solution sont entiers
    public static boolean isInteger(float[] solution) {
        for (float i : solution) {
            if (i != (int) i) {
                return false;
            }
        }
        return true;
    }

    // Ajouter un nouveau noeud
    public static void addNode(Node current, Item[] items, ArrayList<Node> queue, int finalValue) {
        finalSolution[xi] = finalValue;
        Node node = new Node(upperBound(items), current.getLevel() + 1, finalSolution, currentSolution);
        queue.add(node);
    }

    // L'algo de Branch and Bound
    public static float[] solve(Item[] items) {
        float[] solutionOptimal = new float[n];

        finalSolution = new float[n];
        for (int i = 0; i < n; i++) {
            finalSolution[i] = -1;
        }

        // 1. Initialisation
            // calculer un LB
        Arrays.sort(items, new sortByUtility());
        System.out.println("Array sorted by Utility: " + Arrays.toString(items));
        LB = lowerBound(items);
        System.out.println(ANSI_RED + "A possible admissible solution would be " + Arrays.toString(currentSolution) + " of value " + LB + ANSI_RESET);
        System.out.println();
        solutionOptimal = currentSolution;

            // calculer un UB
        Arrays.sort(items, new sortByRatio());
        System.out.println("Array sorted by Ratio: " + Arrays.toString(items));
        float UB = upperBound(items);
        System.out.println();

        // construire l'arbre
        Node current = new Node(UB, 0, finalSolution, currentSolution);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.remove(0);
            System.out.println(ANSI_GREEN + "Node level " + current.getLevel() + ":" + ANSI_RESET);
            // 4. vérifier les conditions arrêts pour chaque noeud ( <=> ne poursuivre pas sur ce noeud )
                // si UBi < LB => arrêt
            if (current.getUb() < LB) {
                current.setFlag(Flag.UBLTLB);
                // sinon <=> Ubi >= LB
            } else {
                // si le solution est irréalisable => arrêt
                if (isUnachievable(current.getFinalSolution(), items)) {
                    current.setFlag(Flag.UNACHIEVABLE);
                }
                else {
                    // si les valeurs de la solution actuelle sont entiers => arrêt
                    if (isInteger(current.getCurrentSolution())) {
                        current.setFlag(Flag.SOLUTION);
                        // si LB < UBi alors LB = UBi
                        if (LB < current.getUb()) {
                            System.out.println(ANSI_RED + "Update LB from " + LB + " to " + current.getUb() + ANSI_RESET);
                            LB = current.getUb();
                            solutionOptimal = current.getCurrentSolution();
                        }
                    }
                }
            }

            System.out.println(current);

            if (current.getFlag() == Flag.CONTINUE) {
                finalSolution = current.getFinalSolution();
                // 2. choisir une variable xi qui est le plus fractionnaire dans la solution fournie par continue
                for (int i = 0; i < n; i++) {
                    if (current.getCurrentSolution()[i] != (int) current.getCurrentSolution()[i] ) {
                        xi = i;
                    }
                }
                // 3. diviser l'arbre selon 2 contraintes et calculer Ubi pour chaque noeud i
                addNode(current, items, queue, 0);
                addNode(current, items, queue, 1);
            }
        }
        return solutionOptimal;
    }

    public static void main(String[] args) {
        System.out.println(ANSI_RED + "KNAPSACK PROBLEM SOLUTION" + ANSI_RESET);
        System.out.println();

        // J'ai mis des tests différents
        // Vous pouvez les tester ou faire un nouveau fonction de test comme au-dessous (ou bien mettre directement les codes ici)
        // Puis l'appeler ici pour tester
        test1();

        items = new Item[n];
        for(int i = 0; i < n; i++) {
            items[i] = new Item(i, utilities[i], weights[i]);
        }

        float[] solution = solve(items);
        System.out.println();
        System.out.println(ANSI_RED + "OPTIMAL SOLUTION: value = " + LB + ", solution = " + Arrays.toString(solution) + ANSI_RESET);
    }

    // 38 (0, 1, 1, 0)
    public static void test1() {
        n = 4;
        B = 17;
        weights = new float[] {3, 7, 9, 6};
        utilities = new float[] {8, 18, 20, 11};
    }

    // 235 (1, 0, 1, 1, 0)
    public static void test2() {
        n = 5;
        B = 10;
        weights = new float[] {2, (float) 3.14, (float) 1.98, 5, 3};
        utilities = new float[] {40, 50, 100, 95, 30};
    }

    // 220 (0, 1, 1)
    public static void test3() {
        n = 3;
        B = 50;
        weights = new float[] {10, 20, 30};
        utilities = new float[] {60, 100, 120};
    }

    // 200 (1, 1, 0, 1)
    public static void test4() {
        n = 4;
        B = 60;
        weights = new float[] {20, 10, 40, 30};
        utilities = new float[] {40, 100, 50, 60};
    }

    // 51 (0, 1, 1, 1, 0)
    public static void test5() {
        n = 5;
        B = 26;
        weights = new float[] {12, 7, 11, 8, 9};
        utilities = new float[] {24, 13, 23, 15, 16};
    }
}
