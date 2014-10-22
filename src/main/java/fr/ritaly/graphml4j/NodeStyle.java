package fr.ritaly.graphml4j;

import org.apache.commons.lang.Validate;

public final class NodeStyle {

	private float height = 30.0f;

	private float width = 30.0f;

	private String fillColor = "#FFCC00";

	private boolean transparentFill = false;

	private String borderColor = "#000000";

	private LineType borderType = LineType.LINE;

	private float borderWidth = 1.0f;

	private Shape shape = Shape.RECTANGLE;

	private Alignment textAlignment = Alignment.CENTER;

	private FontStyle fontStyle = FontStyle.PLAIN;

	private String fontFamily = "Dialog";

	private int fontSize = 12;

	private String textColor = "#000000";

	private boolean hasBackgroundColor = false;

	private boolean hasLineColor = false;

	private boolean visible = true;

	public NodeStyle() {
	}

	public NodeStyle(NodeStyle style) {
		apply(style);
	}

	void apply(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		this.borderColor = style.borderColor;
		this.borderType = style.borderType;
		this.borderWidth = style.borderWidth;
		this.fillColor = style.fillColor;
		this.fontFamily = style.fontFamily;
		this.fontSize = style.fontSize;
		this.fontStyle = style.fontStyle;
		this.hasBackgroundColor = style.hasBackgroundColor;
		this.hasLineColor = style.hasLineColor;
		this.height = style.height;
		this.shape = style.shape;
		this.textAlignment = style.textAlignment;
		this.textColor = style.textColor;
		this.transparentFill = style.transparentFill;
		this.visible = style.visible;
		this.width = style.width;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHasBackgroundColor() {
		return hasBackgroundColor;
	}

	public void setHasBackgroundColor(boolean hasBackgroundColor) {
		this.hasBackgroundColor = hasBackgroundColor;
	}

	public boolean isHasLineColor() {
		return hasLineColor;
	}

	public void setHasLineColor(boolean hasLineColor) {
		this.hasLineColor = hasLineColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		Validate.notNull(textColor, "The given text color is null");

		this.textColor = textColor;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		Validate.isTrue(fontSize > 0, String.format("The given font size (%d) must be positive", fontSize));

		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		Validate.notNull(fontFamily, "The given font family is null");

		this.fontFamily = fontFamily;
	}

	public Alignment getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(Alignment textAlignment) {
		Validate.notNull(textAlignment, "The given text alignment is null");

		this.textAlignment = textAlignment;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle fontStyle) {
		Validate.notNull(fontStyle, "The given font style is null");

		this.fontStyle = fontStyle;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		Validate.isTrue(height > 0, String.format("The given height (%f) must be positive", height));

		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		this.width = width;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		Validate.notNull(fillColor, "The given fill color is null");

		this.fillColor = fillColor;
	}

	public boolean isTransparentFill() {
		return transparentFill;
	}

	public void setTransparentFill(boolean transparentFill) {
		this.transparentFill = transparentFill;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		Validate.notNull(borderColor, "The given border color is null");

		this.borderColor = borderColor;
	}

	public LineType getBorderType() {
		return borderType;
	}

	public void setBorderType(LineType borderType) {
		Validate.notNull(borderType, "The given border type is null");

		this.borderType = borderType;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		Validate.isTrue(borderWidth > 0, String.format("The given border width (%f) must be positive", borderWidth));

		this.borderWidth = borderWidth;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		Validate.notNull(shape, "The given shape is null");

		this.shape = shape;
	}

}