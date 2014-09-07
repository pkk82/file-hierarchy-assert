package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertContainsFileRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsFileRegex("file\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFail() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenContainsFile("file\\d{3}", "dir2", "dir22", "dir221");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a file with a name matching to:\n <file\\d{3}>,\n" +
						"but it contains:\n <%s>\n <%s>\n <%s>\n",
				preparePath("dir2", "dir22", "dir221"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22111"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22112"),
				preparePath("dir2", "dir22", "dir221", "file2211")));
	}

	@Test
	public void shouldSucceedWhenTwoFilesInDifferentPaths() {
		givenFileHierarchyAssert();
		whenContainsFileRegex("file\\d{2}1", "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWhenTwoFilesInTheSamePath() {
		givenFileHierarchyAssert();
		whenContainsFileRegex("file2\\d{1}1", "dir\\d", "dir\\d{2}");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenNoFilesInDifferentPaths() {
		givenFileHierarchyAssert();
		whenContainsFileRegex("file3", "dir\\d", "(dir12)|(dir21)");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n <%s>\n" +
						"to contain a file with a name matching to:\n <file3>,\n" +
						"but they contain:\n <%s>\n <%s>\n <%s>\n",

				preparePath("dir1", "dir12"),
				preparePath("dir2", "dir21"),

				preparePath("dir1", "dir12", "file121"),
				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212")));
	}

	private void whenContainsFile(String fileName, String... dirPath) {
		try {
			fileHierarchyAssert.containsFile(fileName, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenContainsFileRegex(String fileName, String... dirPath) {
		try {
			fileHierarchyAssert.containsFile(fileName, NameMatcherType.REGEX, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}