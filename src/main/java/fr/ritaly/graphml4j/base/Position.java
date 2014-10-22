package fr.ritaly.graphml4j.base;

public enum Position {
	ANYWHERE(null),

	// --- //

	CENTER("c"),
	TOP("t"),
	BOTTOM("b"),
	LEFT("l"),
	RIGHT("r"),
	TOP_LEFT("tl"),
	TOP_RIGHT("tr"),
	BOTTOM_LEFT("bl"),
	BOTTOM_RIGHT("br"),

	// --- //

	NORTH_WEST("nw"),
	NORTH_EAST("ne"),
	SOUTH_WEST("sw"),
	SOUTH_EAST("se"),

	// --- //

	NORTH("n"),
	SOUTH("s"),
	EAST("e"),
	WEST("w");

	private final String value;

	private Position(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
