package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFiles(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFiles(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> files\n" +
						"but contains:\n <0> files\n", preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFiles(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFiles(0);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <0> files\n" +
						"but contains:\n <1> file\n <%s>\n", preparePath(), preparePath("oneFile"))
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfFiles(9);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfFiles(2, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfFiles(3, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfFiles(1, "dir1", "dir12", "file121");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <file121>,\n" +
						"but it contains:\n <no directories>\n",
				preparePath("dir1", "dir12")));
	}




	private void whenHasCountOfFiles(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFiles(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}