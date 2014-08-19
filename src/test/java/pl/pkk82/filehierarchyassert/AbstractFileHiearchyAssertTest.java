package pl.pkk82.filehierarchyassert;

import static org.assertj.core.api.BDDAssertions.then;
import static pl.pkk82.filehierarchygenerator.FileHierarchyGenerator.createRootDirectory;

import java.nio.file.Path;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ThrowableAssert;

import pl.pkk82.filehierarchygenerator.FileHierarchy;
import pl.pkk82.filehierarchygenerator.FileHierarchyGenerator;

public class AbstractFileHiearchyAssertTest {

	protected FileHierarchyGenerator fileHierarchyGenerator;
	protected FileHierarchyAssert fileHierarchyAssert;
	protected ThrowableAssert throwableAssert;

	protected FileHierarchy fileHierarchy;
	protected FileHierarchy fileHierarchyEmpty;
	protected FileHierarchy fileHierarchyWithOneFile;


	protected void givenFileHierarchyEmptyAssert() {
		fileHierarchyEmpty =
				createRootDirectory("fileHierarchyEmpty")
						.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchyEmpty);
	}

	protected void givenFileHierarchyWithOneFileAssert() {
		fileHierarchyWithOneFile =
				createRootDirectory("fileHierarchyWithOneFile")
						.file("oneFile")
						.line("oneFile")
						.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchyWithOneFile);
	}


	protected void givenFileHierarchyAssert(StringMatcher stringMatcher) {
		givenFileHierarchyGenerator();
		fileHierarchy = fileHierarchyGenerator.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy, stringMatcher);
	}

	protected void givenFileHierarchyAssert() {
		givenFileHierarchyGenerator();
		fileHierarchy = fileHierarchyGenerator.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy);
	}


	private void givenFileHierarchyGenerator() {
		fileHierarchyGenerator = createRootDirectory("fileHierarchy")
				.directory("dir1")
				.directory("dir11").up()
				.directory("dir12")
				.file("file121").line("file121").line("file121").up()
				.file("file11").line("file11").up()
				.directory("dir2")
				.directory("dir21")
				.file("file211").line("file211").line("file211")
				.file("file212").line("file212").line("file212").up()
				.directory("dir22")
				.directory("dir221")
				.directory("dir2211")
				.directory("dir22111").up()
				.file("file22111").line("file22111").line("file22111").line("file22111").line("file22111")
				.file("file22112").line("file22112").line("file22112").line("file22112").line("file22112").up()
				.file("file2211").line("file2211").line("file2211").line("file2211").up().up().up()
				.file("file1")
				.file("file2");
	}

	protected void thenAssertionIsSucceeded() {
		String description = throwableAssert == null ? null : throwableAssert.descriptionText();
		then(throwableAssert).overridingErrorMessage("throwable assert shoul be null but was " + description).isNull();
	}

	protected ThrowableAssert thenAssertionIsFailed() {
		ObjectAssert<ThrowableAssert> then = then(throwableAssert);
		then.overridingErrorMessage("assert error should be thrown").isNotNull();
		return throwableAssert;
	}

	protected Path preparePath(String... path) {
		Path resolvedPath = getNonNullFileHierarchy().getRootDirectoryAsPath();
		for (String pathElement : path) {
			resolvedPath = resolvedPath.resolve(pathElement);
		}
		return resolvedPath;
	}


	protected Path getNonExistingDirFromFileHierarchyEmpty() {
		return fileHierarchyEmpty.getRootDirectoryAsPath().resolve("someDir");
	}


	protected Path getFileFromFileHierarchyWithOneFile() {
		return fileHierarchyWithOneFile.getRootDirectoryAsPath().resolve("oneFile");
	}

	private FileHierarchy getNonNullFileHierarchy() {
		if (fileHierarchy != null) {
			return fileHierarchy;
		} else if (fileHierarchyWithOneFile != null) {
			return fileHierarchyWithOneFile;

		} else {
			return fileHierarchyEmpty;
		}
	}
}
