package fr.ritaly.graphml4j;

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

	private void setState(State newState) {
		Validate.notNull(newState, "The given new state is null");

		if (!this.state.isTransitionAllowed(newState)) {
			throw new IllegalArgumentException(String.format("Transition from state '%s' to '%s' is forbidden", state.name(),
					newState.name()));
		}

		this.state = newState;
	}

	public void startDocument() throws GraphMLException {
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
			this.streamWriter.writeAttribute("id", "d4");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", "d5");

			// Define the type 'node'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", "d6");
			this.streamWriter.writeAttribute("yfiles.type", "nodegraphics");

			// Define the attributes for type 'graph'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "Description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "graph");
			this.streamWriter.writeAttribute("id", "d7");

			// Define the attributes for type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "url");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d8");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d9");

			// Define the type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d10");
			this.streamWriter.writeAttribute("yfiles.type", "edgegraphics");

			setState(State.DOCUMENT_OPENED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	public void endDocument() throws GraphMLException {
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

	public void startGraph() throws GraphMLException {
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

	public void endGraph() throws GraphMLException {
		assertState(State.GRAPH_OPENED);

		try {
			this.streamWriter.writeEndElement(); // </graph>

			setState(State.GRAPH_CLOSED);
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
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

	private void writeFill(String color, boolean transparent) throws GraphMLException {
		Validate.notNull(color, "The given color is null");

		try {
			// y:Fill
			this.streamWriter.writeEmptyElement("y:Fill");
			this.streamWriter.writeAttribute("color", color);
			this.streamWriter.writeAttribute("transparent", Boolean.toString(transparent));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void writeBorderStyle(String color, LineType type, float width) throws GraphMLException {
		Validate.notNull(color, "The given color is null");
		Validate.notNull(type, "The given line type is null");
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		try {
            // y:BorderStyle
            this.streamWriter.writeEmptyElement("y:BorderStyle");
            this.streamWriter.writeAttribute("color", color);
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

	private void writeLineStyle(String color, LineType type, float width) throws GraphMLException {
		Validate.notNull(color, "The given color is null");
		Validate.notNull(type, "The given line type is null");
		Validate.isTrue(width > 0, String.format("The given width (%f) must be positive", width));

		try {
            // y:LineStyle
            this.streamWriter.writeEmptyElement("y:LineStyle");
            this.streamWriter.writeAttribute("color", color);
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

	private void writeBorderInsets(float all) throws GraphMLException {
		writeBorderInsets(all, all, all, all);
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

	private void writeInsets(float all) throws GraphMLException {
		writeInsets(all, all, all, all);
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

	private void writeNodeLabel(String label, Alignment alignment, FontStyle fontStyle) throws GraphMLException {
		Validate.notNull(label, "The given label is null");
		Validate.notNull(alignment, "The given alignment is null");
		Validate.notNull(fontStyle, "The given font style is null");

		try {
			// y:NodeLabel
			this.streamWriter.writeStartElement("y:NodeLabel");
			this.streamWriter.writeAttribute("alignement", alignment.getValue());
			this.streamWriter.writeAttribute("fontFamily", "Dialog");
			this.streamWriter.writeAttribute("fontSize", "12");
			this.streamWriter.writeAttribute("fontStyle", fontStyle.getValue());
			this.streamWriter.writeAttribute("hasBackgroundColor", "false");
			this.streamWriter.writeAttribute("hasLineColor", "false");
			this.streamWriter.writeAttribute("textColor", "#000000");
			this.streamWriter.writeAttribute("visible", "true");
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
			this.streamWriter.writeAttribute("key", "d6");

			this.streamWriter.writeStartElement("y:ShapeNode");

			writeGeometry(30.0f, 30.0f);
			writeFill("#FFCC00", false);
			writeBorderStyle("#000000", LineType.LINE, 1.0f);
			writeNodeLabel(label, Alignment.CENTER, FontStyle.PLAIN);
			writeShape(Shape.RECTANGLE);

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
			this.streamWriter.writeAttribute("key", "d10");

			this.streamWriter.writeStartElement("y:PolyLineEdge");

			writePath(0.0f, 0.0f, 0.0f, 0.0f);
			writeLineStyle("#000000", LineType.LINE, 1.0f);
			writeArrows(Arrow.NONE, Arrow.STANDARD);
			writeBendStyle(false);

			this.streamWriter.writeEndElement(); // </y:PolyLineEdge>
			this.streamWriter.writeEndElement(); // </data>
			this.streamWriter.writeEndElement(); // </edge>

			return edgeId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Group --- //

	public String startGroup(String label) throws GraphMLException {
		assertState(State.GRAPH_OPENED);

		try {
			// A group is also a node
			final String groupId = nextNodeId();

			this.streamWriter.writeStartElement("node");
			this.streamWriter.writeAttribute("id", groupId);
			this.streamWriter.writeAttribute("yfiles.foldertype", "group");

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", "d4");

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", "d5");

			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", "d6");

			this.streamWriter.writeStartElement("y:ProxyAutoBoundsNode");

			this.streamWriter.writeStartElement("y:Realizers");
			this.streamWriter.writeAttribute("active", "0");

			// Define the group node when closed
			this.streamWriter.writeStartElement("y:GroupNode");

			writeGeometry(80.0f,  140.0f);
			writeFill("#F5F5F5", false);
			writeBorderStyle("#000000", LineType.DASHED, 1.0f);
			writeNodeLabel_Group(label, Alignment.RIGHT, FontStyle.PLAIN);
			writeShape(Shape.ROUNDED_RECTANGLE);
			writeState(false,  50, 50, false);
			writeInsets(15);
			writeBorderInsets(0);

			this.streamWriter.writeEndElement(); // </y:GroupNode>

			// Define the group node when open
			this.streamWriter.writeStartElement("y:GroupNode");

			writeGeometry(50.0f,  50.0f);
			writeFill("#F5F5F5", false);
			writeBorderStyle("#000000", LineType.LINE, 1.0f);
			writeNodeLabel_Group(label, Alignment.RIGHT, FontStyle.PLAIN);
			writeShape(Shape.ROUNDED_RECTANGLE);
			writeState(true,  50, 50, false);
			writeInsets(5);
			writeBorderInsets(0);

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

	public void endGroup() throws GraphMLException {
		assertState(State.GRAPH_OPENED);

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