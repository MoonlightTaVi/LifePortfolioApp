package lifeportfolio.service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;

import lifeportfolio.enums.EnumSupplier;
import lifeportfolio.infrastructure.OsUtils;
import lifeportfolio.models.LifeEntry;

@Service
public class OutputService {

	public String generateReportName() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		return LocalDateTime.now().format(format) + ".csv";
	}
	
	public File generateReport(List<LifeEntry> weeklyEntries) {
		Map<String, Float> hours = new HashMap<>();
		Map<String, String> areas = new HashMap<>();
		Map<String, Integer> satisfaction = new HashMap<>();
		Map<String, Integer> importance = new HashMap<>();
		Map<String, Integer> count = new HashMap<>();
		weeklyEntries.sort(Comparator.reverseOrder());
		for (LifeEntry entry : weeklyEntries) {
			String unitName = entry.getGroup();
			hours.put(unitName, hours.getOrDefault(unitName, 0f) + entry.getHours());
			areas.put(unitName, EnumSupplier.getNameFromArea(entry.getArea()));
			satisfaction.put(unitName, satisfaction.getOrDefault(unitName, 0) + entry.getSatisfaction());
			importance.put(unitName, importance.getOrDefault(unitName, 0) + entry.getImportance());
			count.put(unitName, count.getOrDefault(unitName, 0) + 1);
		}
		String[] unitNames = areas.keySet().toArray(String[]::new);
		String[][] table = new String[unitNames.length + 1][5];
		table[0] = new String[] {
				"\"Area\"","\"Unit\"","\"Importance (0-10)\"","\"Satisfaction (0-10)\"","\"Time (hours/week)\""
		};
		int i = 1;
		for (String unitName : unitNames) {
			table[i++] = new String[] {
					String.format("\"%s\"", areas.get(unitName)),
					String.format("\"%s\"", unitName),
					String.valueOf(importance.get(unitName) / count.get(unitName)),
					String.valueOf(satisfaction.get(unitName) / count.get(unitName)),
					String.valueOf(hours.get(unitName).intValue())
			};
		}
		String filename = generateReportName();
		File file = new File("output/" + filename);
		try (FileWriter writer = new FileWriter(file)) {
			for (int l = 0; l < table.length; l++) {
				writer.append(String.join(",", table[l]));
				if (l < table.length - 1) {
					writer.append("\n");
				}
			}
		} catch (IOException e) {
			return null;
		}
		return file;
	}
	
	public String openOutputFile(File file) {
		String result = "Successfully opened.";
		try {
			OsUtils.openInSystem(file);
		} catch (Exception e) {
			result = e.getLocalizedMessage();
		}
		return result;
	}
	
}
