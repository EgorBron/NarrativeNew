# The Narrative JSON format

### Tags and dynamic meta

Most of all entries in the Narrative JSON
have tags and dynamic meta in their fields.

The tags are used in story processing. The library that will process the story
must provide a callback-based system which will fire subscribed functions that
matched the tag filter.

The dynamic meta also will be passed to the subscriber. It is presented
as a dynamically typed object, and often used to pass additional context to
the element processor.

```kotlin
// An example

val storyProcessor = SomeStoryProcessor()

// This subscriber will match anything tagged with
// first_label inside the main label
storyProcessor.subscribe(".main", "first_label") {
    val meta = dynamicMeta
    val notOwnTags = tags.filter { it !in matchedTags }
    output(
        meta?.get("support")?.jsonPrimitive?.boolean.toString()
    )
    if (it is LabelText) {
        val entity = it.phrase.first()
        output(
            Helper.renderEntity(entity, resources)
        )
    }
}

// ...

labels {
    main {
        // This element will be passed to the subscriber above
        text(
            phrase = "Some Text".toStringEntitySingleList(),
            tags = listOf("first_label")
        )
    }
}
```

### References

The references are a convenient way to present values that resolve
at run time. Like the current date or even localized texts.
Object that implemented the `Referrable` have a string `ref` field,
which is used to replace content of such an object according to the
resource contents.

Narrative supports only string and numeric primitives, Actor and StringEntity
objects, and null reference as the placeholder to the resource.

The order in which all resources are resolved when it is not found:

    current object ref -> outer object ref (label) -> global resources -> default value

Narrative runtime can extend references functionality, allowing
to use not only resource-based data.

### Signals

Signals is the only way to define custom actions that should be run 
in the story flow. They look like `tags + dynamic meta` approach, but
signals can guarantee type safety and run only for specific processor.