package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Arrow;
import fr.ritaly.graphml4j.base.LineType;

public final class EdgeStyle {

	private Color color = Color.BLACK;

	private LineType type = LineType.LINE;

	private float width = 1.0f;

	private Arrow sourceArrow = Arrow.NONE;

	private Arrow targetArrow = Arrow.STANDARD;

	private boolean smoothed = false;

	public EdgeStyle() {
	}

	public EdgeStyle(EdgeStyle style) {
		apply(style);
	}

	void apply(EdgeStyle style) {
		Validate.notNull(style, "The given style is null");

		this.color = style.color;
		this.type = style.type;
		this.width = style.width;
		this.sourceArrow = style.sourceArrow;
		this.targetArrow = style.targetArrow;
		this.smoothed = style.smoothed;
	}

	public boolean isSmoothed() {
		return smoothed;
	}

	public void setSmoothed(boolean smoothed) {
		this.smoothed = smoothed;
	}

	public Arrow getSourceArrow() {
		return sourceArrow;
	}

	public void setSourceArrow(Arrow arrow) {
		Validate.notNull(arrow, "The given arrow is null");

		this.sourceArrow = arrow;
	}

	public Arrow getTargetArrow() {
		return targetArrow;
	}

	public void setTargetArrow(Arrow arrow) {
		Validate.notNull(arrow, "The given arrow is null");

		this.targetArrow = arrow;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		Validate.notNull(color, "The given color is null");

		this.color = color;
	}

	public LineType getType() {
		return type;
	}

	public void setType(LineType type) {
		Validate.notNull(type, "The given line type is null");

		this.type = type;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		this.width = width;
	}

	void writeTo(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// What is the path used for ?
		// TODO Create properties for sx, sy, tx and ty

		// y:Path
		writer.writeEmptyElement("y:Path");
		writer.writeAttribute("sx", String.format("%.1f", 0));
		writer.writeAttribute("sy", String.format("%.1f", 0));
		writer.writeAttribute("tx", String.format("%.1f", 0));
		writer.writeAttribute("ty", String.format("%.1f", 0));

		// y:LineStyle
		writer.writeEmptyElement("y:LineStyle");
		writer.writeAttribute("color", Utils.encode(color));
		writer.writeAttribute("type", type.getValue());
		writer.writeAttribute("width", String.format("%.1f", width));

		// y:Arrows
		writer.writeEmptyElement("y:Arrows");
		writer.writeAttribute("source", sourceArrow.getValue());
		writer.writeAttribute("target", targetArrow.getValue());

		// y:BendStyle
		writer.writeEmptyElement("y:BendStyle");
		writer.writeAttribute("smoothed", Boolean.toString(smoothed));
	}
}