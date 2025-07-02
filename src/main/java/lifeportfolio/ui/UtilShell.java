package lifeportfolio.ui;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import lifeportfolio.service.*;
import lombok.Setter;

@ShellComponent
@ShellCommandGroup("[Other commands]")
public class UtilShell {
	@Autowired
	@Setter
	private LifeDbService service;
	@Autowired
	@Setter
	private OutputService writer;
	
	@ShellMethod(key = "gen-report", value = "Generate weekly report and save to a CSV file.")
	public String generateReport() {
		String[] result = new String[2];
		File savedFile = writer.generateReport(service.filterByLastWeek());
		result[0] = savedFile.getAbsolutePath();
		result[1] = writer.openOutputFile(savedFile);
		return String.join("\n", result);
	}
	
}
