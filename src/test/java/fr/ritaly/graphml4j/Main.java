package fr.ritaly.graphml4j;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.ritaly.graphml4j.base.Alignment;
import fr.ritaly.graphml4j.base.Arrow;
import fr.ritaly.graphml4j.base.FontStyle;
import fr.ritaly.graphml4j.base.LineType;
import fr.ritaly.graphml4j.base.Placement;
import fr.ritaly.graphml4j.base.Position;
import fr.ritaly.graphml4j.base.Shape;

public class Main {

	private static final boolean CUSTOMIZE_STYLES = false;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println(String.format("%s <output-file>", Main.class.getSimpleName()));
			System.exit(1);
		}

		final FileWriter fileWriter = new FileWriter(new File(args[0]));

		final GraphMLWriter writer = new GraphMLWriter(fileWriter);

		try {
			if (CUSTOMIZE_STYLES) {
				// Customize the rendering of nodes
				final NodeStyle nodeStyle = writer.getNodeStyle();
				nodeStyle.setBorderType(LineType.LINE);
				nodeStyle.setBorderWidth(2.0f);
				nodeStyle.setFillColor(Color.GRAY.brighter());
				nodeStyle.setFontSize(13);
				nodeStyle.setHeight(60.0f);
				nodeStyle.setWidth(60.0f);
				nodeStyle.setShape(Shape.ROUNDED_RECTANGLE);
				nodeStyle.setTextAlignment(Alignment.CENTER);
				nodeStyle.setShadowColor(Color.GRAY);
				nodeStyle.setShadowOffsetX(5);
				nodeStyle.setShadowOffsetY(5);
				nodeStyle.setPlacement(Placement.INTERNAL);
				nodeStyle.setPosition(Position.LEFT);
				nodeStyle.setRotationAngle(new Random().nextInt(90));

				writer.setNodeStyle(nodeStyle);

				// Customize the rendering of edges
				final EdgeStyle edgeStyle = writer.getEdgeStyle();
				edgeStyle.setColor(Color.BLACK);
				edgeStyle.setSmoothed(true);
				edgeStyle.setSourceArrow(Arrow.CIRCLE);
				edgeStyle.setTargetArrow(Arrow.DELTA);
				edgeStyle.setType(LineType.LINE);
				edgeStyle.setWidth(2.0f);

				writer.setEdgeStyle(edgeStyle);

				// Customize the rendering of groups
				final GroupStyles groupStyles = writer.getGroupStyles();
				groupStyles.setBackgroundColor(Color.GRAY.brighter());
				groupStyles.setBorderColor(Color.BLACK);
				groupStyles.setBorderType(LineType.DASHED);
				groupStyles.setBorderWidth(2.0f);
				groupStyles.setFillColor(Color.GREEN.brighter());
				groupStyles.setFontStyle(FontStyle.BOLD);

				writer.setGroupStyles(groupStyles);
			}

			// Generate a random graph
			writer.graph();

			final List<String> nodeIds = new ArrayList<String>();

			// Generate some nodes
			for (int i = 0; i < 3; i++) {
				writer.group(String.format("Group #%d", i + 1), new Random().nextBoolean());

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
		} finally {
			writer.close();
			fileWriter.close();
		}
	}
}