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
