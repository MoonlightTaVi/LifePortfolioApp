package lifeportfolio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
	
	
	/**
	 * Retrieves database connection credentials
	 */
	@Autowired
	private Environment env;
	/**
	 * Sets SQLite configuration
	 * @return DataSource configuration bean for SQLite
	 */
	@Bean
	public DataSource dataSource() {
	    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("driverClassName"));
	    dataSource.setUrl(env.getProperty("url"));
	    dataSource.setUsername(env.getProperty("user"));
	    dataSource.setPassword(env.getProperty("password"));
	    return dataSource;
	}
	
}
