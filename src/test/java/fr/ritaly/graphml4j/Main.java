package fr.ritaly.graphml4j;

import java.io.StringWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		final StringWriter stringWriter = new StringWriter();

		GraphMLWriter writer = new GraphMLWriter(stringWriter);
		writer.startGraph();
		writer.startGroup("TEST");

		String prevNodeId = null;

		for (int i = 0; i < 5; i++) {
			final String nodeId = writer.node(Integer.toString(i));

			if ((i > 0) && (i < 4)) {
				writer.edge(prevNodeId, nodeId);
			}

			prevNodeId = nodeId;
		}

		writer.endGroup();
		writer.endGraph();

		System.out.println(stringWriter.toString());
	}
}