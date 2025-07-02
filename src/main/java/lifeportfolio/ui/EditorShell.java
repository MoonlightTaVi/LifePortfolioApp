package lifeportfolio.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import lifeportfolio.enums.*;
import lifeportfolio.models.LifeEntry;
import lifeportfolio.service.LifeDbService;
import lombok.Setter;

@ShellComponent
@ShellCommandGroup("[Entry editing]")
public class EditorShell {
	@Autowired
	@Setter
	private LifeDbService service;
	private EnumListing enumListing = new EnumListing();

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
		String[] list = enumListing.listAreas(service.getCachedEntries());
		return String.join("\n", list);
	}
	
	@ShellMethod(
			value = "List PERMAV elements."
			)
	public String permav() {
		String[] list = enumListing.listElements(service.getCachedEntries());
		return String.join("\n", list);
	}
	
}
