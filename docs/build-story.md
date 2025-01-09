# Building the story

TODO write this document

```kotlin
@Serializable
@SerialName("some_actor")
data class MyActor(
    override val name: List<StringEntity>,
    override val tags: List<String> = listOf(),
    override val ref: String? = null,
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Actor

val me = MyActor("me".toStringEntitySingleList())
val you = MyActor("you".toStringEntitySingleList())

@OptIn(NonStandardNarrativeApi::class)
val story = buildStory {
    version = 1
    meta {
        name = "mystory"
        environment = StoryEnvironments.AnyEnv

        title = "Cool story!".toStringEntitySingleList()
        authors = mutableListOf("Me", "You", "And someone else")
        description = "Some description".toStringEntitySingleList()
        
        dynamicMeta = mutableMapOf(
            "support" to true
        )
    }
    
    /* resources {
        
    } */
    
    labels {
        main {
            text("Some text")
            text(me, buildEntityString {
                +put("Some text").bold().toStringEntity()
            })
            stopSignal()
        }
    }
}
```