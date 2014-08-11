package pl.pkk82.filehierarchyassert;

import java.util.regex.Pattern;

public enum NameMatcher {

	STANDARD("equal"), REGEX("matching");

	private final String description;

	private NameMatcher(String description) {
		this.description = description;
	}

	public String toRegex(String name) {
		if (this == STANDARD) {
			return Pattern.quote(name);
		} else {
			return name;
		}
	}

	public String getDescription() {
		return description;
	}
}