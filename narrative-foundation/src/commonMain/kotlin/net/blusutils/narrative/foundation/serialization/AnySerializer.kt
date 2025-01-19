package net.blusutils.narrative.foundation.serialization

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A base serializer class for [Any] values.
 *
 * @property descriptor The [SerialDescriptor] for the [Any] value.
 */
abstract class AnySerializer : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    /**
     * Tries to decode the [Any] value from the [Decoder].
     *
     * @param decoder The [Decoder] to decode the [Any] value from.
     * @return The decoded [Any] value.
     * @throws SerializationException if the [Decoder] is not supported by this serializer.
     */
    abstract fun decode(decoder: Decoder): Any?
    /**
     * Checks whether the [Decoder] is supported by this serializer. Must be implemented by subclasses.
     * @param decoder The [Decoder] to check.
     * @return Whether the [Decoder] is supported by this serializer.
     */
    abstract fun isDecoder(decoder: Decoder): Boolean

    /**
     * Encodes the [Any] value to the [Encoder].
     * It will try to encode primitives first, then compound [List]s and [Map]s,
     * and then will check for a registered serializer for the type.
     *
     * @param encoder The [Encoder] to encode the [Any] value to.
     * @param value The [Any] value to encode.
     * @throws SerializationException if the [Encoder] is not supported by this serializer.
     */
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

    /**
     * Decodes the [Any] value from the [Decoder].
     * @param decoder The [Decoder] to decode the [Any] value from.
     * @return The decoded [Any] value.
     * @throws SerializationException if the [Decoder] is not supported by this serializer.
     */
    override fun deserialize(decoder: Decoder): Any? {
        if (!isDecoder(decoder)) {
            throw SerializationException("decoder $decoder is not supported in ${this::class.simpleName}")
        }
        return decode(decoder)
    }
}