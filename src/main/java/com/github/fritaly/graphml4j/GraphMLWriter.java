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

/**
 * <p>This class is a stream writer ("a la Stax") used for generating GraphML
 * markup language for yEd.</p>
 * <p>Check the samples for examples of how to use this class.</p>
 * <p>
 * The typical usage looks like:
 * <ul>
 * <li>Create a new graph writer</li>
 * <li>Open the graph</li>
 * <li>Create some nodes</li>
 * <li>Possibly
 * <ul>
 * <li>Open a group</li>
 * <li>Create some nodes inside the group</li>
 * <li>Close the group</li>
 * </ul>
 * </li>
 * <li>Create some edges</li>
 * <li>Close the graph</li>
 * <li>Close the graph writer</li>
 * </ul>
 * </p>
 *
 * @author francois_ritaly
 */
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
	static enum State {
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

	private final GroupStyles groupStyles = new GroupStyles();

	/**
	 * Creates a new instance of {@link GraphMLWriter} using the given writer to
	 * generate the GraphML markup language.
	 *
	 * @param writer
	 *            a {@link Writer} where the GraphML markup language will be
	 *            written. Can't be null.
	 * @throws GraphMLException
	 *             if an error occurs when initializing the writer.
	 */
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

	State getState() {
		// Getter mainly for unit tests
		return state;
	}

	/**
	 * <p>
	 * Returns the styles applied to new groups.
	 * </p>
	 * <p>
	 * Note: In order to preserve encapsulation, the returned style is a copy of
	 * the "live" style. To apply a new style, one needs to call the method
	 * {@link #setGroupStyles(GroupStyles)}.
	 * </p>
	 *
	 * @return a new instance {@link GroupStyles}. Never returns null.
	 */
	public GroupStyles getGroupStyles() {
		// Defensive recopy
		return new GroupStyles(groupStyles);
	}

	/**
	 * <p>Sets the styles to be applied to new groups.</p>
	 *
	 * @param styles the style for rendering new groups. Can't be null.
	 */
	public void setGroupStyles(GroupStyles styles) {
		Validate.notNull(styles, "The given groups styles is null");

		// Defensive recopy
		this.groupStyles.apply(styles);
	}

	/**
	 * <p>
	 * Returns the style applied to new nodes.
	 * </p>
	 * <p>
	 * Note: In order to preserve encapsulation, the returned style is a copy of
	 * the "live" style. To apply a new style, one needs to call the method
	 * {@link #setNodeStyle(NodeStyle)}.
	 * </p>
	 *
	 * @return a new instance {@link NodeStyle}. Never returns null.
	 */
	public NodeStyle getNodeStyle() {
		// Defensive recopy
		return new NodeStyle(nodeStyle);
	}

	/**
	 * <p>Sets the style to be applied to new nodes.</p>
	 *
	 * @param style the style for rendering new nodes. Can't be null.
	 */
	public void setNodeStyle(NodeStyle style) {
		Validate.notNull(style, "The given style is null");

		// Defensive recopy
		this.nodeStyle.apply(style);
	}

	/**
	 * <p>
	 * Returns the style applied to new edges.
	 * </p>
	 * <p>
	 * Note: In order to preserve encapsulation, the returned style is a copy of
	 * the "live" style. To apply a new style, one needs to call the method
	 * {@link #setEdgeStyle(EdgeStyle)}.
	 * </p>
	 *
	 * @return a new instance {@link EdgeStyle}. Never returns null.
	 */
	public EdgeStyle getEdgeStyle() {
		// Defensive recopy
		return new EdgeStyle(edgeStyle);
	}

	/**
	 * <p>Sets the style to be applied to new edges.</p>
	 *
	 * @param style the style for rendering new edges. Can't be null.
	 */
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

	/**
	 * <p>
	 * Opens the graph. The graph can only be opened once.
	 * </p>
	 * <p>
	 * This method will fail if the graph has already been opened.
	 * </p>
	 *
	 * @throws GraphMLException
	 *             if an error occurs when opening the graph.
	 */
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

	/**
	 * <p>
	 * Closes the current graph. Once closed, the graph can't be reopened.
	 * </p>
	 * <p>
	 * This method will fail if the graph hasn't been opened or if a group is
	 * currently open.
	 * </p>
	 *
	 * @throws GraphMLException
	 *             if an error occurs when closing the graph.
	 * @see #graph()
	 */
	public void closeGraph() throws GraphMLException {
		// This method can only be called when not inside a group
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

	// --- Node --- //

	/**
	 * <p>Creates a new node with the given label and returns the identifier
	 * assigned to the node.</p>
	 *
	 * @param label
	 *            a string representing the node label. Can't be null.
	 * @return a string identifying the newly created node in the graph. Never
	 *         returns null.
	 * @throws GraphMLException
	 *             if an error occurs when creating the node.
	 */
	public String node(String label) throws GraphMLException {
		return node(label, 0.0f, 0.0f);
	}

	public String node(String label, float x, float y) throws GraphMLException {
		Validate.notNull(label, "The given label is null");

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

			nodeStyle.writeTo(streamWriter, label, x, y);

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

	/**
	 * <p>Creates a new edge between the 2 nodes identified by the provided node
	 * ids and returns the id assigned to the edge.</p>
	 * <p>This method will fail if the graph hasn't been opened.</p>
	 *
	 * @param sourceNodeId
	 *            a string representing the id of the edge's source node. Can't
	 *            be null.
	 * @param targetNodeId
	 *            a string representing the id of the edge's target node. Can't
	 *            be null.
	 * @return a string corresponding to the id assigned to the newly created
	 *         edge. Never returns null.
	 * @throws GraphMLException
	 *             if an error occurs when creating the edge.
	 */
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

			edgeStyle.writeTo(streamWriter);

			this.streamWriter.writeEndElement(); // </y:PolyLineEdge>
			this.streamWriter.writeEndElement(); // </data>
			this.streamWriter.writeEndElement(); // </edge>

			return edgeId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	// --- Group --- //

	/**
	 * <p>
	 * Creates a new group (of nodes) with the given label and returns the
	 * identifier assigned to the group.
	 * </p>
	 *
	 * @param label
	 *            a string representing the group label. Can't be null.
	 * @param open
	 *            whether the group should be rendered as open or closed.
	 * @return a string identifying the newly created group node in the graph.
	 *         Never returns null.
	 * @throws GraphMLException
	 *             if an error occurs when creating the group.
	 * @see #closeGroup()
	 */
	public String group(String label, boolean open) throws GraphMLException {
		return group(label, open, 0.0f, 0.0f);
	}

	public String group(String label, boolean open, float x, float y) throws GraphMLException {
		Validate.notNull(label, "The given label is null");

		assertState(State.GRAPH_OPENED);

		try {
			// A group is also a node
			final String groupId = nextNodeId();

			this.streamWriter.writeStartElement("node");
			this.streamWriter.writeAttribute("id", groupId);
			this.streamWriter.writeAttribute("yfiles.foldertype", open ? "group" : "folder");

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_URL);

			this.streamWriter.writeEmptyElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_DESCRIPTION);

			this.streamWriter.writeStartElement("data");
			this.streamWriter.writeAttribute("key", ID_NODE_GRAPHICS);

			this.streamWriter.writeStartElement("y:ProxyAutoBoundsNode");

			this.streamWriter.writeStartElement("y:Realizers");
			this.streamWriter.writeAttribute("active", open ? "0" : "1");

			// Define the group node when open
			this.streamWriter.writeStartElement("y:GroupNode");

			groupStyles.getOpenStyle().writeTo(streamWriter, label, false, x, y);

			this.streamWriter.writeEndElement(); // </y:GroupNode>

			// Define the group node when closed
			this.streamWriter.writeStartElement("y:GroupNode");

			groupStyles.getClosedStyle().writeTo(streamWriter, label, true, x, y);

			this.streamWriter.writeEndElement(); // </y:GroupNode>

			this.streamWriter.writeEndElement(); // </y:Realizers>
			this.streamWriter.writeEndElement(); // </y:ProxyAutoBoundsNode>
			this.streamWriter.writeEndElement(); // </data>

			this.streamWriter.writeStartElement("graph");
			this.streamWriter.writeAttribute("edgedefault", "directed");
			this.streamWriter.writeAttribute("id", groupId + ":");

			// Store the current group id in a stack
			this.groupIds.push(groupId);

			// Store the group node id
			this.nodeIds.add(groupId);

			return groupId;
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	/**
	 * <p>
	 * Closes the current group. Once closed, the group can't be reopened.
	 * </p>
	 * <p>
	 * This method will fail if the graph hasn't been opened or if a group isn't
	 * currently open.
	 * </p>
	 *
	 * @throws GraphMLException
	 *             if an error occurs when closing the group.
	 * @see #group(String, boolean)
	 * @see #group(String, boolean, float, float)
	 */
	public void closeGroup() throws GraphMLException {
		// This method can only be called when inside a group
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

	/**
	 * <p>
	 * Closes the graph writer and all underlying resources. Once closed, the
	 * graph writer can't be reopened.
	 * </p>
	 */
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