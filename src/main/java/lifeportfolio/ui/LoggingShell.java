package lifeportfolio.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import lifeportfolio.models.LifeEntry;
import lifeportfolio.models.LifeEntryComparator;
import lifeportfolio.service.*;
import lombok.Setter;

@ShellComponent
@ShellCommandGroup("[Logging commands]")
public class LoggingShell {
	@Autowired
	@Setter
	private LifeDbService service;
	
	@ShellMethod(value = "Add a new activity entry to log.")
	public String log(
			@ShellOption(
					value = { "--unit", "-u", "--message", "-m", "--desc", "-d" },
					help = "Name / description of the logged activity unit (without quotation marks)."
					) String[] entryMessageWords
			) {
		String entryMessage = String.join(" ", entryMessageWords);
		LifeEntry newEntry = new LifeEntry();
		newEntry.setDate(LocalDate.now());
		newEntry.setUnit(entryMessage);
		service.save(newEntry);
		return "Saved: \n" + newEntry.toString();
	}
	
	@ShellMethod(
			key = "rpt",
			value = "Copy an existing entry and create a new one, repeating this template."
			)
	public String repeat(
			@ShellOption(
					value = { "--id", "-i" },
					help = "ID of the existing template entry."
					) Long id,
			@ShellOption(
					value = { "--message", "-m" },
					help = "Specific message for a new entry (same as from the existing one by default).",
					defaultValue = ShellOption.NULL
					) String[] messageArr
			) {
		LifeEntry oldEntry = service.getById(id);
		LifeEntry newEntry = new LifeEntry();
		newEntry.setDate(LocalDate.now());
		newEntry.setCreatedDate(oldEntry.getCreatedDate());
		if (messageArr == null) {
			newEntry.setUnit(oldEntry.getUnit());
		} else {
			String message = String.join(" ", messageArr);
			String group = oldEntry.getGroup();
			newEntry.setUnit(String.format("%s: %s", group, message));
		}
		newEntry.setHours(oldEntry.getHours());
		newEntry.setImportance(oldEntry.getImportance());
		newEntry.setSatisfaction(oldEntry.getSatisfaction());
		newEntry.setArea(oldEntry.getArea());
		newEntry.setPermav(oldEntry.getPermav());
		service.save(newEntry);
		return "Saved: \n" + newEntry.toString();
	}
	
	@ShellMethod(value = "Set the date for the entry with the specified ID.")
	public String setDate(
			@ShellOption(
					value = { "--id", "-i" },
					help = "ID of the Entry to edit."
					) Long id,
			@ShellOption(
					value = { "--date", "-d" },
					help = "The date to set (yyyy-MM-dd) (default: today).",
					defaultValue = ShellOption.NULL
					) String dateStr,
			@ShellOption(
					value = { "--minus", "-m" },
					help = "Number of days to subtract.",
					defaultValue = "0"
					) Integer substract
			) {
		LifeEntry entry = service.getById(id);
		LocalDate date = LocalDate.now();
		if (dateStr != null) {
			date = LocalDate.parse(dateStr);
		}
		entry.setDate(date.minusDays(substract));
		service.save(entry);
		return "Saved: \n" + entry.toString();
	}
	
	@ShellMethod(
			key = "time",
			value = "Set the time value (in hours) for entry. Set hours below zero to delete entry."
			)
	public String setHours(
			@ShellOption(
					value = { "--id", "-i" },
					help = "ID of the Entry to edit."
					) Long id,
			@ShellOption(
					value = { "--hours", "--time", "-t" },
					help = "Time (in hours) to set (negative to delete).",
					defaultValue = "0"
					) Float hours
			) {
		LifeEntry entry = service.getById(id);
		if (hours < 0) {
			service.delete(entry);
			return "Deleted.";
		} else {
			entry.setHours(hours);
			service.save(entry);
			return "Saved: \n" + entry.toString();
		}
	}
	
	@ShellMethod(value = "Find group of entries")
	public String find(
			@ShellOption(
					value = { "--group-name", "-g", "-n" },
					help = "Name of the group"
					)
			String groupName,
			@ShellOption(
					value = { "--number", "-n" },
					help = "Limit the amount of entries to this number",
					defaultValue = "10"
					)
			Long limitNumber
			) {
		CompletableFuture<List<LifeEntry>> future = service.filterByGroup(groupName);
		List<LifeEntry> entries;
		try {
			entries = future.get();
			return String.join("\n", entries.stream()
					.sorted(LifeEntryComparator.get())
					.limit(limitNumber)
					.map(LifeEntry::toString)
					.toList()
					);
		} catch (InterruptedException | ExecutionException e) {
			return e.getLocalizedMessage();
		}
	}
}
