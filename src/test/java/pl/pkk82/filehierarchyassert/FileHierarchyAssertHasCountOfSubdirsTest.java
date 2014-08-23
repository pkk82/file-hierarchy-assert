package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

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
		whenHasCountOfSubdirs(9);
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
		whenHasCountOfSubdirs(2, "dir2", "dir22", "dir221");
		thenAssertionIsSucceeded();
	}



	@Test
	public void shouldFailWhenNoDirectory() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(1, "dir1", "dir11", "dir111");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "dir11", "dir111")));
	}

	@Test
	public void shouldFailWhenFile() {
		givenFileHierarchyAssert();
		whenHasCountOfSubdirs(1, "dir1", "file11");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be an existing directory\n",
				preparePath("dir1", "file11")));
	}




	private void whenHasCountOfSubdirs(int count, String... path) {
		try {
			fileHierarchyAssert.hasCountOfSubdirs(count, path);
		} catch (AssertionError e) {
			throwableAssert = then(e).describedAs(e.getMessage());
		}
	}
}