package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertContainsSubdirTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir1");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir11", "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir221", "dir2", "dir22");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir111", "dir1", "dir11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name equal to:\n <dir111>,\n" +
				"but it contains:\n <no directories>\n", preparePath("dir1", "dir11")));
	}

	@Test
	public void shouldFailWhenNoDir() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name equal to:\n <dir>,\n" +
				"but it contains:\n <%s>\n <%s>\n", preparePath(), preparePath("dir1"), preparePath("dir2")));
	}

	@Test
	public void shouldFailWhenFileInstead() {
		givenFileHierarchyAssert();
		whenContainsSubdir("file11", "dir1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <file11>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir1"), preparePath("dir1", "dir11"), preparePath("dir1", "dir12")));
	}

	@Test
	public void shouldSucceedWithRegex() {
		givenFileHierarchyAssert();
		whenContainsSubdir("dir\\d", NameMatcherType.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithRegex() {
		givenFileHierarchyAssert();
		whenContainsSubdir("file\\d", NameMatcherType.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name matching to:\n <file\\d>,\n" +
				"but it contains:\n <%s>\n <%s>\n", preparePath(), preparePath("dir1"), preparePath("dir2")));
	}

	private void whenContainsSubdir(String dirName, String... dirPath) {
		try {
			fileHierarchyAssert.containsSubdir(dirName, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenContainsSubdir(String dirName, NameMatcherType nameMatcherType, String... dirPath) {
		try {
			fileHierarchyAssert.containsSubdir(dirName, nameMatcherType, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}