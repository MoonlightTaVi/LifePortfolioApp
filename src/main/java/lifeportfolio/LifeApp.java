package lifeportfolio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LifeApp {

	public static void main(String[] args) {
		createFolders();
		ConfigurableApplicationContext context = SpringApplication.run(LifeApp.class, args);
		context.close();
		
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
