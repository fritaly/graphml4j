package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.LineType;

final class GenericObject {

	private float height = 30.0f;

	private float width = 30.0f;

	// This color is optional
	private Color fillColor = Color.decode("#FFCC00");

	// This color is optional
	private Color fillColor2;

	// This color is optional
	private Color borderColor = Color.BLACK;

	private LineType borderType = LineType.LINE;

	private float borderWidth = 1.0f;

	private boolean transparentFill = false;

	public GenericObject() {
	}

	public GenericObject(GenericObject object) {
		apply(object);
	}

	void apply(GenericObject object) {
		Validate.notNull(object, "The given object is null");

		this.borderColor = object.borderColor;
		this.borderType = object.borderType;
		this.borderWidth = object.borderWidth;
		this.fillColor = object.fillColor;
		this.fillColor2 = object.fillColor2;
		this.height = object.height;
		this.transparentFill = object.transparentFill;
		this.width = object.width;
	}

	public boolean isTransparentFill() {
		return transparentFill;
	}

	public void setTransparentFill(boolean transparentFill) {
		this.transparentFill = transparentFill;
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

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color color) {
		// This color is optional
		this.fillColor = color;
	}

	public Color getFillColor2() {
		return fillColor2;
	}

	public void setFillColor2(Color fillColor2) {
		// This color is optional
		this.fillColor2 = fillColor2;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color color) {
		// This color is optional
		this.borderColor = color;
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

	void writeGeometry(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:Geometry (the x & y attributes are computed when laying out the graph in yEd)
		writer.writeEmptyElement("y:Geometry");
		writer.writeAttribute("height", String.format("%.1f", height));
		writer.writeAttribute("width", String.format("%.1f", width));
		writer.writeAttribute("x", "0.0");
		writer.writeAttribute("y", "0.0");
	}

	void writeFill(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:Fill
		writer.writeEmptyElement("y:Fill");

		if (fillColor != null) {
			writer.writeAttribute("color", Utils.encode(fillColor));
		}
		if (fillColor2 != null) {
			writer.writeAttribute("color2", Utils.encode(fillColor2));
		}

		writer.writeAttribute("transparent", Boolean.toString(transparentFill));
	}

	void writeBorderStyle(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

        // y:BorderStyle
        writer.writeEmptyElement("y:BorderStyle");
        writer.writeAttribute("color", Utils.encode(borderColor));
        writer.writeAttribute("type", borderType.getValue());
        writer.writeAttribute("width", String.format("%.1f", width));
	}
}