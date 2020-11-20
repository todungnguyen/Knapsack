import java.util.Comparator;

public class sortByRatio implements Comparator<Item> {
    public int compare(Item a, Item b) {
        boolean temp = a.getUtility() / a.getWeight() > b.getUtility() / b.getWeight();
        return temp ? -1 : 1; // descending order
    }
}