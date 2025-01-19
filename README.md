# Narrative (Kotlin)

Kotlin implementation of the Narrative format,
a format for writing interactive stories.

<!--Rework of [a previous version](https://github.com/EgorBron/Narrative).-->

Heavily inspired by [Twine](https://twinery.org/),
[Ren'Py](https://www.renpy.org/) and [Cyberscript](https://github.com/cyberscript77).

## A few words about Narrative format

Before you start using this library,
we would like to tell you a few words about the format benefits.

### It does not depend on any environment

Unlike many other storytelling formats,
Narrative does not depend on any environment.

You are taking all work on story display and control.
Feel the true freedom of telling your ideas.
On a web page, in the console, and in any more places.

### It is just a schema for JSON

Any story in Narrative is just a JSON file
that can be loaded everywhere and edited by anyone.

And it still strictly maintains the format specification,
so you can be sure that your stories will look the same
under the hood.

### It is extensible

The format itself providing a minimal set of
building blocks for your needs.
Text and actors, grouped into labels and followed by some
interactive events ("jumps" and "signals").

As the format does not depend on any environment,
you can add your own features to it.
Just describe how you handle them in your code — most times
it is as easy as adding a new class to your sources.

## Getting started

First, add the library to your project.
We recommend using [Gradle](https://gradle.org/) and will use it in all examples.

```kotlin
repositories {
    // be sure to include the mavenCentral() repository
    // if you for some reason don't have it already ಠ__ಠ
    mavenCentral()
}

dependencies {
    val version = "1.0.0" // replace it with the latest version if there is one
    implementation("net.blusutils.narrative:narrative-foundation:$version")
}
```

If you want JSON serialization support, add the following plugin and dependency:

```kotlin
plugins {
    // ...
    kotlin("plugin.serialization") version "<kotlin version>"
}

// ...

dependencies {
    // ...
    implementation("net.blusutils.narrative:narrative-json:$version")
}
```

In some cases, you may need to add the extensions too
(see [extensions](docs/extensions.md) for more details):

```kotlin
dependencies {
    // ...
    implementation("net.blusutils.narrative:narrative-extensions:$version")
}
```

> [!NOTE]
> When using extensions, you need to register serializers
> modules for classes provided by extensions.
> See [extensions](docs/extensions.md) for more details.

### Writing stories

It is very easy to write a story.

```kotlin
val story = buildStory {
    meta {
        name = "my_story"
    }
    labels {
        main {
            text("Hello, world!")
        }
    }
}
```

More details about the format can be found
in the [Building stories](docs/build-story.md) guide.

### Rendering your story

ToDo

```kotlin
var currentLabel = story.labels.find { it.id == "main" }
if (currentLabel == null) {
    for (element in label.elements) {
        when (element) {
            is LabelText -> println(element.text)
            else -> {}
        }
    }
}
```

Detailed guides can be found in the [Rendering stories](docs/story-renderer.md).

## Documentation

The documentation is available [here](https://narrative.blusutils.net/docs/).

## License

Licensed under [**Apache License 2.0**](LICENSE.txt).

```
Copyright 2025 EgorBron

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```