package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesAndDirsTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesAndDirs(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesAndDirs(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> files/directories\n" +
						"but contains:\n <1> file/directory\n <%s>\n", preparePath(), preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesAndDirs(2);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesAndDirs(0);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <0> files/directories\n" +
								"but contains:\n <2> files/directories\n <%s>\n <%s>\n",
						preparePath(), preparePath(), preparePath("oneFile"))
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirs(19);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirs(5, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirs(6, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirs(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndDirs(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "file11")));
	}


	private void whenHasCountOfFilesAndDirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFilesAndDirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}