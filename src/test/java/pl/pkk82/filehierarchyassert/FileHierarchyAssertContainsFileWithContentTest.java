package pl.pkk82.filehierarchyassert;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class FileHierarchyAssertContainsFileWithContentTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file1", ImmutableList.<String>of());
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file11", ImmutableList.of("file11-line1"), "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file212", ImmutableList.of("file212-line1", "file212-line2"), "dir2", "dir21");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenExpectedEmtpy() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file1", ImmutableList.of("line1"));
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain lines equal to:\n <line1>*,\n" +
				"but it contains:\n <no lines>\n", preparePath("file1")));
	}

	@Test
	public void shouldFailWhenExpectedNonEmtpy() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file11", ImmutableList.<String>of(), "dir1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain lines equal to:\n <no lines>,\n" +
				"but it contains:\n <file11-line1>*\n", preparePath("dir1", "file11")));
	}

	@Test
	public void shouldFailAndFindTheDifference() {
		givenFileHierarchyAssert();
		whenContainsFileWithContent("file2211",
				ImmutableList.<String>of("file2211-line1", "file2211-line2", "file2211-line3-other"), "dir2", "dir22", "dir221");
		thenAssertionIsFailed().hasMessage(String.format("\n" +
				"Expecting:\n" +
				" <%s>\n" +
				"to contain lines equal to:\n" +
				" <file2211-line1>\n" +
				" <file2211-line2>\n" +
				" <file2211-line3-other>*,\n" +
				"but it contains:\n" +
				" <file2211-line1>\n" +
				" <file2211-line2>\n" +
				" <file2211-line3>*\n", preparePath("dir2", "dir22", "dir221", "file2211")));
	}

	private void whenContainsFileWithContent(String fileName, List<String> content, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileWithContent(fileName, content, dirPath);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}