package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfDirsRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfDirs(9, "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasCountOfDirs(5, "dir\\d", "dir\\d2");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevelCombination() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsRegex(7, "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenInvalidCount() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsRegex(4, "dir\\d", "dir\\d2");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\nto contain:\n <4> directories\n" +
								"but contains:\n <5> directories\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n",

						preparePath("dir1", "dir12"),
						preparePath("dir2", "dir22"),

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
		whenHasCountOfDirs(1, "dir1", "dir1\\d", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}


	private void whenHasCountOfDirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirs(count, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}

	private void whenHasCountOfDirsRegex(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirs(count, StringMatcher.REGEX, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}
}