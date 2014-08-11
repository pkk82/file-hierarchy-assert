package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfDirsInPathTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfDirsInPath(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfDirsInPath(2);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <2> directories\n" +
						"but contains:\n <1> directory\n <%s>\n", preparePath(), preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfDirsInPath(1);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfDirsInPath(0);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto contain:\n <0> directories\n" +
						"but contains:\n <1> directory\n <%s>\n", preparePath(), preparePath())
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsInPath(10);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsInPath(3, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsInPath(3, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}



	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsInPath(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfDirsInPath(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "file11")));
	}




	private void whenHasCountOfDirsInPath(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfDirsInPath(count, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}
}