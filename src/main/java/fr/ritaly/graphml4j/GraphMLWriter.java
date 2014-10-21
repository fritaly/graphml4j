package fr.ritaly.graphml4j;

import java.io.IOException;
import java.io.Writer;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.Validate;

public class GraphMLWriter {

	private final Writer writer;

	private final XMLStreamWriter streamWriter;

	private boolean closed = false;

	public GraphMLWriter(Writer writer) throws GraphMLException {
		Validate.notNull(writer, "The given writer is null");

		try {
			this.writer = writer;
			this.streamWriter = new IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(writer));
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		} catch (FactoryConfigurationError e) {
			throw new GraphMLException(e);
		}
	}

	public void startDocument() throws GraphMLException {
		assertNotClosed();

		try {
			this.streamWriter.writeStartDocument("1.0");

			// Write the <graphml> root tag and the XML namespaces
			this.streamWriter.writeStartElement("graphml");
			this.streamWriter.writeDefaultNamespace("http://graphml.graphdrawing.org/xmlns");
			this.streamWriter.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			this.streamWriter.writeNamespace("y", "http://www.yworks.com/xml/graphml");
			this.streamWriter.writeNamespace("yed", "http://www.yworks.com/xml/yed/3");
			this.streamWriter.writeAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd");

			// Add the yEd-specific metadata
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "graphml");
			this.streamWriter.writeAttribute("id", "d0");
			this.streamWriter.writeAttribute("yfiles.type", "resources");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d1");
			this.streamWriter.writeAttribute("yfiles.type", "portgraphics");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d2");
			this.streamWriter.writeAttribute("yfiles.type", "portgeometry");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "port");
			this.streamWriter.writeAttribute("id", "d3");
			this.streamWriter.writeAttribute("yfiles.type", "portuserdata");

			// Define the attributes for type 'node'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "url");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", "d4");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", "d5");

			// Define the type 'node'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "node");
			this.streamWriter.writeAttribute("id", "d6");
			this.streamWriter.writeAttribute("yfiles.type", "nodegraphics");

			// Define the attributes for type 'graph'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "Description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "graph");
			this.streamWriter.writeAttribute("id", "d7");

			// Define the attributes for type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "url");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d8");

			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("attr.name", "description");
			this.streamWriter.writeAttribute("attr.type", "string");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d9");

			// Define the type 'edge'
			this.streamWriter.writeEmptyElement("key");
			this.streamWriter.writeAttribute("for", "edge");
			this.streamWriter.writeAttribute("id", "d10");
			this.streamWriter.writeAttribute("yfiles.type", "edgegraphics");
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	public void endDocument() throws GraphMLException {
		assertNotClosed();

		try {
			// Close the <graphml> root tag
			this.streamWriter.writeEndElement();

			this.streamWriter.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new GraphMLException(e);
		}
	}

	private void assertNotClosed() {
		if (closed) {
			throw new IllegalStateException("The writer is closed");
		}
	}

	public void close() {
		assertNotClosed();

		if (streamWriter != null) {
			try {
				streamWriter.close();
			} catch (XMLStreamException e) {
				// Close quietly
			}
		}
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				// Close quietly
			}
		}

		this.closed = true;
	}
}