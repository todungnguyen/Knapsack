import java.util.Comparator;

public class sortByUtility implements Comparator<Item> {
    public int compare(Item a, Item b) {
        boolean temp = a.getUtility() > b.getUtility();
        return temp ? -1 : 1; // ordre décroissant
    }
}