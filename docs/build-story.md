# Building the story

In this guide, we will learn how to write a story
with `Narrative Kotlin`, and how to display it.

## Where should we start?

You may already know that **Narrative** as the format
does nothing -- it is a sort of schema
you should follow to write your own things.

Despite that, now we are using a "reference library" implementing
the **Narrative** in Kotlin, and with the help of it,
we can learn the Narrative format itself.

First of all, we need a place to tell our story
to the person who will read it.
Here you're free to choose.
No matter what it will be, a console app, a web page,
simple GUI application, or a game engine -- it is up to you.

In terms of Narrative, the place where you display
(or, more correctly, _render_ it) called **environment**.
And let's start with it.

## Setting up the environment

For the sake of simplicity, we will use a console app.
Just with `println` and `readln`, as we like.

It is recommended to use **Gradle** to build the project.
Assuming that you have the empty Kotlin application,
let's jump right to `build.gradle.kts` to add the dependencies.

> [!CAUTION]
> THE LIBRARIES ARE NOT YET PUBLISHED!

```kotlin
/* ...plugins and so on... */

repositories {
    mavenCentral()
}

dependencies {
    val narrativeVersion = "replace with the latest version"
    implementation("net.blusutils.narrative:narrative-foundation:$narrativeVersion")
    implementation("net.blusutils.narrative:narrative-extensions:$narrativeVersion")
}

/* ...rest of the file... */
```

We just added two dependencies:
* `narrative-foundation` -- the main library providing data classes and DSL builders for the Narrative format
* `narrative-extensions` -- a library providing non-standard extensions, like rich text formatting and signals (more on that later)

> Don't forget to sync the project in IDE!

## Creating the story

Now, let's take a look at your main function/class.

```kotlin
fun main() {
    println("Hello, world!")
}
```

Here it is -- a simple console app.

> [!NOTE]
> If you plan to serialize your story to JSON or
> any other format supported by `kotlinx-serialization`,
> you should add `kotlinx-serialization` dependencies to your project.
> Then, you also need to configure serializers for the Narrative objects.
> Basically, you need only these serializers:
> ```kotlin
> val narrativeSerializersModule =
>     actorSerializers { /* define actor classes here */ } +
>     signalsSerializers { /* define signal classes here */ } +
>     jumpsSerializers {
>         /* define jumps here */
>         changeLabelJumpSerializerSubclass() // recommended
>     } +
>     JsonAnySerializer.serializersModule // for JSON dynamic metadata
> val json = Json {
>     /* your json configuration here */
>     serializersModule = yourSerializersModule + narrativeSerializersModule
> }
> ```

But let's add a base of our story builder:
```kotlin
val story = buildStory {
    meta {
        name = "mystory"
        environment = StoryEnvironments.AnyEnv
    }
    resources {  }
    labels {  }
}

/* fun main() { ... } */
```

As you can see, we created a `story` variable,
in which we store the model of the story.

At the very top level, our story has three types of data:
the _metadata_, the _resources_ and the _contents_.

### Metadata

Inside **metadata** (`meta` object) we define anything that
environment will read and then use to render the story.
This includes:
* the **name** of the story (used to uniquely identify the story)
* supported **environment**
* displayed **title** and **description** (that will be displayed to the reader)
* **list of authors** made the story
* the **dynamic metadata** field, any @Serializable object or Map<String, Any> (more on that later)

None of these fields are required, but it is strictly recommended
to specify at least the name and the environment.

### Resources

**Resources** are used to store sort of "dynamic" data,
retrieved from the environment at story run time ("render time").

The common case is to store the list of actors, their looks, phrases, etc.

Typically, you can access the resources inside
the story through **references**, which will be discussed later.

