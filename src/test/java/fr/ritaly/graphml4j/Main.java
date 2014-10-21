package fr.ritaly.graphml4j;

import java.io.StringWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		final StringWriter stringWriter = new StringWriter();

		GraphMLWriter writer = new GraphMLWriter(stringWriter);
		writer.startDocument();
		writer.startGraph();

		for (int i = 0; i < 5; i++) {
			writer.node(Integer.toString(i));

			if ((i > 0) && (i < 4)) {
				writer.edge("n" + i, "n" + (i+1));
			}
		}

		writer.endGraph();
		writer.endDocument();

		System.out.println(stringWriter.toString());
	}
}