package lifeportfolio.db;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lifeportfolio.models.LifeEntry;

public interface LifeRepo extends JpaRepository<LifeEntry, Long> {

	@Query("SELECT e FROM LifeEntry e WHERE e.date >= :from AND e.date <= :to")
	public List<LifeEntry> filterByDate(
			@Param("from") LocalDate from,
			@Param("to") LocalDate to
			);
	
}
