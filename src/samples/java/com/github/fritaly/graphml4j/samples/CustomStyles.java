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

import com.github.fritaly.graphml4j.EdgeStyle;
import com.github.fritaly.graphml4j.GraphMLWriter;
import com.github.fritaly.graphml4j.NodeStyle;
import com.github.fritaly.graphml4j.yed.Shape;
import com.github.fritaly.graphml4j.yed.*;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * <p>
 * This sample demonstrates how to use custom styles for rendering node, groups
 * and edges.
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
public class CustomStyles {

	private static void customizeEdges(GraphMLWriter graphWriter) {
		// Create a new default edge style and customize it
		final EdgeStyle edgeStyle = EdgeStyle.builder()
				.color(Color.BLUE)
				.smoothed(true)
				.sourceArrow(Arrow.CIRCLE)
				.targetArrow(Arrow.DELTA)
				.type(LineType.DASHED)
				.width(2.0f)
				.build();

		// All subsequent edges added to the graph will use this style
		graphWriter.setEdgeStyle(edgeStyle);
	}

	private static void customizeNodes(GraphMLWriter graphWriter) {
		// Create a new default node style and customize it
		final NodeStyle nodeStyle = new NodeStyle();
		nodeStyle.setBackgroundColor(Color.BLACK);
		nodeStyle.setBorderColor(Color.BLACK);
		nodeStyle.setBorderDistance(5.0f);
		nodeStyle.setBorderType(LineType.LINE);
		nodeStyle.setBorderWidth(2.0f);
		nodeStyle.setFillColor(Color.ORANGE);
		nodeStyle.setFontFamily("Arial");
		nodeStyle.setFontSize(14);
		nodeStyle.setFontStyle(FontStyle.BOLD_AND_ITALIC);
		nodeStyle.setHeight(100.0f);
		nodeStyle.setInsets(10);
		nodeStyle.setPlacement(Placement.INTERNAL);
		nodeStyle.setPosition(Position.TOP);
		nodeStyle.setRotationAngle(0.0f);
		nodeStyle.setShadowColor(Color.ORANGE.darker());
		nodeStyle.setShadowOffsetX(5);
		nodeStyle.setShadowOffsetY(5);
		nodeStyle.setShape(Shape.ROUNDED_RECTANGLE);
		nodeStyle.setSizePolicy(SizePolicy.NODE_WIDTH);
		nodeStyle.setTextAlignment(Alignment.LEFT);
		nodeStyle.setTextColor(Color.WHITE);
		nodeStyle.setUnderlinedText(true);
		nodeStyle.setVisible(true);
		nodeStyle.setWidth(100.0f);

		// All subsequent nodes added to the graph will use this style
		graphWriter.setNodeStyle(nodeStyle);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println(String.format("%s <output-file>", CustomStyles.class.getSimpleName()));
			System.exit(1);
		}

		final File file = new File(args[0]);

		System.out.println("Writing GraphML file to " + file.getAbsolutePath() + " ...");

		// This list will store the identifiers of nodes added to the graph
		final List<String> nodeIds = new ArrayList<String>();

		final FileWriter fileWriter = new FileWriter(file);

		final GraphMLWriter graphWriter = new GraphMLWriter(fileWriter);

		try {
			// Open the graph
			graphWriter.graph();

			// Customize the different styles
			customizeEdges(graphWriter);
			customizeNodes(graphWriter);

			// Generate 3 groups (of nodes) with 5 nodes in each
			for (int i = 0; i < 3; i++) {
				// Randomly decide whether the group should be rendered as open or closed
				final boolean open = new Random().nextBoolean();

				// Open a new group of nodes
				graphWriter.group(String.format("Group #%d", i + 1), open);

				// Add 5 nodes to the group
				for (int j = 0; j < 5; j++) {
					nodeIds.add(graphWriter.node(String.format("N%d", (i * 5) + j + 1)));
				}

				// Close the group
				graphWriter.closeGroup();
			}

			// Randomly generate 15 edges between the nodes
			for (int i = 0; i < 15; i++) {
				Collections.shuffle(nodeIds);

				graphWriter.edge(nodeIds.get(0), nodeIds.get(1));
			}

			// Close the graph
			graphWriter.closeGraph();

			System.out.println("Done");
		} finally {
			// Calling GraphMLWriter.close() is necessary to dispose the underlying resources
			graphWriter.close();
			fileWriter.close();
		}
	}
}