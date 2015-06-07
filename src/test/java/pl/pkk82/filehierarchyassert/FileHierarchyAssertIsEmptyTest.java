package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertIsEmptyTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWhenRootDirHasNotSubdirsAndFiles() {
		givenFileHierarchyEmptyAssert();
		whenIsEmpty();
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenRootDirHasSubdirsAndFiles() {
		givenFileHierarchyAssert();
		whenIsEmpty();
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be empty, " +
						"but contains:\n <19> files/directories\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n" +
						" <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n <%s>\n",
				preparePath(),
				preparePath("dir1"),
				preparePath("dir1", "dir11"),
				preparePath("dir1", "dir12"),
				preparePath("dir1", "dir12", "file121"),
				preparePath("dir1", "file11"),
				preparePath("dir2"),
				preparePath("dir2", "dir21"),
				preparePath("dir2", "dir21", "file211"),
				preparePath("dir2", "dir21", "file212"),
				preparePath("dir2", "dir22"),
				preparePath("dir2", "dir22", "dir221"),
				preparePath("dir2", "dir22", "dir221", "dir2211"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "dir22111", "dir221111"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22111"),
				preparePath("dir2", "dir22", "dir221", "dir2211", "file22112"),
				preparePath("dir2", "dir22", "dir221", "file2211"),
				preparePath("file1"),
				preparePath("file2")));
	}

	@Test
	public void shouldSucceedWhenRootDirHasSubdirsAndFiles() {
		givenFileHierarchyAssert();
		whenIsNotEmpty();
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenRootDirHasNotSubdirsAndFiles() {
		givenFileHierarchyEmptyAssert();
		whenIsNotEmpty();
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be not empty", preparePath()));
	}

	private void whenIsEmpty() {
		try {
			fileHierarchyAssert.isEmpty();
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenIsNotEmpty() {
		try {
			fileHierarchyAssert.isNotEmpty();
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}