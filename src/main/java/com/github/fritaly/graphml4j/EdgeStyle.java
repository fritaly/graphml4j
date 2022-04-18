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

import com.github.fritaly.graphml4j.yed.Arrow;
import com.github.fritaly.graphml4j.yed.LineType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.apache.commons.lang.Validate;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;


/**
 * Defines the style applied to edges.
 *
 * @author francois_ritaly
 */
@Value
@Builder(builderClassName = "Builder")
@With
public class EdgeStyle {

	public static final EdgeStyle DEFAULT = EdgeStyle.builder().build();

	/**
	 * <p>The color for rendering the edge. Never <code>null</code>.</p>
	 */
	@NonNull
	@lombok.Builder.Default
	private Color color = Color.BLACK;

	/**
	 * <p>The type of line for rendering the edge. Never <code>null</code>.</p>
	 */
	@NonNull
	@lombok.Builder.Default
	private LineType type = LineType.LINE;

	/**
	 * <p>The width of the edge as a float.</p>
	 */
	@lombok.Builder.Default
	private float width = 1.0f;

	/**
	 * <p>The type of arrow for rendering the start of the edge. Never <code>null</code>.</p>
	 */
	@NonNull
	@lombok.Builder.Default
	private Arrow sourceArrow = Arrow.NONE;

	/**
	 * <p>The type of arrow for rendering the target of the edge. Never <code>null</code>.</p>
	 */
	@NonNull
	@lombok.Builder.Default
	private Arrow targetArrow = Arrow.STANDARD;

	/**
	 * <p>Whether the edge should be smoothed.</p>
	 */
	@lombok.Builder.Default
	private boolean smoothed = false;

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