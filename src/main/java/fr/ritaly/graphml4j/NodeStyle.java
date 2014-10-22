package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Alignment;
import fr.ritaly.graphml4j.base.FontStyle;
import fr.ritaly.graphml4j.base.LineType;
import fr.ritaly.graphml4j.base.Placement;
import fr.ritaly.graphml4j.base.Position;
import fr.ritaly.graphml4j.base.Shape;
import fr.ritaly.graphml4j.base.SizePolicy;

public class NodeStyle {

	private final GenericObject genericObject = new GenericObject();

	private final ShapeObject shapeObject = new ShapeObject();

	private final LabelObject labelObject = new LabelObject();

	public NodeStyle() {
	}

	public NodeStyle(NodeStyle style) {
		apply(style);
	}

	void apply(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Apply the label properties
		this.labelObject.apply(style.labelObject);

		// Apply the generic properties
		this.genericObject.apply(style.genericObject);

		// Apply the shape properties
		this.shapeObject.apply(style.shapeObject);
	}

//	public void setInsets(int value) {
//		setBottomInset(value);
//		setTopInset(value);
//		setLeftInset(value);
//		setRightInset(value);
//	}
//
//	public boolean hasInsets() {
//		return (bottomInset != 0) || (topInset != 0) || (leftInset != 0) || (rightInset != 0);
//	}
//
//	public int getBottomInset() {
//		return bottomInset;
//	}
//
//	public int getLeftInset() {
//		return leftInset;
//	}
//
//	public int getRightInset() {
//		return rightInset;
//	}
//
//	public int getTopInset() {
//		return topInset;
//	}
//
//	public void setBottomInset(int value) {
//		this.bottomInset = value;
//	}
//
//	public void setLeftInset(int value) {
//		this.leftInset = value;
//	}
//
//	public void setRightInset(int value) {
//		this.rightInset = value;
//	}
//
//	public void setTopInset(int value) {
//		this.topInset = value;
//	}

//	public boolean isHasBackgroundColor() {
//		return hasBackgroundColor;
//	}
//
//	public void setHasBackgroundColor(boolean hasBackgroundColor) {
//		this.hasBackgroundColor = hasBackgroundColor;
//	}
//
//	public boolean isHasLineColor() {
//		return hasLineColor;
//	}
//
//	public void setHasLineColor(boolean hasLineColor) {
//		this.hasLineColor = hasLineColor;
//	}

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

	// --- Label properties --- //

	public boolean isUnderlinedText() {
		return labelObject.isUnderlinedText();
	}

	public void setUnderlinedText(boolean value) {
		labelObject.setUnderlinedText(value);
	}

	public boolean isVisible() {
		return labelObject.isVisible();
	}

	public void setVisible(boolean visible) {
		labelObject.setVisible(visible);
	}

	public Color getTextColor() {
		return labelObject.getTextColor();
	}

	public void setTextColor(Color color) {
		labelObject.setTextColor(color);
	}

	public int getFontSize() {
		return labelObject.getFontSize();
	}

	public void setFontSize(int fontSize) {
		labelObject.setFontSize(fontSize);
	}

	public String getFontFamily() {
		return labelObject.getFontFamily();
	}

	public void setFontFamily(String fontFamily) {
		labelObject.setFontFamily(fontFamily);
	}

	public Alignment getTextAlignment() {
		return labelObject.getTextAlignment();
	}

	public void setTextAlignment(Alignment textAlignment) {
		labelObject.setTextAlignment(textAlignment);
	}

	public FontStyle getFontStyle() {
		return labelObject.getFontStyle();
	}

	public void setFontStyle(FontStyle fontStyle) {
		labelObject.setFontStyle(fontStyle);
	}

	void writeLabel(XMLStreamWriter writer, String label) throws XMLStreamException {
		labelObject.writeTo(writer, label);
	}

	public Color getBackgroundColor() {
		return labelObject.getBackgroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		labelObject.setBackgroundColor(backgroundColor);
	}

	public Color getLineColor() {
		return labelObject.getLineColor();
	}

	public void setLineColor(Color lineColor) {
		labelObject.setLineColor(lineColor);
	}

	public Placement getPlacement() {
		return labelObject.getPlacement();
	}

	public Position getPosition() {
		return labelObject.getPosition();
	}

	public void setPlacement(Placement placement) {
		labelObject.setPlacement(placement);
	}

	public void setPosition(Position position) {
		labelObject.setPosition(position);
	}

	public int getBottomInset() {
		return labelObject.getBottomInset();
	}

	public int getLeftInset() {
		return labelObject.getLeftInset();
	}

	public int getRightInset() {
		return labelObject.getRightInset();
	}

	public int getTopInset() {
		return labelObject.getTopInset();
	}

	public void setBottomInset(int bottomInset) {
		labelObject.setBottomInset(bottomInset);
	}

	public void setLeftInset(int leftInset) {
		labelObject.setLeftInset(leftInset);
	}

	public void setRightInset(int rightInset) {
		labelObject.setRightInset(rightInset);
	}

	public void setTopInset(int topInset) {
		labelObject.setTopInset(topInset);
	}

	public boolean hasInsets() {
		return labelObject.hasInsets();
	}

	public void setInsets(int value) {
		labelObject.setInsets(value);
	}

	public SizePolicy getSizePolicy() {
		return labelObject.getSizePolicy();
	}

	public void setSizePolicy(SizePolicy policy) {
		labelObject.setSizePolicy(policy);
	}
}