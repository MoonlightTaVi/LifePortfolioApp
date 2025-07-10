package lifeportfolio.ui.util;

import java.util.*;
import java.util.function.Function;

import lifeportfolio.models.LifeEntry;

/**
 * A set of static methods, used by UI elements,
 * for working with textual descriptions and other info.
 */
public class TextUtils {

	/**
	 * Collects info from Life Entries and makes a text summary
	 * on total hours, average satisfaction and importance values,
	 * grouping the result with specified Function.
	 * @param <T> Type of a group key.
	 * @param entries Collection of Life Entries to make the summary for.
	 * @param groupBy The Function to identify the group an Entry belongs to.
	 * @return Textual summary of a strict format (plain text).
	 */
	public static <T> String summaryFromEntries(Collection<LifeEntry> entries, Function<LifeEntry, T> groupBy) {
		// Sort groups alphabetically
		Set<T> groups = new TreeSet<>();
		// Collect
		Map<T, Float> allHours = new HashMap<>();
		Map<T, List<Integer>> allSatisfaction = new HashMap<>();
		Map<T, List<Integer>> allImportance = new HashMap<>();
		for (LifeEntry entry : entries) {
			T group = groupBy.apply(entry);
			groups.add(group);
			
			float hours = allHours.getOrDefault(group, 0f);
			List<Integer> satisfaction = allSatisfaction.getOrDefault(
					group, new ArrayList<>()
					);
			List<Integer> importance = allImportance.getOrDefault(
					group, new ArrayList<>()
					);
			hours += entry.getHours();
			satisfaction.add(entry.getSatisfaction());
			importance.add(entry.getImportance());
			allHours.put(group, hours);
			allSatisfaction.put(group, satisfaction);
			allImportance.put(group, importance);
		}
		List<String> summary = new ArrayList<>();
		for (T group : groups) {
			// Average
			int size = allSatisfaction.get(group).size();
			int satisfactionAvg = 0;
			int importanceAvg = 0;
			for (int i = 0; i < size; i++) {
				satisfactionAvg += allSatisfaction.get(group).get(i);
				importanceAvg += allImportance.get(group).get(i);
			}
			satisfactionAvg /= size;
			importanceAvg /= size;
			// To String
			summary.add(
					String.format(
							"[%s] \n\t ⏱️%.2f hrs \n\t Average: ❤️ %d satisfaction / 🌟 %d importance",
					group.toString(),
					allHours.get(group),
					satisfactionAvg,
					importanceAvg
					)
					);
		}
		return String.join("\n", summary);
	}
	
}
