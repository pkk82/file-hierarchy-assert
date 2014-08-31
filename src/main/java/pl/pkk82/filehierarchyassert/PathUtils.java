package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class PathUtils {
	private static final Function<File, Path> FUNCTION_FILE_2_PATH = new Function<File, Path>() {
		@Override
		public Path apply(File input) {
			return input.toPath();
		}
	};

	static List<String> readLines(Path path) throws IOException {
		return FileUtils.readLines(path.toFile());
	}

	/* Find recursively */

	static Collection<Path> findDirsRecursively(Path dir) {
		return toPaths(findDirsRecursively(dir.toFile()));
	}

	static Collection<Path> findDirsRecursively(Collection<Path> dirs) {
		List<Path> foundedDirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedDirs.addAll(findDirsRecursively(dir));
		}
		return foundedDirs;
	}

	static Collection<Path> findSubdirsRecursively(Path dir) {
		return toPaths(findSubdirsRecursively(dir.toFile()));
	}

	static Collection<Path> findSubdirsRecursively(Collection<Path> dirs) {
		List<Path> foundedDirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedDirs.addAll(findSubdirsRecursively(dir));
		}
		return foundedDirs;
	}

	static Collection<Path> findFilesRecursively(Path dir) {
		return toPaths(findFilesRecursively(dir.toFile()));
	}

	static Collection<Path> findFilesRecursively(Collection<Path> dirs) {
		List<Path> foundedDirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedDirs.addAll(findFilesRecursively(dir));
		}
		return foundedDirs;
	}

	static Collection<Path> findFilesAndDirsRecursively(Path dir) {
		return toPaths(findFilesAndDirsRecursively(dir.toFile()));
	}

	static Collection<Path> findFilesAndDirsRecursively(Collection<Path> dirs) {
		List<Path> foundedDirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedDirs.addAll(findFilesAndDirsRecursively(dir));
		}
		return foundedDirs;
	}

	/* Find in directory */

	static Collection<Path> findFiles(Path dir) {
		return toPaths(dir.toFile().listFiles((FileFilter) FileFileFilter.FILE));
	}

	static Collection<Path> find(Path dir, FileFilter fileFilter) {
		return toPaths(dir.toFile().listFiles(fileFilter));
	}

	static Collection<Path> findSubdirs(Path dir) {
		return toPaths(dir.toFile().listFiles((FileFilter) DirectoryFileFilter.DIRECTORY));
	}

	static Collection<Path> findSubdirs(Collection<Path> dirs) {
		List<Path> foundedSubdirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedSubdirs.addAll(findSubdirs(dir));
		}
		return foundedSubdirs;
	}

	static Collection<Path> findSubdirs(Path dir, String dirName, StringMatcher nameMatcher) {
		File[] files = dir.toFile().listFiles((FileFilter) new AndFileFilter(DirectoryFileFilter.DIRECTORY,
				new IOFileFilterCondition(dirName, nameMatcher)));
		return Lists.transform(Arrays.asList(files), FUNCTION_FILE_2_PATH);
	}

	static Collection<Path> findSubdirs(Collection<Path> dirs, String dirName, StringMatcher nameMatcher) {
		List<Path> foundedSubdirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedSubdirs.addAll(findSubdirs(dir, dirName, nameMatcher));
		}
		return foundedSubdirs;
	}

	/* Find recursively based on file */

	private static Collection<File> findDirsRecursively(File dir) {
		return FileUtils.listFilesAndDirs(dir, FalseFileFilter.FALSE, TrueFileFilter.TRUE);
	}

	private static Collection<File> findSubdirsRecursively(File dir) {
		Collection<File> directories = FileUtils.listFilesAndDirs(dir, FalseFileFilter.FALSE, TrueFileFilter.TRUE);
		directories.remove(dir);
		return directories;
	}

	private static Collection<File> findFilesRecursively(File dir) {
		return FileUtils.listFiles(dir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
	}

	private static Collection<File> findFilesAndDirsRecursively(File dir) {
		return FileUtils.listFilesAndDirs(dir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
	}

	/* Conversion from files to paths methods */

	private static Collection<Path> toPaths(Collection<File> files) {
		return Collections2.transform(files, FUNCTION_FILE_2_PATH);
	}

	private static Collection<Path> toPaths(File... files) {
		return Collections2.transform(Arrays.asList(files), FUNCTION_FILE_2_PATH);
	}


}
