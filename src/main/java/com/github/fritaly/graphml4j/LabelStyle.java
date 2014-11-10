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

import com.github.fritaly.graphml4j.yed.Alignment;
import com.github.fritaly.graphml4j.yed.FontStyle;
import com.github.fritaly.graphml4j.yed.Placement;
import com.github.fritaly.graphml4j.yed.Position;
import com.github.fritaly.graphml4j.yed.SizePolicy;


/**
 * An object defining the style properties in the "Label" tab in yEd.
 *
 * @author francois_ritaly
 */
final class LabelStyle {

	private boolean visible = true;

	private Color textColor = Color.BLACK;

	// This color is optional
	private Color backgroundColor = null;

	// This color is optional
	private Color lineColor = null;

	private Alignment textAlignment = Alignment.CENTER;

	private FontStyle fontStyle = FontStyle.PLAIN;

	private String fontFamily = "Dialog";

	private int fontSize = 12;

	private float borderDistance = 0.0f;

	private float rotationAngle = 0.0f;

	private boolean underlinedText = false;

	private Placement placement = Placement.INTERNAL;

	private Position position = Position.CENTER;

	private int leftInset, rightInset, topInset, bottomInset;

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

	public float getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public float getBorderDistance() {
		return borderDistance;
	}

	public void setBorderDistance(float borderDistance) {
		this.borderDistance = borderDistance;
	}

	public SizePolicy getSizePolicy() {
		return sizePolicy;
	}

	public void setSizePolicy(SizePolicy policy) {
		Validate.notNull(policy, "The given size policy is null");

		this.sizePolicy = policy;
	}

	public int getBottomInset() {
		return bottomInset;
	}

	public int getLeftInset() {
		return leftInset;
	}

	public int getRightInset() {
		return rightInset;
	}

	public int getTopInset() {
		return topInset;
	}

	public void setBottomInset(int bottomInset) {
		this.bottomInset = bottomInset;
	}

	public void setLeftInset(int leftInset) {
		this.leftInset = leftInset;
	}

	public void setRightInset(int rightInset) {
		this.rightInset = rightInset;
	}

	public void setTopInset(int topInset) {
		this.topInset = topInset;
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

	public Placement getPlacement() {
		return placement;
	}

	public Position getPosition() {
		return position;
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

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		// This color is optional
		this.lineColor = lineColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		// This color is optional
		this.backgroundColor = backgroundColor;
	}

	public boolean isUnderlinedText() {
		return underlinedText;
	}

	public void setUnderlinedText(boolean value) {
		this.underlinedText = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color color) {
		Validate.notNull(color, "The given text color is null");

		this.textColor = color;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		Validate.isTrue(fontSize > 0, String.format("The given font size (%d) must be positive", fontSize));

		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		Validate.notNull(fontFamily, "The given font family is null");

		this.fontFamily = fontFamily;
	}

	public Alignment getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(Alignment textAlignment) {
		Validate.notNull(textAlignment, "The given text alignment is null");

		this.textAlignment = textAlignment;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle fontStyle) {
		Validate.notNull(fontStyle, "The given font style is null");

		this.fontStyle = fontStyle;
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
