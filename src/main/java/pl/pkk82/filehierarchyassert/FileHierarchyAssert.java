package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.assertj.core.api.AbstractAssert;

import pl.pkk82.filehierarchygenerator.FileHierarchy;

public class FileHierarchyAssert extends AbstractAssert<FileHierarchyAssert, FileHierarchy> {


	public FileHierarchyAssert(FileHierarchy actual) {
		super(actual, FileHierarchyAssert.class);
	}

	public FileHierarchyAssert hasRootDirectoryWithName(String rootDirectoryName) {
		isNotNull();
		assertThat(actual.getRootDirectoryAsFile()).isDirectory().hasName(rootDirectoryName);
		return this;
	}

	public FileHierarchyAssert hasTempWorkingDirectoryWithName(String rootDirectoryName) {
		isNotNull();
		assertThat(actual.getRootDirectoryAsFile()).isDirectory().hasName(rootDirectoryName);
		return this;
	}

	public FileHierarchyAssert containsDirectoriesInPath(String... directoryNames) {
		isNotNull();
		Path actualPath = actual.getRootDirectoryAsPath();
		for (String directoryName : directoryNames) {
			actualPath = actualPath.resolve(directoryName);
			assertThat(actualPath.toFile()).exists().isDirectory();
		}
		return this;
	}

	public FileHierarchyAssert containsFileInPath(String fileName, String... directoryNames) {
		isNotNull();
		containsDirectoriesInPath(directoryNames);
		Path actualPath = calculatePath(fileName, directoryNames);
		assertThat(actualPath.toFile()).exists().isFile();
		return this;
	}

	public FileHierarchyAssert containsFileInPathWithContent(String fileName, List<String> lines,
			String... directoryNames) {
		isNotNull();
		containsFileInPath(fileName, directoryNames);
		Path path = calculatePath(fileName, directoryNames);
		try {
			assertThat(FileUtils.readLines(path.toFile(), "utf8")).isEqualTo(lines);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return this;
	}

	public FileHierarchyAssert hasCountOfSubdirectories(int nDirectories) {
		isNotNull();
		assertThat(findAllSubdirectories(actual.getRootDirectoryAsFile())).hasSize(nDirectories);
		return this;
	}

	public FileHierarchyAssert hasCountOfFiles(int nDirectories) {
		isNotNull();
		assertThat(findAllFiles(actual.getRootDirectoryAsFile())).hasSize(nDirectories);
		return this;
	}

	private Collection<File> findAllSubdirectories(File rootDirectory) {
		Collection<File> directories = FileUtils.listFilesAndDirs(rootDirectory, FalseFileFilter.FALSE,
				TrueFileFilter.INSTANCE);
		directories.remove(rootDirectory);
		return directories;
	}

	private Collection<File> findAllFiles(File rootDirectory) {
		return FileUtils.listFiles(rootDirectory, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
	}

	private Path calculatePath(String fileName, String[] directoryNames) {
		Path actualPath = actual.getRootDirectoryAsPath();
		for (String directoryName : directoryNames) {
			actualPath = actualPath.resolve(directoryName);
		}
		actualPath = actualPath.resolve(fileName);
		return actualPath;
	}
}