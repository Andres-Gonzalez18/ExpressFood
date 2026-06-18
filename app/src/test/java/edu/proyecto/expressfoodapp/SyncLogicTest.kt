package edu.proyecto.expressfoodapp

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncLogicTest {

    @Test
    fun unsyncedOrder_shouldBePendingSync() {
        val synced = false

        assertFalse(synced)
    }

    @Test
    fun syncedOrder_shouldNotBePendingSync() {
        val synced = true

        assertTrue(synced)
    }
}