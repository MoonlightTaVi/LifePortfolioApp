package lifeportfolio.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lifeportfolio.db.LifeRepo;
import lifeportfolio.models.LifeEntry;
import lombok.Getter;
import lombok.Setter;

@Service
public class LifeDbService implements InitializingBean {
	@Setter
	@Autowired
	private LifeRepo repository;
	@Getter
	private Set<LifeEntry> weekEntries = new HashSet<>();
	
	
	public void save(LifeEntry entry) {
		weekEntries.add(entry);
		repository.save(entry);
	}
	public void delete(LifeEntry entry) {
		weekEntries.remove(entry);
		repository.delete(entry);
	}
	
	public LifeEntry getById(long id) {
		LifeEntry entry = weekEntries.parallelStream()
				.filter(e -> e.getId().longValue() == id)
				.findAny()
				.orElse(null);
		if (entry == null) {
			entry = repository.findById(id).orElse(null);
		}
		return entry;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		LocalDate now = LocalDate.now();
		weekEntries.addAll(
				filter(
						e -> e.getDate()
						.isAfter(now.minusDays(7))
						)
				);
	}
	
	public List<LifeEntry> filter(Predicate<LifeEntry> predicate) {
		return repository
				.findAll()
				.parallelStream()
				.filter(predicate)
				.toList();
	}
	
	

}
