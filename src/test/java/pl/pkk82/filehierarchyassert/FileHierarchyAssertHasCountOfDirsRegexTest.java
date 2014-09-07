package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfDirsRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfDirs(10, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfDirs(6, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsRegex(8, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsRegex(4, "dir\\d", "dir\\d2");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <4> directories\n" +
								"but contains:\n <6> directories\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n",

						preparePath("dir1", "dir12"),
						preparePath("dir2", "dir22"),

						preparePath("dir1", "dir12"),
						preparePath("dir2", "dir22"),
						preparePath("dir2", "dir22", "dir221"),
						preparePath("dir2", "dir22", "dir221", "dir2211"),
						preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111"),
						preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111", "dir221111"))
		);
	}

	@Test
	public void shouldFailWhenNoDirectoryAtLevelOne() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfDirs(1, "dir3");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name matching to:\n <dir3>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath(),
				preparePath("dir1"),
				preparePath("dir2")));
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenHasCountOfDirs(1, "dir\\d{2}");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name matching to:\n <dir\\d{2}>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath(), preparePath("dir1"), preparePath("dir2")));
	}


	private void whenHasCountOfDirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasCountOfDirsRegex(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirs(count, NameMatcherType.REGEX, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}