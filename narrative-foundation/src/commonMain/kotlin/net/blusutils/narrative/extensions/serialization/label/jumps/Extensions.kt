@file:Suppress("unused")
package net.blusutils.narrative.extensions.serialization.label.jumps

import kotlinx.serialization.modules.*
import net.blusutils.narrative.foundation.label.jumps.ChangeLabelJump
import net.blusutils.narrative.foundation.label.jumps.Jump

/**
 * Register the serializers for subclasses of [Jump].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @throws IllegalArgumentException if the [Jump] subclass is already registered
 */
inline fun SerializersModuleBuilder.registerJumps(action: PolymorphicModuleBuilder<Jump>.() -> Unit = {}) {
    polymorphic(Jump::class, builderAction = action)
}

/**
 * Create a [SerializersModule] for the serializers of subclasses of [Jump].
 * @param action The action to perform on the [PolymorphicModuleBuilder]
 * @return The [SerializersModule]
 */
inline fun jumpsSerializers(action: PolymorphicModuleBuilder<Jump>.() -> Unit = {}) =
    SerializersModule {
        polymorphic(Jump::class, builderAction = action)
    }

@Suppress("NOTHING_TO_INLINE")
inline fun PolymorphicModuleBuilder<Jump>.changeLabelJumpSerializerSubclass() {
    subclass(ChangeLabelJump::class)
}

@Suppress("NOTHING_TO_INLINE")
inline fun changeLabelJumpSerializer() =
    SerializersModule {
        registerJumps {
            changeLabelJumpSerializerSubclass()
        }
    }