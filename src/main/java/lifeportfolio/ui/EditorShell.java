package lifeportfolio.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import lifeportfolio.enums.Area;
import lifeportfolio.enums.EnumSupplier;
import lifeportfolio.enums.PERMAV;
import lifeportfolio.models.LifeEntry;
import lifeportfolio.service.LifeDbService;
import lombok.Setter;

@ShellComponent
@ShellCommandGroup("[Entry editing]")
public class EditorShell {
	@Autowired
	@Setter
	private LifeDbService service;

	@ShellMethod(value = "Set an Activity Area for a log entry.")
	public String setArea(
			@ShellOption(
					value = { "--id", "-I" },
					help = "ID of the entry to edit."
					)
			Long id,
			@ShellOption(
					value = { "--area", "-a" },
					help = "Area name (may be partial) to set."
					)
			String areaName
			) {
		LifeEntry entry = service.getById(id);
		Area area = EnumSupplier.findAreaFromName(areaName);
		entry.setArea(area);
		service.save(entry);
		return "Saved: " + entry.toString();
	}

	@ShellMethod(
			key = "set-el",
			value = "Set a PERMAV element fot the existing log entry."
			)
	public String setElement(
			@ShellOption(
					value = { "--id", "-I" },
					help = "ID of the entry to edit."
					)
			Long id,
			@ShellOption(
					value = { "--element", "-e" },
					help = "PERMAV element name (may be partial) to set."
					)
			String elementName
			) {
		LifeEntry entry = service.getById(id);
		PERMAV permav = EnumSupplier.findPermavFromName(elementName);
		entry.setPermav(permav);
		service.save(entry);
		return "Saved: " + entry.toString();
	}
	
	@ShellMethod(
			value = "List Activity areas."
			)
	public String areas() {
		Map<Area, Integer> areas = new HashMap<>();
		for (Area area : Area.values()) {
			areas.put(area, 0);
		}
		service.getCachedEntries().stream().filter(e -> e.getDate().isAfter(LocalDate.now().minusDays(7))).forEach(e -> {
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
		return String.join("\n", list);
	}
	
	@ShellMethod(
			value = "List PERMAV elements."
			)
	public String permav() {
		Map<PERMAV, Integer> elements = new HashMap<>();
		for (PERMAV element : PERMAV.values()) {
			elements.put(element, 0);
		}
		service.getCachedEntries().stream().filter(e -> e.getDate().isAfter(LocalDate.now().minusDays(7))).forEach(e -> {
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
		return String.join("\n", list);
	}
	
}
