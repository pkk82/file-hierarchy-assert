package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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

	private final StringMatcher stringMatcher;

	public FileHierarchyAssert(FileHierarchy actual, StringMatcher stringMatcher) {
		super(actual, FileHierarchyAssert.class);
		this.stringMatcher = stringMatcher;
		exists();
	}

	public FileHierarchyAssert(FileHierarchy actual) {
		this(actual, StringMatcher.STANDARD);
	}

	public FileHierarchyAssert(File actual, StringMatcher stringMatcher) {
		this(actual == null ? null : new FileHierarchy(actual.toPath()), stringMatcher);
	}


	public FileHierarchyAssert(File actual) {
		this(actual, StringMatcher.STANDARD);
	}

	public FileHierarchyAssert(Path actual, StringMatcher stringMatcher) {
		this(actual == null ? null : new FileHierarchy(actual), stringMatcher);
	}


	public FileHierarchyAssert(Path actual) {
		this(actual, StringMatcher.STANDARD);
	}


	public FileHierarchyAssert hasRootDirWithName(final String rootDirName) {
		return hasRootDirWithName(rootDirName, stringMatcher);
	}

	public FileHierarchyAssert hasRootDirWithName(final String rootDirName, final StringMatcher rootDirNameMatcher) {
		then(actual.getRootDirectoryAsFile()).has(new FileNameCondition(rootDirName, rootDirNameMatcher));
		return this;
	}

	public FileHierarchyAssert hasParentDirWithName(final String parentDirName) {
		return hasParentDirWithName(parentDirName, this.stringMatcher);
	}


	public FileHierarchyAssert hasParentDirWithName(final String parentDirName,
			final StringMatcher parentDirNameMatcher) {
		then(actual.getRootDirectoryAsFile().getParentFile())
				.has(new FileNameCondition(parentDirName, parentDirNameMatcher));
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
		return containsSubdirInPath(dirName, stringMatcher, dirPath);
	}

	public FileHierarchyAssert containsSubdirInPath(String dirName, StringMatcher dirNameMatcher, String... dirPath) {
		File searchDir = calculateDirFile(dirPath);
		IOFileFilter searchFilter = DirectoryFileFilter.INSTANCE;
		FileFilter dirFilter = new AndFileFilter(searchFilter, new FileNameCondition(dirName, dirNameMatcher));
		List<File> candidates = Arrays.asList(searchDir.listFiles((FileFilter) searchFilter));
		List<File> result = Arrays.asList(searchDir.listFiles(dirFilter));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a directory with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n", descPath(searchDir.toPath()),
						dirNameMatcher.getDescription(),
						descName(dirName),
						descPaths(candidates, " <no directories>"))
				.isGreaterThan(0);
		return this;
	}


	public FileHierarchyAssert containsFileInPath(String fileName, String... dirPath) {
		return containsFileInPath(fileName, stringMatcher, dirPath);
	}

	public FileHierarchyAssert containsFileInPath(String fileName, StringMatcher fileNameMatcher, String... dirPath) {
		File searchDir = calculateDirFile(dirPath);
		IOFileFilter searchFilter = FileFileFilter.FILE;
		FileFilter fileFilter = new AndFileFilter(searchFilter, new FileNameCondition(fileName, fileNameMatcher));
		List<File> candidates = Arrays.asList(searchDir.listFiles((FileFilter) searchFilter));
		List<File> result = Arrays.asList(searchDir.listFiles(fileFilter));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a file with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n",
						descPath(searchDir.toPath()),
						fileNameMatcher.getDescription(),
						descName(fileName),
						descPaths(candidates, " <no files>"))
				.isGreaterThan(0);
		return this;

	}

	public FileHierarchyAssert containsFileWithContentInPath(String fileName, List<String> content, String... dirPath) {
		return containsFileWithContentInPath(fileName, content, stringMatcher, dirPath);
	}

	public FileHierarchyAssert containsFileWithContentInPath(String fileName, List<String> content,
			StringMatcher contentMatcher, String... dirPath) {
		containsFileInPath(fileName, StringMatcher.STANDARD, dirPath);
		File file = new File(calculateDirFile(dirPath), fileName);
		try {
			List<String> lines = FileUtils.readLines(file);
			then(lines).overridingErrorMessage("\nExpecting:\n%s\nto contain lines:\n%s,\nbut it contains:\n%s\n",
					descPath(file.toPath()),
					descLines(content, " <no lines>"),
					descLines(lines, " <no lines>"))
					.isEqualTo(content);
		} catch (IOException e) {
			fail(e.getMessage());
		}
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

	private String descLines(Collection<String> lines, String whenEmpty) {
		StringBuilder buffer = new StringBuilder("");
		if (lines.isEmpty()) {
			buffer.append(whenEmpty);
		} else {
			int i = lines.size();
			for (String line : lines) {
				buffer.append(descName(line));
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
		private final StringMatcher stringMatcher;

		public FileNameCondition(String rootDirName, StringMatcher stringMatcher) {
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
}