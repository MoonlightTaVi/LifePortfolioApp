package lifeportfolio.models;

import java.time.LocalDate;

import jakarta.persistence.*;
import lifeportfolio.enums.*;
import lombok.*;

@Entity
@Table(name = "life-entries")
public class LifeEntry {
	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Setter
	@Getter
	private LocalDate date = LocalDate.now();
	@Setter
	@Getter
	private Float hours = 0f;
	@Setter
	@Getter
	private Area area = Area.UNKNOWN;
	@Setter
	@Getter
	private PERMAV permav = PERMAV.UNKNOWN;
	@Setter
	@Getter
	private Integer importance = 5;
	@Setter
	@Getter
	private Integer satisfaction = 5;
	@Setter
	@Getter
	private String unit = "<unknown unit>";
	
	@Override
	public boolean equals(Object anotherObject) {
		boolean equals = false;
		if (anotherObject instanceof LifeEntry anotherEntry) {
			equals = this.id == anotherEntry.getId();
		}
		return equals;
	}
	
	@Override
	public int hashCode() {
		return id.intValue();
	}
	
	@Override
	public String toString() {
		return String.format(
				"%d. (🗓️%s) 🚩%s [%s]\n - (⏱️ %.2f hrs) \"%s\" %d 🌟 / %d ❤️",
				id,
				date,
				EnumSupplier.getNameFromArea(area),
				permav,
				hours,
				unit,
				importance,
				satisfaction
				);
	}

}
