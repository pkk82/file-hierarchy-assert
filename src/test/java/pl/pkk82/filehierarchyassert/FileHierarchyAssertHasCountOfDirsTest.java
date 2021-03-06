package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfDirsTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfDirs(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfDirs(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> directories\n" +
						"but contains:\n <1> directory\n <%s>\n", preparePath(), preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfDirs(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfDirs(0);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <0> directories\n" +
						"but contains:\n <1> directory\n <%s>\n", preparePath(), preparePath())
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(11);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(3, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(4, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenNoDirectoryAtLevelOne() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(1, "dir3");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <dir3>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath(),
				preparePath("dir1"),
				preparePath("dir2")));
	}


	@Test
	public void shouldFailWhenNoDirectoryAtLevelTwo() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(1, "dir1", "dir21");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <dir21>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir1"),
				preparePath("dir1", "dir11"),
				preparePath("dir1", "dir12")));
	}


	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfDirs(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <file11>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir1"),
				preparePath("dir1", "dir11"),
				preparePath("dir1", "dir12")));
	}


	private void whenHasCountOfDirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}