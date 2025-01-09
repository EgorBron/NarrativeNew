# Custom serializers for Kotlinx serialization

The Narrative library uses `kotlinx-serialization` for serialization and deserialization.

Originally, the `Narrative` format is supposed to be JSON-encoded.
But there's some derivatives for YAML and (maybe) other formats.
And `kotlinx-serialization` allows you to serialize from JSON to other formats.

Most of the library classes can be serialized with no problem, as they are `@Serializable` classes
with concrete structure defined. But the library introduces a feature called "dynamic metadata",
which allows storing a random set of key-value pairs.

And here comes a problem. `kotlinx-serialization` doesn't know how to serialize `Map<String, Any>`
cause of `Any` type. We could replace it with `JsonElement` type for JSON and `YamlNode` for YAML (kaml library),
but it disallows us to serialize to other formats.

So we need to write a custom serializer for the `Any` type.
It should be contextual to the format you're using.

For this purpose, the library provides `AnySerializer` class.

Let's see how to use it.

## Creating a custom serializer

For example, let's say we want to serialize `Any` to JSON.

The library already provides a serializer for JSON, but we will reimplement it to see how it works.

First, let's create a subclass of `AnySerializer`:
```kotlin
object JsonAnySerializer : AnySerializer() {
    override fun isDecoder(decoder: Decoder) = decoder is JsonDecoder
    override fun decode(decoder: Decoder): Any? = TODO()
}
```

`AnySerializer`, under the hood, have following `deserialize` method defined:
```kotlin
override fun deserialize(decoder: Decoder): Any? {
    if (!isDecoder(decoder)) {
        throw SerializationException(...)
    }
    return decode(decoder)
}
```

As you can see, it uses abstract `isDecoder` and `decode` that we need to implement.

`isDecoder` checks if the decoder is of the type we want to serialize to.
In our case, it's `JsonDecoder`, 'cause we want to serialize to and from JSON.

`decode` is the method that does the actual deserialization. It should return the deserialized value.

Let's implement it:
```kotlin
override fun decode(decoder: Decoder): Any? = parseJsonElement(decoder as JsonDecoder)

private fun parseJsonElement(decoder: JsonDecoder, element: JsonElement? = null): Any? {
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
        is JsonObject -> element.mapValues { (k, v) -> parseJsonElement(decoder, v) }
        is JsonArray -> element.map { parseJsonElement(decoder, it) }
        else -> throw SerializationException("Unsupported element: $element")
    }
}

```

Let's review it step by step.

First, we cast the `Decoder` interface to `JsonDecoder`.
The base class already did type check for us, so we can safely cast it.

Then we call helper method `parseJsonElement` with the decoder and optional `JsonElement` parameter.
If the `JsonElement` is not provided, we call `decodeJsonElement` on the decoder to get the first `JsonElement`.

`parseJsonElement` is a recursive function that parses the `JsonElement` and returns the deserialized value.
The types of the `JsonElement` are:
- `JsonNull` - returns `null`
- `JsonPrimitive` - returns the value of the primitive (numbers, strings, booleans)
- `JsonObject` - returns a `Map<String, *>` and recursively calls `parseJsonElement` for each value
- `JsonArray` - returns a `List<*>` and recursively calls `parseJsonElement` for each value
- any other type - throws an exception (which is not possible in case of JSON)

The serializer is ready to use. To do so, we need to register it in the `SerializersModule`:
```kotlin
val module = SerializersModule {
    contextual(Any::class) {
        JsonAnySerializer
    }
}
```

Now we can serialize the dynamic metadata to JSON.

```kotlin
@Serializable
data class SomeWithDynamicMetadata(
    @Contextual
    override val dynamicMeta: Any? = null
) : DynamicMetaContainer

val jsonString = """
{
    "dynamicMeta": {
        "key": "value",
        "key2": 123
    }
}
"""

val json = Json {
    serializersModule = module
}

json.decodeFromString<SomeWithDynamicMetadata>(jsonString)
// SomeWithDynamicMetadata(dynamicMeta={key=value, key2=123})
```