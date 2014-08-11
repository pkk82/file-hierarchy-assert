package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfFilesInPathTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesInPath(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfFilesInPath(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> files\n" +
						"but contains:\n <0> files\n", preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesInPath(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfFilesInPath(0);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <0> files\n" +
						"but contains:\n <1> file\n <%s>\n", preparePath(), preparePath("oneFile"))
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesInPath(9);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesInPath(2, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesInPath(3, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}



	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesInPath(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfFilesInPath(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "file11")));
	}




	private void whenHasCountOfFilesInPath(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfFilesInPath(count, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}
}