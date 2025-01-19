@file:Suppress("unused")
package net.blusutils.narrative.extensions.serialization.label.signals

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import net.blusutils.narrative.foundation.label.signals.Signal

/**
 * Register the serializers for subclasses of [Signal].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @throws IllegalArgumentException if the [Signal] subclass is already registered
 */
inline fun SerializersModuleBuilder.registerSignals(action: PolymorphicModuleBuilder<Signal>.() -> Unit = {}) {
    polymorphic(Signal::class, builderAction = action)
}

/**
 * Create a [SerializersModule] for the serializers of subclasses of [Signal].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @return The [SerializersModule]
 */
inline fun signalsSerializers(action: PolymorphicModuleBuilder<Signal>.() -> Unit = {}) =
    SerializersModule {
        polymorphic(Signal::class, builderAction = action)
    }

