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

import com.github.fritaly.graphml4j.yed.Shape;
import com.github.fritaly.graphml4j.yed.*;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;


public class GroupStyle extends NodeStyle {

	@Getter
	private float insets = 15.0f;

	public GroupStyle() {
		// Apply the default values here
		setHeight(80.0f);
		setWidth(140.0f);
		setFillColor(Utils.decode("#F5F5F5"));
		setTransparentFill(false);
		setBorderColor(Color.BLACK);
		setBorderType(LineType.LINE);
		setBorderWidth(2.0f);
		setShape(Shape.ROUNDED_RECTANGLE);
		setFontFamily("Dialog");
		setFontSize(15);
		setFontStyle(FontStyle.BOLD);
		setBackgroundColor(null);
		setLineColor(null);
		setTextAlignment(Alignment.CENTER);
		setTextColor(Color.BLACK);
		setVisible(true);
		setSizePolicy(SizePolicy.NODE_WIDTH);
		setBackgroundColor(Utils.decode("#99CCFF"));
		setPlacement(Placement.INTERNAL);
		setPosition(Position.TOP);
		setUnderlinedText(false);

		// This distance is necessary because the group has rounded corners
		setBorderDistance(1.0f);
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

	public void setInsets(float value) {
		Validate.isTrue(value >= 0, String.format("The given insets (%f) must be positive or zero", value));

		this.insets = value;
	}

	private void writeInsets(XMLStreamWriter writer) throws XMLStreamException {
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

	private void writeState(XMLStreamWriter writer, boolean closed) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		writer.writeEmptyElement("y:State");
		writer.writeAttribute("closed", Boolean.toString(closed));
		writer.writeAttribute("closedHeight", String.format("%.1f", 50.0f)); // TODO Create property for closedHeight
		writer.writeAttribute("closedWidth", String.format("%.1f", 50.0f)); // TODO Create property for closedWidth

		// Infer the property innerGraphDisplayEnabled from the closed flag
		writer.writeAttribute("innerGraphDisplayEnabled", Boolean.toString(!closed));
	}

	void writeTo(XMLStreamWriter writer, String label, boolean closed, float x, float y) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// Write the node elements
		writeTo(writer, label, x, y);

		// ... and the group-specific ones
		writeState(writer, closed);
		writeInsets(writer);
	}
}