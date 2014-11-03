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
package com.github.fritaly.graphml4j

import static org.junit.Assert.*

import java.awt.Color

import org.junit.Test

import com.github.fritaly.graphml4j.Utils;

public class UtilsTest {

	@Test(expected = IllegalArgumentException.class)
	public void encodeColor_Null() {
		Utils.encode(null)

		fail("Encoding null should throw an IllegalArgumentException")
	}

	@Test
	public void encodeColor_RGB() {
		assertEquals("#204080", Utils.encode(new Color(32, 64, 128)))
	}

	@Test
	public void encodeColor_RGBA() {
		assertEquals("#204080C0", Utils.encode(new Color(32, 64, 128, 192)))
	}

	@Test(expected = IllegalArgumentException.class)
	public void decodeColor_Null() {
		Utils.decode(null)

		fail("Decoding null should throw an IllegalArgumentException")
	}

	@Test
	public void decodeColor_RGB() {
		final Color color = Utils.decode("#204080")

		assertEquals(0x20, color.getRed())
		assertEquals(0x40, color.getGreen())
		assertEquals(0x80, color.getBlue())
		assertEquals(0xFF, color.getAlpha())
	}

	@Test
	public void decodeColor_RGBA() {
		final Color color = Utils.decode("#204080C0")

		assertEquals(0x20, color.getRed())
		assertEquals(0x40, color.getGreen())
		assertEquals(0x80, color.getBlue())
		assertEquals(0xC0, color.getAlpha())
	}
}