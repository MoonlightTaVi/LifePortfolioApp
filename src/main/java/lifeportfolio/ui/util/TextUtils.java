package lifeportfolio.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lifeportfolio.enums.Area;
import lifeportfolio.enums.EnumSupplier;
import lifeportfolio.models.LifeEntry;

public class TextUtils {

	public static String summaryFromEntries(Collection<LifeEntry> entries) {
		// Collect
			Map<Area, Float> allHours = new HashMap<>();
			Map<Area, List<Integer>> allSatisfaction = new HashMap<>();
			Map<Area, List<Integer>> allImportance = new HashMap<>();
			for (LifeEntry entry : entries) {
				float hours = allHours.getOrDefault(entry.getArea(), 0f);
				List<Integer> satisfaction = allSatisfaction.getOrDefault(
						entry.getArea(), new ArrayList<>()
						);
				List<Integer> importance = allImportance.getOrDefault(
						entry.getArea(), new ArrayList<>()
						);
				hours += entry.getHours();
				satisfaction.add(entry.getSatisfaction());
				importance.add(entry.getImportance());
				allHours.put(entry.getArea(), hours);
				allSatisfaction.put(entry.getArea(), satisfaction);
				allImportance.put(entry.getArea(), importance);
			}
			List<String> summary = new ArrayList<>();
			for (Area area : allSatisfaction.keySet()) {
				// Average
				int size = allSatisfaction.get(area).size();
				int satisfactionAvg = 0;
				int importanceAvg = 0;
				for (int i = 0; i < size; i++) {
					satisfactionAvg += allSatisfaction.get(area).get(i);
					importanceAvg += allImportance.get(area).get(i);
				}
				// To String
				summary.add(
						String.format(
								"[%s]\n\t%.2f hrs\n\t%d satisfaction / %d importance",
						EnumSupplier.getNameFromArea(area),
						allHours.get(area),
						satisfactionAvg / size,
						importanceAvg / size
						)
						);
			}
			return String.join("\n", summary);
	}
	
}
