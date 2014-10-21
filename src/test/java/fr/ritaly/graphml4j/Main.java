package fr.ritaly.graphml4j;

import java.io.StringWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		final StringWriter stringWriter = new StringWriter();

		GraphMLWriter writer = new GraphMLWriter(stringWriter);
		writer.startDocument();
		writer.endDocument();

		System.out.println(stringWriter.toString());
	}
}