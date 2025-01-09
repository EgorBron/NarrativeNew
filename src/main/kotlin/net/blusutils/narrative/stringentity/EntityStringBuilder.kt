package net.blusutils.narrative.stringentity

/**
 * The helper class for building list of [StringEntity].
 * @param T The type of the entity wrapper
 * @param createEntityWrapper The function that creates a new [StringEntityWrapper] of type [T]
 */
class EntityStringBuilder<T: StringEntityWrapper>(
    private val createEntityWrapper: () -> T
) {
    /** The list of [StringEntity] */
    private var elements = mutableListOf<StringEntity>()

    /**
     * Adds a string to the list of [StringEntity]
     */
    operator fun String.unaryPlus() {
        + toStringEntity()
    }

    /**
     * Adds a [StringEntity] to the list
     */
    operator fun StringEntity.unaryPlus() {
        elements.add(this)
    }

    /**
     * Converts a [T] instance to the [StringEntity]
     * and adds it to the list
     */
    operator fun StringEntityWrapper.unaryPlus() {
        + toStringEntity()
    }

    /**
     * Creates a new [T] instance based on the provided string
     * @param str The string to create the [T] instance from
     * @return The [T] instance
     * @throws ClassCastException if the [createEntityWrapper] function returns a wrong type
     */
    fun put(str: String) =
        createEntityWrapper().beginPut(str) as T

    /**
     * Returns the list of [StringEntity] put in the builder
     * @return The list of [StringEntity]
     */
    fun getEntities() =
        elements.toList()
}