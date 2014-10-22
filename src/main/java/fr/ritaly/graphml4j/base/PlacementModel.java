package fr.ritaly.graphml4j.base;

public enum PlacementModel {
	INTERNAL("internal"),
	CUSTOM("custom"),
	EDGE_OPPOSITE("edge_opposite"),
	EIGHT_POSITION("eight_pos"),
	FREE("free"),
	CORNERS("corners"),
	SANDWICH("sandwich"),
	SIDES("sides")
	;

	private final String value;

	private PlacementModel(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
