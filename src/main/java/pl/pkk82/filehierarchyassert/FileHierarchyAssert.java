package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import pl.pkk82.filehierarchygenerator.FileHierarchy;


public class FileHierarchyAssert {


	private static final String DESC_FILE_SING = "file";

	private static final String DESC_FILE_PLURAL = "files";
	private static final String DESC_DIR_SING = "directory";

	private static final String DESC_DIR_PLURAL = "directories";
	private static final String DESC_FILE_AND_DIR_SING = "file/directory";

	private static final String DESC_FILE_AND_DIR_PLURAL = "files/directories";

	private static final ThreadLocal<Collection<Path>> pathResult = new ThreadLocal<>();


	private final NameMatcherType nameMatcherType;
	private final FileHierarchy actual;

	public FileHierarchyAssert(FileHierarchy actual, NameMatcherType nameMatcherType) {
		this.actual = actual;
		this.nameMatcherType = nameMatcherType;
	}

	public FileHierarchyAssert(FileHierarchy actual) {
		this(actual, NameMatcherType.STANDARD);
	}


	public FileHierarchyAssert(File actual, NameMatcherType nameMatcherType) {
		this(actual == null ? null : new FileHierarchy(actual.toPath()), nameMatcherType);
	}

	public FileHierarchyAssert(File actual) {
		this(actual, NameMatcherType.STANDARD);
	}


	public FileHierarchyAssert(Path actual, NameMatcherType nameMatcherType) {
		this(actual == null ? null : new FileHierarchy(actual), nameMatcherType);
	}


	public FileHierarchyAssert(Path actual) {
		this(actual, NameMatcherType.STANDARD);
	}

	public FileHierarchyAssert exists() {
		isNotNull();
		then(actual.getRootDirectoryAsFile())
				.overridingErrorMessage("\nExpecting:\n%s\nto exist\n", descPath(actual.getRootDirectoryAsPath()))
				.exists();
		then(actual.getRootDirectoryAsFile())
				.overridingErrorMessage("\nExpecting:\n%s\nto be directory\n", descPath(actual.getRootDirectoryAsPath()))
				.isDirectory();
		return this;
	}

	public FileHierarchyAssert doesNotExist() {
		isNotNull();
		if (!actual.getRootDirectoryAsFile().isFile()) {
			then(actual.getRootDirectoryAsFile())
					.overridingErrorMessage("\nExpecting:\n%s\nto not exist\n", descPath(actual.getRootDirectoryAsPath()))
					.doesNotExist();
		}
		return this;
	}

	public FileHierarchyAssert isEmpty() {
		isNotNull();
		Collection<Path> dirs = calculateDirPath(nameMatcherType);
		Collection<Path> filesAndSubdirs = PathUtils.findFilesAndSubdirsRecursively(dirs);
		then(filesAndSubdirs.size())
				.overridingErrorMessage("\nExpecting:\n%s\nto be empty, but contains:\n%s\n",
						descPaths(dirs),
						descCountWithDetails(filesAndSubdirs, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL))
				.isEqualTo(0);
		return this;
	}

	public FileHierarchyAssert isNotEmpty() {
		isNotNull();
		Collection<Path> dirs = calculateDirPath(nameMatcherType);
		Collection<Path> filesAndSubdirs = PathUtils.findFilesAndSubdirsRecursively(dirs);
		then(filesAndSubdirs.size()).overridingErrorMessage("\nExpecting:\n%s\nto be not empty", descPaths(dirs))
				.isGreaterThan(0);
		return this;
	}

	public FileHierarchyAssert hasRootDirWithName(final String rootDirName) {
		return hasRootDirWithName(rootDirName, nameMatcherType);
	}

	public FileHierarchyAssert hasRootDirWithName(final String rootDirName, final NameMatcherType rootDirNameMatcher) {
		then(actual.getRootDirectoryAsFile()).has(new IOFileFilterCondition(rootDirName, rootDirNameMatcher));
		return this;
	}

	public FileHierarchyAssert hasParentDirWithName(final String parentDirName) {
		return hasParentDirWithName(parentDirName, nameMatcherType);
	}

	public FileHierarchyAssert hasParentDirWithName(final String parentDirName,
			final NameMatcherType parentDirNameMatcher) {
		then(actual.getRootDirectoryAsFile().getParentFile())
				.has(new IOFileFilterCondition(parentDirName, parentDirNameMatcher));
		return this;
	}


