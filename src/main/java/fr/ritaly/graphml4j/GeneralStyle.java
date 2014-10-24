/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.LineType;

/**
 * An object defining the style properties in the "General" tab in yEd.
 *
 * @author francois_ritaly
 */
final class GeneralStyle {

	// The properties x & y aren't defined here because they're not style
	// properties

	private float height = 40.0f;

	private float width = 40.0f;

	// This color is optional
	private Color fillColor = Utils.decode("#99CC00");

	// This color is optional
	private Color fillColor2;

	// This color is optional
	private Color borderColor = Color.BLACK;

	private LineType borderType = LineType.LINE;

	private float borderWidth = 2.0f;

	private boolean transparentFill = false;

	public GeneralStyle() {
	}

	public GeneralStyle(GeneralStyle style) {
		apply(style);
	}

	void apply(GeneralStyle style) {
		Validate.notNull(style, "The given style is null");

		this.borderColor = style.borderColor;
		this.borderType = style.borderType;
		this.borderWidth = style.borderWidth;
		this.fillColor = style.fillColor;
		this.fillColor2 = style.fillColor2;
		this.height = style.height;
		this.transparentFill = style.transparentFill;
		this.width = style.width;
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

	void writeTo(XMLStreamWriter writer, float x, float y) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		writeGeometry(writer, x, y);
		writeFill(writer);
		writeBorderStyle(writer);
	}

	private void writeGeometry(XMLStreamWriter writer, float x, float y) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:Geometry (the x & y attributes are computed when laying out the graph in yEd)
		writer.writeEmptyElement("y:Geometry");
		writer.writeAttribute("height", String.format("%.1f", height));
		writer.writeAttribute("width", String.format("%.1f", width));
		writer.writeAttribute("x", String.format("%.1f", x));
		writer.writeAttribute("y", String.format("%.1f", y));
	}

	private void writeFill(XMLStreamWriter writer) throws XMLStreamException {
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

	private void writeBorderStyle(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

        // y:BorderStyle
        writer.writeEmptyElement("y:BorderStyle");
        writer.writeAttribute("color", Utils.encode(borderColor));
        writer.writeAttribute("type", borderType.getValue());
        writer.writeAttribute("width", String.format("%.1f", borderWidth));
	}
}