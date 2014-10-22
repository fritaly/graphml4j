package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Shape;

final class ShapeObject {

	private Shape shape = Shape.RECTANGLE;

	// This color is optional
	private Color shadowColor;

	private int shadowOffsetX, shadowOffsetY;

	public ShapeObject() {
	}

	public ShapeObject(ShapeObject object) {
		apply(object);
	}

	void apply(ShapeObject object) {
		Validate.notNull(object, "The given shape object is null");

		this.shape = object.shape;
		this.shadowColor = object.shadowColor;
		this.shadowOffsetX = object.shadowOffsetX;
		this.shadowOffsetY = object.shadowOffsetY;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		Validate.notNull(shape, "The given shape is null");

		this.shape = shape;
	}

	public Color getShadowColor() {
		return shadowColor;
	}

	public int getShadowOffsetX() {
		return shadowOffsetX;
	}

	public int getShadowOffsetY() {
		return shadowOffsetY;
	}

	public void setShadowColor(Color color) {
		// This color is optional
		this.shadowColor = color;
	}

	public void setShadowOffsetX(int value) {
		this.shadowOffsetX = value;
	}

	public void setShadowOffsetY(int value) {
		this.shadowOffsetY = value;
	}

	void writeShape(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:Shape
		writer.writeEmptyElement("y:Shape");
		writer.writeAttribute("type", shape.getValue());
	}

	void writeDropShadow(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		if ((shadowColor == null) || ((shadowOffsetX == 0) && (shadowOffsetY == 0))) {
			// Nothing to render
			return;
		}

		// y:DropShadow
		writer.writeEmptyElement("y:DropShadow");
		writer.writeAttribute("color", Utils.encode(shadowColor));
		writer.writeAttribute("offsetX", Integer.toString(shadowOffsetX));
		writer.writeAttribute("offsetY", Integer.toString(shadowOffsetY));
	}
}
