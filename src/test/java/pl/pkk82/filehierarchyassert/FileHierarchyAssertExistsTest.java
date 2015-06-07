package pl.pkk82.filehierarchyassert;

import static pl.pkk82.filehierarchygenerator.FileHierarchyGenerator.createRootDirectory;

import org.junit.Test;

public class FileHierarchyAssertExistsTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWhenExists() {
		givenFileHierarchyEmptyAssert();
		whenExists();
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenDoesNotExist() {
		givenFileHierarchyAssertPointingToNonExistingFile();
		whenExists();
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto exist\n",
				preparePath("nonexisting")));
	}

	@Test
	public void shouldFailWhenFileExist() {
		givenFileHierarchyAssertPointingToExistingFile();
		whenExists();
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto be directory\n",
				preparePath("file")));
	}

	@Test
	public void shouldSucceedWhenDoesNotExist() {
		givenFileHierarchyAssertPointingToNonExistingFile();
		whenDoesNotExists();
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWhenExist() {
		givenFileHierarchyEmptyAssert();
		whenDoesNotExists();
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto not exist\n", preparePath()));
	}

	@Test
	public void shoulSucceedWhenFileExist() {
		givenFileHierarchyAssertPointingToExistingFile();
		whenDoesNotExists();
		thenAssertionIsSucceeded();
	}

	private void givenFileHierarchyAssertPointingToNonExistingFile() {
		fileHierarchyEmpty = createRootDirectory("fileHierarchyEmpty").generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchyEmpty.getRootDirectoryAsPath()
				.resolve("nonexisting"));
	}

	private void givenFileHierarchyAssertPointingToExistingFile() {
		fileHierarchy = createRootDirectory("fileHierarchy").file("file").generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy.getRootDirectoryAsPath()
				.resolve("file"));
	}


	private void whenExists() {
		try {
			fileHierarchyAssert.exists();
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenDoesNotExists() {
		try {
			fileHierarchyAssert.doesNotExist();
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}