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

import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.Validate;

public final class DirectedGraph {

	/**
	 * Map storing the nodes per id. This map only contains the direct child nodes for the graph.
	 */
	final Map<String, Node> childNodes = new LinkedHashMap<String, Node>();

	/**
	 * Map storing the nodes per id. This map contains all the nodes added to
	 * the graph (direct child or descendant nodes).
	 */
	private final Map<String, Node> allNodes = new LinkedHashMap<String, Node>();

	private final AtomicInteger nodeSequence = new AtomicInteger();

	private final AtomicInteger edgeSequence = new AtomicInteger();

	private final Map<String, Edge> edges = new LinkedHashMap<String, Edge>();

	public DirectedGraph() {
	}

	// --- Edge --- //

	public Edge addEdge(String sourceId, String targetId) {
		Validate.notNull(sourceId, "The given source node id is null");
		Validate.notNull(targetId, "The given target node id is null");

		// ensure the 2 nodes exist in the graph
		Validate.isTrue(nodeExists(sourceId), String.format("The given source node id '%s' doesn't exist", sourceId));
		Validate.isTrue(nodeExists(targetId), String.format("The given target node id '%s' doesn't exist", targetId));

		final String id = String.format("e%d", edgeSequence.incrementAndGet());

		final Edge edge = new Edge(id, getNode(sourceId), getNode(targetId));

		this.edges.put(edge.getId(), edge);

		return edge;
	}

	public Map<String, Edge> getEdges() {
		return Collections.unmodifiableMap(edges);
	}

	public Edge getEdgeById(String id) {
		Validate.notNull(id, "The given edge id is null");

		return this.edges.get(id);
	}

	public boolean hasEdgeWithId(String id) {
		return this.edges.containsKey(id);
	}

	public int getEdgeCount() {
		return this.edges.size();
	}

	// --- Node --- //

	public Node addNode(String label) {
		// validate the label prior generating the next node id
		Validate.notNull(label, "The given node label is null");

		final String id = String.format("n%d", nodeSequence.incrementAndGet());

		final Node node = new Node(this, id, label);

		this.childNodes.put(node.getId(), node);
		this.allNodes.put(node.getId(), node);

		return node;
	}

	public Map<String, Node> getChildNodes() {
		return Collections.unmodifiableMap(childNodes);
	}

	public Map<String, Node> getAllNodes() {
		return Collections.unmodifiableMap(allNodes);
	}

	public Node getNode(String id) {
		Validate.notNull(id, "The given node id is null");

		return this.allNodes.get(id);
	}

	public boolean hasNode(String id) {
		return this.allNodes.containsKey(id);
	}

	public boolean nodeExists(String id) {
		return this.allNodes.containsKey(id);
	}

	public int getNodeCount() {
		return this.allNodes.size();
	}

	// --- Miscellaneous --- //

	private void traverse(GraphMLWriter graphWriter, Map<String, String> nodeMappings, Node node) throws GraphMLException {
		// TODO select the node style before creating the node
		if (node.isGroup()) {
			// TODO decide whether the group should be open or closed
			final String nodeId = graphWriter.group(node.getLabel(), true);

			// store the id generated for this node for future lookups
			nodeMappings.put(node.getId(), nodeId);

			// handle child nodes
			for (Node child : node.getChildren()) {
				traverse(graphWriter, nodeMappings, child);
			}

			graphWriter.closeGroup();
		} else {
			final String nodeId = graphWriter.node(node.getLabel());

			// store the id generated for this node for future lookups
			nodeMappings.put(node.getId(), nodeId);
		}
	}

	public void toGraphML(Writer writer) throws GraphMLException {
		Validate.notNull(writer, "The given writer is null");

		final GraphMLWriter graphWriter = new GraphMLWriter(writer);
		graphWriter.graph();

		// map containing the mapping between internal & external node ids
		final Map<String, String> nodeMappings = new LinkedHashMap<String, String>();

		// generate the nodes and groups
		for (Node node : this.childNodes.values()) {
			traverse(graphWriter, nodeMappings, node);
		}

		// ... then the edges
		for (Edge edge : this.edges.values()) {
			final Node source = edge.getSource();
			final Node target = edge.getTarget();

			graphWriter.edge(nodeMappings.get(source.getId()), nodeMappings.get(target.getId()));
		}

		graphWriter.closeGraph();
		graphWriter.close();

	}
}