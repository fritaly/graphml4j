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

public interface Renderer {

	String getNodeLabel(Node node);

	NodeStyle getNodeStyle(Node node);

	EdgeStyle getEdgeStyle(Edge edge);

	GroupStyles getGroupStyles(Node node);

	/**
	 * Tells whether the given group node should be rendered as open or closed.
	 *
	 * @param node
	 *            a node to render.
	 * @return whether the given group node should be rendered as open or
	 *         closed.
	 */
	boolean isGroupOpen(Node node);
}