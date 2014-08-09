package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.nio.file.Path;

import org.junit.Test;

import pl.pkk82.filehierarchygenerator.FileHierarchy;

public class FileHierarchyAssertTest extends  AbstractFileHiearchyAssertTest  {


	@Test
	public void shouldThrowExceptionWhenPathIsNull() {
		try {
			whenFileHierarchyAssertFrom((Path) null);
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileIsNull() {
		try {
			whenFileHierarchyAssertFrom((File) null);
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileHierarchyIsNull() {
		try {
			whenFileHierarchyAssertFrom((FileHierarchy) null);
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenPathDoesNotExist() {
		givenFileHierarchyEmptyAssert();
		try {
			whenFileHierarchyAssertFrom(getNonExistingDirFromFileHierarchyEmpty());
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileDoesNotExist() {
		givenFileHierarchyEmptyAssert();
		try {
			whenFileHierarchyAssertFrom(getNonExistingDirFromFileHierarchyEmpty().toFile());
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileHierarchyDoesNotExist() {
		givenFileHierarchyEmptyAssert();
		try {
			whenFileHierarchyAssertFrom(new FileHierarchy(getNonExistingDirFromFileHierarchyEmpty()));
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}


	@Test
	public void shouldThrowExceptionWhenPathIsNotDir() {
		givenFileHierarchyWithOneFileAssert();
		try {
			whenFileHierarchyAssertFrom(getFileFromFileHierarchyWithOneFile());
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileIsNotDir() {
		givenFileHierarchyWithOneFileAssert();
		try {
			whenFileHierarchyAssertFrom(getFileFromFileHierarchyWithOneFile().toFile());
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	@Test
	public void shouldThrowExceptionWhenFileHierarchyIsNotDir() {
		givenFileHierarchyWithOneFileAssert();
		try {
			whenFileHierarchyAssertFrom(new FileHierarchy(getFileFromFileHierarchyWithOneFile()));
		} catch (Throwable e) {
			then(e).isInstanceOf(AssertionError.class);
		}
	}

	private void whenFileHierarchyAssertFrom(Path path) {
		fileHierarchyAssert = new FileHierarchyAssert(path);
	}


	private void whenFileHierarchyAssertFrom(File file) {
		fileHierarchyAssert = new FileHierarchyAssert(file);
	}


	private void whenFileHierarchyAssertFrom(FileHierarchy fileHierarchy) {
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy);
	}







}