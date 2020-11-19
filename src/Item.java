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

    public void setIndex(int index) {
        this.index = index;
    }

    public float getUtility() {
        return utility;
    }

    public void setUtility(float utility) {
        this.utility = utility;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
