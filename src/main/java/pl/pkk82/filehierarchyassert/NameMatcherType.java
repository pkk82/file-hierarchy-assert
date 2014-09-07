package pl.pkk82.filehierarchyassert;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public enum NameMatcherType {

	STANDARD("equal"), REGEX("matching");

	private final String description;

	private NameMatcherType(String description) {
		this.description = description;
	}

	public boolean matches(String name, String pattern) {
		if (this == STANDARD) {
			return StringUtils.equals(name, pattern);
		} else {
			return Pattern.compile(pattern).matcher(name).matches();
		}
	}


	public String getDescription() {
		return description;
	}
}
