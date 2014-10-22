package fr.ritaly.graphml4j.base;

public enum FontStyle {
	PLAIN("plain"),
	BOLD("bold"),
	ITALIC("italic"),
	BOLD_AND_ITALIC("bolditalic")
	;

	private final String value;

	private FontStyle(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
