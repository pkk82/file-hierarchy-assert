package pl.pkk82.filehierarchyassert;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
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

	static Collection<File> findFilesRecursively(File rootDir) {
		return FileUtils.listFiles(rootDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	static Collection<File> findFilesAndDirsRecursively(File rootDir) {
		return FileUtils.listFilesAndDirs(rootDir, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
	}

	static List<Path> findDirs(Path dir, String dirName, StringMatcher nameMatcher) {
		File[] files = dir.toFile().listFiles((FileFilter) new AndFileFilter(DirectoryFileFilter.INSTANCE,
				new IOFileFilterCondition(dirName, nameMatcher)));
		return Lists.transform(Arrays.asList(files), FUNCTION_FILE_2_PATH);
	}
}
