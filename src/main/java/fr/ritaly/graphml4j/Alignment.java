package fr.ritaly.graphml4j;

public enum Alignment {
	CENTER("center"),
	LEFT("left"),
	RIGHT("right")
	;

	private final String value;

	private Alignment(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