	public FileHierarchyAssert hasCountOfFilesAndDirs(int count, String... dirPath) {
		return hasCountOfFilesAndDirs(count, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert hasCountOfFilesAndSubdirs(int count, String... dirPath) {
		return hasCountOfFilesAndSubdirs(count, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert hasCountOfFilesAndDirs(int count, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> filesAndDirs = PathUtils.findFilesAndDirsRecursively(dirs);
		then(filesAndDirs.size())
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL),
						descCountWithDetails(filesAndDirs, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL))
				.isEqualTo(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfFilesAndSubdirs(int count, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> filesAndSubdirs = PathUtils.findFilesAndSubdirsRecursively(dirs);
		then(filesAndSubdirs.size())
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL),
						descCountWithDetails(filesAndSubdirs, DESC_FILE_AND_DIR_SING, DESC_FILE_AND_DIR_PLURAL))
				.isEqualTo(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfDirs(int count, String... dirPath) {
		return hasCountOfDirs(count, nameMatcherType, dirPath);
	}


	public FileHierarchyAssert hasCountOfDirs(int count, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> foundedDirs = PathUtils.findDirsRecursively(dirs);
		then(foundedDirs.size())
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(foundedDirs, DESC_DIR_SING, DESC_DIR_PLURAL))
				.isEqualTo(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfSubdirs(int count, String... dirPath) {
		return hasCountOfSubdirs(count, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert hasCountOfSubdirs(int count, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> subdirs = PathUtils.findSubdirsRecursively(dirs);
		then(subdirs.size())
				.overridingErrorMessage(String.format("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_DIR_SING, DESC_DIR_PLURAL),
						descCountWithDetails(subdirs, DESC_DIR_SING, DESC_DIR_PLURAL)))
				.isEqualTo(count);
		return this;
	}

	public FileHierarchyAssert hasCountOfFiles(int count, String... dirPath) {
		return hasCountOfFiles(count, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert hasCountOfFiles(int count, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> dirs = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> files = PathUtils.findFilesRecursively(dirs);
		then(files.size())
				.overridingErrorMessage("\nExpecting:\n%s\nto contain:\n%s\nbut contains:\n%s\n",
						descPaths(dirs),
						descCount(count, DESC_FILE_SING, DESC_FILE_PLURAL),
						descCountWithDetails(files, DESC_FILE_SING, DESC_FILE_PLURAL))
				.isEqualTo(count);
		return this;
	}


	public FileHierarchyAssert containsSubdir(String dirName, String... dirPath) {
		return containsSubdir(dirName, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert containsSubdir(String dirName, NameMatcherType dirNameMatcher, String... dirPath) {
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
		return containsFile(fileName, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert containsFile(String fileName, NameMatcherType nameMatcherType, String... dirPath) {
		Collection<Path> paths = calculateDirPath(nameMatcherType, dirPath);
		Collection<Path> candidates = PathUtils.findFilesRecursively(paths);
		Collection<Path> result = PathUtils.findRecursively(paths,
				new IOFileFilterCondition(fileName, nameMatcherType, FileFileFilter.FILE));
		pathResult.set(result);
		then(result.size())
				.overridingErrorMessage("\nExpecting:\n%s\n" +
								"to contain a file with a name %s to:\n%s,\n" +
								"but %s:\n%s\n",
						descPaths(paths),
						nameMatcherType.getDescription(),
						descName(fileName),
						descContain(paths),
						descPaths(candidates, " <no files>"))
				.isGreaterThan(0);
		return this;

	}


	public FileHierarchyAssert containsFileWithContent(String fileName, List<String> content, String... dirPath) {
		return containsFileWithContent(fileName, content, nameMatcherType, dirPath);
	}

	public FileHierarchyAssert containsFileWithContent(final String fileName, final List<String> expectedContent,
			final NameMatcherType nameMatcherType, final String... dirPath) {
		containsFile(fileName, nameMatcherType, dirPath);
		Collection<Path> candidates = pathResult.get();
		Collection<Path> result = Collections2.filter(candidates, new Predicate<Path>() {
			@Override
			public boolean apply(Path input) {
				return containsFileWithContent(input, expectedContent, nameMatcherType);
			}
		});
		if (candidates.size() == 1) {
			List<String> content = PathUtils.readLines(Iterables.getOnlyElement(candidates));
			then(result.size()).overridingErrorMessage("\nExpecting:\n%s\nto contain lines %s to:\n%s,\nbut %s:\n%s\n",
					descPaths(candidates),
					nameMatcherType.getDescription(),
					descLines(markDifferences(expectedContent, content), " <no lines>"),
					descContain(candidates),
					descLines(markDifferences(content, expectedContent), " <no lines>"))
					.isGreaterThan(0);
		} else {
			then(result.size()).overridingErrorMessage("\nExpecting one of:\n%s\nto contain lines %s to:\n%s,\nbut %s:\n%s\n",
					descPaths(candidates),
					nameMatcherType.getDescription(),
					descLines(expectedContent, " <no lines>"),
					descContain(candidates),
					descPathLines(candidates, " <no lines>"))
					.isGreaterThan(0);

		}


		return this;
	}

	private List<String> markDifferences(List<String> toMark, List<String> template) {
		List<String> marked = new ArrayList<>();
		for (int i = 0; i < toMark.size(); i++) {
			String toCheck = toMark.get(i);
			boolean equals = i < template.size() && StringUtils.equals(toCheck, template.get(i));
			if (equals) {
				marked.add(toCheck);
			} else {
				marked.add(toCheck + "*");
			}
		}
		return marked;
	}

	private boolean containsFileWithContent(Path input, List<String> content, NameMatcherType nameMatcherType) {
		List<String> lines = PathUtils.readLines(input);
		if (lines.size() != content.size()) {
			return false;
		}
		for (int i = 0; i < lines.size(); i++) {
			String pattern = content.get(i);
			String line = lines.get(i);
			boolean matchResult = nameMatcherType.matches(line, pattern);
			if (!matchResult) {
				return false;
			}
		}

		return true;

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

	private Collection<Path> calculateDirPath(NameMatcherType nameMatcherType, String... dirPath) {
		Path actualPath = actual.getRootDirectoryAsPath();
		then(actualPath.toFile())
				.overridingErrorMessage("\nExpecting:\n%s\nto be an existing directory\n",
						descPath(actualPath))
				.exists().isDirectory();
		return calculateDirPath(Collections.singletonList(actualPath), nameMatcherType, dirPath);
	}


	private Collection<Path> calculateDirPath(Collection<Path> parents, NameMatcherType nameMatcherType,
			String... dirPath) {
		if (dirPath.length == 0) {
			return parents;
		} else {
			List<Path> all = new ArrayList<>();
			String nextPathToResolve = dirPath[0];
			Collection<Path> children = PathUtils.findSubdirs(parents, nextPathToResolve, nameMatcherType);
			Collection<Path> allSubdirs = PathUtils.findSubdirs(parents);
			then(children.size())
					.overridingErrorMessage("\nExpecting:\n%s\n" +
									"to contain a directory with a name %s to:\n%s,\n" +
									"but %s:\n%s\n",
							descPaths(parents),
							nameMatcherType.getDescription(),
							descName(nextPathToResolve),
							descContain(parents),
							descPaths(allSubdirs, " <no directories>"))
					.isGreaterThan(0);
			all.addAll(calculateDirPath(children, nameMatcherType, ArrayUtils.subarray(dirPath, 1, dirPath.length)));
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

	private String descPathLines(Collection<Path> paths, String whenEmpty) {
		StringBuilder buffer = new StringBuilder();
		int i = paths.size();
		for (Path path : paths) {
			buffer.append(descPath(path));
			buffer.append('\n');
			buffer.append(descLines(PathUtils.readLines(path), whenEmpty));
			if (--i != 0) {
				buffer.append('\n');
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
		return buffer.toString().replaceAll("\\*>", ">*");
	}

	private String descContain(Collection<?> collection) {
		return collection.size() == 1 ? "it contains" : "they contain";
	}

	private void isNotNull() {
		if (actual == null) {
			throw new AssertionError("File should not be null");
		}
	}

	private FileAssert then(File file) {
		return new FileAssert(file);
	}


	private SizeAssert then(int size) {
		return new SizeAssert(size);
	}


}