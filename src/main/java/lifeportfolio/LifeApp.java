package lifeportfolio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LifeApp {

	public static void main(String[] args) {
		createFolders();
		SpringApplication.run(LifeApp.class, args);
		
	}
	public static boolean createFolders() {
		try {
			Files.createDirectories(Paths.get("./data"));
			Files.createDirectories(Paths.get("./output"));
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
