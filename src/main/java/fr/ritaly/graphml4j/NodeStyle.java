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

	private final GeneralStyle generalStyle = new GeneralStyle();

	private final ShapeStyle shapeStyle = new ShapeStyle();

	private final LabelStyle labelStyle = new LabelStyle();

	public NodeStyle() {
	}

	public NodeStyle(NodeStyle style) {
		apply(style);
	}

	void apply(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Apply the label properties
		this.labelStyle.apply(style.labelStyle);

		// Apply the generic properties
		this.generalStyle.apply(style.generalStyle);

		// Apply the shape properties
		this.shapeStyle.apply(style.shapeStyle);
	}

	// --- Shape properties --- //

	public Shape getShape() {
		return shapeStyle.getShape();
	}

	public void setShape(Shape shape) {
		shapeStyle.setShape(shape);
	}

	public Color getShadowColor() {
		return shapeStyle.getShadowColor();
	}

	public int getShadowOffsetX() {
		return shapeStyle.getShadowOffsetX();
	}

	public int getShadowOffsetY() {
		return shapeStyle.getShadowOffsetY();
	}

	public void setShadowColor(Color color) {
		shapeStyle.setShadowColor(color);
	}

	public void setShadowOffsetX(int value) {
		shapeStyle.setShadowOffsetX(value);
	}

	public void setShadowOffsetY(int value) {
		shapeStyle.setShadowOffsetY(value);
	}

	// --- Generic properties --- //

	public float getHeight() {
		return generalStyle.getHeight();
	}

	public void setHeight(float height) {
		generalStyle.setHeight(height);
	}

	public float getWidth() {
		return generalStyle.getWidth();
	}

	public void setWidth(float width) {
		generalStyle.setWidth(width);
	}

	public Color getFillColor() {
		return generalStyle.getFillColor();
	}

	public void setFillColor(Color color) {
		generalStyle.setFillColor(color);
	}

	public Color getFillColor2() {
		return generalStyle.getFillColor2();
	}

	public void setFillColor2(Color fillColor2) {
		generalStyle.setFillColor2(fillColor2);
	}

	public Color getBorderColor() {
		return generalStyle.getBorderColor();
	}

	public void setBorderColor(Color color) {
		generalStyle.setBorderColor(color);
	}

	public LineType getBorderType() {
		return generalStyle.getBorderType();
	}

	public void setBorderType(LineType borderType) {
		generalStyle.setBorderType(borderType);
	}

	public float getBorderWidth() {
		return generalStyle.getBorderWidth();
	}

	public boolean isTransparentFill() {
		return generalStyle.isTransparentFill();
	}

	public void setTransparentFill(boolean transparentFill) {
		generalStyle.setTransparentFill(transparentFill);
	}

	public void setBorderWidth(float borderWidth) {
		generalStyle.setBorderWidth(borderWidth);
	}

	void writeTo(XMLStreamWriter writer, String label, float x, float y) throws XMLStreamException {
		generalStyle.writeTo(writer, x, y);
		labelStyle.writeTo(writer, label);
		shapeStyle.writeTo(writer);
	}

	// --- Label properties --- //

	public boolean isUnderlinedText() {
		return labelStyle.isUnderlinedText();
	}

	public void setUnderlinedText(boolean value) {
		labelStyle.setUnderlinedText(value);
	}

	public boolean isVisible() {
		return labelStyle.isVisible();
	}

	public void setVisible(boolean visible) {
		labelStyle.setVisible(visible);
	}

	public Color getTextColor() {
		return labelStyle.getTextColor();
	}

	public void setTextColor(Color color) {
		labelStyle.setTextColor(color);
	}

	public int getFontSize() {
		return labelStyle.getFontSize();
	}

	public void setFontSize(int fontSize) {
		labelStyle.setFontSize(fontSize);
	}

	public String getFontFamily() {
		return labelStyle.getFontFamily();
	}

	public void setFontFamily(String fontFamily) {
		labelStyle.setFontFamily(fontFamily);
	}

	public Alignment getTextAlignment() {
		return labelStyle.getTextAlignment();
	}

	public void setTextAlignment(Alignment textAlignment) {
		labelStyle.setTextAlignment(textAlignment);
	}

	public FontStyle getFontStyle() {
		return labelStyle.getFontStyle();
	}

	public void setFontStyle(FontStyle fontStyle) {
		labelStyle.setFontStyle(fontStyle);
	}

	public Color getBackgroundColor() {
		return labelStyle.getBackgroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		labelStyle.setBackgroundColor(backgroundColor);
	}

	public Color getLineColor() {
		return labelStyle.getLineColor();
	}

	public void setLineColor(Color lineColor) {
		labelStyle.setLineColor(lineColor);
	}

	public Placement getPlacement() {
		return labelStyle.getPlacement();
	}

	public Position getPosition() {
		return labelStyle.getPosition();
	}

	public void setPlacement(Placement placement) {
		labelStyle.setPlacement(placement);
	}

	public void setPosition(Position position) {
		labelStyle.setPosition(position);
	}

	public int getBottomInset() {
		return labelStyle.getBottomInset();
	}

	public int getLeftInset() {
		return labelStyle.getLeftInset();
	}

	public int getRightInset() {
		return labelStyle.getRightInset();
	}

	public int getTopInset() {
		return labelStyle.getTopInset();
	}

	public void setBottomInset(int bottomInset) {
		labelStyle.setBottomInset(bottomInset);
	}

	public void setLeftInset(int leftInset) {
		labelStyle.setLeftInset(leftInset);
	}

	public void setRightInset(int rightInset) {
		labelStyle.setRightInset(rightInset);
	}

	public void setTopInset(int topInset) {
		labelStyle.setTopInset(topInset);
	}

	public boolean hasInsets() {
		return labelStyle.hasInsets();
	}

	public void setInsets(int value) {
		labelStyle.setInsets(value);
	}

	public SizePolicy getSizePolicy() {
		return labelStyle.getSizePolicy();
	}

	public void setSizePolicy(SizePolicy policy) {
		labelStyle.setSizePolicy(policy);
	}
}