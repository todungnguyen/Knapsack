import java.util.ArrayList;
import java.util.Arrays;

public class App {
    // number of objects (number of decision variables)
    private static int n;
    // bag capacity
    private static float B;
    private static float[] currentSolution;
    private static int[] finalSolution;
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
            if (finalSolution[i] == 1) {
                weight += items[i].getWeight();
                utility += items[i].getUtility();
            }
            if (finalSolution[i] != -1) {
                currentSolution[i] = finalSolution[i];
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

    public static boolean isUnachievable(int[] finalSolution, Item[] items) {
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
        return total >= B;
    }

    public static boolean isSolution(int[] finalSolution, Item[] items) {
        for (int j : finalSolution) {
            if (j == -1) {
                return false;
            }
        }
        return !isUnachievable(finalSolution, items);
    }

    public static Node solve(Item[] items) {
        Node solution = null;

        finalSolution = new int[n];
        for (int i = 0; i < n; i++) {
            finalSolution[i] = -1;
        }

        Arrays.sort(items, new sortByUtility());
        System.out.println("Array sorted by Utility: " + Arrays.toString(items));

        float LB = lowerBound(items);
        System.out.println("A possible admissible solution would be " + Arrays.toString(currentSolution) + " of value " + LB);

        Arrays.sort(items, new sortByRatio());
        System.out.println("Array sorted by Ratio: " + Arrays.toString(items));
        System.out.println();
        float UB = upperBound(items);

        Node current = new Node(UB, LB, 0, finalSolution, currentSolution);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(current);
        while (!queue.isEmpty()) {
            current = queue.remove(0);
            if (isSolution(current.getFinalSolution(), items)) {
                current.setFlag(Flag.SOLUTION);
                solution = new Node(current.getUb(), current.getLb(), current.getLevel(), current.getFinalSolution(), current.getCurrentSolution());
            }
            if (isUnachievable(current.getFinalSolution(), items)) {
                current.setFlag(Flag.UNACHIEVABLE);
            }
            if (current.getUb() < current.getLb()) {
                current.setFlag(Flag.UBLTLB);
            }
            System.out.println(current);

            if ((current.getLevel() == 0 || current.getUb() >= current.getLb()) && current.getFlag() == Flag.CONTINUE) {
                finalSolution = current.getFinalSolution();
                for (int i = 0; i < n; i++) {
                    if (currentSolution[i] != 1.0 && currentSolution[i] != 0.0) {
                        xi = i;
                    }
                }
                finalSolution[xi] = 0;
                Node left = new Node(upperBound(items), current.getLb(), current.getLevel() + 1, finalSolution, currentSolution);
                queue.add(left);

                finalSolution[xi] = 1;
                Node right = new Node(upperBound(items), current.getLb(), current.getLevel() + 1, finalSolution, currentSolution);
                queue.add(right);
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        System.out.println("KNAPSACK PROBLEM SOLUTION");
        System.out.println();
        n = 4;
        B = 17;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 8, 3);
        items[1] = new Item(1, 18, 7);
        items[2] = new Item(2, 20, 9);
        items[3] = new Item(3, 11, 6);

        /*n = 5;
        B = 10;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 40, 2);
        items[1] = new Item(1, 50, (float) 3.14);
        items[2] = new Item(2, 100, (float) 1.98);
        items[3] = new Item(3, 95, 5);
        items[4] = new Item(4, 30, 3);*/

        Node solution = solve(items);
        System.out.println();
        System.out.println("OPTIMAL SOLUTION " + Arrays.toString(solution.getFinalSolution()) + " of value " + solution.getUb());
    }
}
