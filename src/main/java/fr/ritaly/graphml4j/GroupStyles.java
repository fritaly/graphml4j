package fr.ritaly.graphml4j;

import java.awt.Color;

import org.apache.commons.lang.Validate;

import fr.ritaly.graphml4j.base.Alignment;
import fr.ritaly.graphml4j.base.FontStyle;
import fr.ritaly.graphml4j.base.LineType;
import fr.ritaly.graphml4j.base.Placement;
import fr.ritaly.graphml4j.base.Position;
import fr.ritaly.graphml4j.base.Shape;
import fr.ritaly.graphml4j.base.SizePolicy;

/**
 * The styles applied to a group node. A group can be in 2 states (open or
 * closed) and therefore defines 2 styles for each state.
 *
 * @author francois_ritaly
 */
public class GroupStyles {

	/**
	 * The style applied to open group nodes.
	 */
	private final GroupStyle openStyle = new GroupStyle();

	/**
	 * The style applied to closed group nodes.
	 */
	private final GroupStyle closedStyle = new GroupStyle();

	public GroupStyles() {
	}

	public GroupStyles(GroupStyles styles) {
		apply(styles);
	}

	void apply(GroupStyles styles) {
		Validate.notNull(styles, "The given group styles is null");

		this.openStyle.apply(styles.openStyle);
		this.closedStyle.apply(styles.closedStyle);
	}

	public GroupStyle getClosedStyle() {
		// Defensive recopy
		return new GroupStyle(closedStyle);
	}

	public GroupStyle getOpenStyle() {
		// Defensive recopy
		return new GroupStyle(openStyle);
	}

	public void setClosedStyle(GroupStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.closedStyle.apply(style);
	}

	public void setOpenStyle(GroupStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.openStyle.apply(style);
	}

	public void setShape(Shape shape) {
		closedStyle.setShape(shape);
		openStyle.setShape(shape);
	}

	public void setShadowColor(Color color) {
		closedStyle.setShadowColor(color);
		openStyle.setShadowColor(color);
	}

	public void setInsets(float value) {
		closedStyle.setInsets(value);
		openStyle.setInsets(value);
	}

	public void setShadowOffsetX(int value) {
		closedStyle.setShadowOffsetX(value);
		openStyle.setShadowOffsetX(value);
	}

	public void setShadowOffsetY(int value) {
		closedStyle.setShadowOffsetY(value);
		openStyle.setShadowOffsetY(value);
	}

	public void setHeight(float height) {
		closedStyle.setHeight(height);
		openStyle.setHeight(height);
	}

	public void setWidth(float width) {
		closedStyle.setWidth(width);
		openStyle.setWidth(width);
	}

	public void setFillColor(Color color) {
		closedStyle.setFillColor(color);
		openStyle.setFillColor(color);
	}

	public void setFillColor2(Color fillColor2) {
		closedStyle.setFillColor2(fillColor2);
		openStyle.setFillColor2(fillColor2);
	}

	public void setBorderColor(Color color) {
		closedStyle.setBorderColor(color);
		openStyle.setBorderColor(color);
	}

	public void setBorderType(LineType borderType) {
		closedStyle.setBorderType(borderType);
		openStyle.setBorderType(borderType);
	}

	public void setTransparentFill(boolean transparentFill) {
		closedStyle.setTransparentFill(transparentFill);
		openStyle.setTransparentFill(transparentFill);
	}

	public void setBorderWidth(float borderWidth) {
		closedStyle.setBorderWidth(borderWidth);
		openStyle.setBorderWidth(borderWidth);
	}

	public void setUnderlinedText(boolean value) {
		closedStyle.setUnderlinedText(value);
		openStyle.setUnderlinedText(value);
	}

	public void setVisible(boolean visible) {
		closedStyle.setVisible(visible);
		openStyle.setVisible(visible);
	}

	public void setTextColor(Color color) {
		closedStyle.setTextColor(color);
		openStyle.setTextColor(color);
	}

	public void setFontSize(int fontSize) {
		closedStyle.setFontSize(fontSize);
		openStyle.setFontSize(fontSize);
	}

	public void setFontFamily(String fontFamily) {
		closedStyle.setFontFamily(fontFamily);
		openStyle.setFontFamily(fontFamily);
	}

	public void setTextAlignment(Alignment textAlignment) {
		closedStyle.setTextAlignment(textAlignment);
		openStyle.setTextAlignment(textAlignment);
	}

	public void setFontStyle(FontStyle fontStyle) {
		closedStyle.setFontStyle(fontStyle);
		openStyle.setFontStyle(fontStyle);
	}

	public void setBackgroundColor(Color backgroundColor) {
		closedStyle.setBackgroundColor(backgroundColor);
		openStyle.setBackgroundColor(backgroundColor);
	}

	public void setLineColor(Color lineColor) {
		closedStyle.setLineColor(lineColor);
		openStyle.setLineColor(lineColor);
	}

	public void setPlacement(Placement placement) {
		closedStyle.setPlacement(placement);
		openStyle.setPlacement(placement);
	}

	public void setPosition(Position position) {
		closedStyle.setPosition(position);
		openStyle.setPosition(position);
	}

	public void setBottomInset(int bottomInset) {
		closedStyle.setBottomInset(bottomInset);
		openStyle.setBottomInset(bottomInset);
	}

	public void setLeftInset(int leftInset) {
		closedStyle.setLeftInset(leftInset);
		openStyle.setLeftInset(leftInset);
	}

	public void setRightInset(int rightInset) {
		closedStyle.setRightInset(rightInset);
		openStyle.setRightInset(rightInset);
	}

	public void setTopInset(int topInset) {
		closedStyle.setTopInset(topInset);
		openStyle.setTopInset(topInset);
	}

	public void setInsets(int value) {
		closedStyle.setInsets(value);
		openStyle.setInsets(value);
	}

	public void setSizePolicy(SizePolicy policy) {
		closedStyle.setSizePolicy(policy);
		openStyle.setSizePolicy(policy);
	}
}