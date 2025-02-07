# Narrative format extensions

ToDo

List of ~~available~~ planned extensions:

* related to the format itself:
  * `narrative-extensions-signals` -- provides useful signals that may heavily depend on the environment
    * `InputSignal` (requests input from the user)
    * `StoreSignal` (stores a value in the environment, F.K.A. the "fact database")
    * `TimerSignal` (works with timers, typically used for delays)
    * `ChoiceSignal` (presents a list of choices to the user and stores the chosen one)
    * `PlaySoundSignal` (plays a sound)
    * `ImageSignal` (displays an image)
    * `BellSignal` (plays a bell sound, the `\a` escape sequence) `not recommended extension`
    * `RandomSignal` (generates random values and stores them in the environment) `not recommended extension`
  * `narrative-extensions-jumps` -- some useful jumps
    * `ChangeLabelJump` (changes/jumps the current label)
    * `ClearJump` (clears the display)
    * `StopJump` (stops the story)
    * `ConditionalJump` (jumps to another label if a condition on the environment side is met)
    * `RestartJump` (restarts the story from the `main` label)
  * `narrative-extensions-stringentities` -- additional string entity types
    * `IMAGE` (images)
    * `THUMBNAIL` (thumbnails, smaller inline images)
    * `VIDEO` (videos)
    * `ABBREVATION` ("abbreviations", typically shows details on hover)
    * `QUOTE` (quote of someone, when two or more entities with this type go together, they are shown as a single entity)
    * `DELAYED` (entity displayed after a delay)
    * `FONT` (specifies a font to use for the text)
    * `CANVAS` (a custom renderer at the place of the text)
    * `HEADING` (a heading)
    * `MATHFORMULA` (a math formula, rendered by something like MathJax)
    * `JUMP` (acts like `LINK` that triggers a `Jump`)
    * `WIKILINK` (generates a link from string contents, just like wiki engines in their `[[syntax]]`)
    * `HTML` (displays this entity as HTML)
    * `FLOWYMACRO` (a [Flowy Staje](./flowy.md) macro from the same story file)
    * `MACRO` (a macro, a piece of code that is evaluated in the environment) `not recommended extension`
    * `JSMACRO` (a JavaScript macro, evaluated in the environment) `not recommended extension`
    * `DISPLAYIF` (this entity is displayed if the condition is met) `not recommended extension`
    * `RAWCODE` (raw code to evaluate) `not recommended extension`
    * `CUSTOMSTYLE` (payload contains a custom style, unspecified which format is used) `not recommended extension`
* related only to the Kotlin library
  * `narrative-extensions-kotlin-json` -- provides a `JsonAnySerializer` to use `Any` in dynamic metadata
  * `narrative-extensions-kotlin-basicstringentity` -- provides a `BasicStringEntityWrapper` to use standard string entities in builder