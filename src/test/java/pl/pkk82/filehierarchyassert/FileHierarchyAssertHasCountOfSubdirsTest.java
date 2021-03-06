package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfSubdirsTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfSubdirs(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfSubdirs(1);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <1> directory\n" +
						"but contains:\n <0> directories\n", preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfSubdirs(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfSubdirs(1);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <1> directory\n" +
						"but contains:\n <0> directories\n", preparePath())
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(10);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(2, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(3, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}


	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <dir111>,\n" +
						"but it contains:\n <no directories>\n",
				preparePath("dir1", "dir11")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(1, "dir2", "dir22", "dir221", "file2211");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\n" +
						"to contain a directory with a name equal to:\n <file2211>,\n" +
						"but it contains:\n <%s>\n",
				preparePath("dir2", "dir22", "dir221"),
				preparePath("dir2", "dir22", "dir221", "dir2211")));
	}


	private void whenHasCountOfSubdirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfSubdirs(count, path);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}
}