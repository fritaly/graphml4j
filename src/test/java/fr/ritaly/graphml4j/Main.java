package fr.ritaly.graphml4j;

import java.awt.Color;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		final StringWriter stringWriter = new StringWriter();

		GraphMLWriter writer = new GraphMLWriter(stringWriter);

		// Customize the rendering
		final NodeStyle nodeStyle = writer.getNodeStyle();
		nodeStyle.setBorderType(LineType.LINE);
		nodeStyle.setBorderWidth(2.0f);
		nodeStyle.setFillColor(Color.GRAY);
		nodeStyle.setFontSize(13);
		nodeStyle.setHeight(60.0f);
		nodeStyle.setWidth(60.0f);
		nodeStyle.setShape(Shape.ROUNDED_RECTANGLE);
		nodeStyle.setTextAlignment(Alignment.CENTER);

		writer.setNodeStyle(nodeStyle);

		final EdgeStyle edgeStyle = writer.getEdgeStyle();
		edgeStyle.setColor(Color.BLACK);
		edgeStyle.setSmoothed(true);
		edgeStyle.setSourceArrow(Arrow.CIRCLE);
		edgeStyle.setTargetArrow(Arrow.DELTA);
		edgeStyle.setType(LineType.LINE);
		edgeStyle.setWidth(2.0f);

		writer.setEdgeStyle(edgeStyle);

		// Generate a random graph
		writer.graph();

		final List<String> nodeIds = new ArrayList<String>();

		// Generate some nodes
		for (int i = 0; i < 3; i++) {
			writer.group(String.format("G%d", i + 1));

			for (int j = 0; j < 5; j++) {
				nodeIds.add(writer.node(String.format("N%d", (i * 5) + j + 1)));
			}

			writer.closeGroup();
		}

		// Generate some edges
		for (int i = 0; i < 15; i++) {
			Collections.shuffle(nodeIds);

			writer.edge(nodeIds.get(0), nodeIds.get(1));
		}

		writer.closeGraph();

		System.out.println(stringWriter.toString());
	}
}