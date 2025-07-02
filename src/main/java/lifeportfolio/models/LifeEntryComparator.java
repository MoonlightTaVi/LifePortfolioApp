package lifeportfolio.models;

import java.util.Comparator;

public class LifeEntryComparator implements Comparator<LifeEntry> {
	
	public static LifeEntryComparator get() {
		return new LifeEntryComparator();
	}

	@Override
	public int compare(LifeEntry o1, LifeEntry o2) {
		return o1.compareTo(o2);
	}

}
