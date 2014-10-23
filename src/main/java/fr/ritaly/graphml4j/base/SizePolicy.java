package fr.ritaly.graphml4j.base;

/**
 * Enumeration of possible sizing policies.
 *
 * @author francois_ritaly
 */
public enum SizePolicy {
	CONTENT("content"),
	NODE_WIDTH("node_width"),
	NODE_HEIGHT("node_height"),
	NODE_SIZE("node_size")
	;

	private final String value;

	private SizePolicy(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
