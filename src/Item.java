public class Item {
    private int index;
    private float utility;
    private float weight;

    public Item(int index, float utility, float weight) {
        this.index = index;
        this.utility = utility;
        this.weight = weight;
    }

    public int getIndex() {
        return index;
    }

    public float getUtility() {
        return utility;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "index=" + index +
                ", utility=" + utility +
                ", weight=" + weight +
                '}';
    }
}
