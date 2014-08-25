package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.assertj.core.api.Condition;

class IOFileFilterCondition extends Condition<File> implements IOFileFilter {
	private final String rootDirName;
	private final StringMatcher stringMatcher;

	public IOFileFilterCondition(String rootDirName, StringMatcher stringMatcher) {
		super("file name: " + rootDirName);
		this.rootDirName = rootDirName;
		this.stringMatcher = stringMatcher;
	}

	@Override
	public boolean matches(File value) {
		Matcher matcher = Pattern.compile(stringMatcher.toRegex(rootDirName)).matcher(value.getName());
		return matcher.matches();
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
