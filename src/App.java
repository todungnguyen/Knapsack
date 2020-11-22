import java.util.ArrayList;
import java.util.Arrays;

public class App {
    // number of objects (number of decision variables)
    private static int n;
    // bag capacity
    private static float B;
    private static float[] currentSolution;
    private static float[] finalSolution;
    private static int xi;

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

    public static float upperBound(Item[] items) {
        float weight = 0;
        float utility = 0;
        currentSolution = new float[n];

        for (int i = 0; i < finalSolution.length; i++) {
            if (finalSolution[i] != -1) {
                currentSolution[i] = finalSolution[i];
                weight += items[i].getWeight() * finalSolution[i];
                utility += items[i].getUtility() * finalSolution[i];
            }
        }

        for (Item item : items) {
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

    public static boolean isUnachievable(float[] finalSolution, Item[] items) {
        float total = 0;
        for (int i = 0; i < finalSolution.length; i++) {
            if (finalSolution[i] == 1) {
                for (Item item : items) {
                    if (item.getIndex() == i) {
                        total = total + item.getWeight();
                    }
                }
            }
        }
        return total > B;
    }

    public static boolean isSolution(Node current) {
        for (float i : current.getCurrentSolution()) {
            if (i != 1 && i != 0) {
                return false;
            }
        }
        return current.getUb() == (int) current.getUb();
    }

    public static void addNode(Node current, Item[] items, ArrayList<Node> queue, int finalValue) {
        finalSolution[xi] = finalValue;
        Node node = new Node(current.getLb(), upperBound(items), current.getLevel() + 1, finalSolution, currentSolution);
        queue.add(node);
    }

    public static Solution solve(Item[] items) {
        finalSolution = new float[n];
        for (int i = 0; i < n; i++) {
            finalSolution[i] = -1;
        }

        Arrays.sort(items, new sortByUtility());
        System.out.println("Array sorted by Utility: " + Arrays.toString(items));
        float LB = lowerBound(items);
        System.out.println("A possible admissible solution would be " + Arrays.toString(currentSolution) + " of value " + LB);
        System.out.println();

        /*********/
        Solution solution = new Solution(LB, currentSolution);
        /*********/

        Arrays.sort(items, new sortByRatio());
        System.out.println("Array sorted by Ratio: " + Arrays.toString(items));
        System.out.println();
        float UB = upperBound(items);

        Node current = new Node(LB, UB, 0, finalSolution, currentSolution);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(current);
        while (!queue.isEmpty()) {
            current = queue.remove(0);

            if (current.getUb() < current.getLb()) {
                current.setFlag(Flag.UBLTLB);
            } else {
                if (isUnachievable(current.getFinalSolution(), items)) {
                    current.setFlag(Flag.UNACHIEVABLE);
                } else if (isSolution(current)) {
                    if (current.getLb() <= current.getUb()) {
                        current.setFlag(Flag.SOLUTION);
                        current.setLb(current.getUb());
                        if (current.getUb() > solution.getValue()) {
                            solution = new Solution(current.getUb(), current.getCurrentSolution());
                        }
                    }
                }
            }

            System.out.println(current);

            if (current.getFlag() == Flag.CONTINUE) {
                finalSolution = current.getFinalSolution();
                for (int i = 0; i < n; i++) {
                    if (current.getCurrentSolution()[i] != 1.0 && current.getCurrentSolution()[i] != 0.0) {
                        xi = i;
                    }
                }

                addNode(current, items, queue, 0);
                addNode(current, items, queue, 1);
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        System.out.println("KNAPSACK PROBLEM SOLUTION");
        System.out.println();
        n = 4; // 38 (0, 1, 1, 0)
        B = 17;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 8, 3);
        items[1] = new Item(1, 18, 7);
        items[2] = new Item(2, 20, 9);
        items[3] = new Item(3, 11, 6);

        /*n = 5; // 235 (1, 0, 1, 1, 0)
        B = 10;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 40, 2);
        items[1] = new Item(1, 50, (float) 3.14);
        items[2] = new Item(2, 100, (float) 1.98);
        items[3] = new Item(3, 95, 5);
        items[4] = new Item(4, 30, 3);*/

        /*n = 3; // 220 (0, 1, 1)
        B = 50;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 60, 10);
        items[1] = new Item(1, 100, 20);
        items[2] = new Item(2, 120, 30);*/

        /*n = 4; // 200 (1, 1, 0, 1)
        B = 60;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 40, 20);
        items[1] = new Item(1, 100, 10);
        items[2] = new Item(2, 50, 40);
        items[3] = new Item(3, 60, 30);*/

        /*n = 6; // 60 (1, 1, 1, 1, 0, 1)
        B = 60;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 2, 10);
        items[1] = new Item(1, 10, 4);
        items[2] = new Item(2, 12, 20);
        items[3] = new Item(3, 18, 24);
        items[4] = new Item(4, 9, 18);
        items[5] = new Item(5, 10, 5);*/

        /*n = 4;
        B = 15;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 10, 2);
        items[1] = new Item(1, 10, 4);
        items[2] = new Item(2, 12, 6);
        items[3] = new Item(3, 18, 9);*/

        Solution solution = solve(items);
        System.out.println();
        System.out.println(solution);
    }
}
