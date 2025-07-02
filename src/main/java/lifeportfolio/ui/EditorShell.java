package lifeportfolio.ui;

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
	
	@ShellMethod(value = "Set satisfaction / importance values for a log entry with the specified ID.")
	public String set(
			@ShellOption(
					value = { "--id", "-I" },
					help = "Entry ID."
					) Long id,
			@ShellOption(
					value = { "--importance", "-i" },
					help = "Importance (0-10).",
					defaultValue = ShellOption.NULL
					) Integer importance,
			@ShellOption(
					value = { "--satisfaction", "-s" },
					help = "Satisfaction (0-10).",
					defaultValue = ShellOption.NULL
					) Integer satisfaction
			) {
		LifeEntry entry = service.getById(id);
		if (importance != null) {
			importance = Math.min(Math.max(importance, 0), 10);
			entry.setImportance(importance);
		}
		if (satisfaction != null) {
			satisfaction = Math.min(Math.max(satisfaction, 0), 10);
			entry.setSatisfaction(satisfaction);
		}
		service.save(entry);
		return "Saved: \n" + entry.toString();
	}
	
}
