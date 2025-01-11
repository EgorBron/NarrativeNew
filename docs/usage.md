Steps:
1. Add dependency to a `build.gradle` file
2. Add dependency on a `kotlinx-serialization-json` module (for the sample)
3. Configure `Json` with some `SerializersModule` tweaks:
    ```kotlin
    val json = Json {
        // these two properties are required to be set to true
        // otherwise some values will be lost
        ignoreUnknownKeys = true
        encodeDefaults = true
        // adding JsonAnySerializer is required for dynamic metadata support
        serializersModule = SerializersModule {} + JsonAnySerializer.serializersModule
    }
    ```
4. If you plan to add custom actors or signals, you will need to add their serializers modules as well:
    ```kotlin
    // ...
    serializersModule = SerializersModule {
   
    } + actorSerializers {
        subclass(SomeActor::class)
        // and so on
    } + signalSerializers {
        subclass(SomeSignal::class)
    } + JsonAnySerializer.serializersModule
    // ...
    ```
5. Build the story using `buildStory` function. See [Building stories](build-story.md) for more details.
6. Write the story renderer. See [Story renderer](story-renderer.md) for more details.