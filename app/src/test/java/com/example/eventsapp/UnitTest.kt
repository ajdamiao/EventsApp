package com.example.eventsapp

import com.example.eventsapp.util.Util
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun emailValidationTest() {
        val util = Util()
        val invalidEmail = "teste"
        val validEmail = "teste@test.com"

        assertEquals(util.isEmailValid(validEmail), true)
        assertEquals(util.isEmailValid(invalidEmail), false)
    }

    @Test
    fun nameValidationTest() {
        val util = Util()
        val invalidName = " "
        val validName = "Junior"

        assertEquals(util.isNameValid(validName), true)
        assertEquals(util.isNameValid(invalidName), false)
    }
}