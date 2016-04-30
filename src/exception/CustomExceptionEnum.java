package exception;

/* Enumeration for all exception */

public enum CustomExceptionEnum {

	FileNotFound(1), FileMissingBasePrice(2), FileOptionPriceNotFound(3), CarModelNotFound(4), SavedCarFileNotFound(
			5), OptionSetNotFound(6), OptionNotFound(7);

	private int value;

	private CustomExceptionEnum(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
