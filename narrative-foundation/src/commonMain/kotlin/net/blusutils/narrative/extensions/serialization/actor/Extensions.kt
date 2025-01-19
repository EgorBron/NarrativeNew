@file:Suppress("unused")
package net.blusutils.narrative.extensions.serialization.actor

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import net.blusutils.narrative.foundation.actor.Actor

/**
 * Register the serializers for subclasses of [Actor].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @throws IllegalArgumentException if the [Actor] subclass is already registered
 */
inline fun SerializersModuleBuilder.registerActors(action: PolymorphicModuleBuilder<Actor>.() -> Unit = {}) {
    polymorphic(Actor::class, builderAction = action)
}

/**
 * Create a [SerializersModule] for the serializers of subclasses of [Actor].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @return The [SerializersModule]
 */
inline fun actorSerializers(action: PolymorphicModuleBuilder<Actor>.() -> Unit = {}) =
    SerializersModule {
        polymorphic(Actor::class, builderAction = action)
    }