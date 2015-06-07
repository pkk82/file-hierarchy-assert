package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesAndSubdirsTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesAndSubdirs(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesAndSubdirs(1);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <1> file/directory\n" +
						"but contains:\n <0> files/directories\n", preparePath()));
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesAndSubdirs(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesAndSubdirs(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> files/directories\n" +
								"but contains:\n <1> file/directory\n <%s>\n", preparePath(), preparePath("oneFile"))
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndSubdirs(19);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndSubdirs(4, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndSubdirs(6, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesAndSubdirs(1, "dir2", "dir11", "dir221");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <dir11>,\n" +
						"but it contains:\n <%s>\n <%s>\n",
				preparePath("dir2"),
				preparePath("dir2", "dir21"),
				preparePath("dir2", "dir22")));
	}

	private void whenHasCountOfFilesAndSubdirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFilesAndSubdirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}