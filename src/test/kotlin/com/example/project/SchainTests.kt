/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * See https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/supplychainobject.md#serialization-of-an-openrtb-supplychain-object-into-a-url-parameter
 * for the specification.
 *
 * Ignore the final requirements around URL encoded characters. Or add tests and implement for bonus points
 */
class SchainTests {

    @Test
    fun `no schain in uri - returns null`() {
        Assertions.assertNull(parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566"))
    }

    @Test
    fun `complete schain in uri - parses all values`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1,bid-request-1,publisher1,publisher.com,exto")
        Assertions.assertEquals(actual, defaultSchain)
    }


    @Test
    fun `incomplete schain in uri`() {
        val complete = 0;
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,${complete}!exchange1.com,1234,1,bid-request-1,publisher1,publisher.com,exto")
        Assertions.assertEquals(actual, defaultSchain.copy(complete = 0))
    }


    @Test
    fun `complete schain in uri - optional values omitted`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = listOf(defaultNode.copy(rid = null, name = null, domain = null, ext = null))))
    }

    @Test
    fun `complete schain in uri - optional values omitted but commas present`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1,,,")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = listOf(defaultNode.copy(rid = null, name = null, domain = null, ext = null))))
    }


    @Test
    fun `complete schain in uri - optional values omitted but commas present with whitespace`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1,    ,     ,     ,    ")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = listOf(defaultNode.copy(rid = null, name = null, domain = null, ext = null))))
    }


    @Test
    fun `complete schain in uri - multiple nodes`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual,
                                defaultSchain.copy(nodes = listOf(SchainNode("exchange1.com", "1234", 1), SchainNode("exchange2.com", "abcd", 0))))
    }


    @Test
    fun `invalid schain - version has no decimal point`() {
        val version = "10"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    @Test
    fun `invalid schain - version non numeric`() {
        val version = "a.b"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    @Test
    fun `invalid schain - version negative`() {
        val version = "-1.2"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - version null`() {
        val version = "null"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - version empty`() {
        val version = ""
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - version whitespace`() {
        val version = " \t"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=${version},1!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    @Test
    fun `invalid schain - complete negative`() {
        val complete = "-1"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,${complete}!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - complete not 0 or 1`() {
        val complete = "2"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,${complete}!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - complete empty`() {
        val complete = ""
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,${complete}!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - complete whitespace`() {
        val complete = " \t"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,${complete}!exchange1.com,1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    @Test
    fun `invalid schain - asi whitespace`() {
        val asi = " \t"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!${asi},1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - asi empty`() {
        val asi = ""
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!${asi},1234,1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - sid empty`() {
        val sid = ""
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,${sid},1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - sid whitespace`() {
        val sid = " \t"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,${sid},1!exchange2.com,abcd,0")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    @Test
    fun `invalid schain - hp negative`() {
        val hp = "-1"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,${hp}")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - hp not 0 or 1`() {
        val hp = "2"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!eexchange1.com,1234,${hp}")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - hp empty`() {
        val hp = ""
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,${hp}")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - hp whitespace`() {
        val hp = " \t"
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,${hp}")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }

    @Test
    fun `invalid schain - too many node parameters`() {
        val actual = parse("http://va.lsd.test/vah?sid=6ee64e7e-10d8-487b-8e93-89779da26566&schain=1.0,1!exchange1.com,1234,1,,,,,")
        Assertions.assertEquals(actual, defaultSchain.copy(nodes = emptyList(), complete = 0))
    }


    private fun parse(uri: String): SchainObject? = SchainParser.parse(uri)

    val defaultNode = SchainNode("exchange1.com", "1234", 1, "bid-request-1", "publisher1", "publisher.com", "exto")
    val defaultSchain = SchainObject("1.0", 1, listOf(defaultNode))

}
