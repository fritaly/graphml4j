package fr.ritaly.graphml4j;

import org.apache.commons.lang.Validate;

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
}