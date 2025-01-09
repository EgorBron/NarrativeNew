package net.blusutils.narrative.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
import kotlinx.serialization.serializer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.map
import kotlin.collections.mapValues
import kotlin.reflect.full.createType

abstract class AnySerializer : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    abstract open fun decode(decoder: Decoder): Any?
    abstract fun isDecoder(decoder: Decoder): Boolean

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any?) {
        when (value) {
            null -> encoder.encodeNull()
            is String -> encoder.encodeString(value)
            is Int -> encoder.encodeInt(value)
            is Long -> encoder.encodeLong(value)
            is Float -> encoder.encodeFloat(value)
            is Double -> encoder.encodeDouble(value)
            is Boolean -> encoder.encodeBoolean(value)
            is Map<*, *> -> encoder.encodeSerializableValue(
                MapSerializer(String.serializer(), this),
                value as Map<String, Any?>
            )
            is List<*> -> encoder.encodeSerializableValue(
                ListSerializer(this),
                value
            )
            else -> {
                if(value::class.annotations.any { it is Serializable }) {
                    val ser = encoder.serializersModule.serializer(value::class.createType())
                    encoder.encodeSerializableValue(ser, value)
                } else {
                    throw SerializationException("Unsupported type: ${value.javaClass}")
                }
            }
        }
    }

    override fun deserialize(decoder: Decoder): Any? {
        if (!isDecoder(decoder)) {
            throw SerializationException("decoder $decoder is not supported in ${this::class.simpleName}")
        }
        return decode(decoder)
    }

//    private fun parseScalar(scalar: YamlScalar): Any {
//        return scalar.content.toDoubleOrNull() ?: scalar.content.toBooleanStrictOrNull() ?: scalar.content
//    }
//
//    private fun parseYamlNode(node: YamlNode): Any? {
//        return when (node) {
//            is YamlNull -> null
//            is YamlScalar -> parseScalar(node)
//            is YamlMap -> node.entries.mapKeys { it.key.content }.mapValues { parseYamlNode(it.value) }
//            is YamlList -> node.items.map { parseYamlNode(it) }
//            else -> throw SerializationException("Unsupported YamlNode: $node")
//        }
//    }
//
//    private fun parseJsonElement(element: JsonElement): Any? {
//        return when (element) {
//            is JsonNull -> null
//            is JsonPrimitive -> when {
//                element.isString -> element.content
//                element.booleanOrNull != null -> element.boolean
//                element.doubleOrNull != null -> element.double
//                else -> throw SerializationException("Unsupported primitive: $element")
//            }
//            is JsonObject -> element.mapValues { (k, v) ->
//                println("jobj: $k $v")
//                parseJsonElement(v)
//            }
//            is JsonArray -> element.map {
//                println("jarr: $it")
//                parseJsonElement(it) }
//            else -> throw SerializationException("Unsupported element: $element")
//        }
//    }
}