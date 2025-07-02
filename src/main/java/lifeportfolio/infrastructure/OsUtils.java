package lifeportfolio.infrastructure;

import java.io.File;
import java.io.IOException;

import lifeportfolio.enums.OS;

public class OsUtils {
	
	public static OS getOs() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("win")) {
		    return OS.WINDOWS;
		} else if (osName.contains("nux")) {
			return OS.LINUX;
		} else if (osName.contains("mac")) {
			return OS.MACOS;
		} else {
		    return OS.UNKNOWN;
		}
	}
	
	public static void openInSystem(File fileToOpen) throws IOException {
		switch (getOs()) {
		case WINDOWS:
			Runtime.getRuntime()
			.exec(String.format("explorer.exe /select,\"%s\"", fileToOpen.getAbsolutePath()));
			break;
		case LINUX:
			Runtime.getRuntime()
			.exec(String.format("xdg-open \"%s\"", fileToOpen.getAbsolutePath()));
			break;
		case MACOS:
			Runtime.getRuntime()
			.exec(String.format("open \"%s\"", fileToOpen.getAbsolutePath()));
			break;
		default:
			throw new RuntimeException("Could not open file: Unknown Operational System");
		}
	}

}
