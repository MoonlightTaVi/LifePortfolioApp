package lifeportfolio.service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
	private Set<LifeEntry> cachedEntries = new HashSet<>();
	
	
	public void save(LifeEntry entry) {
		entry = repository.save(entry);
		cachedEntries.add(entry);
	}
	public void delete(LifeEntry entry) {
		cachedEntries.remove(entry);
		repository.delete(entry);
	}
	
	public LifeEntry getById(long id) {
		LifeEntry entry = cachedEntries.parallelStream()
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
		cachedEntries.addAll(filterByLastWeek());
	}
	
	public List<LifeEntry> getWeekly() {
		return cachedEntries.stream()
				.filter(e -> e.getDate().isAfter(
						LocalDate.now().minusDays(7)
						))
				.sorted((a, b) -> b.getDate().compareTo(a.getDate()))
				.toList();
	}
	
	public List<LifeEntry> filterByLastWeek() {
		LocalDate now = LocalDate.now();
		return filterByDate(now.minusDays(6), now);
	}
	
	public List<LifeEntry> filterByDate(LocalDate from, LocalDate to) {
		return repository.filterByDate(from, to);
	}
	
	@Async("customTaskExecutor")
	public CompletableFuture<List<LifeEntry>> filterByGroup(String group) {
		return CompletableFuture.supplyAsync(
					() -> repository.filterbyGroup(group + "%")
					);
	}
	
	public List<LifeEntry> filter(
			long forPastWeeks,
			String nameStartsWith,
			String areaName,
			String elementName,
			boolean showImportant,
			boolean showUnimportant,
			boolean showSatisfied,
			boolean showUnsatisfied,
			float hours
			) {
		LocalDate now = LocalDate.now();
		return repository.filterByDate(
				now.minusWeeks(forPastWeeks),
				now
				)
				.stream().parallel()
				.filter( e -> {
					boolean filter = true;
					filter = filter && (nameStartsWith == null ||
							e.getUnit().toLowerCase()
							.startsWith(nameStartsWith.toLowerCase()));
					filter = filter && (areaName == null ||
							e.getArea().toString().toLowerCase()
							.contains(areaName));
					filter = filter &&
							(elementName == null ||
							e.getPermav().toString().toLowerCase()
							.contains(elementName));
					filter = filter &&
							(!showImportant || e.getImportance() >= 5);
					filter = filter &&
							(!showUnimportant || e.getImportance() <= 5);
					filter = filter &&
							(!showSatisfied || e.getSatisfaction() >= 5);
					filter = filter &&
							(!showUnsatisfied || e.getSatisfaction() <= 5);
					filter = filter &&
							(e.getHours().compareTo(hours) >= 0);
					return filter;
			}).sorted((a, b) -> b.getDate().compareTo(a.getDate())).toList();
	}

}
