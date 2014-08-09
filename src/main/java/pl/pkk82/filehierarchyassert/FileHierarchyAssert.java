package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;

import pl.pkk82.filehierarchygenerator.FileHierarchy;

public class FileHierarchyAssert extends AbstractAssert<FileHierarchyAssert, FileHierarchy> {

	private final NameMatcher nameMatcher;

	public FileHierarchyAssert(FileHierarchy actual, NameMatcher nameMatcher) {
		super(actual, FileHierarchyAssert.class);
		this.nameMatcher = nameMatcher;
		exists();
	}

	public FileHierarchyAssert(FileHierarchy actual) {
		this(actual, NameMatcher.STANDARD);
	}

	public FileHierarchyAssert(File actual, NameMatcher nameMatcher) {
		this(actual == null ? null : new FileHierarchy(actual.toPath()), nameMatcher);
	}


	public FileHierarchyAssert(File actual) {
		this(actual, NameMatcher.STANDARD);
	}

	public FileHierarchyAssert(Path actual, NameMatcher nameMatcher) {
		this(actual == null ? null : new FileHierarchy(actual), nameMatcher);
	}


	public FileHierarchyAssert(Path actual) {
		this(actual, NameMatcher.STANDARD);
	}


	public FileHierarchyAssert hasRootDirWithName(final String rootDirName) {
		return hasRootDirWithName(rootDirName, nameMatcher);
	}

	public FileHierarchyAssert hasRootDirWithName(final String rootDirName, final NameMatcher nameMatcher) {
		then(actual.getRootDirectoryAsFile()).has(new FileNameCondition(rootDirName, nameMatcher));
		return this;
	}

	public FileHierarchyAssert hasParentDirWithName(final String parentDirName) {
		return hasParentDirWithName(parentDirName, this.nameMatcher);
	}


	public FileHierarchyAssert hasParentDirWithName(final String parentDirName, final NameMatcher nameMatcher) {
		then(actual.getRootDirectoryAsFile().getParentFile()).has(new FileNameCondition(parentDirName, nameMatcher));
		return this;
	}

	public FileHierarchyAssert hasCountOfFilesAndDirsInPath(int count, String... dirPath) {
		Collection<File> files = findFilesAndDirsRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage(String.format("Expecting:\n %s\nto contain:\n %s\nbut contains:\n %s",
						actual.getRootDirectoryAsPath().toString(),
						prepareFileAndDirDesc(count),
						prepareFileAndDirDesc(files.size())))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfDirsInPath(int count, String... dirPath) {
		Collection<File> files = findDirsRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage(String.format("Expecting:\n %s\nto contain:\n %s\nbut contains:\n %s",
						actual.getRootDirectoryAsPath().toString(),
						prepareDirDesc(count),
						prepareDirDesc(files.size())))
				.hasSize(count);
		return this;
	}


	public FileHierarchyAssert hasCountOfSubdirsInPath(int count, String... dirPath) {
		Collection<File> files = findSubdirsRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage(String.format("Expecting:\n %s\nto contain:\n %s\nbut contains:\n %s",
						actual.getRootDirectoryAsPath().toString(),
						prepareDirDesc(count),
						prepareDirDesc(files.size())))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfFilesInPath(int count, String... dirPath) {
		Collection<File> files = findFilesRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage(String.format("Expecting:\n %s\nto contain:\n %s\nbut contains:\n %s",
						actual.getRootDirectoryAsPath().toString(),
						prepareFileDesc(count),
						prepareFileDesc(files.size())))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert containsSubdirInPath(String dirName, String... dirPath) {
		return containsSubdirInPath(dirName, nameMatcher, dirPath);
	}

	public FileHierarchyAssert containsSubdirInPath(String dirName, NameMatcher nameMatcher, String... dirPath) {
		File rootDir = calculateDirFile(dirPath);
		IOFileFilter searchFilter = DirectoryFileFilter.INSTANCE;
		FileFilter dirFilter = new AndFileFilter(searchFilter, new FileNameCondition(dirName, nameMatcher));
		List<File> candidates = Arrays.asList(rootDir.listFiles((FileFilter) searchFilter));
		List<File> result = Arrays.asList(rootDir.listFiles(dirFilter));
		then(result.size())
				.overridingErrorMessage("Expecting one of:\n%shas name %s to:\n %s",
						prepareDirDesc(candidates),
						nameMatcher.getDescription(),
						dirName)
				.isGreaterThan(0);
		return this;
	}

	private Collection<File> findDirsRecursively(File rootDir) {
		return FileUtils.listFilesAndDirs(rootDir, FalseFileFilter.FALSE, TrueFileFilter.INSTANCE);
	}


	private Collection<File> findSubdirsRecursively(File rootDir) {
		Collection<File> directories = FileUtils.listFilesAndDirs(rootDir, FalseFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		directories.remove(rootDir);
		return directories;
	}

	private Collection<File> findFilesRecursively(File rootDir) {
		return FileUtils.listFiles(rootDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	private Collection<File> findFilesAndDirsRecursively(File rootDir) {
		return FileUtils.listFilesAndDirs(rootDir, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
	}

	private Path calculateFilePath(String fileName, String... dirPath) {
		Path actualPath = calculateDirPath(dirPath);
		actualPath = actualPath.resolve(fileName);
		then(actualPath.toFile()).exists().isFile();
		return actualPath;
	}

	private Path calculateDirPath(String... dirPath) {
		Path actualPath = actual.getRootDirectoryAsPath();
		then(actualPath.toFile()).exists().isDirectory();
		for (String directoryName : dirPath) {
			actualPath = actualPath.resolve(directoryName);
			then(actualPath.toFile()).exists().isDirectory();
		}
		return actualPath;
	}

	private File calculateDirFile(String... dirPath) {
		return calculateDirPath(dirPath).toFile();
	}


	private String prepareDirDesc(List<File> candidates) {
		StringBuffer buffer = new StringBuffer();
		if (candidates.isEmpty()) {
			buffer.append(" <no candidates>\n");
		} else {
			for (File candidate : candidates) {
				buffer.append(" <");
				buffer.append(candidate.toPath());
				buffer.append(">\n");
			}
		}
		return buffer.toString();
	}

	private String prepareFileAndDirDesc(int count) {
		return String.format("%d %s", count, count == 1 ? "file/directory" : "files/directories");
	}

	private String prepareDirDesc(int noDirs) {
		return String.format("%d %s", noDirs, noDirs == 1 ? "directory" : "directories");
	}

	private String prepareFileDesc(int noFiles) {
		return String.format("%d %s", noFiles, noFiles == 1 ? "file" : "files");
	}

	private FileHierarchyAssert exists() {
		isNotNull();
		then(actual.getRootDirectoryAsFile()).exists().isDirectory();
		return this;
	}


	private static class FileNameCondition extends Condition<File> implements IOFileFilter {
		private final String rootDirName;
		private final NameMatcher nameMatcher;

		public FileNameCondition(String rootDirName, NameMatcher nameMatcher) {
			super("file name: " + rootDirName);
			this.rootDirName = rootDirName;
			this.nameMatcher = nameMatcher;
		}

		@Override
		public boolean matches(File value) {
			Matcher matcher = Pattern.compile(nameMatcher.toRegex(rootDirName)).matcher(value.getName());
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
}