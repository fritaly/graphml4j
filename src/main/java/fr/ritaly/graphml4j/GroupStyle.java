package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Alignment;
import fr.ritaly.graphml4j.base.FontStyle;
import fr.ritaly.graphml4j.base.LineType;
import fr.ritaly.graphml4j.base.Shape;

public class GroupStyle extends NodeStyle {

	private float insets = 15.0f;

	public GroupStyle() {
		// Apply the default values here
		setHeight(80.0f);
		setWidth(140.0f);
		setFillColor(Color.decode("#F5F5F5"));
		setTransparentFill(false);
		setBorderColor(Color.BLACK);
		setBorderType(LineType.DASHED);
		setBorderWidth(1.0f);
		setShape(Shape.ROUNDED_RECTANGLE);
		setFontFamily("Dialog");
		setFontSize(15);
		setFontStyle(FontStyle.PLAIN);
		setBackgroundColor(null);
		setLineColor(null);
		setTextAlignment(Alignment.CENTER);
		setTextColor(Color.BLACK);
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
	}

	public float getInsets() {
		return insets;
	}

	public void setInsets(float value) {
		Validate.isTrue(value >= 0, String.format("The given insets (%f) must be positive or zero", value));

		this.insets = value;
	}

	void writeInsets(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		writer.writeEmptyElement("y:BorderInsets");
		writer.writeAttribute("bottom", String.format("%.0f", insets));
		writer.writeAttribute("bottomF", String.format("%.1f", insets));
		writer.writeAttribute("left", String.format("%.0f", insets));
		writer.writeAttribute("leftF", String.format("%.1f", insets));
		writer.writeAttribute("right", String.format("%.0f", insets));
		writer.writeAttribute("rightF", String.format("%.1f", insets));
		writer.writeAttribute("top", String.format("%.0f", insets));
		writer.writeAttribute("topF", String.format("%.1f", insets));
	}
}