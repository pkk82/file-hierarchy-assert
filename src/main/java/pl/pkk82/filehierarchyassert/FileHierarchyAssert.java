package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AbstractAssert;

import pl.pkk82.filehierarchygenerator.FileHierarchy;


public class FileHierarchyAssert extends AbstractAssert<FileHierarchyAssert, FileHierarchy> {

	private static final String DESC_FILE_SING = "file";
	private static final String DESC_FILE_PLURAL = "files";

	private static final String DESC_DIR_SING = "directory";
	private static final String DESC_DIR_PLURAL = "directories";

	private static final String DESC_FILE_AND_DIR_SING = "file/directory";
	private static final String DESC_FILE_AND_DIR_PLURAL = "files/directories";

	private final StringMatcher nameMatcher;

	public FileHierarchyAssert(FileHierarchy actual, StringMatcher nameMatcher) {
		super(actual, FileHierarchyAssert.class);
		this.nameMatcher = nameMatcher;
		exists();
	}

	public FileHierarchyAssert(FileHierarchy actual) {
		this(actual, StringMatcher.STANDARD);
	}

	public FileHierarchyAssert(File actual, StringMatcher nameMatcher) {
		this(actual == null ? null : new FileHierarchy(actual.toPath()), nameMatcher);
	}


	public FileHierarchyAssert(File actual) {
		this(actual, StringMatcher.STANDARD);
	}

	public FileHierarchyAssert(Path actual, StringMatcher nameMatcher) {
		this(actual == null ? null : new FileHierarchy(actual), nameMatcher);
	}


	public FileHierarchyAssert(Path actual) {
		this(actual, StringMatcher.STANDARD);
	}


	public FileHierarchyAssert hasRootDirWithName(final String rootDirName) {
		return hasRootDirWithName(rootDirName, nameMatcher);
	}

	public FileHierarchyAssert hasRootDirWithName(final String rootDirName, final StringMatcher rootDirNameMatcher) {
		then(actual.getRootDirectoryAsFile()).has(new IOFileFilterCondition(rootDirName, rootDirNameMatcher));
		return this;
	}


	public FileHierarchyAssert hasParentDirWithName(final String parentDirName) {
		return hasParentDirWithName(parentDirName, nameMatcher);
	}

	public FileHierarchyAssert hasParentDirWithName(final String parentDirName,
			final StringMatcher parentDirNameMatcher) {
		then(actual.getRootDirectoryAsFile().getParentFile())
				.has(new IOFileFilterCondition(parentDirName, parentDirNameMatcher));
		return this;
	}

	public FileHierarchyAssert hasCountOfFilesAndDirs(int count, String... dirPath) {
		return hasCountOfFilesAndDirs(count, nameMatcher, dirPath);
	}

