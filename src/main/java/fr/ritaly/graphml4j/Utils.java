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

import org.apache.commons.lang.Validate;

final class Utils {

	/**
	 * Encodes the given color into an hexadecimal string like "#RRGGBBAA" or
	 * "#RRGGBB" (whether the transparency is set to 0).
	 *
	 * @param color
	 *            a color to encode. Can't be null.
	 * @return a string representing the encoded color.
	 */
	static String encode(Color color) {
		Validate.notNull(color, "The given color is null");

		if (color.getAlpha() == 255) {
			return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
		} else {
			return String.format("#%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		}
	}

	/**
	 * Decodes the given hexadecimal string (like "#RRGGBBAA" or "#RRGGBB"
	 * (whether the transparency is set to 0)) into an instance of {@link Color}
	 * .
	 *
	 * @param value
	 *            a string representing the encoded color.Can't be null.
	 * @return a {@link Color}.
	 */
	static Color decode(String value) {
		Validate.notNull(value, "The given encoded value is null");

		if (value.length() == 9) {
	        final int i = Integer.decode(value).intValue();

	        return new Color((i >> 24) & 0xFF, (i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
		}

		return Color.decode(value);
	}
}
