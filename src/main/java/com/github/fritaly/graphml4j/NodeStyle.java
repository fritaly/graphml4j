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

import lombok.experimental.Delegate;
import org.apache.commons.lang.Validate;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class NodeStyle {

	@Delegate
	private final GeneralStyle generalStyle = new GeneralStyle();

	@Delegate
	private final ShapeStyle shapeStyle = new ShapeStyle();

	@Delegate
	private final LabelStyle labelStyle = new LabelStyle();

	public NodeStyle() {
	}

	public NodeStyle(NodeStyle style) {
		apply(style);
	}

	void apply(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Apply the label properties
		this.labelStyle.apply(style.labelStyle);

		// Apply the generic properties
		this.generalStyle.apply(style.generalStyle);

		// Apply the shape properties
		this.shapeStyle.apply(style.shapeStyle);
	}

	void writeTo(XMLStreamWriter writer, String label, float x, float y) throws XMLStreamException {
		generalStyle.writeTo(writer, x, y);
		labelStyle.writeTo(writer, label);
		shapeStyle.writeTo(writer);
	}
}