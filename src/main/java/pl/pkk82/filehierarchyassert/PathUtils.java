package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.io.FileFilter;
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
import org.apache.commons.io.filefilter.TrueFileFilter;

public class PathUtils {
	private static final Function<File, Path> FUNCTION_FILE_2_PATH = new Function<File, Path>() {
		@Override
		public Path apply(File input) {
			return input.toPath();
		}
	};
	private static final Function<Path, File> FUNCTION_PATH_2_FILE = new Function<Path, File>() {

		@Override
		public File apply(Path input) {
			return input.toFile();
		}
	};


	static List<Path> findDirsRecursively(List<Path> dirs) {
		List<Path> foundedDirs = new ArrayList<>();
		for (Path dir : dirs) {
			foundedDirs.addAll(Lists.transform(Lists.newArrayList(FileUtils.listFilesAndDirs(dir.toFile(),
					FalseFileFilter.FALSE, TrueFileFilter.INSTANCE)), FUNCTION_FILE_2_PATH));
		}
		return foundedDirs;
	}

	static Collection<File> findSubdirsRecursively(File rootDir) {
		Collection<File> directories = FileUtils.listFilesAndDirs(rootDir, FalseFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		directories.remove(rootDir);
		return directories;
	}

	static Collection<Path> findFilesRecursively(Path dir) {
		return toPaths(findFilesRecursively(dir.toFile()));
	}

	static Collection<Path> findFilesAndDirsRecursively(Path dir) {
		return toPaths(findFilesAndDirsRecursively(dir.toFile()));
	}

	static List<Path> findDirs(Path dir, String dirName, StringMatcher nameMatcher) {
		File[] files = dir.toFile().listFiles((FileFilter) new AndFileFilter(DirectoryFileFilter.INSTANCE,
				new IOFileFilterCondition(dirName, nameMatcher)));
		return Lists.transform(Arrays.asList(files), FUNCTION_FILE_2_PATH);
	}

	static Collection<File> toFiles(Collection<Path> paths) {
		return Collections2.transform(paths, FUNCTION_PATH_2_FILE);
	}

	private static Collection<Path> toPaths(Collection<File> files) {
		return Collections2.transform(files, FUNCTION_FILE_2_PATH);
	}

	private static Collection<File> findFilesRecursively(File dir) {
		return FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	private static Collection<File> findFilesAndDirsRecursively(File dir) {
		return FileUtils.listFilesAndDirs(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}


}
