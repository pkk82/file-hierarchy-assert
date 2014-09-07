package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesRegexTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfFiles(7, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfFiles(4, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesRegex(6, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesRegex(3, "dir\\d", "dir\\d1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <3> files\n" +
						"but contains:\n <2> files\n <%s>\n <%s>\n",

				preparePath("dir1", "dir11"),
				preparePath("dir2", "dir21"),

				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212")));
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfFilesRegex(1, "dir1", "dir\\d{3}", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name matching to:\n <dir\\d{3}>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir1"),
				preparePath("dir1", "dir11"),
				preparePath("dir1", "dir12")));
	}


	private void whenHasCountOfFiles(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFiles(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasCountOfFilesRegex(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFiles(count, NameMatcherType.REGEX, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}