package net.blusutils.narrative.actor

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

inline fun SerializersModuleBuilder.registerActors(action: PolymorphicModuleBuilder<Actor>.() -> Unit = {}) {
    polymorphic(Actor::class, builderAction = action)
}

inline fun actorSerializers(action: PolymorphicModuleBuilder<Actor>.() -> Unit = {}) =
    SerializersModule {
        polymorphic(Actor::class, builderAction = action)
    }