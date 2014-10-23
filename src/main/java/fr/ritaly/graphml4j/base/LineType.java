package fr.ritaly.graphml4j.base;

/**
 * Enumeration of possible line / border types.
 *
 * @author francois_ritaly
 */
public enum LineType {
	LINE("line"),
	DASHED("dashed"),
	DOTTED("dotted"),
	DASHED_DOTTED("dashed_dotted")
	;

	private final String value;

	private LineType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
