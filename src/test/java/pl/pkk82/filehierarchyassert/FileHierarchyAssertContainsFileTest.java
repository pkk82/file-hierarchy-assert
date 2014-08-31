package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertContainsFileTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWhenFileInMainFolderOfRootHierarchy() {
		givenFileHierarchyAssert();
		whenContainsFile("file1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWhenFileInSubfolderOfRootHierarchy() {
		givenFileHierarchyAssert();
		whenContainsFile("file11");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFile("file11", "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFile("file212", "dir2", "dir21");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyAssert();
		whenContainsFile("file111", "dir1", "dir11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a file with a name equal to:\n <file111>,\n" +
				"but it contains:\n <no files>\n", preparePath("dir1", "dir11")));
	}

	@Test
	public void shouldFailWhenNoDir() {
		givenFileHierarchyAssert();
		whenContainsFile("file11", "dir3");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir3")));
	}

	@Test
	public void shouldFailWhenDirInstead() {
		givenFileHierarchyAssert();
		whenContainsFile("dir221111", "dir2", "dir22", "dir221",  "dir2211", "dir22111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a file with a name equal to:\n <dir221111>,\n" +
				"but it contains:\n <no files>\n",
				preparePath("dir2", "dir22", "dir221",  "dir2211", "dir22111")));
	}

	@Test
	public void shouldSucceedWithRegex() {
		givenFileHierarchyAssert();
		whenContainsFile("file\\d", StringMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithRegex() {
		givenFileHierarchyAssert();
		whenContainsFile("file\\d{3}", StringMatcher.REGEX, "dir2", "dir22", "dir221");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a file with a name matching to:\n <file\\d{3}>,\n" +
						"but it contains:\n <%s>\n <%s>\n <%s>\n",
				preparePath("dir2", "dir22", "dir221"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22111"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22112"),
				preparePath("dir2", "dir22", "dir221", "file2211")));
	}

	private void whenContainsFile(String fileName, String... dirPath) {
		try {
			fileHierarchyAssert.containsFile(fileName, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenContainsFile(String fileName, StringMatcher stringMatcher, String... dirPath) {
		try {
			fileHierarchyAssert.containsFile(fileName, stringMatcher, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}