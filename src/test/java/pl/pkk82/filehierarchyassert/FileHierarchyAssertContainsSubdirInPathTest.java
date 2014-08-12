package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertContainsSubdirInPathTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir1");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir11", "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir221", "dir2", "dir22");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir111", "dir1", "dir11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name equal to:\n <dir111>,\n" +
				"but it contains:\n <no directories>\n", preparePath("dir1", "dir11")));
	}

	@Test
	public void shouldFailWhenNoDir() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name equal to:\n <dir>,\n" +
				"but it contains:\n <%s>\n <%s>\n", preparePath(), preparePath("dir1"), preparePath("dir2")));
	}

	@Test
	public void shouldFailWhenFileInstead() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("file11", "dir1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <file11>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir1"), preparePath("dir1", "dir11"), preparePath("dir1", "dir12")));
	}

	@Test
	public void shouldSucceedWithRegex() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("dir\\d", NameMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithRegex() {
		givenFileHierarchyAssert();
		whenContainsSubdirInPath("file\\d", NameMatcher.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain a directory with a name matching to:\n <file\\d>,\n" +
				"but it contains:\n <%s>\n <%s>\n", preparePath(), preparePath("dir1"), preparePath("dir2")));
	}

	private void whenContainsSubdirInPath(String dirName, String... dirPath) {
		try {
			fileHierarchyAssert.containsSubdirInPath(dirName, dirPath);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

	private void whenContainsSubdirInPath(String dirName, NameMatcher nameMatcher, String... dirPath) {
		try {
			fileHierarchyAssert.containsSubdirInPath(dirName, nameMatcher, dirPath);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

}