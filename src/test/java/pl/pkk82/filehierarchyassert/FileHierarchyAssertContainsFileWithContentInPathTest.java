package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

public class FileHierarchyAssertContainsFileWithContentInPathTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceed() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentInPath("file1", ImmutableList.<String>of());
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldSucceedWithOneLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentInPath("file11", ImmutableList.of("file11"), "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSucceedWithTwoLevelPath() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentInPath("file212", ImmutableList.of("file212", "file212"), "dir2", "dir21");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenExpectedEmtpy() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentInPath("file1", ImmutableList.of("line1"));
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain lines:\n <line1>,\n" +
				"but it contains:\n <no lines>\n", preparePath("file1")));
	}

	@Test
	public void shouldFailWhenExpectedNonEmtpy() {
		givenFileHierarchyAssert();
		whenContainsFileWithContentInPath("file11", ImmutableList.<String>of(), "dir1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
				"to contain lines:\n <no lines>,\n" +
				"but it contains:\n <file11>\n", preparePath("dir1", "file11")));
	}

	private void whenContainsFileWithContentInPath(String fileName, List<String> content, String... dirPath) {
		try {
			fileHierarchyAssert.containsFileWithContentInPath(fileName, content, dirPath);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

}