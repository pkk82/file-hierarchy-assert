package pl.pkk82.filehierarchyassert;

import java.util.Arrays;

import org.junit.Test;

public class FileHierarchyGitRepoExamplesTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldPresentCorrectExamplesInDocumentation() {
		givenFileHierarchyAssert();
		fileHierarchyAssert.hasRootDirWithName("fileHierarchy");
		fileHierarchyAssert.hasCountOfSubdirs(10);
		fileHierarchyAssert.hasCountOfSubdirs(2, "dir1");
		fileHierarchyAssert.hasCountOfFiles(9);
		fileHierarchyAssert.hasCountOfFiles(3, "dir2", "dir22", "dir221");
		fileHierarchyAssert.hasCountOfFilesAndDirs(20);
		fileHierarchyAssert.hasCountOfFilesAndDirs(1, "dir1", "dir11");
		fileHierarchyAssert.containsSubdir("dir221", "dir2", "dir22");
		fileHierarchyAssert.containsFile("file11", "dir1");
		fileHierarchyAssert.containsFileWithContent("file212", Arrays.asList("file212", "file212"), "dir2", "dir21");
	}
}
