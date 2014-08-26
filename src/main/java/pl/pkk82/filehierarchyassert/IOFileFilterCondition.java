package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.assertj.core.api.Condition;

class IOFileFilterCondition extends Condition<File> implements IOFileFilter {
	private final String fileName;
	private final StringMatcher stringMatcher;
	private final IOFileFilter additionalFilter;

	public IOFileFilterCondition(String fileName, StringMatcher nameMatcher, IOFileFilter additionalFilter) {
		super("file name: " + fileName);
		this.fileName = fileName;
		this.stringMatcher = nameMatcher;
		this.additionalFilter = additionalFilter;
	}

	public IOFileFilterCondition(String dirName, StringMatcher nameMatcher) {
		this(dirName, nameMatcher, null);
	}

	@Override
	public boolean matches(File value) {
		Matcher matcher = Pattern.compile(stringMatcher.toRegex(fileName)).matcher(value.getName());
		return matcher.matches() && (additionalFilter == null || additionalFilter.accept(value));
	}

	@Override
	public boolean accept(File pathname) {
		return matches(pathname);
	}

	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
}
