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

import com.github.fritaly.graphml4j.datastructure.Edge;
import com.github.fritaly.graphml4j.datastructure.Node;

/**
 * Default implementation of {@link Renderer}.
 * 
 * @author francois_ritaly
 */
public class DefaultRenderer implements Renderer {

	public DefaultRenderer() {
	}

	@Override
	public String getNodeLabel(Node node) {
		return node.getLabel();
	}

	@Override
	public NodeStyle getNodeStyle(Node node) {
		return new NodeStyle();
	}

	@Override
	public EdgeStyle getEdgeStyle(Edge edge) {
		return EdgeStyle.DEFAULT;
	}

	@Override
	public GroupStyles getGroupStyles(Node node) {
		return new GroupStyles();
	}

	@Override
	public boolean isGroupOpen(Node node) {
		// Render groups as closed nodes
		return false;
	}
}