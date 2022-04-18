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
 * Enumeration of possible arrows (for rendering edges).
 *
 * @author francois_ritaly
 */
public enum Arrow {
	NONE("none"),
	STANDARD("standard"),
	DELTA("delta"),
	WHITE_DELTA("white_delta"),
	DIAMOND("diamond"),
	WHITE_DIAMOND("white_diamond"),
	SHORT("short"),
	PLAIN("plain"),
	CONCAVE("concave"),
	CONVEX("convex"),
	CIRCLE("circle"),
	TRANSPARENT_CIRCLE("transparent_circle"),
	DASH("dash"),
	SKEWED_DASH("skewed_dash"),
	T_SHAPE("t_shape"),
	CROWS_FOOT_ONE_MANDATORY("crows_foot_one_mandatory"),
	CROWS_FOOT_MANY_MANDATORY("crows_foot_many_mandatory"),
	CROWS_FOOT_ONE_OPTIONAL("crows_foot_one_optional"),
	CROWS_FOOT_MANY_OPTIONAL("crows_foot_many_optional"),
	CROWS_FOOT_ONE("crows_foot_one"),
	CROWS_FOOT_MANY("crows_foot_many"),
	CROWS_FOOT_OPTIONAL("crows_foot_optional")
	;

	private final String value;

	Arrow(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
