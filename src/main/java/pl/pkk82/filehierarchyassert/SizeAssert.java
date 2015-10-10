package pl.pkk82.filehierarchyassert;

class SizeAssert {
	private final int size;
	private String overriddenMessage;

	SizeAssert(int size) {
		this.size = size;
	}

	public SizeAssert overridingErrorMessage(String message, String... args) {
		this.overriddenMessage = String.format(message, args);
		return this;
	}

	public SizeAssert isGreaterThan(int i) {
		if (size <= i) {
			throw new AssertionError(overriddenMessage);
		}
		return this;
	}

	public SizeAssert isEqualTo(int i) {
		if (size != i) {
			throw new AssertionError(overriddenMessage);
		}
		return this;
	}
}
