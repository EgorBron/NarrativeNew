# Narrative format compared to Ren'Py

## Similarities

* both use structured blocks of text to define story flow, and both formed by the labels, and managed by "jumps"
* both define the story flow with separate elements
* both use the concept of "speakers" (`Character` in Ren'Py and `Actor` in Narrative)
* Kotlin implementation of Narrative uses the similar syntax to the Ren'Py language
* both can run in many environments (with limitations)

## Differences

* Ren'Py is defined in a dialect of Python
  * While the Narrative is defined in JSON with strict schema
* Ren'Py allows mixing the real Python code with the story
  * but since the Narrative is just a JSON, it is not possible to directly embed code into the story; the *signals*, *jumps* and *macros* solve that problem
* Conditional statements have first-class support in Ren'Py, `if`s allow you many things starting from the branching of the story and ending with the execution of the code
  * but the Narrative just can utilize special `Jump`s to do that (which is a bit more limited and harder to understand)
* Ren'Py is bound not only to the Python environment, but more to its own engine, which is used to render the story, with all power of graphics and audio
  * and in contrast, the Narrative is not tied to any environment or language, so you can use it in any place you want, just implement the environment which is easy