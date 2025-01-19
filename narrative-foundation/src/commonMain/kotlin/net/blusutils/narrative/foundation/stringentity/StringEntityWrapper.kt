package net.blusutils.narrative.foundation.stringentity

/**
 * Base interface for string entity wrappers.
 *
 * The string entity wrapper is used to create
 * string entities with additional formatting.
 */
interface StringEntityWrapper {
    /**
     * Sets up the string entity wrapper for the given string.
     * @param str The string to set up the wrapper for
     * @return The current instance of the wrapper
     */
    fun beginPut(str: String): StringEntityWrapper

    /**
     * Converts the string entity wrapper to a string entity.
     * @return The string entity
     */
    fun toStringEntity(): StringEntity
}
