package lifeportfolio.service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lifeportfolio.db.LifeRepo;
import lifeportfolio.models.LifeEntry;
import lifeportfolio.models.LifeEntryComparator;
import lombok.Getter;
import lombok.Setter;

@Service
public class LifeDbService implements InitializingBean {
	@Setter
	@Autowired
	private LifeRepo repository;
	@Getter
	private Set<LifeEntry> cachedEntries = new HashSet<>();
	
	/**
	 * Save an existing or a new LifeEntry to the database.
	 * @param entry LifeEntry to be saved.
	 */
	public void save(LifeEntry entry) {
		entry = repository.save(entry);
		cachedEntries.add(entry);
	}
	/**
	 * Delete an existing LifeEntry from the database. 
	 * @param entry LifeEntry to be removed.
	 * @return true if deleted successfully, false if the
	 * Entry is not existent inside the database.
	 * @throws OptimisticLockingFailureException If the Entry exists,
	 * but could not be deleted
	 * due to optimistic locking.
	 */
	public boolean delete(LifeEntry entry) throws OptimisticLockingFailureException {
		boolean success = false;
		cachedEntries.remove(entry);
		Long id = entry.getId();
		if (id == null) {
			id = -1L;
		}
		if (repository.existsById(id)) {
			repository.delete(entry);
			success = true;
		}
		return success;
	}
	
	/**
	 * First tries to find the LifeEntry with the specified ID in cache;
	 * then searches through DB. <br>
	 * It is faster then searching by other attributes.
	 * @param id ID of the LifeEntry.
	 * @return LifeEntry with the specified ID if exists, null otherwise.
	 */
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
	
	/**
	 * Adds all Entries of the past week to the cache
	 * on startup.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		cachedEntries.addAll(filterByLastWeek());
	}
	
	/**
	 * Fetches logged Entries, filtering by last week, from cache.
	 * @return List of Entries, filtered by date.
	 * @see #afterPropertiesSet()
	 * @see #getWeekly(long)
	 */
	public List<LifeEntry> getWeekly() {
		return getWeekly(0);
	}
	
	/**
	 * Fetches logged Entries, filtering by last week, from cache.
	 * @param limitNumber Max amount of Entries in the List.
	 * @return List of Entries, filtered by date and limited by max size.
	 * @see #afterPropertiesSet()
	 */
	public List<LifeEntry> getWeekly(long limitNumber) {
		if (limitNumber <= 0) {
			limitNumber = cachedEntries.size();
		}
		return cachedEntries.stream()
				.filter(e -> e.getDate().isAfter(
						LocalDate.now().minusDays(7)
						))
				.sorted(LifeEntryComparator.get())
				.limit(limitNumber)
				.toList();
	}
	
	/**
	 * Fetches all Entries of the past week from the database.
	 * @return List of Entries, filtered by date (past week).
	 */
	public List<LifeEntry> filterByLastWeek() {
		LocalDate now = LocalDate.now();
		return filterByDate(now.minusDays(6), now);
	}
	
	/**
	 * Fetches all Entries of the specified time period from the database.
	 * @param from Beginning of the time period (inclusive)
	 * @param to Ending of the time period (inclusive)
	 * @return List of Entries, filtered by date.
	 */
	public List<LifeEntry> filterByDate(LocalDate from, LocalDate to) {
		return repository.filterByDate(from, to);
	}
	
	/**
	 * Filters all Entries by group name. Repository uses {@code LIKE}
	 * SQL keyword for this purpose.
	 * @param group LifeEntry must start with this sequence of characters.
	 * @return CompletableFuture, that collects the result in another Thread
	 * (must be awaited for completion).
	 */
	@Async("customTaskExecutor")
	public CompletableFuture<List<LifeEntry>> filterByGroup(String group) {
		return CompletableFuture.supplyAsync(
					() -> repository.filterbyGroup(group + "%")
					);
	}
	
	/**
	 * Filters all Entries by the given parameters and
	 * returns the result.
	 * @param endDate Final date to be taken into account.
	 * @param forPastWeeks Time period to subtract from endDate (weeks)
	 * to find the starting point of filtering.
	 * @param forPastDays Time period to subtract from endDate (days)
	 * to find the starting point of filtering. Days are added to {@code forPastWeeks} weeks
	 * @param nameContains Partial LifeEntry name.
	 * @param areaName Partial LifeEntry Area name.
	 * @param elementName Partial LifeEntry PERMAV name.
	 * @param showImportant Filter by: only importance >= 5.
	 * @param showUnimportant Filter by: only importance <= 5.
	 * @param showSatisfied Filter by: only satisfaction >= 5.
	 * @param showUnsatisfied Filter by: only satisfaction <= 5.
	 * @param hours Filter by: only hours >= {this number}.
	 * @return Filtered List of LifeEntries.
	 */
	public List<LifeEntry> filter(
			LocalDate endDate,
			long forPastWeeks,
			long forPastDays,
			String nameContains,
			String areaName,
			String elementName,
			boolean showImportant,
			boolean showUnimportant,
			boolean showSatisfied,
			boolean showUnsatisfied,
			float hours
			) {
		return repository.filterByDate(
				endDate.minusDays(7 * forPastWeeks + forPastDays).plusDays(1),
				endDate
				)
				.stream().parallel()
				.filter( e -> {
					boolean filter = true;
					filter = filter && (nameContains == null ||
							e.getUnit().toLowerCase()
							.contains(nameContains.toLowerCase()));
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
			}).sorted(LifeEntryComparator.get()).toList();
	}

}
