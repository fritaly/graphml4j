package fr.ritaly.graphml4j;

import org.apache.commons.lang.Validate;

public class GroupStyle extends NodeStyle {

	private float insets = 15.0f;

	private float borderInsets = 0.0f;

	public GroupStyle() {
		// Apply the default values here
		setHeight(80.0f);
		setWidth(140.0f);
		setFillColor("#F5F5F5");
		setTransparentFill(false);
		setBorderColor("#000000");
		setBorderType(LineType.DASHED);
		setBorderWidth(1.0f);
		setShape(Shape.ROUNDED_RECTANGLE);
		setFontFamily("Dialog");
		setFontSize(15);
		setFontStyle(FontStyle.PLAIN);
		setHasBackgroundColor(false);
		setHasLineColor(false);
		setTextAlignment(Alignment.CENTER);
		setTextColor("#000000");
		setVisible(true);
	}

	public GroupStyle(GroupStyle style) {
		apply(style);
	}

	void apply(GroupStyle style) {
		Validate.notNull(style, "The given style is null");

		// Apply the node-specific attributes
		super.apply(style);

		// Apply the group-specific attributes
		this.insets = style.insets;
		this.borderInsets = style.borderInsets;
	}

	public float getBorderInsets() {
		return borderInsets;
	}

	public void setBorderInsets(float value) {
		Validate.isTrue(value >= 0, String.format("The given border insets (%f) must be positive or zero", value));

		this.borderInsets = value;
	}

	public float getInsets() {
		return insets;
	}

	public void setInsets(float value) {
		Validate.isTrue(value >= 0, String.format("The given insets (%f) must be positive or zero", value));

		this.insets = value;
	}
}