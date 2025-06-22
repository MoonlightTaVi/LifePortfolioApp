package lifeportfolio.db;

import org.springframework.data.jpa.repository.JpaRepository;

import lifeportfolio.models.LifeEntry;

public interface LifeRepo extends JpaRepository<LifeEntry, Long> {

}
