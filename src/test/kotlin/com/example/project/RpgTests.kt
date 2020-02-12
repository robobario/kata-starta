package com.example.project

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Character {
    fun attack(c2: Character) {
    }

    var health: Int = 100
}

// https://github.com/ardalis/kata-catalog/blob/master/katas/RPG%20Combat.md
class RpgTests {

    @Test
    fun `character attack damages another character`() {
        val c1 = Character()
        val c2 = Character()
        c1.attack(c2)
        Assertions.assertEquals(95, c2.health)
    }

}
