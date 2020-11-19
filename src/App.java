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

        for(int i = 0; i < finalSolution.length; i++) {
            if(finalSolution[i] == 1) {
                weight += items[i].getWeight();
                utility += items[i].getUtility();
            }
            if(finalSolution[i] != -1) {
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

    public static void main(String[] args) {
        System.out.println("Knapsack problem solution");
        n = 4;
        B = 17;
        // list of item
        Item[] items = new Item[n];
        items[0] = new Item(0, 8, 3);
        items[1] = new Item(1, 18, 7);
        items[2] = new Item(2, 20, 9);
        items[3] = new Item(3, 11, 6);

        finalSolution = new int[n];
        for(int i = 0; i < n; i++) {
            finalSolution[i] = -1;
        }

        Arrays.sort(items, new sortByUtility());
        System.out.println("Array sorted by Utility: " + Arrays.toString(items));

        float LB = lowerBound(items);
        System.out.println("Une solution admissible possible serait " + Arrays.toString(currentSolution) + " de valeur " + LB);

        Arrays.sort(items, new sortByRatio());
        System.out.println("Array sorted by Ratio: " + Arrays.toString(items));
        float UB = upperBound(items);

        Node current = new Node(UB, LB, 0, finalSolution, currentSolution);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(current);
        int count = 0;
        while(!queue.isEmpty() && count < 9) {
            count++;
            current = queue.remove(0);
            System.out.println(current);

            if(current.getLevel() == 0 || current.getUb() >= current.getLb()) {
                finalSolution = current.getFinalSolution();
                for(int i = 0; i < n; i++) {
                    if(currentSolution[i] != 1.0 && currentSolution[i] != 0.0) {
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
            System.out.println();
        }
    }
}
