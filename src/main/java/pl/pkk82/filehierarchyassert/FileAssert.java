package pl.pkk82.filehierarchyassert;

import java.io.File;

public class FileAssert {
	private final File file;
	private String overriddenMessage;

	FileAssert(File file) {
		this.file = file;
	}

	public FileAssert overridingErrorMessage(String s, String... args) {
		overriddenMessage = String.format(s, args);
		return this;
	}

	public FileAssert doesNotExist() {
		if (file.exists()) {
			handleAssert(null);
		}
		return this;
	}

	public FileAssert isDirectory() {
		if (!file.isDirectory()) {
			handleAssert(null);
		}
		return this;
	}

	public FileAssert exists() {
		if (!file.exists()) {
			return handleAssert(null);
		}
		return this;
	}

	public FileAssert has(IOFileFilterCondition ioFileFilterCondition) {
		if (!ioFileFilterCondition.matches(file)) {
			handleAssert(String.format("\nExpecting:\n <%s>\nto have:\n <%s>",
					file, ioFileFilterCondition.getDescription()));
		}
		return this;
	}

	private FileAssert handleAssert(String message) {
		throw new AssertionError(overriddenMessage == null ? message : overriddenMessage);
	}
}
