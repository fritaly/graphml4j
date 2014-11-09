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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public final class Node implements Comparable<Node> {

	private final String id;

	private final String label;

	private final Set<Node> children = new TreeSet<Node>();

	private final DirectedGraph graph;

	private Node parent;

	private Object data;

	Node(DirectedGraph graph, String id, String label) {
		Validate.notNull(graph, "The given graph is null");
		Validate.notNull(id, "The given node id is null");
		Validate.notNull(label, "The given node label is null");

		this.graph = graph;
		this.id = id;
		this.label = label;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		// the data can be null

		this.data = data;
	}

	DirectedGraph getGraph() {
		return graph;
	}

	@Override
	public int compareTo(Node node) {
		return this.id.compareTo(node.id);
	}

	public Node getParent() {
		return this.parent;
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

	public String getLabel() {
		return label;
	}

	public String getId() {
		return id;
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

		this.children.add(node);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id).append("label", label).toString();
	}
}
