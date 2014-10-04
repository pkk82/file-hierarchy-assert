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
	protected FileHierarchy fileHierarchy;
	protected FileHierarchy fileHierarchyEmpty;
	protected FileHierarchy fileHierarchyWithOneFile;

	private ThrowableAssert throwableAssert;


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


	protected void givenFileHierarchyAssert(NameMatcherType nameMatcherType) {
		givenFileHierarchyGenerator();
		fileHierarchy = fileHierarchyGenerator.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy, nameMatcherType);
	}

	protected void givenFileHierarchyAssert() {
		givenFileHierarchyGenerator();
		fileHierarchy = fileHierarchyGenerator.generate();
		fileHierarchyAssert = new FileHierarchyAssert(fileHierarchy);
	}


	private void givenFileHierarchyGenerator() {
		fileHierarchyGenerator = createRootDirectory("fileHierarchy")

				.file("file1")

				.file("file2")

				.directory("dir1")
				.file("file11").line("file11-line1")
				.directory("dir11").up()
				.directory("dir12")
				.file("file121").line("file121-line1").line("file121-line2").up().up()

				.directory("dir2")

				.directory("dir21")
				.file("file211").line("file211-line1").line("file211-line2")
				.file("file212").line("file212-line1").line("file212-line2").up()

				.directory("dir22")
				.directory("dir221")
				.file("file2211").line("file2211-line1").line("file2211-line2").line("file2211-line3")

				.directory("dir2211")
				.file("file22111").line("file22111-line1").line("file22111-line2").line("file22111-line3")
				.line("file22111-line4")
				.file("file22112").line("file22112-line1").line("file22112-line2").line("file22112-line3")
				.line("file22112-line4")
				.directory("dir22111")
				.directory("dir221111");
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

	protected void handleAssertionError(AssertionError e) {
		throwableAssert = then(e).describedAs(e.getMessage());
	}
}
