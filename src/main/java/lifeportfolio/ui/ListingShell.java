package lifeportfolio.ui;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import lifeportfolio.enums.EnumListing;
import lifeportfolio.models.LifeEntry;
import lifeportfolio.service.LifeDbService;
import lifeportfolio.ui.util.TextUtils;
import lombok.Setter;

@ShellComponent
@ShellCommandGroup("[Listing commands]")
public class ListingShell {
	@Autowired
	@Setter
	private LifeDbService service;
	private EnumListing enumListing = new EnumListing();

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

	@ShellMethod(
			value = "List weekly entries."
			)
	public String list(
			@ShellOption(
					value = { "--number", "-n" },
					help = "Limit results to this number of entries",
					defaultValue = "7"
					)
			Long limitNumber
			) {
		List<LifeEntry> entries = service.getWeekly();
		String entriesStr = String.join(
				"\n",
				entries
				.stream()
				.limit(limitNumber)
				.map(e -> e.toString())
				.toList()
				);
		String summary = TextUtils.summaryFromEntries(entries, entry -> entry.getGroup());
		return String.format("%s\n\n%s\n", entriesStr, summary);
	}
	
	@ShellMethod(
			value = "List filtered entries."
			)
	public String filter(
			@ShellOption(
					value = { "--name", "-n" },
					help = "(Optional) Unit name starts with this sequence of characters.",
					defaultValue = ShellOption.NULL
					) String nameStartsWith,
			@ShellOption(
					value = { "--area", "-a" },
					help = "(Optional) Filter by partial area name.",
					defaultValue = ShellOption.NULL
					) String areaName,
			@ShellOption(
					value = { "--element", "-e" },
					help = "(Optional) Filter by partial PERMAV element name.",
					defaultValue = ShellOption.NULL
					) String elementName,
			@ShellOption(
					value = { "--important", "-i" },
					help = "(Optional) Filter by importance >= 5.",
					defaultValue = "false"
					) Boolean showImportant,
			@ShellOption(
					value = { "--unimportant", "-I" },
					help = "(Optional) Filter by importance <= 5.",
					defaultValue = "false"
					) Boolean showUnimportant,
			@ShellOption(
					value = { "--satisfied", "-s" },
					help = "(Optional) Filter by satisfaction >= 5.",
					defaultValue = "false"
					) Boolean showSatisfied,
			@ShellOption(
					value = { "--unsatisfied", "-S" },
					help = "(Optional) Filter by satisfaction <= 5.",
					defaultValue = "false"
					) Boolean showUnsatisfied,
			@ShellOption(
					value = { "--weeks", "-w" },
					help = "(Optional) Filter by occurences in this number of passed weeks.",
					defaultValue = "1"
					) Long weeks,
			@ShellOption(
					value = { "--hours", "--h", "--time", "-t" },
					help = "(Optional) Filter by hours >= {number}.",
					defaultValue = "0"
					) Float hours
			) {
		List<LifeEntry> entries = service.filter(
				weeks,
				nameStartsWith,
				areaName,
				elementName,
				showImportant,
				showUnimportant,
				showSatisfied,
				showUnsatisfied,
				hours
				);
		
		return String.format(
				"%s\nSummary:\n%s\n",
				String.join("\n", entries.stream()
						.map(e -> e.toString())
						.toList()
						),
				TextUtils.summaryFromEntries(entries, entry -> entry.getGroup())
				);
	}
}
