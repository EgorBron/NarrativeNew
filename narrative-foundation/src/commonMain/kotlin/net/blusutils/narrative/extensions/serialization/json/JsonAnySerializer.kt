package net.blusutils.narrative.extensions.serialization.json

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.modules.SerializersModule
import net.blusutils.narrative.foundation.serialization.AnySerializer

/**
 * A [AnySerializer] implementation that can serialize and deserialize [Any] value from JSON.
 */
object JsonAnySerializer : AnySerializer() {
    /**
     * The [SerializersModule] that contains the [JsonAnySerializer].
     */
    @Suppress("unused")
    val serializersModule = SerializersModule {
        contextual(Any::class) {
            this@JsonAnySerializer
        }
    }

    override fun isDecoder(decoder: Decoder) = decoder is JsonDecoder
    override fun decode(decoder: Decoder): Any? = parseJsonElement(decoder)

    private fun parseJsonElement(decoder: Decoder, e: JsonElement? = null): Any? {
        val element = e ?: (decoder as JsonDecoder).decodeJsonElement()
        return when (element) {
            is JsonNull -> null
            is JsonPrimitive -> when {
                element.isString -> element.content
                element.booleanOrNull != null -> element.boolean
                element.intOrNull != null -> element.int
                element.longOrNull != null -> element.long
                element.floatOrNull != null -> element.float
                element.doubleOrNull != null -> element.double
                else -> throw SerializationException("Unsupported primitive: $element")
            }
            is JsonObject -> element.mapValues { (_, v) -> parseJsonElement(decoder, v) }
            is JsonArray -> element.map { parseJsonElement(decoder, it) }
//            else -> throw SerializationException("Unsupported element: $element")
        }
    }
}