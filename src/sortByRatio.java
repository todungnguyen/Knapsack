import java.util.Comparator;

public class sortByRatio implements Comparator<Item> {
    public int compare(Item a, Item b) {
        boolean temp = (float) a.getUtility() / a.getWeight() > (float) b.getUtility() / b.getWeight();
        return temp ? -1 : 1; // descending order
    }
}