package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class FileHierarchyAssertHasParentDirWithNameTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWithRegexInAssert() {
		givenFileHierarchyAssert(NameMatcher.REGEX);
		whenHasParentDirWithName("file-hierarchy-generator-\\d+");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInAssert() {
		givenFileHierarchyAssert(NameMatcher.REGEX);
		whenHasParentDirWithName("\\d+");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: \\d+>",
				prepareParentPath()));
	}

	@Test
	public void shouldSucceedWithRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasParentDirWithName("file-hierarchy-generator-\\d+", NameMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasParentDirWithName("\\d+", NameMatcher.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: \\d+>",
				prepareParentPath()));
	}

	private void whenHasParentDirWithName(String rootDirName) {
		try {
			fileHierarchyAssert.hasParentDirWithName(rootDirName);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

	private void whenHasParentDirWithName(String rootDirName, NameMatcher nameMatcher) {
		try {
			fileHierarchyAssert.hasParentDirWithName(rootDirName, nameMatcher);
		} catch (AssertionError e) {
			throwableAssert = then(e);
		}
	}

	private String prepareParentPath() {
		return fileHierarchy.getRootDirectoryAsPath().getParent().toString();
	}

}