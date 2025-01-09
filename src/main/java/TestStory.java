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
