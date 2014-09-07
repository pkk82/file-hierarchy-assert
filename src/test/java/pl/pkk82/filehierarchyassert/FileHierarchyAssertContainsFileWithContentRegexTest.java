package pl.pkk82.filehierarchyassert;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

public class FileHierarchyAssertContainsFileWithContentRegexTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenContainsFileWithContent("file\\d", ImmutableList.<String>of());
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentRegex("file\\d{2}", ImmutableList.of("file11"), "dir\\d");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenContainsFileWithContent("file\\d{3}", ImmutableList.of("file\\d{3}", "file\\d{3}"), "dir\\d",
				"(dir12)|(dir21)");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithTwoLevelPath() {
		givenFileHierarchyAssert(NameMatcherType.REGEX);
		whenContainsFileWithContent("file\\d{3}", ImmutableList.of("file\\d{2}", "file\\d{3}"), "dir\\d",
				"(dir12)|(dir21)");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting one of:\n <%s>\n <%s>\n <%s>\n" +
						"to contain lines matching to:\n <file\\d{2}>\n <file\\d{3}>,\n" +
						"but they contain:\n" +
						" <%s>\n <file121>\n <file121>\n" +
						" <%s>\n <file211>\n <file211>\n" +
						" <%s>\n <file212>\n <file212>\n",


				preparePath("dir1", "dir12", "file121"),
				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212"),

				preparePath("dir1", "dir12", "file121"),
				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212")));
	}


	private void whenContainsFileWithContent(String fileName, List<String> content, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileWithContent(fileName, content, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenContainsFileWithContentRegex(String fileName, List<String> content, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileWithContent(fileName, content, NameMatcherType.REGEX, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}