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
package com.github.fritaly.graphml4j.yed;

/**
 * Enumeration of possible shapes (for rendering nodes &amp; groups).
 *
 * @author francois_ritaly
 */
public enum Shape {
	RECTANGLE("rectangle"),
	TRIANGLE("triangle"),
	ROUNDED_RECTANGLE("roundrectangle"),
	ELLIPSE("ellipse"),
	PARALLELOGRAM("parallelogram"),
	HEXAGON("hexagon"),
	RECTANGLE_3D("rectangle3d"),
	OCTAGON("octagon"),
	DIAMOND("diamond"),
	TRAPEZOID("trapezoid"),
	TRAPEZOID_2("trapezoid2")
	;

	private final String value;

	Shape(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
