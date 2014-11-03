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
package com.github.fritaly.graphml4j;

import java.awt.Color;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

import com.github.fritaly.graphml4j.base.Shape;


/**
 * An object defining the style properties in the "Shape" tab in yEd.
 *
 * @author francois_ritaly
 */
final class ShapeStyle {

	private Shape shape = Shape.ROUNDED_RECTANGLE;

	// This color is optional
	private Color shadowColor = Utils.decode("#B3A691");

	private int shadowOffsetX = 3, shadowOffsetY = 3;

	public ShapeStyle() {
	}

	public ShapeStyle(ShapeStyle style) {
		apply(style);
	}

	void apply(ShapeStyle object) {
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

	void writeTo(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		writeShape(writer);
		writeDropShadow(writer);
	}

	private void writeShape(XMLStreamWriter writer) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:Shape
		writer.writeEmptyElement("y:Shape");
		writer.writeAttribute("type", shape.getValue());
	}

	private void writeDropShadow(XMLStreamWriter writer) throws XMLStreamException {
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
