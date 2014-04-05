/**
 * SortType.java
 * 
 * An enumerable to represent the different sort types.
 * 
 * @author MikeYeaple
 *
 */
public enum SortType {
	Insertion(0),
	Count(1),
	Quick(2);
	
	private final int value;
    private SortType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
