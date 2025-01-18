package net.blusutils.narrative.serialization

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class AnySerializer : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    abstract fun decode(decoder: Decoder): Any?
    abstract fun isDecoder(decoder: Decoder): Boolean

    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
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
                val ser = value::class.serializerOrNull()
                if (ser != null) {
                    val nser = ser as KSerializer<Any>
                    //encoder.encodeSerializableValue(ser, value)
                    nser.serialize(encoder, value)
                } else {
                    throw SerializationException("Unsupported type: $value")
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