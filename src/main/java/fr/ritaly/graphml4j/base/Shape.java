package fr.ritaly.graphml4j.base;

public enum Shape {
	RECTANGLE("rectangle"),
	TRIANGLE("triangle"),
	ROUNDED_RECTANGLE("roundrectangle"),
	ELLIPSE("ellipse"),
	PARALLELOGRAM("parallelogram"),
	HEXAGON("hexagon"),
	RECTANGLE_3D("rectangle3d"),
	OCTAGON("octagon"),
	DIAMOND("diamond"),
	TRAPEZOID("trapezoid"),
	TRAPEZOID_2("trapezoid2")
	;

	private final String value;

	private Shape(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
