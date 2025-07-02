package lifeportfolio.enums;

import java.time.LocalDate;
import java.util.*;

import lifeportfolio.models.LifeEntry;

public class EnumListing {

	/**
	 * Returns String[] representation of Activity Areas, counting occurrences
	 * of each area inside the specified list of LifeEntries.
	 * @param occurrences List of LifeEntries (for area statistic).
	 * @return array of Area names + occurrences count for each area.
	 */
	public String[] listAreas(Collection<LifeEntry> occurrences) {
		Map<Area, Integer> areas = new HashMap<>();
		for (Area area : Area.values()) {
			areas.put(area, 0);
		}
		occurrences.stream().filter(e -> e.getDate().isAfter(LocalDate.now().minusDays(7))).forEach(e -> {
			Area area = e.getArea();
			int count = areas.get(area);
			count++;
			areas.put(area, count);
		});
		String[] list = new String[areas.size()];
		int i = 0;
		for (Area area : Area.values()) {
			list[i] = String.format(
					"%d. %s (%d)",
					i++,
					EnumSupplier.getNameFromArea(area),
					areas.get(area)
					);
		}
		return list;
	}
	
	/**
	 * Returns String[] representation of PERMAV Elements, counting occurrences
	 * of each element inside the specified list of LifeEntries.
	 * @param occurrences List of LifeEntries (for element statistic).
	 * @return array of Element names + occurrences count for each element.
	 */
	public String[] listElements(Collection<LifeEntry> occurrences) {
		Map<PERMAV, Integer> elements = new HashMap<>();
		for (PERMAV element : PERMAV.values()) {
			elements.put(element, 0);
		}
		occurrences.stream().filter(e -> e.getDate().isAfter(LocalDate.now().minusDays(7))).forEach(e -> {
			PERMAV element = e.getPermav();
			int count = elements.get(element);
			count++;
			elements.put(element, count);
		});
		String[] list = new String[elements.size()];
		int i = 0;
		for (PERMAV element : PERMAV.values()) {
			list[i] = String.format(
					"%d. %s (%d)",
					i++,
					EnumSupplier.getElementName(element),
					elements.get(element)
					);
		}
		return list;
	}
	
}
