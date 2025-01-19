@file:Suppress("unused")
package net.blusutils.narrative.foundation.stringentity

import net.blusutils.narrative.foundation.NonStandardNarrativeApi

/**
 * Convert a string to a [StringEntity].
 * @return [StringEntity]
 */
fun String.toStringEntity() =
    StringEntity(this)

/**
 * Convert a string to a single element list of [StringEntity]
 * @return [List] of one [StringEntity]
 */
fun String.toStringEntitySingleList() =
    listOf(toStringEntity())

/**
 * Build a list of [StringEntity] from a block.
 * @param createT function that creates a [StringEntityWrapper] when needed
 * @param block block that builds the [EntityStringBuilder]
 * @return [List] of [StringEntity]
 * @see EntityStringBuilder
 * @see StringEntityWrapper
 */
fun<T: StringEntityWrapper> buildEntityString(
    createT: () -> T,
    block: EntityStringBuilder<T>.()->Unit
): List<StringEntity> {
    return EntityStringBuilder(createT)
        .apply(block)
        .getEntities()
}

/**
 * Build a list of [StringEntity] from a block with a [BasicStringEntityWrapper]
 * used as default [StringEntityWrapper] implementation.
 *
 * As this function uses [BasicStringEntityWrapper] as default implementation,
 * it is not part of the official Narrative API.
 *
 * @param block block that builds the [EntityStringBuilder]
 * @return [List] of [StringEntity]
 * @see EntityStringBuilder
 * @see BasicStringEntityWrapper
 */
@NonStandardNarrativeApi
fun buildEntityString(
    block: EntityStringBuilder<BasicStringEntityWrapper>.()->Unit
): List<StringEntity> {
    return buildEntityString({ BasicStringEntityWrapper() }, block)
}