package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesAndDirsRegexTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfFilesAndDirs(16, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfFilesAndDirs(9, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirsRegex(13, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirsRegex(3, "dir\\d", "dir\\d1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <3> files/directories\n" +
						"but contains:\n <4> files/directories\n <%s>\n <%s>\n <%s>\n <%s>\n",

				preparePath("dir1", "dir11"),
				preparePath("dir2", "dir21"),

				preparePath("dir1", "dir11"),
				preparePath("dir2", "dir21"),
				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212")));
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfFilesAndDirsRegex(1, "dir1", "dir1\\d", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}

	private void whenHasCountOfFilesAndDirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFilesAndDirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasCountOfFilesAndDirsRegex(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFilesAndDirs(count, StringMatcher.REGEX, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}