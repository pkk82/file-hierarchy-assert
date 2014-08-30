package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfSubdirsRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(7, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(3, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsRegex(3, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsRegex(4, "dir\\d", "dir\\d2");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <4> directories\n" +
								"but contains:\n <3> directories\n <%s>\n <%s>\n <%s>\n",

						preparePath("dir1", "dir12"),
						preparePath("dir2", "dir22"),

						preparePath("dir2", "dir22", "dir221"),
						preparePath("dir2", "dir22", "dir221", "dir2211"),
						preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111"))
		);
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(1, "dir1", "dir1\\d", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}


	private void whenHasCountOfSubdir(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfSubdirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasCountOfSubdirsRegex(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfSubdirs(count, StringMatcher.REGEX, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}