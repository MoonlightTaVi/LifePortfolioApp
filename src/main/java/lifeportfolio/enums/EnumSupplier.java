package lifeportfolio.enums;

public class EnumSupplier {
	
	public static Area getAreaFromName(String name) throws IllegalArgumentException {
		String format = name.replaceAll("^[a-zA-Z]+", "");
		format = name.replace("and", "And");
		return Area.valueOf(format);
	}
	
	public static Area findAreaFromName(String partialName) throws IllegalArgumentException {
		Area result = Area.UNKNOWN;
		for (Area area : Area.values()) {
			if (area.toString().toLowerCase().startsWith(partialName.toLowerCase())) {
				result = area;
				break;
			}
		}
		return result;
	}
	
	public static PERMAV findPermavFromName(String partialName) throws IllegalArgumentException {
		PERMAV result = PERMAV.UNKNOWN;
		for (PERMAV element : PERMAV.values()) {
			if (element.toString().toLowerCase().startsWith(partialName.toLowerCase())) {
				result = element;
				break;
			}
		}
		return result;
	}
	
	public static String getNameFromArea(Area area) {
		String areaName = null;
		switch (area) {
		case Relationships:
			areaName = "Relationships 🤗";
			break;
		case BodyMindSpirituality:
			areaName = "Body/Mind/Spirituality 🧘‍♀️";
			break;
		case CommunityAndSociety:
			areaName = "Community and Society 🌍";
			break;
		case JobLearningFinances:
			areaName = "Job/Learning/Finances 💼";
			break;
		case InterestsAndEntertainment:
			areaName = "Interests and Entertainment 🎨";
			break;
		case PersonalCare:
			areaName = "Personal Care 💆‍♂️";
			break;
		default:
			areaName = "<unknown area>";
		}
		return areaName;
	}
	
	public static String getElementName(PERMAV element) {
		StringBuilder builder = new StringBuilder();
		for (char ch : element.toString().toCharArray()) {
			if (Character.isUpperCase(ch)) {
				builder.append(" ");
			}
			builder.append(ch);
		}
		return builder.toString();
	}
	
}
