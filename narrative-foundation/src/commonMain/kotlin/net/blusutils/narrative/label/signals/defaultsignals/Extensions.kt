package net.blusutils.narrative.label.signals.defaultsignals

import kotlinx.serialization.modules.*
import net.blusutils.narrative.NonStandardNarrativeApi
import net.blusutils.narrative.label.signals.Signal

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

/**
 * Register the serializers of some basic [Signal] implementations.
 *
 * As these signals are not part of the base Narrative API, they are not registered by default.
 *
 * @throws IllegalArgumentException if the [Signal] subclass is already registered
 */
@NonStandardNarrativeApi
fun PolymorphicModuleBuilder<Signal>.sampleSignalsSerializers() {
    subclass(StopSignal::class)
    subclass(StoreFactSignal::class)
}

/**
 * Register the serializers of some basic [Signal] implementations.
 *
 * As these signals are not part of the base Narrative API, they are not registered by default.
 *
 * @throws IllegalArgumentException if the [Signal] subclass is already registered
 */
@NonStandardNarrativeApi
fun SerializersModuleBuilder.registerSampleSignals() {
    registerSignals {
        sampleSignalsSerializers()
    }
}

/**
 * Create a [SerializersModule] for the serializers of some basic [Signal] implementations.
 *
 * As these signals are not part of the base Narrative API, they are not registered by default.
 * @return The [SerializersModule]
 */
@NonStandardNarrativeApi
fun sampleSignalsSerializers() =
    signalsSerializers {
        sampleSignalsSerializers()
    }
