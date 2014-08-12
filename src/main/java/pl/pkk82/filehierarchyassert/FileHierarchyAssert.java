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
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;

import pl.pkk82.filehierarchygenerator.FileHierarchy;

public class FileHierarchyAssert extends AbstractAssert<FileHierarchyAssert, FileHierarchy> {

	private static final String DESC_FILE_SING = "file";
	private static final String DESC_FILE_PLURAL = "files";

	private static final String DESC_DIR_SING = "directory";
	private static final String DESC_DIR_PLURAL = "directories";

	private static final String DESC_FILE_AND_DIR_SING = "file/directory";
	private static final String DESC_FILE_AND_DIR_PLURAL = "files/directories";

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
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPath(actual.getRootDirectoryAsPath()),
						descCount(count, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL),
						descCountWithDetails(files, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL))
				.hasSize(count);
		return this;
	}

	private String descCount(int count, String descSingular, String descPlural) {
		if (count == 1) {
			return String.format(" <1> %s", descSingular);
		} else {
			return String.format(" <%d> %s", count, descPlural);
		}
	}

	public FileHierarchyAssert hasCountOfDirsInPath(int count, String... dirPath) {
		Collection<File> files = findDirsRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPath(actual.getRootDirectoryAsPath()),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(files, DESC_DIR_SING, DESC_DIR_PLURAL))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfSubdirsInPath(int count, String... dirPath) {
		Collection<File> files = findSubdirsRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage(String.format("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPath(actual.getRootDirectoryAsPath()),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(files, DESC_DIR_SING, DESC_DIR_PLURAL)))
				.hasSize(count);
		return this;
	}


	public FileHierarchyAssert hasCountOfFilesInPath(int count, String... dirPath) {
		Collection<File> files = findFilesRecursively(calculateDirFile(dirPath));
		then(files)
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPath(actual.getRootDirectoryAsPath()),
						descCount(count, DESC_FILE_SING, DESC_FILE_PLURAL),
						descCountWithDetails(files, DESC_FILE_SING, DESC_FILE_PLURAL))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert containsSubdirInPath(String dirName, String... dirPath) {
		return containsSubdirInPath(dirName, nameMatcher, dirPath);
	}

	public FileHierarchyAssert containsSubdirInPath(String dirName, NameMatcher nameMatcher, String... dirPath) {
		File searchDir = calculateDirFile(dirPath);
		IOFileFilter searchFilter = DirectoryFileFilter.INSTANCE;
		FileFilter dirFilter = new AndFileFilter(searchFilter, new FileNameCondition(dirName, nameMatcher));
		List<File> candidates = Arrays.asList(searchDir.listFiles((FileFilter) searchFilter));
		List<File> result = Arrays.asList(searchDir.listFiles(dirFilter));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a directory with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n", descPath(searchDir.toPath()),
						nameMatcher.getDescription(),
						descName(dirName),
						descPaths(candidates, " <no directories>"))
				.isGreaterThan(0);
		return this;
	}


	public FileHierarchyAssert containsFileInPath(String fileName, String... dirPath) {
		return containsFileInPath(fileName, nameMatcher, dirPath);
	}

	public FileHierarchyAssert containsFileInPath(String fileName, NameMatcher nameMatcher, String... dirPath) {
		File searchDir = calculateDirFile(dirPath);
		IOFileFilter searchFilter = FileFileFilter.FILE;
		FileFilter fileFilter = new AndFileFilter(searchFilter, new FileNameCondition(fileName, nameMatcher));
		List<File> candidates = Arrays.asList(searchDir.listFiles((FileFilter) searchFilter));
		List<File> result = Arrays.asList(searchDir.listFiles(fileFilter));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a file with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n",
						descPath(searchDir.toPath()),
						nameMatcher.getDescription(),
						descName(fileName),
						descPaths(candidates, " <no files>"))
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
		then(actualPath.toFile())
				.overridingErrorMessage("\nExpecting:\n%s\nto be an existing directory\n",
						descPath(actualPath))
				.exists().isDirectory();
		for (String directoryName : dirPath) {
			actualPath = actualPath.resolve(directoryName);
			then(actualPath.toFile())
					.overridingErrorMessage("\nExpecting:\n%s\nto be an existing directory\n",
							descPath(actualPath))
					.exists().isDirectory();
		}
		return actualPath;
	}

	private File calculateDirFile(String... dirPath) {
		return calculateDirPath(dirPath).toFile();
	}

	private String descCountWithDetails(Collection<File> files, String descSingular, String descPlural) {
		if (files.isEmpty()) {
			return String.format(" <0> %s", descPlural);
		} else if (files.size() == 1) {
			return String.format(" <1> %s\n%s", descSingular, descPaths(files));
		} else {
			return String.format(" <%d> %s\n%s", files.size(), descPlural, descPaths(files));
		}
	}

	private String descName(String name) {
		return String.format(" <%s>", name);
	}

	private String descPath(Path path) {
		return String.format(" <%s>", path);
	}

	private String descPaths(Collection<File> paths) {
		return descPaths(paths, "");
	}


	private String descPaths(Collection<File> paths, String whenEmpty) {
		StringBuilder buffer = new StringBuilder("");
		if (paths.isEmpty()) {
			buffer.append(whenEmpty);
		} else {
			int i = paths.size();
			for (File candidate : paths) {
				buffer.append(descPath(candidate.toPath()));
				if (--i != 0) {
					buffer.append('\n');
				}

			}
		}
		return buffer.toString();
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