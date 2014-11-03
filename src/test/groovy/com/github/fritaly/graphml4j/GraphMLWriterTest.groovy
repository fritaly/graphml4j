/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License") you may not use this file except in compliance with
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
package com.github.fritaly.graphml4j

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.github.fritaly.graphml4j.GraphMLWriter.State;

public class GraphMLWriterTest {

	private StringWriter stringWriter

	private GraphMLWriter graphWriter

	@Before
	public void setUp() throws Exception {
		this.stringWriter = new StringWriter(8192)
		this.graphWriter = new GraphMLWriter(stringWriter)
	}

	private static void assertState(State expected, State actual) {
		assertEquals(String.format("The writer's state isn't valid. It should be %s but is currently set to %s", expected.name(),
				actual.name()), expected, actual)
	}

	@Test
	public void "check the write's initial state"() throws Exception {
		assertState(State.INITIAL, graphWriter.getState())
	}

	@Test
	public void "check the writer state after opening the graph"() throws Exception {
		graphWriter.graph()

		assertState(State.GRAPH_OPENED, graphWriter.getState())
	}

	@Test
	public void "check the writer state after opening a group"() throws Exception {
		graphWriter.graph()
		graphWriter.group("group", false)

		// The state is still GRAPH_OPENED when inside a group
		assertState(State.GRAPH_OPENED, graphWriter.getState())
	}

	@Test
	public void "check the writer state after closing a group"() throws Exception {
		graphWriter.graph()
		graphWriter.group("group", false)
		graphWriter.closeGroup()

		// The state is still GRAPH_OPENED when outside a group
		assertState(State.GRAPH_OPENED, graphWriter.getState())
	}

	@Test
	public void "check the writer state after closing the graph"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGraph()

		assertState(State.DOCUMENT_CLOSED, graphWriter.getState())
	}

	@Test
	public void "check the writer final state"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGraph()
		graphWriter.close()

		assertState(State.CLOSED, graphWriter.getState())
	}

	@Test(expected = IllegalStateException.class)
	public void "opening the graph twice should fail"() throws Exception {
		graphWriter.graph()
		graphWriter.graph() // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "closing the graph twice should fail"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGraph()
		graphWriter.closeGraph() // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "closing the graph writer twice should fail"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGraph()
		graphWriter.close()
		graphWriter.close() // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "closing a non-opened group should fail"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGroup() // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "closing a non-opened graph should fail"() throws Exception {
		graphWriter.closeGraph() // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "adding a node to a non-opened graph should fail"() throws Exception {
		graphWriter.node("label") // <-- Error

		fail()
	}

	@Test(expected = IllegalStateException.class)
	public void "adding a group to a non-opened graph should fail"() throws Exception {
		graphWriter.group("label", true) // <-- Error

		fail()
	}

	@Test
	public void "building an empty graph should succeed"() throws Exception {
		graphWriter.graph()
		graphWriter.closeGraph()
		graphWriter.close()

		String text = stringWriter.toString()

		// The output shouldn't be empty
		assertNotNull(text)
		assertTrue(text.length() > 0)

		// The generated XML should be well-formed
		def root = new XmlSlurper().parseText(text)
	}

	@Test
	public void "building a graph with some nodes, edges and groups should succeed"() throws Exception {
		graphWriter.graph()

		def n1 = graphWriter.node("N1")
		def n2 = graphWriter.node("N2")
		def n3 = graphWriter.node("N3")

		// Create a group with 2 nodes
		graphWriter.group("G1", true)

		def n4 = graphWriter.node("N4")
		def n5 = graphWriter.node("N5")

		graphWriter.closeGroup()

		// Create some edges
		graphWriter.edge(n1, n2)
		graphWriter.edge(n1, n3)
		graphWriter.edge(n2, n4)
		graphWriter.edge(n4, n5)

		graphWriter.closeGraph()
		graphWriter.close()

		String text = stringWriter.toString()

		// The output shouldn't be empty
		assertNotNull(text)
		assertTrue(text.length() > 0)

		// The generated XML should be well-formed
		def root = new XmlSlurper().parseText(text)
	}

	@Test
	public void "building a graph with nested groups should succeed"() throws Exception {
		graphWriter.graph()

		// Create a first group with some nodes
		graphWriter.group("G1", true)

		def n1 = graphWriter.node("N1")
		def n2 = graphWriter.node("N2")
		def n3 = graphWriter.node("N3")

		// Create a second nested group with some other nodes
		graphWriter.group("G2", true)

		def n4 = graphWriter.node("N4")
		def n5 = graphWriter.node("N5")

		// Close the 2 groups
		graphWriter.closeGroup()
		graphWriter.closeGroup()

		// Create some edges
		graphWriter.edge(n1, n2)
		graphWriter.edge(n1, n3)
		graphWriter.edge(n2, n4)
		graphWriter.edge(n4, n5)

		graphWriter.closeGraph()
		graphWriter.close()

		String text = stringWriter.toString()

		// The output shouldn't be empty
		assertNotNull(text)
		assertTrue(text.length() > 0)

		// The generated XML should be well-formed
		def root = new XmlSlurper().parseText(text)
	}
}