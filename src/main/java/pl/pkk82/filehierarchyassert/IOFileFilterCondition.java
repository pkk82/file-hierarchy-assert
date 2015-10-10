package pl.pkk82.filehierarchyassert;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

class IOFileFilterCondition implements IOFileFilter {
	private final String fileName;
	private final NameMatcherType nameMatcherType;
	private final IOFileFilter additionalFilter;

	public IOFileFilterCondition(String fileName, NameMatcherType nameMatcherType, IOFileFilter additionalFilter) {
		this.fileName = fileName;
		this.nameMatcherType = nameMatcherType;
		this.additionalFilter = additionalFilter;
	}

	public IOFileFilterCondition(String dirName, NameMatcherType nameMatcherType) {
		this(dirName, nameMatcherType, null);
	}

	public boolean matches(File value) {
		return nameMatcherType.matches(value.getName(), fileName) && (additionalFilter == null || additionalFilter.accept(value));
	}

	public String getDescription() {
		return "file name: " + fileName;
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
