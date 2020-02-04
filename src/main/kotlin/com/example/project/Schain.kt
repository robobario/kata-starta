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

data class SchainObject(val version: String, val complete: Int, val nodes: List<SchainNode>)
data class SchainNode(val asi: String,
                      val sid: String,
                      val hp: Int,
                      val rid: String? = null,
                      val name: String? = null,
                      val domain: String? = null,
                      val ext: String? = null)

object SchainParser {

    fun parse(uri: String): SchainObject? {
        return null
    }

}
