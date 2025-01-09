# Can I use `Narrative` in Java?

The answer is **yes**. But!

Just have a look to the sample code that configures a basic story and then dumps it to string:
```java
import kotlin.Unit;
import kotlinx.serialization.*;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonKt;
import kotlinx.serialization.modules.SerializersModuleBuildersKt;
import kotlinx.serialization.modules.SerializersModuleKt;
import net.blusutils.narrative.serialization.JsonAnySerializer;
import net.blusutils.narrative.story.Story;
import net.blusutils.narrative.story.StoryKt;
import net.blusutils.narrative.stringentity.BasicStringEntityWrapper;

import java.lang.reflect.Type;

public class TestStory {
    public static void main(String[] args) {
        Story story = StoryKt.buildStory((st -> {
            st.setVersion(1);
            st.meta((storyMeta -> {
                storyMeta.setName("mystory");
                return Unit.INSTANCE;
            }));
            st.labels((labelBuilder -> {
                labelBuilder.main(mainLabel -> {
                    mainLabel.text("Some text");
                    BasicStringEntityWrapper.Companion.text(
                            mainLabel,
                            null,
                            wrapper -> {
                                wrapper.unaryPlus(
                                    wrapper
                                        .put("Some text")
                                        .bold()
                                        .toStringEntity()
                                );
                                return Unit.INSTANCE;
                    });
                    return Unit.INSTANCE;
                });
                return Unit.INSTANCE;
            }));
            return Unit.INSTANCE;
        }));
        Json json = JsonKt.Json(Json.Default, cfg -> {
            cfg.setPrettyPrint(true);
            cfg.setIgnoreUnknownKeys(true);
            cfg.setEncodeDefaults(true);
            cfg.setLenient(true);
            cfg.setSerializersModule(
                SerializersModuleKt.plus(
                    SerializersModuleBuildersKt.SerializersModule(mdl -> Unit.INSTANCE),
                    JsonAnySerializer.INSTANCE.getSerializersModule()
                )
            );
            return Unit.INSTANCE;
        });
        Type type = Story.class;
        KSerializer<Object> ser = SerializersKt.serializer(type);
        String serialized = json.encodeToString(ser, story);

        System.out.println(serialized);
    }
}
```

Scared of it? Then look at the Kotlin version:
```kotlin
import kotlinx.serialization.json.Json
import net.blusutils.narrative.serialization.JsonAnySerializer
import net.blusutils.narrative.story.Story
import net.blusutils.narrative.story.buildStory

fun main() {
    val story = buildStory {
        version = 1
        meta {
            name = "mystory"
        }
        labels {
            main {
                text("Some text")
                text(null, buildStringEntity {
                    +put("Some text").bold().toStringEntity()
                })
            }
        }
    }
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        serializersModule = SerializersModule {} + JsonAnySerializer.serializersModule
    }
    val serialized = json.encodeToString(story)
    println(serialized)
}
```

In general, Java usage is possible, but it requires a lot of code to be written.
Plus, if you don't know how Kotlin produces Java-compatible code, that will be a bit challenging when you need
to use, for example, extension functions like `buildStringEntity` or `put`.

Also, obviously, Java doesn't support `kotlinx-serialization` `@Serializable` annotaion, so all things you can do
from Java are to serialize or deserialize only Kotlin `@Serializable` classes.

> or write a custom serializer for your Java class... why? really, why?
> `kotlinx-serialization` is supposed to work only in a Kotlin world, not Java.