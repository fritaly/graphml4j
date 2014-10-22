package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Alignment;
import fr.ritaly.graphml4j.base.FontStyle;
import fr.ritaly.graphml4j.base.LineType;
import fr.ritaly.graphml4j.base.Shape;

public class NodeStyle {

	private Alignment textAlignment = Alignment.CENTER;

	private FontStyle fontStyle = FontStyle.PLAIN;

	private String fontFamily = "Dialog";

	private int fontSize = 12;

	private Color textColor = Color.BLACK;

	private boolean hasBackgroundColor = false;

	private boolean hasLineColor = false;

	private boolean visible = true;

	private boolean underlinedText = false;

	private int bottomInset, leftInset, rightInset, topInset;

	private final GenericObject genericObject = new GenericObject();

	private final ShapeObject shapeObject = new ShapeObject();

	public NodeStyle() {
	}

	public NodeStyle(NodeStyle style) {
		apply(style);
	}

	void apply(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		this.fontFamily = style.fontFamily;
		this.fontSize = style.fontSize;
		this.fontStyle = style.fontStyle;
		this.hasBackgroundColor = style.hasBackgroundColor;
		this.hasLineColor = style.hasLineColor;
		this.textAlignment = style.textAlignment;
		this.textColor = style.textColor;
		this.visible = style.visible;

		// Apply the generic properties
		this.genericObject.apply(style.genericObject);

		// Apply the shape properties
		this.shapeObject.apply(style.shapeObject);

		this.underlinedText = style.underlinedText;

		this.bottomInset = style.bottomInset;
		this.leftInset = style.leftInset;
		this.rightInset = style.rightInset;
		this.topInset = style.topInset;
	}

	public void setInsets(int value) {
		setBottomInset(value);
		setTopInset(value);
		setLeftInset(value);
		setRightInset(value);
	}

	public boolean hasInsets() {
		return (bottomInset != 0) || (topInset != 0) || (leftInset != 0) || (rightInset != 0);
	}

	public int getBottomInset() {
		return bottomInset;
	}

	public int getLeftInset() {
		return leftInset;
	}

	public int getRightInset() {
		return rightInset;
	}

	public int getTopInset() {
		return topInset;
	}

	public void setBottomInset(int value) {
		this.bottomInset = value;
	}

	public void setLeftInset(int value) {
		this.leftInset = value;
	}

	public void setRightInset(int value) {
		this.rightInset = value;
	}

	public void setTopInset(int value) {
		this.topInset = value;
	}

	public boolean isUnderlinedText() {
		return underlinedText;
	}

	public void setUnderlinedText(boolean value) {
		this.underlinedText = value;
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

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color color) {
		Validate.notNull(color, "The given text color is null");

		this.textColor = color;
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

	// --- Shape properties --- //

	public Shape getShape() {
		return shapeObject.getShape();
	}

	public void setShape(Shape shape) {
		shapeObject.setShape(shape);
	}

	public Color getShadowColor() {
		return shapeObject.getShadowColor();
	}

	public int getShadowOffsetX() {
		return shapeObject.getShadowOffsetX();
	}

	public int getShadowOffsetY() {
		return shapeObject.getShadowOffsetY();
	}

	public void setShadowColor(Color color) {
		shapeObject.setShadowColor(color);
	}

	public void setShadowOffsetX(int value) {
		shapeObject.setShadowOffsetX(value);
	}

	public void setShadowOffsetY(int value) {
		shapeObject.setShadowOffsetY(value);
	}

	void writeShape(XMLStreamWriter writer) throws XMLStreamException {
		shapeObject.writeShape(writer);
	}

	void writeDropShadow(XMLStreamWriter writer) throws XMLStreamException {
		shapeObject.writeDropShadow(writer);
	}

	// --- Generic properties --- //

	public float getHeight() {
		return genericObject.getHeight();
	}

	public void setHeight(float height) {
		genericObject.setHeight(height);
	}

	public float getWidth() {
		return genericObject.getWidth();
	}

	public void setWidth(float width) {
		genericObject.setWidth(width);
	}

	public Color getFillColor() {
		return genericObject.getFillColor();
	}

	public void setFillColor(Color color) {
		genericObject.setFillColor(color);
	}

	public Color getFillColor2() {
		return genericObject.getFillColor2();
	}

	public void setFillColor2(Color fillColor2) {
		genericObject.setFillColor2(fillColor2);
	}

	public Color getBorderColor() {
		return genericObject.getBorderColor();
	}

	public void setBorderColor(Color color) {
		genericObject.setBorderColor(color);
	}

	public LineType getBorderType() {
		return genericObject.getBorderType();
	}

	public void setBorderType(LineType borderType) {
		genericObject.setBorderType(borderType);
	}

	public float getBorderWidth() {
		return genericObject.getBorderWidth();
	}

	public boolean isTransparentFill() {
		return genericObject.isTransparentFill();
	}

	public void setTransparentFill(boolean transparentFill) {
		genericObject.setTransparentFill(transparentFill);
	}

	public void setBorderWidth(float borderWidth) {
		genericObject.setBorderWidth(borderWidth);
	}

	void writeGeometry(XMLStreamWriter writer) throws XMLStreamException {
		genericObject.writeGeometry(writer);
	}

	void writeFill(XMLStreamWriter writer) throws XMLStreamException {
		genericObject.writeFill(writer);
	}

	void writeBorderStyle(XMLStreamWriter writer) throws XMLStreamException {
		genericObject.writeBorderStyle(writer);
	}
}