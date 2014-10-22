package fr.ritaly.graphml4j;

import java.io.StringWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		final StringWriter stringWriter = new StringWriter();

		GraphMLWriter writer = new GraphMLWriter(stringWriter);

		// Customize the rendering
		final NodeStyle nodeStyle = writer.getNodeStyle();
		nodeStyle.setBorderType(LineType.LINE);
		nodeStyle.setBorderWidth(2.0f);
		nodeStyle.setFillColor("#CCCCCC");
		nodeStyle.setFontSize(13);
		nodeStyle.setHeight(60.0f);
		nodeStyle.setWidth(60.0f);
		nodeStyle.setShape(Shape.ROUNDED_RECTANGLE);
		nodeStyle.setTextAlignment(Alignment.CENTER);

		writer.setNodeStyle(nodeStyle);

		final EdgeStyle edgeStyle = writer.getEdgeStyle();
		edgeStyle.setColor("#000000");
		edgeStyle.setSmoothed(true);
		edgeStyle.setSourceArrow(Arrow.CIRCLE);
		edgeStyle.setTargetArrow(Arrow.DELTA);
		edgeStyle.setType(LineType.LINE);
		edgeStyle.setWidth(2.0f);

		writer.setEdgeStyle(edgeStyle);

		// Generate the graph
		writer.graph();
		writer.group("TEST");

		String prevNodeId = null;

		for (int i = 0; i < 5; i++) {
			final String nodeId = writer.node(Integer.toString(i));

			if ((i > 0) && (i < 4)) {
				writer.edge(prevNodeId, nodeId);
			}

			prevNodeId = nodeId;
		}

		writer.closeGroup();
		writer.closeGraph();

		System.out.println(stringWriter.toString());
	}
}