package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfSubdirsRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(8, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(4, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsRegex(4, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsRegex(5, "dir\\d", "dir\\d2");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <5> directories\n" +
								"but contains:\n <4> directories\n <%s>\n <%s>\n <%s>\n <%s>\n",

						preparePath("dir1", "dir12"),
						preparePath("dir2", "dir22"),

						preparePath("dir2", "dir22", "dir221"),
						preparePath("dir2", "dir22", "dir221", "dir2211"),
						preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111"),
						preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111", "dir221111"))
		);
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfSubdir(1, "dir1", "dir1\\d", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\n" +
						"to contain a directory with a name matching to:\n <dir111>,\n" +
						"but they contain:\n <no directories>\n",
				preparePath("dir1", "dir11"),
				preparePath("dir1", "dir12")));
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