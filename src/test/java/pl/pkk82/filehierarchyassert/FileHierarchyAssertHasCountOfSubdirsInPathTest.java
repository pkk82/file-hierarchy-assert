package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertHasCountOfSubdirsInPathTest extends AbstractFileHiearchyAssertTest {

	@Test
	public void shouldSucceedWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfSubdirsInPath(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenEmpty() {
		givenFileHierarchyEmptyAssert();
		whenHasCountOfSubdirsInPath(1);
		thenAssertionIsFailed().hasMessage(String.format("Expecting:\n %s\nto contain:\n 1 directory\n" +
						"but contains:\n 0 directories", preparePath())
		);
	}

	@Test
	public void shouldSucceedWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfSubdirsInPath(0);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenOneFile() {
		givenFileHierarchyWithOneFileAssert();
		whenHasCountOfSubdirsInPath(1);
		thenAssertionIsFailed().hasMessage(String.format("Expecting:\n %s\nto contain:\n 1 directory\n" +
						"but contains:\n 0 directories", preparePath())
		);
	}

	@Test
	public void shouldSuccedForRoot() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsInPath(9);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForOneLevelPath() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsInPath(2, "dir1");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldSuccedForTwoLevel() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsInPath(2, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}



	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsInPath(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting file:<%s> to exist",
				preparePath("dir1", "dir11", "dir111")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirsInPath(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory",
				preparePath("dir1", "file11")));
	}




	private void whenHasCountOfSubdirsInPath(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfSubdirsInPath(count, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}
}