	public FileHierarchyAssert hasCountOfFilesAndDirs(int count, StringMatcher nameMatcher, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcher, dirPath);
		Collection<Path> filesAndDirs = PathUtils.findFilesAndDirsRecursively(dirs);
		then(filesAndDirs)
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL),
						descCountWithDetails(filesAndDirs, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL))
				.hasSize(count);
		return this;
	}


	public FileHierarchyAssert hasCountOfDirs(int count, String... dirPath) {
		return hasCountOfDirs(count, nameMatcher, dirPath);
	}

	public FileHierarchyAssert hasCountOfDirs(int count, StringMatcher nameMatcher, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcher, dirPath);
		Collection<Path> foundedDirs = PathUtils.findDirsRecursively(dirs);
		then(foundedDirs)
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(foundedDirs, DESC_DIR_SING, DESC_DIR_PLURAL))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfSubdirs(int count, String... dirPath) {
		return hasCountOfSubdirs(count, nameMatcher, dirPath);
	}

	public FileHierarchyAssert hasCountOfSubdirs(int count, StringMatcher nameMatcher, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcher, dirPath);
		Collection<Path> subdirs = PathUtils.findSubdirsRecursively(dirs);
		then(subdirs)
				.overridingErrorMessage(String.format("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(subdirs, DESC_DIR_SING, DESC_DIR_PLURAL)))
				.hasSize(count);
		return this;
	}


	public FileHierarchyAssert hasCountOfFiles(int count, String... dirPath) {
		return hasCountOfFiles(count, nameMatcher, dirPath);
	}

	public FileHierarchyAssert hasCountOfFiles(int count, StringMatcher nameMatcher, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcher, dirPath);
		Collection<Path> files = PathUtils.findFilesRecursively(dirs);
		then(files)
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_FILE_SING, DESC_FILE_PLURAL),
						descCountWithDetails(files, DESC_FILE_SING, DESC_FILE_PLURAL))
				.hasSize(count);
		return this;
	}

	public FileHierarchyAssert containsSubdir(String dirName, String... dirPath) {
		return containsSubdir(dirName, nameMatcher, dirPath);
	}

	public FileHierarchyAssert containsSubdir(String dirName, StringMatcher dirNameMatcher, String... dirPath) {
		Path searchDir = calculateDir(dirPath);
		Collection<Path> candidates = PathUtils.findSubdirs(searchDir);
		Collection<Path> result = PathUtils.find(searchDir,
				new IOFileFilterCondition(dirName, dirNameMatcher, DirectoryFileFilter.DIRECTORY));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a directory with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n", descPath(searchDir),
						dirNameMatcher.getDescription(),
						descName(dirName),
						descPaths(candidates, " <no directories>"))
				.isGreaterThan(0);
		return this;
	}


	public FileHierarchyAssert containsFile(String fileName, String... dirPath) {
		return containsFile(fileName, nameMatcher, dirPath);
	}

	public FileHierarchyAssert containsFile(String fileName, StringMatcher fileNameMatcher, String... dirPath) {
		Path searchDir = calculateDir(dirPath);
		Collection<Path> candidates = PathUtils.findFiles(searchDir);
		Collection<Path> result = PathUtils.find(searchDir,
				new IOFileFilterCondition(fileName, fileNameMatcher, FileFileFilter.FILE));
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a file with a name %s to:\n%s,\n" +
								"but it contains:\n%s\n",
						descPath(searchDir),
						fileNameMatcher.getDescription(),
						descName(fileName),
						descPaths(candidates, " <no files>"))
				.isGreaterThan(0);
		return this;

	}

	public FileHierarchyAssert containsFileWithContent(String fileName, List<String> content, String... dirPath) {
		containsFile(fileName, StringMatcher.STANDARD, dirPath);
		try {
			Path file = calculateDir(dirPath).resolve(fileName);
			List<String> lines = PathUtils.readLines(file);
			then(lines).overridingErrorMessage("\nExpecting:\n%s\nto contain lines:\n%s,\nbut it contains:\n%s\n",
					descPath(file),
					descLines(content, " <no lines>"),
					descLines(lines, " <no lines>"))
					.isEqualTo(content);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		return this;
	}

	private Path calculateDir(String... dirPath) {
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


	private Collection<Path> calculateDirPath(StringMatcher nameMatcher, String... dirPath) {
		Path actualPath = actual.getRootDirectoryAsPath();
		then(actualPath.toFile())
				.overridingErrorMessage("\nExpecting:\n%s\nto be an existing directory\n",
						descPath(actualPath))
				.exists().isDirectory();
		return calculateDirPath(Arrays.asList(actualPath), nameMatcher, dirPath);
	}

	private Collection<Path> calculateDirPath(Collection<Path> parents, StringMatcher nameMatcher, String... dirPath) {
		if (dirPath.length == 0) {
			return parents;
		} else {
			List<Path> all = new ArrayList<>();
			for (Path parent : parents) {
				Collection<Path> children = PathUtils.findSubdirs(parent, dirPath[0], nameMatcher);
				if (children.isEmpty()) {
					Path actualPath = parent.resolve(dirPath[0]);
					then(actualPath.toFile())
							.overridingErrorMessage("\nExpecting:\n%s\nto be an existing directory\n",
									descPath(actualPath))
							.exists().isDirectory();
				}
				all.addAll(calculateDirPath(children, nameMatcher, ArrayUtils.subarray(dirPath, 1, dirPath.length)));
			}
			return all;
		}
	}

	private String descCountWithDetails(Collection<Path> files, String descSingular, String descPlural) {
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


	private String descPaths(Collection<Path> paths) {
		return descPaths(paths, "");
	}

	private String descPaths(Collection<Path> paths, String whenEmpty) {
		StringBuilder buffer = new StringBuilder("");
		if (paths.isEmpty()) {
			buffer.append(whenEmpty);
		} else {
			int i = paths.size();
			for (Path candidate : paths) {
				buffer.append(descPath(candidate));
				if (--i != 0) {
					buffer.append('\n');
				}

			}
		}
		return buffer.toString();
	}

	private String descCount(int count, String descSingular, String descPlural) {
		if (count == 1) {
			return String.format(" <1> %s", descSingular);
		} else {
			return String.format(" <%d> %s", count, descPlural);
		}
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


}