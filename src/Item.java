public class Item {
    private final int index;
    private final float utility;
    private final float weight;

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
        return "{" + index + ", " + utility + ", " + weight + "}";
    }
}
