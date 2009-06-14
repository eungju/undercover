package undercover.report;

import java.util.Comparator;

public class ItemComplexityAscending implements Comparator<Item> {
	public int compare(Item a, Item b) {
		return a.getBlockMetrics().getComplexity() - b.getBlockMetrics().getComplexity();
	}
}
