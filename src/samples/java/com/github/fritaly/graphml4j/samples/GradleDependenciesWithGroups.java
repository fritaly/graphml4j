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
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.github.fritaly.graphml4j.GraphMLWriter;
import com.github.fritaly.graphml4j.NodeStyle;


/**
 * <p>
 * This sample demonstrates how to generate a dependency graph (to be visualized
 * in yEd) from the output of a "gradle dependencies" command. The Gradle output
 * has been saved as a resource file (see resource "gradle-dependencies.txt").
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
public class GradleDependenciesWithGroups {

	private static class Relationship {
		private final Artifact parent, child;

		public Relationship(Artifact parent, Artifact child) {
			this.parent = parent;
			this.child = child;
		}
	}

	private static class Artifact {

		private final String group, artifact, version;

		public Artifact(String value) {
			// Ex: "junit:junit:4.8.1"
			this.group = StringUtils.substringBefore(value, ":");
			this.artifact = StringUtils.substringBetween(value, ":");
			this.version = StringUtils.substringAfterLast(value, ":");
		}

		String getLabel() {
			return String.format("%s\n%s", artifact, version);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Artifact) {
				final Artifact other = (Artifact) obj;

				return StringUtils.equals(this.group, other.group)
						&& StringUtils.equals(this.artifact, other.artifact)
						&& StringUtils.equals(this.version, other.version);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(7, 23).append(this.group).append(this.artifact).append(this.version).toHashCode();
		}

		@Override
		public String toString() {
			return String.format("%s:%s:%s", group, artifact, version);
		}
	}

	private static Artifact createArtifact(String value) {
		// Examples of input values:
		// "org.springframework:spring-core:3.2.0.RELEASE (*)"
		// "com.acme:acme-logging:1.16.0 -> 1.16.3 (*)"
		// "junit:junit:3.8.1 -> 4.8.1"
		// "sun-jaxb:jaxb-impl:2.2"

		if (value.endsWith(" (*)")) {
			// Ex: "org.springframework:spring-core:3.2.0.RELEASE (*)" => "org.springframework:spring-core:3.2.0.RELEASE"
			value = value.replace(" (*)", "");
		}
		if (value.contains(" -> ")) {
			// Ex: "junit:junit:3.8.1 -> 4.8.1" => "junit:junit:4.8.1"
			final String version = StringUtils.substringAfter(value, " -> ");

			value = StringUtils.substringBeforeLast(value, ":") + ":" + version;
		}

		return new Artifact(value);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println(String.format("%s <output-file>", GradleDependenciesWithGroups.class.getSimpleName()));
			System.exit(1);
		}

		final File file = new File(args[0]);

		System.out.println("Writing GraphML file to " + file.getAbsolutePath() + " ...");

		FileWriter fileWriter = null;
		GraphMLWriter graphWriter = null;
		Reader reader = null;
		LineNumberReader lineReader = null;

		try {
			fileWriter = new FileWriter(file);
			graphWriter = new GraphMLWriter(fileWriter);

			// Customize the rendering of nodes
			final NodeStyle nodeStyle = graphWriter.getNodeStyle();
			nodeStyle.setWidth(250.0f);
			nodeStyle.setHeight(50.0f);

			graphWriter.setNodeStyle(nodeStyle);

			// The dependency graph has been generated by Gradle with the
			// command "gradle dependencies". The output of this command has
			// been saved to a text file which will be parsed to rebuild the
			// dependency graph
			reader = new InputStreamReader(GradleDependenciesWithGroups.class.getResourceAsStream("gradle-dependencies.txt"));
			lineReader = new LineNumberReader(reader);

			String line = null;

			// Stack containing the artifacts per depth inside the dependency
			// graph (the topmost dependency is the first one in the stack)
			final Stack<Artifact> stack = new Stack<Artifact>();

			final Map<String, Set<Artifact>> artifactsByGroup = new HashMap<String, Set<Artifact>>();

			// List of parent/child relationships between artifacts
			final List<Relationship> relationships = new ArrayList<Relationship>();

			while ((line = lineReader.readLine()) != null) {
				// Determine the depth of the current dependency inside the
				// graph. The depth can be inferred from the indentation used by
				// Gradle. Each level of depth adds 5 more characters of
				// indentation
				final int initialLength = line.length();

				// Remove the strings used by Gradle to indent dependencies
				line = StringUtils.replace(line, "+--- ", "");
				line = StringUtils.replace(line, "|    ", "");
				line = StringUtils.replace(line, "\\--- ", "");
				line = StringUtils.replace(line, "     ", "");

				// The depth can easily be inferred now
				final int depth = (initialLength - line.length()) / 5;

				// Remove unnecessary artifacts
				while (depth <= stack.size()) {
					stack.pop();
				}

				// Create an artifact from the dependency (group, artifact,
				// version) tuple
				final Artifact artifact = createArtifact(line);

				stack.push(artifact);

				if (stack.size() > 1) {
					// Store the artifact and its parent
					relationships.add(new Relationship(stack.get(stack.size() - 2), artifact));
				}

				if (!artifactsByGroup.containsKey(artifact.group)) {
					artifactsByGroup.put(artifact.group, new HashSet<Artifact>());
				}

				artifactsByGroup.get(artifact.group).add(artifact);
			}

			// Open the graph
			graphWriter.graph();

			final Map<Artifact, String> nodeIdsByArtifact = new HashMap<Artifact, String>();

			// Loop over the groups and generate the associated nodes
			for (String group : artifactsByGroup.keySet()) {
				graphWriter.group(group, true);

				for (Artifact artifact : artifactsByGroup.get(group)) {
					final String nodeId = graphWriter.node(artifact.getLabel());

					nodeIdsByArtifact.put(artifact, nodeId);
				}

				graphWriter.closeGroup();
			}

			// Generate the edges
			for (Relationship relationship : relationships) {
				final String parentId = nodeIdsByArtifact.get(relationship.parent);
				final String childId = nodeIdsByArtifact.get(relationship.child);

				graphWriter.edge(parentId, childId);
			}

			// Close the graph
			graphWriter.closeGraph();

			System.out.println("Done");
		} finally {
			// Calling GraphMLWriter.close() is necessary to dispose the underlying resources
			graphWriter.close();
			fileWriter.close();
			lineReader.close();
			reader.close();
		}
	}
}