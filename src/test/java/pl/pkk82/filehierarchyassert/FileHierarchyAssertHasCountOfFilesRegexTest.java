package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesRegexTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfFiles(7, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
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
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfFilesRegex(1, "dir1", "dir1\\d", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
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
			fileHierarchyAssert.hasCountOfFiles(count, StringMatcher.REGEX, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}