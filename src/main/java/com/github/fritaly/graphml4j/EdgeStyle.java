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

import com.github.fritaly.graphml4j.yed.Arrow;
import com.github.fritaly.graphml4j.yed.LineType;


/**
 * Defines the style applied to edges.
 *
 * @author francois_ritaly
 */
public final class EdgeStyle {

	private Color color = Color.BLACK;

	private LineType type = LineType.LINE;

	private float width = 1.0f;

	private Arrow sourceArrow = Arrow.NONE;

	private Arrow targetArrow = Arrow.STANDARD;

	private boolean smoothed = false;

	/**
	 * Creates a new default edge style.
	 */
	public EdgeStyle() {
	}

	/**
	 * Creates a new edge style from the given one.
	 *
	 * @param style an edge style to copy. Can't be null.
	 */
	public EdgeStyle(EdgeStyle style) {
		apply(style);
	}

	/**
	 * Applies the given edge style to this style.
	 *
	 * @param style an edge style. Can't be null.
	 */
	void apply(EdgeStyle style) {
		Validate.notNull(style, "The given style is null");

		this.color = style.color;
		this.type = style.type;
		this.width = style.width;
		this.sourceArrow = style.sourceArrow;
		this.targetArrow = style.targetArrow;
		this.smoothed = style.smoothed;
	}

	/**
	 * Returns whether the edge should be smoothed.
	 *
	 * @return whether the edge should be smoothed.
	 */
	public boolean isSmoothed() {
		return smoothed;
	}

	/**
	 * Sets whether the edge should be smoothed.
	 *
	 * @param smoothed whether the edge should be smoothed.
	 */
	public void setSmoothed(boolean smoothed) {
		this.smoothed = smoothed;
	}

	/**
	 * Returns the type of arrow for rendering the start of the edge.
	 *
	 * @return an instance of arrow. Never returns null.
	 */
	public Arrow getSourceArrow() {
		return sourceArrow;
	}

	/**
	 * Sets the type of arrow for rendering the start of the edge.
	 *
	 * @param arrow an instance of arrow. Can't be null.
	 */
	public void setSourceArrow(Arrow arrow) {
		Validate.notNull(arrow, "The given arrow is null");

		this.sourceArrow = arrow;
	}

	/**
	 * Returns the type of arrow for rendering the end of the edge.
	 *
	 * @return an instance of arrow. Never returns null.
	 */
	public Arrow getTargetArrow() {
		return targetArrow;
	}

	/**
	 * Sets the type of arrow for rendering the end of the edge.
	 *
	 * @param arrow an instance of arrow. Can't be null.
	 */
	public void setTargetArrow(Arrow arrow) {
		Validate.notNull(arrow, "The given arrow is null");

		this.targetArrow = arrow;
	}

	/**
	 * Returns the color for rendering the edge.
	 *
	 * @return a {@link Color}. Never returns null.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color for rendering the edge.
	 *
	 * @param color a {@link Color}. Can't be null.
	 */
	public void setColor(Color color) {
		Validate.notNull(color, "The given color is null");

		this.color = color;
	}

	/**
	 * Returns the type of line for rendering the edge.
	 *
	 * @return an instance of {@link LineType}. Never returns null.
	 */
	public LineType getType() {
		return type;
	}

	/**
	 * Sets the type of line for rendering the edge.
	 *
	 * @param type an instance of {@link LineType}. Can't be null.
	 */
	public void setType(LineType type) {
		Validate.notNull(type, "The given line type is null");

		this.type = type;
	}

	/**
	 * Returns the width of the edge as a float.
	 *
	 * @return a float representing the edge width.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the width of the edge.
	 *
	 * @param width a (positive) float representing the edge width.
	 */
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
		writer.writeAttribute("sx", String.format("%.1f", 0.0f));
		writer.writeAttribute("sy", String.format("%.1f", 0.0f));
		writer.writeAttribute("tx", String.format("%.1f", 0.0f));
		writer.writeAttribute("ty", String.format("%.1f", 0.0f));

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