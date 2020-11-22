import java.util.ArrayList;
import java.util.Arrays;

public class App {
    // nombre d'objet (nombre de variable de décision)
    private static int n;
    // capacité maximum
    private static float B;
    // liste de poid
    private static float[] weights;
    // liste d'utilité
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
                break;
            }
        }
        return utility;
    }

    // Déterminer la résolution (en variables continues) en chaque noeud à l'aide de Fayard et Plateau
    public static float upperBound(Item[] items) {
        float weight = 0;
        float utility = 0;
        currentSolution = new float[n];

        for (int i = 0; i < finalSolution.length; i++) {
            // si la valeur de l'objet i est connu (0 ou 1)
            if (finalSolution[i] != -1) {
                currentSolution[i] = finalSolution[i];
                weight += items[i].getWeight() * finalSolution[i];
                utility += items[i].getUtility() * finalSolution[i];
            }
        }

        for (Item item : items) {
            // continue le calcul avec les objets qu'on a pas encore connu les valeurs
            if (finalSolution[item.getIndex()] == -1) {
                if (weight + item.getWeight() <= B) {
                    currentSolution[item.getIndex()] = 1;
                    weight += item.getWeight();
                    utility += item.getUtility();
                } else {
                    currentSolution[item.getIndex()] = (B - weight) / item.getWeight();
                    utility += item.getUtility() * (B - weight) / item.getWeight();
                    break;
                }
            }
        }
        return utility;
    }

    // Vérifier si la solution est irréalisable
    public static boolean isUnachievable(float[] solution, Item[] items) {
        float total = 0;
        for (int i = 0; i < solution.length; i++) {
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
            if (i != 1 && i != 0) {
                return false;
            }
        }
        return true;
    }

    // Ajouter un nouveau noeud
    public static void addNode(Node current, Item[] items, ArrayList<Node> queue, int finalValue) {
        finalSolution[xi] = finalValue;
        Node node = new Node(current.getLb(), upperBound(items), current.getLevel() + 1, finalSolution, currentSolution);
        queue.add(node);
    }

    // L'algo de Branch and Bound
    public static Solution solve(Item[] items) {
        finalSolution = new float[n];
        for (int i = 0; i < n; i++) {
            finalSolution[i] = -1;
        }

        // 1. Initialisation
            // calculer un LB
        Arrays.sort(items, new sortByUtility());
        System.out.println("Array sorted by Utility: " + Arrays.toString(items));
        float LB = lowerBound(items);
        System.out.println("A possible admissible solution would be " + Arrays.toString(currentSolution) + " of value " + LB);
        System.out.println();
        Solution solutionOptimal = new Solution(LB, currentSolution);

            // calculer un UB
        Arrays.sort(items, new sortByRatio());
        System.out.println("Array sorted by Ratio: " + Arrays.toString(items));
        System.out.println();
        float UB = upperBound(items);

        // construire l'arbre
        Node current = new Node(LB, UB, 0, finalSolution, currentSolution);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(current);

        while (!queue.isEmpty()) {
            current = queue.remove(0);
            // 4. vérifier les conditions arrêts (ne poursuivre pas) pour chaque noeud
                // si UBi < LB => arrêt
            if (current.getUb() < current.getLb()) {
                current.setFlag(Flag.UBLTLB);
                // sinon <=> Ubi >= LB
            } else {
                // si le solution est irréalisable => arrêt
                if (isUnachievable(current.getCurrentSolution(), items)) {
                    current.setFlag(Flag.UNACHIEVABLE);
                }
                // si les valeurs sont entiers => arrêt
                else if (isInteger(current.getCurrentSolution())) {
                    // c'est une solution admissible
                    current.setFlag(Flag.SOLUTION);
                    // UBi est entier => LB = UBi
                    current.setLb(current.getUb());
                    // mais il faut vérifier si c'est la solution optimale
                    if (current.getUb() > solutionOptimal.getValue()) {
                        solutionOptimal = new Solution(current.getUb(), current.getCurrentSolution());
                    }
                }
            }

            System.out.println(current);

            if (current.getFlag() == Flag.CONTINUE) {
                finalSolution = current.getFinalSolution();
                // 2. chosir une variable xi qui est le plus fractionnaire dans la solution fournie par continue
                for (int i = 0; i < n; i++) {
                    if (current.getCurrentSolution()[i] != 1.0 && current.getCurrentSolution()[i] != 0.0) {
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
        System.out.println("KNAPSACK PROBLEM SOLUTION");
        System.out.println();

        // J'ai mis des tests différents
        // Vous pouvez les tester ou faire un nouveau fonction de test (qui contient les valeurs de n, B et les objets) comme au-dessous
        // Puis l'appeler ici pour tester
        test1();
        items = new Item[n];
        for(int i = 0; i < n; i++) {
            items[i] = new Item(i, utilities[i], weights[i]);
        }
        Solution solution = solve(items);

        System.out.println();
        System.out.println(solution);
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

    // 50 (0, 1, 1, 1, 0, 1)
    public static void test5() {
        n = 6;
        B = 60;
        weights = new float[] {10, 4, 20, 24, 18, 5};
        utilities = new float[] {2, 10, 12, 18, 9, 10};
    }

    // 56 (1, 1, 0, 1)
    public static void test6() {
        n = 4;
        B = 21;
        weights = new float[] {6, 3, 5, 9};
        utilities = new float[] {18, 20, 14, 18};
    }

    // 49 (0, 1, 1, 1, 0)
    public static void test7() {
        n = 5;
        B = 26;
        weights = new float[] {12, 7, 11, 8, 9};
        utilities = new float[] {24, 13, 23, 15, 16};
    }
}
