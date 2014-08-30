package pl.pkk82.filehierarchyassert;

import org.junit.Test;

public class FileHierarchyAssertHasRootDirWithNameTest extends AbstractFileHiearchyAssertTest {


	@Test
	public void shouldSucceedWithName() {
		givenFileHierarchyAssert();
		whenHasRootDirWithName("fileHierarchy");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithDifferentName() {
		givenFileHierarchyAssert();
		whenHasRootDirWithName("fileHierarchy1");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: fileHierarchy1>",
				preparePath()));
	}

	@Test
	public void shouldSucceedWithRegexInAssert() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasRootDirWithName("f.l[eE]Hierar.+");
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInAssert() {
		givenFileHierarchyAssert(StringMatcher.REGEX);
		whenHasRootDirWithName("f.l[eE]Hirar.+");
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: f.l[eE]Hirar.+>",
				preparePath()));
	}

	@Test
	public void shouldSucceedWithRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasRootDirWithName("f.l[eE]Hierar.+", StringMatcher.REGEX);
		thenAssertionIsSucceeded();
	}

	@Test
	public void shouldFailWithUnmatchingRegexInMethod() {
		givenFileHierarchyAssert();
		whenHasRootDirWithName("f.l[eE]Hirar.+", StringMatcher.REGEX);
		thenAssertionIsFailed().hasMessage(String.format("\nExpecting:\n <%s>\nto have:\n <file name: f.l[eE]Hirar.+>",
				preparePath()));
	}

	private void whenHasRootDirWithName(String rootDirName) {
		try {
			fileHierarchyAssert.hasRootDirWithName(rootDirName);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

	private void whenHasRootDirWithName(String rootDirName, StringMatcher stringMatcher) {
		try {
			fileHierarchyAssert.hasRootDirWithName(rootDirName, stringMatcher);
		} catch (AssertionError e) {
			handleAssertionError(e);
		}
	}

}