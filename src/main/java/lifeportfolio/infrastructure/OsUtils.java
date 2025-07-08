package lifeportfolio.infrastructure;

import java.io.File;
import java.io.IOException;

import lifeportfolio.enums.OS;

/**
 * Utilities for working with Operational System.
 */
public class OsUtils {
	
	/**
	 * Get the currently used Operational System.
	 * @return Enum describing the currently used OS
	 * ({@code OS.UNKNOWN} if could not fetch).
	 */
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
	
	/**
	 * For Windows: opens a directory, containing
	 * the file, in System Explorer, and selects (highlights) it. <br>
	 * For UNIX: Simply opens the said directory, using appropriate
	 * File Manager.
	 * @param fileToOpen File inside a directory that must be opened in Explorer.
	 * @throws IOException If can't open for some reason.
	 */
	public static void openInSystem(File fileToOpen) throws IOException {
		switch (getOs()) {
		case WINDOWS:
			Runtime.getRuntime()
			.exec(String.format("explorer.exe /select,\"%s\"", fileToOpen.getAbsolutePath()));
			break;
		case LINUX:
			Runtime.getRuntime()
			.exec(String.format("xdg-open \"%s\"", fileToOpen.getParentFile()));
			break;
		case MACOS:
			Runtime.getRuntime()
			.exec(String.format("open \"%s\"", fileToOpen.getParentFile()));
			break;
		default:
			throw new RuntimeException("Could not open file: Unknown Operational System");
		}
	}

}
