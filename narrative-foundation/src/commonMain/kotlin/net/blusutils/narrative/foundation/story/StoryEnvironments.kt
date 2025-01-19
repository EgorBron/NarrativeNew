@file:Suppress("unused")
package net.blusutils.narrative.foundation.story

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
     * The story can be executed in any console application
     * @since Narrative "ver": 1
     */
    Console,

    /**
     * The story can be executed in any GUI application
     * @since Narrative "ver": 1
     */
    Gui,

    /**
     * The story can be executed in any web page
     */
    Web,

    /**
     * The story can be executed only in Kotlin/JVM or KotlinScript in JVM
     * @since Narrative "ver": 1
     */
    KotlinJvm,
    KotlinJvmConsole, KotlinJvmGui,

    /**
     * The story can be executed only in Kotlin/JS (IR or WASM)
     * @since Narrative "ver": 1
     */
    KotlinJs,
    KotlinJsWebIR, KotlinJsNodeIR, KotlinJsWebWasm,

    /**
     * The story can be executed in any JavaScript environment
     * @since Narrative "ver": 1
     */
    AnyJs,
    JsWeb, JsNodeConsole, JsNodeGui,
}