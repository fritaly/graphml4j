package fr.ritaly.graphml4j;

public enum Arrow {
	NONE("none"),
	STANDARD("standard"),
	DELTA("delta"),
	WHITE_DELTA("white_delta"),
	DIAMOND("diamond"),
	WHITE_DIAMOND("white_diamond"),
	SHORT("short"),
	PLAIN("plain"),
	CONCAVE("concave"),
	CONVEX("convex"),
	CIRCLE("circle"),
	TRANSPARENT_CIRCLE("transparent_circle"),
	DASH("dash"),
	SKEWED_DASH("skewed_dash"),
	T_SHAPE("t_shape"),
	CROWS_FOOT_ONE_MANDATORY("crows_foot_one_mandatory"),
	CROWS_FOOT_MANY_MANDATORY("crows_foot_many_mandatory"),
	CROWS_FOOT_ONE_OPTIONAL("crows_foot_one_optional"),
	CROWS_FOOT_MANY_OPTIONAL("crows_foot_many_optional"),
	CROWS_FOOT_ONE("crows_foot_one"),
	CROWS_FOOT_MANY("crows_foot_many"),
	CROWS_FOOT_OPTIONAL("crows_foot_optional")
	;

	private final String value;

	private Arrow(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
