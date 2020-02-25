package com.example.project

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

fun add(str: String): Int {
    return 0
}

// https://github.com/ardalis/kata-catalog/blob/master/katas/String%20Calculator.md
class AddTests {

    @Test
    fun `one plus one is two`() {
        Assertions.assertEquals(2, add("1,1"))
    }

}
