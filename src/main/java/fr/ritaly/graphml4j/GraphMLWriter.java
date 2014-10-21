package fr.ritaly.graphml4j;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
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

	private final Writer writer;

	private final XMLStreamWriter streamWriter;

	/**
	 * Tells whether the writer has been already closed.
	 */
	private boolean closed = false;

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

	public void startDocument() throws GraphMLException {
		assertNotClosed();

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
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	public void endDocument() throws GraphMLException {
		assertNotClosed();

		try {
			// Close the <graphml> root tag
			this.streamWriter.writeEndElement();

			this.streamWriter.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Graph --- //

	public void startGraph() throws GraphMLException {
		assertNotClosed();

		try {
			// TODO Introduce parameters for the 2 attributes
			this.streamWriter.writeStartElement("graph");
			this.streamWriter.writeAttribute("edgedefault", "directed");
			this.streamWriter.writeAttribute("id", "G");

			// TODO Generate the <data key="d7"/>
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	public void endGraph() throws GraphMLException {
		assertNotClosed();

		try {
			// Close the element <graph>
			this.streamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Internal helper methods --- //

	private void writeGeometry(float height, float width) throws GraphMLException {
		try {
			// y:Geometry
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
		try {
			// y:Shape
			this.streamWriter.writeEmptyElement("y:Shape");
			this.streamWriter.writeAttribute("type", shape.getValue());
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Node --- //

	public String node(String label) throws GraphMLException {
		assertNotClosed();

		try {
			final String nodeId = nextNodeId();

			this.streamWriter.writeStartElement("node");
			this.streamWriter.writeAttribute("id", nodeId);

			// TODO Generate the <data key="d5"/> (node description)

			// Generate the tags for rendering the node
			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", "d6");

			this.streamWriter.writeStartElement("y:ShapeNode");

			// y:Geometry
			writeGeometry(30.0f, 30.0f);

			// y:Fill
			writeFill("#FFCC00", false);

			// y:BorderStyle
			writeBorderStyle("#000000", LineType.LINE, 1.0f);

			// y:NodeLabel
			this.streamWriter.writeStartElement("y:NodeLabel");
			this.streamWriter.writeAttribute("alignement", "center");
			this.streamWriter.writeAttribute("fontFamily", "Dialog");
			this.streamWriter.writeAttribute("fontSize", "12");
			this.streamWriter.writeAttribute("fontStyle", "plain");
			this.streamWriter.writeAttribute("hasBackgroundColor", "false");
			this.streamWriter.writeAttribute("hasLineColor", "false");
			this.streamWriter.writeAttribute("textColor", "#000000");
			this.streamWriter.writeAttribute("visible", "true");
			this.streamWriter.writeCharacters(label);
			this.streamWriter.writeEndElement(); // </y:NodeLabel>

			// y:Shape
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

		assertNotClosed();

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

			// y:Path
			// TODO What is this used for ?
			this.streamWriter.writeEmptyElement("y:Path");
			this.streamWriter.writeAttribute("sx", "0.0");
			this.streamWriter.writeAttribute("sy", "0.0");
			this.streamWriter.writeAttribute("tx", "0.0");
			this.streamWriter.writeAttribute("ty", "0.0");

			// y:LineStyle
			this.streamWriter.writeEmptyElement("y:LineStyle");
			this.streamWriter.writeAttribute("color", "#000000");
			this.streamWriter.writeAttribute("type", "Line");
			this.streamWriter.writeAttribute("width", "1.0");

			// y:Arrows
			this.streamWriter.writeEmptyElement("y:Arrows");
			this.streamWriter.writeAttribute("source", "none");
			this.streamWriter.writeAttribute("target", "standard");

			// y:BendStyle
			this.streamWriter.writeEmptyElement("y:BendStyle");
			this.streamWriter.writeAttribute("smoothed", "false");

			this.streamWriter.writeEndElement(); // </y:PolyLineEdge>
			this.streamWriter.writeEndElement(); // </data>
			this.streamWriter.writeEndElement(); // </edge>

			return edgeId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Group --- //

	public String startGroup() throws GraphMLException {
		assertNotClosed();

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

			// y:Geometry
			writeGeometry(80.0f,  140.0f);

			// y:Fill
			writeFill("#F5F5F5", false);

			// y:BorderStyle
			writeBorderStyle("#000000", LineType.DASHED, 1.0f);

			// y:Shape
			writeShape(Shape.ROUNDED_RECTANGLE);

			this.streamWriter.writeEmptyElement("y:State");
			this.streamWriter.writeAttribute("closed", "false");
			this.streamWriter.writeAttribute("closedHeight", "50.0");
			this.streamWriter.writeAttribute("closedWidth", "50.0");
			this.streamWriter.writeAttribute("innerGraphDisplayEnabled", "false");

			this.streamWriter.writeEmptyElement("y:Insets");
			this.streamWriter.writeAttribute("bottom", "15");
			this.streamWriter.writeAttribute("bottomF", "15.0");
			this.streamWriter.writeAttribute("left", "15");
			this.streamWriter.writeAttribute("leftF", "15.0");
			this.streamWriter.writeAttribute("right", "15");
			this.streamWriter.writeAttribute("rightF", "15.0");
			this.streamWriter.writeAttribute("top", "15");
			this.streamWriter.writeAttribute("topF", "15.0");

			this.streamWriter.writeEmptyElement("y:BorderInsets");
			this.streamWriter.writeAttribute("bottom", "0");
			this.streamWriter.writeAttribute("bottomF", "0.0");
			this.streamWriter.writeAttribute("left", "0");
			this.streamWriter.writeAttribute("leftF", "0.0");
			this.streamWriter.writeAttribute("right", "0");
			this.streamWriter.writeAttribute("rightF", "0.0");
			this.streamWriter.writeAttribute("top", "0");
			this.streamWriter.writeAttribute("topF", "0.0");

			this.streamWriter.writeEndElement(); // </y:GroupNode>

			// Define the group node when open
			this.streamWriter.writeStartElement("y:GroupNode");

			// y:Geometry
			writeGeometry(50.0f,  50.0f);

			// y:Fill
			writeFill("#F5F5F5", false);

			// y:BorderStyle
			writeBorderStyle("#000000", LineType.LINE, 1.0f);

			// y:Shape
			writeShape(Shape.ROUNDED_RECTANGLE);

			this.streamWriter.writeEmptyElement("y:State");
			this.streamWriter.writeAttribute("closed", "true");
			this.streamWriter.writeAttribute("closedHeight", "50.0");
			this.streamWriter.writeAttribute("closedWidth", "50.0");
			this.streamWriter.writeAttribute("innerGraphDisplayEnabled", "false");

			this.streamWriter.writeEmptyElement("y:Insets");
			this.streamWriter.writeAttribute("bottom", "5");
			this.streamWriter.writeAttribute("bottomF", "5.0");
			this.streamWriter.writeAttribute("left", "5");
			this.streamWriter.writeAttribute("leftF", "5.0");
			this.streamWriter.writeAttribute("right", "5");
			this.streamWriter.writeAttribute("rightF", "5.0");
			this.streamWriter.writeAttribute("top", "5");
			this.streamWriter.writeAttribute("topF", "5.0");

			this.streamWriter.writeEmptyElement("y:BorderInsets");
			this.streamWriter.writeAttribute("bottom", "0");
			this.streamWriter.writeAttribute("bottomF", "0.0");
			this.streamWriter.writeAttribute("left", "0");
			this.streamWriter.writeAttribute("leftF", "0.0");
			this.streamWriter.writeAttribute("right", "0");
			this.streamWriter.writeAttribute("rightF", "0.0");
			this.streamWriter.writeAttribute("top", "0");
			this.streamWriter.writeAttribute("topF", "0.0");

			this.streamWriter.writeEndElement(); // </y:GroupNode>

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
		assertNotClosed();

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

	private void assertNotClosed() {
		if (closed) {
			throw new IllegalStateException("The writer is closed");
		}
	}

	public void close() {
		assertNotClosed();

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

		this.closed = true;
	}
}