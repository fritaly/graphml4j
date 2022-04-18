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
package com.github.fritaly.graphml4j.datastructure;

import com.github.fritaly.graphml4j.Renderer;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ToString
public final class Node implements Comparable<Node> {

	@Getter
	private final String id;

	@ToString.Exclude
	private final Set<Node> children = new TreeSet<Node>();

	@ToString.Exclude
	private final Graph graph;

	@Getter
	@ToString.Exclude
	private Node parent;

	@Getter
	private final Object data;

	// TODO Create a class NodeRenderer to generate the label of a node

	Node(Graph graph, String id, Object data) {
		// the node data can be null
		Validate.notNull(graph, "The given graph is null");
		Validate.notNull(id, "The given node id is null");

		this.graph = graph;
		this.id = id;
		this.data = data;
	}

	public String getLabel() {
		return hasData() ? getData().toString() : getId();
	}

	String getLabel(Renderer renderer) {
		// the renderer can be null
		return (renderer != null) ? renderer.getNodeLabel(this) : getLabel();
	}

	public boolean hasData() {
		return (data != null);
	}

	Graph getGraph() {
		return graph;
	}

	@Override
	public int compareTo(Node node) {
		return this.id.compareTo(node.id);
	}

	public boolean hasParent() {
		return (this.parent != null);
	}

	public void setParent(Node node) {
		// the parent can be null

		if (this.parent != null) {
			// if there's a current parent node, unlink the 2 nodes
			this.parent.children.remove(this);
		} else {
			// the parent is the root graph
			this.graph.childNodes.remove(this.getId());
		}

		this.parent = node;

		if (node != null) {
			// link the 2 nodes
			node.children.add(this);
		} else {
			// the parent is the root graph
			this.graph.childNodes.put(this.getId(), this);
		}
	}

	public void detach() {
		// detach the node from its parent (if any)
		// this method won't remove the node from the graph, it'll move
		// the node to the root of the graph
		setParent(null);
	}

	public boolean isGroup() {
		return !this.children.isEmpty();
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(this.children);
	}

	public void addChild(Node node) {
		// TODO check that the node belongs to the same graph
		Validate.notNull(node, "The given node is null");
		Validate.isTrue(this.graph == node.graph, "The given node doesn't belong to the same graph");
		Validate.isTrue(this != node, "A node can't be its own child or parent");

		this.children.add(node);
	}
}
