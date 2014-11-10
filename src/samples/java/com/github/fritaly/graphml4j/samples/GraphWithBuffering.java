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
package com.github.fritaly.graphml4j.samples;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.fritaly.graphml4j.Node;

/**
 * <p>
 * This sample demonstrates how to use the Graphml4j API to generate a simple
 * graph (with the default styles).
 * </p>
 * <p>
 * Instructions:
 * <ul>
 * <li>Download and install yEd (if necessary)</li>
 * <li>Execute this sample and generate a GraphML file</li>
 * <li>Open the generated file in yEd</li>
 * <li>In yEd, render the graph with the "Hierarchical" layout</li>
 * </ul>
 * </p>
 *
 * @author francois_ritaly
 */
public class GraphWithBuffering {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println(String.format("%s <output-file>", GraphWithBuffering.class.getSimpleName()));
			System.exit(1);
		}

		final File file = new File(args[0]);

		System.out.println("Writing GraphML file to " + file.getAbsolutePath() + " ...");

		// This list will store the identifiers of nodes added to the graph
		final List<Node> nodes = new ArrayList<Node>();
		final List<Node> groups = new ArrayList<Node>();

		final FileWriter fileWriter = new FileWriter(file);

		// this data structure will hold the edges & nodes in memory
		final com.github.fritaly.graphml4j.Graph graph = new com.github.fritaly.graphml4j.Graph();

		try {
			// generate 15 nodes
			for (int i = 0; i < 15; i++) {
				nodes.add(graph.addNode(String.format("N%d", (i + 1))));
			}

			// create 3 group nodes
			for (int i = 0; i < 3; i++) {
				groups.add(graph.addNode(String.format("Group #%d", i + 1)));
			}

			// After the fact, add 5 nodes in each group. The buffering allows
			// moving nodes inside the graph thus simplifying the generation of
			// GraphML files (groups can be handled after generating the nodes)
			final Iterator<Node> iterator = nodes.iterator();

			for (Node group : groups) {
				for (int i = 0; i < 5; i++) {
					iterator.next().setParent(group);
				}
			}

			// Randomly generate 15 edges between the nodes
			for (int i = 0; i < 15; i++) {
				Collections.shuffle(nodes);

				graph.addEdge("Depends on", nodes.get(0), nodes.get(1));
			}

			graph.toGraphML(fileWriter);

			System.out.println("Done");
		} finally {
			fileWriter.close();
		}
	}
}