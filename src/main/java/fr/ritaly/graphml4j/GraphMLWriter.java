package fr.ritaly.graphml4j;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

public final class GraphMLWriter {

	/** The id associated to the node URL property */
	private static final String ID_NODE_URL = "d4";

	/** The id associated to the node description property */
	private static final String ID_NODE_DESCRIPTION = "d5";

	/** The id associated to the node graphics property */
	private static final String ID_NODE_GRAPHICS = "d6";

	/** The id associated to the graph description property */
	private static final String ID_GRAPH_DESCRIPTION = "d7";

	/** The id associated to the edge URL property */
	private static final String ID_EDGE_URL = "d8";

	/** The id associated to the edge description property */
	private static final String ID_EDGE_DESCRIPTION = "d9";

	/** The id associated to the edge graphics property */
	private static final String ID_EDGE_GRAPHICS = "d10";

	/**
	 * Enumeration of possible writer states.
	 *
	 * @author francois_ritaly
	 */
	private static enum State {
		INITIAL, DOCUMENT_OPENED, GRAPH_OPENED, GRAPH_CLOSED, DOCUMENT_CLOSED, CLOSED;

		/**
		 * Tells whether the transition from this state to the target one is allowed.
		 *
		 * @param target a state representing the target of the transition.
		 * @return whether the transition from this state to the target one is allowed.
		 */
		boolean isTransitionAllowed(State target) {
			switch(this) {
			case INITIAL:
				return EnumSet.of(DOCUMENT_OPENED, CLOSED).contains(target);
			case DOCUMENT_OPENED:
				return EnumSet.of(GRAPH_OPENED, DOCUMENT_CLOSED, CLOSED).contains(target);
			case GRAPH_OPENED:
				return EnumSet.of(GRAPH_CLOSED, CLOSED).contains(target);
			case GRAPH_CLOSED:
				return EnumSet.of(DOCUMENT_CLOSED, CLOSED).contains(target);
			case DOCUMENT_CLOSED:
				return CLOSED.equals(target);
			case CLOSED:
				return false;
			default:
				throw new UnsupportedOperationException("Unsupported state: " + this);
			}
		}
	}

	private final Writer writer;

	private final XMLStreamWriter streamWriter;

	/**
	 * The writer's current state. Use for validating the sequence of method
	 * calls. All state changes must be done by method {@link #setState(State)}.
	 */
	private State state = State.INITIAL;

	/**
	 * Sequence used for generating node identifiers.
	 */
	private final AtomicInteger nodeSequence = new AtomicInteger();

	/**
	 * Sequence used for generating edge identifiers.
	 */
	private final AtomicInteger edgeSequence = new AtomicInteger();

	/**
	 * Set containing the identifiers of nodes added to the graph.
	 */
	private final Set<String> nodeIds = new TreeSet<String>();

	/**
	 * Stack containing the identifiers of groups.
	 */
	private final Stack<String> groupIds = new Stack<String>();

	/**
	 * The style applied to nodes.
	 */
	private final NodeStyle nodeStyle = new NodeStyle();

	/**
	 * The style applied to edges.
	 */
	private final EdgeStyle edgeStyle = new EdgeStyle();

	/**
	 * The style applied to open group nodes.
	 */
	private final GroupStyle openGroupStyle = new GroupStyle();

	/**
	 * The style applied to closed group nodes.
	 */
	private final GroupStyle closedGroupStyle = new GroupStyle();

