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

import com.github.fritaly.graphml4j.yed.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.Validate;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;


/**
 * An object defining the style properties in the "Label" tab in yEd.
 *
 * @author francois_ritaly
 */
final class LabelStyle {

	@Getter
	@Setter
	private boolean visible = true;

	@Getter
	@Setter
	@NonNull
	private Color textColor = Color.BLACK;

	/**
	 * <p>This color is optional. Can be <code>null</code>.</p>
	 */
	@Getter
	@Setter
	private Color backgroundColor;

	/**
	 * <p>This color is optional. Can be <code>null</code>.</p>
	 */
	@Getter
	@Setter
	private Color lineColor;

	@Getter
	@Setter
	@NonNull
	private Alignment textAlignment = Alignment.CENTER;

	@Getter
	@Setter
	@NonNull
	private FontStyle fontStyle = FontStyle.PLAIN;

	@Getter
	@Setter
	@NonNull
	private String fontFamily = "Dialog";

	@Getter // + custom setter
	private int fontSize = 12;

	@Getter
	@Setter
	private float borderDistance = 0.0f;

	@Getter
	@Setter
	private float rotationAngle = 0.0f;

	@Getter
	@Setter
	private boolean underlinedText = false;

	@Getter // + custom setter
	private Placement placement = Placement.INTERNAL;

	@Getter // + custom setter
	private Position position = Position.CENTER;

	@Getter
	@Setter
	private int leftInset, rightInset, topInset, bottomInset;

	@Getter
	@Setter
	@NonNull
	private SizePolicy sizePolicy = SizePolicy.CONTENT;

	public LabelStyle() {
	}

	public LabelStyle(LabelStyle style) {
		apply(style);
	}

	void apply(LabelStyle style) {
		Validate.notNull(style, "The given label style is null");

		this.visible = style.visible;
		this.textColor = style.textColor;
		this.textAlignment = style.textAlignment;
		this.fontStyle = style.fontStyle;
		this.fontFamily = style.fontFamily;
		this.fontSize = style.fontSize;
		this.underlinedText = style.underlinedText;
		this.backgroundColor = style.backgroundColor;
		this.lineColor = style.lineColor;
		this.placement = style.placement;
		this.position = style.position;
		this.leftInset = style.leftInset;
		this.rightInset = style.rightInset;
		this.topInset = style.topInset;
		this.bottomInset = style.bottomInset;
		this.sizePolicy = style.sizePolicy;
		this.borderDistance = style.borderDistance;
		this.rotationAngle = style.rotationAngle;
	}

	public boolean hasInsets() {
		return (topInset != 0) || (bottomInset != 0) || (leftInset != 0) || (rightInset != 0);
	}

	public void setInsets(int value) {
		setLeftInset(value);
		setRightInset(value);
		setTopInset(value);
		setBottomInset(value);
	}

	public void setPlacement(Placement placement) {
		if (placement == null) {
			this.placement = Placement.INTERNAL;
		} else {
			this.placement = placement;
		}
	}

	public void setPosition(Position position) {
		if (position == null) {
			this.position = Position.CENTER;
		} else {
			this.position = position;
		}
	}

	public void setFontSize(int fontSize) {
		Validate.isTrue(fontSize > 0, String.format("The given font size (%d) must be positive", fontSize));

		this.fontSize = fontSize;
	}

	void writeTo(XMLStreamWriter writer, String label) throws XMLStreamException {
		Validate.notNull(writer, "The given stream writer is null");

		// y:NodeLabel
		writer.writeStartElement("y:NodeLabel");
		writer.writeAttribute("alignement", textAlignment.getValue());
		writer.writeAttribute("autoSizePolicy", sizePolicy.getValue());
		writer.writeAttribute("fontFamily", fontFamily);
		writer.writeAttribute("fontSize", Integer.toString(fontSize));
		writer.writeAttribute("fontStyle", fontStyle.getValue());
		writer.writeAttribute("modelName", placement.getValue());
		writer.writeAttribute("modelPosition", position.getValue());

		if (borderDistance != 0.0f) {
			writer.writeAttribute("borderDistance", String.format("%.1f", borderDistance));
		}
		if (rotationAngle != 0.0f) {
			writer.writeAttribute("rotationAngle", String.format("%.1f", rotationAngle));
		}

		if (backgroundColor != null) {
			writer.writeAttribute("backgroundColor", Utils.encode(backgroundColor));
		} else {
			writer.writeAttribute("hasBackgroundColor", "false");
		}
		if (lineColor != null) {
			writer.writeAttribute("lineColor", Utils.encode(lineColor));
		} else {
			writer.writeAttribute("hasLineColor", "false");
		}
		if (hasInsets()) {
			writer.writeAttribute("bottomInset", Integer.toString(bottomInset));
			writer.writeAttribute("topInset", Integer.toString(topInset));
			writer.writeAttribute("leftInset", Integer.toString(leftInset));
			writer.writeAttribute("rightInset", Integer.toString(rightInset));
		}

		writer.writeAttribute("textColor", Utils.encode(textColor));
		writer.writeAttribute("visible", Boolean.toString(visible));

		if (underlinedText) {
			writer.writeAttribute("underlinedText", Boolean.toString(underlinedText));
		}

		writer.writeCharacters(label);
		writer.writeEndElement(); // </y:NodeLabel>
	}
}