> [!WARN]
> Currently, resource definition APIs are not supported in Narrative Kotlin.
> But you still can implement resource loading in your environment.
> See [Narrative schema](http://TODO) for more info.

### Contents

The **contents** of the story are the main part.
It is a tree formed by the **labels** and its **elements**.

**Labels** are the main building blocks of the story.
They organize the story into logical parts.

Each label has a **name**, **environment tags** and a **list of elements**.

**Elements** represent the actual content of the story.
**Narrative** introduces three base element types:
_phrases_, _signals_ and _jumps_.

**Phrases** are the visual part of the story.
Commonly, they are used to display **text** and anything else
by the **actors**.
In short, actors are just containers for styling phrases.

**Signals** are used to control the flow of the story.
When the environment reaches a signal element,
some actions are performed, like text input, data storing, and so on.
The key rule of signals is that they
**do not affect neither the story flow nor the story state**.

**Jumps** are used to change the story flow.
The most used case is to start another label
(so that's why jumps are originally called "jumps").

Typically, when the environment starts rendering your story,
it already jumps to the special `main` label.

## Building the story flow

The rest of story creation will be done inside the `labels` block.

Let's start with the `main` label, which is the entry point of the story.

Time to write your "Hello, world!" in Narrative.

```kotlin
labels {
    main { // an alias to label("main")
        text("Hello, world!")
        text(null, "A world...")
    }
}
```

As you can see, plain text phrases are added using the `text` function.

Take a look at the second call of this function.
We see that it takes two arguments, the last of which is the text itself.
But what is the first argument?
Why is it `null`?

Well, that's the actor reference.
When you put a `null` value here,
it means that the text is displayed
by the "narrator", e.g., with no actor.

"But ***I*** want to say this phrase!"

To do so, you need to instantiate an actor and use it.

```kotlin
val me = Actor("me", ref = null)
```

But we also can derive a new class from `Actor` to get
more control over the actor model.

```kotlin
// if you don't use kotlinx-serialization,
// you don't need to add any of these annotations
@Serializable
@SerialName("actor_custom")
data class CustomActor(
    override val name: List<StringEntity>,
    override val tags: List<String> = listOf(),
    override val ref: String? = null,
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Actor

// we're calling the toStringEntitySingleList() function
// to create a list of StringEntity objects
// used to format the actor name
val me = CustomActor("me".toStringEntitySingleList())
val you = CustomActor("you".toStringEntitySingleList())
```

> [!NOTE]
> If you use kotlinx-serialization, you need to add all the annotations above,
> as well as register the serializer for the `CustomActor` class:
> ```kotlin
> val customActorsSerializers = actorSerializers { subclass(CustomActor::class) }
> // and then add it to your serializers module (see above)
> ```

Now, let's use our actors:
```kotlin
/* ...main label... */
text(me, "Hello, world!")
text(you, "Hi there!")
```

Good! We learned how to display text.

As you can see, each actor is just a class instance.
This class implicitly implements some interfaces, and these "base"
interfaces used throughout the Narrative format.
That is: string entities, tags, dynamic metadata, and references.

### String entities

When it comes to displaying a text, you, as a story writer, may want to
apply some formatting to it.
Like coloring, bold, italic, etc.
To support this, **Narrative** introduces the concept of string entities.
Each string here is followed by a list of "entities", which specify how
this string should be displayed.

Example:
```kotlin
val entities = listOf<StringEntity>(
    StringEntity("Hello, "),
    StringEntity(
        "world!",
        mutableListOf(
            StringEntity.EntitySpec(
                StringEntity.EntitySpec.BOLD,
                "" // empty payload
            ),
            StringEntity.EntitySpec(
                StringEntity.EntitySpec.LINK,
                "https://example.com"
            )
        )
    )
)
```

Defining string entities is a bit tedious, so **Narrative** provides
a DSL for it -- `buildEntityString`.

But there is a catch: while the possible entities are known and are
the part of format, there's no guarantee that it will fit your needs.
There may be a situation when the environment doesn’t support
links, coloring, etc.

So, `buildEntityString` takes a function that returns an instance of
`EntityStringBuilder`, which is a builder for string entities.

The default `EntityStringBuilder` does not include any of these entity specs
(the reason why is explained above).

If you don't want to implement your own `EntityStringBuilder`,
you can use an overload extension function that doesn't take a function,
and gives you access to `BasicStringEntityWrapper`.

> [!NOTE]
> The `BasicStringEntityWrapper` is not a part of standard Narrative API.
> You need to declare a dependency to Narrative Extensions and then apply @OptIn annotation to use it.

```kotlin
val entities = buildEntityString {
    + put("Hello, ")
    + put("world!").bold().link("https://example.com")
}
```

Now you can pass these entities to the `text` function,
actor name, story title/description or everywhere string entities are expected.

In case if you don't want to format the text,
but want to pass a simple string where a string entities are expected,
you can use the `toStringEntitySingleList` extension function.
This returns a list of single string entity based on the given string.

```kotlin
"Hello".toStringEntitySingleList()
// same as
listOf("Hello".toStringEntity())
// and same as
listOf(StringEntity("Hello"))
```

<!-- weird, but the last one is shorter than extension... -->

### Tags

When environment processes a story element, it can use **tags**
to make some decisions and perform some actions.

As an example: environment can fire an event when the phrase
has the tag `"foo"`.

Tags are just lists of strings. Nothing special here.

### Dynamic metadata

Alongside the tags, the environment may receive some
additional data from the story.
Because we can't predict what type of data it will be,
we use term "dynamic metadata" and expect `Any?` as a type.

Typically, you use a map of `Any?` to store this data.

> [!NOTE]
> In case of `kotlinx-serialization`, the `Any?` class can’t be serialized
> to a consistent format, like a mentioned map of `Any?`.
> You need to mark the `dynamicMeta` field with `@Contextual` annotation,
> if you define your own class.
> Also, you must register a serializer for the `Any?` class.
> Standard Narrative API provides a JSON serializer for it.
> ```kotlin
> // manual way
> SerializersModule {
>   /* your serializers */
>   contextual(Any::class) {
>       JsonAnySerializer
>   }
> }
> // or use plus operator
> yourSerializersModule + JsonAnySerializer.serializersModule
> ```

### References

References are used to substitute some values in the story
when it is rendered by the environment.

Currently, references are supported only in string entities.

When the story is rendered, the environment replaces all references
with the values specified in the story.

There's a list of conventions for references:
- `@` - a reference to resource relative to project root
- `!` - a reference to resource in shared environment resources
- `#` - a reference to resource in a key-value database provided by the environment
- leading slash is interpreted as a path relative to the resources root (shipped with the story set)

## Signals

We covered the phrase part. Now let's talk about signals.

As we mentioned before, signals are used to notify the environment
about something.

Signals are defined by a `Signal` class.
You need to derive it and implement a constructor
with all parameters you need to send.

A typical signal would request a user input.
Let's write it:

```kotlin
@Serializable
@SerialName("user_input")
data class UserInputSignal(
    val name: String,
    val prompt: List<StringEntity>,
    val default: String? = null,
    override val payload: @Contextual Any? = null
) : Signal()
```

> [!NOTE]
> If you use `kotlinx-serialization`, you need to add all the annotations above,
> as well as register the serializer for the `UserInputSignal` class:
> ```kotlin
> val customSignalsSerializers = signalsSerializers { subclass(UserInputSignal::class) }
> // and then add it to your serializers module (see above)
> ```

When the environment receives a signal,
it will pause story renderer and wait for a user input.
The environment will also decide what it should do with the input.

For now, let's just add a signal to our story:

```kotlin
/* ... */
signal(
    UserInputSignal(
        "username",
        "Say your name".toStringEntitySingleList(),
        "<anonymous>"
    )
)

text { 
    + "So, hello, "
    + ref("username")
}
/* ... */
```


Later, when we write a story renderer,
we will store input from the user in the map,
and then replace the reference with the input.

## Jumps

Jumps are used to change the flow of the story.

The key difference between jumps and signals is that jumps
can change the story flow definitely, while signals
can only notify about small side effects/etc.

Jumps are defined similarly to signals -- by deriving a class from `Jump`:
```kotlin
@Serializable
@SerialName("crash")
data class CrashJump(
    val message: List<StringEntity>,
    override val payload: @Contextual Any? = null,
    override val tags: List<String> = listOf()
) : Jump()
```

> [!NOTE]
> If you use `kotlinx-serialization`, you need to add all the annotations above,
> as well as register the serializer for the `CrashJump` class:
> ```kotlin
> val customJumpsSerializers = jumpsSerializers { subclass(CrashJump::class) }
> // and then add it to your serializers module (see above)
> ```

We created a jump that will "crash" the story.
It is similar to a jump provided by the library (`StopJump`),
but in our case it will be more dramatic!

Let's add it to our story too:
```kotlin
/* ... */
jump(
    CrashJump(
        "Crashed!".toStringEntitySingleList()
    )
)
/* ... */
```

By the way, the library also provides a `ChangeLabelJump`,
which is used to change the current label.
It can be called like this:
```kotlin
jump("label_name")
```

Yes, that's it.

## Conclusion

That's all you need to know to create a story.

The next step is to create a story renderer.

See [Rendering your story](story-renderer.md) for more details.

## Full example

```kotlin
// imports are omitted

@Serializable
@SerialName("actor_custom")
data class CustomActor(
    override val name: List<StringEntity>,
    override val tags: List<String> = listOf(),
    override val ref: String? = null,
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Actor

@Serializable
@SerialName("user_input")
data class UserInputSignal(
    val name: String,
    val prompt: List<StringEntity>,
    val default: String? = null,
    override val payload: @Contextual Any? = null
) : Signal()

@Serializable
@SerialName("crash")
data class CrashJump(
    val message: List<StringEntity>,
    override val payload: @Contextual Any? = null,
    override val tags: List<String> = listOf()
) : Jump()

val me = CustomActor("me".toStringEntitySingleList())
val you = CustomActor("you".toStringEntitySingleList())

val story = buildStory {
    meta {
        name = "mystory"
        environment = StoryEnvironments.AnyEnv
    }
    resources {  }
    labels { 
        main {
            text("Hello, world!")
            text(null, "A world...")
            
            text(me, "Hello, world!")
            text(you, "Hi there!")
            signal(
                UserInputSignal(
                    "username",
                    "Say your name".toStringEntitySingleList(),
                    "<anonymous>"
                )
            )
            text {
                + "So, hello, "
                + ref("username")
            }
            
            
            jump(
                CrashJump(
                    "Crashed!".toStringEntitySingleList()
                )
            )
        }
    }
}

fun main() {
    println("Hello, world!")
}
```
