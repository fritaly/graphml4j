graphml4j
=========

[![Build Status](https://travis-ci.org/fritaly/graphml4j.svg?branch=master)](https://travis-ci.org/fritaly/graphml4j)

A simple Java API for generating [GraphML](http://graphml.graphdrawing.org/) files for [yEd](http://www.yworks.com/en/products/yfiles/yed/).

Why would I need this ?
=======================

Let's assume you want to render the dependency graph of a complex project. One typical solution consists in using [Graphviz](http://www.graphviz.org/) to do the job. This tool is very popular but has some limitations when the graphs become complex: very often, the rendered graph is difficult to read because the layout algorithms are not powerful enough.

Another alternative consists in using a graph editor like yEd which can dynamically layout graphs thanks to several very powerful algorithms (Hierarchical, Organic, Orthogonal, Circular, etc) supported out of the box.

yEd supports file formats for reading graph files: GraphML, GraphML (compressed), Graph Modeling Language, Graph Modeling Language (XML), etc.

What about reusing yEd's capabilities for laying out and rendering graphs ? This would greatly simplify our problem.

This API provides a simple way to generate GraphML files meant to be visualized in yEd.

What this API is not
====================

* This API isn't another implementation of a graph data structure. There are tons of very good open source APIs to address this need.
* This API doesn't provide layout algorithms to render graphs (instead it relies on yEd to address this need).

How do I build ?
================

To build the project, open a command prompt in the root directory and issue "./gradlew build" (on Unix / OSX) or "gradlew.bat build" (on Windows). Gradle will auto-install on-the-fly and build the project.

How do I use the API ?
======================

Check the samples which provides instructions about how to generate GraphML files and visualize them in yEd.

License
=======

This project is licensed under the ASF 2.0 open source license.

Limitations
===========

* The API only supports the generation of directed graphs
* The API only supports the generation of simple shape nodes (that is, the "Modern Nodes" from yEd aren't supported for the moment)
* The generated XML file isn't a pure GraphML file, it also contains yEd-specific tags used for the visualization