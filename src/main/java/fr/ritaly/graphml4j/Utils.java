package fr.ritaly.graphml4j;

import java.awt.Color;

import org.apache.commons.lang.Validate;

final class Utils {

	/**
	 * Encodes the given color into an hexadecimal string like "#RRGGBB".
	 *
	 * @param color
	 *            a color to encode. Can't be null.
	 * @return a string representing the encoded color.
	 */
	static String encode(Color color) {
		Validate.notNull(color, "The given color is null");

		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}
}
