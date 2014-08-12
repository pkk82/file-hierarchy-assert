package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertContainsFileInPathTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file1");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file11", "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file212", "dir2", "dir21");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file111", "dir1", "dir11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a file with a name equal to:\n <file111>,\n" +
				"but it contains:\n <no files>\n", preparePath("dir1", "dir11")));
	}

	@Test
	public void shouldFailWhenNoDir() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file11", "dir3");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir3")));
	}

	@Test
	public void shouldFailWhenDirInstead() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("dir21", "dir2");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a file with a name equal to:\n <dir21>,\n" +
				"but it contains:\n <no files>\n", preparePath("dir2")));
	}

	@Test
	public void shouldSucceedWithRegex() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("file\\d", NameMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithRegex() {
		givenFileHierarchyAssert();
		whenContainsFileInPath("dir\\d", NameMatcher.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a file with a name matching to:\n <dir\\d>,\n" +
				"but it contains:\n <%s>\n <%s>\n", preparePath(), preparePath("file1"), preparePath("file2")));
	}

	private void whenContainsFileInPath(String fileName, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileInPath(fileName, dirPath);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

	private void whenContainsFileInPath(String fileName, NameMatcher nameMatcher, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileInPath(fileName, nameMatcher, dirPath);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

}