	public GraphMLWriter(Writer writer) throws GraphMLException {
		Validate.notNull(writer, "The given writer is null");

		try {
			this.writer = writer;

			// Indent the XML generated
			this.streamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(writer));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		} catch (FactoryConfigurationError e) {
			throw new GraphMLException(e);
		}
	}

	/**
	 * Encodes the given color into an hexadecimal string like "#RRGGBB".
	 *
	 * @param color
	 *            a color to encode. Can't be null.
	 * @return a string representing the encoded color.
	 */
	private static String encode(Color color) {
		Validate.notNull(color, "The given color is null");

		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	public GroupStyle getClosedGroupStyle() {
		// Defensive recopy
		return new GroupStyle(closedGroupStyle);
	}

	public void setClosedGroupStyle(GroupStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.closedGroupStyle.apply(style);
	}

	public GroupStyle getOpenGroupStyle() {
		// Defensive recopy
		return new GroupStyle(openGroupStyle);
	}

	public void setOpenGroupStyle(GroupStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.openGroupStyle.apply(style);
	}

	public NodeStyle getNodeStyle() {
		// Defensive recopy
		return new NodeStyle(nodeStyle);
	}

	public void setNodeStyle(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.nodeStyle.apply(style);
	}

	public EdgeStyle getEdgeStyle() {
		// Defensive recopy
		return new EdgeStyle(edgeStyle);
	}

	public void setEdgeStyle(EdgeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.edgeStyle.apply(style);
	}

	/**
	 * Returns the current level of depth. When inside the parent graph, the
	 * depth is 0. For a subgraph, the depth is 1. For a sub-subgraph, the depth
	 * is 2 and so on.
	 *
	 * @return an int denoting a level of depth inside the graph.
	 */
	private int getDepth() {
		return groupIds.size();
	}

	private boolean insideGroup() {
		return (getDepth() > 0);
	}

	private void setState(State newState) {
		Validate.notNull(newState, "The given new state is null");

		if (!this.state.isTransitionAllowed(newState)) {
			throw new IllegalArgumentException(String.format("Transition from state '%s' to '%s' is forbidden", state.name(),
					newState.name()));
		}

		this.state = newState;
	}

	private void startDocument() throws GraphMLException {
		assertState(State.INITIAL);

		try {
			this.streamWriter.writeStartDocument("1.0");

			// Write the <graphml> root tag and the XML namespaces
			this.streamWriter.writeStartElement("graphml");
			this.streamWriter.writeDefaultNamespace("http://graphml.graphdrawing.org/xmlns");
			this.streamWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			this.streamWriter.writeNamespace("y", "http://www.yworks.com/xml/graphml");
			this.streamWriter.writeNamespace("yed", "http://www.yworks.com/xml/yed/3");
			this.streamWriter.writeAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd");

			// Add the yEd-specific metadata
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "graphml");
			this.streamWriter.writeAttribute("id", "d0");
			this.streamWriter.writeAttribute("yfiles.type", "resources");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d1");
			this.streamWriter.writeAttribute("yfiles.type", "portgraphics");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d2");
			this.streamWriter.writeAttribute("yfiles.type", "portgeometry");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d3");
			this.streamWriter.writeAttribute("yfiles.type", "portuserdata");

			// Define the attributes for type 'node'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "url");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", ID_NODE_URL);

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", ID_NODE_DESCRIPTION);

			// Define the type 'node'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", ID_NODE_GRAPHICS);
			this.streamWriter.writeAttribute("yfiles.type", "nodegraphics");

			// Define the attributes for type 'graph'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "Description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "graph");
			this.streamWriter.writeAttribute("id", ID_GRAPH_DESCRIPTION);

			// Define the attributes for type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "url");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", ID_EDGE_URL);

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", ID_EDGE_DESCRIPTION);

			// Define the type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", ID_EDGE_GRAPHICS);
			this.streamWriter.writeAttribute("yfiles.type", "edgegraphics");

			setState(State.DOCUMENT_OPENED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void endDocument() throws GraphMLException {
		assertState(State.GRAPH_CLOSED);

		try {
			this.streamWriter.writeEndElement(); // </graphml>
			this.streamWriter.writeEndDocument();

			setState(State.DOCUMENT_CLOSED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Graph --- //

	public void graph() throws GraphMLException {
		startDocument();

		assertState(State.DOCUMENT_OPENED);

		try {
			// TODO Introduce parameters for the 2 attributes
			this.streamWriter.writeStartElement("graph");
			this.streamWriter.writeAttribute("edgedefault", "directed");
			this.streamWriter.writeAttribute("id", "G");

			// TODO Generate the <data key="d7"/>

			setState(State.GRAPH_OPENED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// This method can only be called when not inside a group
	public void closeGraph() throws GraphMLException {
		assertState(State.GRAPH_OPENED);
		if (insideGroup()) {
			throw new IllegalStateException("The writer is inside a group. Close the group(s) first");
		}

		try {
			this.streamWriter.writeEndElement(); // </graph>

			setState(State.GRAPH_CLOSED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}

		endDocument();
	}

	// --- Internal helper methods --- //

	private void writeGeometry(float height, float width) throws GraphMLException {
		Validate.isTrue(height > 0, String.format("The given height (%f) must be positive", height));
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		try {
			// y:Geometry (the x & y attributes are computed when laying out the graph in yEd)
			this.streamWriter.writeEmptyElement("y:Geometry");
			this.streamWriter.writeAttribute("height", String.format("%.1f", height));
			this.streamWriter.writeAttribute("width", String.format("%.1f", width));
			this.streamWriter.writeAttribute("x", "0.0");
			this.streamWriter.writeAttribute("y", "0.0");
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeFill(Color color, Color color2, boolean transparent) throws GraphMLException {
		// The colors are optional
		Validate.notNull(color, "The given color is null");

		try {
			// y:Fill
			this.streamWriter.writeEmptyElement("y:Fill");

			if (color != null) {
				this.streamWriter.writeAttribute("color", encode(color));
			}
			if (color2 != null) {
				this.streamWriter.writeAttribute("color2", encode(color2));
			}

			this.streamWriter.writeAttribute("transparent", Boolean.toString(transparent));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeBorderStyle(Color color, LineType type, float width) throws GraphMLException {
		Validate.notNull(color, "The given color is null");
		Validate.notNull(type, "The given line type is null");
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		try {
            // y:BorderStyle
            this.streamWriter.writeEmptyElement("y:BorderStyle");
            this.streamWriter.writeAttribute("color", encode(color));
            this.streamWriter.writeAttribute("type", type.getValue());
            this.streamWriter.writeAttribute("width", String.format("%.1f", width));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeShape(Shape shape) throws GraphMLException {
		Validate.notNull(shape, "The given shape is null");

		try {
			// y:Shape
			this.streamWriter.writeEmptyElement("y:Shape");
			this.streamWriter.writeAttribute("type", shape.getValue());
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writePath(float sx, float sy, float tx, float ty) throws GraphMLException {
		try {
			// y:Path
			this.streamWriter.writeEmptyElement("y:Path");
			this.streamWriter.writeAttribute("sx", String.format("%.1f", sx));
			this.streamWriter.writeAttribute("sy", String.format("%.1f", sy));
			this.streamWriter.writeAttribute("tx", String.format("%.1f", tx));
			this.streamWriter.writeAttribute("ty", String.format("%.1f", ty));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeLineStyle(Color color, LineType type, float width) throws GraphMLException {
		Validate.notNull(color, "The given color is null");
		Validate.notNull(type, "The given line type is null");
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		try {
            // y:LineStyle
            this.streamWriter.writeEmptyElement("y:LineStyle");
            this.streamWriter.writeAttribute("color", encode(color));
            this.streamWriter.writeAttribute("type", type.getValue());
            this.streamWriter.writeAttribute("width", String.format("%.1f", width));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeArrows(Arrow source, Arrow target) throws GraphMLException {
		Validate.notNull(source, "The given source arrow is null");
		Validate.notNull(target, "The given target arrow is null");

		try {
			// y:Arrows
			this.streamWriter.writeEmptyElement("y:Arrows");
			this.streamWriter.writeAttribute("source", source.getValue());
			this.streamWriter.writeAttribute("target", target.getValue());
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeBendStyle(boolean smoothed) throws GraphMLException {
		try {
			// y:BendStyle
			this.streamWriter.writeEmptyElement("y:BendStyle");
			this.streamWriter.writeAttribute("smoothed", Boolean.toString(smoothed));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeDropShadow(Color color, int offsetX, int offsetY) throws GraphMLException {
		try {
			if ((color == null) || ((offsetX == 0) && (offsetY == 0))) {
				// Nothing to render
				return;
			}

			// y:DropShadow
			this.streamWriter.writeEmptyElement("y:DropShadow");
			this.streamWriter.writeAttribute("color", encode(color));
			this.streamWriter.writeAttribute("offsetX", Integer.toString(offsetX));
			this.streamWriter.writeAttribute("offsetY", Integer.toString(offsetY));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeBorderInsets(float value) throws GraphMLException {
		writeBorderInsets(value, value, value, value);
	}

	private void writeBorderInsets(float bottom, float left, float top, float right) throws GraphMLException {
		try {
			this.streamWriter.writeEmptyElement("y:BorderInsets");
			this.streamWriter.writeAttribute("bottom", String.format("%.0f", bottom));
			this.streamWriter.writeAttribute("bottomF", String.format("%.1f", bottom));
			this.streamWriter.writeAttribute("left", String.format("%.0f", left));
			this.streamWriter.writeAttribute("leftF", String.format("%.1f", left));
			this.streamWriter.writeAttribute("right", String.format("%.0f", right));
			this.streamWriter.writeAttribute("rightF", String.format("%.1f", right));
			this.streamWriter.writeAttribute("top", String.format("%.0f", top));
			this.streamWriter.writeAttribute("topF", String.format("%.1f", top));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeInsets(float value) throws GraphMLException {
		writeInsets(value, value, value, value);
	}

	private void writeInsets(float bottom, float left, float top, float right) throws GraphMLException {
		try {
			this.streamWriter.writeEmptyElement("y:Insets");
			this.streamWriter.writeAttribute("bottom", String.format("%.0f", bottom));
			this.streamWriter.writeAttribute("bottomF", String.format("%.1f", bottom));
			this.streamWriter.writeAttribute("left", String.format("%.0f", left));
			this.streamWriter.writeAttribute("leftF", String.format("%.1f", left));
			this.streamWriter.writeAttribute("right", String.format("%.0f", right));
			this.streamWriter.writeAttribute("rightF", String.format("%.1f", right));
			this.streamWriter.writeAttribute("top", String.format("%.0f", top));
			this.streamWriter.writeAttribute("topF", String.format("%.1f", top));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeState(boolean closed, float height, float width, boolean innerGraphDisplayEnabled) throws GraphMLException {
		try {
			this.streamWriter.writeEmptyElement("y:State");
			this.streamWriter.writeAttribute("closed", Boolean.toString(closed));
			this.streamWriter.writeAttribute("closedHeight", String.format("%.1f", height));
			this.streamWriter.writeAttribute("closedWidth", String.format("%.1f", width));
			this.streamWriter.writeAttribute("innerGraphDisplayEnabled", Boolean.toString(innerGraphDisplayEnabled));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeNodeLabel(String label) throws GraphMLException {
		Validate.notNull(label, "The given label is null");

		try {
			// y:NodeLabel
			this.streamWriter.writeStartElement("y:NodeLabel");
			this.streamWriter.writeAttribute("alignement", nodeStyle.getTextAlignment().getValue());
			this.streamWriter.writeAttribute("fontFamily", nodeStyle.getFontFamily());
			this.streamWriter.writeAttribute("fontSize", Integer.toString(nodeStyle.getFontSize()));
			this.streamWriter.writeAttribute("fontStyle", nodeStyle.getFontStyle().getValue());
			this.streamWriter.writeAttribute("hasBackgroundColor", Boolean.toString(nodeStyle.isHasBackgroundColor()));
			this.streamWriter.writeAttribute("hasLineColor", Boolean.toString(nodeStyle.isHasLineColor()));
			this.streamWriter.writeAttribute("textColor", encode(nodeStyle.getTextColor()));
			this.streamWriter.writeAttribute("visible", Boolean.toString(nodeStyle.isVisible()));

			if (nodeStyle.isUnderlinedText()) {
				this.streamWriter.writeAttribute("underlinedText", Boolean.toString(nodeStyle.isUnderlinedText()));
			}
			if (nodeStyle.hasInsets()) {
				this.streamWriter.writeAttribute("bottomInset", Integer.toString(nodeStyle.getBottomInset()));
				this.streamWriter.writeAttribute("topInset", Integer.toString(nodeStyle.getTopInset()));
				this.streamWriter.writeAttribute("leftInset", Integer.toString(nodeStyle.getLeftInset()));
				this.streamWriter.writeAttribute("rightInset", Integer.toString(nodeStyle.getRightInset()));
			}

			this.streamWriter.writeCharacters(label);
			this.streamWriter.writeEndElement(); // </y:NodeLabel>
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeNodeLabel_Group(String label, Alignment alignment, FontStyle fontStyle) throws GraphMLException {
		Validate.notNull(label, "The given label is null");
		Validate.notNull(alignment, "The given alignment is null");
		Validate.notNull(fontStyle, "The given font style is null");

		try {
			// y:NodeLabel
			this.streamWriter.writeStartElement("y:NodeLabel");
			this.streamWriter.writeAttribute("alignement", alignment.getValue());
			this.streamWriter.writeAttribute("autoSizePolicy", "node_width");
			this.streamWriter.writeAttribute("backgroundColor", "#EBEBEB");
			this.streamWriter.writeAttribute("borderDistance", "0.0");
			this.streamWriter.writeAttribute("fontFamily", "Dialog");
			this.streamWriter.writeAttribute("fontSize", "15");
			this.streamWriter.writeAttribute("fontStyle", fontStyle.getValue());
			this.streamWriter.writeAttribute("hasLineColor", "false");
			this.streamWriter.writeAttribute("height", "21.0");
			this.streamWriter.writeAttribute("modelName", "internal");
			this.streamWriter.writeAttribute("modelPosition", "t");
			this.streamWriter.writeAttribute("textColor", "#000000");
			this.streamWriter.writeAttribute("visible", "true");
			this.streamWriter.writeAttribute("width", "200.0");
			this.streamWriter.writeAttribute("x", "0.0");
			this.streamWriter.writeAttribute("y", "0.0");
			// TODO Support insets
			this.streamWriter.writeAttribute("underlinedText", "false");
			this.streamWriter.writeCharacters(label);
			this.streamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Node --- //

	public String node(String label) throws GraphMLException {
		assertState(State.GRAPH_OPENED);

		try {
			final String nodeId = nextNodeId();

			this.streamWriter.writeStartElement("node");
			this.streamWriter.writeAttribute("id", nodeId);

			// TODO Generate the <data key="d5"/> (node description)

			// Generate the tags for rendering the node
			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_GRAPHICS);

			this.streamWriter.writeStartElement("y:ShapeNode");

			writeGeometry(nodeStyle.getHeight(), nodeStyle.getWidth());
			writeFill(nodeStyle.getFillColor(), nodeStyle.getFillColor2(), nodeStyle.isTransparentFill());
			writeBorderStyle(nodeStyle.getBorderColor(), nodeStyle.getBorderType(), nodeStyle.getBorderWidth());
			writeNodeLabel(label);
			writeShape(nodeStyle.getShape());
			writeDropShadow(nodeStyle.getShadowColor(), nodeStyle.getShadowOffsetX(), nodeStyle.getShadowOffsetY());

			this.streamWriter.writeEndElement(); // </y:ShapeNode>
			this.streamWriter.writeEndElement(); // </data>
			this.streamWriter.writeEndElement(); // </node>

			// Store the node id
			this.nodeIds.add(nodeId);

			return nodeId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Edge --- //

	public String edge(String sourceNodeId, String targetNodeId) throws GraphMLException {
		Validate.isTrue(nodeIds.contains(sourceNodeId),
				String.format("The (source) node with given id '%s' doesn't exist", sourceNodeId));
		Validate.isTrue(nodeIds.contains(targetNodeId),
				String.format("The (target) node with given id '%s' doesn't exist", targetNodeId));

		assertState(State.GRAPH_OPENED);

		try {
			final String edgeId = nextEdgeId();

			this.streamWriter.writeStartElement("edge");
			this.streamWriter.writeAttribute("id", edgeId);
			this.streamWriter.writeAttribute("source", sourceNodeId);
			this.streamWriter.writeAttribute("target", targetNodeId);

			// TODO Generate the <data key="d9"/> (edge description)

			// Generate the tags for rendering the edge
			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", ID_EDGE_GRAPHICS);

			this.streamWriter.writeStartElement("y:PolyLineEdge");

			// What is the path used for ?
			writePath(0.0f, 0.0f, 0.0f, 0.0f);
			writeLineStyle(edgeStyle.getColor(), edgeStyle.getType(), edgeStyle.getWidth());
			writeArrows(edgeStyle.getSourceArrow(), edgeStyle.getTargetArrow());
			writeBendStyle(edgeStyle.isSmoothed());

			this.streamWriter.writeEndElement(); // </y:PolyLineEdge>
			this.streamWriter.writeEndElement(); // </data>
			this.streamWriter.writeEndElement(); // </edge>

			return edgeId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Group --- //

	public String group(String label) throws GraphMLException {
		assertState(State.GRAPH_OPENED);

		try {
			// A group is also a node
			final String groupId = nextNodeId();

			this.streamWriter.writeStartElement("node");
			this.streamWriter.writeAttribute("id", groupId);
			this.streamWriter.writeAttribute("yfiles.foldertype", "group");

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_URL);

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_DESCRIPTION);

			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_GRAPHICS);

			this.streamWriter.writeStartElement("y:ProxyAutoBoundsNode");

			this.streamWriter.writeStartElement("y:Realizers");
			this.streamWriter.writeAttribute("active", "0");

			// Define the group node when open
			this.streamWriter.writeStartElement("y:GroupNode");

			writeGeometry(openGroupStyle.getHeight(), openGroupStyle.getWidth());
			writeFill(openGroupStyle.getFillColor(), openGroupStyle.getFillColor2(), openGroupStyle.isTransparentFill());
			writeBorderStyle(openGroupStyle.getBorderColor(), openGroupStyle.getBorderType(), openGroupStyle.getBorderWidth());
			writeNodeLabel_Group(label, Alignment.RIGHT, FontStyle.PLAIN);
			writeShape(openGroupStyle.getShape());
			writeDropShadow(openGroupStyle.getShadowColor(), openGroupStyle.getShadowOffsetX(), openGroupStyle.getShadowOffsetY());
			writeState(false,  50, 50, false);
			writeInsets(openGroupStyle.getInsets());
			writeBorderInsets(openGroupStyle.getBorderInsets());

			this.streamWriter.writeEndElement(); // </y:GroupNode>

			// Define the group node when closed
			this.streamWriter.writeStartElement("y:GroupNode");

			writeGeometry(closedGroupStyle.getHeight(), closedGroupStyle.getWidth());
			writeFill(closedGroupStyle.getFillColor(), closedGroupStyle.getFillColor2(), closedGroupStyle.isTransparentFill());
			writeBorderStyle(closedGroupStyle.getBorderColor(), closedGroupStyle.getBorderType(), closedGroupStyle.getBorderWidth());
			writeNodeLabel_Group(label, Alignment.RIGHT, FontStyle.PLAIN);
			writeShape(closedGroupStyle.getShape());
			writeState(true,  50, 50, false);
			writeInsets(closedGroupStyle.getInsets());
			writeBorderInsets(closedGroupStyle.getBorderInsets());

			this.streamWriter.writeEndElement(); // </y:GroupNode>
			this.streamWriter.writeEndElement(); // </y:Realizers>
			this.streamWriter.writeEndElement(); // </y:ProxyAutoBoundsNode>
			this.streamWriter.writeEndElement(); // </data>

			this.streamWriter.writeStartElement("graph");
			this.streamWriter.writeAttribute("edgedefault", "directed");
			this.streamWriter.writeAttribute("id", groupId + ":");

			// Store the current group id in a stack
			this.groupIds.push(groupId);

			return groupId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// This method can only be called when inside a group
	public void closeGroup() throws GraphMLException {
		assertState(State.GRAPH_OPENED);
		if (!insideGroup()) {
			throw new IllegalStateException("The writer isn't inside a group. Invalid method call");
		}

		try {
			this.streamWriter.writeEndElement(); // </graph>
			this.streamWriter.writeEndElement(); // </node>

			// Pop the id of the closed group
			this.groupIds.pop();
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Others --- //

	private String nextNodeId() {
		if (groupIds.isEmpty()) {
			// We're not currently in a group
			return String.format("n%d", nodeSequence.getAndIncrement());
		}

		// What's the id of the current group ?
		final String groupId = groupIds.peek();

		// The node id looks like "<groupId>::<nodeId>"
		return String.format("%s::n%d", groupId, nodeSequence.getAndIncrement());
	}

	private String nextEdgeId() {
		return String.format("e%d", edgeSequence.getAndIncrement());
	}

	private void assertState(State expected) {
		if (state != expected) {
			throw new IllegalStateException(String.format("The writer is in an invalid state (Actual: %s, Expected: %s)",
					state.name(), expected.name()));
		}
	}

	private void assertNotState(State expected) {
		if (state == expected) {
			throw new IllegalStateException(String.format("The writer is already in state %s", expected.name()));
		}
	}

	public void close() {
		assertNotState(State.CLOSED);

		if (streamWriter != null) {
			try {
				streamWriter.close();
			} catch (XMLStreamException e) {
				// Close quietly
			}
		}
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				// Close quietly
			}
		}

		setState(State.CLOSED);
	}
}