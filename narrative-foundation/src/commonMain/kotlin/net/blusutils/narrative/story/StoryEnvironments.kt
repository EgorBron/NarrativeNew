package net.blusutils.narrative.story

/**
 * The environment in which the story can be executed.
 * @see StoryMeta.environment
 */
enum class StoryEnvironments {
    /**
     * The story can be executed in any environment
     * @since Narrative "ver": 1
     */
    AnyEnv,

    /**
     * The story can be executed only in Kotlin/JVM or KotlinScript in JVM
     * @since Narrative "ver": 1
     */
    KotlinJvm,
}