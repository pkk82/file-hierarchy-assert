package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasParentDirWithNameTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWithRegexInAssert() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasParentDirWithName("file-hierarchy-generator-\\d+");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInAssert() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasParentDirWithName("\\d+");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: \\d+>",
				prepareParentPath()));
	}

	@Test
	public void shouldSucceedWithRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasParentDirWithName("file-hierarchy-generator-\\d+", StringMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasParentDirWithName("\\d+", StringMatcher.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: \\d+>",
				prepareParentPath()));
	}

	private void whenHasParentDirWithName(String rootDirName) {
		try {
			fileHierarchyAssert.hasParentDirWithName(rootDirName);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasParentDirWithName(String rootDirName, StringMatcher stringMatcher) {
		try {
			fileHierarchyAssert.hasParentDirWithName(rootDirName, stringMatcher);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private String prepareParentPath() {
		return fileHierarchy.getRootDirectoryAsPath().getParent().toString();
	}

}